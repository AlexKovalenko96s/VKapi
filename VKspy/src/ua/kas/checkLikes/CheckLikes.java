package ua.kas.checkLikes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class CheckLikes implements Runnable {

	private static ArrayList<String> list = new ArrayList<>();
	private static ArrayList<String> listIdWall = new ArrayList<>();
	private static ArrayList<Integer> like = new ArrayList<>();

	private String id = "";

	public CheckLikes(String id) {
		this.id = id;
	}

	@Override
	public void run() {
		// список всех постов
		String url = "https://api.vk.com/method/" + "wall.get" + "?owner_id=" + id + "&filter=all" + "&count=99";
		String line = "";
		URL url2;
		BufferedReader reader = null;

		try {
			url2 = new URL(url);
			reader = new BufferedReader(new InputStreamReader(url2.openStream()));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (line.contains("\"id\":")) {
			listIdWall.add(line.substring(line.indexOf("id:") + 5, line.indexOf(",")));
			line = line.substring(line.indexOf("\"id\":") + 1);
		}
		listIdWall.remove(0);

		// получаем лайкнувших
		for (int i = 0; i < listIdWall.size(); i++) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			url = "https://api.vk.com/method/" + "likes.getList" + "?type=post" + "&owner_id=" + id + "&item_id="
					+ listIdWall.get(i);
			line = "";
			try {
				url2 = new URL(url);
				reader = new BufferedReader(new InputStreamReader(url2.openStream()));
				line = reader.readLine();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

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

		// bubbleSort

		for (int i = like.size() - 1; i > 0; i--) {
			for (int j = 0; j < i; j++) {
				if (like.get(j) < like.get(j + 1)) {
					int temp_int = like.get(j);
					String temp_str = list.get(j);

					like.set(j, like.get(j + 1));
					list.set(j, list.get(j + 1));

					like.set(j + 1, temp_int);
					list.set(j + 1, temp_str);
				}
			}
		}
		System.out.println(like);

		reader = null;
		for (int i = 0; i < list.size(); i++) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + list.get(i);
			try {
				url2 = new URL(url);
				reader = new BufferedReader(new InputStreamReader(url2.openStream()));
				System.out.println(reader.readLine() + " поставил/ла:" + like.get(i));
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
