package ro.etr.betvictor.processingapp.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {
	
	@Value("${server.thread-pool-size}")
	private int threadPoolSize;

	@Bean(name = "taskExecutor")
	public ExecutorService taskExecutor() {
		return Executors.newFixedThreadPool(threadPoolSize);
	}
	
}
