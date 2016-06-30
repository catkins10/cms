/*
 * Created on 2006-7-1
 *
 */
package com.yuanluesoft.j2oa.addresslist.service.spring;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.addresslist.pojo.AddressList;
import com.yuanluesoft.j2oa.addresslist.service.AddressListService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 *
 * @author linchuan
 *
 */
public class AddressListServiceImpl extends BusinessServiceImpl implements AddressListService {
    
    /* (non-Javadoc)
     * @see com.yuanluesoft.j2oa.addresslist.service.AddresslistService#addAddress(java.lang.String, java.lang.String)
     */
    public void addAddressList(String mailFrom, SessionInfo sessionInfo) throws ServiceException {
        String name;
        String mailAddress;
        int index = mailFrom.indexOf("<");
		if(index==-1) {
		    mailAddress = mailFrom;
		    name = mailFrom.substring(0, mailFrom.indexOf('@'));
		}
		else {
		    mailAddress = mailFrom.substring(index + 1, mailFrom.length() - 1);
		    name = mailFrom.substring(0, index).trim();
			if(name.charAt(0)=='"') {
				name = name.substring(1, name.length() - 1);
			}
		}
        //判断用户名是否重复
        String hql = "from AddressList AddressList" +
        			 " where AddressList.isPersonal='1'" + 
        			 " and AddressList.creatorId=" + sessionInfo.getUserId() + 
        			 " and AddressList.name='" + JdbcUtils.resetQuot(name) + "'";
        if(getDatabaseService().findRecordByHql(hql)!=null) {
            return;
        }
        //新建记录
        AddressList addressList = new AddressList();
        addressList.setId(UUIDLongGenerator.generateId());
        addressList.setCreatorId(sessionInfo.getUserId());
        addressList.setIsPersonal('1');
        addressList.setName(name);
        addressList.setEmail(mailAddress);
        getDatabaseService().saveRecord(addressList);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		List defaultCategories = ListUtils.generateList("国际组织,常用网址,常用电话号码,单位内部各部门,下级单位,兄弟单位,客户,朋友,同学", ",");
        List categories = getDatabaseService().findRecordsByHql("select distinct AddressList.category from AddressList AddressList where AddressList.creatorId=" + sessionInfo.getUserId() + " and AddressList.category<>'' order by AddressList.category");
        if(categories==null || categories.isEmpty()) {
            return defaultCategories;
        }
        for(Iterator iterator = defaultCategories.iterator(); iterator.hasNext();) {
            if(categories.indexOf(iterator.next())!=-1) { //删除重复的分类
                iterator.remove();
            }
        }
        categories.addAll(defaultCategories);
        return categories;
	}
}