package com.yuanluesoft.enterprise.iso.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * ISO:文档所属目录(iso_document_subjection)
 * @author linchuan
 *
 */
public class IsoDocumentSubjection extends Record {
	private long documentId; //文档ID
	private long directoryId; //目录ID
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
}
