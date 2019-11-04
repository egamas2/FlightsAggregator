package com.company.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;

@SpringBootApplication
@EnableAsync(proxyTargetClass=true)
public class FlightsAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightsAggregatorApplication.class, args);
	}

	@Value("${threadPoolTaskExecutor.corePoolSize}")
	private int corePoolSize;

	@Value("${threadPoolTaskExecutor.MaxPoolSize}")
	private int maxPoolSize;

	@Value("${hardJet.http.address}")
	private String hardJetAddress;

	@Value("${mongerAir.http.address}")
	private String mongerAirAddress;

	@Value("${flightAggregator.timeZone}")
	private String timeZone;

	@Bean("threadPoolTaskExecutor")
	public ThreadPoolTaskExecutor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setThreadNamePrefix("Async-");
		return executor;
	}

	@Bean
	public String hardJetAddress(){
		return hardJetAddress;
	}

	@Bean
	public String mongerAirAddress(){
		return mongerAirAddress;
	}

	@Bean
	public ZoneId zoneId(){
		return ZoneId.of(timeZone);
	}

	@Bean
	public RestTemplate restTemplateExternal() {
		return new RestTemplate();
	}//For the external services


}
