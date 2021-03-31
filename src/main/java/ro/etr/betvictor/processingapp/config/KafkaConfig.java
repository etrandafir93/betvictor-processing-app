package ro.etr.betvictor.processingapp.config;

import java.util.Arrays;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import lombok.Data;

@Data
@Configuration
public class KafkaConfig {

	@Value("${kafka.topic}")
	private String topic;

	@Value("${kafka.partitions}")
	private int partitions;

	@Value("${kafka.replicas}")
	private short replicas;

	@Autowired
	private Environment environment;

	@Bean
	public NewTopic topic1() {
		
		if( !isTestEnv() ) {
			return new NewTopic(topic, partitions, replicas);
		}
		return null;
	}

	private boolean isTestEnv() {
		return Arrays.asList( environment.getActiveProfiles() ).contains( "test" );
	} 
}
