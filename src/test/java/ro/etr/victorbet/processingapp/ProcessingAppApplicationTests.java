package ro.etr.victorbet.processingapp;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ro.etr.victorbet.processingapp.controller.TextProcessingContoller;
import ro.etr.victorbet.processingapp.dto.ProcessedTextDto;

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
		long avgTime = time / 15;

		assertThat( response.getTotalProcessingTimeInMllis() ).isStrictlyBetween( time-50, time+50 );
		assertThat( response.getAvgProcessingTimeInMillis() ).isStrictlyBetween( avgTime - 1.0f, avgTime + 1.0f );
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
