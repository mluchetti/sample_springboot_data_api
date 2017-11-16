package com.luchetti.springboot.jpa.sample.domain;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MessageProcessor {
	static Logger l = LogManager.getLogger(MessageProcessor.class);
	
	@Async("threadPoolTaskExecutor")
	public <T> void processMessages(List<T> messages) {
		try {
			Thread.sleep(5000);
			l.info("Messages Processed");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Async("threadPoolTaskExecutor")
	public <T> void processMessage(T message) {
		try {
			Thread.sleep(1000);
			l.info(String.format("Message %s Processed", message.getClass().getSimpleName()));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
