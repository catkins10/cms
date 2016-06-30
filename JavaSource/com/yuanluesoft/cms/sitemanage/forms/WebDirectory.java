package com.yuanluesoft.cms.sitemanage.forms;

import java.util.Set;

import com.yuanluesoft.jeaf.directorymanage.forms.DirectoryForm;

/**
 * 
 * 
 * @author linchuan
 *
 */
public class WebDirectory extends DirectoryForm {
	private String shortName; //目录简称(英文)
	private String description; //描述
	private long workflowId; //绑定的流程ID,添加信息时,查找当前目录绑定的流程，如果没有则查询上级目录
	private String workflowName; //绑定的流程名称
	private Set synchToDirectories; //同步到其他栏目
	private Set synchFromDirectories; //同步到本栏目的其他栏目
	private char editorDeleteable = '0'; //允许编辑删除,0/从上级继承,1/允许,2/不允许
	private char editorReissueable = '0'; //允许编辑撤销,0/从上级继承,1/允许,2/不允许
	private char anonymousLevel = '0'; //匿名用户访问级别,0/从上级继承, 1/不能访问,2/仅标题,3/完全访问
	private char synchIssue = '0'; //同步的文章发布,0/从上级继承,1/直接发布,2/相同站点的直接发布,3/不直接发布
	private char halt = '0'; //是否停用,对外不展现
	private String redirectUrl; //重定向URL
	
	private String synchToDirectoryIds; //同步到的其他栏目ID列表
	private String synchToDirectoryNames; //同步到的其他栏目名称列表
	private String synchFromDirectoryIds; //同步到本栏目的其他栏目ID列表
	private String synchFromDirectoryNames; //同步到本栏目的其他栏目名称列表
	
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
	 * @return the synchFromDirectories
	 */
	public Set getSynchFromDirectories() {
		return synchFromDirectories;
	}

	/**
	 * @param synchFromDirectories the synchFromDirectories to set
	 */
	public void setSynchFromDirectories(Set synchFromDirectories) {
		this.synchFromDirectories = synchFromDirectories;
	}

	/**
	 * @return the synchFromDirectoryIds
	 */
	public String getSynchFromDirectoryIds() {
		return synchFromDirectoryIds;
	}

	/**
	 * @param synchFromDirectoryIds the synchFromDirectoryIds to set
	 */
	public void setSynchFromDirectoryIds(String synchFromDirectoryIds) {
		this.synchFromDirectoryIds = synchFromDirectoryIds;
	}

	/**
	 * @return the synchFromDirectoryNames
	 */
	public String getSynchFromDirectoryNames() {
		return synchFromDirectoryNames;
	}

	/**
	 * @param synchFromDirectoryNames the synchFromDirectoryNames to set
	 */
	public void setSynchFromDirectoryNames(String synchFromDirectoryNames) {
		this.synchFromDirectoryNames = synchFromDirectoryNames;
	}

	/**
	 * @return the synchToDirectories
	 */
	public Set getSynchToDirectories() {
		return synchToDirectories;
	}

	/**
	 * @param synchToDirectories the synchToDirectories to set
	 */
	public void setSynchToDirectories(Set synchToDirectories) {
		this.synchToDirectories = synchToDirectories;
	}

	/**
	 * @return the synchToDirectoryIds
	 */
	public String getSynchToDirectoryIds() {
		return synchToDirectoryIds;
	}

	/**
	 * @param synchToDirectoryIds the synchToDirectoryIds to set
	 */
	public void setSynchToDirectoryIds(String synchToDirectoryIds) {
		this.synchToDirectoryIds = synchToDirectoryIds;
	}

	/**
	 * @return the synchToDirectoryNames
	 */
	public String getSynchToDirectoryNames() {
		return synchToDirectoryNames;
	}

	/**
	 * @param synchToDirectoryNames the synchToDirectoryNames to set
	 */
	public void setSynchToDirectoryNames(String synchToDirectoryNames) {
		this.synchToDirectoryNames = synchToDirectoryNames;
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

	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * @return the anonymousLevel
	 */
	public char getAnonymousLevel() {
		return anonymousLevel;
	}

	/**
	 * @param anonymousLevel the anonymousLevel to set
	 */
	public void setAnonymousLevel(char anonymousLevel) {
		this.anonymousLevel = anonymousLevel;
	}

	/**
	 * @return the halt
	 */
	public char getHalt() {
		return halt;
	}

	/**
	 * @param halt the halt to set
	 */
	public void setHalt(char halt) {
		this.halt = halt;
	}

	/**
	 * @return the redirectUrl
	 */
	public String getRedirectUrl() {
		return redirectUrl;
	}

	/**
	 * @param redirectUrl the redirectUrl to set
	 */
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	/**
	 * @return the synchIssue
	 */
	public char getSynchIssue() {
		return synchIssue;
	}

	/**
	 * @param synchIssue the synchIssue to set
	 */
	public void setSynchIssue(char synchIssue) {
		this.synchIssue = synchIssue;
	}
}