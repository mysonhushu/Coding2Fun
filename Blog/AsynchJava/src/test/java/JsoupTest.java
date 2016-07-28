import java.io.IOException;

import org.jsoup.Jsoup;


public class JsoupTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/*Stream<String> urlList = Files.lines(Paths.get("Links1.txt"));
		urlList.forEach(url -> {
			try {
				final String html = Jsoup.connect(url).timeout(60000).get().html();
				final String heading = Jsoup.parse(html).title();
				System.out.println(heading);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		urlList.close();*/
		final String url = "http://www.foxsports.com.au/cricket/new-zealand-cricket-team-duck-for-cover-after-bizarre-bee-attack-in-zimbabwe/news-story/774463881f2d639562f0c7d0c26d1dce";
		System.out.println(Jsoup.connect(url).timeout(70000).get().title());
		
	}

}
