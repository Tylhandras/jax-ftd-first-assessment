package com.cooksys.ftd.assessment.filesharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.assessment.filesharing.dao.*;
import com.cooksys.ftd.assessment.filesharing.server.Server;

public class Main {
	
	private static Logger log = LoggerFactory.getLogger(Main.class);

	private static String driver = "com.mysql.cj.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/sakila";
	private static String username = "root";
	private static String password = "bondstone";
	
	public static void main(String[] args) throws ClassNotFoundException {
		Class.forName(driver);
		ExecutorService executor = Executors.newCachedThreadPool();
		
		try (Connection conn = DriverManager.getConnection(url, username, password)) {
			Server server = new Server();
			server.setExecutor(executor);
			
			UserDao userDao = new UserDao();
			userDao.setConn(conn);
			server.setUserDao(userDao);
			
			FileDao fileDao = new FileDao();
			fileDao.setConn(conn);
			server.setFileDao(fileDao);
			
			Future<?> serverFuture = executor.submit(server);
			
			serverFuture.get();
		} catch (SQLException | InterruptedException | ExecutionException e) {
			log.error("A fatal error occured during serever startup.  Shutting down after error log.", e);
		} finally {
			executor.shutdown();
		}
	}
}
