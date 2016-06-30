/*
 * Created on 2005-8-17
 *
 */
package com.yuanluesoft.j2oa.dispatch.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * WORD模板配置(dispatch_config_template)
 * @author linchuan
 *
 */
public class DispatchTemplateConfig extends Record {
	private String docTypes; //适用的文件种类
	private String docWords; //适用的文件字
	private String handlingTemplate; //办理单模板
	
	/**
	 * @return the docTypes
	 */
	public String getDocTypes() {
		return docTypes;
	}
	/**
	 * @param docTypes the docTypes to set
	 */
	public void setDocTypes(String docTypes) {
		this.docTypes = docTypes;
	}
	/**
	 * @return the docWords
	 */
	public String getDocWords() {
		return docWords;
	}
	/**
	 * @param docWords the docWords to set
	 */
	public void setDocWords(String docWords) {
		this.docWords = docWords;
	}
	/**
	 * @return the handlingTemplate
	 */
	public String getHandlingTemplate() {
		return handlingTemplate;
	}
	/**
	 * @param handlingTemplate the handlingTemplate to set
	 */
	public void setHandlingTemplate(String handlingTemplate) {
		this.handlingTemplate = handlingTemplate;
	}
}