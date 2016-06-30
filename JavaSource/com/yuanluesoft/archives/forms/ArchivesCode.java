/*
 * Created on 2006-9-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.archives.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 *
 * @author linchuan
 *
 */
public class ArchivesCode extends ActionForm {
	private String archivesType; //档案类别,文书/科技
	private String codeConfig; //档号规则
	
	/**
	 * @return Returns the codeConfig.
	 */
	public String getCodeConfig() {
		return codeConfig;
	}
	/**
	 * @param codeConfig The codeConfig to set.
	 */
	public void setCodeConfig(String codeConfig) {
		this.codeConfig = codeConfig;
	}
	/**
	 * @return Returns the archivesType.
	 */
	public String getArchivesType() {
		return archivesType;
	}
	/**
	 * @param archivesType The archivesType to set.
	 */
	public void setArchivesType(String archivesType) {
		this.archivesType = archivesType;
	}
}