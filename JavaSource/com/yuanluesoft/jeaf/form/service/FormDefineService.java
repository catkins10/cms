/*
 * Created on 2005-9-19
 *
 */
package com.yuanluesoft.jeaf.form.service;

import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.model.Form;

/**
 * 
 * @author linchuan
 *
 */
public interface FormDefineService {
	
	/**
	 * 获取表单定义模型
	 * @param formClass
	 * @return
	 * @throws ServiceException
	 */
	public Form loadFormDefine(Class formClass) throws ServiceException;
	
	/**
	 * 获取表单定义模型
	 * @param formClassName
	 * @return
	 * @throws ServiceException
	 */
	public Form loadFormDefine(String formClassName) throws ServiceException;
	
	/**
	 * 按应用程序和表单名称获取表单定义
	 * @param applicationName
	 * @param formName
	 * @return
	 * @throws ServiceException
	 */
	public Form loadFormDefine(String applicationName, String formName) throws ServiceException;
	
	/**
	 * 获取应用程序的表单列表
	 * @param applicationName
	 * @return
	 * @throws ServiceException
	 */
	public List listFormDefines(String applicationName) throws ServiceException;
	
	/**
	 * 保存表单定义
	 * @param applicationName
	 * @param forms
	 * @throws ServiceException
	 */
	public void saveFormDefine(String applicationName, List forms) throws ServiceException;
}