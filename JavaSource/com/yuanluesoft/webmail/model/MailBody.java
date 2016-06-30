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
public class MailBody {
	private String contentType;
	private String charset;
	private String contentTransferEncoding;
	private String body;
	
    /**
     * @return Returns the body.
     */
    public String getBody() {
        return body;
    }
    /**
     * @param body The body to set.
     */
    public void setBody(String body) {
        this.body = body;
    }
    /**
     * @return Returns the charset.
     */
    public String getCharset() {
        return charset;
    }
    /**
     * @param charset The charset to set.
     */
    public void setCharset(String charset) {
        this.charset = charset;
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
}
