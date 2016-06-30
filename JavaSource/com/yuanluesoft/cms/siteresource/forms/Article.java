package com.yuanluesoft.cms.siteresource.forms;


/**
 * 
 * 
 * @author linchuan
 *
 */
public class Article extends Resource {
	private String relationResourceIds; //相关的资源ID
	private String relationResourceSubjects; //相关的资源标题
	private String relationResourceColumnIds; //关联资源所在栏目
	
	/**
	 * @return the relationResourceIds
	 */
	public String getRelationResourceIds() {
		return relationResourceIds;
	}
	/**
	 * @param relationResourceIds the relationResourceIds to set
	 */
	public void setRelationResourceIds(String relationResourceIds) {
		this.relationResourceIds = relationResourceIds;
	}
	/**
	 * @return the relationResourceSubjects
	 */
	public String getRelationResourceSubjects() {
		return relationResourceSubjects;
	}
	/**
	 * @param relationResourceSubjects the relationResourceSubjects to set
	 */
	public void setRelationResourceSubjects(String relationResourceSubjects) {
		this.relationResourceSubjects = relationResourceSubjects;
	}
	/**
	 * @return the relationResourceColumnIds
	 */
	public String getRelationResourceColumnIds() {
		return relationResourceColumnIds;
	}
	/**
	 * @param relationResourceColumnIds the relationResourceColumnIds to set
	 */
	public void setRelationResourceColumnIds(String relationResourceColumnIds) {
		this.relationResourceColumnIds = relationResourceColumnIds;
	}
}