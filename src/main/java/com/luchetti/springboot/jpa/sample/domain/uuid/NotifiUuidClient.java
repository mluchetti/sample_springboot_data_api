package com.luchetti.springboot.jpa.sample.domain.uuid;

import com.luchetti.springboot.jpa.sample.domain.uuid.EmailUuid.ResponseCode;

public class NotifiUuidClient implements UuidClient {

	@Override
	public EmailUuid lookupUuid(String uuid) {
		// TODO Write client code to consume Kubra Notifi Email UUID (AccountLookup) API
		
		return new EmailUuid("1", "me@someone.com", ResponseCode.VALID);
	}

}
