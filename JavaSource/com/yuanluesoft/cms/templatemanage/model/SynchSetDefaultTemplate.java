package com.yuanluesoft.cms.templatemanage.model;

import java.io.Serializable;

/**
 * 同步设置默认模板
 * @author linchuan
 *
 */
public class SynchSetDefaultTemplate implements Serializable {
	private long templateId; //模板ID

	public SynchSetDefaultTemplate(long templateId) {
		super();
		this.templateId = templateId;
	}

	/**
	 * @return the templateId
	 */
	public long getTemplateId() {
		return templateId;
	}

	/**
	 * @param templateId the templateId to set
	 */
	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}
}