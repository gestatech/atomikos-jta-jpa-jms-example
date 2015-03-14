package com.npstrandberg.jta.examples;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.JMSException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class JpaAndJmsTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private JpaAndJms service;

    @Test
    public void testCommit() throws JMSException {

        // setup
        Person person = new Person();
        person.setName("Niels Peter");

        // when
        service.registerNewPerson(person, false);

        // then
        assertEquals(1, personRepository.count());
        assertEquals("Niels Peter", personRepository.findByName("Niels Peter").getName());
        assertEquals("{name: 'Niels Peter'}", service.receivePersonCreatedMessage());
    }

    @Test
    public void testRollback() {

        // setup
        Person person = new Person();
        person.setName("Niels Peter");

        // when
        try {
            service.registerNewPerson(person, true);
        } catch (IllegalStateException e) {
            assertEquals("BOOM", e.getMessage());
        }

        // then
        assertEquals(0, personRepository.count());
        assertNull(service.receivePersonCreatedMessage());
    }
}
