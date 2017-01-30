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
	TextField tf;

	@FXML
	CheckBox cb_wall;
	@FXML
	CheckBox cb_photo;

	private Thread thread = null;

	// 224429310
	// 18472340
	// 102002615

	public boolean button(ActionEvent actionEvent) throws IOException {
		String id = tf.getText();
		String url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + id;
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		String line = reader.readLine();
		if (!line.contains("\"error\"") && line.length() > 15 && !line.contains(" ")) {
			if (!id.equals("224429310")) {
				if (cb_photo.isSelected() || cb_wall.isSelected()) {
					if (cb_wall.isSelected() && !cb_photo.isSelected()) {
						thread = new Thread(new ThreadStart(id, 0, actionEvent));
					} else if (cb_photo.isSelected() && !cb_wall.isSelected()) {
						thread = new Thread(new ThreadStart(id, 1, actionEvent));
					} else if (cb_photo.isSelected() && cb_wall.isSelected()) {
						thread = new Thread(new ThreadStart(id, 2, actionEvent));
					}
					thread.start();

					JOptionPane.showMessageDialog(null, "Program was launched. Please wait, it will take some time.");

					return true;
				} else {
					JOptionPane.showMessageDialog(null, "Please select categories!");
					return false;
				}
			} else {
				JOptionPane.showMessageDialog(null, "LOCKED!!");
				return false;
			}
		} else {
			JOptionPane.showMessageDialog(null, "notCorrect");
			return false;
		}
	}
}
