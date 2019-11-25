import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.Page;


public class Scraper {
	//Note: Facebook will only allow you to scrape 250 pages unless a business-
	//level access token is used.
	final static String accessToken = "yourAccessTokenHere";
	
	public static void main(String[] args) throws Exception {
		Scanner scan = new Scanner(System.in);
		//user is given the option of rescraping a single page or the entire archive.
		System.out.println("1. Rescrape single page\n2. Rescrape entire news archive\n-1. To quit");
		int select = scan.nextInt();
		
		//scrapes data from single URL using "updatePage()" method
			if(select == 1) {
				System.out.println("Please enter the link of the page you'd like to rescrape:");
				String target = scan.next();
				updatePage(target);
			} 
		//scrapes data from archive of links using JSoup & "updatePages()" method.
			else if(select == 2) {
				ArrayList <String> links = new ArrayList<String>();
				for(int i = 0; i < 47; i++) {
					String url = "https://umfoundation.com/main/news-archive/?offset=" + (i + 1);
					final Document document = Jsoup.connect(url).get();
					int counter = 1;
					for(Element row: document.select(
							"div.additional-grid a")) {
						if(counter % 2 == 0) {
						links.add(row.attr("href"));
						
						}
						counter++;
					}
					System.out.println("Capturing https://umfoundation.com/main/news-archive/?offset=" + (i + 1));
				}
				System.out.println("Link Capturing Complete!");
				updatePages(links);
			} 
			else if(select == -1) {
				System.out.println("Goodbye.");
			} 
			else {
				System.out.println("Please enter one of the designated numerical values.");
			}
		
		
		
		

		
	
	}
	//Facebook API used to rescrape pages
	
	public static void updatePage(String url) {
		FacebookClient fbClient = new DefaultFacebookClient(accessToken, Version.LATEST);
		System.out.println("Scraping...");
		Page page = fbClient.fetchObject(url, Page.class, Parameter.with("scrape", true));
		System.out.println("Scraping Complete!");
	}
	
	public static void updatePages(ArrayList<String> list) {
		FacebookClient fbClient = new DefaultFacebookClient(accessToken, Version.LATEST);
		System.out.println("Scraping...");
		for(int i = 0; i < list.size(); i++) {
			System.out.println("Scraping " + list.get(i));
			Page page = fbClient.fetchObject(list.get(i), Page.class, Parameter.with("scrape", true));
		}
		System.out.println("Scraping Complete!");
		
	}
	
	
}
