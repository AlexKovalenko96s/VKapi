package ua.kas.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import javax.swing.JOptionPane;

public class ThreadConstructor implements Callable<String> {

	private ArrayList<String> listIdWall = new ArrayList<>();

	private URL url2;

	private BufferedReader reader = null;

	private String url = "";
	private String id = "";
	private String line = "";
	private String userID = "";

	private int count = 0;
	private int check = 0;

	public ThreadConstructor(String id, String userID, int check) {
		this.id = id;
		this.userID = userID;
		this.check = check;
	}

	private void photo() throws IOException {
		// список всех фоток
		url = "https://api.vk.com/method/" + "photos.get" + "?owner_id=" + id + "&extended=1" + "&album_id=profile"
				+ "&count=1000";
		url2 = new URL(url);
		reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		line = reader.readLine();
		reader.close();

		while (line.contains("\"post_id\"")) {
			line = line.substring(line.indexOf("\"post_id\":") + 10);
			listIdWall.add(line.substring(0, (line.indexOf(","))));
		}
	}

	private void wall() throws IOException {
		// список всех постов
		url = "https://api.vk.com/method/" + "wall.get" + "?owner_id=" + id + "&filter=all" + "&count=100";
		url2 = new URL(url);
		reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		line = reader.readLine();
		reader.close();

		if (line.contains("error"))
			return;

		while (line.contains("\"id\":")) {
			line = line.substring(line.indexOf("\"id\":") + 5);
			if (!listIdWall.contains(line.substring(0, line.indexOf(",")))) {
				listIdWall.add(line.substring(0, line.indexOf(",")));
			}
		}

	}

	@Override
	public String call() throws Exception {

		listIdWall.clear();

		if (check == 0) {
			try {
				wall();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (check == 1) {
			try {
				photo();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (check == 2) {
			try {
				photo();
				wall();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < listIdWall.size(); i++) {

			if (!Thread.currentThread().isInterrupted()) {
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					break;
				}
				url = "https://api.vk.com/method/" + "likes.getList" + "?type=post" + "&owner_id=" + id + "&item_id="
						+ listIdWall.get(i);
				try {
					url2 = new URL(url);
					reader = new BufferedReader(new InputStreamReader(url2.openStream()));
					line = reader.readLine();

					if (line.contains(userID))
						count++;

					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, Thread.currentThread());
				}
			}
		}
		return id + " " + count;
	}

}
