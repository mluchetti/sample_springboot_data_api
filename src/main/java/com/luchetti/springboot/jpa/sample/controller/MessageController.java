package com.luchetti.springboot.jpa.sample.controller;

import java.util.List;
import java.util.concurrent.Executor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.luchetti.springboot.jpa.sample.domain.*;

@RestController
@RequestMapping("/message")
@EnableAsync
public class MessageController {

	static Logger l = LogManager.getLogger(MessageController.class);
	MessageProcessor _mp;
	
	@Autowired
	MessageController(MessageProcessor mp){
		this._mp = mp;
	}
	
	@RequestMapping(path="/emails",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EmailMessage>> saveEmailMessages(@RequestBody List<EmailMessage> messages){
		l.info(String.format("Received %d messages", messages.size()));
		messages.forEach(message -> this._mp.processMessage(message));
		return  new ResponseEntity<List<EmailMessage>>(messages, HttpStatus.ACCEPTED);
	}
	
	@RequestMapping(path="/texts",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TextMessage>> saveTextMessages(@RequestBody List<TextMessage> messages){
		l.info(String.format("Received %d messages", messages.size()));
		messages.forEach(message -> this._mp.processMessage(message));
		return  new ResponseEntity<List<TextMessage>>(messages, HttpStatus.ACCEPTED);
	}
	
    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
    	ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
    	pool.setCorePoolSize(10);
    	pool.setMaxPoolSize(25);
    	pool.setAllowCoreThreadTimeOut(true);
    	pool.initialize();
    	return pool;
    }

}
