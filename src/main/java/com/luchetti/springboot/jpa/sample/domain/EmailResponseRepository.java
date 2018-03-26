package com.luchetti.springboot.jpa.sample.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "emailResponses", path = "emailResponses")
public interface EmailResponseRepository extends PagingAndSortingRepository<EmailResponse, Long> {

	List<EmailResponse> findAll();
	Optional<EmailResponse> findById(@Param("id") Long id);
	Optional<EmailResponse> findByUuid(@Param("uuid") String uuid);
	
}
