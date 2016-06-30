package com.yuanluesoft.j2oa.info.actions.revise;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.info.forms.ReviseInfo;
import com.yuanluesoft.j2oa.info.pojo.InfoFilter;
import com.yuanluesoft.j2oa.info.pojo.InfoReceive;
import com.yuanluesoft.j2oa.info.pojo.InfoRevise;
import com.yuanluesoft.j2oa.info.service.InfoService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author linchuan
 *
 */
public class ReviseAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(!OPEN_MODE_CREATE.equals(openMode)) {
			InfoRevise revise = (InfoRevise)record;
			if(sessionInfo.getUserId()==revise.getRevisePersonId()) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			throw new PrivilegeException();
		}
		ReviseInfo reviseForm = (ReviseInfo)form;
		if(reviseForm.getIsReceive()==1) { //投稿
			if(acl.contains("manageUnit_filter")) { //有过滤权限
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		else {
			try {
				if(getRecordControlService().getAccessLevel(reviseForm.getInfoId(), InfoFilter.class.getName(), sessionInfo)>=RecordControlService.ACCESS_LEVEL_READONLY) {
					return RecordControlService.ACCESS_LEVEL_EDITABLE;
				}
			}
			catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		throw new PrivilegeException();
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		ReviseInfo reviseForm = (ReviseInfo)form;
		InfoService infoService = (InfoService)getService("infoService");
		reviseForm.setSubject(getSubject(reviseForm));
		reviseForm.setRevisePersonTel(infoService.getRevisePersonTel(sessionInfo.getUserId()));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			ReviseInfo reviseForm = (ReviseInfo)form;
			InfoRevise revise = (InfoRevise)record;
			revise.setSubject(getSubject(reviseForm));
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
	
	/**
	 * 获取信息标题
	 * @param reviseForm
	 * @return
	 * @throws Exception
	 */
	private String getSubject(ReviseInfo reviseForm) throws Exception {
		InfoService infoService = (InfoService)getService("infoService");
		if(reviseForm.getIsReceive()==1) { //投稿
			return ((InfoReceive)infoService.load(InfoReceive.class, reviseForm.getInfoId())).getSubject();
		}
		else {
			return ((InfoFilter)infoService.load(InfoFilter.class, reviseForm.getInfoId())).getSubject();
		}
	}
}