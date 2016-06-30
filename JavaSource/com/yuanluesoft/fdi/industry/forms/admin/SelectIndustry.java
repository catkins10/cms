package com.yuanluesoft.fdi.industry.forms.admin;

import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;

/**
 * 
 * @author linchuan
 *
 */
public class SelectIndustry extends TreeDialog {
	private boolean editabled; //输出有编辑权限的分类
	private boolean readabled; //输出有查询权限的分类
	
	/**
	 * @return the editabled
	 */
	public boolean isEditabled() {
		return editabled;
	}
	/**
	 * @param editabled the editabled to set
	 */
	public void setEditabled(boolean editabled) {
		this.editabled = editabled;
	}
	/**
	 * @return the readabled
	 */
	public boolean isReadabled() {
		return readabled;
	}
	/**
	 * @param readabled the readabled to set
	 */
	public void setReadabled(boolean readabled) {
		this.readabled = readabled;
	}
}