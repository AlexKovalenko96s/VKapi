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

public class ThreadStart implements Runnable {

	private ArrayList<String> listFriends = new ArrayList<>();

	private ArrayList<Future<String>> listThreads = new ArrayList<>();

	private String id = "";

	private int check = 0;

	public ThreadStart(String id, int check) {
		this.id = id;
		this.check = check;
	}

	@Override
	public void run() {
		try {
			parseFriendsList();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ExecutorService ex = Executors.newCachedThreadPool();

		for (int i = 0; i < listFriends.size(); i++) {
			Future<String> s = ex.submit(new ThreadConstructor(listFriends.get(i), id, check));
			listThreads.add(s);
		}

		for (int i = 0; i < listThreads.size(); i++) {
			try {
				System.out.println(listThreads.get(i).get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		ex.shutdown();
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
