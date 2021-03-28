package ro.etr.victorbet.processingapp;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.Fault;
import com.google.gson.Gson;

import ro.etr.victorbet.processingapp.config.Config;
import ro.etr.victorbet.processingapp.infrastructure.RandomTextResponse;

@SpringBootTest
public abstract class EndToEndTestBase {

	@Autowired
	protected Config config;
	
	protected static WireMockServer wireMockServer;
	
	private static String paragraphWithThreeWords = "<p>Roses are red.</p> ";
	private static String paragraphWithFiveWords = "<p>One two three for roses.</p> ";


	@BeforeAll
	public static void setup() {
		wireMockServer = new WireMockServer(9999);
		wireMockServer.start();
		initDummyResponses();
	}

	@AfterAll
	public static void teardown() {
		wireMockServer.stop();
	}

	@BeforeEach
	public void before() {
		config.setRandomTextApiUrl("http://localhost:9999/api/giberish/p-%d/%d-%d");
	}
	
	private static void initDummyResponses() {
		
		for(int count = 1; count<=5; count++) {
			for(int minw = 1; minw<=5; minw++) {
				for(int maxw = 1; maxw<=5; maxw++) {
					
					String path = String.format("/api/giberish/p-%d/%d-%d", count, minw, maxw);
					
					wireMockServer.stubFor(get(urlPathMatching( path ))
							.willReturn(ok().withBody(aDummyResponse( count, minw, maxw ))));
				}	
			}	
		}
		

		wireMockServer.stubFor(get(urlPathMatching( "/api/giberish/p-6/6-6" ))
				.willReturn( aResponse().withStatus( 503 ) ));
	}

	private static String aDummyResponse(int count, int minWords, int maxWords) {

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
	private static String generateDummyParagraphs(int count, int minWords, int maxWords) {
		
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
}
