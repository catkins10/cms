package com.yuanluesoft.cms.sitemanage.pojo;

import java.util.Set;

import com.yuanluesoft.jeaf.directorymanage.pojo.Directory;
import com.yuanluesoft.jeaf.stat.pojo.AccessStat;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 站点管理:目录(cms_directory)
 * @author linchuan
 *
 */
public class WebDirectory extends Directory {
	private String shortName; //目录简称(英文)
	private String description; //描述
	private long workflowId; //绑定的流程ID,添加信息时,查找当前目录绑定的流程，如果没有则查询上级目录
	private String workflowName; //绑定的流程名称
	private char editorDeleteable = '0'; //允许编辑删除,0/从上级继承,1/允许,2/不允许
	private char editorReissueable = '0'; //允许编辑撤销,0/从上级继承,1/允许,2/不允许
	private char anonymousLevel = '0'; //匿名用户访问级别,0/从上级继承,1/不能访问,2/仅标题,3/完全访问
	private char synchIssue = '0'; //同步的文章发布,0/从上级继承,1/直接发布,2/相同站点的直接发布,3/不直接发布
	private char halt = '0'; //是否停用,对外不展现
	private String redirectUrl; //重定向URL
	private Set accessStats; //访问统计
	private Set synchToDirectories; //同步到其他栏目
	private Set synchFromDirectories; //同步到本栏目的其他栏目
	
	//扩展属性
	private int resourceCount; //文章数量
	
	/**
	 * 获取访问次数
	 * @return
	 */
	public long getAccessCount() {
		try {
			return ((AccessStat)accessStats.iterator().next()).getTimes();
		}
		catch(Exception e) {
			return 0;
		}
	}
	
	/**
	 * 获取URL
	 * @return
	 */
	public String getUrl() {
		if(redirectUrl!=null && !redirectUrl.isEmpty()) {
			return "{FINAL}" + redirectUrl;
		}
		return "{FINAL}" + Environment.getContextPath() + "/cms/sitemanage/index.shtml?siteId=" + getId();
	}

	/**
	 * @return the accessStats
	 */
	public Set getAccessStats() {
		return accessStats;
	}

	/**
	 * @param accessStats the accessStats to set
	 */
	public void setAccessStats(Set accessStats) {
		this.accessStats = accessStats;
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
	 * @return the resourceCount
	 */
	public int getResourceCount() {
		return resourceCount;
	}

	/**
	 * @param resourceCount the resourceCount to set
	 */
	public void setResourceCount(int resourceCount) {
		this.resourceCount = resourceCount;
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
