/*
 * Created on 2006-6-21
 *
 */
package com.yuanluesoft.j2oa.reimburse.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 *
 * @author linchuan
 *
 */
public interface ChargeStandardService extends BusinessService {
    /**
     * 获取费用标准,如:200.0元/天
     * @param sessionInfo
     * @return
     * @throws ServiceException
     */
    public String getChargeStandard(SessionInfo sessionInfo, String chargeCategory) throws ServiceException;
}
