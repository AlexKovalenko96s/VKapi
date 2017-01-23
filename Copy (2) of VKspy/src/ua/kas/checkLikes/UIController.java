package ua.kas.checkLikes;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class UIController extends Application implements Runnable {

	private ActionEvent actionEvent = null;

	private Thread thread = null;

	public UIController(ActionEvent actionEvent, Thread thread) {
		this.actionEvent = actionEvent;
		this.thread = thread;
	}

	public void showDialog() throws Exception {
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(this.getClass().getResource("CheckLikes.fxml"));
		stage.setTitle("TOP");
		stage.setResizable(false);
		stage.setScene(new Scene(root));
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
		stage.getIcons().add(new Image(this.getClass().getResourceAsStream("vk_icon.png")));
		stage.show();
		start(stage);
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

	@Override
	public void run() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				try {
					showDialog();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
