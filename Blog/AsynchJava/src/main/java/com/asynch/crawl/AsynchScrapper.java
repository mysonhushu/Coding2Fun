package com.asynch.crawl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.asynch.common.Article;
import com.asynch.common.Result;
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
		CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
			try {
				return getPageSource(urlList.get(0));
			} catch (Exception e) {
				return null;
			}
		});
		CompletableFuture<Article> future2 = future1.thenApply(value -> {
			if(value != null){
				return fetchArticle(value);
			}else{
				return null;
			}
		});
		CompletableFuture<Result> future3 = future2.thenApplyAsync(article -> {
			if(article != null){
				return getResult(article);
			}else{
				return null;
			}			
		});	
		
	}

}
