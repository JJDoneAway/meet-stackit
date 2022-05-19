package de.hoehne.stackit.test.controler;

import java.time.Duration;
import java.util.Random;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hoehne.stackit.test.entities.Name;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class GreetController {

	private Random random = new Random();

	@GetMapping(path = "/")
	public String hello() {
		return "meet-stackit test application";
	}

	@Timed(value = "greet_persons", description = "simple hello world service", histogram = true)
	@Counted(value ="greet_persons",description = "Counting all hello world calls")
	@GetMapping(path = "hello")
	public ResponseEntity<Name> greet(@RequestParam(defaultValue = "What is your name: name=xyz") String name) {
		StopWatch time = StopWatch.createStarted();

		try {

			try {
				Thread.sleep(random.nextLong(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (random.nextFloat(100) < 90) {
				return new ResponseEntity<Name>(
						Name.builder().greet("Hello").name(name).duration(Duration.ofNanos(time.getNanoTime())).build(),
						HttpStatus.OK);
			} else {
				log.error("Random exception for test reasons");
				return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Got error for {}", name);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		finally {
			time.stop();
			log.info("Fired greeting for {}. It took {}", name, Duration.ofNanos(time.getNanoTime()));

		}

	}

}
