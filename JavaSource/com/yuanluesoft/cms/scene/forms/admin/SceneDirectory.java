package com.yuanluesoft.cms.scene.forms.admin;

import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class SceneDirectory extends ActionForm {
	private long parentDirectoryId; //上级目录ID
	private String directoryName; //目录名称
	private float priority; //优先级
	private String directoryType; //类型,场景/链接/内容
	private Set childDirectories; //子目录列表
	
	private String parentDirectoryName; //上级目录名称
	private long sceneServiceId; //场景服务ID
	private long copyToDirectoryId; //拷贝目标目录ID
	
	/**
	 * @return the childDirectories
	 */
	public Set getChildDirectories() {
		return childDirectories;
	}
	/**
	 * @param childDirectories the childDirectories to set
	 */
	public void setChildDirectories(Set childDirectories) {
		this.childDirectories = childDirectories;
	}
	/**
	 * @return the directoryName
	 */
	public String getDirectoryName() {
		return directoryName;
	}
	/**
	 * @param directoryName the directoryName to set
	 */
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	/**
	 * @return the directoryType
	 */
	public String getDirectoryType() {
		return directoryType;
	}
	/**
	 * @param directoryType the directoryType to set
	 */
	public void setDirectoryType(String directoryType) {
		this.directoryType = directoryType;
	}
	/**
	 * @return the parentDirectoryId
	 */
	public long getParentDirectoryId() {
		return parentDirectoryId;
	}
	/**
	 * @param parentDirectoryId the parentDirectoryId to set
	 */
	public void setParentDirectoryId(long parentDirectoryId) {
		this.parentDirectoryId = parentDirectoryId;
	}
	/**
	 * @return the priority
	 */
	public float getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(float priority) {
		this.priority = priority;
	}
	/**
	 * @return the parentDirectoryName
	 */
	public String getParentDirectoryName() {
		return parentDirectoryName;
	}
	/**
	 * @param parentDirectoryName the parentDirectoryName to set
	 */
	public void setParentDirectoryName(String parentDirectoryName) {
		this.parentDirectoryName = parentDirectoryName;
	}
	/**
	 * @return the sceneServiceId
	 */
	public long getSceneServiceId() {
		return sceneServiceId;
	}
	/**
	 * @param sceneServiceId the sceneServiceId to set
	 */
	public void setSceneServiceId(long sceneServiceId) {
		this.sceneServiceId = sceneServiceId;
	}
	/**
	 * @return the copyToDirectoryId
	 */
	public long getCopyToDirectoryId() {
		return copyToDirectoryId;
	}
	/**
	 * @param copyToDirectoryId the copyToDirectoryId to set
	 */
	public void setCopyToDirectoryId(long copyToDirectoryId) {
		this.copyToDirectoryId = copyToDirectoryId;
	}
}