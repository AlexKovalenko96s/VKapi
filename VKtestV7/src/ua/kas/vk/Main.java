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

	public static void main(String[] args) throws IOException, InterruptedException {
		// список всех фоток
		String url = "https://api.vk.com/method/" + "photos.get" + "?owner_id=224429310" + "&extended=1"
				+ "&album_id=profile" + "&count=1000";
		String line = "";
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		line = reader.readLine();
		reader.close();

		while (line.contains("\"post_id\"")) {
			line = line.substring(line.indexOf("\"post_id\":") + 10);
			System.out.println(line);
			listIdWall.add(line.substring(0, (line.indexOf(","))));
		}

		// получаем лайкнувших
		for (int i = 0; i < listIdWall.size(); i++) {
			Thread.sleep(250);
			url = "https://api.vk.com/method/" + "likes.getList" + "?type=post" + "&owner_id=224429310" + "&item_id="
					+ listIdWall.get(i);
			line = "";
			url2 = new URL(url);
			reader = new BufferedReader(new InputStreamReader(url2.openStream()));
			line = reader.readLine();
			reader.close();

			for (int j = 0;; j++) {
				if (j == 0) {
					line = line.substring(line.indexOf(",") + 1);
					try {
						if (list.contains(line.substring(line.indexOf("[") + 1, line.indexOf(",")))) {
							like.set(list.indexOf(line.substring(line.indexOf("[") + 1, line.indexOf(","))),
									like.get(list.indexOf(line.substring(line.indexOf("[") + 1, line.indexOf(","))))
											+ 1);
							line = line.substring(line.indexOf(",") + 1);
						} else {
							list.add(line.substring(line.indexOf("[") + 1, line.indexOf(",")));
							like.add(1);
							line = line.substring(line.indexOf(",") + 1);
						}
					} catch (Exception ex) {
						if (line.substring(line.indexOf("[") + 1, line.indexOf("]")).length() == 0) {
							break;
						} else if (line.substring(line.indexOf("[") + 1, line.indexOf("]")).length() != 0) {
							if (list.contains(line.substring(line.indexOf("[") + 1, line.indexOf("]")))) {
								like.set(list.indexOf(line.substring(line.indexOf("[") + 1, line.indexOf("]"))),
										like.get(list.indexOf(line.substring(line.indexOf("[") + 1, line.indexOf("]"))))
												+ 1);
							} else {
								list.add(line.substring(line.indexOf("[") + 1, line.indexOf("]")));
								like.add(1);
							}
							break;
						}
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
		reader = null;
		for (int i = 0; i < list.size(); i++) {
			Thread.sleep(250);
			url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + list.get(i);
			url2 = new URL(url);
			reader = new BufferedReader(new InputStreamReader(url2.openStream()));
			System.out.println(reader.readLine() + " поставил/ла:" + like.get(i));
		}
		reader.close();

	}

}
