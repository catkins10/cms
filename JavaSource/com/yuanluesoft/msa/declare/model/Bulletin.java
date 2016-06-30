package com.yuanluesoft.msa.declare.model;

import java.sql.Timestamp;

/**
 * 海事局业务申报公告
 * @author lmiky
 *
 */
public class Bulletin {
	private String sourceUrl; //源记录URL
	private String subject; //标题
	private String editor; //发布者
	private String editorUnit; //发布机构
	private Timestamp created; //发布时间
	private String body; //正文
	
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the editor
	 */
	public String getEditor() {
		return editor;
	}
	/**
	 * @param editor the editor to set
	 */
	public void setEditor(String editor) {
		this.editor = editor;
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	/**
	 * @return the editorUnit
	 */
	public String getEditorUnit() {
		return editorUnit;
	}
	/**
	 * @param editorUnit the editorUnit to set
	 */
	public void setEditorUnit(String editorUnit) {
		this.editorUnit = editorUnit;
	}
	/**
	 * @return the sourceUrl
	 */
	public String getSourceUrl() {
		return sourceUrl;
	}
	/**
	 * @param sourceUrl the sourceUrl to set
	 */
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
}