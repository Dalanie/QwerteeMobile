package com.qwertee.network;

import java.io.IOException;
import java.util.List;

import com.qwertee.general.QwerteeParser;
import com.qwertee.tees.Category;
import com.qwertee.tees.PreviousTee;
import com.qwertee.tees.Tee;
import com.qwertee.tees.VoteTee;

public class Network {

	static List<PreviousTee> previousTees;
	static List<Tee> buyTees;
	static List<Category> faqs;
	static List<VoteTee> voteTees;

	private static List<PreviousTee> parsePreviousTees() {
		String response = "";
		try {
			response = QwerteeParser
					.getHTML("http://www.qwertee.com/tees/previous");
			return QwerteeParser.parseResponseToPrevious(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static List<Tee> parseBuyTees() {
		String response = "";
		try {
			response = QwerteeParser.getHTML("http://www.qwertee.com/");
			return QwerteeParser.parseResponseToBuy(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static List<Category> parseFAQ() {
		String response = "";
		try {
			response = QwerteeParser.getHTML("http://www.qwertee.com/help");
			return QwerteeParser.parseFAQ(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static List<VoteTee> parseVoteTees() {
		String response = "";
		try {
			response = QwerteeParser
					.getHTML("http://www.qwertee.com/tees/vote/newest");
			return QwerteeParser.parseResponseToVotes(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<Tee> getBuyTees() {
		return buyTees;
	}

	public static List<Category> getFAQ() {
		return faqs;
	}

	public static List<PreviousTee> getPreviousTees() {
		return previousTees;
	}

	public static List<VoteTee> getVoteTees() {
		return voteTees;
	}

	public static void init() {
		previousTees = parsePreviousTees();
		buyTees = parseBuyTees();
		faqs = parseFAQ();
		voteTees = parseVoteTees();
	}

	public static Tee getTodayTee() {
		return buyTees.get(0);
	}

	public static Tee getLastChanceTee() {
		return buyTees.get(1);
	}
}
