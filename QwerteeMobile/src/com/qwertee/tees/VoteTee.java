package com.qwertee.tees;

public class VoteTee extends Tee {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9073800372854609885L;
	private static final String imagePath = "images/designs/full/";
	private static final String imageThumbPath = "images/designs/thumbs/";
	private String designPath;

	private int numberOfVotes;
	private int numberOfComments;

	public VoteTee(String userName, int userId, String designName, int teeId,
			int numberOfVotes, int numberOfComments, String designPath) {
		super(userName, userId, designName, teeId);
		this.numberOfVotes = numberOfVotes;
		this.numberOfComments = numberOfComments;
		this.designPath = designPath;
	}

	public VoteTee() {
	}

	public String getImagePath() {
		return imagePath + teeId + imageEnding;
	}

	public String getThumbPath() {
		return imageThumbPath + teeId + imageEnding;
	}

	public String getUserPath() {
		return userPath + userId;
	}

	public String getDesignPath() {
		return designPath;
	}

	public String getDesignName() {
		return designName;
	}

	public String getUserName() {
		return userName;
	}

	public int getNumberOfVotes() {
		return numberOfVotes;
	}

	public int getNumberOfComments() {
		return numberOfComments;
	}

}
