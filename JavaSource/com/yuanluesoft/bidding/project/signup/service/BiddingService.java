package com.yuanluesoft.bidding.project.signup.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.bidding.enterprise.pojo.BiddingBidEnterprise;
import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.signup.model.SignUpTotal;
import com.yuanluesoft.bidding.project.signup.pojo.BiddingSignUp;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sso.matcher.Matcher;

/**
 * 投标服务
 * @author linchuan
 *
 */
public interface BiddingService extends BusinessService {

	/**
	 * 完成报名
	 * @param projectId
	 * @param request
	 * @param response
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public BiddingSignUp completeSignUp(long projectId, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 按报名号获取报名记录
	 * @param signUpNo
	 * @param checkPaid
	 * @return
	 * @throws ServiceException
	 */
	public BiddingSignUp loadSignUp(String signUpNo, boolean checkPaid) throws ServiceException;
	
	/**
	 * 按企业获取报名记录
	 * @param enterpriseId
	 * @param projectId
	 * @param checkPaid
	 * @return
	 * @throws ServiceException
	 */
	public BiddingSignUp loadSignUpByEnterprise(long enterpriseId, long projectId, boolean checkPaid) throws ServiceException;
	
	/**
	 * 领取纸质标书、图纸
	 * @param signUpNo
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public void receivePaperDocuments(String signUpNo, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 重定向到支付页面
	 * @param signUpNo
	 * @param paymentType
	 * @param siteId
	 * @param response
	 * @throws ServiceException
	 */
	public void redirectToPayment(String signUpNo, String paymentType, long siteId, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 获取保证金支付商户ID列表
	 * @param projectId
	 * @param projectPledgeAccount
	 * @return
	 * @throws ServiceException
	 */
	public String getPledgePaymentMerchantIds(long projectId, String projectPledgeAccount) throws ServiceException;
	
	/**
	 * 完成投标书上传
	 * @param signUpNo
	 * @throws ServiceException
	 */
	public void completeUploadBid(String signUpNo) throws ServiceException;
	
	/**
	 * 报名统计
	 * @param projectId
	 * @return
	 * @throws ServiceException
	 */
	public SignUpTotal totalSignUp(long projectId) throws ServiceException;
	
	/**
	 * 获取报名费金额
	 * @param projectId
	 * @return
	 * @throws ServiceException
	 */
	public double getSignUpPrice(long projectId) throws ServiceException;
	
	/**
	 * 获取投标书上传的缓冲时间,以分钟为单位
	 * @return
	 */
	public int getBidUploadPaddingMinutes();
	
	/**
	 * 处理银行交易记录
	 * @param bank
	 * @param transactionsFileName
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void processBankTransactions(String bank, String transactionsFileName, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 对企业信息解密
	 * @param project
	 * @return
	 * @throws ServiceException
	 */
	public void decryptSignUps(BiddingProject project) throws ServiceException;
	
	/**
	 * 对企业信息解密
	 * @param signUp
	 * @throws ServiceException
	 */
	public void decryptSignUp(BiddingSignUp signUp) throws ServiceException;
	
	/**
	 * 账户信息完善
	 * @param projectId
	 * @param signUpId
	 * @param enterpriseName
	 * @param enterpriseBank
	 * @param enterpriseAccount
	 * @throws ServiceException
	 */
	public void accountComplement(long projectId, long signUpId, String enterpriseName, String enterpriseBank, String enterpriseAccount) throws ServiceException;
	
	/**
	 * 批量账户信息完善
	 * @param biddingProject
	 * @param request
	 * @throws ServiceException
	 */
	public void accountsComplement(BiddingProject biddingProject, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 保存评标排行
	 * @param biddingProject
	 * @param rankingSignUpIds
	 * @throws ServiceException
	 */
	public void saveSignUpRanking(BiddingProject biddingProject, String rankingSignUpIds) throws ServiceException;
	
	/**
	 * 获取候选报名记录列表
	 * @param biddingProject
	 * @return
	 * @throws ServiceException
	 */
	public List listRankingSignUps(BiddingProject biddingProject) throws ServiceException;
	
	/**
	 * 计算保证金回退金额
	 * @param biddingProject
	 * @throws ServiceException
	 */
	public void retrievePledgeReturnMoney(BiddingProject biddingProject) throws ServiceException;
	
	/**
	 * 生成保证金转账单
	 * @param project
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public void writePledgeTransferFile(BiddingProject project, HttpServletRequest request, HttpServletResponse response) throws ServiceException;
	
	/**
	 * 保证金返还转账
	 * @param project
	 * @param transferPassword 转账密码
	 * @param passwordMatcher 密码匹配器
	 * @param request
	 * @throws ServiceException
	 */
	public void pledgeReturnTransfer(BiddingProject project, String transferPassword, Matcher passwordMatcher, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 查询银行交易记录,更新保证金的缴纳、退还状态
	 * @throws ServiceException
	 */
	public void bankTransactionsQuery() throws ServiceException;
	
	/**
	 * 如果报名记录中有保证金已转账但未确认退还的,则检查保证金是否退还成功
	 * @param projectId
	 * @throws ServiceException
	 */
	public void updatePledgeReturnStatus(long projectId) throws ServiceException;
	
	/**
	 * 更新保证金缴纳状态
	 * @param projectId
	 * @return
	 * @throws ServiceException
	 */
	public void updatePledgeStatus(BiddingProject project) throws ServiceException;
	
	/**
	 * 保证金信息是否可以显示
	 * @param project
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	public boolean isPledgeVisible(BiddingProject project, SessionInfo sessionInfo) throws ServiceException;
	
	/**
	 * 更新报名记录
	 * @param bidEnterprise
	 */
	public void updateSignUps(BiddingBidEnterprise bidEnterprise) throws ServiceException;
	
	/**
	 * 检查是否不在报名时间范围内
	 * @param project
	 * @return
	 */
	public boolean isSignUpTimeout(BiddingProject project);
	
	/**
	 * 检查是否不在缴纳保证金时间范围内
	 * @param project
	 * @return
	 */
	public boolean isPledgePaymentTimeout(BiddingProject project);
}