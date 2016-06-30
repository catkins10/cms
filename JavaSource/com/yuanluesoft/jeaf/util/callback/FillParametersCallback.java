package com.yuanluesoft.jeaf.util.callback;

import javax.servlet.http.HttpServletRequest;

/**
 * StringUtils.fillParameters回调
 * @author linchuan
 *
 */
public interface FillParametersCallback {
	
	/**
	 * 获取参数值
	 * @param parameterName
	 * @param bean
	 * @param request
	 * @return
	 */
	public Object getParameterValue(String parameterName, Object bean, HttpServletRequest request);
}