package com.yuanluesoft.cms.scene.pojo;

import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 场景服务:场景目录(scene_directory)
 * @author linchuan
 *
 */
public class SceneDirectory extends Record {
	private long parentDirectoryId; //上级目录ID
	private String directoryName; //目录名称
	private float priority; //优先级
	private String directoryType; //类型,场景/链接/内容
	private Set childDirectories; //子目录列表
	
	private SceneService sceneService; //场景服务,页面显示时使用
	
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
	 * @return the sceneService
	 */
	public SceneService getSceneService() {
		return sceneService;
	}
	/**
	 * @param sceneService the sceneService to set
	 */
	public void setSceneService(SceneService sceneService) {
		this.sceneService = sceneService;
	}
}