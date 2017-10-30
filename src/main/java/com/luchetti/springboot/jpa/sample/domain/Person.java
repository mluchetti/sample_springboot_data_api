package com.luchetti.springboot.jpa.sample.domain;

import java.time.*;
import java.time.temporal.ChronoUnit;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
public class Person {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	String firstName, lastName;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	LocalDate birthDate;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[XXX]")
	ZonedDateTime lastUpdated;

	@JsonProperty(access=Access.READ_ONLY)
	private String ageMessage;
	@JsonProperty(access=Access.READ_ONLY)
	private int age;

	public long getId() {
		return id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public LocalDate getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
		LocalDate today = LocalDate.now(ZoneId.of("America/Chicago"));

		Period p = Period.between(birthDate, today);
		long p2 = ChronoUnit.DAYS.between(birthDate, today);
		
		this.ageMessage = String.format("%s is %d years, %d months, and %d days old. (%d days total)", this.getFirstName(),p.getYears(),p.getMonths(),p.getDays(),p2);
		this.age = p.getYears();
	}
	public String getAgeMessage() {
		return this.ageMessage;
	}
	public int getAge() {
		return this.age;
	}
	@JsonFormat(timezone="America/Chicago",pattern = "yyyy-MM-dd'T'HH:mm:ss[XXX]")
	public ZonedDateTime getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(ZonedDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
}
