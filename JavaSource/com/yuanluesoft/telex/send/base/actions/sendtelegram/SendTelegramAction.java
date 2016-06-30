package com.yuanluesoft.telex.send.base.actions.sendtelegram;

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
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.telex.base.service.TelexService;
import com.yuanluesoft.telex.send.base.pojo.SendTelegram;

/**
 * 
 * @author linchuan
 *
 */
public abstract class SendTelegramAction extends FormAction {
	protected boolean isCryptic = false;

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("application_manager") || acl.contains("manageUnit_create")) {
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(OPEN_MODE_CREATE.equals(openMode)) {
			throw new PrivilegeException();
		}
		if(acl.contains("application_visitor")) {
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		form.setSubForm("../" + form.getSubForm());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		com.yuanluesoft.telex.send.base.forms.SendTelegram telegramForm = (com.yuanluesoft.telex.send.base.forms.SendTelegram)form;
		telegramForm.setCreated(DateTimeUtils.now()); //创建时间
		telegramForm.setCreator(sessionInfo.getUserName()); //创建人
		telegramForm.setSendTime(DateTimeUtils.now()); //发送时间
		//设置流水号
		TelexService telexService = (TelexService)getService("telexService");
		telegramForm.setSequence("" + telexService.getNextSequence(true, isCryptic, true));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			SendTelegram telegram = (SendTelegram)record;
			telegram.setCreated(DateTimeUtils.now()); //创建时间
			telegram.setCreator(sessionInfo.getUserName()); //创建人
			telegram.setCreatorId(sessionInfo.getUserId()); //创建人ID
			//设置流水号
			TelexService telexService = (TelexService)getService("telexService");
			telegram.setSequence("" + telexService.getNextSequence(true, isCryptic, false));
			//明/密
			telegram.setIsCryptic(isCryptic ? '1' : '0');
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}