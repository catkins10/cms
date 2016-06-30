/*
 * Created on 2005-8-29
 *
 */
package com.yuanluesoft.j2oa.dispatch.forms;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 *
 * @author linchuan
 *
 */
public class TemplateConfig extends ActionForm {
	private String docTypes; //适用的文件种类
	private String docWords; //适用的文件字
	private String handlingTemplate; //办理单模板
	
	private List fields; //字段列表
	
	private String allDocTypes; //所有的文件种类列表
	private String allDocWords; //所有的文件字列表
	
	/**
	 * @return the allDocTypes
	 */
	public String getAllDocTypes() {
		return allDocTypes;
	}
	/**
	 * @param allDocTypes the allDocTypes to set
	 */
	public void setAllDocTypes(String allDocTypes) {
		this.allDocTypes = allDocTypes;
	}
	/**
	 * @return the allDocWords
	 */
	public String getAllDocWords() {
		return allDocWords;
	}
	/**
	 * @param allDocWords the allDocWords to set
	 */
	public void setAllDocWords(String allDocWords) {
		this.allDocWords = allDocWords;
	}
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
	 * @return the fields
	 */
	public List getFields() {
		return fields;
	}
	/**
	 * @param fields the fields to set
	 */
	public void setFields(List fields) {
		this.fields = fields;
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
