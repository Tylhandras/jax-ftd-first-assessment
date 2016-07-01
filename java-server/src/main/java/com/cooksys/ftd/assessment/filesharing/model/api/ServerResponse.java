package com.cooksys.ftd.assessment.filesharing.model.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ServerResponse<T> {
	
	/**
	 * if true, an error was encountered and had no data returned.
	 * a message is optionally provided.
	 */
	@XmlElement(name = "error")
	private Boolean error;
	
	/**
	 * an optional message field
	 */
	@XmlElement(name = "message")
	private String message;
	
	/**
	 * the data returned by the server.
	 * will be missing in the event of an error.
	 */
	@XmlElement(name = "data")
	private T data;

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
