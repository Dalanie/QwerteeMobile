package com.qwertee.tees;

import java.util.List;

public abstract class BuyableTee extends Tee{
	protected List<CurrencyPrice> prices;
	
	public BuyableTee(String userName, int userId, String designName, int teeId) {
		super(userName, userId, designName, teeId);
		initCurrencyPrice();
	}
	
	public BuyableTee(){
		initCurrencyPrice();
	}

	protected abstract void initCurrencyPrice();

}
