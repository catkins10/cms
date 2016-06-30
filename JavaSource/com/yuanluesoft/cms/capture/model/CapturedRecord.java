package com.yuanluesoft.cms.capture.model;

import java.util.List;

/**
 * 抓取到的记录
 * @author linchuan
 *
 */
public class CapturedRecord {
	private String url; //URL
	private List fields; //字段(CaptureField)列表
	private Object record; //记录
	
	/**
	 * @return the fields
	 */
	public List getFields() {
		return fields;
	}
	/**
	 * @param fields the fields to set
	 */
	public void setFields(List fields) {
		this.fields = fields;
	}
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
	 * @return the record
	 */
	public Object getRecord() {
		return record;
	}
	/**
	 * @param record the record to set
	 */
	public void setRecord(Object record) {
		this.record = record;
	}
}