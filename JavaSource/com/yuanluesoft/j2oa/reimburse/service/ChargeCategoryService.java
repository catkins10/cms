/*
 * Created on 2006-6-22
 *
 */
package com.yuanluesoft.j2oa.reimburse.service;

import java.util.List;

import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 *
 * @author linchuan
 *
 */
public interface ChargeCategoryService extends BusinessService {
    /**
     * 获取费用类别列表
     * @return
     * @throws ServiceException
     */
    public List listChargeCategories() throws ServiceException;

}
