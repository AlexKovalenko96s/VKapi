package ua.kas.whoLikesYou;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UIController implements Runnable {

	private ActionEvent actionEvent = null;

	public UIController(ActionEvent actionEvent) {
		this.actionEvent = actionEvent;
		run();
	}

	public void showDialog() throws Exception {
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(this.getClass().getResource("res/CheckLikes.fxml"));
		stage.setTitle("TOP");
		stage.setResizable(false);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		scene.getStylesheets().add(getClass().getResource("res/application.css").toExternalForm());
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
		stage.getIcons().add(new Image(this.getClass().getResourceAsStream("res/vk_icon.png")));
		stage.show();
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
