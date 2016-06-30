package com.yuanluesoft.msa.exam.forms.admin;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class ImportData extends ActionForm {
	private String examName; //考试名称,从导入的文件名解析
	private long creatorId; //创建者ID
	private String creator; //创建者
	private Timestamp created; //创建时间
	
	private boolean deleteData; //删除记录时是否删除导入的数据
	
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
	 * @return the examName
	 */
	public String getExamName() {
		return examName;
	}
	/**
	 * @param examName the examName to set
	 */
	public void setExamName(String examName) {
		this.examName = examName;
	}
	/**
	 * @return the deleteData
	 */
	public boolean isDeleteData() {
		return deleteData;
	}
	/**
	 * @param deleteData the deleteData to set
	 */
	public void setDeleteData(boolean deleteData) {
		this.deleteData = deleteData;
	}
}