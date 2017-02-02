package ua.kas.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class Controller {

	@FXML
	CheckBox cb_wall;
	@FXML
	CheckBox cb_photo;

	@FXML
	TextField tf_first;

	private Thread thread = null;

	public boolean check(ActionEvent actionEvent) {
		String id = tf_first.getText();

		String url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + id;
		URL url2;
		String line = "";
		try {
			url2 = new URL(url);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));
			line = reader.readLine();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Отсутствует соединение с интернетом!");
			return false;
		}

		if (!line.contains("\"error\"") && line.length() > 15 && !line.contains(" ")) {
			id = line.substring(line.indexOf("\"uid\":") + 6, line.indexOf(",\"first_name\""));
			if (!id.equals("!224429310") && !id.equals("202930417")) {
				if (cb_photo.isSelected() || cb_wall.isSelected()) {
					String userName = getName(line);
					if (cb_wall.isSelected() && !cb_photo.isSelected()) {
						thread = new Thread(new CheckLikes(id, 0, actionEvent, userName));
					} else if (cb_photo.isSelected() && !cb_wall.isSelected()) {
						thread = new Thread(new CheckLikes(id, 1, actionEvent, userName));
					} else if (cb_photo.isSelected() && cb_wall.isSelected()) {
						thread = new Thread(new CheckLikes(id, 2, actionEvent, userName));
					}
					thread.start();

					JOptionPane.showMessageDialog(null, "Программа запущена.\nПожалуйста, подождите некоторое время.");

					// threadUI.join();
					return true;
					// btn_first.setDisable(true);
					// for (;;) {
					// if (thread.isInterrupted()) {
					// System.out.println("dd");
					// btn_first.setDisable(false);
					// break;
					// }
					// }
				} else {
					JOptionPane.showMessageDialog(null, "Пожалуйста, выберите категорию!");
					return false;
				}
			} else {
				JOptionPane.showMessageDialog(null, "LOCKED!!!");
				return false;
			}
		} else {
			JOptionPane.showMessageDialog(null, "Указанный id являеться некорректным!");
			return false;
		}
	}

	private String getName(String line) {
		String first_name = "";
		String last_name = "";

		line = line.substring(line.indexOf("\"first_name\":\"") + 14);
		first_name = line.substring(0, line.indexOf("\""));
		line = line.substring(line.indexOf("\"last_name\":\"") + 13);
		last_name = line.substring(0, line.indexOf("\""));

		return first_name + " " + last_name;
	}
}
