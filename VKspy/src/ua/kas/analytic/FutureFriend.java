package ua.kas.analytic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class FutureFriend implements Callable<String> {

	private ArrayList<String> list = new ArrayList<>();
	private ArrayList<String> friendsList = new ArrayList<>();
	private ArrayList<String> all = new ArrayList<>();

	private String id = "";

	public FutureFriend(ArrayList<String> list, String id) {
		this.list = list;
		this.id = id;
	}

	@Override
	public String call() throws Exception {
		getFriendsId();

		all.addAll(list);

		int sum = all.size();

		for (int i = 0; i < friendsList.size(); i++) {
			for (;;) {
				if (all.contains(friendsList.get(i))) {
					all.remove(friendsList.get(i));
				} else {
					break;
				}
			}

		}

		return "sum" + sum + ",friends=" + (sum - all.size()) + ",noFriends=" + all.size();
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

	private void getFriendsId() throws IOException {
		String idFriends = getFriendsList();

		for (int i = 0;; i++) {
			if (i == 0) {
				friendsList.add(idFriends.substring(idFriends.indexOf("[") + 1, idFriends.indexOf(",")));
				idFriends = idFriends.substring(idFriends.indexOf(",") + 1);
			} else {
				try {
					friendsList.add(idFriends.substring(0, idFriends.indexOf(",")));
					idFriends = idFriends.substring(idFriends.indexOf(",") + 1);
				} catch (Exception ex) {
					friendsList.add(idFriends.substring(0, idFriends.indexOf("]")));
					break;
				}
			}
		}
	}

}
