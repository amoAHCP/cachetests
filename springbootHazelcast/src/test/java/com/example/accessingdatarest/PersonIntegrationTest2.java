package com.example.accessingdatarest;

import com.example.accessingdatarest.entity.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openjdk.jmh.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;


@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@ActiveProfiles(profiles = "dev")
public class PersonIntegrationTest2 extends AbstractBenchmark{

    private static final ObjectMapper om = new ObjectMapper();
   // public static final String URL = "http://localhost:8080";
    public static final String URL = "http://192.168.64.38";

    private static TestRestTemplate restTemplate = new TestRestTemplate();


    @Before
    public void setUp() throws JsonProcessingException {
        List<Person> persons = IntStream.range(0,5000).mapToObj(val-> {
            Faker faker = new Faker();


            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            return new Person(firstName,lastName);

        }).collect(Collectors.toList());

        persons.stream().forEach(p-> {
            ResponseEntity<String> response = restTemplate.postForEntity(URL + "/api/v1/persons", p, String.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            System.out.println("---"+response.getBody());
        });
    }


   @Benchmark
    public void findAll() {
        ResponseEntity<String> response = restTemplate.getForEntity(URL + "/api/v1/persons",String.class);
    }
}
