package com.yuanluesoft.portal.container.pojo;

import com.yuanluesoft.cms.templatemanage.pojo.Template;

/**
 * PORTLET实体:模板(portlet_entity_template)
 * @author linchuan
 *
 */
public class PortletEntityTemplate extends Template {
	private long entityId; //PORTLET实体ID

	/**
	 * @return the entityId
	 */
	public long getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}
}