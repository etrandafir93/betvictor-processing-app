package ro.etr.victorbet.processingapp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import lombok.Getter;
import ro.etr.victorbet.processingapp.infrastructure.RandomTextResponse;
import ro.etr.victorbet.processingapp.utils.MutableInt;

@Service
public class NaturalLanguageProcessor {
	
	@Getter
	private Map<String, MutableInt> bagOfWords = new Hashtable<>();

	public void process(RandomTextResponse response) {
		
		List<String> paragraphs = breakIntoParagraphs( response.getTextOut() );
		
		paragraphs.stream()
			.map( this :: breakIntoWords )
			.flatMap( List :: stream )
			.forEach(word -> { 
				if( bagOfWords.containsKey(word) ) {
					bagOfWords.get(word).increment();
				} else {
					bagOfWords.put(word, new MutableInt());
				}
			});

		
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
