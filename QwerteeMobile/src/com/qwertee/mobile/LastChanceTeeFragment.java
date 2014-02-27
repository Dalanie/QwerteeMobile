package com.qwertee.mobile;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.qwertee.network.NetworkCommunication;
import com.qwertee.tees.LastChanceTee;

public class LastChanceTeeFragment extends TeeFragment {

	public static final int UID = 1;

	@Override
	protected void getTeeToBuy() {
		NetworkCommunication.loadLastChanceTeeToBuy(
				new Listener<LastChanceTee>() {

					@Override
					public void onResponse(LastChanceTee responseTee) {
						tee = responseTee;
						initTee();
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
					}
				}, parentActivity);
	}
}