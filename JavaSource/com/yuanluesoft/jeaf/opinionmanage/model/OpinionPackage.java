/*
 * Created on 2005-9-13
 *
 */
package com.yuanluesoft.jeaf.opinionmanage.model;

import java.util.Collection;
import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class OpinionPackage {
	//意见操作类型
	public static final String OPINION_ACTION_SAVE = "saveOpinion"; //保存意见
	public static final String OPINION_ACTION_APPEND_OFTEN_USE = "appendOftenUseOpinion"; //添加常用意见
	public static final String OPINION_ACTION_DELETE_OFTEN_USE = "deleteOftenUseOpinion"; //删除常用意见
	
	private List opinionTypes; //意见类型列表,按优先级排序
	private Collection opinionList; //意见列表
	private List oftenUseOpinions; //常用意见

	private String opinionAction; //当前需要执行的意见操作,saveOpinion/appendOftenUseOpinion/deleteOftenUseOpinion
	private String opinion; //输入的意见
	private String selectedOftenUseOpinion; //常用意见选择
	private String opinionType; //当前用户可填写的意见类型
	private long opinionId; //意见记录ID
	private String writeOpinionActionName = "writeOpinion";
	
	private boolean modifiable; //是否允许修改
	private String mainRecordClassName; //主记录类名称
	
	/**
	 * 是否有效，用于判断是否显示意见TAB
	 * @return
	 */
	public boolean isDisable() {
		return (opinionList==null || opinionList.isEmpty())  && opinionType==null;
	}
	/**
	 * @return Returns the opinion.
	 */
	public String getOpinion() {
		return opinion;
	}
	/**
	 * @param opinion The opinion to set.
	 */
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	/**
	 * @return Returns the opinionList.
	 */
	public Collection getOpinionList() {
		return opinionList;
	}
	/**
	 * @param opinionList The opinionList to set.
	 */
	public void setOpinionList(Collection opinionList) {
		this.opinionList = opinionList;
	}
	/**
	 * @return Returns the oftenUsedOpinions.
	 */
	public List getOftenUseOpinions() {
		return oftenUseOpinions;
	}
	/**
	 * @param oftenUsedOpinions The oftenUsedOpinions to set.
	 */
	public void setOftenUseOpinions(List oftenUsedOpinions) {
		this.oftenUseOpinions = oftenUsedOpinions;
	}
	/**
	 * @return Returns the opinionType.
	 */
	public String getOpinionType() {
		return opinionType;
	}
	/**
	 * @param opinionType The opinionType to set.
	 */
	public void setOpinionType(String opinionType) {
		this.opinionType = opinionType;
	}
	/**
	 * @return Returns the opinionId.
	 */
	public long getOpinionId() {
		return opinionId;
	}
	/**
	 * @param opinionId The opinionId to set.
	 */
	public void setOpinionId(long opinionId) {
		this.opinionId = opinionId;
	}
	/**
	 * @return Returns the opinionTypes.
	 */
	public List getOpinionTypes() {
		return opinionTypes;
	}
	/**
	 * @param opinionTypes The opinionTypes to set.
	 */
	public void setOpinionTypes(List opinionTypes) {
		this.opinionTypes = opinionTypes;
	}
	/**
	 * @return Returns the selectedOftenUseOpinion.
	 */
	public String getSelectedOftenUseOpinion() {
		return selectedOftenUseOpinion;
	}
	/**
	 * @param selectedOftenUseOpinion The selectedOftenUseOpinion to set.
	 */
	public void setSelectedOftenUseOpinion(String selectedOftenUseOpinion) {
		this.selectedOftenUseOpinion = selectedOftenUseOpinion;
	}
	/**
	 * @return the opinionAction
	 */
	public String getOpinionAction() {
		return opinionAction;
	}
	/**
	 * @param opinionAction the opinionAction to set
	 */
	public void setOpinionAction(String opinionAction) {
		this.opinionAction = opinionAction;
	}
	/**
	 * @return the writeOpinionActionName
	 */
	public String getWriteOpinionActionName() {
		return writeOpinionActionName;
	}
	/**
	 * @param writeOpinionActionName the writeOpinionActionName to set
	 */
	public void setWriteOpinionActionName(String writeOpinionActionName) {
		this.writeOpinionActionName = writeOpinionActionName;
	}
	/**
	 * @return the mainRecordClassName
	 */
	public String getMainRecordClassName() {
		return mainRecordClassName;
	}
	/**
	 * @param mainRecordClassName the mainRecordClassName to set
	 */
	public void setMainRecordClassName(String mainRecordClassName) {
		this.mainRecordClassName = mainRecordClassName;
	}
	/**
	 * @return the modifiable
	 */
	public boolean isModifiable() {
		return modifiable;
	}
	/**
	 * @param modifiable the modifiable to set
	 */
	public void setModifiable(boolean modifiable) {
		this.modifiable = modifiable;
	}
}
