package com.yuanluesoft.jeaf.tools.filebrowse.model;

import java.net.URLEncoder;

/**
 * 
 * @author linchuan
 *
 */
public class Folder {
	private String name;
	private String path;
	
	public String getEncodedPath() {
		try {
			return URLEncoder.encode(path, "utf-8");
		}
		catch (Exception e) {
			return path;
		}
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	
}
