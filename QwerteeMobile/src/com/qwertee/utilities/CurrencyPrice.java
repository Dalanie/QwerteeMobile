package com.qwertee.utilities;

public class CurrencyPrice {
	private String currencyName;
	private String currencyChar;
	private int price; // TODO maybe change

	public CurrencyPrice(String currencyName, String currencyChar, int price) {
		this.currencyName = currencyName;
		this.currencyChar = currencyChar;
		this.price = price;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getCurrencyChar() {
		return currencyChar;
	}

	public void setCurrencyChar(String currencyChar) {
		this.currencyChar = currencyChar;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

}
