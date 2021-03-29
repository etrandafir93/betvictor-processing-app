package ro.etr.betvictor.processingapp.service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ro.etr.betvictor.processingapp.config.Config;
import ro.etr.betvictor.processingapp.dto.ProcessedTextDto;
import ro.etr.betvictor.processingapp.exceptions.Warning;
import ro.etr.betvictor.processingapp.infrastructure.repositoryapp.RepoAppClient;
import ro.etr.betvictor.processingapp.service.nlp.PresenceCounter;

@Service
public class TextProcessingService {

	@Autowired
    @Qualifier("taskExecutor")
	private ExecutorService threadPool;

	@Autowired
	private Config config;
	
	@Autowired
	private RepoAppClient repoAppClient;

	public ProcessedTextDto process(ProcessRequestParams requestParams) {

		PresenceCounter<String> bagOfWords = new PresenceCounter<>();
		PresenceCounter<Integer> paragraphSizes = new PresenceCounter<>();
		Set<Warning> warnings = new HashSet<>();
		
		long startProcessingTimestamp = System.currentTimeMillis();
		
		computeAndSync(requestParams, bagOfWords, paragraphSizes, warnings);
		
		long totalProcessingTime = System.currentTimeMillis() - startProcessingTimestamp;
		
		ProcessedTextDto dto = ProcessedTextDto.builder()
			.mostFrequentWord( getMostFrequentWord( bagOfWords ) )
			.totalProcessingTimeInMllis( totalProcessingTime )
			.avgProcessingTimeInMillis( 1.0f * totalProcessingTime / getNumberOfParagraphs(paragraphSizes) )
			.avgParagraphSize( getNumberOfWords( paragraphSizes ) )
			.warnings( warnings )
			.build();
		
		repoAppClient.send(dto);
		
		return dto;
	}

	private void computeAndSync(ProcessRequestParams requestParams, 
													PresenceCounter<String> bagOfWords,
													PresenceCounter<Integer> paragraphSizes,
													Set<Warning> warnings ) 
	{
		CompletableFuture<?>[] tasks = IntStream.range(requestParams.getStartParagraph(), requestParams.getEndParagraph() + 1)
			.boxed() 
			.map(index -> ProcessRequestRunnable.builder()
					.index(index)
					.requestParams(requestParams)
					.avgParagraph(paragraphSizes)
					.bagOfWords(bagOfWords)
					.url(config.getRandomTextApiUrl())
					.warnings(warnings)
					.build() ) 
			.map(runnable -> CompletableFuture.runAsync(runnable, threadPool))
			.toArray(CompletableFuture[]::new);

		CompletableFuture.allOf(tasks).join();
	}

	private String getMostFrequentWord(PresenceCounter<String> bagOfWords) {
		return bagOfWords.getEntrySet().stream()
			.max( PresenceCounter.compareEntriesByPresence )
			.get()
			.getKey();
	}

	private float getNumberOfWords(PresenceCounter<Integer> paragraphSizes) {
		
		int nrOfWords = paragraphSizes.getEntrySet().stream()
			.map( entry -> entry.getKey() * entry.getValue().get() )
			.mapToInt(Integer :: intValue)
			.sum();
		
		return 1.0f * nrOfWords / getNumberOfParagraphs(paragraphSizes);
	}

	private int getNumberOfParagraphs(PresenceCounter<Integer> paragraphSizes) {
		return paragraphSizes.getEntrySet().stream()
			.map( entry -> entry.getValue().get() )
			.mapToInt(Integer :: intValue)
			.sum();
	}

}
