/*
 * Created on 2006-9-17
 *
 */
package com.yuanluesoft.j2oa.dispatch.service;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.dispatch.pojo.DispatchTemplateConfig;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 *
 * @author linchuan
 *
 */
public interface DispatchTemplateService {
	
	/**
	 * 根据模板ID获取模板
	 * @param templateId
	 * @return
	 * @throws ServiceException
	 */
	public Attachment getWordTemplateById(long templateId) throws ServiceException;
	
	/**
	 * 根据文件种类和文件字获取WORD模板
	 * @param docType
	 * @param docMark
	 * @return
	 * @throws ServiceException
	 */
	public Attachment getWordTemplate(String docType, String docMark) throws ServiceException;
	
	/**
	 * 保存WORD模板
	 * @param templateConfig
	 * @param request
	 * @throws ServiceException
	 */
	public void saveWordTemplate(DispatchTemplateConfig templateConfig, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 获取办理单模板
	 * @param docType
	 * @param docMark
	 * @return
	 * @throws ServiceException
	 */
	public String getHandlingTemplate(String docType, String docMark) throws ServiceException;
}