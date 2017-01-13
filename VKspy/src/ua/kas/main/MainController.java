package ua.kas.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import ua.kas.checkLikes.CheckLikes;
import ua.kas.spyOnline.SpyOnline;

public class MainController {
	// 224429310
	// 18472340
	@FXML
	CheckBox cb_wall;
	@FXML
	CheckBox cb_photo;

	@FXML
	TextField tf_first;
	@FXML
	TextField tf_second;

	@FXML
	Button btn_first;
	@FXML
	Button btn_second;

	public void checkLikes() throws IOException {
		String id = tf_first.getText();

		String url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + id;
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		String line = reader.readLine();

		if (!line.contains("\"error\"")) {
			Thread thread = new Thread(new CheckLikes(id));
			thread.start();
			btn_first.setDisable(true);
		} else {
			JOptionPane.showMessageDialog(null, "Not correct ID!");
		}
	}

	public void spyOnline() throws IOException {
		String id = tf_second.getText();

		String url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + id;
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		String line = reader.readLine();

		if (!line.contains("\"error\"")) {
			Thread thread = new Thread(new SpyOnline(id));
			thread.start();
			btn_second.setDisable(true);
		} else {
			JOptionPane.showMessageDialog(null, "Not correct ID!");
		}
	}

}
