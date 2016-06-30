package com.yuanluesoft.cms.capture.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 抓取日志(cms_capture_log)
 * @author linchuan
 *
 */
public class CmsCaptureLog extends Record {
	private long captureTaskId; //任务ID
	private String captureTaskDescription; //任务说明
	private Timestamp captureTime; //抓取时间
	private String captureHourMinute; //抓取时段
	private String captureUrl; //抓取的URL
	private char captureSuccess; //抓取是否成功
	
	/**
	 * 是否成功
	 * @return
	 */
	public String getSuccess() {
		return captureSuccess=='1' ? "√" : "";
	}
	
	/**
	 * @return the captureSuccess
	 */
	public char getCaptureSuccess() {
		return captureSuccess;
	}
	/**
	 * @param captureSuccess the captureSuccess to set
	 */
	public void setCaptureSuccess(char captureSuccess) {
		this.captureSuccess = captureSuccess;
	}
	/**
	 * @return the captureTime
	 */
	public Timestamp getCaptureTime() {
		return captureTime;
	}
	/**
	 * @param captureTime the captureTime to set
	 */
	public void setCaptureTime(Timestamp captureTime) {
		this.captureTime = captureTime;
	}
	/**
	 * @return the captureUrl
	 */
	public String getCaptureUrl() {
		return captureUrl;
	}
	/**
	 * @param captureUrl the captureUrl to set
	 */
	public void setCaptureUrl(String captureUrl) {
		this.captureUrl = captureUrl;
	}

	/**
	 * @return the captureHourMinute
	 */
	public String getCaptureHourMinute() {
		return captureHourMinute;
	}

	/**
	 * @param captureHourMinute the captureHourMinute to set
	 */
	public void setCaptureHourMinute(String captureHourMinute) {
		this.captureHourMinute = captureHourMinute;
	}

	/**
	 * @return the captureTaskDescription
	 */
	public String getCaptureTaskDescription() {
		return captureTaskDescription;
	}

	/**
	 * @param captureTaskDescription the captureTaskDescription to set
	 */
	public void setCaptureTaskDescription(String captureTaskDescription) {
		this.captureTaskDescription = captureTaskDescription;
	}

	/**
	 * @return the captureTaskId
	 */
	public long getCaptureTaskId() {
		return captureTaskId;
	}

	/**
	 * @param captureTaskId the captureTaskId to set
	 */
	public void setCaptureTaskId(long captureTaskId) {
		this.captureTaskId = captureTaskId;
	}
}