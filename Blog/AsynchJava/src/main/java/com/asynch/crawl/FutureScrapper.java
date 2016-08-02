package com.asynch.crawl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.asynch.common.Article;
import com.asynch.common.Result;
import com.asynch.common.Tuple;
import com.asynch.util.CommonUtils;

public class FutureScrapper extends CommonScrapper{
	
	private final List<String> urlList;
	private final ExecutorService executor;
	
	public FutureScrapper(final String urlFile, final ExecutorService executor) throws IOException {
		this.urlList = CommonUtils.getLinks(urlFile);
		this.executor = executor;
	}
	
	@Override
	public void process() {
		final List<Future<Result>> futureList = new ArrayList<>(10);
		for(final String url : urlList){
			futureList.add(executor.submit(invokeCallable(url)));
		}
		int i = 0;
		while(i < futureList.size()){
			if(futureList.isEmpty()){
				break;
			}else{
				final Future<Result> resultFuture = futureList.get(i);
				try {
					if(resultFuture.isDone()){					
						System.out.println(resultFuture.get());
						futureList.remove(i);
					}else{
						Thread.sleep(100);
						i = i+1;
						continue;
					}
				} catch (InterruptedException | ExecutionException e) {					
					e.printStackTrace();
				}
			}
		}		
	}
	
	private Callable<Result> invokeCallable(final String url){
		return new Callable<Result>() {
			@Override
			public Result call() throws Exception {
				final Tuple tuple = getPageSource(url);
				final Article article = fetchArticle(tuple);					
				return getResult(article);
			}
			
		};
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		final ExecutorService executor = Executors.newFixedThreadPool(30);
		final String urlFile = "Links.txt";
		final FutureScrapper scrapper = new FutureScrapper(urlFile, executor);
		scrapper.process();
		executor.shutdown();
	}

}
