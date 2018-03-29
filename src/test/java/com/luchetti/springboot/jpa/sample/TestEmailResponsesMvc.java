package com.luchetti.springboot.jpa.sample;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.luchetti.springboot.jpa.sample.controller.EmailResponseController;
import com.luchetti.springboot.jpa.sample.domain.EmailResponse;
import com.luchetti.springboot.jpa.sample.domain.EmailResponseRepository;
import com.luchetti.springboot.jpa.sample.domain.EmailResponseStatus;
import com.luchetti.springboot.jpa.sample.domain.EmailResponseStatusRepository;
import com.luchetti.springboot.jpa.sample.domain.uuid.EmailUuid;
import com.luchetti.springboot.jpa.sample.domain.uuid.EmailUuid.ResponseCode;
import com.luchetti.springboot.jpa.sample.domain.uuid.UuidClient;

@RunWith(MockitoJUnitRunner.class)
//@WebMvcTest
public class TestEmailResponsesMvc {

	@Mock
	private EmailResponseRepository resp;
	@Mock
	private EmailResponseStatusRepository statResp;
	@Mock
	UuidClient client;
	@InjectMocks
	EmailResponseController controller;
	
	MockMvc mockMvc;
	
	@Before
	public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
	}
	
	@Test
	public void processEmailResponseLink_returnHttpStatusInvalidUuid() throws Exception {
		//arrange
		Optional<EmailResponse> entity = Optional.empty();
		
		when(resp.findByUuid("Invalid-UUID")).thenReturn(entity);
		when(client.lookupUuid("Invalid-UUID")).thenReturn(new EmailUuid(null, null, ResponseCode.NOT_FOUND));
		
		//act
		mockMvc.perform(get("/responses")
				.param("source", "PPO")
				.param("uuid", "Invalid-UUID")
				.param("status", "2"))
			.andExpect(status().is3xxRedirection())
			.andExpect(header().string("Location", "https://www.ameren.com/404NotFound"))
			.andExpect(redirectedUrl("https://www.ameren.com/404NotFound"));
	}

	@Test
	public void processEmailResponseLink_returnHttpStatusExpiredUuid() throws Exception {
		//arrange
		Optional<EmailResponse> entity = Optional.empty();
		
		when(resp.findByUuid("Invalid-UUID")).thenReturn(entity);
		when(client.lookupUuid("Invalid-UUID")).thenReturn(new EmailUuid(null, null, ResponseCode.EXPIRED));
		
		//act
		mockMvc.perform(get("/responses")
				.param("source", "PPO")
				.param("uuid", "Invalid-UUID")
				.param("status", "2"))
			.andExpect(status().is3xxRedirection())
			.andExpect(header().string("Location", "https://www.ameren.com/404NotFound"))
			.andExpect(redirectedUrl("https://www.ameren.com/404NotFound"));
	}

	@Test
	public void processEmailResponseLink_returnHttpStatusValidUuid() throws Exception {
		//arrange
		EmailResponse entity = new EmailResponse();
		ZonedDateTime now = ZonedDateTime.now();
		entity.setCreated(now);
		entity.setLastUpdated(now);
		entity.setSource("PPO");
		entity.setUuid("Valid-UUID");
		EmailResponseStatus response = new EmailResponseStatus()
				.created(now)
				.respVal(2)
				.uuid(entity.getUuid())
				.response(entity);

		
		when(resp.findByUuid("Valid-UUID")).thenReturn(Optional.empty());
		when(client.lookupUuid("Valid-UUID")).thenReturn(new EmailUuid(null, null, ResponseCode.VALID));
		when(resp.save(entity)).thenReturn(entity);
		when(statResp.save(response)).thenReturn(response);
		
		//act
		mockMvc.perform(get("/responses")
				.param("source", "PPO")
				.param("uuid", "Valid-UUID")
				.param("status", "2"))
			.andExpect(status().is3xxRedirection())
			.andExpect(header().string("Location", "https://www.ameren.com"))
			.andExpect(redirectedUrl("https://www.ameren.com"));
	}

	@Test
	public void processEmailResponseLink_returnHttpStatusRepeatedUuid() throws Exception {
		//arrange
		EmailResponse entity = new EmailResponse();
		ZonedDateTime now = ZonedDateTime.now();
		entity.setCreated(now);
		entity.setLastUpdated(now);
		entity.setSource("PPO");
		entity.setUuid("Valid-UUID");
		EmailResponseStatus response = new EmailResponseStatus()
				.created(now)
				.respVal(2)
				.uuid(entity.getUuid())
				.response(entity);
		entity.addResponses(response);

		when(resp.findByUuid("Valid-UUID")).thenReturn(Optional.of(entity));
		when(client.lookupUuid("Valid-UUID")).thenReturn(new EmailUuid(null, null, ResponseCode.VALID));
		when(resp.save(entity)).thenReturn(entity);
		when(statResp.save(Matchers.any(EmailResponseStatus.class))).thenReturn(response);
		
		//act
		mockMvc.perform(get("/responses")
				.param("source", "PPO")
				.param("uuid", "Valid-UUID")
				.param("status", "2"))
			.andExpect(status().is3xxRedirection())
			.andExpect(header().string("Location", "https://outagemap.ameren.com/"))
			.andExpect(redirectedUrl("https://outagemap.ameren.com/"));
	}
	
	@Test
	public void processEmailResponseLink_returnHttpStatus500() throws Exception {
		when(resp.findByUuid(Matchers.anyString())).thenThrow(IllegalArgumentException.class);
		
		//act
		mockMvc.perform(get("/responses")
				.param("source", "PPO")
				.param("uuid", "Valid-UUID")
				.param("status", "2"))
			.andExpect(status().is5xxServerError())
			.andExpect(header().doesNotExist("Location"));
	}
}
