package ro.etr.victorbet.processingapp.service;

import lombok.AllArgsConstructor;
import ro.etr.victorbet.processingapp.infrastructure.RandomTextClient;
import ro.etr.victorbet.processingapp.infrastructure.RandomTextResponse;
import ro.etr.victorbet.processingapp.service.nlp.BagOfWords;
import ro.etr.victorbet.processingapp.service.nlp.NaturalLanguageProcessor;

@AllArgsConstructor
public class ProcessRequestRunnable implements Runnable {
	
	private int index;
	private ProcessRequestParams requestParams;
	private BagOfWords bagOfWords;

	@Override
	public void run() {
		RandomTextResponse response = new RandomTextClient().requestData(index, requestParams);
		BagOfWords newWords = new NaturalLanguageProcessor().process( response );
		bagOfWords.merge( newWords );
	}

}
