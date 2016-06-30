package com.yuanluesoft.cms.capture.forms;

import com.yuanluesoft.cms.capture.model.CapturedRecord;

/**
 * 
 * @author linchuan
 *
 */
public class RecordCaptureTest extends Task {
	private String url; //被抓取的URL
	private CapturedRecord capturedRecord; //字段抓取测试
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the capturedRecord
	 */
	public CapturedRecord getCapturedRecord() {
		return capturedRecord;
	}
	/**
	 * @param capturedRecord the capturedRecord to set
	 */
	public void setCapturedRecord(CapturedRecord capturedRecord) {
		this.capturedRecord = capturedRecord;
	}
}