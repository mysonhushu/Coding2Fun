package com.asynch.interfaces;

import com.asynch.common.Article;
import com.asynch.common.Result;

public interface IScrapper {
	
	public Article fetchArticle(final String url);
	
	public Result getResult(final Article article);

}
