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

	private ArrayList<Thread> listThreads = new ArrayList<>();

	private String id = "";

	public ThreadStart(String id) {
		this.id = id;
	}

	@Override
	public void run() {
		try {
			parseFriendsList();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ExecutorService ex = Executors.newCachedThreadPool();

		System.out.println(listFriends);
		for (int i = 0; i < listFriends.size() / 5; i++) {
			Future<ArrayList<String>> s = ex
					.submit(new ThreadConstructor(listFriends.get(i * 5), listFriends.get(i * 5 + 1),
							listFriends.get(i * 5 + 2), listFriends.get(i * 5 + 3), listFriends.get(i * 5 + 4)));

			try {
				System.out.println(s.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		int residue = listFriends.size() % 5;
		if (residue != 0) {
			Future<ArrayList<String>> s = null;
			if (residue == 1) {
				s = ex.submit(new ThreadConstructor(listFriends.get(listFriends.size() - 1)));
			} else if (residue == 2) {
				s = ex.submit(new ThreadConstructor(listFriends.get(listFriends.size() - 1),
						listFriends.get(listFriends.size() - 2)));
			} else if (residue == 3) {
				s = ex.submit(new ThreadConstructor(listFriends.get(listFriends.size() - 1),
						listFriends.get(listFriends.size() - 2), listFriends.get(listFriends.size() - 3)));
			} else if (residue == 4) {
				s = ex.submit(new ThreadConstructor(listFriends.get(listFriends.size() - 1),
						listFriends.get(listFriends.size() - 2), listFriends.get(listFriends.size() - 3),
						listFriends.get(listFriends.size() - 4)));
			}

			try {
				System.out.println(s.get());
			} catch (InterruptedException | ExecutionException e) {

				e.printStackTrace();
			}
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
				listFriends.add(idFriends.substring(idFriends.indexOf("[") + 1, idFriends.indexOf(",")));
				idFriends = idFriends.substring(idFriends.indexOf(",") + 1);
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
