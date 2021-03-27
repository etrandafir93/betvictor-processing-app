package ro.etr.victorbet.processingapp.service.nlp;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import lombok.Getter;
import ro.etr.victorbet.processingapp.utils.MutableInt;

public class PresenceCounter <T> {
	
	@Getter
	private Map<T, MutableInt> values = new Hashtable<>();

	public void add(T newValue) {
		add(newValue, 1);
	}
	
	public void add(T newValue, int units) {
		if( values.containsKey(newValue) ) {
			values.get(newValue).increment(units);
		} else {
			values.put(newValue, new MutableInt(units));
		}
	}
	
	public Set<Entry<T, MutableInt>> getEntrySet() {
		return values.entrySet();
	}

	public void merge(PresenceCounter<T> newWords) {
		newWords.getEntrySet()
			.forEach( (newWord) -> { 
				add( newWord.getKey(), newWord.getValue().get() ); 
			});
	}
}
