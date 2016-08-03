package com.asynch.crawl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.asynch.util.CommonUtils;

public class ParallelScrapper extends CommonScrapper {

	private final List<String> urlList;

	public ParallelScrapper(final String urlFile) throws IOException {
		this.urlList = CommonUtils.getLinks(urlFile);
	}

	@Override
	public void process() {
		urlList
				.stream()
					.parallel()
					.map(url -> getPageSource(url))
					.map(pageSource -> fetchArticle(pageSource))
					.map(article -> getResult(article))
					.forEach(System.out::println);
	}

	public static void main(String[] args) throws IOException {
		System.out.println(new Date());
		final String file = "Links.txt";
		final ParallelScrapper parallelScrapper = new ParallelScrapper(file);
		parallelScrapper.process();
		System.out.println(new Date());
	}

}
