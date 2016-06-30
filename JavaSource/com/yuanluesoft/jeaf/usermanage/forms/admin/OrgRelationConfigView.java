package com.yuanluesoft.jeaf.usermanage.forms.admin;

import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 组织机构相关配置视图
 * @author linchuan
 *
 */
public class OrgRelationConfigView extends ViewForm {
	private String applicationName; //URL参数:应用名称
	private String viewApplicationName; //URL参数:视图所在应用名称,默认applicationName
	private String viewName; //URL参数:视图名称
	private long orgId; //URL参数:站点ID,如果没有指定,自动打开用户有管理权限的第一个站点
	private String orgTypes; //URL参数:组织机构类型
	private String popedomNames; //URL参数:权限类型,默认manager 
	
	private Tree orgTree; //站点目录树

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
	 * @return the viewName
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * @param viewName the viewName to set
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * @return the viewApplicationName
	 */
	public String getViewApplicationName() {
		return viewApplicationName;
	}

	/**
	 * @param viewApplicationName the viewApplicationName to set
	 */
	public void setViewApplicationName(String viewApplicationName) {
		this.viewApplicationName = viewApplicationName;
	}

	/**
	 * @return the orgId
	 */
	public long getOrgId() {
		return orgId;
	}

	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	/**
	 * @return the orgTree
	 */
	public Tree getOrgTree() {
		return orgTree;
	}

	/**
	 * @param orgTree the orgTree to set
	 */
	public void setOrgTree(Tree orgTree) {
		this.orgTree = orgTree;
	}

	/**
	 * @return the orgTypes
	 */
	public String getOrgTypes() {
		return orgTypes;
	}

	/**
	 * @param orgTypes the orgTypes to set
	 */
	public void setOrgTypes(String orgTypes) {
		this.orgTypes = orgTypes;
	}

	/**
	 * @return the popedomNames
	 */
	public String getPopedomNames() {
		return popedomNames;
	}

	/**
	 * @param popedomNames the popedomNames to set
	 */
	public void setPopedomNames(String popedomNames) {
		this.popedomNames = popedomNames;
	}
}