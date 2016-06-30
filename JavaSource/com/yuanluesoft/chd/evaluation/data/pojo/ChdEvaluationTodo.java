package com.yuanluesoft.chd.evaluation.data.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 待办事项(chd_evaluation_todo)
 * @author linchuan
 *
 */
public class ChdEvaluationTodo extends Record {
	private String task; //事项名称
	private long directoryId; //目录ID
	private String directorName; //目录名称
	private char taskType = '0'; //事项类型, 资料提交|0,自查|1
	private Timestamp created; //创建时间
	private String actionName; //操作名称
	private String actionLink; //操作链接
	private Set visitors; //访问者列表
	/**
	 * @return the actionLink
	 */
	public String getActionLink() {
		return actionLink;
	}
	/**
	 * @param actionLink the actionLink to set
	 */
	public void setActionLink(String actionLink) {
		this.actionLink = actionLink;
	}
	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return actionName;
	}
	/**
	 * @param actionName the actionName to set
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the task
	 */
	public String getTask() {
		return task;
	}
	/**
	 * @param task the task to set
	 */
	public void setTask(String task) {
		this.task = task;
	}
	/**
	 * @return the visitors
	 */
	public Set getVisitors() {
		return visitors;
	}
	/**
	 * @param visitors the visitors to set
	 */
	public void setVisitors(Set visitors) {
		this.visitors = visitors;
	}
	/**
	 * @return the directorName
	 */
	public String getDirectorName() {
		return directorName;
	}
	/**
	 * @param directorName the directorName to set
	 */
	public void setDirectorName(String directorName) {
		this.directorName = directorName;
	}
	/**
	 * @return the directoryId
	 */
	public long getDirectoryId() {
		return directoryId;
	}
	/**
	 * @param directoryId the directoryId to set
	 */
	public void setDirectoryId(long directoryId) {
		this.directoryId = directoryId;
	}
	/**
	 * @return the taskType
	 */
	public char getTaskType() {
		return taskType;
	}
	/**
	 * @param taskType the taskType to set
	 */
	public void setTaskType(char taskType) {
		this.taskType = taskType;
	}
}
