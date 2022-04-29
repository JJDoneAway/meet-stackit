package de.hoehne.stackit.test.controler;

import java.time.Duration;
import java.util.Random;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hoehne.stackit.test.entity.Name;
import de.hoehne.stackit.test.services.KillMeService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

@RestController
@EnableScheduling
@Slf4j
public class GreetController {

	private Counter counter;
	private Counter error;
	private Timer timer;

	private Random random = new Random();
	
	@Autowired
	private KillMeService killMeService;

	public GreetController(MeterRegistry meterRegistry) {
		this.counter = meterRegistry.counter("GreetController_counter");
		this.error = meterRegistry.counter("GreetController_error");
		this.timer = meterRegistry.timer("GreetController_timer");
	}

	@GetMapping(path = "/")
	public String hello() {
		return "meet-stackit test application";
	}
	
	@GetMapping(path = "hello")
	public ResponseEntity<Name> greet(@RequestParam(defaultValue = "What is your name: name=xyz") String name) {
		StopWatch time = StopWatch.createStarted();

		try {

			try {
				Thread.sleep(random.nextLong(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if(random.nextFloat(100) < 90) {
				return new ResponseEntity<Name>(
						Name.builder().greet("Hello").name(name).duration(Duration.ofNanos(time.getNanoTime())).build(),
						HttpStatus.OK);				
			} else {
				error.increment();
				log.error("Random exception for test reasons");
				return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);				
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			error.increment();
			log.error("Got error for {}", name);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		finally {
			time.stop();
			counter.increment();
			timer.record(Duration.ofNanos(time.getNanoTime()));
			log.info("Fired greeting for {}. It took {}", name, Duration.ofNanos(time.getNanoTime()));

		}

	}
	
	@GetMapping("/kill/memory")
	public HttpStatus killMyMemory() {
		killMeService.killMemory();
		return HttpStatus.OK;
	}

}
