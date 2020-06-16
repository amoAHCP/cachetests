package com.example.accessingdatarest.api;

import com.example.accessingdatarest.entity.Person;
import com.example.accessingdatarest.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/persons")
public class ParsonAPI {

    private final PersonService personService;

    public ParsonAPI(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<List<Person>> findAll() {
        return ResponseEntity.ok(personService.findAll());
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody Person product) {
        return ResponseEntity.ok(personService.save(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable Long id) {
        Optional<Person> stock = personService.findById(id);
        if (!stock.isPresent()) {
            ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(stock.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> update(@PathVariable Long id, @Valid @RequestBody Person product) {
        if (!personService.findById(id).isPresent()) {
            ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(personService.save(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!personService.findById(id).isPresent()) {
            ResponseEntity.badRequest().build();
        }

        personService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
