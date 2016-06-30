package com.yuanluesoft.cms.siteresource.forms;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;

/**
 * 
 * @author linchuan
 *
 */
public class ModifyReaders extends ActionForm {
	private long siteId; //站点ID
	private String mode; //修改方式,同步栏目编辑和管理员|synchColumnVisitor,添加用户|addUser,删除用户|deleteUser
	private boolean deleteNotColumnVisitor; //是否删除不是栏目编辑和管理员的人员,mode==synchColumnVisitor时有效
	private RecordVisitorList readers = new RecordVisitorList(); //需要添加或删除的用户
	private boolean selectedOnly; //只处理选中的记录
	
	/**
	 * @return the deleteNotColumnVisitor
	 */
	public boolean isDeleteNotColumnVisitor() {
		return deleteNotColumnVisitor;
	}
	/**
	 * @param deleteNotColumnVisitor the deleteNotColumnVisitor to set
	 */
	public void setDeleteNotColumnVisitor(boolean deleteNotColumnVisitor) {
		this.deleteNotColumnVisitor = deleteNotColumnVisitor;
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
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
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