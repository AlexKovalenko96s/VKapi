package ua.kas.checkLikes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javafx.event.ActionEvent;

public class CheckLikes implements Runnable {

	private static ArrayList<String> list = new ArrayList<>();
	private static ArrayList<String> listIdWall = new ArrayList<>();
	private static ArrayList<Integer> like = new ArrayList<>();
	private static ArrayList<String> top = new ArrayList<>();
	private static ArrayList<String> topForFile = new ArrayList<>();

	private URL url2;

	private BufferedReader reader = null;

	private ActionEvent actionEvent = null;

	private String first_name = "";
	private String last_name = "";
	private String url = "";
	private String id = "";
	private String line = "";
	private String userName = "";
	private String path = "";

	private int check = 0;

	UIController controller;

	public CheckLikes(String id, Integer check, ActionEvent actionEvent, String userName, String path) {
		this.id = id;
		this.check = check;
		this.actionEvent = actionEvent;
		this.userName = userName;
		this.path = path;
	}

	private void photo() throws IOException {
		// список всех фоток
		url = "https://api.vk.com/method/" + "photos.get" + "?owner_id=" + id + "&extended=1" + "&album_id=profile"
				+ "&count=1000";
		url2 = new URL(url);
		reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		line = reader.readLine();
		reader.close();

		while (line.contains("\"post_id\"")) {
			line = line.substring(line.indexOf("\"post_id\":") + 10);
			listIdWall.add(line.substring(0, (line.indexOf(","))));
		}
	}

	private void wall() throws IOException {
		// список всех постов
		url = "https://api.vk.com/method/" + "wall.get" + "?owner_id=" + id + "&filter=all" + "&count=100";
		url2 = new URL(url);
		reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		line = reader.readLine();
		reader.close();

		while (line.contains("\"id\":")) {
			line = line.substring(line.indexOf("\"id\":") + 5);
			if (!listIdWall.contains(line.substring(0, line.indexOf(",")))) {
				listIdWall.add(line.substring(0, line.indexOf(",")));
			}
		}
	}

	@Override
	public void run() {

		like.clear();
		listIdWall.clear();
		list.clear();
		top.clear();
		topForFile.clear();

		if (check == 0) {
			try {
				wall();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (check == 1) {
			try {
				photo();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (check == 2) {
			try {
				photo();
				wall();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// получаем лайкнувших
		for (int i = 0; i < listIdWall.size(); i++) {
			if (!Thread.currentThread().isInterrupted()) {
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					break;
				}
				url = "https://api.vk.com/method/" + "likes.getList" + "?type=post" + "&owner_id=" + id + "&item_id="
						+ listIdWall.get(i);
				line = "";
				try {
					url2 = new URL(url);
					reader = new BufferedReader(new InputStreamReader(url2.openStream()));
					line = reader.readLine();
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				for (int j = 0;; j++) {
					if (j == 0) {
						line = line.substring(line.indexOf(",") + 1);
						try {
							if (list.contains(line.substring(line.indexOf("[") + 1, line.indexOf(",")))) {
								like.set(list.indexOf(line.substring(line.indexOf("[") + 1, line.indexOf(","))),
										like.get(list.indexOf(line.substring(line.indexOf("[") + 1, line.indexOf(","))))
												+ 1);
								line = line.substring(line.indexOf(",") + 1);
							} else {
								list.add(line.substring(line.indexOf("[") + 1, line.indexOf(",")));
								like.add(1);
								line = line.substring(line.indexOf(",") + 1);
							}
						} catch (Exception ex) {
							if (line.substring(line.indexOf("[") + 1, line.indexOf("]")).length() == 0) {
								break;
							} else if (line.substring(line.indexOf("[") + 1, line.indexOf("]")).length() != 0) {
								if (list.contains(line.substring(line.indexOf("[") + 1, line.indexOf("]")))) {
									like.set(list.indexOf(line.substring(line.indexOf("[") + 1, line.indexOf("]"))),
											like.get(list
													.indexOf(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))
													+ 1);
								} else {
									list.add(line.substring(line.indexOf("[") + 1, line.indexOf("]")));
									like.add(1);
								}
								break;
							}
						}
					} else {
						try {
							if (list.contains(line.substring(0, line.indexOf(",")))) {
								like.set(list.indexOf(line.substring(0, line.indexOf(","))),
										like.get(list.indexOf(line.substring(0, line.indexOf(",")))) + 1);
								line = line.substring(line.indexOf(",") + 1);
							} else {
								list.add(line.substring(0, line.indexOf(",")));
								like.add(1);
								line = line.substring(line.indexOf(",") + 1);
							}
						} catch (Exception ex) {
							if (list.contains(line.substring(0, line.indexOf("]")))) {
								like.set(list.indexOf(line.substring(0, line.indexOf("]"))),
										like.get(list.indexOf(line.substring(0, line.indexOf("]")))) + 1);
							} else {
								list.add(line.substring(0, line.indexOf("]")));
								like.add(1);
							}
							break;
						}
					}
				}
			} else
				return;
		}

		// bubbleSort

		for (int i = like.size() - 1; i > 0; i--) {
			if (!Thread.currentThread().isInterrupted()) {
				for (int j = 0; j < i; j++) {
					if (like.get(j) < like.get(j + 1)) {
						int temp_int = like.get(j);
						String temp_str = list.get(j);

						like.set(j, like.get(j + 1));
						list.set(j, list.get(j + 1));

						like.set(j + 1, temp_int);
						list.set(j + 1, temp_str);
					}
				}
			} else
				return;
		}

		int size = 10;
		size = (list.size() <= 10) ? list.size() : 10;

		if (path.length() != 0) {
			Thread thread = new Thread(new ThreadSaveFile(list, like, path));
			thread.start();
		}

		getName(size, list);

		controller = new UIController(actionEvent);
		CheckLikesController.setUserName(userName);
		CheckLikesController.setTop(top);

		// CheckLikesController c = new CheckLikesController();
		// c.start(top);

		// FOR CONSOLE ---->

		// reader = null;
		// for (int i = 0; i < list.size(); i++) {
		// if (!Thread.currentThread().isInterrupted()) {
		// try {
		// Thread.sleep(250);
		// } catch (InterruptedException e) {
		// return;
		// }
		// url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" +
		// list.get(i);
		// try {
		// url2 = new URL(url);
		// reader = new BufferedReader(new
		// InputStreamReader(url2.openStream()));
		// System.out.println(reader.readLine() + " поставил/ла:" +
		// like.get(i));
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// } else
		// break;
		// }

		// <---- END CONSOLE

		try

		{
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getName(Integer size, ArrayList<String> listForAdding) {
		for (int i = 0; i < size; i++) {
			if (!Thread.currentThread().isInterrupted()) {
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					return;
				}
				url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + listForAdding.get(i);
				try {
					url2 = new URL(url);
					reader = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));

					String line = reader.readLine();
					line = line.substring(line.indexOf("\"first_name\":\"") + 14);
					first_name = line.substring(0, line.indexOf("\""));
					line = line.substring(line.indexOf("\"last_name\":\"") + 13);
					last_name = line.substring(0, line.indexOf("\""));

					top.add(first_name + " " + last_name + " поставил/ла: " + like.get(i));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else
				return;
		}
	}
}
