package com.cooksys.ftd.assessment.filesharing.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.assessment.filesharing.dao.*;
import com.cooksys.ftd.assessment.filesharing.model.api.*;
import com.cooksys.ftd.assessment.filesharing.model.db.ClientMessage;

public class ClientHandler implements Runnable {
	
	private Logger log = LoggerFactory.getLogger(ClientHandler.class);
	
	private BufferedReader reader;
	private PrintWriter writer;
	
	private JAXBContext content;
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;
	
	private FileDao fileDao;
	private UserDao userDao;
	
	@Override
	public void run() {
		try {
			StringReader sr = new StringReader(this.reader.readLine());
			this.content = JAXBContext.newInstance(ClientMessage.class);
			ClientMessage message = (ClientMessage) unmarshaller.unmarshal(sr);
			if (message.getCommand() == "register") {
				StringWriter sw = new StringWriter();
				ServerResponse<String> temp = CreateUser.newUser(message.getContent());
				this.content = JAXBContext.newInstance(ServerResponse.class);
				marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
				marshaller.marshal(temp.getData(), sw);
				this.writer.println(sw.toString());
				this.writer.flush();
			} else if (message.getCommand() == "login") {
				StringWriter sw = new StringWriter();
				ServerResponse<String> temp = GetUser.getPassword(message.getContent());
				this.content = JAXBContext.newInstance(ServerResponse.class);
				marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
				marshaller.marshal(temp.getData(), sw);
				this.writer.println(sw.toString());
				this.writer.flush();
			} else if (message.getCommand() == "files") {
				StringWriter sw = new StringWriter();
				ServerResponse<List<String>> temp = IndexFile.getFileList(message.getContent());
				this.content = JAXBContext.newInstance(ServerResponse.class);
				marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
				marshaller.marshal(temp.getData(), sw);
				this.writer.println(sw.toString());
				this.writer.flush();
			} else if (message.getCommand() == "upload") {
				StringWriter sw = new StringWriter();
				ServerResponse<String> temp = AddFile.newFile(message.getContent());
				this.content = JAXBContext.newInstance(ServerResponse.class);
				marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
				marshaller.marshal(temp.getData(), sw);
				this.writer.println(sw.toString());
				this.writer.flush();
			} else if (message.getCommand() == "download") {
				StringWriter sw = new StringWriter();
				ServerResponse<String> temp = SendFile.getFile(message.getContent());
				this.content = JAXBContext.newInstance(ServerResponse.class);
				marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
				marshaller.marshal(temp.getData(), sw);
				this.writer.println(sw.toString());
				this.writer.flush();
			}
		} catch (IOException | JAXBException e) {
			log.error("A client error occured.", e);
		}
	}

	public BufferedReader getReader() {
		return reader;
	}

	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}

	public PrintWriter getWriter() {
		return writer;
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}

	public FileDao getFileDao() {
		return fileDao;
	}

	public void setFileDao(FileDao fileDao) {
		this.fileDao = fileDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
