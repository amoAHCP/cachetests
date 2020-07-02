package com.example.accessingdatarest;

import com.example.accessingdatarest.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class RedisCommandLineRunner implements CommandLineRunner {
    @Autowired
    PersonService personService;
    public static void main(String[] args) {
        SpringApplication.run(RedisCommandLineRunner.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
