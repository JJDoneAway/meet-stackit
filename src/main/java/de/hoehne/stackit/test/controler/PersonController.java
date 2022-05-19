package de.hoehne.stackit.test.controler;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import de.hoehne.stackit.test.entities.Person;
import de.hoehne.stackit.test.repositories.PersonRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

@Controller
@Transactional(timeout = 10)
@RequestMapping(path = "/person")
@EnableScheduling
@Slf4j
public class PersonController {

	@Autowired
	private PersonRepository personRepository;

	@Value("${max-persons}")
	private long maxPersons;

	private AtomicLong amount = new AtomicLong(0);

	public PersonController(MeterRegistry registry) {

		Gauge.builder("person_anount", amount, (amount) -> new BigDecimal(amount.get()).doubleValue())
				.description("Tells the current amount of persisted persons").register(registry);

	}

	@Timed(value = "create_person", description = "Upsert person in DB", histogram = true)
	@Counted(value = "create_person", description = "Counting created persons")
	@Bulkhead(name = "backendDB", fallbackMethod = "toManyRequests")
	@PostMapping
	public ResponseEntity<Person> create(@RequestParam String name) {
		return new ResponseEntity<Person>(personRepository.save(Person.builder().name(name).build()), HttpStatus.OK);
	}

	public ResponseEntity<Person> toManyRequests(Throwable e) {
		return new ResponseEntity<Person>(new Person(), HttpStatus.TOO_MANY_REQUESTS);
	}

	@Timed(value = "get_all_persons", description = "Query all persons from the DB", histogram = true)
	@Counted(value = "get_all_persons", description = "Counting all requests of all persons")
	@GetMapping
	public ResponseEntity<Iterable<Person>> getAll() throws ResponseStatusException {
		long amount = personRepository.count();
		if (amount > 500) {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.WARNING,
					"We have currently %s persons. This is to large for one result, Please use pagination"
							.formatted(amount));
			return new ResponseEntity<>(headers, HttpStatus.PAYLOAD_TOO_LARGE);
		}
		return new ResponseEntity<>(personRepository.findAll(), HttpStatus.OK);
	}

	@Timed(value = "get_all_persons", description = "Query all persons from the DB", histogram = true)
	@Counted(value = "get_all_persons", description = "Counting all requests of all persons")
	@GetMapping("page/{page}")
	@Bulkhead(name = "backendDB")
	public @ResponseBody Iterable<Person> getAll(@PathVariable Integer page) {
		return personRepository.findAll(PageRequest.of(page, 100));
	}

	@Timed(value = "delete_all_persons", description = "Delete all persons from the DB", histogram = true)
	@Counted(value = "delete_all_persons", description = "Counting all requests of delete all persons")
	@DeleteMapping
	public @ResponseBody HttpStatus deleteAll() {
		try {
			int page = 0;
			Page<Person> result;
			do {
				result = personRepository.findAll(PageRequest.of(page, 100));
				personRepository.deleteAll(result);
				page++;
			} while (!result.isEmpty());
		} catch (RuntimeException e) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return HttpStatus.NO_CONTENT;
	}

	@Scheduled(fixedDelay = 5_000, initialDelay = 10_000)
	void monitorAmount() {

		amount.set(personRepository.count());
	}

	@Scheduled(fixedDelay = 5_000, initialDelay = 10_000)
	@Async
	void cleanDB() {
		if (this.amount.get() > maxPersons * 2) {
			Random rnd = new Random();
			log.info("Currently we have to many personsn ({}) persisted. So we delete some", this.amount.get());
			long curentamount = this.amount.get();
			long delted = 0;
			while (curentamount > maxPersons) {
				int page = rnd.nextInt((int)(curentamount/100));
				Page<Person> toBeDelete = personRepository.findAll(PageRequest.of(page,100));
				personRepository.deleteAll(toBeDelete);
				delted += toBeDelete.getNumberOfElements();
				curentamount = personRepository.count();
			}
			log.info("We deleted {} persons. Now we have {} persons", delted, curentamount);

		}
	}
}
