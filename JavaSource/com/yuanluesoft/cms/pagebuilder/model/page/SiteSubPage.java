package com.yuanluesoft.cms.pagebuilder.model.page;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 * 站点应用子页面,包括3种类型:
 * 1、type="jsp"(默认),引用jsp页面,name="base"时,引用<ext:applicationForm>标记内部jsp
 * 2、type="iframe",IFRAME方式,显示iframeUrl指定的页面
 * 3、type="template",系统预置页面,路径为:[webAppPath]/[applicationName]/template/subpage/[name]/template.html
 * @author linchuan
 *
 */
public class SiteSubPage extends CloneableObject {
	private String name; //名称
	private String title; //标题
	private String type; //类型
	private String iframeUrl; //iframe指向的链接
	private String normalCssFile; //默认引用的CSS,如果没有指定,则默认为/cms/css/application.css
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the iframeUrl
	 */
	public String getIframeUrl() {
		return iframeUrl;
	}
	/**
	 * @param iframeUrl the iframeUrl to set
	 */
	public void setIframeUrl(String iframeUrl) {
		this.iframeUrl = iframeUrl;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the normalCssFile
	 */
	public String getNormalCssFile() {
		return normalCssFile;
	}
	/**
	 * @param normalCssFile the normalCssFile to set
	 */
	public void setNormalCssFile(String normalCssFile) {
		this.normalCssFile = normalCssFile;
	}
}