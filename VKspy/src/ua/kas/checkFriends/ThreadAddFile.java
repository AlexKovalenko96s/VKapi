package ua.kas.checkFriends;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class ThreadAddFile implements Runnable {

	private ArrayList<String> list = new ArrayList<>();

	private String id = "";
	private String path = "";

	public ThreadAddFile(String id, String path) {
		this.id = id;
		this.path = path;
	}

	@Override
	public void run() {
		String idFriends = "";
		try {
			idFriends = getFriendsList();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

					try {
						saveFile();
					} catch (IOException e) {
						e.printStackTrace();
					}

					break;
				}
			}
		}

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

	private void saveFile() throws IOException {
		FileWriter fileWriter;
		fileWriter = new FileWriter(new File(path));
		for (int i = 0; i < list.size(); i++) {
			fileWriter.write(list.get(i) + "|");
		}
		fileWriter.close();

		JOptionPane.showMessageDialog(null, "File create.");
	}
}
