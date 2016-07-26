package com.asynch.crawl;

import java.util.concurrent.CompletableFuture;

import org.jsoup.Jsoup;

import com.asynch.common.Article;
import com.asynch.common.Result;
import com.asynch.interfaces.IAsynchScrapper;
import com.asynch.interfaces.IScrapper;
import com.asynch.interfaces.SynchScrapper;

public abstract class CommonScrapper implements IScrapper, IAsynchScrapper, SynchScrapper {

	public CompletableFuture<String> getAsynchPageSource(final String url) {
		CompletableFuture<String> result = null;
		try {
			final String html = Jsoup.connect(url).get().html();
			result = CompletableFuture.completedFuture(html);
		} catch (Exception e) {
			result = new CompletableFuture<>();
			result.completeExceptionally(e);
		}
		return result;
	}
	
	public String getPageSource(final String url)  {
		try {
			final String html = Jsoup.connect(url).timeout(70000).get().html();
			return html;
		} catch (Exception e) {
			return null;
			//throw e;
		}
	}

	@Override
	public Article fetchArticle(String pageSource) {
		// TODO Auto-generated method stub
		final Article article = new Article();
		article.setCleanContent(Jsoup.parse(pageSource).title());
		return article;
	}

	@Override
	public Result getResult(Article article) {
		// TODO Auto-generated method stub
		return new Result(article);
	}

	@Override
	public abstract void process();

}
