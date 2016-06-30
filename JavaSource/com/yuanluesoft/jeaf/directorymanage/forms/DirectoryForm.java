package com.yuanluesoft.jeaf.directorymanage.forms;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 目录
 * @author linchuan
 *
 */
public class DirectoryForm extends ActionForm {
	private String directoryName; //目录名称
	private long parentDirectoryId; //上级目录ID
	private float priority; //优先级
	private String directoryType; //目录类型
	private String creator; //创建人
	private long creatorId; //创建人ID
	private Timestamp created; //创建时间
	private String remark; //备注
	private Set subjections; //目录的上级目录
	private Set childSubjections; //目录的下级目录
	private Set directoryPopedoms; //目录权限配置
	
	//扩展属性
	private String parentDirectoryName; //上级目录名称
	private List popedomConfigs; //权限配置列表
	private boolean changeParentDirectoryDisabled; //是否禁止修改父目录,如：没有上级目录的管理权限
	private String linkedDirectoryIds; //引用的目录ID列表
	private String linkedDirectoryNames; //引用的目录名称列表
	
	/**
	 * @return the childSubjections
	 */
	public Set getChildSubjections() {
		return childSubjections;
	}
	/**
	 * @param childSubjections the childSubjections to set
	 */
	public void setChildSubjections(Set childSubjections) {
		this.childSubjections = childSubjections;
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
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
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
	 * @return the directoryPopedoms
	 */
	public Set getDirectoryPopedoms() {
		return directoryPopedoms;
	}
	/**
	 * @param directoryPopedoms the directoryPopedoms to set
	 */
	public void setDirectoryPopedoms(Set directoryPopedoms) {
		this.directoryPopedoms = directoryPopedoms;
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
	 * @return the parentName
	 */
	public String getParentDirectoryName() {
		return parentDirectoryName;
	}
	/**
	 * @param parentName the parentName to set
	 */
	public void setParentDirectoryName(String parentName) {
		this.parentDirectoryName = parentName;
	}
	/**
	 * @return the popedomConfigs
	 */
	public List getPopedomConfigs() {
		return popedomConfigs;
	}
	/**
	 * @param popedomConfigs the popedomConfigs to set
	 */
	public void setPopedomConfigs(List popedomConfigs) {
		this.popedomConfigs = popedomConfigs;
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
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the subjections
	 */
	public Set getSubjections() {
		return subjections;
	}
	/**
	 * @param subjections the subjections to set
	 */
	public void setSubjections(Set subjections) {
		this.subjections = subjections;
	}
	/**
	 * @return the changeParentDirectoryDisabled
	 */
	public boolean isChangeParentDirectoryDisabled() {
		return changeParentDirectoryDisabled;
	}
	/**
	 * @param changeParentDirectoryDisabled the changeParentDirectoryDisabled to set
	 */
	public void setChangeParentDirectoryDisabled(
			boolean changeParentDirectoryDisabled) {
		this.changeParentDirectoryDisabled = changeParentDirectoryDisabled;
	}
	/**
	 * @return the linkedDirectoryIds
	 */
	public String getLinkedDirectoryIds() {
		return linkedDirectoryIds;
	}
	/**
	 * @param linkedDirectoryIds the linkedDirectoryIds to set
	 */
	public void setLinkedDirectoryIds(String linkedDirectoryIds) {
		this.linkedDirectoryIds = linkedDirectoryIds;
	}
	/**
	 * @return the linkedDirectoryNames
	 */
	public String getLinkedDirectoryNames() {
		return linkedDirectoryNames;
	}
	/**
	 * @param linkedDirectoryNames the linkedDirectoryNames to set
	 */
	public void setLinkedDirectoryNames(String linkedDirectoryNames) {
		this.linkedDirectoryNames = linkedDirectoryNames;
	}
}
