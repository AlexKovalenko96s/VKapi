package ua.kas.vkTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

public class Main {

	private static ArrayList<String> list = new ArrayList<>();
	private static String access_token = "79222237511ddbac02022ad71746c99c1cce71dbb7cb3a725105fd5af9f1c79f039f0026fe110a21a2568";

	public static String getFriendsList() throws IOException {
		String url = "https://api.vk.com/method/" + "friends.get" + "?user_id=224429310" + "&return_system=1"
				+ "&access_token=" + access_token;
		String line = "";
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		line = reader.readLine();
		reader.close();

		return line;
	}

	public static void getUserName() throws IOException, InterruptedException {
		BufferedReader reader = null;
		for (int i = 0; i < list.size(); i++) {
			Thread.sleep(250);
			String url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + list.get(i) + "&access_token="
					+ access_token;

			URL url2 = new URL(url);
			reader = new BufferedReader(new InputStreamReader(url2.openStream()));
			System.out.println(reader.readLine());
		}
		reader.close();
	}

	public static String sendMessage() throws IOException {
		String url = "https://api.vk.com/method/" + "messages.send" + "?user_id=110602479" + "&title=приветствую"
				+ "&message=(нет)" + "&access_token=" + access_token;
		String line = "";
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		line = reader.readLine();
		reader.close();

		return line;
	}

	public static String wallPost() throws IOException {
		String url = "https://api.vk.com/method/" + "wall.post" + "?owner_id=110602479" + "&message=test"
				+ "&access_token=" + access_token;
		String line = "";
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		line = reader.readLine();
		reader.close();

		return line;
	}

	public static void main(String[] args) throws ClientProtocolException, NoSuchAlgorithmException, IOException,
			URISyntaxException, InterruptedException {

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
		// System.out.println(list);
		// getUserName();
		// System.out.println(sendMessage());
		System.out.println(wallPost());
	}
}
