package com.qwertee.utilities;

public class NavDrawItem {
	public String text;

	public enum Type {
		CATEGORY, ENTRY
	};

	public Type type;

	public NavDrawItem(String text, Type type) {
		this.text = text;
		this.type = type;
	}
}
