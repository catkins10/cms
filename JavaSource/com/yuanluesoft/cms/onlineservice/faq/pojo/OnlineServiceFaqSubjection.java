package com.yuanluesoft.cms.onlineservice.faq.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 96123:常见问题解答隶属目录(onlineservice_faq_subjection)
 * @author linchuan
 *
 */
public class OnlineServiceFaqSubjection extends Record {
	private long faqId; //FAQID
	private long directoryId; //隶属目录ID
	
	/**
	 * @return the directoryId
	 */
	public long getDirectoryId() {
		return directoryId;
	}
	/**
	 * @param directoryId the directoryId to set
	 */
	public void setDirectoryId(long directoryId) {
		this.directoryId = directoryId;
	}
	/**
	 * @return the faqId
	 */
	public long getFaqId() {
		return faqId;
	}
	/**
	 * @param faqId the faqId to set
	 */
	public void setFaqId(long faqId) {
		this.faqId = faqId;
	}
}