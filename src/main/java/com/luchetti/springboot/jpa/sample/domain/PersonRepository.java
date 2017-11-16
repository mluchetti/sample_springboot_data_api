package com.luchetti.springboot.jpa.sample.domain;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.format.annotation.DateTimeFormat;

@RepositoryRestResource(collectionResourceRel = "people", path = "people")
public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {

	List<Person> findAll();
	List<Person> findByLastName(@Param("name") String name);
	Optional<Person> findById(@Param("id") Long id);
	
//	@Query("select a from Person a where a.lastUpdated >= ?1")
	List<Person> findByLastUpdatedGreaterThanEqual(
			@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[XXX]") @Param("startDate") ZonedDateTime start);
	
	List<Person> findByLastUpdatedBetween(
			@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[XXX]") @Param("startDate") ZonedDateTime start, 
			@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[XXX]") @Param("endDate") ZonedDateTime end);
}