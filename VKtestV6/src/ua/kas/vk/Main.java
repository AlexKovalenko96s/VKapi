package ua.kas.vk;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Main {

	private static ArrayList<String> list = new ArrayList<>();
	private static ArrayList<String> checkList = new ArrayList<>();
	private static ArrayList<String> newList = new ArrayList<>();

	private static String access_token = "";

	public static void main(String[] args)
			throws InterruptedException, AWTException, IOException, URISyntaxException, NoSuchAlgorithmException {

		// http://oauth.vk.com/authorize?client_id=5809983&scope=messages,wall,friends,likes,users&redirect_uri=http://oauth.vk.com/blank.html&display=popup&response_type=token

		// Создадим раскрывающееся меню
		PopupMenu popup = new PopupMenu();
		// Создадим элемент меню
		MenuItem exitItem = new MenuItem("Выход");
		// Добавим для него обработчик
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		// Добавим пункт в меню
		popup.add(exitItem);
		SystemTray systemTray = SystemTray.getSystemTray();
		// получим картинку
		Image image = Toolkit.getDefaultToolkit().getImage("img/vk_icon.png");
		TrayIcon trayIcon = new TrayIcon(image, "VK_Online", popup);
		trayIcon.setImageAutoSize(true);
		// добавим иконку в трей
		systemTray.add(trayIcon);
		trayIcon.displayMessage("VK_Friends", "Соединяемся с сервером", TrayIcon.MessageType.INFO);

		access_token = "eb6581e2f31964f8046910070b60223a2cf73759f18ad18552f2dd9ac2f95e9e67e60c335329090103fc6";

		trayIcon.displayMessage("VK_Friends", "Соединение установлено", TrayIcon.MessageType.INFO);

		list = getFriends();

		// Бескоечный цикл
		for (;;) {
			Thread.sleep(3000);
			// Здесь отработка
			checkList = getFriends();
			if (!list.equals(checkList)) {
				for (String item1 : checkList) {
					for (String item2 : list) {
						if (item1.equals(item2)) {
							break;
						} else if (item1.compareTo(item2) < 0) {
							newList.add(item2);
						}
					}
				}
			}
			System.out.println(newList);
		}
	}

	// trayIcon.displayMessage("VK_Friends", "",
	// TrayIcon.MessageType.INFO);
	static ArrayList<String> getFriends() throws IOException {
		ArrayList<String> list = new ArrayList<>();

		String url = "https://api.vk.com/method/" + "friends.get" + "?user_id=" + "224429310" + "&access_token="
				+ access_token;
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		String line = reader.readLine();

		for (int i = 0;; i++) {
			if (i == 0) {
				list.add(line.substring(line.indexOf("[") + 1, line.indexOf(",")));
				line = line.substring(line.indexOf(",") + 1);
			} else {
				try {
					list.add(line.substring(0, line.indexOf(",")));
					line = line.substring(line.indexOf(",") + 1);
				} catch (Exception ex) {
					list.add(line.substring(0, line.indexOf("]")));
					break;
				}
			}
		}
		return list;
	}
}
