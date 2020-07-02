package com.example.accessingdatarest.service;

import com.example.accessingdatarest.entity.Person;
import com.example.accessingdatarest.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(@Autowired PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Cacheable(cacheNames = "persons")
    public List<Person> findAll() {
        System.out.println("Load all persons");
        return personRepository.findAll();
    }

    @Cacheable(cacheNames = "persons", key = "#id")
    public Optional<Person> findById(Long id) {
        System.out.println("Load person:" + id);
        return personRepository.findById(id);
    }


    @Caching(evict = {
            @CacheEvict(value = "persons", allEntries = true)
    }, put = {
            @CachePut(cacheNames = "persons" ,key = "#person.id")}
    )
    public Person save(Person person) {
        return personRepository.save(person);
    }


    @Caching(evict = {
            @CacheEvict(value = "persons", allEntries = true)
    })
    public void deleteById(Long id) {
        personRepository.deleteById(id);
    }
}
