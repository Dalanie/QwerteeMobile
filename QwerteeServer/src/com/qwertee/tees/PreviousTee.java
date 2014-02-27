package com.qwertee.tees;

public class PreviousTee extends Tee {
	private String designPath;
	private static final String imagePath = "images/designs/mens/";

	public PreviousTee(String userName, int userId, String designName,
			int teeId, String designPath) {
		super(userName, userId, designName, teeId);
		this.designPath = designPath;
	}

	public PreviousTee() {
	};
	
	public String getDesignPath() {
		return designPath;
	}

	public void setDesignPath(String designPath) {
		this.designPath = designPath;
	}

	public String getImagePath() {
		return imagePath + teeId + imageEnding;
	}
}
