/*
 * Created on 2006-6-22
 *
 */
package com.yuanluesoft.j2oa.reimburse.service.spring;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.reimburse.pojo.ReimburseChargeCategory;
import com.yuanluesoft.j2oa.reimburse.service.ChargeCategoryService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 *
 * @author linchuan
 *
 */
public class ChargeCategoryServiceImpl extends BusinessServiceImpl implements ChargeCategoryService {

    /* (non-Javadoc)
     * @see com.yuanluesoft.j2oa.reimburse.service.ChargeStandardService#listChargeCategories()
     */
    public List listChargeCategories() throws ServiceException {
        return getDatabaseService().findRecordsByHql("from ReimburseChargeCategory ReimburseChargeCategory order by ReimburseChargeCategory.category");
    }

    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		return getDatabaseService().findRecordsByHql("select ReimburseChargeCategory.category, ReimburseChargeCategory.id from ReimburseChargeCategory ReimburseChargeCategory order by ReimburseChargeCategory.category");
	}

	/* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateDataIntegrity(java.lang.Object)
     */
    public List validateDataIntegrity(Record record, boolean isNew) throws ServiceException {
        List errors = new ArrayList();
        //用户ID+费用类别不能重复
        ReimburseChargeCategory currentChargeCategory = (ReimburseChargeCategory)record;
        ReimburseChargeCategory category = (ReimburseChargeCategory)getDatabaseService().findRecordByHql("from ReimburseChargeCategory ReimburseChargeCategory where ReimburseChargeCategory.category='" + JdbcUtils.resetQuot(currentChargeCategory.getCategory()) + "'");
        if(category!=null && category.getId()!=currentChargeCategory.getId()) {
			errors.add("相同费用类别已存在");
		}
		return errors.isEmpty() ? null : errors;
    }
}