package ua.kas.analytic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class FutureGender implements Callable<String> {

	private ArrayList<String> list = new ArrayList<>();

	private int man = 0;
	private int woman = 0;
	private int deleted = 0;

	public FutureGender(ArrayList<String> list) {
		this.list = list;
	}

	@Override
	public String call() throws Exception {
		usersGet(list);
		return "man=" + man + ",woman=" + woman + ",deleted=" + deleted;
	}

	private void usersGet(ArrayList<String> list) throws IOException, InterruptedException {
		BufferedReader reader = null;
		String sex = "";
		String string = "";

		for (int i = 0; i < list.size(); i++) {
			try {
				Thread.sleep(250);
			} catch (Exception e) {
				e.printStackTrace();
			}

			String url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + list.get(i) + "&fields=sex";
			URL url2 = new URL(url);
			reader = new BufferedReader(new InputStreamReader(url2.openStream()));

			string = reader.readLine();
			sex = string.substring(string.indexOf("\"sex\":") + 6, string.indexOf("\"sex\":") + 7);

			if (sex.equals("1")) {
				woman++;
			} else if (sex.equals("2")) {
				man++;
			} else if (sex.equals("0")) {
				deleted++;
			}
		}
		reader.close();
	}

}
