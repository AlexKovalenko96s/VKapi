package ua.kas.checkFriends;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ThreadCheckFriends implements Runnable {

	private ArrayList<String> list = new ArrayList<>();
	private ArrayList<String> lastId = new ArrayList<>();
	private ArrayList<String> addId = new ArrayList<>();

	private File file = null;

	private String id = "";
	private String del = "";
	private String add = "";

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
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("VKspy");
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("res/vk_icon.png").toString()));
			alert.setHeaderText("Выбран некорректный файл!");
			alert.setContentText(null);
			stage.show();
		}

		while (str.length() >= 1) {
			list.add(str.substring(0, str.indexOf("|")));
			str = str.substring(str.indexOf("|") + 1);
		}

		getFriendsId();
		check();
	}

	private void check() {
		for (int i = 0; i < lastId.size(); i++) {
			if (list.contains(lastId.get(i))) {
				list.remove(lastId.get(i));
			} else {
				addId.add(lastId.get(i));
			}
		}

		try {
			saveFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (list.size() != 0) {
			try {
				del = getUserName(list);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (addId.size() != 0) {
			try {
				add = getUserName(addId);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (add.length() > 0 && del.length() == 0)
			JOptionPane.showMessageDialog(null, "Added users: \n" + add);
		else if (del.length() > 0 && add.length() == 0)
			JOptionPane.showMessageDialog(null, "Deleted users: \n" + del);
		else if (add.length() > 0 && del.length() > 0)
			JOptionPane.showMessageDialog(null, "Add users: \n" + add + "Delete users: \n" + del);
		else
			JOptionPane.showMessageDialog(null, "No change!");
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

	private void saveFile() throws IOException {
		FileWriter fileWriter;
		fileWriter = new FileWriter(new File(file.getAbsolutePath()));
		for (int i = 0; i < lastId.size(); i++) {
			fileWriter.write(lastId.get(i) + "|");
		}
		fileWriter.close();
	}

	public static String getUserName(ArrayList<String> list) throws IOException, InterruptedException {
		BufferedReader reader = null;
		String line = "";
		String result = "";
		for (int i = 0; i < list.size(); i++) {
			Thread.sleep(250);
			String url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + list.get(i);

			URL url2 = new URL(url);
			reader = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));
			line = reader.readLine();
			line = line.substring(line.indexOf("\"first_name\":\"") + 14);
			String first_name = line.substring(0, line.indexOf("\""));
			line = line.substring(line.indexOf("\"last_name\":\"") + 13);
			String last_name = line.substring(0, line.indexOf("\""));

			result = result + first_name + " " + last_name + "\n";
		}
		return result;
	}
}
