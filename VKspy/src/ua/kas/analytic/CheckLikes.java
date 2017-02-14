package ua.kas.analytic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javafx.event.ActionEvent;

public class CheckLikes implements Runnable {

	private Future<String> futureGender = null;
	private Future<String> futureFriend = null;

	private ExecutorService ex = null;

	private ActionEvent actionEvent;

	private static ArrayList<String> listIdWall = new ArrayList<>();
	private static ArrayList<String> list = new ArrayList<>();

	private URL url2;

	private BufferedReader reader = null;

	private String url = "";
	private String line = "";
	private String id = "";
	private String name = "";
	private String check = "";

	private Integer checkLikeOne = 0;

	UIController controller;

	public CheckLikes(String id, String check, ActionEvent actionEvent, String name, Integer checkLikeOne) {
		this.id = id;
		this.check = check;
		this.actionEvent = actionEvent;
		this.name = name;
		this.checkLikeOne = checkLikeOne;
	}

	@Override
	public void run() {
		listIdWall.clear();
		list.clear();

		if (check.contains("0")) {
			try {
				wall();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (check.contains("2")) {
			try {
				ava();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (check.contains("1")) {
			try {
				photo();
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

				if (checkLikeOne == 1) {
					for (int j = 0;; j++) {
						if (j == 0) {
							line = line.substring(line.indexOf(",") + 1);
							try {
								if (!list.contains(line.substring(line.indexOf("[") + 1, line.indexOf(",")))) {
									list.add(line.substring(line.indexOf("[") + 1, line.indexOf(",")));
								}
								line = line.substring(line.indexOf(",") + 1);
							} catch (Exception ex) {
								if (line.substring(line.indexOf("[") + 1, line.indexOf("]")).length() == 0) {
									break;
								} else if (line.substring(line.indexOf("[") + 1, line.indexOf("]")).length() != 0) {
									if (!list.contains(line.substring(line.indexOf("[") + 1, line.indexOf("]")))) {
										list.add(line.substring(line.indexOf("[") + 1, line.indexOf("]")));
									}
									break;
								}
							}
						} else {
							try {
								if (!list.contains(line.substring(0, line.indexOf(",")))) {
									list.add(line.substring(0, line.indexOf(",")));
								}
								line = line.substring(line.indexOf(",") + 1);
							} catch (Exception ex) {
								if (!list.contains(line.substring(0, line.indexOf("]")))) {
									list.add(line.substring(0, line.indexOf("]")));
								}
								break;
							}
						}
					}
				} else if (checkLikeOne == 2) {
					for (int j = 0;; j++) {
						if (j == 0) {
							line = line.substring(line.indexOf(",") + 1);
							try {
								list.add(line.substring(line.indexOf("[") + 1, line.indexOf(",")));
								line = line.substring(line.indexOf(",") + 1);
							} catch (Exception ex) {
								if (line.substring(line.indexOf("[") + 1, line.indexOf("]")).length() == 0) {
									break;
								} else if (line.substring(line.indexOf("[") + 1, line.indexOf("]")).length() != 0) {
									list.add(line.substring(line.indexOf("[") + 1, line.indexOf("]")));
									break;
								}
							}
						} else {
							try {
								list.add(line.substring(0, line.indexOf(",")));
								line = line.substring(line.indexOf(",") + 1);
							} catch (Exception ex) {
								list.add(line.substring(0, line.indexOf("]")));
								break;
							}
						}
					}
				}
			} else
				return;
		}

		ex = Executors.newCachedThreadPool();

		// 224429310
		futureGender = ex.submit(new FutureGender(list));
		futureFriend = ex.submit(new FutureFriend(list, id));

		try {
			ChartController.setManWoman(futureGender.get());
			ChartController.setFriendsOrNo(futureFriend.get());
			ChartController.setName(name);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		controller = new UIController(actionEvent);

		ex.shutdown();

	}

	private void ava() throws IOException {
		// список всех ав
		url = "https://api.vk.com/method/" + "photos.get" + "?owner_id=" + id + "&extended=1" + "&album_id=profile"
				+ "&count=1000";
		url2 = new URL(url);
		reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		line = reader.readLine();
		reader.close();
		while (line.contains("\"post_id\"")) {
			line = line.substring(line.indexOf("\"post_id\":") + 10);
			if (!listIdWall.contains(line.substring(0, line.indexOf(","))))
				listIdWall.add(line.substring(0, line.indexOf(",")));
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
			if (!listIdWall.contains(line.substring(0, line.indexOf(","))))
				listIdWall.add(line.substring(0, line.indexOf(",")));
		}
	}

	private void photo() throws IOException {
		// список всех фоток
		url = "https://api.vk.com/method/" + "photos.get" + "?owner_id=" + id + "&extended=1" + "&album_id=wall"
				+ "&count=1000";
		url2 = new URL(url);
		reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		line = reader.readLine();
		reader.close();

		while (line.contains("\"post_id\"")) {
			line = line.substring(line.indexOf("\"post_id\":") + 10);
			if (!listIdWall.contains(line.substring(0, line.indexOf(","))))
				listIdWall.add(line.substring(0, line.indexOf(",")));
		}
	}

}
