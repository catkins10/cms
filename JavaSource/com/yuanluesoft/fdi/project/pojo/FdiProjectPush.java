package com.yuanluesoft.fdi.project.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 意向项目:推进情况(fdi_project_push)
 * @author linchuan
 *
 */
public class FdiProjectPush extends Record {
	private long projectId; //项目ID
	private Timestamp pushTime; //时间
	private String transactor; //经办人
	private String content; //洽谈与推进内容
	
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the projectId
	 */
	public long getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return the pushTime
	 */
	public Timestamp getPushTime() {
		return pushTime;
	}
	/**
	 * @param pushTime the pushTime to set
	 */
	public void setPushTime(Timestamp pushTime) {
		this.pushTime = pushTime;
	}
	/**
	 * @return the transactor
	 */
	public String getTransactor() {
		return transactor;
	}
	/**
	 * @param transactor the transactor to set
	 */
	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}
}