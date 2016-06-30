package com.yuanluesoft.jeaf.form.service;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 表单安全服务
 * @author linchuan
 *
 */
public interface FormSecurityService {
	
	/**
	 * 生成请求编码,用来防止恶意提交,对匿名页面有效
	 * @param validateCodeImage
	 * @return
	 * @throws ServiceException
	 */
	public String registRequest(boolean validateCodeImageRequired) throws ServiceException;
	
	/**
	 * 设置是否必须校验验证码
	 * @param requestCode
	 * @param validateCodeImageRequired
	 * @throws ServiceException
	 */
	public void setValidateCodeImageRequired(String requestCode, boolean validateCodeImageRequired) throws ServiceException;
	
	/**
	 * 删除请求
	 * @param requestCode
	 * @throws ServiceException
	 */
	public void removeRequest(String requestCode) throws ServiceException;
	
	/**
	 * 校验请求编码
	 * @param requestCode
	 * @return
	 * @throws ServiceException
	 */
	public boolean validateRequest(String requestCode) throws ServiceException;
	
	/**
	 * 检查验证码图片是不是必须有的
	 * @param requestCode
	 * @return
	 * @throws ServiceException
	 */
	public boolean isValidateCodeImageRequired(String requestCode) throws ServiceException;
}