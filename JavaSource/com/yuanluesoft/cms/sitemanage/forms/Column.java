package com.yuanluesoft.cms.sitemanage.forms;

import java.util.Set;


/**
 * 
 * 
 * @author linchuan
 *
 */
public class Column extends WebDirectory {
	private Set relationLinks; //相关链接
	private String relationColumnIds; //相关的栏目ID
	private String relationColumnNames; //相关的栏目名称

	/**
	 * @return the relationLinks
	 */
	public Set getRelationLinks() {
		return relationLinks;
	}

	/**
	 * @param relationLinks the relationLinks to set
	 */
	public void setRelationLinks(Set relationLinks) {
		this.relationLinks = relationLinks;
	}

	/**
	 * @return the relationColumnIds
	 */
	public String getRelationColumnIds() {
		return relationColumnIds;
	}

	/**
	 * @param relationColumnIds the relationColumnIds to set
	 */
	public void setRelationColumnIds(String relationColumnIds) {
		this.relationColumnIds = relationColumnIds;
	}

	/**
	 * @return the relationColumnNames
	 */
	public String getRelationColumnNames() {
		return relationColumnNames;
	}

	/**
	 * @param relationColumnNames the relationColumnNames to set
	 */
	public void setRelationColumnNames(String relationColumnNames) {
		this.relationColumnNames = relationColumnNames;
	}
}