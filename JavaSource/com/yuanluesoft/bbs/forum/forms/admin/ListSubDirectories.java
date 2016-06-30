package com.yuanluesoft.bbs.forum.forms.admin;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author yuanluesoft
 *
 */
public class ListSubDirectories extends ActionForm {
	private long parentId;
    private List directories;
	/**
	 * @return the directories
	 */
	public List getDirectories() {
		return directories;
	}
	/**
	 * @param directories the directories to set
	 */
	public void setDirectories(List directories) {
		this.directories = directories;
	}
	/**
	 * @return the parentId
	 */
	public long getParentId() {
		return parentId;
	}
	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
}