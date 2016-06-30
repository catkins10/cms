package com.yuanluesoft.cms.onlineservice.actions.admin.serviceitem.material;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.onlineservice.actions.admin.serviceitem.ServiceItemAction;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class ServiceItemMaterialAction extends ServiceItemAction  {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkDeleteComponentPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeleteComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		super.checkDeleteComponentPrivilege(form, request, record, component, acl, sessionInfo);
		OnlineServiceItem serviceItem = (OnlineServiceItem)record;
		if(serviceItem!=null && "漳州市行政服务中心".equals(serviceItem.getServiceItemGuide().getAddress()) && serviceItem.getCode()!=null && !serviceItem.getCode().isEmpty()) { //for 漳州行政服务中心
			throw new PrivilegeException();
		}
	}
}