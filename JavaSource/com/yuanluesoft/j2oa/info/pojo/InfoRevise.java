package com.yuanluesoft.j2oa.info.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 信息采编:退改稿(info_revise)
 * @author linchuan
 *
 */
public class InfoRevise extends Record {
	private String subject; //稿件标题
	private long infoId; //稿件ID,原始稿件ID或者录用稿件ID
	private int isReceive; //是否来稿
	private long revisePersonId; //退改稿人ID
	private String revisePerson; //退改稿人
	private String revisePersonTel; //退改稿人电话
	private String reviseOpinion; //退改稿意见
	private Timestamp reviseTime; //退改稿时间
	private String newBody; //修改后的正文
	private Timestamp editTime; //修改时间
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
	public Timestamp getEditTime() {
		return editTime;
	}
	/**
	 * @param editTime the editTime to set
	 */
	public void setEditTime(Timestamp editTime) {
		this.editTime = editTime;
	}
	/**
	 * @return the infoId
	 */
	public long getInfoId() {
		return infoId;
	}
	/**
	 * @param infoId the infoId to set
	 */
	public void setInfoId(long infoId) {
		this.infoId = infoId;
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
	/**
	 * @return the reviseOpinion
	 */
	public String getReviseOpinion() {
		return reviseOpinion;
	}
	/**
	 * @param reviseOpinion the reviseOpinion to set
	 */
	public void setReviseOpinion(String reviseOpinion) {
		this.reviseOpinion = reviseOpinion;
	}
	/**
	 * @return the revisePerson
	 */
	public String getRevisePerson() {
		return revisePerson;
	}
	/**
	 * @param revisePerson the revisePerson to set
	 */
	public void setRevisePerson(String revisePerson) {
		this.revisePerson = revisePerson;
	}
	/**
	 * @return the revisePersonId
	 */
	public long getRevisePersonId() {
		return revisePersonId;
	}
	/**
	 * @param revisePersonId the revisePersonId to set
	 */
	public void setRevisePersonId(long revisePersonId) {
		this.revisePersonId = revisePersonId;
	}
	/**
	 * @return the revisePersonTel
	 */
	public String getRevisePersonTel() {
		return revisePersonTel;
	}
	/**
	 * @param revisePersonTel the revisePersonTel to set
	 */
	public void setRevisePersonTel(String revisePersonTel) {
		this.revisePersonTel = revisePersonTel;
	}
	/**
	 * @return the reviseTime
	 */
	public Timestamp getReviseTime() {
		return reviseTime;
	}
	/**
	 * @param reviseTime the reviseTime to set
	 */
	public void setReviseTime(Timestamp reviseTime) {
		this.reviseTime = reviseTime;
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
	 * @return the isReceive
	 */
	public int getIsReceive() {
		return isReceive;
	}
	/**
	 * @param isReceive the isReceive to set
	 */
	public void setIsReceive(int isReceive) {
		this.isReceive = isReceive;
	}
}