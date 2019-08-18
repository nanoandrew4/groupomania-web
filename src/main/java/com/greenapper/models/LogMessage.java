package com.greenapper.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class LogMessage {

	@Id
	private String id;

	private String message;

	private String logLevel;

	private String timestamp;

	public LogMessage(final String message, final String logLevel) {
		this.message = message;
		this.logLevel = logLevel;
		this.timestamp = String.valueOf(LocalDateTime.now());
	}

	public LogMessage(final String message, final Throwable t, final String logLevel) {
		this(message, logLevel);

		if (message == null)
			this.message = "";
		for (StackTraceElement stackTraceElement : t.getStackTrace())
			this.message += stackTraceElement.toString() + '\n';
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp() {
		this.timestamp = String.valueOf(LocalDateTime.now());
	}
}
