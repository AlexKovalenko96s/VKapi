package ua.kas.main;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class ThreadConstructor implements Callable<ArrayList<String>> {

	private String id0 = "";
	private String id1 = "";
	private String id2 = "";
	private String id3 = "";
	private String id4 = "";

	public ThreadConstructor(String id0) {
		this.id0 = id0;
	}

	public ThreadConstructor(String id0, String id1) {
		this.id0 = id0;
		this.id1 = id1;
	}

	public ThreadConstructor(String id0, String id1, String id2) {
		this.id0 = id0;
		this.id1 = id1;
		this.id2 = id2;
	}

	public ThreadConstructor(String id0, String id1, String id2, String id3) {
		this.id0 = id0;
		this.id1 = id1;
		this.id2 = id2;
		this.id3 = id3;
	}

	public ThreadConstructor(String id0, String id1, String id2, String id3, String id4) {
		this.id0 = id0;
		this.id1 = id1;
		this.id2 = id2;
		this.id3 = id3;
		this.id4 = id4;
	}

	@Override
	public ArrayList<String> call() throws Exception {
		ArrayList<String> list = new ArrayList<>();
		if (!id0.equals(""))
			list.add(id0);
		if (!id1.equals(""))
			list.add(id1);
		if (!id2.equals(""))
			list.add(id2);
		if (!id3.equals(""))
			list.add(id3);
		if (!id4.equals(""))
			list.add(id4);
		return list;
	}

}
