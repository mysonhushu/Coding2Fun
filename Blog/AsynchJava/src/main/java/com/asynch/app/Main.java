package com.asynch.app;

import com.asynch.crawl.AkkaStreamScrapper;
import com.asynch.crawl.AsynchScrapper;
import com.asynch.crawl.FutureScrapper;
import com.asynch.crawl.ParallelScrapper;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kkishore on 8/4/16.
 */
public class Main {

    public static void main(String[] ags) throws InterruptedException, IOException {
        final String file = "Links.txt";


        /*ExecutorService executor = Executors.newFixedThreadPool(20);
        final AkkaStreamScrapper akkaScrapper = new AkkaStreamScrapper(file, executor);
        akkaScrapper.process();*/



        ExecutorService executor = Executors.newFixedThreadPool(30);
        final AsynchScrapper asyncScrapper = new AsynchScrapper(file, executor);
        asyncScrapper.process();


        final ParallelScrapper parallelScrapper = new ParallelScrapper(file);
        parallelScrapper.process();


        final FutureScrapper futureScrapper = new FutureScrapper(file, executor);
        futureScrapper.process();
        executor.shutdown();


        System.out.println("\n\n\n");
        System.out.println(asyncScrapper.getTime());
        System.out.println(parallelScrapper.getTime());
        System.out.println(futureScrapper.getTime());

    }
}
