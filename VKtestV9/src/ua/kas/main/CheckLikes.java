package ua.kas.main;

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

	private static ArrayList<String> list = new ArrayList<>();
	private static ArrayList<String> listIdWall = new ArrayList<>();
	private static ArrayList<String> top = new ArrayList<>();
	private ArrayList<String> listId = new ArrayList<>();
	private ArrayList<Integer> listCount = new ArrayList<>();

	private ArrayList<Future<String>> listThreads = new ArrayList<>();

	private Future<String> future = null;

	private ExecutorService ex = null;

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
	private String temp = "";

	private int check = 0;

	UIController controller;

	public CheckLikes(String id, Integer check, ActionEvent actionEvent, String userName) {
		this.id = id;
		this.check = check;
		this.actionEvent = actionEvent;
		this.userName = userName;
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

		listIdWall.clear();
		list.clear();
		top.clear();
		listCount.clear();
		listId.clear();
		listThreads.clear();

		try {
			photo();
			wall();
		} catch (IOException e1) {
			e1.printStackTrace();
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
			} else
				return;
		}

		// получив список все кто лайкал жертву "list". начинаем подсчет лайков
		// у них от жертвы

		int count = 0;
		int fulSize = list.size() / 100;
		int restSize = list.size() % 100;

		for (int i = 0; i <= fulSize; i++) {
			if (!Thread.currentThread().isInterrupted()) {
				ex = Executors.newCachedThreadPool();

				count = (i == fulSize) ? restSize : 100;

				for (int j = 0; j < count; j++) {
					future = ex.submit(new ThreadConstructor(list.get(j + (i * 100)), id, check));
					listThreads.add(future);
				}

				for (int j = 0; j < listThreads.size(); j++) {
					if (!Thread.currentThread().isInterrupted()) {
						try {
							temp = listThreads.get(j).get();
							listId.add(temp.substring(0, temp.indexOf(" ")));
							listCount.add(Integer.parseInt(temp.substring(temp.indexOf(" ") + 1)));
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
					} else {
						return;
					}
				}

				ex.shutdown();
				listThreads.clear();
			} else
				return;
		}

		// bubbleSort

		for (int i = listCount.size() - 1; i > 0; i--) {
			if (!Thread.currentThread().isInterrupted()) {
				for (int j = 0; j < i; j++) {
					if (listCount.get(j) < listCount.get(j + 1)) {
						int temp_int = listCount.get(j);
						String temp_str = listId.get(j);

						listCount.set(j, listCount.get(j + 1));
						listId.set(j, listId.get(j + 1));

						listCount.set(j + 1, temp_int);
						listId.set(j + 1, temp_str);
					}
				}
			} else
				return;
		}

		int size = 10;
		size = (listId.size() <= 10) ? listId.size() : 10;

		// if (path.length() != 0) {
		Thread thread = new Thread(new ThreadSaveFile(listId, listCount, path));
		thread.start();
		// }

		for (int i = 0; i < size; i++) {
			if (!Thread.currentThread().isInterrupted()) {
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					return;
				}
				url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + listId.get(i);
				try {
					url2 = new URL(url);
					reader = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));

					String line = reader.readLine();
					line = line.substring(line.indexOf("\"first_name\":\"") + 14);
					first_name = line.substring(0, line.indexOf("\""));
					line = line.substring(line.indexOf("\"last_name\":\"") + 13);
					last_name = line.substring(0, line.indexOf("\""));

					top.add("У " + first_name + " " + last_name + " - " + listCount.get(i));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else
				return;
		}

		controller = new UIController(actionEvent);
		CheckLikesController.setUserName(userName);
		CheckLikesController.setTop(top);

		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
