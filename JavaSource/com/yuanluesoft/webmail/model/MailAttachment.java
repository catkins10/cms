/*
 * Created on 2006-5-11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.model;

/**
 *
 * @author linchuan
 *
 */
public class MailAttachment {
	private String contentType;
	private String name;
	private long size;
	private String contentTransferEncoding;
	private String contentId;
	private long beginIndex; //在邮件文件中的开始位置,为0时表示文件是单独的附件文件
	private long endIndex; //在邮件文件中的结束位置
	
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
}
