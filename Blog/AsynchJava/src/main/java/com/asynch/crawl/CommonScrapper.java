package com.asynch.crawl;

import org.jsoup.Jsoup;

import com.asynch.common.Article;
import com.asynch.common.Result;
import com.asynch.interfaces.IScrapper;

public abstract class CommonScrapper implements IScrapper{
	
	public String getPageSource(final String url) throws Exception {
		try {
			final String html = Jsoup.connect(url).get().html();
			return html;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Article fetchArticle(String pageSource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result getResult(Article article) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public abstract void process(); 

}
