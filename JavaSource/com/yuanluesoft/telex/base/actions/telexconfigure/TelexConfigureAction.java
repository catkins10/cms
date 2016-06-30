package com.yuanluesoft.telex.base.actions.telexconfigure;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.telex.base.forms.TelexConfigure;
import com.yuanluesoft.telex.base.service.TelexService;

/**
 * 
 * @author linchuan
 *
 */
public class TelexConfigureAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("application_manager")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		TelexConfigure configureForm = (TelexConfigure)form;
		TelexService telexService = (TelexService)getService("telexService");
		configureForm.setSecurityLevels(ListUtils.join(telexService.listTelegramSecurityLevels(), ",", false)); //密级列表
		configureForm.setLevels(ListUtils.join(telexService.listTelegramLevels(), ",", false)); //级别列表
		configureForm.setCategories(ListUtils.join(telexService.listTelegramCategories(), ",", false)); //分类列表
		//流水号
		configureForm.setCurrentSendSn(telexService.getCurrentSequence(true, false));
		configureForm.setCurrentCrypticSendSn(telexService.getCurrentSequence(true, true));
		configureForm.setCurrentReceiveSn(telexService.getCurrentSequence(false, false));
		configureForm.setCurrentCrypticReceiveSn(telexService.getCurrentSequence(false, true));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		TelexConfigure configureForm = (TelexConfigure)form;
		TelexService telexService = (TelexService)getService("telexService");
		telexService.saveTelegramSecurityLevels(configureForm.getSecurityLevels()); //保存密级列表
		telexService.saveTelegramLevels(configureForm.getLevels()); //保存等级列表
		telexService.saveTelegramCategories(configureForm.getCategories()); //保存级别列表
		//保存流水号
		telexService.saveCurrentSequence(true, false, configureForm.getCurrentSendSn());
		telexService.saveCurrentSequence(true, true, configureForm.getCurrentCrypticSendSn());
		telexService.saveCurrentSequence(false, false, configureForm.getCurrentReceiveSn());
		telexService.saveCurrentSequence(false, true, configureForm.getCurrentCrypticReceiveSn());
		return null;
	}
}
