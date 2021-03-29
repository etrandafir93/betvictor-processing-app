package ro.etr.victorbet.processingapp.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
public class KafkaConfig {

	@Getter
	@Value("${kafka.topic}")
	private String topic;


	@Value("${kafka.partitions}")
	private int partitions;


	@Value("${kafka.replicas}")
	private short replicas;

	@Bean
	public NewTopic topic1() {
	    return new NewTopic(topic, partitions, replicas);
	}
}
