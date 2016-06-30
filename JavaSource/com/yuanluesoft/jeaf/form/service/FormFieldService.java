package com.yuanluesoft.jeaf.form.service;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.model.RequestInfo;

/**
 * 表单字段服务
 * @author chuan
 *
 */
public interface FormFieldService {

	/**
	 * 输出表单字段
	 * @param fieldElement
	 * @param formField
	 * @param fieldValue
	 * @param bean
	 * @param recordId
	 * @param componentRecordId
	 * @param applicationName
	 * @param requestInfo
	 * @param request
	 * @throws ServiceException
	 */
	public void writeFormField(HTMLElement fieldElement, Field formField, Object fieldValue, Object bean, long recordId, long componentRecordId, String applicationName, RequestInfo requestInfo, HttpServletRequest request) throws ServiceException;
}