package ua.kas.vk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Main {

	private static ArrayList<String> list = new ArrayList<>();
	private static ArrayList<String> listIdWall = new ArrayList<>();
	private static ArrayList<Integer> like = new ArrayList<>();

	private static String access_token = "70b6fb754c2b39085ae447c80b6e2cea3aea304c5551e9ff7dbfe1c3ff77015e020b378f6502a5597dad2";

	public static void main(String[] args) throws IOException, InterruptedException {
		// список всех постов
		String url = "https://api.vk.com/method/" + "wall.get" + "?owner_id=34582594" + "&filter=owner"
				+ "&access_token=" + access_token;
		String line = "";
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		line = reader.readLine();
		reader.close();

		while (line.contains("\"id\":")) {
			listIdWall.add(line.substring(line.indexOf("id:") + 5, line.indexOf(",")));
			line = line.substring(line.indexOf("\"id\":") + 1);
		}
		listIdWall.remove(0);

		// получаем лайкнувших
		for (int i = 0; i < listIdWall.size(); i++) {
			Thread.sleep(250);
			url = "https://api.vk.com/method/" + "likes.getList" + "?type=post" + "&owner_id=34582594" + "&item_id="
					+ listIdWall.get(i) + "&access_token=" + access_token;
			line = "";
			url2 = new URL(url);
			reader = new BufferedReader(new InputStreamReader(url2.openStream()));
			line = reader.readLine();
			reader.close();

			for (int j = 0;; j++) {
				if (j == 0) {
					line = line.substring(line.indexOf(",") + 1);

					if (list.contains(line.substring(line.indexOf("[") + 1, line.indexOf(",")))) {
						like.set(list.indexOf(line.substring(line.indexOf("[") + 1, line.indexOf(","))),
								like.get(list.indexOf(line.substring(line.indexOf("[") + 1, line.indexOf(",")))) + 1);
						line = line.substring(line.indexOf(",") + 1);
					} else {
						list.add(line.substring(line.indexOf("[") + 1, line.indexOf(",")));
						like.add(1);
						line = line.substring(line.indexOf(",") + 1);
					}
				} else {
					try {
						if (list.contains(line.substring(0, line.indexOf(",")))) {
							like.set(list.indexOf(line.substring(0, line.indexOf(","))),
									like.get(list.indexOf(line.substring(0, line.indexOf(",")))) + 1);
							line = line.substring(line.indexOf(",") + 1);
						} else {
							list.add(line.substring(0, line.indexOf(",")));
							like.add(1);
							line = line.substring(line.indexOf(",") + 1);
						}
					} catch (Exception ex) {
						if (list.contains(line.substring(0, line.indexOf("]")))) {
							like.set(list.indexOf(line.substring(0, line.indexOf("]"))),
									like.get(list.indexOf(line.substring(0, line.indexOf("]")))) + 1);
						} else {
							list.add(line.substring(0, line.indexOf("]")));
							like.add(1);
						}
						break;
					}
				}
			}
		}

		System.out.println(list.size());

		reader = null;
		for (int i = 0; i < list.size(); i++) {
			Thread.sleep(250);
			url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + list.get(i) + "&access_token="
					+ access_token;

			url2 = new URL(url);
			reader = new BufferedReader(new InputStreamReader(url2.openStream()));
			System.out.println(reader.readLine() + " поставил/ла:" + like.get(i));
		}
		reader.close();
	}
}
