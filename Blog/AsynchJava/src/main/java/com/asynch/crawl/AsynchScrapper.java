package com.asynch.crawl;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
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

	public AsynchScrapper(final String urlFile, final Executor executor)
			throws IOException {
		this.urlList = CommonUtils.getLinks(urlFile);
		this.executor = executor;
	}

	@Override
	public void process() {

		//Sol - 1
		Stream<CompletableFuture<Article>> stream = urlList
				.stream()
				.map(url -> CompletableFuture.supplyAsync(
						() -> getPageSource(url), executor))
				.map(future -> future.thenApply(pageSource -> {
					final Article article = fetchArticle(pageSource);
					return article;
				}));
		List<CompletableFuture<Article>> collect = stream.collect(Collectors
				.toList());

		collect.stream().forEach(
				future -> future.whenComplete((result, error) -> {
					System.out.println(result);
				}));

		//Sol - 2
		/*for (final String url : urlList) {
		CompletableFuture.supplyAsync(() -> getPageSource(url), executor)
				.thenApply(pageSource -> {
					final Article article = fetchArticle(pageSource);
					return article;
				}).whenComplete((result, error) -> {
					System.out.println(url + " - " + result);
				});
		}*/
	}

	/*public static void main(String[] args) throws IOException,
			InterruptedException {
		final ExecutorService executor = Executors.newFixedThreadPool(50);
		final String urlFile = "Links1.txt";
		final AsynchScrapper scrapper = new AsynchScrapper(urlFile, executor);
		Instant start = Instant.now();
		scrapper.process();
		Instant stop = Instant.now();
		System.out.println(Duration.between(start, stop));
		executor.shutdown();		
	}*/

}
