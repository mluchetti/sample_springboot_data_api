package com.luchetti.springboot.jpa.sample;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.luchetti.springboot.jpa.sample.domain.Person;
import com.luchetti.springboot.jpa.sample.domain.PersonRepository;

@RunWith(SpringRunner.class)
//@WebMvcTest(value = PersonController.class, secure = false)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class TestPersonRepository {

//	@Autowired 
//	private MockMvc mockMvc;

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private PersonRepository pr;

	private Person p1, p2;
	
	@Before
	public void loadTestData() {
		//given
		p1 = new Person();
		p1.setFirstName("Joe");
		p1.setLastName("Smith");
		p1.setBirthDate(LocalDate.parse("1980-07-01"));
		p1.setLastUpdated(ZonedDateTime.now(ZoneId.of("America/Chicago")));
		entityManager.persist(p1);
		entityManager.flush();

		p2 = new Person();
		p2.setFirstName("Jane");
		p2.setLastName("Doe");
		p2.setBirthDate(LocalDate.parse("1984-02-29"));
		p2.setLastUpdated(ZonedDateTime.now(ZoneId.of("America/Chicago")));
		entityManager.persist(p2);
		entityManager.flush();		
	}
	
	@Test
	public void whenFindAll() {

		//when
		List<Person> people = pr.findAll();

		System.out.println(people);
		
		//then
		assertThat(people.size()).isEqualTo(2);
		
		assertThat(people.get(0)).isEqualTo(p1);
		assertThat(people.get(0).getId()).isEqualTo(p1.getId());
		assertThat(people.get(0).getLastName()).isEqualTo(p1.getLastName());
		assertThat(people.get(0).getLastUpdated()).isEqualTo(p1.getLastUpdated());
		assertThat(people.get(0).getAge()).isEqualTo(p1.getAge());
		assertThat(people.get(0).getAgeMessage()).isEqualTo(p1.getAgeMessage());
		
		assertThat(people.get(1)).isEqualTo(p2);
		assertThat(people.get(1).getId()).isEqualTo(p2.getId());
		assertThat(people.get(1).getLastName()).isEqualTo(p2.getLastName());
		assertThat(people.get(1).getLastUpdated()).isEqualTo(p2.getLastUpdated());
		assertThat(people.get(1).getAge()).isEqualTo(p2.getAge());
		assertThat(people.get(1).getAgeMessage()).isEqualTo(p2.getAgeMessage());
	}

	@Test
	public void whenFindByLastName() {

		//when
		List<Person> people = pr.findByLastName("Smith");
		long num = people.stream().filter(p -> p.getLastName().equals("Smith")).count();

		//then
		assertThat(num).isGreaterThan(0);
		assertThat(people.size()).isEqualTo(1);
		assertThat(people.get(0)).isEqualTo(p1);
	}

	@Test
	public void whenFindById() {

		//when
		Optional<Person> people = pr.findById(p1.getId());

		//then
		assertThat(people.isPresent()).isTrue();
		assertThat(people.get()).isEqualTo(p1);
	}

}
