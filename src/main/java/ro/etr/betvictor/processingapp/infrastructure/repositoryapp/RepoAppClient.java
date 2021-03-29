package ro.etr.betvictor.processingapp.infrastructure.repositoryapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ro.etr.betvictor.processingapp.config.KafkaConfig;
import ro.etr.betvictor.processingapp.dto.ProcessedTextDto;

@Slf4j
@Component
public class RepoAppClient {

	@Autowired
	private KafkaConfig config;
	
	@Autowired
	private Gson gson;
	
	@Getter
	@Setter
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	public void send( ProcessedTextDto dto ) {
		log.info("sending message to the '{}' topic", config.getTopic());
		kafkaTemplate.send(config.getTopic(), gson.toJson(dto));
	}
}
