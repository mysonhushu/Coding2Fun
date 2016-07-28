package com.asynch.crawl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletionStage;

import com.asynch.common.Result;
import com.asynch.util.CommonUtils;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

/**
 * Created by kkishore on 7/27/16.
 */
public class AkkaStreamScrapper extends CommonScrapper {

	private final List<String> urlList;
	private final ActorSystem actorSystem = ActorSystem.create("AkkaStreamScrapper");
	private final ActorMaterializer actorMaterializer = ActorMaterializer.create(actorSystem);

	public AkkaStreamScrapper(final String file) throws IOException {
		this.urlList = CommonUtils.getLinks(file);
	}

	public void process() {
		Source<String, NotUsed> source = Source.from(urlList);
		Flow<String, Result, NotUsed> stages = Flow
				.of(String.class)
					.map(this::getPageSource).async()
					.map(this::fetchArticle).async()
					.map(this::getResult);
		CompletionStage<Done> graph = source.via(stages).runWith(Sink.foreach(System.out::println),
				actorMaterializer);
		graph.whenComplete((result, error) -> {
			actorSystem.terminate();
		});
	}

	public static void main(String[] ags) throws IOException {
		final AkkaStreamScrapper scrapper = new AkkaStreamScrapper("Links.txt");
		scrapper.process();
	}
}
