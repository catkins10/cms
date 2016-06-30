package com.yuanluesoft.jeaf.tools.upgradetemplate.upgrader;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.templatemanage.pojo.Template;

/**
 * 
 * @author chuan
 *
 */
public interface TemplateUpgrader {

	/**
	 * 获取升级器创建时间,用来和最后更新时间做比对,如果早于最后更新时间,则不需要做升级
	 * @return
	 */
	public String getCreateDate();
	
	/**
	 * 升级模板,返回新的模板HTML
	 * @param template
	 * @param templateHTML
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String upgrade(Template template, String templateHTML, HttpServletRequest request) throws Exception;
}