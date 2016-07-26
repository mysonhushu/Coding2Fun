package com.asynch.interfaces;

import com.asynch.common.Article;
import com.asynch.common.Result;

public interface IScrapper {
	
	public void process();
	
	public Article fetchArticle(final String pageSource);
	
	public Result getResult(final Article article);

}
