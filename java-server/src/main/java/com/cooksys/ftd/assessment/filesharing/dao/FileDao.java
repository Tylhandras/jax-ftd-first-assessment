package com.cooksys.ftd.assessment.filesharing.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.assessment.filesharing.model.db.UserFiles;
import com.cooksys.ftd.assessment.filesharing.server.ClientHandler;
import com.mysql.cj.api.jdbc.Statement;

public class FileDao extends AbstractDao {
	
	private Logger log = LoggerFactory.getLogger(ClientHandler.class);
	
	public UserFiles addFile(Short user, UserFiles userFile) {		
		try {
			String sql = "INSERT INTO user_files (user_id, absolute_path, file_content) VALUES (?, ?, ?)";
			PreparedStatement stmt = this.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setShort(1, user);
			stmt.setString(2, userFile.getAbsolutePath());
			stmt.setBytes(3, userFile.getFileData());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				userFile.setFileId(rs.getInt(1));
			}
		} catch (SQLException e) {
			log.error("An SQL error occured.", e);
		}
		return userFile;
	}
	
	public List<String> indexFile(Short user) {
		List<String> output = new ArrayList<>();
		
		try {
			String sql = "SELECT absolute_path FROM user_files WHERE user_id = ?";
			PreparedStatement stmt = this.getConn().prepareStatement(sql);
			stmt.setShort(1, user);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String absolutePath = rs.getString("absolute_path");
				output.add(absolutePath);
			}
		} catch (SQLException e) {
			log.error("An SQL error occured.", e);
		}
		return output;
	}
	
	public byte[] sendFile(short user, short fileId) {
		byte[] output = new byte[8192];
		
		try {
			String sql = "SELECT file_content FROM user_files WHERE user_id = ? AND file_id = ?";
			PreparedStatement stmt = this.getConn().prepareStatement(sql);
			stmt.setShort(1, user);
			stmt.setShort(2, fileId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				output = rs.getBytes(1);
			}
		} catch (SQLException e) {
			log.error("An SQL error occured.", e);
		}
		
		return output;
	}
}
