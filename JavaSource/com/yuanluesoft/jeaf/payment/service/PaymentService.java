package com.yuanluesoft.jeaf.payment.service;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.payment.pojo.Payment;
import com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant;
import com.yuanluesoft.jeaf.sso.matcher.Matcher;

/**
 * 支付服务
 * @author linchuan
 *
 */
public interface PaymentService extends BusinessService {
	
	/**
	 * 获取商户参数列表
	 * @param paymentMerchant
	 * @return
	 * @throws ServiceException
	 */
	public Set listPaymentMerchantParameters(String paymentMethodName, PaymentMerchant paymentMerchant) throws ServiceException;
	
	/**
	 * 保存或更新商户参数
	 * @param paymentMerchant
	 * @param parameterValues
	 * @throws ServiceException
	 */
	public void savePaymentMerchantParameters(PaymentMerchant paymentMerchant, String[] parameterValues) throws ServiceException;
	
	/**
	 * 重定向到支付页面
	 * @param applicationName
	 * @param applicationOrderId
	 * @param merchantIds
	 * @param b2cOnly
	 * @param b2bOnly
	 * @param payerId
	 * @param payerName
	 * @param paymentReason
	 * @param money
	 * @param providerId
	 * @param providerName
	 * @param providerMoney
	 * @param redirectOfSuccess
	 * @param redirectOfFailed
	 * @param siteId
	 * @param response
	 * @throws ServiceException
	 */
	public void redirectToPayment(String applicationName, String applicationOrderId, String merchantIds, boolean b2cOnly, boolean b2bOnly, String payerId, String payerName, String paymentReason, double money, String providerId, String providerName, double providerMoney, String redirectOfSuccess, String redirectOfFailed, long siteId, HttpServletResponse response) throws ServiceException;
	//public void redirectToPayment(String applicationName, String applicationOrderId, String merchantIds, boolean b2cOnly, boolean b2bOnly, String payerId, String payerName, String paymentReason, double money, String providerId, String providerName, double providerMoney, String redirectOfSuccess, String redirectOfFailed, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 创建支付
	 * @param applicationName 应用名称
	 * @param applicationOrderId 应用指定的订单ID
	 * @param payerId 支付人ID
	 * @param payerName 支付人姓名
	 * @param paymentReason 支付原因
	 * @param money 支付金额
	 * @param providerId 服务提供者ID,0表示系统
	 * @param providerName 服务提供者姓名
	 * @param providerMoney 服务提供者应得的金额
	 * @param pageMode 页面打开方式, dialog/对话框,popup/弹出页面,redirect/重定向
	 * @param paymentFrom 打开支付页面的URL
	 * @param redirectOfSuccess 支付完成跳转的URL,重定向方式时有效
	 * @param redirectOfFailed 支付失败跳转的URL,重定向方式时有效
	 *  @param payerIp 支付人IP
	 * @return 0/创建成功, -1/失败, 1/已经支付过
	 * @throws ServiceException
	 */
	public Payment createPayment(String applicationName, String applicationOrderId, String payerId, String payerName, String paymentReason, double money, String providerId, String providerName, double providerMoney, String pageMode, String paymentFrom, String redirectOfSuccess, String redirectOfFailed, String payerIp) throws ServiceException;
	
	/**
	 * 完成支付
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public Payment completePayment(HttpServletRequest request) throws ServiceException;
	
	/**
	 * 按应用名称和订单ID获取支付记录,自动检查订单的完成情况
	 * @param applicationName
	 * @param applicationOrderId
	 * @param payerInfoRequired 是否必须有支付人信息
	 * @return
	 * @throws ServiceException
	 */
	public Payment loadPayment(String applicationName, String applicationOrderId, boolean payerInfoRequired) throws ServiceException;
	//public Payment loadPayment(String applicationName, String applicationOrderId) throws ServiceException;
		
	/**
	 * 定时调用:清理超时支付记录
	 * @throws ServiceException
	 */
	public void clearTimeoutPayment() throws ServiceException;
	
	/**
	 * 获取商户列表
	 * @param merchantIds
	 * @param b2cOnly
	 * @param b2bOnly
	 * @return
	 * @throws ServiceException
	 */
	public List listPaymentMerchants(String merchantIds, boolean b2cOnly, boolean b2bOnly) throws ServiceException;
	
	/**
	 * 按帐号获取商户
	 * @param account
	 * @return
	 * @throws ServiceException
	 */
	public PaymentMerchant getPaymentMerchantByAccount(String account) throws ServiceException;
	
	/**
	 * 解析交易记录文件
	 * @param paymentMethodName
	 * @param filePath
	 * @return
	 * @throws ServiceException
	 */
	public List parseTransactionsFile(String paymentMethodName, String filePath) throws ServiceException;
	
	/**
	 * 查询交易记录
	 * @param paymentMerchantId
	 * @param day
	 * @return
	 * @throws ServiceException
	 */
	public List listTransactions(long paymentMerchantId, Date day) throws ServiceException;
	
	/**
	 * 批量转账
	 * @param paymentMerchantId
	 * @param transfers
	 * @param transferPassword 转账密码
	 * @param passwordMatcher 密码匹配器
	 * @throws ServiceException
	 */
	public void transfer(long paymentMerchantId, List transfers, String transferPassword, Matcher passwordMatcher) throws ServiceException;
	
	/**
	 * 获取余额
	 * @param paymentMerchantId
	 * @return
	 * @throws ServiceException
	 */
	public double getBalance(long paymentMerchantId) throws ServiceException;
	
	/**
	 * 生成批量转账单,返回完整的文件名
	 * @param paymentMerchantId
	 * @param transfers
	 * @param transferFilePath 存放目录
	 * @param subject 主题
	 * @return
	 * @throws ServiceException
	 */
	public String createTransferFile(long paymentMerchantId, List transfers, String transferFilePath, String subject) throws ServiceException;
	
	/**
	 * 获取帐户类型列表
	 * @param paymentMerchantId
	 * @param b2cOnly
	 * @param b2bOnly
	 * @return
	 * @throws ServiceException
	 */
	public List listAccountTypes(long paymentMerchantId, boolean b2cOnly, boolean b2bOnly) throws ServiceException;
	
	/**
	 * 重定向到银行页面
	 * @param paymentId 订单ID
	 * @param merchantId 商户ID
	 * @param accountType 账户类型
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public void redirectToBankPage(long paymentId, long merchantId, String accountType, HttpServletRequest request, HttpServletResponse response) throws ServiceException;
}