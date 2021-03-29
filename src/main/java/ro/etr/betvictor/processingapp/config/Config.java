package ro.etr.betvictor.processingapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class Config {

	@Value("${infra.random-text-api.url}")
	private String randomTextApiUrl;
	
}
