package ro.etr.victorbet.processingapp.service;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import ro.etr.victorbet.processingapp.exceptions.RandomTextServerException;
import ro.etr.victorbet.processingapp.exceptions.Warning;
import ro.etr.victorbet.processingapp.infrastructure.randomtext.RandomTextClient;
import ro.etr.victorbet.processingapp.infrastructure.randomtext.RandomTextResponse;
import ro.etr.victorbet.processingapp.service.nlp.NaturalLanguageProcessor;
import ro.etr.victorbet.processingapp.service.nlp.PresenceCounter;

@Slf4j
@Builder
@AllArgsConstructor
public class ProcessRequestRunnable implements Runnable {

	private int index;
	private Set<Warning> warnings;
	private ProcessRequestParams requestParams;
	private PresenceCounter<Integer> avgParagraph;
	private PresenceCounter<String> bagOfWords;
	private String url;

	@Override
	public void run() {
		
		try {
			RandomTextResponse response = new RandomTextClient(url).requestData(index, requestParams);
			NaturalLanguageProcessor nlp = new NaturalLanguageProcessor();
			
			nlp.process(response);
			
			bagOfWords.merge( nlp.getBagOfWords() );
			avgParagraph.merge( nlp.getAvgParagraphSize() );
			
		} catch (Exception e) 
		{
			String msg = e instanceof RandomTextServerException? "bad respone from the external service!" : "internal error trying to send the request!";
			log.error(msg);
			warnings.add( new Warning(msg) );
		}
	}

}
