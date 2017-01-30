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

public class ThreadStart implements Runnable {

	private ArrayList<String> listFriends = new ArrayList<>();
	private ArrayList<String> listId = new ArrayList<>();
	private ArrayList<Integer> listCount = new ArrayList<>();
	private static ArrayList<String> top = new ArrayList<>();

	private ArrayList<Future<String>> listThreads = new ArrayList<>();

	private Future<String> future = null;

	private URL url2;

	private BufferedReader reader = null;

	private ActionEvent actionEvent = null;

	private ExecutorService ex = null;

	private String id = "";
	private String temp = "";
	private String first_name = "";
	private String last_name = "";
	private String url = "";

	private int check = 0;

	UIController controller;

	public ThreadStart(String id, int check, ActionEvent actionEvent) {
		this.id = id;
		this.check = check;
		this.actionEvent = actionEvent;
	}

	@Override
	public void run() {

		listCount.clear();
		listFriends.clear();
		listId.clear();
		listThreads.clear();

		try {
			parseFriendsList();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// ex = Executors.newCachedThreadPool();
		// for (int i = 0; i < listFriends.size(); i++) {
		// future = ex.submit(new ThreadConstructor(listFriends.get(i), id,
		// check));
		// listThreads.add(future);
		// }
		//
		// for (int i = 0; i < listThreads.size(); i++) {
		// try {
		// System.out.println(listThreads.get(i).get());
		// } catch (InterruptedException | ExecutionException e) {
		// e.printStackTrace();
		// }
		// }

		int count = 0;
		int fulSize = listFriends.size() / 300;
		int restSize = listFriends.size() % 300;

		for (int i = 0; i <= fulSize; i++) {
			ex = Executors.newCachedThreadPool();

			count = (i == fulSize) ? restSize : 300;

			for (int j = 0; j < count; j++) {
				future = ex.submit(new ThreadConstructor(listFriends.get(j + (i * 300)), id, check));
				listThreads.add(future);
			}

			for (int j = 0; j < listThreads.size(); j++) {
				try {
					temp = listThreads.get(j).get();
					listId.add(temp.substring(0, temp.indexOf(" ")));
					listCount.add(Integer.parseInt(temp.substring(temp.indexOf(" ") + 1)));
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}

			ex.shutdown();
			listThreads.clear();
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

					System.out.println(first_name + " " + last_name);

					top.add("У " + first_name + " " + last_name + " " + listCount.get(i));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else
				return;
		}

		controller = new UIController(actionEvent);
		CheckLikesController.setTop(top);

		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getFriendsList() throws IOException {
		String url = "https://api.vk.com/method/" + "friends.get" + "?user_id=" + id + "&return_system=1";
		String line = "";
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		line = reader.readLine();
		reader.close();

		return line;
	}

	private void parseFriendsList() throws IOException {
		String idFriends = getFriendsList();

		for (int i = 0;; i++) {
			if (i == 0) {
				try {
					listFriends.add(idFriends.substring(idFriends.indexOf("[") + 1, idFriends.indexOf(",")));
					idFriends = idFriends.substring(idFriends.indexOf(",") + 1);
				} catch (Exception ex) {
					listFriends.add(idFriends.substring(idFriends.indexOf("[") + 1, idFriends.indexOf("]")));
					break;
				}
			} else {
				try {
					listFriends.add(idFriends.substring(0, idFriends.indexOf(",")));
					idFriends = idFriends.substring(idFriends.indexOf(",") + 1);
				} catch (Exception ex) {
					listFriends.add(idFriends.substring(0, idFriends.indexOf("]")));
					break;
				}
			}
		}
	}
}
