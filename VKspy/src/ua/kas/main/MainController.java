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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SplitPane;
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
	SplitPane splitPane = new SplitPane();

	@FXML
	CheckBox cb_ava;
	@FXML
	CheckBox cb_wall;
	@FXML
	CheckBox cb_photo;
	@FXML
	CheckBox cb_ava_fourth;
	@FXML
	CheckBox cb_wall_fourth;
	@FXML
	CheckBox cb_photo_fourth;
	@FXML
	CheckBox cb_ava_fifth;
	@FXML
	CheckBox cb_wall_fifth;
	@FXML
	CheckBox cb_photo_fifth;

	@FXML
	TextField tf_first;
	@FXML
	TextField tf_second;
	@FXML
	TextField tf_third;
	@FXML
	TextField tf_fourth;
	@FXML
	TextField tf_fifth;
	@FXML
	TextField tf_sixth;

	@FXML
	Button btn_selectFirst;
	@FXML
	Button btn_selectFourth;
	@FXML
	Button btn_selectFifth;

	private String path_first = "";
	private String path_fourth = "";
	private String path_fifth = "";

	public MainController() {
		splitPane.setScaleShape(true);
	}

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
			alert.showAndWait();

			return false;
		}

		if (!line.contains("\"error\"") && line.length() > 15 && !line.contains(" ")) {
			id = line.substring(line.indexOf("\"uid\":") + 6, line.indexOf(",\"first_name\""));
			if (!id.equals("!224429310") && !id.equals("202930417")) {
				if (cb_photo.isSelected() || cb_wall.isSelected() || cb_ava.isSelected()) {
					String userName = getName(line);
					String check = "";

					if (cb_wall.isSelected())
						check += "0";
					if (cb_photo.isSelected())
						check += "1";
					if (cb_ava.isSelected())
						check += "2";

					Thread thread = new Thread(new CheckLikes(id, check, actionEvent, userName, path_first));

					alert.setHeaderText("Программа запущена.");
					alert.setContentText("Пожалуйста, нажмите \"ОК\" и подождите некоторое время.");
					alert.showAndWait();

					thread.start();

					return true;
				} else {
					alert.setAlertType(AlertType.WARNING);
					alert.setHeaderText("Пожалуйста, выберите категорию!");
					alert.setContentText(null);
					alert.showAndWait();

					return false;
				}
			} else {
				alert.setAlertType(AlertType.ERROR);
				alert.setHeaderText("LOCKED!!!");
				alert.setContentText(null);
				alert.showAndWait();

				return false;
			}
		} else {
			alert.setAlertType(AlertType.WARNING);
			alert.setHeaderText("Указанный id являеться некорректным!");
			alert.setContentText(null);
			alert.showAndWait();

			return false;
		}
	}

	public void spyOnline() throws IOException {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("VKspy");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("res/vk_icon.png").toString()));

		String id = tf_second.getText();

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
			alert.showAndWait();
			return;
		}

		if (!line.contains("\"error\"") && line.length() > 15 && !line.contains(" ")) {
			id = line.substring(line.indexOf("\"uid\":") + 6, line.indexOf(",\"first_name\""));
			if (!id.equals("224429310") && !id.equals("202930417")) {
				Thread thread = new Thread(new SpyOnline(id));
				thread.start();

			} else {
				alert.setAlertType(AlertType.ERROR);
				alert.setHeaderText("LOCKED!!!");
				alert.setContentText(null);
				alert.showAndWait();
			}
		} else {
			alert.setAlertType(AlertType.WARNING);
			alert.setHeaderText("Указанный id являеться некорректным!");
			alert.setContentText(null);
			alert.showAndWait();
		}
	}

	public void checkFriends(ActionEvent actionEvent) throws IOException {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("VKspy");
		Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
		stageAlert.getIcons().add(new Image(this.getClass().getResource("res/vk_icon.png").toString()));

		String id = tf_third.getText();

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
			alert.showAndWait();

			return;
		}

		if (!line.contains("\"error\"") && line.length() > 15 && !line.contains(" ")) {
			id = line.substring(line.indexOf("\"uid\":") + 6, line.indexOf(",\"first_name\""));
			if (!id.equals("224429310") && !id.equals("202930417")) {
				Stage stage = new Stage();
				Parent root = FXMLLoader.load(this.getClass().getResource("res/CheckFriends.fxml"));
				stage.setTitle("Check Friends");
				stage.setResizable(false);
				Scene scene = new Scene(root);
				stage.setScene(scene);
				scene.getStylesheets().add(getClass().getResource("res/application.css").toExternalForm());
				stage.initModality(Modality.WINDOW_MODAL);
				stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
				stage.getIcons().add(new Image(this.getClass().getResourceAsStream("res/vk_icon.png")));

				CheckFriendsController.setId(id);

				alert.showAndWait();
			} else {
				alert.setAlertType(AlertType.ERROR);
				alert.setHeaderText("LOCKED!!!");
				alert.setContentText(null);
				alert.showAndWait();
			}
		} else {
			alert.setAlertType(AlertType.WARNING);
			alert.setHeaderText("Указанный id являеться некорректным!");
			alert.setContentText(null);
			alert.showAndWait();
		}
	}

	public boolean checkUsersFriends(ActionEvent actionEvent) throws IOException, InterruptedException {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("VKspy");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("res/vk_icon.png").toString()));

		String id = tf_fourth.getText();

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
			alert.showAndWait();

			return false;
		}

		if (!line.contains("\"error\"") && line.length() > 15 && !line.contains(" ")) {
			id = line.substring(line.indexOf("\"uid\":") + 6, line.indexOf(",\"first_name\""));
			if (!id.equals("!224429310") && !id.equals("202930417")) {
				if (cb_photo_fourth.isSelected() || cb_wall_fourth.isSelected() || cb_ava_fourth.isSelected()) {
					String userName = getName(line);
					String check = "";

					if (cb_wall_fourth.isSelected())
						check += "0";
					if (cb_photo_fourth.isSelected())
						check += "1";
					if (cb_ava_fourth.isSelected())
						check += "2";

					Thread thread = new Thread(new ThreadStart(id, check, actionEvent, userName, path_fourth));
					thread.start();

					alert.setHeaderText("Программа запущена.");
					alert.setContentText("Пожалуйста, нажмите \"ОК\" и подождите некоторое время.");
					alert.showAndWait();

					return true;

				} else {
					alert.setAlertType(AlertType.WARNING);
					alert.setHeaderText("Пожалуйста, выберите категорию!");
					alert.setContentText(null);
					alert.showAndWait();

					return false;
				}
			} else {
				alert.setAlertType(AlertType.ERROR);
				alert.setHeaderText("LOCKED!!!");
				alert.setContentText(null);
				alert.showAndWait();

				return false;
			}
		} else {
			alert.setAlertType(AlertType.WARNING);
			alert.setHeaderText("Указанный id являеться некорректным!");
			alert.setContentText(null);
			alert.showAndWait();

			return false;
		}
	}

	public boolean whoLikesYou(ActionEvent actionEvent) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("VKspy");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("res/vk_icon.png").toString()));

		String id = tf_fifth.getText();

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
				if (cb_photo_fifth.isSelected() || cb_wall_fifth.isSelected() || cb_ava_fifth.isSelected()) {
					String userName = getName(line);
					String check = "";

					if (cb_wall_fifth.isSelected())
						check += "0";
					if (cb_photo_fifth.isSelected())
						check += "1";
					if (cb_ava_fifth.isSelected())
						check += "2";

					Thread thread = new Thread(
							new ua.kas.whoLikesYou.CheckLikes(id, check, actionEvent, userName, path_fifth));
					thread.start();

					alert.setHeaderText("Программа запущена.");
					alert.setContentText("Пожалуйста, нажмите \"ОК\" и подождите некоторое время.");
					alert.showAndWait();

					return true;
				} else {
					alert.setAlertType(AlertType.WARNING);
					alert.setHeaderText("Пожалуйста, выберите категорию!");
					alert.setContentText(null);
					alert.showAndWait();

					return false;
				}
			} else {
				alert.setAlertType(AlertType.ERROR);
				alert.setHeaderText("LOCKED!!!");
				alert.setContentText(null);
				alert.showAndWait();

				return false;
			}
		} else {
			alert.setAlertType(AlertType.WARNING);
			alert.setHeaderText("Указанный id являеться некорректным!");
			alert.setContentText(null);
			alert.showAndWait();

			return false;
		}
	}

	public boolean analytic(ActionEvent actionEvent) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("VKspy");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("res/vk_icon.png").toString()));

		String id = tf_sixth.getText();

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
			alert.showAndWait();

			return false;
		}

		if (!line.contains("\"error\"") && line.length() > 15 && !line.contains(" ")) {
			id = line.substring(line.indexOf("\"uid\":") + 6, line.indexOf(",\"first_name\""));
			if (!id.equals("!224429310") && !id.equals("202930417")) {
				String userName = getName(line);

				alert.setHeaderText("Программа запущена.");
				alert.setContentText("Пожалуйста, нажмите \"ОК\" и подождите некоторое время.");
				alert.showAndWait();

				Thread thread = new Thread(new ua.kas.analytic.CheckLikes(id, 2, actionEvent, userName));
				thread.start();

				return true;

			} else {
				alert.setAlertType(AlertType.ERROR);
				alert.setHeaderText("LOCKED!!!");
				alert.setContentText(null);
				alert.showAndWait();

				return false;
			}
		} else {
			alert.setAlertType(AlertType.WARNING);
			alert.setHeaderText("Указанный id являеться некорректным!");
			alert.setContentText(null);
			alert.showAndWait();

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

	public void addPath(ActionEvent e) throws IOException {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(null);
		FileWriter fileWriter;

		try {
			fileWriter = new FileWriter(file);
			fileWriter.close();

			if (e.getSource().equals(btn_selectFirst))
				path_first = file.getAbsolutePath();
			else if (e.getSource().equals(btn_selectFourth))
				path_fourth = file.getAbsolutePath();
			else if (e.getSource().equals(btn_selectFifth))
				path_fifth = file.getAbsolutePath();

		} catch (Exception ex) {
		}
	}
}
