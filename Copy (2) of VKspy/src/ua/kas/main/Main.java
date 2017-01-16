package ua.kas.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("VKspy");
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("vk_icon.png")));
			primaryStage.setScene(scene);
			primaryStage.show();

			// primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			// @Override
			// public void handle(WindowEvent event) {
			// System.exit(0);
			// }
			// });

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
