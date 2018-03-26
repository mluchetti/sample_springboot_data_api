package com.luchetti.springboot.jpa.sample.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "emailResponseStatus", path = "responseStatus")
public interface EmailResponseStatusRepository extends PagingAndSortingRepository<EmailResponseStatus, Long> {

	List<EmailResponseStatus> findAll();
	Optional<EmailResponseStatus> findById(@Param("id") Long id);
	Optional<EmailResponseStatus> findByUuid(@Param("uuid") String uuid);
	
}
