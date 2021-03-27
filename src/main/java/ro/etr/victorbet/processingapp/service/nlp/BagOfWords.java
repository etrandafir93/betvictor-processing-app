package ro.etr.victorbet.processingapp.service.nlp;

import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lombok.Getter;
import ro.etr.victorbet.processingapp.utils.MutableInt;

public class BagOfWords {

	@Getter
	private Map<String, MutableInt> words = new Hashtable<>();

	public void add(String word) {
		add(word, 1);
	}
	
	public void add(String word, int units) {
		if( words.containsKey(word) ) {
			words.get(word).increment(units);
		} else {
			words.put(word, new MutableInt(units));
		}
	}
	
	public Set<Entry<String, MutableInt>> getEntrySet() {
		return words.entrySet();
	}

	public void merge(BagOfWords newWords) {
		newWords.getEntrySet()
			.forEach( (newWord) -> { 
				add( newWord.getKey(), newWord.getValue().get() ); 
			});
	}
}
