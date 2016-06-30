package com.yuanluesoft.j2oa.info.actions.inforeceive;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.info.pojo.InfoReceive;
import com.yuanluesoft.j2oa.info.service.InfoService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class InfoReceiveAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("manageUnit_filter")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		InfoReceive infoReceive = (InfoReceive)record;
		if(!acl.contains(AccessControlService.ACL_APPLICATION_MANAGER) || (infoReceive.getInfoFilters()!=null && !infoReceive.getInfoFilters().isEmpty())) {
			throw new PrivilegeException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		InfoReceive infoReceive = (InfoReceive)record;
		com.yuanluesoft.j2oa.info.forms.InfoReceive infoReceiveForm = (com.yuanluesoft.j2oa.info.forms.InfoReceive)form;
		InfoService infoService = (InfoService)getService("infoService");
		infoReceiveForm.setNextInfoId(infoService.getNextInfoId(infoReceive, true));
		infoReceiveForm.setPreviousInfoId(infoService.getPreviousReceiveInfoId(infoReceive, true));
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		if(infoReceiveForm.getNextInfoId()==0) {
			infoReceiveForm.getFormActions().removeFormAction("下一篇");
		}
		if(infoReceiveForm.getPreviousInfoId()==0) {
			infoReceiveForm.getFormActions().removeFormAction("上一篇");
		}
		infoReceiveForm.getTabs().addTab(-1, "basic", "基本信息", "SUBFORM", true);
		if(infoReceive.getRevises()!=null && !infoReceive.getRevises().isEmpty()) {
			infoReceiveForm.getTabs().addTab(-1, "revises", "退改稿", "infoRevises.jsp", false);
		}
		if(ListUtils.findObjectByProperty(infoReceive.getRevises(), "editorId", new Long(0))!=null) { //获取已经退了,但没有修改完成
			infoReceiveForm.getFormActions().removeFormAction("退改稿");
		}
	}
}