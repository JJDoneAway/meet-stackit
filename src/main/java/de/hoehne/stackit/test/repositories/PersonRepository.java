package de.hoehne.stackit.test.repositories;

import org.springframework.data.repository.CrudRepository;

import de.hoehne.stackit.test.entities.Person;


public interface PersonRepository extends CrudRepository<Person, Integer>{

}
