/*
 * Created on 2006-7-1
 *
 */
package com.yuanluesoft.j2oa.addresslist.service;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 *
 * @author linchuan
 *
 */
public interface AddressListService extends BusinessService {
	
    /**
     * 添加记录到通讯录
     * @param name
     * @param mailAddress
     * @throws ServiceException
     */
    public void addAddressList(String mailFrom, SessionInfo sessionInfo) throws ServiceException;
}
