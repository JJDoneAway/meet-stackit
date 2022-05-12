package de.hoehne.stackit.test.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.hoehne.stackit.test.entities.Person;
import de.hoehne.stackit.test.repositories.PersonRepository;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;

@Controller
@RequestMapping(path = "/person")
public class PersonController {

	@Autowired
	private PersonRepository personRepository;

	@Timed(value = "create_person", description = "Upsert person in DB", histogram = true)
	@Counted(value = "create_person", description = "Counting created persons")
	@PostMapping
	public @ResponseBody Person create(@RequestParam String name) {
		return personRepository.save(Person.builder().name(name).build());
	}

	@Timed(value = "get_all_persons", description = "Query all persons from the DB", histogram = true)
	@Counted(value ="get_all_persons",description = "Counting all requests of all persons")
	@GetMapping
	public @ResponseBody Iterable<Person> getAll() {
		return personRepository.findAll();
	}

	@Timed(value = "delete_all_persons", description = "Delete all persons from the DB", histogram = true)
	@Counted(value ="delete_all_persons",description = "Counting all requests of delete all persons")
	@DeleteMapping
	public @ResponseBody HttpStatus deleteAll() {
		try {
			personRepository.deleteAllInBatch();
		} catch (RuntimeException e) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return HttpStatus.NO_CONTENT;
	}

}
