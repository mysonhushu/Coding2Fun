package com.asynch.interfaces;

import com.asynch.common.Article;
import com.asynch.common.Result;

public interface IScrapper {
	
	public void process();
	
	public String getPageSource(final String url)throws Exception;
	
	public Article fetchArticle(final String pageSource);
	
	public Result getResult(final Article article);

}
