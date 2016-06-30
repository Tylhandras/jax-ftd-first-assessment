package com.cooksys.ftd.assessment.filesharing.model.api;

import java.util.Base64;

import com.cooksys.ftd.assessment.filesharing.dao.FileDao;

public class SendFile {
	
	public static ServerResponse<String> getFile(String userInfo) {
		FileDao fileDao = new FileDao();
		ServerResponse<String> output = new ServerResponse<String>();
		String[] args = userInfo.split(" ");
		
		byte[] data = fileDao.sendFile(Short.parseShort(args[0]), Short.parseShort(args[1]));
		output.setData(Base64.getEncoder().encodeToString(data));
		
		return output;
	}

}
