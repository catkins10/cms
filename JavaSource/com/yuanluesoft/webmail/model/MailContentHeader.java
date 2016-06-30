/*
 * Created on 2005-5-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.model;

/**
 * 
 * @author root
 * 
 */
public class MailContentHeader {
	private String contentType;
	private String type;
	private String charset;
	private String boundary;
	private String name;
	private String fileName;
	private String contentTransferEncoding;
	private String contentLocation;	
	private String contentId;
	private String contentDisposition;
	
	/**
	 * @return Returns the boundary.
	 */
	public String getBoundary() {
		return boundary;
	}
	/**
	 * @param boundary The boundary to set.
	 */
	public void setBoundary(String boundary) {
		this.boundary = boundary;
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
	 * @return Returns the contentDisposition.
	 */
	public String getContentDisposition() {
		return contentDisposition;
	}
	/**
	 * @param contentDisposition The contentDisposition to set.
	 */
	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
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
	 * @return Returns the contentLocation.
	 */
	public String getContentLocation() {
		return contentLocation;
	}
	/**
	 * @param contentLocation The contentLocation to set.
	 */
	public void setContentLocation(String contentLocation) {
		this.contentLocation = contentLocation;
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
	 * @return Returns the fileName.
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName The fileName to set.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
