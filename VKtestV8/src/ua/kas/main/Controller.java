package ua.kas.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Controller {

	@FXML
	TextField tf;

	// 224429310
	// 18472340

	public void button() throws IOException {
		String id = tf.getText();

		String url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + id;
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		String line = reader.readLine();

		if (!line.contains("\"error\"") && line.length() > 15) {
			Thread thread = new Thread(new ThreadStart(id));
			thread.start();
		} else
			JOptionPane.showMessageDialog(null, "notCorrect");
	}
}
