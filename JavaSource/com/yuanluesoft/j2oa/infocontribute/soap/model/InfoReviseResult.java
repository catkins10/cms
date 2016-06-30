package com.yuanluesoft.j2oa.infocontribute.soap.model;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 退改稿修改结果
 * @author linchuan
 *
 */
public class InfoReviseResult implements Serializable {
	private String newBody; //修改后的正文
	private Calendar editTime; //修改时间
	private long editorId; //修改人ID
	private String editor; //修改人
	
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
	 * @return the editorId
	 */
	public long getEditorId() {
		return editorId;
	}
	/**
	 * @param editorId the editorId to set
	 */
	public void setEditorId(long editorId) {
		this.editorId = editorId;
	}
	/**
	 * @return the editTime
	 */
	public Calendar getEditTime() {
		return editTime;
	}
	/**
	 * @param editTime the editTime to set
	 */
	public void setEditTime(Calendar editTime) {
		this.editTime = editTime;
	}
	/**
	 * @return the newBody
	 */
	public String getNewBody() {
		return newBody;
	}
	/**
	 * @param newBody the newBody to set
	 */
	public void setNewBody(String newBody) {
		this.newBody = newBody;
	}
}