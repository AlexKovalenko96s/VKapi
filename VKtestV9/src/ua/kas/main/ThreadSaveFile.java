package ua.kas.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class ThreadSaveFile implements Runnable {

	private ArrayList<String> listId = new ArrayList<>();
	private ArrayList<Integer> listCount = new ArrayList<>();
	private ArrayList<String> listTop = new ArrayList<>();

	private URL url2;

	private BufferedReader reader = null;

	private String path = "";
	private String first_name = "";
	private String last_name = "";
	private String url = "";

	public ThreadSaveFile(ArrayList<String> listId, ArrayList<Integer> listCount, String path) {
		this.listId = listId;
		this.listCount = listCount;
		this.path = path;
	}

	@Override
	public void run() {
		getName(listId, listCount);
		try {
			createFile(listTop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getName(ArrayList<String> listForAdding, ArrayList<Integer> listCount) {
		for (int i = 0; i < listForAdding.size(); i++) {
			if (listCount.get(i) != 0) {
				if (!Thread.currentThread().isInterrupted()) {
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						return;
					}
					url = "https://api.vk.com/method/" + "users.get" + "?user_ids=" + listForAdding.get(i);
					try {
						url2 = new URL(url);
						reader = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));

						String line = reader.readLine();
						line = line.substring(line.indexOf("\"first_name\":\"") + 14);
						first_name = line.substring(0, line.indexOf("\""));
						line = line.substring(line.indexOf("\"last_name\":\"") + 13);
						last_name = line.substring(0, line.indexOf("\""));

						listTop.add("У " + first_name + " " + last_name + " - " + listCount.get(i));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else
					return;
			} else
				break;
		}
	}

	private void createFile(ArrayList<String> listTop) throws IOException {
		FileWriter fileWriter;
		fileWriter = new FileWriter(new File("D:\\Games\\1111.txt"));

		for (int i = 0; i < listTop.size(); i++) {
			fileWriter.write(listTop.get(i));
			fileWriter.append(System.lineSeparator());
		}
		fileWriter.flush();
		fileWriter.close();
	}
}
