package com.cooksys.ftd.assessment.filesharing.model.db;

public class UserFiles {
	
	private Integer fileId;
	private String absolutePath;
	private byte[] fileData;
	
	public UserFiles() {
		super();
	}
	
	public UserFiles(Integer fileId, String absolutePath, byte[] fileData) {
		super();
		this.fileId = fileId;
		this.absolutePath = absolutePath;
		this.fileData = fileData;
	}
	
	public Integer getFileId() {
		return fileId;
	}
	
	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}
	
	public String getAbsolutePath() {
		return absolutePath;
	}
	
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	
	public byte[] getFileData() {
		return fileData;
	}
	
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((absolutePath == null) ? 0 : absolutePath.hashCode());
		result = prime * result + ((fileData == null) ? 0 : fileData.hashCode());
		result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserFiles other = (UserFiles) obj;
		if (absolutePath == null) {
			if (other.absolutePath != null)
				return false;
		} else if (!absolutePath.equals(other.absolutePath))
			return false;
		if (fileData == null) {
			if (other.fileData != null)
				return false;
		} else if (!fileData.equals(other.fileData))
			return false;
		if (fileId == null) {
			if (other.fileId != null)
				return false;
		} else if (!fileId.equals(other.fileId))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "UserFiles [fileId=" + fileId + ", absolutePath=" + absolutePath + ", fileData=" + fileData + "]";
	}
}
