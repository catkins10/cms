package com.yuanluesoft.fdi.customer.service.spring;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import com.yuanluesoft.fdi.customer.pojo.FdiCustomerCompany;
import com.yuanluesoft.fdi.customer.pojo.FdiCustomerCompanyIndustry;
import com.yuanluesoft.fdi.customer.pojo.FdiCustomerContact;
import com.yuanluesoft.fdi.customer.service.FdiCustomerService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class FdiCustomerServiceImpl extends BusinessServiceImpl implements FdiCustomerService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		Record record = super.load(recordClass, id);
		if(record instanceof FdiCustomerCompany) {
			FdiCustomerCompany company = (FdiCustomerCompany)record;
			if(company.getContacts()!=null && !company.getContacts().isEmpty()) {
				//获取往来记录列表
				String hql = "from FdiCustomerContactDiscuss FdiCustomerContactDiscuss" +
							 " where FdiCustomerContactDiscuss.contactId in (" +
							 "  select FdiCustomerContact.id from FdiCustomerContact FdiCustomerContact where FdiCustomerContact.companyId=" + id +
							 " )" +
							 " order by FdiCustomerContactDiscuss.discussTime DESC";
				List discusses = getDatabaseService().findRecordsByHql(hql);
				for(Iterator iterator = company.getContacts().iterator(); iterator.hasNext();) {
					FdiCustomerContact contact = (FdiCustomerContact)iterator.next();
					List contactDiscusses = ListUtils.getSubListByProperty(discusses, "contactId", new Long(contact.getId()));
					if(contactDiscusses!=null && !contactDiscusses.isEmpty()) {
						contact.setDiscusses(new LinkedHashSet(contactDiscusses));
					}
					else {
						contact.setDiscusses(null);
					}
				}
			}
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.fdi.customer.service.FdiCustomerService#saveOrUpdateIndustries(long, java.lang.String, java.lang.String)
	 */
	public void saveOrUpdateIndustries(long companyId, String industryIds, String industryNames) throws ServiceException {
		getDatabaseService().deleteRecordsByHql("from FdiCustomerCompanyIndustry FdiCustomerCompanyIndustry where FdiCustomerCompanyIndustry.companyId=" + companyId);
		String[] ids = industryIds.split(",");
		String[] names = industryNames.split(",");
		for(int i=0; i<ids.length; i++) {
			FdiCustomerCompanyIndustry companyIndustry = new FdiCustomerCompanyIndustry();
			companyIndustry.setId(UUIDLongGenerator.generateId());
			companyIndustry.setCompanyId(companyId);
			companyIndustry.setIndustryId(Long.parseLong(ids[i]));
			companyIndustry.setIndustry(names[i]);
			getDatabaseService().saveRecord(companyIndustry);
		}
	}
}