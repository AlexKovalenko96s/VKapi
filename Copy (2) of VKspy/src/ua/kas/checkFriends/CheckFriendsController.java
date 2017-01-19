package ua.kas.checkFriends;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class CheckFriendsController {

	@FXML
	Button btn_start = null;

	private File file = null;

	private Thread thread = null;

	private String path = "";

	private static String id = "";

	public void addFile() throws IOException {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(null);
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(file);
			fileWriter.close();
			path = file.getAbsolutePath();
			createFile();
		} catch (Exception ex) {
			btn_start.setDisable(true);
		}
	}

	public void selectFile() {
		FileChooser fileChose = new FileChooser();
		fileChose.getExtensionFilters().addAll(new ExtensionFilter("TXT files (*.txt)", "*.txt"));
		file = fileChose.showOpenDialog(null);

		if (file != null) {
			btn_start.setDisable(false);
		} else {
			btn_start.setDisable(true);
		}
	}

	private void createFile() {
		thread = new Thread(new ThreadAddFile(id, path));
		thread.start();
	}

	public void start() {
		thread = new Thread(new ThreadCheckFriends(id, file));
		thread.start();
	}

	public static void setId(String id) {
		CheckFriendsController.id = id;
	}
}
