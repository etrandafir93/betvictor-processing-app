package ro.etr.victorbet.processingapp.service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ro.etr.victorbet.processingapp.dto.ProcessedTextDto;
import ro.etr.victorbet.processingapp.service.nlp.PresenceCounter;

@Service
public class TextProcessingService {

	@Autowired
    @Qualifier("taskExecutor")
	private ExecutorService threadPool;

	public ProcessedTextDto process(ProcessRequestParams requestParams) throws IOException, InterruptedException {

		PresenceCounter<String> bagOfWords = new PresenceCounter<>();
		PresenceCounter<Integer> paragraphSizes = new PresenceCounter<>();

		long startProcessingTimestamp = System.currentTimeMillis();
		
		CompletableFuture<?>[] tasks = computeAndSync(requestParams, bagOfWords, paragraphSizes);
		
		long totalProcessingTime = System.currentTimeMillis() - startProcessingTimestamp;
		
		return ProcessedTextDto.builder()
			.mostFrequentWord( getMostFrequentWord( bagOfWords ) )
			.totalProcessingTimeInMllis( totalProcessingTime )
			.avgProcessingTimeInMillis( totalProcessingTime / tasks.length )
			.avgParagraphSize( getNumberOfWords( paragraphSizes ) )
			.build();
	}

	private CompletableFuture<?>[] computeAndSync(ProcessRequestParams requestParams, 
												PresenceCounter<String> bagOfWords,
												PresenceCounter<Integer> paragraphSizes) 
	{
		CompletableFuture<?>[] tasks = IntStream
			.range(requestParams.getStartParagraph(), requestParams.getEndParagraph() + 1).boxed()
			.map(index -> new ProcessRequestRunnable(index, requestParams, paragraphSizes, bagOfWords))
			.map(runnable -> CompletableFuture.runAsync(runnable, threadPool))
			.toArray(CompletableFuture[]::new);

		CompletableFuture.allOf(tasks).join();
		return tasks;
	}

	private String getMostFrequentWord(PresenceCounter<String> bagOfWords) {
		return bagOfWords.getEntrySet().stream()
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
