package com.asynch.crawl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.asynch.interfaces.IProcessor;
import com.asynch.util.CommonUtils;

public class AsynchScrapper extends CommonScrapper implements IProcessor {

	private final List<String> urlList;
	private final Executor executor;

	public AsynchScrapper(final String urlFile, final Executor executor) throws IOException {
		this.urlList = CommonUtils.getLinks(urlFile);
		this.executor = executor;
	}

	@Override
	public void process() {
		urlList.stream()
				.map(url -> CompletableFuture.supplyAsync(() -> fetchArticle(url), executor))
				.map(future -> future.thenApply(article -> getResult(article)));
	}

}
