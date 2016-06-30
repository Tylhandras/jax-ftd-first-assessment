package com.cooksys.ftd.assessment.filesharing.model.api;

import java.util.List;

import com.cooksys.ftd.assessment.filesharing.dao.FileDao;

public class IndexFile {
	
	public static ServerResponse<List<String>> getFileList(String userInfo) {
		ServerResponse<List<String>> output = new ServerResponse<List<String>>();
		FileDao fileDao = new FileDao();
		short id = GetUser.getId(userInfo).getData();
		
		output.setData(fileDao.indexFile(id));
		
		return output;
	}

}
