package ro.etr.victorbet.processingapp.service;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import ro.etr.victorbet.processingapp.dto.ProcessedTextDto;
import ro.etr.victorbet.processingapp.service.nlp.PresenceCounter;

@Service
public class TextProcessingService {

	@Autowired
	private ApplicationContext context;

	public String process(ProcessRequestParams requestParams) throws IOException, InterruptedException {

		PresenceCounter<String> bagOfWords = new PresenceCounter<>();
		PresenceCounter<Integer> paragraphSizes = new PresenceCounter<>();

		ExecutorService threadPool = (ExecutorService) context.getBean("taskExecutor");

		long startProcessingTimestamp = new Date().getTime();
		
		CompletableFuture<?>[] tasks = IntStream
				.range(requestParams.getStartParagraph(), requestParams.getEndParagraph() + 1).boxed()
				.map(index -> new ProcessRequestRunnable(index, requestParams, paragraphSizes, bagOfWords))
				.map(runnable -> CompletableFuture.runAsync(runnable, threadPool))
				.toArray(CompletableFuture[]::new);

		CompletableFuture.allOf(tasks).join();

		long endProcessingTimestamp = new Date().getTime();
		long totalProcessingTime = endProcessingTimestamp - startProcessingTimestamp;
		
		ProcessedTextDto dto = ProcessedTextDto.builder()
			.mostFrequentWord( getMostFrequentWord( bagOfWords ) )
			.totalProcessingTimeInMllis( totalProcessingTime )
			.avgProcessingTimeInMillis( totalProcessingTime / tasks.length )
			.avgParagraphSize( getNumberOfWords( paragraphSizes ) )
			.build();

		return new Gson().toJson( dto );

	}

	private String getMostFrequentWord(PresenceCounter<String> bagOfWords) {
		return bagOfWords.getEntrySet()
			.stream()
			.max( PresenceCounter.compareEntriesByPresence )
			.get()
			.getKey();
	}

	private int getNumberOfWords(PresenceCounter<Integer> paragraphSizes) {
		
		int nrOfParagraphs = paragraphSizes.getEntrySet().stream()
			.map( entry -> entry.getValue().get() )
			.mapToInt(Integer :: intValue)
			.sum();
		
		int nrOfWords = paragraphSizes.getEntrySet().stream()
			.map( entry -> entry.getKey() * entry.getValue().get() )
			.mapToInt(Integer :: intValue)
			.sum();
		
		return nrOfWords / nrOfParagraphs;
	}

}
