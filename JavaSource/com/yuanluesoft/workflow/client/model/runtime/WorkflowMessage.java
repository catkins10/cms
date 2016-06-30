/*
 * Created on 2006-4-15
 *
 */
package com.yuanluesoft.workflow.client.model.runtime;

/**
 * 
 * @author linchuan
 *
 */
public class WorkflowMessage {
	private String type; //类型, 如：发文、出差
	private String content; //内容
	private String href; //链接
	
	public WorkflowMessage() {
		super();
	}
	
	public WorkflowMessage(String type, String content, String href) {
		super();
		this.type = type;
		this.content = content;
		this.href = href;
	}


	/**
	 * @return Returns the href.
	 */
	public String getHref() {
		return href;
	}
	/**
	 * @param href The href to set.
	 */
	public void setHref(String href) {
		this.href = href;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
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