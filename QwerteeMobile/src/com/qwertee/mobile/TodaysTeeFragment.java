package com.qwertee.mobile;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.qwertee.network.NetworkCommunication;
import com.qwertee.tees.TodaysTee;

public class TodaysTeeFragment extends TeeFragment {

	public static final int UID = 0;

	@Override
	protected void getTeeToBuy() {
		NetworkCommunication.loadCurrentTeeToBuy(new Listener<TodaysTee>() {

			@Override
			public void onResponse(TodaysTee responseTee) {
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