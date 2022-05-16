package de.hoehne.stackit.test.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.hoehne.stackit.test.entities.Person;
import de.hoehne.stackit.test.repositories.PersonRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;

@Controller
@Transactional(timeout = 10)
@RequestMapping(path = "/person")
public class PersonController {

	@Autowired
	private PersonRepository personRepository;

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
	public @ResponseBody Iterable<Person> getAll() {
		return personRepository.findAll();
	}

	@Timed(value = "get_all_persons", description = "Query all persons from the DB", histogram = true)
	@Counted(value = "get_all_persons", description = "Counting all requests of all persons")
	@GetMapping("page/{page}")
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


}
