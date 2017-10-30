package com.luchetti.springboot.jpa.sample.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.luchetti.springboot.jpa.sample.domain.Person;
import com.luchetti.springboot.jpa.sample.domain.PersonRepository;
import com.luchetti.springboot.jpa.sample.exceptions.UserNotFoundException;

@RestController
@RequestMapping("/persons")
public class PersonController {
	
	private final PersonRepository pr;
//	ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

	@Autowired
	PersonController(PersonRepository pr){
		this.pr = pr;	
	}
	
	@RequestMapping(path="",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Person>> setPeople(@RequestBody List<Person> p) throws JsonParseException, JsonMappingException, IOException {
		
//		this.validatePerson(p.getId());
//		List<Person> pl = pr.findByLastName(p.getLastName());
		pr.save(p);
		return  new ResponseEntity<List<Person>>(p, HttpStatus.OK);
	}

	@RequestMapping(path="",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Person>> getPeople() {
		return  new ResponseEntity<List<Person>>(pr.findAll(), HttpStatus.OK);
	}	
	
	private void validatePerson(long userId) {
		this.pr.findById(userId).orElseThrow(
				() -> new UserNotFoundException(userId));
	}

}
