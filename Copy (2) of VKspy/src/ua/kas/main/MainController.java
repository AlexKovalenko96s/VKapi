package ua.kas.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ua.kas.checkFriends.CheckFriendsController;
import ua.kas.checkLikes.CheckLikes;
import ua.kas.checkLikes.UIController;
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
	TextField tf_third;

	private Thread thread = null;
	private Thread threadUI = null;

	public boolean checkLikes(ActionEvent actionEvent) throws IOException, InterruptedException {
		String id = tf_first.getText();

		String url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + id;
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		String line = reader.readLine();

		if (!line.contains("\"error\"") && line.length() > 15 && !line.contains(" ")) {
			if (cb_photo.isSelected() || cb_wall.isSelected()) {
				if (cb_wall.isSelected() && !cb_photo.isSelected()) {
					thread = new Thread(new CheckLikes(id, 0));
				} else if (cb_photo.isSelected() && !cb_wall.isSelected()) {
					thread = new Thread(new CheckLikes(id, 1));
				} else if (cb_photo.isSelected() && cb_wall.isSelected()) {
					thread = new Thread(new CheckLikes(id, 2));
				}
				thread.start();
				threadUI = new Thread(new UIController(actionEvent, thread));
				threadUI.start();
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
				JOptionPane.showMessageDialog(null, "Please select categories!");
				return false;
			}
		} else {
			JOptionPane.showMessageDialog(null, "Not correct ID!");
			return false;
		}
	}

	public void spyOnline() throws IOException {
		String id = tf_second.getText();

		String url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + id;
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		String line = reader.readLine();

		if (!line.contains("\"error\"") && line.length() > 15 && !line.contains(" ")) {
			thread = new Thread(new SpyOnline(id));
			thread.start();
		} else {
			JOptionPane.showMessageDialog(null, "Not correct ID!");
		}
	}

	public void checkFriends(ActionEvent actionEvent) throws IOException {
		String id = tf_third.getText();

		String url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + id;
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		String line = reader.readLine();

		if (!line.contains("\"error\"") && line.length() > 15 && !line.contains(" ")) {
			Stage stage = new Stage();
			Parent root = FXMLLoader.load(this.getClass().getResource("CheckFriends.fxml"));
			stage.setTitle("Check Friends");
			stage.setResizable(false);
			stage.setScene(new Scene(root));
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
			stage.getIcons().add(new Image(this.getClass().getResourceAsStream("vk_icon.png")));

			CheckFriendsController.setId(id);

			stage.show();
		} else {
			JOptionPane.showMessageDialog(null, "Not correct ID!");
		}
	}
}
