package com.asynch.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.asynch.common.Article;
import com.asynch.common.Result;
import com.asynch.interfaces.IScrapper;
import com.asynch.util.CommonConstants;
import com.asynch.util.Tuple;

public abstract class CommonScrapper implements IScrapper {

	public Tuple getPageSource(final String url) {
		try {
			final String html = Jsoup.connect(url).timeout(70000).get().html();
			return new Tuple(url, html);
		} catch (Exception e) {
			return new Tuple(url, CommonConstants.DUMMY_PAGE_SOURCE);
		}
	}

	@Override
	public Article fetchArticle(final Tuple tuple) {
		final Article article = new Article();
		final Document document = Jsoup.parse(tuple.get_2());
		article.setUrl(tuple.get_1());
		article.setRawHtml(tuple.get_2());
		article.setCleanContent(document.text());
		article.setTitle(document.title());
		return article;		
	}

	@Override
	public Result getResult(Article article) {
		return new Result(article);
	}

	@Override
	public abstract void process();

}
