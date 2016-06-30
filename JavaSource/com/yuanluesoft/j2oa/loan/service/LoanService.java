/*
 * Created on 2006-6-22
 *
 */
package com.yuanluesoft.j2oa.loan.service;

import java.util.List;

import com.yuanluesoft.j2oa.loan.pojo.Loan;
import com.yuanluesoft.j2oa.loan.pojo.LoanConfig;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 *
 * @author linchuan
 *
 */
public interface LoanService extends BusinessService {
    
	/**
     * 获取个人需要还款的借款单列表
     * @param sessionInfo
     * @return
     * @throws ServiceException
     */
    public List listPersonalTorepayLoans(SessionInfo sessionInfo) throws ServiceException;
    
    /**
     * 借款核销
     * @param sessionInfo
     * @param loanId
     * @param money
     * @throws ServiceException
     */
    public double reimburseLoan(Loan loan, double money, SessionInfo sessionInfo) throws ServiceException;
    
    /**
     * 获取报销配置
     * @return
     * @throws ServiceException
     */
    public LoanConfig getLoanConfig() throws ServiceException;
}
