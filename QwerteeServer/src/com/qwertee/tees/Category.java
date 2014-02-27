package com.qwertee.tees;

import java.util.List;

import com.google.gson.Gson;

public class Category {
	private String name;
	private List<FAQEntry> entries;

	public Category(String name, List<FAQEntry> entries) {
		this.name = name;
		this.entries = entries;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FAQEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<FAQEntry> entries) {
		this.entries = entries;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
