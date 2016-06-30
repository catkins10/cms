package com.yuanluesoft.cms.capture.forms;

import com.yuanluesoft.cms.capture.model.CapturedRecordList;

/**
 * 
 * @author linchuan
 *
 */
public class ListCaptureTest extends Task {
	private String url; //被抓取的URL,如果不指定就是抓取任务URL
	private int pageIndex; //页码
	private CapturedRecordList capturedRecordList; //列表抓取测试
	
	/**
	 * @return the capturedRecordList
	 */
	public CapturedRecordList getCapturedRecordList() {
		return capturedRecordList;
	}
	/**
	 * @param capturedRecordList the capturedRecordList to set
	 */
	public void setCapturedRecordList(CapturedRecordList capturedRecordList) {
		this.capturedRecordList = capturedRecordList;
	}
	/**
	 * @return the pageIndex
	 */
	public int getPageIndex() {
		return pageIndex;
	}
	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
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
}