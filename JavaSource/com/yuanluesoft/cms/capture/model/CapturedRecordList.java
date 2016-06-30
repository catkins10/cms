package com.yuanluesoft.cms.capture.model;

import java.util.List;

/**
 * 抓取到的记录列表
 * @author linchuan
 *
 */
public class CapturedRecordList {
	private List records; //记录(RecordPage)列表
	private String nextPageURL; //下一页的URL
	
	/**
	 * @return the nextPageURL
	 */
	public String getNextPageURL() {
		return nextPageURL;
	}
	/**
	 * @param nextPageURL the nextPageURL to set
	 */
	public void setNextPageURL(String nextPageURL) {
		this.nextPageURL = nextPageURL;
	}
	/**
	 * @return the records
	 */
	public List getRecords() {
		return records;
	}
	/**
	 * @param records the records to set
	 */
	public void setRecords(List records) {
		this.records = records;
	}
}