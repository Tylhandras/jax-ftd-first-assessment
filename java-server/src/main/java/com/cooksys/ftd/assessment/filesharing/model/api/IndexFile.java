package com.cooksys.ftd.assessment.filesharing.model.api;

import java.util.List;

import com.cooksys.ftd.assessment.filesharing.dao.FileDao;
import com.cooksys.ftd.assessment.filesharing.dao.UserDao;

public class IndexFile {
	
	public static ServerResponse<List<String>> getFileList(String userInfo, UserDao userDao, FileDao fileDao) {
		ServerResponse<List<String>> output = new ServerResponse<List<String>>();
		short id = GetUser.getId(userInfo, userDao).getData();
		
		output.setData(fileDao.indexFile(id));
		
		return output;
	}

}
