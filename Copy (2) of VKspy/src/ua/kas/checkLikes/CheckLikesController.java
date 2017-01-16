package ua.kas.checkLikes;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class CheckLikesController implements Initializable {

	private static ArrayList<Label> label = new ArrayList<>();

	private static ArrayList<String> top = new ArrayList<>();

	private static int size = 10;

	@FXML
	Label l0 = new Label();
	@FXML
	Label l1 = new Label();
	@FXML
	Label l2 = new Label();
	@FXML
	Label l3 = new Label();
	@FXML
	Label l4 = new Label();
	@FXML
	Label l5 = new Label();
	@FXML
	Label l6 = new Label();
	@FXML
	Label l7 = new Label();
	@FXML
	Label l8 = new Label();
	@FXML
	Label l9 = new Label();
	@FXML
	Label l10 = new Label();

	public static void setTop(ArrayList<String> top) {
		CheckLikesController.top = top;
	}

	public static void setSize(int size) {
		CheckLikesController.size = size;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		top.clear();
		label.clear();
		while (top.size() != size) {
			try {
				Thread.currentThread();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		label.add(l1);
		label.add(l2);
		label.add(l3);
		label.add(l4);
		label.add(l5);
		label.add(l6);
		label.add(l7);
		label.add(l8);
		label.add(l9);
		label.add(l10);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				l0.setText("TOP");
				for (int i = 0; i < top.size(); i++) {
					label.get(i).setText(top.get(i));
					label.get(i).setVisible(true);
				}
			}
		});
	}
}
