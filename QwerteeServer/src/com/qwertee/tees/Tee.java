package com.qwertee.tees;

import java.io.Serializable;

import com.google.gson.Gson;

public abstract class Tee implements Serializable {

	protected static final String userPath = "profile/";
	protected static final String imageEnding = ".jpg";

	protected String userName;
	protected int userId;
	protected String designName;
	protected int teeId;

	public Tee() {
	}

	public Tee(String userName, int userId, String designName, int teeId) {
		this.userId = userId;
		this.userName = userName;
		this.designName = designName;
		this.teeId = teeId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getDesignName() {
		return designName;
	}

	public void setDesignName(String designName) {
		this.designName = designName;
	}

	public int getTeeId() {
		return teeId;
	}

	public void setTeeId(int teeId) {
		this.teeId = teeId;
	}

	public static String getUserpath() {
		return userPath;
	}

	public static String getImageending() {
		return imageEnding;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}