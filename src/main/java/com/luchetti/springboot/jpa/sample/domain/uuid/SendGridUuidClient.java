package com.luchetti.springboot.jpa.sample.domain.uuid;

import com.luchetti.springboot.jpa.sample.domain.uuid.EmailUuid.ResponseCode;

public class SendGridUuidClient implements UuidClient {

	@Override
	public EmailUuid lookupUuid(String uuid) {
		// TODO Write SendGrid Client code to consume Email UUID Lookup API
		// At this time there is not anything. This is for pattern demo purposes
		
		return new EmailUuid("1", "me@sendgrid.net", ResponseCode.EXPIRED);
	}

}
