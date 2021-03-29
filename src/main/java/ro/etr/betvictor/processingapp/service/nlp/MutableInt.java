package ro.etr.betvictor.processingapp.service.nlp;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class MutableInt 
{
	private int value = 1;
	
	public void increment() {
		++value;
	}
	
	public void increment(int units) {
		value += units;
	}
	
	public int get() {
		return value;
	}

	@Override
	public String toString() {
		return "MutableInt [" + value + "]";
	}
}
