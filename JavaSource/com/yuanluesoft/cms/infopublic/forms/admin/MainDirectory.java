package com.yuanluesoft.cms.infopublic.forms.admin;

import java.util.Set;

/**
 * 
 * @author yuanluesoft
 *
 */
public class MainDirectory extends Directory {
	private long siteId; //隶属站点ID,仅对根目录有效
	private String siteName; //隶属站点名称,仅对根目录有效
	private char manualCodeEnabled = '0'; //允许手工编制索引号,仅对根目录有效,当手工编制的索引号存在时,就不再自动编号
	private int recodeEnabled = 0; //信息删除后重新编号
	private int sequenceByDirectory; //按目录分类号编制流水号

	private Set directoryColumns; //主目录关联的站点栏目设置
	private Set guides; //信息公开指南
	private Set unitCodes; //机构代码列表
	
	//信息公开指南
	private String guide;
	
	//站点栏目关联配置
	private String columnRelationName; //关联名称
	private long relationColumnId; //关联的栏目ID
	private String relationColumnName; //关联的栏目名称
	private String selectedRelationIds; //选中的ID列表

	/**
	 * @return the columnRelationName
	 */
	public String getColumnRelationName() {
		return columnRelationName;
	}
	/**
	 * @param columnRelationName the columnRelationName to set
	 */
	public void setColumnRelationName(String columnRelationName) {
		this.columnRelationName = columnRelationName;
	}
	/**
	 * @return the directoryColumns
	 */
	public Set getDirectoryColumns() {
		return directoryColumns;
	}
	/**
	 * @param directoryColumns the directoryColumns to set
	 */
	public void setDirectoryColumns(Set directoryColumns) {
		this.directoryColumns = directoryColumns;
	}
	/**
	 * @return the guide
	 */
	public String getGuide() {
		return guide;
	}
	/**
	 * @param guide the guide to set
	 */
	public void setGuide(String guide) {
		this.guide = guide;
	}
	/**
	 * @return the guides
	 */
	public Set getGuides() {
		return guides;
	}
	/**
	 * @param guides the guides to set
	 */
	public void setGuides(Set guides) {
		this.guides = guides;
	}
	/**
	 * @return the manualCodeEnabled
	 */
	public char getManualCodeEnabled() {
		return manualCodeEnabled;
	}
	/**
	 * @param manualCodeEnabled the manualCodeEnabled to set
	 */
	public void setManualCodeEnabled(char manualCodeEnabled) {
		this.manualCodeEnabled = manualCodeEnabled;
	}
	/**
	 * @return the relationColumnId
	 */
	public long getRelationColumnId() {
		return relationColumnId;
	}
	/**
	 * @param relationColumnId the relationColumnId to set
	 */
	public void setRelationColumnId(long relationColumnId) {
		this.relationColumnId = relationColumnId;
	}
	/**
	 * @return the relationColumnName
	 */
	public String getRelationColumnName() {
		return relationColumnName;
	}
	/**
	 * @param relationColumnName the relationColumnName to set
	 */
	public void setRelationColumnName(String relationColumnName) {
		this.relationColumnName = relationColumnName;
	}
	/**
	 * @return the selectedRelationIds
	 */
	public String getSelectedRelationIds() {
		return selectedRelationIds;
	}
	/**
	 * @param selectedRelationIds the selectedRelationIds to set
	 */
	public void setSelectedRelationIds(String selectedRelationIds) {
		this.selectedRelationIds = selectedRelationIds;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the siteName
	 */
	public String getSiteName() {
		return siteName;
	}
	/**
	 * @param siteName the siteName to set
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	/**
	 * @return the recodeEnabled
	 */
	public int getRecodeEnabled() {
		return recodeEnabled;
	}
	/**
	 * @param recodeEnabled the recodeEnabled to set
	 */
	public void setRecodeEnabled(int recodeEnabled) {
		this.recodeEnabled = recodeEnabled;
	}
	/**
	 * @return the unitCodes
	 */
	public Set getUnitCodes() {
		return unitCodes;
	}
	/**
	 * @param unitCodes the unitCodes to set
	 */
	public void setUnitCodes(Set unitCodes) {
		this.unitCodes = unitCodes;
	}
	/**
	 * @return the sequenceByDirectory
	 */
	public int getSequenceByDirectory() {
		return sequenceByDirectory;
	}
	/**
	 * @param sequenceByDirectory the sequenceByDirectory to set
	 */
	public void setSequenceByDirectory(int sequenceByDirectory) {
		this.sequenceByDirectory = sequenceByDirectory;
	}
}