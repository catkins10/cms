package com.yuanluesoft.fet.tradestat.forms.admin;

import java.sql.Timestamp;

import org.apache.struts.upload.FormFile;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author yuanluesoft
 *
 */
public class UploadData extends ActionForm {
	private long transactorId; //经办人ID
	private String transactor; //经办人
	private Timestamp uploadTime; //上传时间
	private char isExport = '0'; //进口/出口
	private int dataYear; //年度
	private int dataMonth; //月份
	private FormFile upload;
	
	/**
	 * @return the dataMonth
	 */
	public int getDataMonth() {
		return dataMonth;
	}
	/**
	 * @param dataMonth the dataMonth to set
	 */
	public void setDataMonth(int dataMonth) {
		this.dataMonth = dataMonth;
	}
	/**
	 * @return the dataYear
	 */
	public int getDataYear() {
		return dataYear;
	}
	/**
	 * @param dataYear the dataYear to set
	 */
	public void setDataYear(int dataYear) {
		this.dataYear = dataYear;
	}
	/**
	 * @return the isExport
	 */
	public char getIsExport() {
		return isExport;
	}
	/**
	 * @param isExport the isExport to set
	 */
	public void setIsExport(char isExport) {
		this.isExport = isExport;
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
	/**
	 * @return the transactorId
	 */
	public long getTransactorId() {
		return transactorId;
	}
	/**
	 * @param transactorId the transactorId to set
	 */
	public void setTransactorId(long transactorId) {
		this.transactorId = transactorId;
	}
	/**
	 * @return the uploadTime
	 */
	public Timestamp getUploadTime() {
		return uploadTime;
	}
	/**
	 * @param uploadTime the uploadTime to set
	 */
	public void setUploadTime(Timestamp uploadTime) {
		this.uploadTime = uploadTime;
	}
	/**
	 * @return the upload
	 */
	public FormFile getUpload() {
		return upload;
	}
	/**
	 * @param upload the upload to set
	 */
	public void setUpload(FormFile upload) {
		this.upload = upload;
	}
}