package com.yuanluesoft.cms.publicservice.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;

/**
 * 
 * @author linchuan
 *
 */
public class ModifyReaders extends ActionForm {
	private String applicationName; //应用名称
	private String mode; //修改方式,同步站点编辑和管理员|synchSiteVisitor,添加用户|addUser,删除用户|deleteUser
	private RecordVisitorList readers = new RecordVisitorList(); //需要添加或删除的用户
	private boolean selectedOnly; //只处理选中的记录
	
	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}
	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	/**
	 * @return the readers
	 */
	public RecordVisitorList getReaders() {
		return readers;
	}
	/**
	 * @param readers the readers to set
	 */
	public void setReaders(RecordVisitorList readers) {
		this.readers = readers;
	}
	/**
	 * @return the selectedOnly
	 */
	public boolean isSelectedOnly() {
		return selectedOnly;
	}
	/**
	 * @param selectedOnly the selectedOnly to set
	 */
	public void setSelectedOnly(boolean selectedOnly) {
		this.selectedOnly = selectedOnly;
	}
}