package ro.etr.victorbet.processingapp.service;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import ro.etr.victorbet.processingapp.infrastructure.RandomTextResponse;

@Service
public class NaturalLanguageProcessor {

	private Map<String, Integer> bagOfWords = new Hashtable<>();

	public void process(RandomTextResponse response) {
		System.err.println( response.getTextOut() );
	}
	
	
}
