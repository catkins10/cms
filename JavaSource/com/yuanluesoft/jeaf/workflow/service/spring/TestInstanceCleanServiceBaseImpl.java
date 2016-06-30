/*
 * Created on 2006-9-10
 *
 */
package com.yuanluesoft.jeaf.workflow.service.spring;

import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.workflow.service.TestInstanceCleanService;

/**
 * 
 * @author linchuan
 *
 */
public class TestInstanceCleanServiceBaseImpl implements TestInstanceCleanService {
	private DatabaseService databaseService;
	private String pojoClassName;
	private BusinessDefineService businessDefineService;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.service.TestInstanceCleanService#clean()
	 */
	public void clean() throws ServiceException {
		//查找流程对象,权限记录数为0的记录
		String pojoPrivilege = getPrivilegePojoClassName(pojoClassName);
		pojoPrivilege = pojoPrivilege.substring(pojoClassName.lastIndexOf(".") + 1);
		String hql = " from " + pojoPrivilege + " " + pojoPrivilege +
					 " where (select count(*) from " + pojoPrivilege + " " + pojoPrivilege +
					 " 		   where " + pojoPrivilege + ".recordId=" + pojoClassName + ".id)=0";
		List pojos = databaseService.findRecordsByHql(hql);
		if(pojos==null) {
			return;
		}
		BusinessObject businessObject = businessDefineService.getBusinessObject(pojoClassName);
		BusinessService businessService = (BusinessService)Environment.getService(businessObject.getBusinessServiceName()==null ? "businessService" : businessObject.getBusinessServiceName());
		//删除流程对象
		for(Iterator iterator = pojos.iterator(); iterator.hasNext();) {
			Record record = (Record)iterator.next();
			try {
				businessService.delete(record);
			} 
			catch (Exception e) {
				Logger.exception(e);
			}
		}
	}
	
	/**
	 * 获取权限POJO名称
	 * @param pojoClassName
	 * @return
	 */
	private String getPrivilegePojoClassName(String pojoClassName) {
		for(int i=0; i<5; i++) {
			try {
				String className = pojoClassName + "Privilege";
				Class.forName(className); //检查类是否存在
				return className.substring(className.lastIndexOf('.') + 1);
			}
			catch (ClassNotFoundException e) {
				
			}
			try {
				pojoClassName = Class.forName(pojoClassName).getSuperclass().getName(); //查找父类对应的权限POJO
			}
			catch (ClassNotFoundException e) {
				
			}
		}
		return null;
	}
	
	/**
	 * @return Returns the databaseService.
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}
	/**
	 * @param databaseService The databaseService to set.
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
	/**
	 * @return Returns the pojoClassName.
	 */
	public String getPojoClassName() {
		return pojoClassName;
	}
	/**
	 * @param pojoClassName The pojoClassName to set.
	 */
	public void setPojoClassName(String pojoClassName) {
		this.pojoClassName = pojoClassName;
	}

	/**
	 * @return the businessDefineService
	 */
	public BusinessDefineService getBusinessDefineService() {
		return businessDefineService;
	}

	/**
	 * @param businessDefineService the businessDefineService to set
	 */
	public void setBusinessDefineService(BusinessDefineService businessDefineService) {
		this.businessDefineService = businessDefineService;
	}
}