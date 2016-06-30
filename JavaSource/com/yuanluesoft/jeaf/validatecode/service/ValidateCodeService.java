package com.yuanluesoft.jeaf.validatecode.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface ValidateCodeService {

	/**
	 * 生成校验码图片
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public void generateValidateCodeImage(HttpServletRequest request, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 发送校验短信
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public void sendValidateCodeSms(HttpServletRequest request, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 校验验证码
	 * @param code
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public boolean validateCode(String code, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 清除校验码,以避免“暴力注册”等事件,在页面正常提交以后,务必调用本方法
	 * @param request
	 * @param response
	 * @return
	 * @throws ServiceException
	 */
	public void cleanCode(HttpServletRequest request, HttpServletResponse response) throws ServiceException;
}