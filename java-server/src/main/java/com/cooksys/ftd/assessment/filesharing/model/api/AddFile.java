package com.cooksys.ftd.assessment.filesharing.model.api;

import java.util.Base64;

import com.cooksys.ftd.assessment.filesharing.dao.FileDao;
import com.cooksys.ftd.assessment.filesharing.dao.UserDao;
import com.cooksys.ftd.assessment.filesharing.model.db.UserFiles;

public class AddFile {
	
	public static ServerResponse<String> newFile(String userInfo, UserDao userDao, FileDao fileDao) {
		UserFiles userFile = new UserFiles();
		ServerResponse<String> output = new ServerResponse<String>();
		String[] args = userInfo.split(" ");
		
		short id = GetUser.getId(args[0], userDao).getData();
		userFile.setAbsolutePath(args[1]);
		userFile.setFileData(Base64.getDecoder().decode(args[2]));
		
		fileDao.addFile(id, userFile);
		
		output.setData("File transfered successfully.");
		
		return output;
	}
}
