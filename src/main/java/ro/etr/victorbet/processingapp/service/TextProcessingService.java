package ro.etr.victorbet.processingapp.service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import ro.etr.victorbet.processingapp.service.nlp.AvgParagraphSize;
import ro.etr.victorbet.processingapp.service.nlp.BagOfWords;

@Service
public class TextProcessingService {

	@Autowired
	private ApplicationContext context;

	public String process(ProcessRequestParams requestParams) throws IOException, InterruptedException {

		BagOfWords bagOfWords = new BagOfWords();
		AvgParagraphSize avgParagraph = new AvgParagraphSize();

		ExecutorService threadPool = (ExecutorService) context.getBean("taskExecutor");

		CompletableFuture<?>[] futures = IntStream
				.range(requestParams.getStartParagraph(), requestParams.getEndParagraph() + 1).boxed()
				.map(index -> new ProcessRequestRunnable(index, requestParams, bagOfWords, avgParagraph))
				.map(runnable -> CompletableFuture.runAsync(runnable, threadPool)).toArray(CompletableFuture[]::new);

		CompletableFuture.allOf(futures).join();

		return new Gson().toJson(bagOfWords);

	}

}
