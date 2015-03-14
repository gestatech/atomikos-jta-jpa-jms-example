package com.npstrandberg.jta.examples;

import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {
    Person findByName(String username);
}