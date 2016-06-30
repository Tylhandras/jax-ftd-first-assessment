package com.cooksys.ftd.assessment.filesharing.server;

import java.io.BufferedReader;
import java.io.PrintWriter;

import com.cooksys.ftd.assessment.filesharing.dao.*;

public class ClientHandler implements Runnable {
	
	private BufferedReader reader;
	private PrintWriter writer;
	
	private FileDao fileDao;
	private UserDao userDao;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
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
