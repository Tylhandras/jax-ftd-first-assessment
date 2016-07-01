package com.cooksys.ftd.assessment.filesharing.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.assessment.filesharing.dao.*;
import com.cooksys.ftd.assessment.filesharing.server.ClientHandler;

public class Server implements Runnable {
	
	private Logger log = LoggerFactory.getLogger(Server.class);
	
	private ExecutorService executor;
	
	private FileDao fileDao;
	private UserDao userDao;
	
	@Override
	public void run() {
		try (ServerSocket serverSocket = new ServerSocket(667)) {
			log.info("Starting server.");
			while (true) {
				Socket socket = serverSocket.accept();
				ClientHandler handler = this.createClientHandler(socket);
				this.executor.execute(handler);
			}
		} catch (IOException e) {
			this.log.error("Fatal server connection error while listening for new connections.  Shutting down after error log.", e);
		}
	}
	
	public ClientHandler createClientHandler(Socket socket) throws IOException {
		ClientHandler handler = new ClientHandler(socket);
		
		handler.setUserDao(userDao);
		handler.setFileDao(fileDao);
		
		return handler;
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
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
