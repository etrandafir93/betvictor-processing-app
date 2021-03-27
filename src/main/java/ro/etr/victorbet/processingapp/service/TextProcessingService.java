package ro.etr.victorbet.processingapp.service;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import lombok.Getter;
import ro.etr.victorbet.processingapp.infrastructure.RandomTextClient;
import ro.etr.victorbet.processingapp.infrastructure.RandomTextResponse;
import ro.etr.victorbet.processingapp.service.nlp.BagOfWords;
import ro.etr.victorbet.processingapp.service.nlp.NaturalLanguageProcessor;

@Service
public class TextProcessingService {


    @Autowired
    private NaturalLanguageProcessor responseProcessor;
    
    @Autowired
    private ApplicationContext context;

    @Getter
    private BagOfWords bagOfWords = new BagOfWords();
    
	public void process(ProcessRequestParams requestParams) throws IOException, InterruptedException {
		
	    Executor threadPool = (Executor) context.getBean("taskExecutor");
	    
		IntStream.range( requestParams.getMinWordCount(), requestParams.getMaxWordCount() )
	    	.boxed()
	    	.map(index -> {
    			Runnable task = () -> {
	    			RandomTextResponse response = new RandomTextClient().requestData(index, requestParams);
					BagOfWords newWords = new NaturalLanguageProcessor().process( response );
					bagOfWords.merge( newWords );
	    		};
	    		return task; 
    		})
	    	.forEach(threadPool :: execute);
	    
	}

	
	
	
	
}
