/*
 * Created on 2006-9-14
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.archives.administrative.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 档案管理:文书档案HTML正文(archives_administrative_body)
 * @author linchuan
 *
 */
public class AdministrativeArchivesBody extends Record {
	private long archivesId; //文件ID
	private String htmlBody; //HTML正文
	
	/**
	 * @return Returns the archivesId.
	 */
	public long getArchivesId() {
		return archivesId;
	}
	/**
	 * @param archivesId The archivesId to set.
	 */
	public void setArchivesId(long archivesId) {
		this.archivesId = archivesId;
	}
	/**
	 * @return Returns the htmlBody.
	 */
	public java.lang.String getHtmlBody() {
		return htmlBody;
	}
	/**
	 * @param htmlBody The htmlBody to set.
	 */
	public void setHtmlBody(java.lang.String htmlBody) {
		this.htmlBody = htmlBody;
	}
}
