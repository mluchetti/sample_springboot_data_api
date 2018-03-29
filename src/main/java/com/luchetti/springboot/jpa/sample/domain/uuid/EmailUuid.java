package com.luchetti.springboot.jpa.sample.domain.uuid;

public class EmailUuid {

	public EmailUuid(String account, String email, ResponseCode response) {
		this.account = account;
		this.email = email;
		this.response = response;
	}

	private String account, email;
	private ResponseCode response;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public ResponseCode getResponse() {
		return response;
	}
	public void setResponse(ResponseCode response) {
		this.response = response;
	}

	public enum ResponseCode {
		VALID("VALID"),
		NOT_FOUND("NOTFOUND"),
		EXPIRED("EXPIRED");
	    
		private String code;

	    ResponseCode(String code) {
	        this.code = code;
	    }

	    public String responseCode() {
	        return code;
	    }
	}

}
