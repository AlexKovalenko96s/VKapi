package ua.kas.checkLikes;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class CheckLikesController implements Initializable {

	private static ArrayList<String> top = new ArrayList<>();

	@FXML
	Label l0;
	@FXML
	Label l1;
	@FXML
	Label l2;
	@FXML
	Label l3;
	@FXML
	Label l4;
	@FXML
	Label l5;
	@FXML
	Label l6;
	@FXML
	Label l7;
	@FXML
	Label l8;
	@FXML
	Label l9;
	@FXML
	Label l10;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		while (top.size() == 0) {
			System.out.println("dd");
			l0.setText("Please wait!");
		}
		l0.setText("TOP");
	}

	public static void setTop(ArrayList<String> top) {
		CheckLikesController.top = top;
	}
}
