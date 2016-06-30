package com.yuanluesoft.dpc.keyproject.forms;

import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectOfficialDocuments;

/**
 * 
 * @author linchuan
 *
 */
public class OfficialDocument extends Project {
	private KeyProjectOfficialDocuments officialDocument = new KeyProjectOfficialDocuments();

	/**
	 * @return the officialDocument
	 */
	public KeyProjectOfficialDocuments getOfficialDocument() {
		return officialDocument;
	}

	/**
	 * @param officialDocument the officialDocument to set
	 */
	public void setOfficialDocument(KeyProjectOfficialDocuments officialDocument) {
		this.officialDocument = officialDocument;
	}
}