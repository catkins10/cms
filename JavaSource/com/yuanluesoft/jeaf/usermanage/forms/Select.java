package com.yuanluesoft.jeaf.usermanage.forms;

import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;
import com.yuanluesoft.jeaf.usermanage.pojo.OrgRoot;

/**
 * 
 * @author linchuan
 *
 */
public class Select extends TreeDialog {
	private boolean hideRoot; //URL参数:是否隐藏根组织树
	private boolean postOnly; //URL参数:是否仅岗位,选择角色时有效
	private boolean managedOnly; //URL参数:是否仅输出有管理权限的
	private OrgRoot root; //根组织,如果为空,则不显示
	
	/**
	 * @return the hideRoot
	 */
	public boolean isHideRoot() {
		return hideRoot;
	}
	/**
	 * @param hideRoot the hideRoot to set
	 */
	public void setHideRoot(boolean hideRoot) {
		this.hideRoot = hideRoot;
	}
	/**
	 * @return the root
	 */
	public OrgRoot getRoot() {
		return root;
	}
	/**
	 * @param root the root to set
	 */
	public void setRoot(OrgRoot root) {
		this.root = root;
	}
	/**
	 * @return the postOnly
	 */
	public boolean isPostOnly() {
		return postOnly;
	}
	/**
	 * @param postOnly the postOnly to set
	 */
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}
	/**
	 * @return the managedOnly
	 */
	public boolean isManagedOnly() {
		return managedOnly;
	}
	/**
	 * @param managedOnly the managedOnly to set
	 */
	public void setManagedOnly(boolean managedOnly) {
		this.managedOnly = managedOnly;
	}
}