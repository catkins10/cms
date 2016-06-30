package com.yuanluesoft.bbs.forum.model;

import java.util.List;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Category {
	private long id;
	private String name; //分类名称
	private String description; //描述
	private List managers; //版主列表
	private List forums; //版块列表
	/**
	 * @return the forums
	 */
	public List getForums() {
		return forums;
	}
	/**
	 * @param forums the forums to set
	 */
	public void setForums(List forums) {
		this.forums = forums;
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
	 * @return the managers
	 */
	public List getManagers() {
		return managers;
	}
	/**
	 * @param managers the managers to set
	 */
	public void setManagers(List managers) {
		this.managers = managers;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
}
