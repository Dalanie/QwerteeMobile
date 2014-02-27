package com.qwertee.utilities;

import java.util.ArrayList;

public class Category {
	private String name;
	private ArrayList<FAQEntry> entries;

	public Category(String name, ArrayList<FAQEntry> entries) {
		this.name = name;
		this.entries = entries;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<FAQEntry> getEntries() {
		return entries;
	}

	public void setEntries(ArrayList<FAQEntry> entries) {
		this.entries = entries;
	}
}
