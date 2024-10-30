package com.test.logger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.logger.entities.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    
}
