/*
 * Created on 2005-5-20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.pojo;

import com.yuanluesoft.jeaf.database.Record;


/**
 * 邮件附件(webmail_mail_attachment)
 * @author linchuan
 *
 */
public class MailAttachment extends Record {
	private long mailId; //邮件记录ID
	private String contentType; //附件类型
	private String name; //附件名称
	private long size; //附件大小
	private String contentTransferEncoding; //传输编码
	private String contentId; //内容ID
	private long beginIndex; //在邮件中的起始位置
	private long endIndex; //在邮件中的结束位置

	/**
	 * 以千字节为单位的附件大小
	 * @return
	 */
	public double getSizeKBytes() {
	    return size/1000.0;
	}

	/**
	 * @return Returns the contentId.
	 */
	public String getContentId() {
		return contentId;
	}
	/**
	 * @param contentId The contentId to set.
	 */
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	/**
	 * @return Returns the contentTransferEncoding.
	 */
	public String getContentTransferEncoding() {
		return contentTransferEncoding;
	}
	/**
	 * @param contentTransferEncoding The contentTransferEncoding to set.
	 */
	public void setContentTransferEncoding(String contentTransferEncoding) {
		this.contentTransferEncoding = contentTransferEncoding;
	}
	/**
	 * @return Returns the contentType.
	 */
	public String getContentType() {
		return contentType;
	}
	/**
	 * @param contentType The contentType to set.
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the size.
	 */
	public long getSize() {
		return size;
	}
	/**
	 * @param size The size to set.
	 */
	public void setSize(long size) {
		this.size = size;
	}
	/**
	 * @return Returns the mailId.
	 */
	public long getMailId() {
		return mailId;
	}
	/**
	 * @param mailId The mailId to set.
	 */
	public void setMailId(long mailId) {
		this.mailId = mailId;
	}
    /**
     * @return Returns the endIndex.
     */
    public long getEndIndex() {
        return endIndex;
    }
    /**
     * @param endIndex The endIndex to set.
     */
    public void setEndIndex(long endIndex) {
        this.endIndex = endIndex;
    }
    /**
     * @return Returns the beginIndex.
     */
    public long getBeginIndex() {
        return beginIndex;
    }
    /**
     * @param beginIndex The beginIndex to set.
     */
    public void setBeginIndex(long beginIndex) {
        this.beginIndex = beginIndex;
    }
}