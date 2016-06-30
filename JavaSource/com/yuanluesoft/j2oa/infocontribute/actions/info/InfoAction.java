package com.yuanluesoft.j2oa.infocontribute.actions.info;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.infocontribute.forms.Info;
import com.yuanluesoft.j2oa.infocontribute.pojo.InfoContribute;
import com.yuanluesoft.j2oa.infocontribute.service.InfoContributeService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class InfoAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		InfoContribute info = (InfoContribute)record;
		//检查用户的投稿权限
		if(!getOrgService().checkPopedom(OPEN_MODE_CREATE.equals(openMode) ? sessionInfo.getUnitId() : info.getFromUnitId(), "infoContribute", sessionInfo)) {
			throw new PrivilegeException();
		}
		return info==null || info.getContributeTime()==null ? RecordControlService.ACCESS_LEVEL_EDITABLE : RecordControlService.ACCESS_LEVEL_READONLY;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Info infoForm = (Info)form;
		InfoContributeService infoContributeService = (InfoContributeService)getService("infoContributeService");
		if(infoContributeService.getReceiveUnits().indexOf(",")==-1) { //只有一个单位
			infoForm.setReceiveUnitNames(infoContributeService.getReceiveUnits().split(","));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		Info infoForm = (Info)form;
		if(infoForm.getPresetMagazines()!=null && !infoForm.getPresetMagazines().isEmpty()) {
			infoForm.setPresetMagazineIds(infoForm.getPresetMagazines().split(","));
		}
		String receiveUnitNames = ListUtils.join(infoForm.getReceiveUnits(), "unitName", ",", false);
		if(receiveUnitNames!=null && !receiveUnitNames.isEmpty()) {
			infoForm.setReceiveUnitNames(receiveUnitNames.split(","));
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Info infoForm = (Info)form;
		InfoContribute info = (InfoContribute)record;
		infoForm.getTabs().addTab(-1, "basic", "基本信息", "SUBFORM", true);
		if(info!=null && info.getRevises()!=null && !info.getRevises().isEmpty()) {
			infoForm.getTabs().addTab(-1, "revises", "退改稿", "infoRevises.jsp", false);
		}
		if(info!=null && info.getUses()!=null && !info.getUses().isEmpty()) {
			infoForm.getTabs().addTab(-1, "uses", "录用情况", "infoUses.jsp", false);
		}
		if(info!=null && info.getInstructs()!=null && !info.getInstructs().isEmpty()) {
			infoForm.getTabs().addTab(-1, "instructs", "领导批示", "infoInstructs.jsp", false);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Info infoForm = (Info)form;
		InfoContribute info = (InfoContribute)record;
		InfoContributeService infoContributeService = (InfoContributeService)getService("infoContributeService");
		info.setPresetMagazines(ListUtils.join(infoForm.getPresetMagazineIds(), ",", false));
		info = (InfoContribute)super.saveRecord(form, record, openMode, request, response, sessionInfo);
		infoContributeService.updateInfoReceiveUnits(info, infoForm.getReceiveUnitNames());
		return info;
	}
}