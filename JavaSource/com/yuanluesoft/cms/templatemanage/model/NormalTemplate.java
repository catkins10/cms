package com.yuanluesoft.cms.templatemanage.model;

import org.w3c.dom.html.HTMLDocument;

/**
 * 默认模板
 * @author linchuan
 *
 */
public class NormalTemplate {
	private HTMLDocument htmlDocument; //模板
	private String templatePath; //模板路径
	private int themeType; //主题类型
	
	public NormalTemplate(HTMLDocument htmlDocument, String templatePath, int themeType) {
		super();
		this.htmlDocument = htmlDocument;
		this.templatePath = templatePath;
		this.themeType = themeType;
	}
	/**
	 * @return the htmlDocument
	 */
	public HTMLDocument getHtmlDocument() {
		return htmlDocument;
	}
	/**
	 * @param htmlDocument the htmlDocument to set
	 */
	public void setHtmlDocument(HTMLDocument htmlDocument) {
		this.htmlDocument = htmlDocument;
	}
	/**
	 * @return the themeType
	 */
	public int getThemeType() {
		return themeType;
	}
	/**
	 * @param themeType the themeType to set
	 */
	public void setThemeType(int themeType) {
		this.themeType = themeType;
	}
	/**
	 * @return the templatePath
	 */
	public String getTemplatePath() {
		return templatePath;
	}
	/**
	 * @param templatePath the templatePath to set
	 */
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
}