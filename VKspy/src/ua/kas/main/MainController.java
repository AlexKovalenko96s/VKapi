package ua.kas.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.WindowEvent;
import ua.kas.checkLikes.CheckLikes;
import ua.kas.spyOnline.SpyOnline;

public class MainController extends Application {
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

	private Thread thread = null;

	public boolean checkLikes() throws IOException {
		String id = tf_first.getText();

		String url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + id;
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		String line = reader.readLine();

		if (!line.contains("\"error\"") && !(line.length() == 15)) {
			if (cb_photo.isSelected() || cb_wall.isSelected()) {
				if (cb_wall.isSelected() && !cb_photo.isSelected()) {
					thread = new Thread(new CheckLikes(id, 0));
				} else if (cb_photo.isSelected() && !cb_wall.isSelected()) {
					thread = new Thread(new CheckLikes(id, 1));
				} else if (cb_photo.isSelected() && cb_wall.isSelected()) {
					thread = new Thread(new CheckLikes(id, 2));
				}
				thread.start();
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

		if (!line.contains("\"error\"")) {
			thread = new Thread(new SpyOnline(id));
			thread.start();
		} else {
			JOptionPane.showMessageDialog(null, "Not correct ID!");
		}
	}

	public void showDialog(ActionEvent actionEvent) throws Exception {

		if (checkLikes()) {
			Stage stage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("../checkLikes/CheckLikes.FXML"));
			stage.setTitle("TOP");
			stage.setResizable(false);
			stage.setScene(new Scene(root));
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
			stage.getIcons().add(new Image(getClass().getResourceAsStream("main/vk_icon.png")));
			stage.show();
			start(stage);
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				thread.interrupt();
			}
		});
	}
}
