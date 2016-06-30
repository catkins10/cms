package com.yuanluesoft.portal.container.pojo;

import java.util.LinkedHashSet;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * PORTLET实体(portlet_entity)
 * @author linchuan
 *
 */
public class PortletEntity extends Record {
	private long orgId = -1; //组织机构ID
	private long siteId = -1; //站点ID
	private String category; //分类
	private String entityName; //实体名称
	private String description; //描述
	private String portletApplication; //PORTLET应用名称,非远程PORTLET时有效
	private String portletName; //PORTLET名称
	private Set visitors; //访问者列表
	private Set preferences; //默认的个性化设置
	private Set templates; //模板列表
	
	/**
	 * 添加个性化设置参数
	 * @param parameterName
	 * @param parameterValue
	 */
	public void addPreference(String preferenceName, String preferenceValue) {
		PortletEntityPreference preference = new PortletEntityPreference();
		preference.setId(UUIDLongGenerator.generateId()); //ID
		preference.setEntityId(getId()); //PORTLET实体ID
		preference.setName(preferenceName); //参数名称
		preference.setValue(preferenceValue); //参数值
		if(preferences==null) {
			preferences = new LinkedHashSet();
		}
		else {
			ListUtils.removeObjectByProperty(preferences, "name", preferenceName);
		}
		preferences.add(preference);
	}
	
	/**
	 * 获取参数值
	 * @param preferenceName
	 * @return
	 */
	public String getPreferenceValue(String preferenceName) {
		if(preferences==null) {
			return null;
		}
		PortletEntityPreference preference = (PortletEntityPreference)ListUtils.findObjectByProperty(preferences, "name", preferenceName);
		return preference==null ? null : preference.getValue();
	}
	
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
	 * @return the preferences
	 */
	public Set getPreferences() {
		return preferences;
	}

	/**
	 * @param preferences the preferences to set
	 */
	public void setPreferences(Set preferences) {
		this.preferences = preferences;
	}
}