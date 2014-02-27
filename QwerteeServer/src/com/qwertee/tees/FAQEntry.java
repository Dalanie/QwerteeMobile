package com.qwertee.tees;

public class FAQEntry {
	public String title;
	public String answer;

	public FAQEntry(String title, String answer) {
		this.title = title;
		this.answer = answer;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
