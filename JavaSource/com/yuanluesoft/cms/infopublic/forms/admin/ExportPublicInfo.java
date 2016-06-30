package com.yuanluesoft.cms.infopublic.forms.admin;

import java.sql.Date;
import java.util.List;

import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * @author linchuan
 *
 */
public class ExportPublicInfo extends ViewForm {
	private Date beginDate;
	private Date endDate;
	private String directoryName; //目录
	private long directoryId; //目录ID
	private List publicInfos; //信息列表
	
	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}
	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the publicInfos
	 */
	public List getPublicInfos() {
		return publicInfos;
	}
	/**
	 * @param publicInfos the publicInfos to set
	 */
	public void setPublicInfos(List publicInfos) {
		this.publicInfos = publicInfos;
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
}