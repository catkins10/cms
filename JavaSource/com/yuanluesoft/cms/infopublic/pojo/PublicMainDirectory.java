package com.yuanluesoft.cms.infopublic.pojo;

import java.util.Set;

/**
 * 信息公开:目录(public_main_directory)
 * @author linchuan
 *
 */
public class PublicMainDirectory extends PublicDirectory {
	private long siteId; //隶属站点ID,仅对根目录有效
	private String siteName; //隶属站点名称,仅对根目录有效
	private char manualCodeEnabled = '0'; //允许手工编制索引号,仅对根目录有效,当手工编制的索引号存在时,就不再自动编号
	private int recodeEnabled; //信息删除后重新编号
	private int sequenceByDirectory; //按目录分类号编制流水号
	private Set guides; //信息公开指南
	private Set unitCodes; //机构代码列表
	
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