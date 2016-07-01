package com.cooksys.ftd.assessment.filesharing.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Socket;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.assessment.filesharing.dao.*;
import com.cooksys.ftd.assessment.filesharing.model.api.*;
import com.cooksys.ftd.assessment.filesharing.model.db.ClientMessage;

public class ClientHandler implements Runnable {

	private Logger log = LoggerFactory.getLogger(ClientHandler.class);

	private BufferedReader reader;
	private PrintWriter writer;

	private JAXBContext context;
	private JAXBContext content;
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;

	private FileDao fileDao;
	private UserDao userDao;

	public ClientHandler(Socket socket) {
		try {
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.writer = new PrintWriter(socket.getOutputStream());

			this.context = JAXBContext.newInstance(ClientMessage.class);
			this.content = JAXBContext.newInstance(ServerResponse.class);

			this.marshaller = content.createMarshaller();
			this.marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
			this.marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
			this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			this.unmarshaller = context.createUnmarshaller();
			this.unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
			this.unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, true);
		} catch (JAXBException | IOException e) {
			log.error("Error creating client.", e);
		}
	}

	@Override
	public void run() {
		try {
			StringReader sr = new StringReader(this.reader.readLine());
			ClientMessage message = (ClientMessage) unmarshaller.unmarshal(sr);
			if (message.getCommand().equals("register")) {
				StringWriter sw = new StringWriter();
				ServerResponse<String> temp = CreateUser.newUser(message.getContent(), this.userDao);
				marshaller.marshal(temp, sw);
				log.info(sw.toString());
				this.writer.println(sw.toString());
				this.writer.flush();
			} else if (message.getCommand().equals("login")) {
				StringWriter sw = new StringWriter();
				ServerResponse<String> temp = GetUser.getPassword(message.getContent(), this.userDao);
				marshaller.marshal(temp.getData(), sw);
				this.writer.println(sw.toString());
				this.writer.flush();
			} else if (message.getCommand() == "files") {
				StringWriter sw = new StringWriter();
				ServerResponse<List<String>> temp = IndexFile.getFileList(message.getContent());
				marshaller.marshal(temp.getData(), sw);
				this.writer.println(sw.toString());
				this.writer.flush();
			} else if (message.getCommand() == "upload") {
				StringWriter sw = new StringWriter();
				ServerResponse<String> temp = AddFile.newFile(message.getContent());
				marshaller.marshal(temp.getData(), sw);
				this.writer.println(sw.toString());
				this.writer.flush();
			} else if (message.getCommand() == "download") {
				StringWriter sw = new StringWriter();
				ServerResponse<String> temp = SendFile.getFile(message.getContent());
				marshaller.marshal(temp.getData(), sw);
				this.writer.println(sw.toString());
				this.writer.flush();
			}
		} catch (IOException | JAXBException e) {
			log.error("A client error occured.", e);
		}
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
