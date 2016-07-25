import java.io.IOException;

import org.jsoup.Jsoup;


public class JsoupTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		final String url = "http://indianexpress.com/article/world/world-news/america-its-july-theres-already-been-250-mass-shootings-twitter-reacts-to-florida-nightclub-shooting-2934610/";
		final String content = Jsoup.connect(url).get().html();
		System.out.println(content);
	}

}
