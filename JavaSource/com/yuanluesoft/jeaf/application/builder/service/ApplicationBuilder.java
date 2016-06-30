package com.yuanluesoft.jeaf.application.builder.service;

import java.util.List;

import com.yuanluesoft.jeaf.application.builder.model.formtemplate.FormTemplate;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 应用生成器
 * @author linchuan
 *
 */
public interface ApplicationBuilder {
	
	/**
	 * 获取表单模板
	 * @param templateName
	 * @return
	 */
	public FormTemplate getFormTemplate(String templateName);
	
	/**
	 * 获取模板列表
	 * @return
	 */
	public List listFormTemplates();
	
	/**
	 * 生成应用
	 * @param applicationId
	 * @throws ServiceException
	 */
	public void build(long applicationId) throws ServiceException;
	
	/**
	 * 重新部署应用
	 * @throws ServiceException
	 */
	public void redeployment() throws ServiceException;
}