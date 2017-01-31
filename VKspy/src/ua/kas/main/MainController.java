package ua.kas.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ua.kas.checkFriends.CheckFriendsController;
import ua.kas.checkLikes.CheckLikes;
import ua.kas.checkUsersFriends.ThreadStart;
import ua.kas.spyOnline.SpyOnline;

public class MainController {
	// 224429310
	// 18472340

	@FXML
	CheckBox cb_wall;
	@FXML
	CheckBox cb_photo;
	@FXML
	CheckBox cb_wall_fourth;
	@FXML
	CheckBox cb_photo_fourth;

	@FXML
	TextField tf_first;
	@FXML
	TextField tf_second;
	@FXML
	TextField tf_third;
	@FXML
	TextField tf_fourth;

	private Thread thread = null;

	private String path_first = "";
	private String path_fourth = "";

	public boolean checkLikes(ActionEvent actionEvent) throws IOException, InterruptedException {
		String id = tf_first.getText();

		String url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + id;
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		String line = reader.readLine();

		if (!line.contains("\"error\"") && line.length() > 15 && !line.contains(" ")) {
			if (cb_photo.isSelected() || cb_wall.isSelected()) {
				String userName = getName(line);

				if (cb_wall.isSelected() && !cb_photo.isSelected()) {
					thread = new Thread(new CheckLikes(id, 0, actionEvent, userName, path_first));
				} else if (cb_photo.isSelected() && !cb_wall.isSelected()) {
					thread = new Thread(new CheckLikes(id, 1, actionEvent, userName, path_first));
				} else if (cb_photo.isSelected() && cb_wall.isSelected()) {
					thread = new Thread(new CheckLikes(id, 2, actionEvent, userName, path_first));
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
			JOptionPane.showMessageDialog(null, "Указанный id являеться некорректным!");
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
			JOptionPane.showMessageDialog(null, "Указанный id являеться некорректным!");
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
			JOptionPane.showMessageDialog(null, "Указанный id являеться некорректным!");
		}
	}

	public boolean checkUsersFriends(ActionEvent actionEvent) throws IOException, InterruptedException {
		String id = tf_fourth.getText();

		String url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + id;
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		String line = reader.readLine();

		if (!line.contains("\"error\"") && line.length() > 15 && !line.contains(" ")) {
			// if (!id.equals("224429310")) {
			String userName = getName(line);

			if (cb_photo_fourth.isSelected() || cb_wall_fourth.isSelected()) {
				if (cb_wall_fourth.isSelected() && !cb_photo_fourth.isSelected()) {
					thread = new Thread(new ThreadStart(id, 0, actionEvent, userName));
				} else if (cb_photo_fourth.isSelected() && !cb_wall_fourth.isSelected()) {
					thread = new Thread(new ThreadStart(id, 1, actionEvent, userName));
				} else if (cb_photo_fourth.isSelected() && cb_wall_fourth.isSelected()) {
					thread = new Thread(new ThreadStart(id, 2, actionEvent, userName));
				}
				thread.start();

				JOptionPane.showMessageDialog(null, "Программа запущена.\nПожалуйста, подождите некоторое время.");

				return true;

			} else {
				JOptionPane.showMessageDialog(null, "Пожалуйста, выберите категорию!");
				return false;
			}
			// } else {
			// JOptionPane.showMessageDialog(null, "LOCKED!!");
			// return false;
			// }
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

	public void addPathFirst() throws IOException {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(null);
		FileWriter fileWriter;

		fileWriter = new FileWriter(file);
		fileWriter.close();

		path_first = file.getAbsolutePath();
	}

	public void addPathFourth() throws IOException {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(null);
		FileWriter fileWriter;

		fileWriter = new FileWriter(file);
		fileWriter.close();

		path_fourth = file.getAbsolutePath();
	}
}
