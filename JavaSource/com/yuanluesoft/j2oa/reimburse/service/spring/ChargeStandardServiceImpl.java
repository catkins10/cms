/*
 * Created on 2006-6-21
 *
 */
package com.yuanluesoft.j2oa.reimburse.service.spring;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.j2oa.reimburse.pojo.ReimburseChargeCategory;
import com.yuanluesoft.j2oa.reimburse.pojo.ReimburseChargeStandard;
import com.yuanluesoft.j2oa.reimburse.service.ChargeStandardService;
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
public class ChargeStandardServiceImpl extends BusinessServiceImpl implements ChargeStandardService {

    /* (non-Javadoc)
     * @see com.yuanluesoft.j2oa.reimburse.service.ChargeStandardService#getChargeStandard(com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
     */
    public String getChargeStandard(SessionInfo sessionInfo, String chargeCategory) throws ServiceException {
        String hql = "from ReimburseChargeStandard ReimburseChargeStandard" +
        			 " where ReimburseChargeStandard.chargeCategory.category='" + JdbcUtils.resetQuot(chargeCategory) + "'" +
        			 " and ReimburseChargeStandard.userId in (" + sessionInfo.getUserIds() + ")" +
        			 " order by ReimburseChargeStandard.money DESC";
        ReimburseChargeStandard chargeStandard = (ReimburseChargeStandard)getDatabaseService().findRecordByHql(hql);
        if(chargeStandard==null) {
            hql = "from ReimburseChargeCategory ReimburseChargeCategory" +
			 " where ReimburseChargeCategory.category='" + JdbcUtils.resetQuot(chargeCategory) + "'";
		    ReimburseChargeCategory category = (ReimburseChargeCategory)getDatabaseService().findRecordByHql(hql);
		    if(category==null || category.getMoney()==0.0) {
		        return "";
		    }
		    String unit = category.getUnit();
            return category.getMoney() + "元" + (unit==null || unit.equals("") ? "" : "/" + unit);
        }
        else {
            String unit = chargeStandard.getChargeCategory().getUnit();
            return chargeStandard.getMoney() + "元" + (unit==null || unit.equals("") ? "" : "/" + unit);
        }
    }
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateDataIntegrity(java.lang.Object)
     */
    public List validateDataIntegrity(Record record, boolean isNew) throws ServiceException {
        List errors = new ArrayList();
        //用户ID+费用类别不能重复
        ReimburseChargeStandard currentChargeStandard = (ReimburseChargeStandard)record;
        ReimburseChargeStandard standard = (ReimburseChargeStandard)getDatabaseService().findRecordByHql("from ReimburseChargeStandard ReimburseChargeStandard where ReimburseChargeStandard.userId=" + currentChargeStandard.getUserId() + " and ReimburseChargeStandard.chargeCategoryId=" + currentChargeStandard.getChargeCategoryId());
        if(standard!=null && standard.getId()!=currentChargeStandard.getId()) {
			errors.add("相同费用标准已存在");
		}
		return errors.isEmpty() ? null : errors;

    }
}
