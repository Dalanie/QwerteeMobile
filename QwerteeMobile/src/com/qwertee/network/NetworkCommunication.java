package com.qwertee.network;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.gson.reflect.TypeToken;
import com.qwertee.tees.BuyableTee;
import com.qwertee.tees.LastChanceTee;
import com.qwertee.tees.PreviousTee;
import com.qwertee.tees.TodaysTee;
import com.qwertee.tees.VoteTee;
import com.qwertee.utilities.Category;

public class NetworkCommunication {

	public static String qwerteeURL = "http://www.qwertee.com/";
	public static String serverURL = "http://85.25.49.60:8090/";
	public static String currentTeeToBuyURL = "today/";
	public static String lastChanceTeeToBuyURL = "last/";
	public static String previousTeeURL = "previous/";
	public static String voteURL = "vote/";
	public static String FAQ = "faq/";

	public static void addRequest(Request<?> request, Context context) {
		VolleySingleton.getRequestQueue().add(request);
	}

	public static void loadVotes(Listener<ArrayList<VoteTee>> successListener,
			ErrorListener errorListener, Context context) {
		Type voteTeeListType = new TypeToken<ArrayList<VoteTee>>() {
		}.getType();
		Request<ArrayList<VoteTee>> request = new GsonRequest<ArrayList<VoteTee>>(
				Method.GET, serverURL + voteURL, voteTeeListType,
				successListener, errorListener);
		addRequest(request, context);
	}

	public static void loadCurrentTeeToBuy(Listener<TodaysTee> successListener,
			ErrorListener errorListener, Context context) {
		Request<TodaysTee> request = new GsonRequest<TodaysTee>(Method.GET,
				serverURL + currentTeeToBuyURL, TodaysTee.class,
				successListener, errorListener);
		addRequest(request, context);
	}

	public static void loadLastChanceTeeToBuy(
			Listener<LastChanceTee> successListener,
			ErrorListener errorListener, Context context) {
		Request<LastChanceTee> request = new GsonRequest<LastChanceTee>(
				Method.GET, serverURL + lastChanceTeeToBuyURL,
				LastChanceTee.class, successListener, errorListener);
		addRequest(request, context);
	}

	public static void loadFAQ(Listener<ArrayList<Category>> successListener,
			ErrorListener errorListener, Context context) {
		Type categoryListType = new TypeToken<ArrayList<Category>>() {
		}.getType();
		Request<ArrayList<Category>> request = new GsonRequest<ArrayList<Category>>(
				Method.GET, serverURL + FAQ, categoryListType, successListener,
				errorListener);
		addRequest(request, context);
	}

	public static void loadPreviousTees(
			Listener<ArrayList<PreviousTee>> successListener,
			ErrorListener errorListener, Context context) {
		Type previousTeeListType = new TypeToken<ArrayList<PreviousTee>>() {
		}.getType();
		Request<ArrayList<PreviousTee>> request = new GsonRequest<ArrayList<PreviousTee>>(
				Method.GET, serverURL + previousTeeURL, previousTeeListType,
				successListener, errorListener);
		addRequest(request, context);
	}
}
