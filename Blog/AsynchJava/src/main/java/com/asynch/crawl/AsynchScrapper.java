package com.asynch.crawl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.asynch.common.Article;
import com.asynch.util.CommonUtils;

public class AsynchScrapper extends CommonScrapper {

	private final List<String> urlList;
	private final Executor executor;

	public AsynchScrapper(final String urlFile, final Executor executor) throws IOException {
		this.urlList = CommonUtils.getLinks(urlFile);
		this.executor = executor;
	}

	@Override
	public void process() {
		List<CompletableFuture<Article>> collect = urlList
				.stream()
					.map(url -> CompletableFuture.supplyAsync(() -> getPageSource(url), executor)
												 .thenApply(pageSource -> fetchArticle(pageSource)))
					.collect(Collectors.toList());
		collect.stream().forEach(future -> future.whenComplete((result, error) -> {
			System.out.println(result.getCleanContent());
		}));
		
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		final ExecutorService executor = Executors.newFixedThreadPool(50);
		final String urlFile = "Links.txt";
		final AsynchScrapper scrapper = new AsynchScrapper(urlFile, executor);
		scrapper.process();
		Thread.sleep(10000);
		executor.shutdown();
	}

}
