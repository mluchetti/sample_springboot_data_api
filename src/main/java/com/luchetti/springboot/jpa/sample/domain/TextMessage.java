package com.luchetti.springboot.jpa.sample.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class TextMessage {

	@JsonProperty(access=Access.READ_ONLY)
	private String msgType = "TextMessage";
	
	private String clientNum, body;

	public String getClientNum() {
		return clientNum;
	}
	public void setClientNum(String clientNum) {
		this.clientNum = clientNum;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
}
