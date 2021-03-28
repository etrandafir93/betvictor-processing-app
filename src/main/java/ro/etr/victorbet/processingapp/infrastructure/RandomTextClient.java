package ro.etr.victorbet.processingapp.infrastructure;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.springframework.http.HttpStatus;

import com.google.gson.Gson;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ro.etr.victorbet.processingapp.exceptions.RandomTextServerException;
import ro.etr.victorbet.processingapp.service.ProcessRequestParams;

@RequiredArgsConstructor
public class RandomTextClient {

	@NonNull
	private String randomTextUrl;

	private HttpClient client = HttpClient.newHttpClient();

	@SneakyThrows
	public RandomTextResponse requestData(int paragraphNumber, ProcessRequestParams processRequest) {

		HttpRequest request = HttpRequest.newBuilder()
				.uri(getUrl(paragraphNumber, processRequest.getMinWordCount(), processRequest.getMaxWordCount()))
				.build();

		HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

		if (HttpStatus.valueOf(response.statusCode()) != HttpStatus.OK) {
			throw new RandomTextServerException();
		}

		return new Gson().fromJson(response.body(), RandomTextResponse.class);
	}

	private URI getUrl(int paragraphNumber, int minWords, int maxWords) {
		return URI.create(String.format(randomTextUrl, paragraphNumber, minWords, maxWords));
	}

}
