package com.asynch.interfaces;

import com.asynch.common.Article;
import com.asynch.common.Result;
import com.asynch.util.Tuple;

public interface IScrapper {
	
	public void process();
	
	public Tuple<String, String> getPageSource(final String url) throws Exception;
	
	public Article fetchArticle(final Tuple<String, String> tuple);
	
	public Result getResult(final Article article);

}
