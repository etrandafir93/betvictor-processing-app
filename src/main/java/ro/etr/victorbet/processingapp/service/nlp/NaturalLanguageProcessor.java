package ro.etr.victorbet.processingapp.service.nlp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import ro.etr.victorbet.processingapp.infrastructure.RandomTextResponse;

@Service
public class NaturalLanguageProcessor {
	

	public BagOfWords process(RandomTextResponse response) {
		
		BagOfWords bagOfWords = new BagOfWords();
		
		breakIntoParagraphs( response.getTextOut() )
			.stream()
			.map( this :: breakIntoWords )
			.flatMap( List :: stream )
			.forEach( bagOfWords :: add );
		
		return bagOfWords;
	}

	public List<String> breakIntoParagraphs(String html) {
		if (html.length() == 0) {
			return new ArrayList<>();
		}
		html = html.substring("<p>".length());
		return Arrays.asList(html.split("<p>|.</p> <p>|.</p> "));
	}

	public List<String> breakIntoWords(String paragraph) {
		return Arrays.asList(paragraph.split(" "));
	}

	
}
