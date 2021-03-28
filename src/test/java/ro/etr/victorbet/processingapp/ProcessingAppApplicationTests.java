package ro.etr.victorbet.processingapp;

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.gson.Gson;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import ro.etr.victorbet.processingapp.config.Config;
import ro.etr.victorbet.processingapp.controller.TextProcessingContoller;
import ro.etr.victorbet.processingapp.dto.ProcessedTextDto;
import ro.etr.victorbet.processingapp.infrastructure.RandomTextResponse;

@SpringBootTest
class ProcessingAppApplicationTests {

	public static WireMockServer wireMockServer;

	@Autowired
	private TextProcessingContoller controller;

	@Autowired
	private Config config;

	@BeforeAll
	public static void setup() {
		wireMockServer = new WireMockServer(9999);
		wireMockServer.start();
	}

	@AfterAll
	public static void teardown() {
		wireMockServer.stop();
	}

	@BeforeEach
	public void before() {
		config.setRandomTextApiUrl("http://localhost:9999/api/giberish/p-%d/%d-%d");
	}

	@Test
	void simpleFlow() throws IOException, InterruptedException {

		wireMockServer.stubFor(get(urlPathMatching("/api/giberish/p-1/3-5"))
			.willReturn(ok()
				.withBody(aDummyResponse(1,3,5))));

		ProcessedTextDto response = controller.processText(1, 1, 3, 5);

		Assertions.assertThat(response.getAvgParagraphSize()).isEqualTo(3);
	}

	private String aDummyResponse(int count, int minWords, int maxWords) {

		RandomTextResponse response = new RandomTextResponse();
		response.setType("gibberish");
		response.setFormat("p");
		response.setMaxNumber(minWords);
		response.setNumber(maxWords);
		response.setTime("15:46:03");
		response.setAmount(count);
		response.setTextOut(generateDummyParagraphs(count, minWords, maxWords));

		return new Gson().toJson(response);
	}

	
	/*
	 * (4, 3, 5) => 3 word p, 5 word p, 3 word p, 5 word p
	 * (4, 3, 3) => 3 word p, 3 word p, 3 word p, 3 word p
	 * (4, 5, 5) => 5 word p, 5 word p, 5 word p, 5 word p
	 * & most common word = 'roses'
	 * */
	private String generateDummyParagraphs(int count, int minWords, int maxWords) {
		
		StringBuilder sb = new StringBuilder();
		String newParagraph = "";
		
		for(int i=0; i<count; i++) {
			if( minWords == maxWords ) {
				newParagraph = minWords == 3? paragraphWithThreeWords : paragraphWithFiveWords;
			} else {
				newParagraph = i % 2 == 0? paragraphWithThreeWords : paragraphWithFiveWords;
			}
			sb.append(newParagraph);
		}
		return sb.toString();
	}

	private String paragraphWithThreeWords = "<p>Roses are red.</p> ";
	private String paragraphWithFiveWords = "<p>One two three for roses</p> ";

}
