package com.yuanluesoft.portal.container.forms;

import com.yuanluesoft.cms.templatemanage.forms.Template;

/**
 * 
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