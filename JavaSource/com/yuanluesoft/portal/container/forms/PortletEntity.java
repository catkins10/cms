package com.yuanluesoft.portal.container.forms;

import java.util.Set;

import com.yuanluesoft.jeaf.form.DynamicForm;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;

/**
 * 
 * @author linchuan
 *
 */
public class PortletEntity extends DynamicForm {
	private long orgId = -1; //组织机构ID
	private long siteId = -1; //站点ID
	private String category; //分类
	private String entityName; //实体名称
	private String description; //描述
	private String portletApplication; //PORTLET应用名称,非远程PORTLET时有效
	private String portletName; //PORTLET名称
	private Set visitors; //访问者列表
	private Set templates; //模板列表
	private Set defaultPreferences; //默认的个性化设置
	
	private RecordVisitorList entityVisitors = new RecordVisitorList(); //访问者列表
	
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}
	/**
	 * @param entityName the entityName to set
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
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
	 * @return the portletApplication
	 */
	public String getPortletApplication() {
		return portletApplication;
	}
	/**
	 * @param portletApplication the portletApplication to set
	 */
	public void setPortletApplication(String portletApplication) {
		this.portletApplication = portletApplication;
	}
	/**
	 * @return the portletName
	 */
	public String getPortletName() {
		return portletName;
	}
	/**
	 * @param portletName the portletName to set
	 */
	public void setPortletName(String portletName) {
		this.portletName = portletName;
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
	 * @return the templates
	 */
	public Set getTemplates() {
		return templates;
	}
	/**
	 * @param templates the templates to set
	 */
	public void setTemplates(Set templates) {
		this.templates = templates;
	}
	/**
	 * @return the visitors
	 */
	public Set getVisitors() {
		return visitors;
	}
	/**
	 * @param visitors the visitors to set
	 */
	public void setVisitors(Set visitors) {
		this.visitors = visitors;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the entityVisitors
	 */
	public RecordVisitorList getEntityVisitors() {
		return entityVisitors;
	}
	/**
	 * @param entityVisitors the entityVisitors to set
	 */
	public void setEntityVisitors(RecordVisitorList entityVisitors) {
		this.entityVisitors = entityVisitors;
	}
	/**
	 * @return the defaultPreferences
	 */
	public Set getDefaultPreferences() {
		return defaultPreferences;
	}
	/**
	 * @param defaultPreferences the defaultPreferences to set
	 */
	public void setDefaultPreferences(Set defaultPreferences) {
		this.defaultPreferences = defaultPreferences;
	}
}