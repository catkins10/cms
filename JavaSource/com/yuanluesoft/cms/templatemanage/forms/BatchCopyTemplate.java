package com.yuanluesoft.cms.templatemanage.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class BatchCopyTemplate extends ActionForm {
	private long sourceTemplateId; //源模板ID
	private String targetPageNames; //目标页面名称列表
	
	/**
	 * @return the sourceTemplateId
	 */
	public long getSourceTemplateId() {
		return sourceTemplateId;
	}
	/**
	 * @param sourceTemplateId the sourceTemplateId to set
	 */
	public void setSourceTemplateId(long sourceTemplateId) {
		this.sourceTemplateId = sourceTemplateId;
	}
	/**
	 * @return the targetPageNames
	 */
	public String getTargetPageNames() {
		return targetPageNames;
	}
	/**
	 * @param targetPageNames the targetPageNames to set
	 */
	public void setTargetPageNames(String targetPageNames) {
		this.targetPageNames = targetPageNames;
	}
}