package com.yuanluesoft.jeaf.adjustpriority.service.spring;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.jeaf.adjustpriority.service.PriorityService;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;
import com.yuanluesoft.jeaf.view.service.ViewService;

/**
 * 
 * @author linchuan
 *
 */
public class PriorityServiceImpl implements PriorityService {
	private ViewService viewService;
	private ViewDefineService viewDefineService;
	private BusinessDefineService businessDefineService; //业务逻辑定义服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.adjustpriority.service.PriorityService#adjustPriority(java.lang.String, com.yuanluesoft.jeaf.view.model.View)
	 */
	public void adjustPriority(String highPriorityRecordIds, View view, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		//获取业务服务
		BusinessObject businessObject = businessDefineService.getBusinessObject(view.getPojoClassName());
		BusinessService businessService = (BusinessService)Environment.getService(businessObject==null || businessObject.getBusinessServiceName()==null ? "businessService" : businessObject.getBusinessServiceName());
		//清除现有记录的优先级
		List highPriorityRecords = listHighPriorityRecords(view, request, sessionInfo);
		if(highPriorityRecords!=null && !highPriorityRecords.isEmpty()) {
			for(Iterator iterator = highPriorityRecords.iterator(); iterator.hasNext();) {
				Record record = (Record)iterator.next();
				try {
					record = businessService.load(record.getClass(), record.getId());
					PropertyUtils.setProperty(record, "priority", new Float(0.0f));
					businessService.update(record);
				}
				catch (Exception e) {
					Logger.exception(e);
					throw new ServiceException();
				}
			}
		}
		if(highPriorityRecordIds==null || "".equals(highPriorityRecordIds)) {
			return;
		}
		//设置优先级
		String[] ids = highPriorityRecordIds.split(",");
		for(int i=0; i<ids.length; i++) {
			try {
				Record record = businessService.load(Class.forName(view.getPojoClassName()),Long.parseLong(ids[i]));
				PropertyUtils.setProperty(record, "priority", new Float(ids.length - i));
				businessService.update(record);
			}
			catch (Exception e) {
				Logger.exception(e);
				throw new ServiceException();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.adjustpriority.service.PriorityService#listHighPriorityRecords(com.yuanluesoft.jeaf.view.model.View)
	 */
	public List listHighPriorityRecords(View view, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		String pojoShortName = view.getPojoClassName().substring(view.getPojoClassName().lastIndexOf(".") + 1);
		view.addWhere(pojoShortName + ".priority>0");
		if(view.getOrderBy()==null || view.getOrderBy().equals("")) {
			view.setOrderBy(pojoShortName + ".priority DESC");
		}
		else if(view.getOrderBy().indexOf(".priority")==-1) {
			view.setOrderBy(pojoShortName + ".priority DESC" + "," + view.getOrderBy());
		}
		ViewPackage viewPackage = new ViewPackage();
		viewPackage.setViewMode(View.VIEW_DISPLAY_MODE_NORMAL);
		try {
			viewService.retrieveViewPackage(viewPackage, view, 0, true, true, false, request, sessionInfo);
		}
		catch (PrivilegeException e) {
			
		}
		return viewPackage.getRecords();
	}

	/**
	 * @return the viewDefineService
	 */
	public ViewDefineService getViewDefineService() {
		return viewDefineService;
	}

	/**
	 * @param viewDefineService the viewDefineService to set
	 */
	public void setViewDefineService(ViewDefineService viewDefineService) {
		this.viewDefineService = viewDefineService;
	}

	/**
	 * @return the viewService
	 */
	public ViewService getViewService() {
		return viewService;
	}

	/**
	 * @param viewService the viewService to set
	 */
	public void setViewService(ViewService viewService) {
		this.viewService = viewService;
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
