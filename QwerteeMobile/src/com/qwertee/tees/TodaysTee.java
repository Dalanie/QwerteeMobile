package com.qwertee.tees;

import java.util.ArrayList;

import com.qwertee.utilities.CurrencyPrice;

public class TodaysTee extends BuyableTee {

	public TodaysTee(String userName, int userId, String designName, int teeId) {
		super(userName, userId, designName, teeId);
	}

	public TodaysTee() {
	}

	@Override
	protected void initCurrencyPrice() {
		prices = new ArrayList<CurrencyPrice>();
		prices.add(new CurrencyPrice("eur", "\u20ac", 10));
		prices.add(new CurrencyPrice("gbp", "\u00a3", 8));
		prices.add(new CurrencyPrice("usd", "$", 12));
	}

}
