package com.asynch.crawl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.asynch.common.Result;
import com.asynch.util.CommonUtils;

public class AsynchScrapper extends CommonScrapper {

	private final List<String> urlList;
	private final ExecutorService executor;

	public AsynchScrapper(final String urlFile, final ExecutorService executor)
			throws IOException {
		this.urlList = CommonUtils.getLinks(urlFile);
		this.executor = executor;
	}

	@Override
	public void process() {

		// Sol - 1
		final Stream<CompletableFuture<Result>> stream = urlList
				.stream()
				.map(url -> CompletableFuture.supplyAsync(() -> getPageSource(url), executor))
				.map(future -> future.thenApply(pageSource -> fetchArticle(pageSource))
									 .thenApply(article -> getResult(article)));
		final List<CompletableFuture<Result>> collect = stream.collect(Collectors.toList());
		collect.stream().forEach(future -> future.whenComplete((result, error) -> System.out.println(result)));

		// Sol - 2
		/*
		 * for (final String url : urlList) { CompletableFuture.supplyAsync(()
		 * -> getPageSource(url), executor) .thenApply(pageSource -> { final
		 * Article article = fetchArticle(pageSource); return article;
		 * }).whenComplete((result, error) -> { System.out.println(url + " - " +
		 * result); }); }
		 */
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		final ExecutorService executor = Executors.newFixedThreadPool(30);
		final String urlFile = "Links.txt";
		final AsynchScrapper scrapper = new AsynchScrapper(urlFile, executor);
		scrapper.process();
		executor.shutdown();
	}

}
