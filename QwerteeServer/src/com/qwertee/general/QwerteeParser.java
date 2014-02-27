package com.qwertee.general;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.qwertee.tees.Category;
import com.qwertee.tees.FAQEntry;
import com.qwertee.tees.LastChanceTee;
import com.qwertee.tees.PreviousTee;
import com.qwertee.tees.Tee;
import com.qwertee.tees.TodaysTee;
import com.qwertee.tees.VoteTee;

public class QwerteeParser {

	public static List<PreviousTee> parseResponseToPrevious(String response) {
		List<PreviousTee> previous = new ArrayList<PreviousTee>();
		Document htmlDocument = Jsoup.parse(response); // kind of caching
		Elements elems = htmlDocument.getElementsByClass("design-item");
		for (int i = 0; i < elems.size(); i++) {
			Element element = elems.get(i);
			previous.add(parseElementToPrevious(element));
		}
		return previous;
	}

	public static PreviousTee parseElementToPrevious(Element element) {
		String[] imagePathSplit = element.getElementsByClass("design")
				.select("img").attr("data-img").split("/");
		int teeId = Integer.parseInt(imagePathSplit[imagePathSplit.length - 1]
				.replace(".jpg", ""));

		String[] userPathSplit = element.getElementsByClass("user")
				.attr("href").split("/");
		int userId = Integer.parseInt(userPathSplit[userPathSplit.length - 1]);
		String designPath = element.getElementsByClass("design-name").first()
				.attr("href");
		String designName = element.getElementsByClass("design-name").text();
		String userName = element.getElementsByClass("user").text();

		PreviousTee vote = new PreviousTee(userName, userId, designName, teeId,
				designPath);
		return vote;
	}

	public static List<VoteTee> parseResponseToVotes(String response) {
		List<VoteTee> votes = new ArrayList<VoteTee>();
		Document htmlDocument = Jsoup.parse(response); // kind of caching
		Elements elems = htmlDocument
				.getElementsByClass("design-image-wrap-with-vote");
		for (int i = 0; i < elems.size(); i++) {
			Element element = elems.get(i);
			votes.add(parseElementToVote(element));
		}
		return votes;
	}

	public static VoteTee parseElementToVote(Element element) {
		String[] imagePathSplit = element.getElementsByClass("design")
				.select("img").attr("data-img").split("/");
		int teeId = Integer.parseInt(imagePathSplit[imagePathSplit.length - 1]
				.replace(".jpg", ""));

		String[] userPathSplit = element.getElementsByClass("user")
				.attr("href").split("/");
		int userId = Integer.parseInt(userPathSplit[userPathSplit.length - 1]);
		String designPath = element.getElementsByClass("design-name").first()
				.attr("href");
		String designName = element.getElementsByClass("design-name").text();

		String userName = element.getElementsByClass("user").text();
		String votes = element.getElementsByClass("votes-count").text();
		String comments = element.getElementsByClass("comments").text();
		int numberOfVotes = Integer.parseInt(votes);
		int numberOfComments = Integer.parseInt(comments);

		VoteTee vote = new VoteTee(userName, userId, designName, teeId,
				numberOfVotes, numberOfComments, designPath);
		return vote;
	}

	public static List<Tee> parseResponseToBuy(String response) {
		Document htmlDocument = Jsoup.parse(response); // kind of caching
		TodaysTee todaysTee = parseTodaysTee(htmlDocument);

		// TODO prices
		LastChanceTee lastChanceTee = parseLastChanceTee(htmlDocument);

		List<Tee> tees = new ArrayList<Tee>();
		tees.add(todaysTee);
		tees.add(lastChanceTee);

		return tees;
	}

	private static TodaysTee parseTodaysTee(Document htmlDocument) {
		Element currentElem = htmlDocument.getElementsByClass("index-current")
				.first();
		String designName = currentElem.getElementsByClass("raleway").text();
		String userName = currentElem.getElementsByClass("lora").text();
		String[] userPathSplit = currentElem.getElementsByClass("lora")
				.select("a").attr("href").split("/");
		int userId = Integer.parseInt(userPathSplit[userPathSplit.length - 1]);

		String[] imagePathSplit = currentElem.getElementsByClass("buy")
				.select("img").attr("src").split("/");
		int teeId = Integer.parseInt(imagePathSplit[imagePathSplit.length - 1]
				.replace(".jpg", ""));
		return new TodaysTee(userName, userId, designName, teeId);
	}

	private static LastChanceTee parseLastChanceTee(Document htmlDocument) {
		Element currentElem = htmlDocument.getElementsByClass(
				"index-lastchance").first();
		String designName = currentElem.getElementsByClass("raleway").text();
		String userName = currentElem.getElementsByClass("lora").text();
		String[] userPathSplit = currentElem.getElementsByClass("lora")
				.select("a").attr("href").split("/");
		int userId = Integer.parseInt(userPathSplit[userPathSplit.length - 1]);

		String[] imagePathSplit = currentElem.getElementsByClass("buy")
				.select("img").attr("src").split("/");
		int teeId = Integer.parseInt(imagePathSplit[imagePathSplit.length - 1]
				.replace(".jpg", ""));
		return new LastChanceTee(userName, userId, designName, teeId);
	}

	public static List<Category> parseFAQ(String response) {
		Document htmlDocument = Jsoup.parse(response); // kind of caching
		Map<String, Category> categories = new HashMap<String, Category>();

		Elements elems = htmlDocument.getElementsByClass("faq-tab");
		for (int i = 0; i < elems.size(); i++) {
			Element element = elems.get(i);
			categories.put(getHref(element), new Category(getTitle(element),
					new ArrayList<FAQEntry>()));
		}
		Elements contentTabs = htmlDocument
				.getElementsByClass("faq-tab-content");
		for (int i = 0; i < contentTabs.size(); i++) {
			Element element = contentTabs.get(i);
			Category tempCategory = categories.get(element.attr("id"));
			Elements headers = element.select("h3");
			Elements contents = element.select("div");
			contents.remove(0);

			for (int j = 0; j < headers.size(); j++) {
				String content = contents.get(j).toString()
						.replace("<div>", "").replace("</div>", "");
				Element header = headers.get(j);
				FAQEntry entry = new FAQEntry(header.text(), content.trim());
				tempCategory.getEntries().add(entry);
			}
		}

		return new ArrayList<Category>(categories.values());
	}

	private static String getTitle(Element element) {
		return element.select("a").text();
	}

	private static String getHref(Element element) {
		return element.select("a").attr("href").substring(1);
	}

	public static String getHTML(String stringUrl) throws IOException {
		URL url = new URL(stringUrl);
		URLConnection spoof = url.openConnection();

		// Spoof the connection so we look like a web browser
		spoof.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0;    H010818)");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				spoof.getInputStream()));
		String strLine = "";
		String finalHTML = "";
		// Loop through every line in the source
		while ((strLine = in.readLine()) != null) {
			finalHTML += strLine;
		}
		return finalHTML;
	}
}
