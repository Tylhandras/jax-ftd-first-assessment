package com.cooksys.ftd.assessment.filesharing.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.assessment.filesharing.model.db.User;
import com.cooksys.ftd.assessment.filesharing.server.ClientHandler;
import com.mysql.cj.api.jdbc.Statement;

public class UserDao extends AbstractDao {
	
	private Logger log = LoggerFactory.getLogger(ClientHandler.class);
	
	public User createUser(User user) {
		try {
			String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
			PreparedStatement stmt = this.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				user.setUserId(rs.getShort(1));
			}
		} catch (SQLException e) {
			log.error("An SQL error occured.", e);
		}
		
		return user;
	}
	
	public Optional<String> getUserPassword (String username) {
		try {
			String sql = "SELECT password FROM users WHERE username = ?";
			PreparedStatement stmt = this.getConn().prepareStatement(sql);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return Optional.of(rs.getString(1));
			}
		} catch (SQLException e) {
			log.error("An SQL error occured.", e);
		}
		
		return Optional.empty();
	}
	
	public Optional<Short> getUserId (String username) {
		try {
			String sql = "SELECT user_id FROM users WHERE username = ?";
			PreparedStatement stmt = this.getConn().prepareStatement(sql);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return Optional.of(rs.getShort(1));
			}
		} catch (SQLException e) {
			log.error("An SQL error occured.", e);
		}
		
		return Optional.empty();
	}
}
