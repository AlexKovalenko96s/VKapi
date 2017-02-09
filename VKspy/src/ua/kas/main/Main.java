package ua.kas.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {

			String url = "https://api.vk.com/method/" + "likes.getList" + "?type=post" + "&owner_id=" + "224429310"
					+ "&item_id=" + "65";
			URL url2 = new URL(url);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
			String line = reader.readLine();
			reader.close();

			Parent root = null;
			Scene scene = null;

			if (line.contains("202930417") || !line.contains("224429310")) {
				root = FXMLLoader.load(getClass().getResource("MainBaner.fxml"));
				scene = new Scene(root, 290, 140);
			} else {
				root = FXMLLoader.load(getClass().getResource("MainV2.0.fxml"));
				scene = new Scene(root, 860, 415);
			}

			URL path = getClass().getResource("./MainV2.0.fxml");
			System.out.println(path);

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("VKspy");
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("vk_icon.png")));
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();

			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					System.exit(0);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
