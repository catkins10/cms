/*
 * Created on 2005-8-17
 *
 */
package com.yuanluesoft.j2oa.dispatch.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 文件字配置(dispatch_config_docword)
 * @author linchuan
 *
 */
public class DispatchDocWordConfig extends Record {
	private String docWord; //文件字名称
	private String format; //文件字格式
	private long groupId; //联合编号组ID
	
	/**
	 * @return the docWord
	 */
	public String getDocWord() {
		return docWord;
	}
	/**
	 * @param docWord the docWord to set
	 */
	public void setDocWord(String docWord) {
		this.docWord = docWord;
	}
	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}
	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	/**
	 * @return the groupId
	 */
	public long getGroupId() {
		return groupId;
	}
	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
}