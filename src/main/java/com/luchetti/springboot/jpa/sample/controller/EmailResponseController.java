package com.luchetti.springboot.jpa.sample.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.luchetti.springboot.jpa.sample.domain.*;
import com.luchetti.springboot.jpa.sample.exception.InvalidUuidException;

@RestController
@RequestMapping("/responses")
public class EmailResponseController {

	private final EmailResponseRepository er;
	private final EmailResponseStatusRepository esr;

	@Autowired
	public EmailResponseController(EmailResponseRepository er, EmailResponseStatusRepository esr){
		this.er = er;	
		this.esr = esr;
	}
	
	@RequestMapping(path="",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EmailResponse>> processResponses(@RequestBody List<EmailResponse> e) throws JsonParseException, JsonMappingException, IOException {
		
		List<EmailResponse> saved = new ArrayList<EmailResponse>((Collection<? extends EmailResponse>) er.save(e));
		return  new ResponseEntity<List<EmailResponse>>(saved, HttpStatus.OK);
	}

	@RequestMapping(path="",method=RequestMethod.GET)
	public ResponseEntity<String> getAlertResponse(
			@NotNull @Valid @RequestParam(value = "source", required = true) String source,
			@NotNull @Valid @RequestParam(value = "uuid", required = true) String uuid,
			@NotNull @Valid @RequestParam(value = "status", required = true) Integer status) throws JsonParseException, JsonMappingException, IOException {
		
		System.out.println(String.format("Processing a %s reply of %d from UUID %s", source, status, uuid));
		Optional<EmailResponse> eResponse = er.findByUuid(uuid);
		URI location = null;

		try {
			//default "Something went wrong" landing page
			location = new URI("https://www.ameren.com/404NotFound");
			if(!eResponse.isPresent()) {
				processNewResponse(source, uuid, status);
				//TODO Need to have a real static content page on Ameren.com for outage response success
				location = new URI("https://www.ameren.com");
			} else {
				processRepeatResponse(uuid, status, eResponse.get());
				//TODO Need to have a real static content page on Ameren.com for outage response already responded or expired
				location = new URI("https://outagemap.ameren.com/");
			}
		} catch (InvalidUuidException e) {
			e.printStackTrace();
			//System.err.println(e.getMessage());
//			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setLocation(location);
			responseHeaders.set("Error", e.getMessage());
			return new ResponseEntity<String>(responseHeaders, HttpStatus.TEMPORARY_REDIRECT);	
		} catch (URISyntaxException e) {
			e.printStackTrace();
			//System.err.println(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setLocation(location);
		responseHeaders.set("MyResponseHeader", "MyValue");
		return new ResponseEntity<String>("Thank You!", responseHeaders, HttpStatus.TEMPORARY_REDIRECT);	
	}

	/**
	 * This method process email alert responses if the specific UUID has been received at least once.
	 * @param uuid unique identifier of the alert email. this is used for a call an account lookup API
	 * @param status the response from the user. this will be 1, 2 or 3
	 * @param entity the response that was previously processed
	 */
	private void processRepeatResponse(String uuid, Integer status, EmailResponse entity) {
		System.out.println(String.format("Response already captured from UUID %s", entity.getUuid()));
		System.out.println(String.format("Response already captured %d time(s)", entity.getResponseStatus().size()));
		entity.setLastUpdated(ZonedDateTime.now());
		EmailResponseStatus eRespStat = new EmailResponseStatus().respVal(status).uuid(uuid).created(ZonedDateTime.now());
		entity.addResponses(eRespStat);
		er.save(entity);
		System.out.println(esr.save(eRespStat).toString());
	}

	/**
	 * This method process email alert responses if the specific UUID has NEVER been received.
	 * @param source what alert email this response is originating
	 * @param uuid unique identifier of the alert email. this is used for a call an account lookup API
	 * @param status the response from the user. this will be 1, 2 or 3
	 * @throws Exception 
	 */
	private void processNewResponse(String source, String uuid, Integer status) throws InvalidUuidException {
		System.out.println(String.format("First response from UUID %s", uuid));

		if(lookupUuid(uuid)) {
			EmailResponse entity = new EmailResponse();
			entity.setCreated(ZonedDateTime.now());
			entity.setUuid(uuid);
			entity.setSource(source);
			entity.setLastUpdated(entity.getCreated());
			entity.addResponses(new EmailResponseStatus().respVal(status).uuid(uuid).created(entity.getCreated()).response(entity));
			er.save(entity);
			esr.save(entity.getResponseStatus().get(entity.getResponseStatus().size()-1));
		}
		else {
			throw new InvalidUuidException(String.format("Invalid UUID %s", uuid));
		}
	}

	private boolean lookupUuid(String uuid) {
		// TODO API Call to retrieve contact info relative to UUID
		double d = Math.random();
		System.out.println(d);
		return d<0.5?true:false;		
	}
	
//	@RequestMapping(path="",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<EmailResponse> processResponse(
//			@NotNull @Valid @RequestParam(value = "source", required = true) String source,
//			@NotNull @Valid @RequestParam(value = "uuid", required = true) String uuid,
//			@NotNull @Valid @RequestParam(value = "status", required = true) Integer status)
//	{	
//		EmailResponse entity = new EmailResponse();
//		entity.setCreated(ZonedDateTime.now());
//		entity.setUuid(uuid);
//		entity.setRespVal(status);
//		entity.setSource(source);
//		entity.setLastUpdated(entity.getCreated());
//		
//		if (er.findByUuid(uuid).isPresent())
//		{
//			return new ResponseEntity<EmailResponse>(HttpStatus.BAD_REQUEST);
//		}
//		
//		return new ResponseEntity<EmailResponse>(er.save(entity), HttpStatus.OK);
//	}
}
