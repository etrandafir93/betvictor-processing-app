package ro.etr.betvictor.processingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;



@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
public class ProcessingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcessingAppApplication.class, args);
	}

}
