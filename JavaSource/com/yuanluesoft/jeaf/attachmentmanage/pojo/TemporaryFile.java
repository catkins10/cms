package com.yuanluesoft.jeaf.attachmentmanage.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 临时文件(attachment_temporary)
 * @author linchuan
 *
 */
public class TemporaryFile extends Record {
	private String filePath; //临时文件路径
	private Timestamp expires; //过期时间
	
	/**
	 * @return the expires
	 */
	public Timestamp getExpires() {
		return expires;
	}
	/**
	 * @param expires the expires to set
	 */
	public void setExpires(Timestamp expires) {
		this.expires = expires;
	}
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}