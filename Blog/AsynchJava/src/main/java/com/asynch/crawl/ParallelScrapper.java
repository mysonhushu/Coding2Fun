package com.asynch.crawl;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.asynch.util.CommonUtils;

public class ParallelScrapper extends CommonScrapper {

	private final List<String> urlList;
    private LocalDateTime time1, time2;

	public ParallelScrapper(final String urlFile) throws IOException {
		this.urlList = CommonUtils.getLinks(urlFile);
	}

	@Override
	public void process() {
        time1 = LocalDateTime.now();
		urlList
				.stream()
					.parallel()
					.map(url -> getPageSource(url))
					.map(pageSource -> fetchArticle(pageSource))
					.map(article -> getResult(article))
					.forEach(System.out::println);
        time2 = LocalDateTime.now();
	}

    public long getTime(){
        return Duration.between(time1, time2).getSeconds();
    }
}
