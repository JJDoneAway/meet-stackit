package de.hoehne.stackit.test.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.hoehne.stackit.test.entities.Person;
import de.hoehne.stackit.test.repositories.PersonRepository;

@Controller
@RequestMapping(path="/person")
public class PersonController {

	@Autowired
	private PersonRepository personRepository;
	
	
	@PostMapping
	public @ResponseBody Person create(@RequestParam String name) {
		return personRepository.save(Person.builder().name(name).build());
	}
	
	@GetMapping
	public @ResponseBody Iterable<Person> getAll(){
		return personRepository.findAll();
	}
	
	
	
}
