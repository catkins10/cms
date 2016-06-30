package com.yuanluesoft.appraise.actions.appraise.admin;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.appraise.actions.AppraiseFormAction;
import com.yuanluesoft.appraise.forms.admin.Appraise;
import com.yuanluesoft.appraise.pojo.AppraiseTask;
import com.yuanluesoft.appraise.service.AppraiseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class AppraiseAction extends AppraiseFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) { //新记录
			Appraise appraiseForm = (Appraise)form;
			AppraiseTask appraiseTask = getAppraiseTask(appraiseForm, request);
			if(getOrgService().checkPopedom(appraiseTask.getAreaId(), "appraiseManager", sessionInfo)) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			throw new PrivilegeException();
		}
		if(acl.contains(AccessControlService.ACL_APPLICATION_MANAGER)) { //应用管理
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		super.checkLoadPrivilege(form, request, record, openMode, acl, sessionInfo);
		return RecordControlService.ACCESS_LEVEL_READONLY; //旧记录,总是只读
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		if(!OPEN_MODE_CREATE.equals(openMode)) {
			form.setSubForm("Read");
		}
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		form.getTabs().addTab(-1, "basic", "基本信息", "SUBFORM", true);
		if(!OPEN_MODE_CREATE.equals(openMode)) {
			form.getTabs().addTab(-1, "unitAppraises", "参评单位", "unitAppraises.jsp", true);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.appraise.actions.AppraiseFormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Appraise appraiseForm = (Appraise)form;
		AppraiseTask appraiseTask = getAppraiseTask(appraiseForm, request);
		AppraiseService appraiseService = (AppraiseService)getService("appraiseService");
		appraiseForm.setTaskName(appraiseTask.getName()); //评议任务名称
		appraiseForm.setAppraiserType(appraiseTask.getAppraiserType()); //评议员类型,0/基础,1/管理服务对象
		appraiseForm.setAreaId(appraiseTask.getAreaId()); //地区ID
		appraiseForm.setArea(appraiseTask.getArea()); //地区名称
		Date date = DateTimeUtils.date();
		if(appraiseForm.getAppraiseYear()==0) {
			appraiseForm.setAppraiseYear(DateTimeUtils.getYear(date)); //评议年度
			appraiseForm.setAppraiseMonth(DateTimeUtils.getMonth(date) + 1); //评议月份
		}
		appraiseForm.setName(appraiseService.generateAppraiseName(appraiseTask, appraiseForm.getAppraiseYear(), appraiseForm.getAppraiseMonth()));
		appraiseForm.setCreator(sessionInfo.getUserName()); //发起人
		appraiseForm.setCreated(DateTimeUtils.now()); //发起时间
		appraiseForm.setEndTime(DateTimeUtils.add(appraiseForm.getCreated(), Calendar.DAY_OF_MONTH, appraiseTask.getAppraiseDays())); //截止时间
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			AppraiseService appraiseService = (AppraiseService)getService("appraiseService");
			Appraise appraiseForm = (Appraise)form;
			try {
				return appraiseService.startupAppraise(appraiseForm.getName(), appraiseForm.getTaskId(), appraiseForm.getAppraiseYear(), appraiseForm.getAppraiseMonth(), appraiseForm.getEndTime(), true, appraiseForm.isCancelNoAppraisersUnit(), sessionInfo);
			}
			catch(ServiceException se) {
				if(se.getMessage()!=null) {
					form.setError(se.getMessage());
					throw new ValidateException();
				}
				throw se;
			}
		}
		return record;
	}

	/**
	 * 获取评测任务
	 * @param appraiseForm
	 * @param request
	 * @return
	 */
	private AppraiseTask getAppraiseTask(Appraise appraiseForm, HttpServletRequest request) {
		try {
			AppraiseService appraiseService = (AppraiseService)getService("appraiseService");
			return (AppraiseTask)appraiseService.load(AppraiseTask.class, appraiseForm.getTaskId());
		} 
		catch (ServiceException e) {
			throw new Error();
		}
	}
}