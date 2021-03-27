package ro.etr.victorbet.processingapp.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ro.etr.victorbet.processingapp.infrastructure.RandomTextResponse;
import ro.etr.victorbet.processingapp.service.nlp.BagOfWords;
import ro.etr.victorbet.processingapp.service.nlp.NaturalLanguageProcessor;

 
public class NaturalLanguageProcessorTest {

	private NaturalLanguageProcessor nlp = new NaturalLanguageProcessor();

	private String dummyParagraph = "<p>Hence this foolishly into anathematically willful swept after impala far.</p> ";
	
		
	@ParameterizedTest
	@CsvSource({"0,0", "1,1", "5,5", "100,100"})
	public void testBreakingIntoParagraphs(int input, int expected) {
		List<String> result = nlp.breakIntoParagraphs( paragraphs(input) );
		assertThat( result ).hasSize( expected );
	}

	@ParameterizedTest
	@CsvSource({"0,0", "1,1", "5,5", "100,100"})
	public void testAddingToBagOfWordsMap(int input, int expected) {
		
		RandomTextResponse dummyResponse = new RandomTextResponse();
		dummyResponse.setTextOut( paragraphs(input) );
		
		BagOfWords words = nlp.process( dummyResponse );
		
		words.getWords().values()
			.forEach( (count) -> { assertThat( count.get() ).isEqualTo( expected ); } );
	}
	
	
	private String paragraphs(int numberOfParagraphs) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<numberOfParagraphs; i++) {
			sb.append(dummyParagraph);
		}
		return sb.toString();
	}
}
