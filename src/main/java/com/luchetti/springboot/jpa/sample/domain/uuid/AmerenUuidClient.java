package com.luchetti.springboot.jpa.sample.domain.uuid;

import com.luchetti.springboot.jpa.sample.domain.uuid.EmailUuid.ResponseCode;

public class AmerenUuidClient implements UuidClient {

	@Override
	public EmailUuid lookupUuid(String uuid) {
		// TODO Write client code to consume Ameren Email UUID Lookup API
		// At this time there is not anything. This is for pattern demo purposes
		return new EmailUuid("2", "me@ameren.com", ResponseCode.NOT_FOUND);
	}

}
