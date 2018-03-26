package com.luchetti.springboot.jpa.sample.domain;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="email_response")
public class EmailResponse {

	@Override
	public boolean equals(Object obj) {
		return this.getUuid().equals(((EmailResponse)obj).getUuid());
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String uuid, source;
	
	@OneToMany(mappedBy="response")
	private List<EmailResponseStatus> responseStatus = new ArrayList<>();
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[XXX]")
	private ZonedDateTime created;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[XXX]")
	private ZonedDateTime lastUpdated;

	public long getId() {
		return id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<EmailResponseStatus> getResponseStatus() {
		return responseStatus;
	}

	public void setResponsStatus(List<EmailResponseStatus> responses) {
		for (EmailResponseStatus emailResponseStatus : responses) {
			this.addResponses(emailResponseStatus);
		}
	}
	public void addResponses(EmailResponseStatus response) {
		this.responseStatus.add(response);
		response.setResponse(this);
	}

	@JsonFormat(timezone="America/Chicago",pattern = "yyyy-MM-dd'T'HH:mm:ss[XXX]")
	public ZonedDateTime getCreated() {
		return created;
	}
	public void setCreated(ZonedDateTime created) {
		this.created = created;
	}

	@JsonFormat(timezone="America/Chicago",pattern = "yyyy-MM-dd'T'HH:mm:ss[XXX]")
	public ZonedDateTime getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(ZonedDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	
}
