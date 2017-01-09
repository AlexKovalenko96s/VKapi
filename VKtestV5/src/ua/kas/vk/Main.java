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

public class Main {

	static String access_token = "";

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
		trayIcon.displayMessage("VK_Online", "Соединяемся с сервером", TrayIcon.MessageType.INFO);

		access_token = "e7240682d33d5b819d94a871aaf88701969d48c87f001df1d6bd9b68807f9fc5b551dd72cd6d6125e654a";

		trayIcon.displayMessage("VK_Online", "Соединение установлено", TrayIcon.MessageType.INFO);
		String status = getOnline();
		// Бескоечный цикл
		for (;;) {
			Thread.sleep(3000); // ждем три секунды
			// Здесь отработка

			if (!status.equals(getOnline())) {
				status = getOnline();
				if (Integer.parseInt(status) == 1) {
					trayIcon.displayMessage("VKNotifer", "Online", TrayIcon.MessageType.INFO);
				} else if (Integer.parseInt(status) == 0) {
					trayIcon.displayMessage("VKNotifer", "Offline", TrayIcon.MessageType.INFO);
				} else if (Integer.parseInt(status) == 2) {
					trayIcon.displayMessage("VKNotifer", "Online_mobile", TrayIcon.MessageType.INFO);
				}
			}
		}
	}

	static String getOnline() throws IOException {
		String url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + "224429310" + "&fields=online"
				+ "&access_token=" + access_token;
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		String line = reader.readLine();
		System.out.println(line);
		if (line.contains("online_mobile")) {
			return "2";
		}
		return line.substring(line.indexOf("\"online\":") + 9, line.indexOf("}"));
	}
}
