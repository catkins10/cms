package com.yuanluesoft.jeaf.payment.paymentmethod;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.payment.model.PaymentResult;
import com.yuanluesoft.jeaf.payment.pojo.Payment;
import com.yuanluesoft.jeaf.payment.pojo.PaymentMerchant;

/**
 * 支付方式服务
 * @author linchuan
 *
 */
public interface PaymentMethodService {
	
	/**
	 * 获取支付方式名称,如光大银行
	 * @return
	 */
	public String getName();

	/**
	 * 获取支付方式图标文件URL
	 * @return
	 */
	public String getIconUrl();
	
	/**
	 * 获取参数定义(PaymentMethodParameterDefine)列表
	 * @return
	 */
	public List getParameterDefines();
	
	/**
	 * 获取B2C帐户类型列表, 用逗号分隔, 如: 个人
	 * @return
	 */
	public String getB2CAccountTypes();
	
	/**
	 * 获取B2B帐户类型列表, 用逗号分隔, 如: 企业
	 * @return
	 */
	public String getB2BAccountTypes();
	
	/**
	 * 检查交易完成情况
	 * @param payment
	 * @param paymentMerchant
	 * @return
	 * @throws ServiceException
	 */
	public PaymentResult queryPayment(Payment payment, PaymentMerchant paymentMerchant) throws ServiceException;
	
	/**
	 * 适用银行回调URL检查交易完成情况
	 * @param payment
	 * @param paymentMerchant
	 * @param callbackQueryString 支付后银行返回的参数
	 * @return
	 * @throws ServiceException
	 */
	public PaymentResult queryPaymentByCallbackURL(Payment payment, PaymentMerchant paymentMerchant, String callbackQueryString) throws ServiceException;
	
	/**
	 * 重定向到银行页面
	 * @param payment
	 * @param paymentMerchant
	 * @param paymentCompleteCallbackURL
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public void redirectToBankPage(Payment payment, PaymentMerchant paymentMerchant, String paymentCompleteCallbackURL, HttpServletRequest request, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 解析交易记录(Transaction)列表
	 * @param transactionsFilePath
	 * @return
	 * @throws ServiceException
	 */
	public List parseTransactions(String transactionsFilePath) throws ServiceException;
	
	/**
	 * 生成批量转账单,返回完整的文件名
	 * @param transfers
	 * @param transferFilePath 存放目录
	 * @param subject 主题
	 * @param paymentMerchant
	 * @return
	 * @throws ServiceException
	 */
	public String createTransferFile(List transfers, String transferFilePath, String subject, PaymentMerchant paymentMerchant) throws ServiceException;
	
	/**
	 * 交易明细查询,返回Transaction列表
	 * @param day
	 * @param paymentMerchant
	 * @return
	 * @throws ServiceException
	 */
	public List listTransactions(Date day, PaymentMerchant paymentMerchant) throws ServiceException;
	
	/**
	 * 转账
	 * @param transfers
	 * @param paymentMerchant
	 * @throws ServiceException
	 */
	public void transfer(List transfers, PaymentMerchant paymentMerchant) throws ServiceException;
	
	/**
	 * 获取余额
	 * @param paymentMerchant
	 * @return
	 * @throws ServiceException
	 */
	public double getBalance(PaymentMerchant paymentMerchant) throws ServiceException;
}