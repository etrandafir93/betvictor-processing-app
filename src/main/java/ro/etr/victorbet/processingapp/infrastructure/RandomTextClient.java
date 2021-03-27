package ro.etr.victorbet.processingapp.infrastructure;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;

import lombok.SneakyThrows;
import ro.etr.victorbet.processingapp.service.ProcessRequestParams;

public class RandomTextClient {

	private static final String BASE_URL = "http://www.randomtext.me/api/giberish/p-%d/%d-%d";

	private HttpClient client = HttpClient.newHttpClient();
	
	@SneakyThrows
	public RandomTextResponse requestData(int paragraphNumber, ProcessRequestParams processRequest) {
	
		HttpRequest request = HttpRequest.newBuilder()
				.uri(getUrl(paragraphNumber, processRequest.getMinWordCount(), processRequest.getMaxWordCount()))
				.build();

		HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

		return new Gson().fromJson(response.body(), RandomTextResponse.class);
	}

	private URI getUrl(int paragraphNumber, int minWords, int maxWords) {
		return URI.create(String.format(BASE_URL, paragraphNumber, minWords, maxWords));
	}

}
