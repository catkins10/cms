package com.yuanluesoft.cms.infopublic.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;

/**
 * 
 * @author linchuan
 *
 */
public class ModifyReaders extends ActionForm {
	private long directoryId; //目录ID
	private String mode; //修改方式,同步栏目编辑和管理员|synchDirectoryVisitor,添加用户|addUser,删除用户|deleteUser
	private boolean deleteNotDirectoryVisitor; //是否删除不是栏目编辑和管理员的人员,mode==synchColumnVisitor时有效
	private RecordVisitorList readers = new RecordVisitorList(); //需要添加或删除的用户
	private boolean selectedOnly; //只处理选中的记录
	
	/**
	 * @return the deleteNotDirectoryVisitor
	 */
	public boolean isDeleteNotDirectoryVisitor() {
		return deleteNotDirectoryVisitor;
	}
	/**
	 * @param deleteNotDirectoryVisitor the deleteNotDirectoryVisitor to set
	 */
	public void setDeleteNotDirectoryVisitor(boolean deleteNotDirectoryVisitor) {
		this.deleteNotDirectoryVisitor = deleteNotDirectoryVisitor;
	}
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