package ua.kas.spyOnline;

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
import java.net.URL;

public class SpyOnline implements Runnable {

	private String id = "";
	private String first_name = "";
	private String last_name = "";

	private boolean work = true;

	public SpyOnline(String id) {
		this.id = id;
	}

	private String getOnline() throws IOException {
		String url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + id + "&fields=online";
		URL url2 = new URL(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
		String line = reader.readLine();

		line = line.substring(line.indexOf("\"first_name\":\"") + 14);
		first_name = line.substring(0, line.indexOf("\""));
		line = line.substring(line.indexOf("\"last_name\":\"") + 13);
		last_name = line.substring(0, line.indexOf("\""));

		if (line.contains("online_mobile")) {
			return "2";
		}
		return line.substring(line.indexOf("\"online\":") + 9, line.indexOf("}"));
	}

	@Override
	public void run() {
		// Создадим раскрывающееся меню
		PopupMenu popup = new PopupMenu();
		// Создадим элемент меню
		MenuItem exitItem = new MenuItem("Exit");
		// Добавим для него обработчик
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread.currentThread().interrupt();// not work
				work = false;
			}
		});
		// Нулевая отметка
		String status = null;
		try {
			status = getOnline();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Добавим пункт в меню
		popup.add(exitItem);
		SystemTray systemTray = SystemTray.getSystemTray();
		// получим картинку
		Image image = Toolkit.getDefaultToolkit().getImage("img/vk_icon.png");
		TrayIcon trayIcon = new TrayIcon(image, "VK_SpyOnline - " + first_name + " " + last_name, popup);
		trayIcon.setImageAutoSize(true);

		// добавим иконку в трей
		try {
			systemTray.add(trayIcon);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}

		trayIcon.displayMessage("VK_SpyOnline", first_name + " " + last_name + " - Spy activated!",
				TrayIcon.MessageType.INFO);

		// Бескоечный цикл
		for (;;) {
			// if (!Thread.currentThread().isInterrupted()) { //not work
			if (work) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					return;
				} // ждем три секунды

				// Здесь отработка
				try {
					if (!status.equals(getOnline())) {
						status = getOnline();
						if (Integer.parseInt(status) == 1) {
							trayIcon.displayMessage("VK_SpyOnline", first_name + " " + last_name + " - Online",
									TrayIcon.MessageType.INFO);
						} else if (Integer.parseInt(status) == 0) {
							trayIcon.displayMessage("VK_SpyOnline", first_name + " " + last_name + " - Offline",
									TrayIcon.MessageType.INFO);
						} else if (Integer.parseInt(status) == 2) {
							trayIcon.displayMessage("VK_SpyOnline", first_name + " " + last_name + " - Online_mobile",
									TrayIcon.MessageType.INFO);
						}
					}
				} catch (NumberFormatException | IOException e1) {
					e1.printStackTrace();
				}
			} else {
				systemTray.remove(trayIcon);
				return;
			}
		}
	}
}
