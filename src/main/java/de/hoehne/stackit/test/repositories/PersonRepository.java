package de.hoehne.stackit.test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hoehne.stackit.test.entities.Person;


public interface PersonRepository extends JpaRepository<Person, Integer>{

}
