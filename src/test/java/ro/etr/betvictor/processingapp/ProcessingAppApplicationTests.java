package ro.etr.betvictor.processingapp;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ro.etr.betvictor.processingapp.controller.TextProcessingContoller;
import ro.etr.betvictor.processingapp.dto.ProcessedTextDto;

class ProcessingAppApplicationTests extends EndToEndTestBase {

	@Autowired
	private TextProcessingContoller controller;


	@Test
	void calculateAverage() throws IOException, InterruptedException {

		ProcessedTextDto response = (ProcessedTextDto)controller.processText(4, 4, 3, 5).getBody();

		assertThat(response.getAvgParagraphSize()).isEqualTo(4);
	}

	@Test
	void calculateMostFreqWord() {

		ProcessedTextDto response = (ProcessedTextDto)controller.processText(1, 5, 3, 5).getBody();

		assertThat(response.getMostFrequentWord()).isEqualTo("roses");
	}

	@Test
	void testProcessTime() {

		long start = System.currentTimeMillis();
		
		// 1 + 2 + 3 + 4 + 5 => 15 paragraphs, 5 requests
		ProcessedTextDto response = (ProcessedTextDto)controller.processText(1, 5, 3, 5).getBody();
		
		long time = System.currentTimeMillis() - start;
		float avgTime = time / 15.0f;

		assertThat( response.getTotalProcessingTimeInMllis() ).isStrictlyBetween( time-200, time );
		assertThat( response.getAvgProcessingTimeInMillis() ).isStrictlyBetween( avgTime - 10.0f, avgTime );
	}

	@Test
	void oneBadRequestShouldNotAffectTheRestOfTheProcessing() {

		/*
		 * wiremock will return 
		 * ok  for p-1/x-x 
		 * ...
		 * ok  for p-5/x-x
		 * 503 for p-6/x-x
		 * */
		ProcessedTextDto goodReply = (ProcessedTextDto) controller.processText(1, 5, 3, 3).getBody();
		
		ResponseEntity<?> response = controller.processText(1, 6, 3, 3);
		ProcessedTextDto partialReply = (ProcessedTextDto) response.getBody(); 

		assertThat( goodReply.getAvgParagraphSize() ).isEqualTo( partialReply.getAvgParagraphSize() );
		assertThat( goodReply.getMostFrequentWord() ).isEqualTo( partialReply.getMostFrequentWord() );
		assertThat( response.getStatusCode() ).isEqualTo( HttpStatus.PARTIAL_CONTENT );
	}

}
