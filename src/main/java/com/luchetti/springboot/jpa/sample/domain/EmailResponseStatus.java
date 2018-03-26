package com.luchetti.springboot.jpa.sample.domain;

import java.time.ZonedDateTime;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="email_response_status")
@Embeddable
public class EmailResponseStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	@JoinColumn(name="response_id_fk")
	private EmailResponse response;
	
	private String uuid;
	private int respVal;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[XXX]")
	private ZonedDateTime created;

	public EmailResponseStatus response(EmailResponse response) {
		this.response = response;
		return this;
	}
	
	public EmailResponseStatus uuid(String uuid) {
		this.uuid = uuid;
		return this;
	}
	
	public EmailResponseStatus respVal(int resp) {
		this.respVal = resp;
		return this;
	}

	public EmailResponseStatus created(ZonedDateTime created) {
		this.created = created;
		return this;
	}

	public EmailResponse getResponse() {
		return response;
	}

	public void setResponse(EmailResponse response) {
		this.response = response;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getRespVal() {
		return respVal;
	}

	public void setRespVal(int respVal) {
		this.respVal = respVal;
	}

	@JsonFormat(timezone="America/Chicago",pattern = "yyyy-MM-dd'T'HH:mm:ss[XXX]")
	public ZonedDateTime getCreated() {
		return created;
	}

	public void setCreated(ZonedDateTime created) {
		this.created = created;
	}

	@Override
	public String toString() {
		return String.format("EmailResponseStatus [id=%s, response=%s, uuid=%s, respVal=%s, created=%s]", id, response,
				uuid, respVal, created);
	}

}
