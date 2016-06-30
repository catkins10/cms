package com.yuanluesoft.jeaf.tools.filetransfertest.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class FileTransferTest extends ActionForm {
	private String anonymousDownloadURL;
	private String dynamicDownloadURL;
	
	/**
	 * @return the anonymousDownloadURL
	 */
	public String getAnonymousDownloadURL() {
		return anonymousDownloadURL;
	}
	/**
	 * @param anonymousDownloadURL the anonymousDownloadURL to set
	 */
	public void setAnonymousDownloadURL(String anonymousDownloadURL) {
		this.anonymousDownloadURL = anonymousDownloadURL;
	}
	/**
	 * @return the dynamicDownloadURL
	 */
	public String getDynamicDownloadURL() {
		return dynamicDownloadURL;
	}
	/**
	 * @param dynamicDownloadURL the dynamicDownloadURL to set
	 */
	public void setDynamicDownloadURL(String dynamicDownloadURL) {
		this.dynamicDownloadURL = dynamicDownloadURL;
	}
}