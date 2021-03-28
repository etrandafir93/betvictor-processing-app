package ro.etr.victorbet.processingapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Config {

	@Value("${infra.random-text-api.url}")
	private String randomTextApiUrl;

}
