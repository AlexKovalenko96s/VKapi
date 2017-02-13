package ua.kas.analytic;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

public class ChartController implements Initializable {

	ObservableList<PieChart.Data> pieChartData;

	@FXML
	PieChart pieChartFirst = new PieChart();
	@FXML
	PieChart pieChartSecond = new PieChart();

	@FXML
	Label l_man = new Label();
	@FXML
	Label l_woman = new Label();
	@FXML
	Label l_friend = new Label();
	@FXML
	Label l_noFriends = new Label();
	@FXML
	Label l_firstSum = new Label();
	@FXML
	Label l_secondSum = new Label();
	@FXML
	Label l_name = new Label();

	private ObservableList<PieChart.Data> detailsFirst = FXCollections.observableArrayList();
	private ObservableList<PieChart.Data> detailsSecond = FXCollections.observableArrayList();

	private static String friendsOrNo = "";
	private static String manWoman = "";
	private static String name = "";

	private int man = 0;
	private int woman = 0;
	private int deleted = 0;
	private int friends = 0;
	private int noFriends = 0;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		man = Integer.parseInt(manWoman.substring(manWoman.indexOf("man=") + 4, manWoman.indexOf(",")));
		woman = Integer.parseInt(manWoman.substring(manWoman.indexOf("woman=") + 6, manWoman.indexOf(",deleted")));
		deleted = Integer.parseInt(manWoman.substring(manWoman.indexOf("deleted=") + 8));
		friends = Integer.parseInt(
				friendsOrNo.substring(friendsOrNo.indexOf("friends=") + 8, friendsOrNo.indexOf(",noFriends")));
		noFriends = Integer.parseInt(friendsOrNo.substring(friendsOrNo.indexOf("noFriends=") + 10));

		detailsFirst.addAll(new PieChart.Data("парни", man), new PieChart.Data("девушки", woman));
		detailsSecond.addAll(new PieChart.Data("друзья", friends), new PieChart.Data("не друзья", noFriends));

		pieChartFirst.setData(detailsFirst);
		pieChartSecond.setData(detailsSecond);

		if (deleted == 0) {
			l_firstSum.setText(l_firstSum.getText() + (man + woman));
		} else if (deleted != 0) {
			l_firstSum.setText(l_firstSum.getText() + (man + woman) + " + " + deleted + " удаленный/х");
		}
		l_man.setText(
				l_man.getText() + man + " - " + new DecimalFormat("#0.00").format((100.0 / (man + woman)) * man) + "%");
		l_woman.setText(l_woman.getText() + woman + " - "
				+ new DecimalFormat("#0.00").format((100.0 / (man + woman)) * woman) + "%");

		l_secondSum.setText(l_secondSum.getText() + (friends + noFriends));
		l_friend.setText(l_friend.getText() + friends + " - "
				+ new DecimalFormat("#0.00").format((100.0 / (friends + noFriends)) * friends) + "%");
		l_noFriends.setText(l_noFriends.getText() + noFriends + " - "
				+ new DecimalFormat("#0.00").format((100.0 / (friends + noFriends)) * noFriends) + "%");

		l_name.setText("Анализ лайков на странице " + name);

		sound();
	}

	private void sound() {
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}

		AudioInputStream inputStream;

		try {
			inputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("res/sound.wav"));
			clip.open(inputStream);
			clip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public static void setFriendsOrNo(String friendsOrNo) {
		ChartController.friendsOrNo = friendsOrNo;
	}

	public static void setManWoman(String manWoman) {
		ChartController.manWoman = manWoman;
	}

	public static void setName(String name) {
		ChartController.name = name;
	}
}
