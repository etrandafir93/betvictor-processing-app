package ro.etr.victorbet.processingapp.infrastructure.repositoryapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import ro.etr.victorbet.processingapp.dto.ProcessedTextDto;

@Component
public class RepoAppClient {

	private static final String TOPIC = "words.processed";
	
	@Autowired
	private Gson gson;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	public void send( ProcessedTextDto dto ) {
		kafkaTemplate.send(TOPIC, gson.toJson(dto));
	}
}
