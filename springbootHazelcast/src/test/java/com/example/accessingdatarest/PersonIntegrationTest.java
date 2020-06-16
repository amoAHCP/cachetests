package com.example.accessingdatarest;

import antlr.collections.impl.IntRange;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@ActiveProfiles(profiles = "dev")
public class PersonIntegrationTest extends AbstractBenchmark{

    private static final ObjectMapper om = new ObjectMapper();

    private static TestRestTemplate restTemplate;

    @Autowired
    public  void setRestTemplate(TestRestTemplate restTemplate) {
        PersonIntegrationTest.restTemplate = restTemplate;
    }

    @Before
    public void setUp() throws JsonProcessingException {
        List<Person> persons = IntStream.range(0,1000).mapToObj(val-> {
            Faker faker = new Faker();


            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            return new Person(firstName,lastName);

        }).collect(Collectors.toList());

        persons.stream().forEach(p-> {
            ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/persons", p, String.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            System.out.println(response.getBody());
        });
    }
    @Test
    public void testInsert() throws JsonProcessingException {
        List<Person> persons =  Arrays.asList(new Person("Andy","M"),new Person("Arno","J"),new Person("Dominic","L"),new Person("Thomas","vS"));
        String payload = om.writeValueAsString(persons);
        persons.stream().forEach(p-> {
            ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/persons", p, String.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        });
    }

   @Benchmark
    public void findAll() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/persons",String.class);
    }
}
