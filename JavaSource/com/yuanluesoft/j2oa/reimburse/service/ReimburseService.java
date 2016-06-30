/*
 * Created on 2006-6-20
 *
 */
package com.yuanluesoft.j2oa.reimburse.service;

import com.yuanluesoft.j2oa.reimburse.pojo.Reimburse;
import com.yuanluesoft.j2oa.reimburse.pojo.ReimburseConfig;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 *
 * @author linchuan
 *
 */
public interface ReimburseService extends BusinessService {

	/**
     * 保存待核销借款单
     * @param reimburse
     * @param selectedLoanIds
     * @throws ServiceException
     */
    public void saveReimburseLoans(Reimburse reimburse, String selectedLoanIds) throws ServiceException;
    
    /**
     * 支付
     * @param reimburse
     * @throws ServiceException
     */
    public void pay(Reimburse reimburse, SessionInfo sessionInfo) throws ServiceException;
    
    /**
     * 获取报销参数配置
     * @return
     * @throws ServiceException
     */
    public ReimburseConfig getReimburseConfig() throws ServiceException;
}