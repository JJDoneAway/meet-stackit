package de.hoehne.stackit.test;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.hoehne.stackit.test.controler.GreetController;
import lombok.extern.slf4j.Slf4j;

@Component
@EnableScheduling
@Slf4j
public class RandomDataGenerator {
	private Random random = new Random();

	@Autowired
	private GreetController greetController;

	@Scheduled(fixedRate = 1_000, initialDelay = 1_000)
	void generateRandomData() {
		long waiting = random.nextLong(1000);
		try {
			Thread.sleep(waiting);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		greetController.greet("Random User");
		log.info("Triggered new greeting event after {} ms", waiting);
	}
}
