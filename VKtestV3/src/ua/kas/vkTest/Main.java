package ua.kas.vkTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

public class Main {

	private static ArrayList<String> list = new ArrayList<>();
	private static ArrayList<String> listIdWall = new ArrayList<>();
	private static Map countLike = new HashMap<String, Integer>();
	private static ArrayList<String> photoId = new ArrayList<>();

	// http://oauth.vk.com/authorize?client_id=5809983&scope=messages,wall,friends,likes,users&redirect_uri=http://oauth.vk.com/blank.html&display=popup&response_type=token
	private static String access_token = "70b6fb754c2b39085ae447c80b6e2cea3aea304c5551e9ff7dbfe1c3ff77015e020b378f6502a5597dad2";

	public static String getFriendsList() throws IOException {
		String url = "https://api.vk.com/method/" + "friends.get" + "?user_id=224429310" + "&return_system=1";
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
			String url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + list.get(i) + "&fields=online";

			URL url2 = new URL(url);
			reader = new BufferedReader(new InputStreamReader(url2.openStream()));
			System.out.println(reader.readLine());
		}
		reader.close();
	}

	public static String sendMessage() throws IOException {
		// String space = "%20";
		// %0A

		String url = "https://api.vk.com/method/" + "messages.send" + "?user_id=202930417" + "&title=title"
				+ "&message=привет" + "&access_token=" + access_token;
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

	public static void getPhoto11() throws IOException, InterruptedException {
		BufferedReader reader = null;
		for (int i = 0; i < list.size(); i++) {
			Thread.sleep(250);
			String url = "https://api.vk.com/method/" + "photos.get" + "?user_ids=" + list.get(i);

			URL url2 = new URL(url);
			reader = new BufferedReader(new InputStreamReader(url2.openStream()));
			System.out.println(reader.readLine());
		}
		reader.close();
	}

	public static void likes() throws IOException {
		String url = "https://api.vk.com/method/" + "likes.getList" + "?type=post" + "&owner_id=34582594"
				+ "&item_id=3966" + "&access_token=" + access_token;
		String line = "";
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		line = reader.readLine();
		reader.close();

		System.out.println(line);
		for (int i = 0;; i++) {
			if (i == 0) {
				line = line.substring(line.indexOf(",") + 1);
				list.add(line.substring(line.indexOf("[") + 1, line.indexOf(",")));
				line = line.substring(line.indexOf(",") + 1);
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
		System.out.println(list);
	}

	public static void wall() throws IOException {
		String url = "https://api.vk.com/method/" + "wall.get" + "?owner_id=34582594" + "&filter=owner" + "&count=100"
				+ "&access_token=" + access_token;
		String line = "";
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		line = reader.readLine();
		reader.close();
		System.out.println(line);

		for (int i = 0; 100 > i; i++) {
			listIdWall.add(line.substring(line.indexOf("id:") + 5, line.indexOf(",")));
			line = line.substring(line.indexOf("\"id\":") + 1);
		}
		listIdWall.remove(0);
	}

	public static void countWallLikes() throws InterruptedException {
		for (int i = 0; i < listIdWall.size(); i++) {
			Thread.sleep(250);
		}
	}

	public static String getPhoto() throws IOException {
		String url = "https://api.vk.com/method/" + "photos.get" + "?owner_id=224429310" + "&extended=1"
				+ "&album_id=profile" + "&count=1000";
		String line = "";
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		line = reader.readLine();
		reader.close();
		return line;
	}

	public static void getPhotoId() throws IOException {
		String line = getPhoto();
		while (line.contains("\"post_id\"")) {
			line = line.substring(line.indexOf("\"post_id\":") + 10);
			System.out.println(line);
			photoId.add(line.substring(0, (line.indexOf(","))));
		}
	}

	public static void main(String[] args) throws ClientProtocolException, NoSuchAlgorithmException, IOException,
			URISyntaxException, InterruptedException {

		// String idFriends = getFriendsList();
		// String idFriends = likes();

		// System.out.println(getFriendsList());

		// for (int i = 0;; i++) {
		// if (i == 0) {
		// list.add(idFriends.substring(idFriends.indexOf("[") + 1,
		// idFriends.indexOf(",")));
		// idFriends = idFriends.substring(idFriends.indexOf(",") + 1);
		// } else {
		// try {
		// list.add(idFriends.substring(0, idFriends.indexOf(",")));
		// idFriends = idFriends.substring(idFriends.indexOf(",") + 1);
		// } catch (Exception ex) {
		// list.add(idFriends.substring(0, idFriends.indexOf("]")));
		// break;
		// }
		// }
		// }
		// System.out.println("dd");
		// getUserName();
		// getPhoto();

		//
		// System.out.println(sendMessage());
		// System.out.println(wallPost());

		// likes();
		// getUserName();

		// wall();

		// getPhotoId();
	}
}
