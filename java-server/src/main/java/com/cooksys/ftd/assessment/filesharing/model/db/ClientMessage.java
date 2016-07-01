package com.cooksys.ftd.assessment.filesharing.model.db;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClientMessage {

	@XmlElement(name = "command")
	private String command;
	@XmlElement(name = "content")
	private String content;
	
	/*public ClientMessage() {
		// this.command;
		// this.content;
	}*/
	
	public String getCommand() {
		return this.command;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
