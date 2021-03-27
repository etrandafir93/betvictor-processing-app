package ro.etr.victorbet.processingapp.service;

import lombok.AllArgsConstructor;
import ro.etr.victorbet.processingapp.infrastructure.RandomTextClient;
import ro.etr.victorbet.processingapp.infrastructure.RandomTextResponse;
import ro.etr.victorbet.processingapp.service.nlp.NaturalLanguageProcessor;
import ro.etr.victorbet.processingapp.service.nlp.PresenceCounter;

@AllArgsConstructor
public class ProcessRequestRunnable implements Runnable {

	private int index;
	private ProcessRequestParams requestParams;
	private PresenceCounter<Integer> avgParagraph;
	private PresenceCounter<String> bagOfWords;

	@Override
	public void run() {
		
		RandomTextResponse response = new RandomTextClient().requestData(index, requestParams);
		NaturalLanguageProcessor nlp = new NaturalLanguageProcessor();
		
		nlp.process(response);
		
		bagOfWords.merge( nlp.getBagOfWords() );
		avgParagraph.merge( nlp.getAvgParagraphSize() );
	}

}
