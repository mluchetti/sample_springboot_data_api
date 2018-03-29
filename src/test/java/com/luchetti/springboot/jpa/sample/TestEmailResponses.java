package com.luchetti.springboot.jpa.sample;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.luchetti.springboot.jpa.sample.controller.EmailResponseController;
import com.luchetti.springboot.jpa.sample.domain.EmailResponse;
import com.luchetti.springboot.jpa.sample.domain.EmailResponseRepository;
import com.luchetti.springboot.jpa.sample.domain.EmailResponseStatus;
import com.luchetti.springboot.jpa.sample.domain.EmailResponseStatusRepository;
import com.luchetti.springboot.jpa.sample.domain.uuid.UuidClient;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class TestEmailResponses {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private EmailResponseRepository resp;
	@Autowired
	private EmailResponseStatusRepository statResp;
	@Autowired
	private UuidClient uuidClient;

    //private MockMvc mockMvc;

	private EmailResponseController controller;
	
	private EmailResponse e1, e2;
	
	@Before
	public void loadTestData() {
		//given
		e1 = new EmailResponse();
		e1.setSource("PPO");
		e1.setUuid("test-ppo-uuid-1");
		e1.addResponses(new EmailResponseStatus().created(ZonedDateTime.now()).uuid(e1.getUuid()).respVal(2).response(e1));
		entityManager.persistFlushFind(e1);
		entityManager.persistFlushFind(e1.getResponseStatus().get(e1.getResponseStatus().size()-1));

		e2 = new EmailResponse();
		e2.setSource("RESTORE");
		e2.setUuid("test-restore-uuid-1");
		e2.addResponses(new EmailResponseStatus().created(ZonedDateTime.now()).uuid(e2.getUuid()).respVal(1).response(e2));	
		entityManager.persistFlushFind(e2);
		entityManager.persistFlushFind(e2.getResponseStatus().get(e2.getResponseStatus().size()-1));
		
	}
	
	@Test
	public void whenFindByUuid() {
		//act
		Optional<EmailResponse> responses = resp.findByUuid(e2.getUuid());
		
		//assert
		assertThat(responses.isPresent()).isTrue();
		assertThat(responses.get()).isEqualTo(e2);
		assertThat(responses.get().getResponseStatus().get(0).getUuid().equals(e2.getUuid()));
	}
	
	@Test
	public void processEmailResponseLink_returnHttpStatusRedirect() throws JsonParseException, JsonMappingException, IOException {
		//arrange
		controller = new EmailResponseController(resp, statResp, uuidClient);
		
		//act
		ResponseEntity<String> e = controller.getAlertResponse(e1.getSource(), e1.getUuid(), e1.getResponseStatus().get(0).getRespVal());
		
		//assert
		assertThat(e.getStatusCode()).isSameAs(HttpStatus.TEMPORARY_REDIRECT);
		assertThat(e.getHeaders().containsKey("Location"));
	}

	@Test
	public void processEmailResponseLink_returnHttpStatusNotFound() throws JsonParseException, JsonMappingException, IOException {
		//arrange
//		Mockito.when(Math.random()).thenReturn(0.4);
		controller = new EmailResponseController(resp, statResp, uuidClient);
		
		//act
		ResponseEntity<String> e = controller.getAlertResponse(e1.getSource(), "Unknown_UUID", e1.getResponseStatus().get(0).getRespVal());
		
		//assert
		assertThat(e.getStatusCode()).isSameAs(HttpStatus.TEMPORARY_REDIRECT);
		assertThat(e.getHeaders().containsKey("Location"));
	}

	@Test
	public void processEmailResponseLink_returnHttpStatusInvalidUuid() throws Exception {
		//arrange
		controller = new EmailResponseController(resp, statResp, uuidClient);
		
		//act
		ResponseEntity<String> e = controller.getAlertResponse(e1.getSource(), "Invalid_UUID", e1.getResponseStatus().get(0).getRespVal());
		
		//assert
		assertThat(e.getStatusCode()).isSameAs(HttpStatus.TEMPORARY_REDIRECT);
		assertThat(e.getHeaders().containsKey("Location"));
		assertThat(e.getHeaders().get("Location").get(0)).isSameAs("https://www.ameren.com/404NotFound");
	}
	

}
