package ua.kas.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Main {

	public static String getGroups() throws IOException {
		String url = "https://api.vk.com/method/" + "gifts.get" + "?user_id=224429310";
		String line = "";
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		line = reader.readLine();
		reader.close();

		return line;
	}

	private static String getFriendsList() throws IOException {
		String url = "https://api.vk.com/method/" + "friends.get" + "?user_id=224429310" + "&return_system=1";
		String line = "";
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		line = reader.readLine();
		reader.close();

		return line;
	}

	private static ArrayList<String> stringToList() throws IOException {
		ArrayList<String> list = new ArrayList<>();
		String idFriends = getFriendsList();

		for (int i = 0;; i++) {
			if (i == 0) {
				list.add(idFriends.substring(idFriends.indexOf("[") + 1, idFriends.indexOf(",")));
				idFriends = idFriends.substring(idFriends.indexOf(",") + 1);
			} else {
				try {
					list.add(idFriends.substring(0, idFriends.indexOf(",")));
					idFriends = idFriends.substring(idFriends.indexOf(",") + 1);
				} catch (Exception ex) {
					list.add(idFriends.substring(0, idFriends.indexOf("]")));
					break;
				}
			}
		}
		return list;
	}

	private static void usersGet(ArrayList<String> list) throws IOException, InterruptedException {
		BufferedReader reader = null;
		for (int i = 0; i < list.size(); i++) {

			try {
				Thread.sleep(250);
			} catch (Exception e) {
				e.printStackTrace();
			}

			String url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + list.get(i) + "&fields=sex,bdate";

			URL url2 = new URL(url);
			reader = new BufferedReader(new InputStreamReader(url2.openStream()));
			System.out.println(reader.readLine());
		}
		reader.close();
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		// usersGet(stringToList());
		System.out.println(getGroups());
	}
}
