package com.qwertee.tees;

import java.util.ArrayList;

public class LastChanceTee extends BuyableTee {

	public LastChanceTee(String userName, int userId, String designName,
			int teeId) {
		super(userName, userId, designName, teeId);
	}

	public LastChanceTee() {
	};

	@Override
	protected void initCurrencyPrice() {
		prices = new ArrayList<CurrencyPrice>();
		prices.add(new CurrencyPrice("eur", "\u20ac", 12));
		prices.add(new CurrencyPrice("gbp", "\u00a3", 10));
		prices.add(new CurrencyPrice("usd", "$", 14));
	}
}
