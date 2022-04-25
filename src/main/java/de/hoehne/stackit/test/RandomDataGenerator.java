package de.hoehne.stackit.test;

import java.net.URI;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
@EnableScheduling
public class RandomDataGenerator {
	private final Logger log = LoggerFactory.getLogger(RandomDataGenerator.class);
	
	private Random random = new Random();

	@Value("${meet_stackit.service.name}")
	private String serviceURL;

	@Scheduled(fixedRate = 1_000, initialDelay = 1_000)
	void generateRandomData() {
		long waiting = random.nextLong(1000);
		try {
			Thread.sleep(waiting);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			WebClient.create().get().uri(URI.create("%s/hello?name=Random_User".formatted(serviceURL)))
					.exchangeToMono(response -> {
						if (response.statusCode().equals(HttpStatus.OK)) {
							log.info("Triggered new greeting event after {} ms", waiting);
							return response.bodyToMono(String.class);
						} else if (response.statusCode().is4xxClientError()) {
							log.warn("Got Client Error while calling greeting service after {} ms", waiting);
							return Mono.just("Error response");
						} else if (response.statusCode().is5xxServerError()) {
							log.warn("Got Server Error while calling greeting service after {} ms", waiting);
							return Mono.just("Error response");
						} else {
							log.error("Got unexpected Error while calling greeting service after {} ms", waiting);
							return response.createException().flatMap(Mono::error);
						}
					}).block();

		} catch (Exception e) {
			log.error("Got System Exception while calling demo service", e);
		}
	}
}
