package com.asynch.interfaces;
import java.util.concurrent.CompletableFuture;

public interface IAsynchScrapper {
	
	public CompletableFuture<String> getAsynchPageSource(final String url);

}
