package com.yuanluesoft.jeaf.tools.filebrowse.forms;

import java.net.URLEncoder;
import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class FileBrowse extends ActionForm {
	private String path;
	private List folders;
	private List files;
	private boolean unzip;
	private String lastUploadFiles;
	private String command; //需要在服务器上执行的命令
	
	public String getEncodedPath() {
		try {
			return URLEncoder.encode(path, "utf-8");
		}
		catch (Exception e) {
			return path;
		}
	}
	
	/**
	 * @return the files
	 */
	public List getFiles() {
		return files;
	}
	/**
	 * @param files the files to set
	 */
	public void setFiles(List files) {
		this.files = files;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the folders
	 */
	public List getFolders() {
		return folders;
	}
	/**
	 * @param folders the folders to set
	 */
	public void setFolders(List folders) {
		this.folders = folders;
	}

	/**
	 * @return the unzip
	 */
	public boolean isUnzip() {
		return unzip;
	}

	/**
	 * @param unzip the unzip to set
	 */
	public void setUnzip(boolean unzip) {
		this.unzip = unzip;
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * @return the lastUploadFiles
	 */
	public String getLastUploadFiles() {
		return lastUploadFiles;
	}

	/**
	 * @param lastUploadFiles the lastUploadFiles to set
	 */
	public void setLastUploadFiles(String lastUploadFiles) {
		this.lastUploadFiles = lastUploadFiles;
	}
}