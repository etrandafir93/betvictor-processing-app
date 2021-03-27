package ro.etr.victorbet.processingapp.confg;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

	private int maxPoolSize = 2;
	private int corePoolSize = 2;
	private int queueCapacity = 200;

	@Bean(name = "taskExecutor")
	public Executor taskExecutor() {
		
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize );
		executor.setQueueCapacity(queueCapacity);
		executor.initialize();
		
		return executor;
	}
	
}
