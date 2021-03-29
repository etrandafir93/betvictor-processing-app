package ro.etr.victorbet.processingapp.service.nlp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import ro.etr.victorbet.processingapp.infrastructure.randomtext.RandomTextResponse;

@Getter
public class NaturalLanguageProcessor {

	private PresenceCounter<Integer> avgParagraphSize;
	private PresenceCounter<String> bagOfWords;

	public void process(RandomTextResponse response) {
		
		List<String> paragraphs = breakIntoParagraphs( response.getTextOut().toLowerCase() );
		
		avgParagraphSize = getAvgParagraphSize(paragraphs);
		bagOfWords = getBagOfWords(paragraphs);
	}

	
	private PresenceCounter<Integer> getAvgParagraphSize(List<String> paragraphs) {

		PresenceCounter<Integer> avgParagraphSize = new PresenceCounter<>();
		
		paragraphs.stream()
			.map( this :: breakIntoWords )
			.map( List :: size )
			.forEach( avgParagraphSize :: add );
		
		return avgParagraphSize;
	}
	
	private PresenceCounter<String> getBagOfWords(List<String> paragraphs) {
		
		PresenceCounter<String> bagOfWords = new PresenceCounter<>();
		
		paragraphs.stream()
			.map( this :: breakIntoWords )
			.flatMap( List :: stream )
			.forEach( bagOfWords :: add );
		
		return bagOfWords;
	}

	public List<String> breakIntoParagraphs(String html) {
		if (html.length() == 0) {
			return new ArrayList<>();
		}
		html = html.substring("<p>".length()).replaceAll(".</p>","");
		return Arrays.asList(html.split("<p>"));
	}

	public List<String> breakIntoWords(String paragraph) {
		return Arrays.asList(paragraph.split(" "));
	}

	
}
