package ro.etr.victorbet.processingapp;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
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
	void calculateMostFreqWord() throws IOException, InterruptedException {

		ProcessedTextDto response = (ProcessedTextDto)controller.processText(1, 5, 3, 5).getBody();

		assertThat(response.getMostFrequentWord()).isEqualTo("roses");
	}

	@Test
	void testProcessTime() throws IOException, InterruptedException {

		long start = System.currentTimeMillis();
		
		// 1 + 2 + 3 + 4 + 5 => 15 paragraphs, 5 requests
		ProcessedTextDto response = (ProcessedTextDto)controller.processText(1, 5, 3, 5).getBody();
		
		long time = System.currentTimeMillis() - start;
		long avgTime = time / 5;

		assertThat( response.getTotalProcessingTimeInMllis() ).isStrictlyBetween( time-50, time+50 );
		assertThat( response.getAvgProcessingTimeInMillis() ).isStrictlyBetween( avgTime-10, avgTime+10 );
	}

	@Test
	void handleBadRequestFromExternalService() throws IOException, InterruptedException {

		// wiremock will return 503 for these values
		ResponseEntity<?> response = controller.processText(6, 6, 6, 6); 
		
		assertThat( response.getStatusCode() ).isEqualTo( HttpStatus.INTERNAL_SERVER_ERROR );
	}


}
