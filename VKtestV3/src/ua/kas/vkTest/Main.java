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
	private static String access_token = "ed3ffdef7c885ef6863a50259f8fdfc93f6920bc5e3fc2bb548dceb5036637e586455d3dd7d24458e1871";

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
		String space = "%20";

		// %0A
		// String enter = ""
		String url = "https://api.vk.com/method/" + "messages.send" + "?user_id=110602479" + "&title=title"
				+ "&message=віпвіі%20ds%0Anпа" + "&access_token=" + access_token;
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
		System.out.println(sendMessage());
		// System.out.println(wallPost());
	}
}
