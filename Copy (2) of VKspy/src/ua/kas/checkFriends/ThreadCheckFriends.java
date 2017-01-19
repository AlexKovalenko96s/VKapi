package ua.kas.checkFriends;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class ThreadCheckFriends implements Runnable {

	private ArrayList<String> list = new ArrayList<>();
	private ArrayList<String> lastId = new ArrayList<>();

	private File file = null;

	private String id = "";

	public ThreadCheckFriends(String id, File file) {
		this.id = id;
		this.file = file;
	}

	@Override
	public void run() {
		BufferedReader buf_reader;
		String str = "";
		try {
			buf_reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "windows-1251"));
			str = buf_reader.readLine();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Not correct file!");
		}

		while (str.length() >= 1) {
			list.add(str.substring(0, str.indexOf("|")));
			str = str.substring(str.indexOf("|") + 1);
		}

		getFriendsId();
	}

	private void getFriendsId() {
		String idFriends = "";
		try {
			idFriends = getFriendsList();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0;; i++) {
			if (i == 0) {
				lastId.add(idFriends.substring(idFriends.indexOf("[") + 1, idFriends.indexOf(",")));
				idFriends = idFriends.substring(idFriends.indexOf(",") + 1);
			} else {
				try {
					lastId.add(idFriends.substring(0, idFriends.indexOf(",")));
					idFriends = idFriends.substring(idFriends.indexOf(",") + 1);
				} catch (Exception ex) {
					lastId.add(idFriends.substring(0, idFriends.indexOf("]")));
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
}
