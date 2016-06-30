package com.yuanluesoft.cms.infopublic.pojo;

import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;

/**
 * 信息公开:目录(public_directory)
 * @author linchuan
 *
 */
public class PublicDirectory extends Directory {
	private String code; //类目代码,当类型为根目录时，填写机构代码，否则是目录代码
	private String description; //描述
	private String synchSiteIds; //同步更新到网站栏目列表
	private long workflowId; //绑定的流程ID,添加信息时,查找当前目录绑定的流程，如果没有则查询上级目录
	private String workflowName; //绑定的流程名称
	private char editorDeleteable = '0'; //允许编辑删除,0/从上级继承,1/允许,2/不允许
	private char editorReissueable = '0'; //允许编辑撤销,0/从上级继承,1/允许,2/不允许
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
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
	 * @return the synchSiteIds
	 */
	public String getSynchSiteIds() {
		return synchSiteIds;
	}
	/**
	 * @param synchSiteIds the synchSiteIds to set
	 */
	public void setSynchSiteIds(String synchSiteIds) {
		this.synchSiteIds = synchSiteIds;
	}
	/**
	 * @return the workflowId
	 */
	public long getWorkflowId() {
		return workflowId;
	}
	/**
	 * @param workflowId the workflowId to set
	 */
	public void setWorkflowId(long workflowId) {
		this.workflowId = workflowId;
	}
	/**
	 * @return the workflowName
	 */
	public String getWorkflowName() {
		return workflowName;
	}
	/**
	 * @param workflowName the workflowName to set
	 */
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}
	/**
	 * @return the editorDeleteable
	 */
	public char getEditorDeleteable() {
		return editorDeleteable;
	}
	/**
	 * @param editorDeleteable the editorDeleteable to set
	 */
	public void setEditorDeleteable(char editorDeleteable) {
		this.editorDeleteable = editorDeleteable;
	}
	/**
	 * @return the editorReissueable
	 */
	public char getEditorReissueable() {
		return editorReissueable;
	}
	/**
	 * @param editorReissueable the editorReissueable to set
	 */
	public void setEditorReissueable(char editorReissueable) {
		this.editorReissueable = editorReissueable;
	}
}
