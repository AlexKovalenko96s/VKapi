package ua.kas.analytic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Controller {

	@FXML
	TextField tf_first;

	private Thread thread;

	public boolean checkLikes(ActionEvent actionEvent) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("VKspy");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("res/vk_icon.png").toString()));

		String id = tf_first.getText();

		String url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + id;
		URL url2;
		String line = "";
		try {
			url2 = new URL(url);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));
			line = reader.readLine();
		} catch (IOException e) {
			alert.setAlertType(AlertType.ERROR);
			alert.setHeaderText("Отсутствует соединение с интернетом!");
			alert.setContentText(null);
			stage.show();

			return false;
		}

		if (!line.contains("\"error\"") && line.length() > 15 && !line.contains(" ")) {
			id = line.substring(line.indexOf("\"uid\":") + 6, line.indexOf(",\"first_name\""));
			if (!id.equals("!224429310") && !id.equals("202930417")) {
				String userName = getName(line);

				thread = new Thread(new CheckLikes(id, 2, actionEvent, userName));

				alert.setHeaderText("Программа запущена.");
				alert.setContentText("Пожалуйста, нажмите \"ОК\" и подождите некоторое время.");
				stage.show();

				thread.start();

				return true;

			} else {
				alert.setAlertType(AlertType.ERROR);
				alert.setHeaderText("LOCKED!!!");
				alert.setContentText(null);
				stage.show();

				return false;
			}
		} else {
			alert.setAlertType(AlertType.WARNING);
			alert.setHeaderText("Указанный id являеться некорректным!");
			alert.setContentText(null);
			stage.show();

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
