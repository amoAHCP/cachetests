package com.example.accessingdatarest;

import com.example.accessingdatarest.entity.Person;
import com.example.accessingdatarest.repository.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles(profiles = "devlocal")
public class PersonRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void whenFindByName_thenReturnEmployee() {
        // given
        Person alex = new Person("alex","test");
        entityManager.persist(alex);
        entityManager.flush();

        // when
        List<Person> byLastName = personRepository.findByLastName(alex.getLastName());

        // then
        Person person = byLastName.get(0);
        assertThat(person.getFirstName())
                .isEqualTo(alex.getFirstName());
    }
}
