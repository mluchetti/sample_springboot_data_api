package com.luchetti.springboot.jpa.sample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.luchetti.springboot.jpa.sample.domain.uuid.*;

@Configuration
public class AppConfig {
	
	@ConditionalOnProperty(name="active.uuid.impl", havingValue="Kubra", matchIfMissing=true)
	@Bean
	public UuidClient getNotifiUuidClient() {
		return new NotifiUuidClient();
	}
	
	@ConditionalOnProperty(name="active.uuid.impl", havingValue="Ameren", matchIfMissing=true)
	@Bean
	public UuidClient getAmerenUuidClient() {
		return new AmerenUuidClient();
	}
	
	@ConditionalOnProperty(name="active.uuid.impl", havingValue="SendGrid", matchIfMissing=true)
	@Bean
	public UuidClient getSendGridUuidClient() {
		return new SendGridUuidClient();
	}
	
	@ConditionalOnProperty(name="active.uuid.impl", havingValue="Random", matchIfMissing=true)
	@Bean
	public UuidClient getRandomUuidClient() {
		return new RandomUuidClient();
	}
	
	
}
