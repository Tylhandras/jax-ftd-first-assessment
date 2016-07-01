package com.cooksys.ftd.assessment.filesharing.model.api;

import java.util.Optional;

import com.cooksys.ftd.assessment.filesharing.dao.UserDao;

public class GetUser {
	
	public static ServerResponse<String> getPassword(String username, UserDao userDao) {
		
		ServerResponse<String> output = new ServerResponse<String>();
		Optional<String> temp = userDao.getUserPassword(username); 
		
		output.setData(temp.get());
		
		return output;
	}
	
	public static ServerResponse<Short> getId(String username, UserDao userDao) {
		
		ServerResponse<Short> output = new ServerResponse<Short>();
		Optional<Short> temp = userDao.getUserId(username); 
		
		if (temp.isPresent()) {
			output.setData(temp.get());
		} else {
			output.setData((short) 0);
		}
		
		return output;
	}

}
