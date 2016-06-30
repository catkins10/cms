package com.yuanluesoft.j2oa.exchange.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 公文:撤销记录(exchange_document_undo)
 * @author linchuan
 *
 */
public class ExchangeDocumentUndo extends Record {
	private long documentId; //公文ID
	private String undoReason; //撤销发布的原因
	private char resign = '0'; //是否重新签收
	private Timestamp undoTime; //撤销时间
	private String undoPerson; //撤销人
	private long undoPersonId; //撤销人ID
	/**
	 * @return the documentId
	 */
	public long getDocumentId() {
		return documentId;
	}
	/**
	 * @param documentId the documentId to set
	 */
	public void setDocumentId(long documentId) {
		this.documentId = documentId;
	}
	/**
	 * @return the resign
	 */
	public char getResign() {
		return resign;
	}
	/**
	 * @param resign the resign to set
	 */
	public void setResign(char resign) {
		this.resign = resign;
	}
	/**
	 * @return the undoPerson
	 */
	public String getUndoPerson() {
		return undoPerson;
	}
	/**
	 * @param undoPerson the undoPerson to set
	 */
	public void setUndoPerson(String undoPerson) {
		this.undoPerson = undoPerson;
	}
	/**
	 * @return the undoPersonId
	 */
	public long getUndoPersonId() {
		return undoPersonId;
	}
	/**
	 * @param undoPersonId the undoPersonId to set
	 */
	public void setUndoPersonId(long undoPersonId) {
		this.undoPersonId = undoPersonId;
	}
	/**
	 * @return the undoReason
	 */
	public String getUndoReason() {
		return undoReason;
	}
	/**
	 * @param undoReason the undoReason to set
	 */
	public void setUndoReason(String undoReason) {
		this.undoReason = undoReason;
	}
	/**
	 * @return the undoTime
	 */
	public Timestamp getUndoTime() {
		return undoTime;
	}
	/**
	 * @param undoTime the undoTime to set
	 */
	public void setUndoTime(Timestamp undoTime) {
		this.undoTime = undoTime;
	}
}
