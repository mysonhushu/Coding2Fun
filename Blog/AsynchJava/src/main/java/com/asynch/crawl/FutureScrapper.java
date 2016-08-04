package com.asynch.crawl;

import com.asynch.common.Article;
import com.asynch.common.Result;
import com.asynch.common.Tuple;
import com.asynch.util.CommonUtils;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class FutureScrapper extends CommonScrapper {

    private final List<String> urlList;
    private final ExecutorService executor;
    private LocalDateTime time1, time2;

    public FutureScrapper(final String urlFile, final ExecutorService executor) throws IOException {
        this.urlList = CommonUtils.getLinks(urlFile);
        this.executor = executor;
    }

    @Override
    public void process() {
        time1 = LocalDateTime.now();
        final List<Future<Result>> futureList = new ArrayList<>(10);
        for (final String url : urlList) {
            futureList.add(executor.submit(invokeCallable(url)));
        }

        int i = 0;
        while (i < futureList.size()) {
            final Future<Result> resultFuture = futureList.get(i);
            try {
                System.out.println(resultFuture.get());
                i++;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }
        time2 = LocalDateTime.now();
    }

    private Callable<Result> invokeCallable(final String url) {
        return () -> {
            final Tuple tuple = getPageSource(url);
            final Article article = fetchArticle(tuple);
            return getResult(article);
        };
    }

    public long getTime(){
        return Duration.between(time1, time2).getSeconds();
    }

}
