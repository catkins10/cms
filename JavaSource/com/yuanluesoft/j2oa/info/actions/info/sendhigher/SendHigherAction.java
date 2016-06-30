package com.yuanluesoft.j2oa.info.actions.info.sendhigher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.info.actions.info.InfoAction;
import com.yuanluesoft.j2oa.info.forms.SendHigher;
import com.yuanluesoft.j2oa.info.pojo.InfoFilter;
import com.yuanluesoft.j2oa.info.pojo.InfoMagazineDefine;
import com.yuanluesoft.j2oa.info.pojo.InfoSendHigher;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class SendHigherAction extends InfoAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadComponentPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		//检查用户是否刊物的编辑或者主编
		InfoFilter info = (InfoFilter)record;
		try {
			if(getRecordControlService().getAccessLevel(info.getMagazineDefineId(), InfoMagazineDefine.class.getName(), sessionInfo)>=RecordControlService.ACCESS_LEVEL_READONLY) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		catch (ServiceException e) {
		
		}
		return super.checkLoadComponentPrivilege(form, request, record, component, acl, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillComponentForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillComponentForm(ActionForm form, Record component, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.fillComponentForm(form, component, mainRecord, componentName, sessionInfo, request);
		InfoSendHigher sendHigher = (InfoSendHigher)component;
		SendHigher sendHigherForm = (SendHigher)form;
		sendHigherForm.setUsed(sendHigher.getUseTime()!=null);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponentRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		InfoSendHigher sendHigher = (InfoSendHigher)component;
		SendHigher sendHigherForm = (SendHigher)form;
		if(!sendHigherForm.isUsed()) {
			sendHigher.setUseTime(null);
		}
		else if(sendHigher.getUseTime()==null) {
			sendHigher.setUseTime(DateTimeUtils.now());
			sendHigher.setUseRegister(sessionInfo.getUserName());
			sendHigher.setUseRegisterId(sessionInfo.getUserId());
		}
		super.saveComponentRecord(form, mainRecord, component, componentName, foreignKeyProperty, sessionInfo, request);
	}
}