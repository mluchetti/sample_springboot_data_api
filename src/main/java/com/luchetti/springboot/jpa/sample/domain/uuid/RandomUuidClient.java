package com.luchetti.springboot.jpa.sample.domain.uuid;

import com.luchetti.springboot.jpa.sample.domain.uuid.EmailUuid.ResponseCode;

public class RandomUuidClient implements UuidClient {

	@Override
	public EmailUuid lookupUuid(String uuid) {
		// TODO Write client code to consume Kubra Notifi Email UUID (AccountLookup) API
		double d = Math.random();
		System.out.println(d);
		return d<0.5?new EmailUuid("1", "me@someone.com", ResponseCode.VALID):new EmailUuid("1", "me@someone.com", ResponseCode.NOT_FOUND);		
	}

}
