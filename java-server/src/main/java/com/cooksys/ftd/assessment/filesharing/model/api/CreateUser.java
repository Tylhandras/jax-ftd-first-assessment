package com.cooksys.ftd.assessment.filesharing.model.api;

import com.cooksys.ftd.assessment.filesharing.dao.UserDao;
import com.cooksys.ftd.assessment.filesharing.model.db.User;

public class CreateUser {

	public static ServerResponse<String> newUser(String userInfo, UserDao userDao) {
		User user = new User();
		ServerResponse<String> output = new ServerResponse<String>();
		String[] args = userInfo.split(" ");
		short check = GetUser.getId(args[0], userDao).getData();
		
		if (check == 0) {
			user.setUsername(args[0]);
			user.setPassword(args[1]);
			userDao.createUser(user);
			output.setData("Username successfully created.");
		} else {
			output.setError(true);
			output.setData("Username already exists.");
		}
			
		return output;
	}
}
