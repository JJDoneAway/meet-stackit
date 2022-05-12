package de.hoehne.stackit.test.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import de.hoehne.stackit.test.entities.Person;


public interface PersonRepository extends PagingAndSortingRepository<Person, Integer>, CrudRepository<Person, Integer>{

}
