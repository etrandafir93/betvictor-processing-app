package ro.etr.betvictor.processingapp;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.gson.Gson;

import ro.etr.betvictor.processingapp.config.Config;
import ro.etr.betvictor.processingapp.infrastructure.randomtext.RandomTextResponse;

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
		
		for(int count = 1; count<=6; count++) {
			for(int minw = 3; minw<=5; minw+=2) {
				for(int maxw = 3; maxw<=5; maxw+=2) {

					String path = String.format("/api/giberish/p-%d/%d-%d", count, minw, maxw);
					
					if(count == 6) 
					{
						wireMockServer.stubFor(get(urlPathMatching( path ))
								.willReturn( aResponse().withStatus( 503 ) ));
					} else 
					{
						wireMockServer.stubFor(get(urlPathMatching( path ))
								.willReturn(ok().withBody(aDummyResponse( count, minw, maxw ))));
					}
				}	
			}	
		}
		

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
