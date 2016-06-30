package com.yuanluesoft.telex.receive.base.actions.receivetelegram;

import java.util.Iterator;
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
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.telex.base.service.TelexService;
import com.yuanluesoft.telex.receive.base.pojo.ReceiveTelegram;
import com.yuanluesoft.telex.receive.base.pojo.TelegramSign;

/**
 * 
 * @author linchuan
 *
 */
public abstract class ReceiveTelegramAction extends FormAction {
	protected boolean isCryptic = false;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("application_manager")) { //管理员,始终都有编辑权限
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(OPEN_MODE_CREATE.equals(openMode)) { //登记
			if(acl.contains("manageUnit_create")) { //有登记权限
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			throw new PrivilegeException();
		}
		//非登记状态且未归档
		ReceiveTelegram telegram = (ReceiveTelegram)record;
		if(telegram.getArchiveTime()==null && (acl.contains("manageUnit_create") || isSignTransactor(form, sessionInfo))) { //未归档,有签收清退经办权限
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		if(acl.contains("application_visitor")) {
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		throw new PrivilegeException();
	}
	
	/**
	 * 检查用户是否有签收清退经办权限
	 * @param sessionInfo
	 * @return
	 */
	protected boolean isSignTransactor(ActionForm form, SessionInfo sessionInfo) {
		//检查用户的签收/清退经办权限
		try {
			List acl = getAccessControlService().getAcl(form.getFormDefine().getApplicationName(), sessionInfo);
			if(acl.contains("application_manager") || acl.contains("manageUnit_sign")) {
				return true;
			}
		}
		catch(Exception e) {
			
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		com.yuanluesoft.telex.receive.base.forms.ReceiveTelegram telegramForm = (com.yuanluesoft.telex.receive.base.forms.ReceiveTelegram)form;
		//删除没有权限的操作
		List actions = form.getFormActions();
		ReceiveTelegram telegram = (ReceiveTelegram)record;
		telegramForm.setSignEnabled(isSignTransactor(form, sessionInfo));
		if(telegram==null || !telegramForm.isSignEnabled() || telegram.getArchiveTime()!=null) { //不是经办人或已归档
			actions.remove(ListUtils.findObjectByProperty(actions, "title", "签收/清退"));
			actions.remove(ListUtils.findObjectByProperty(actions, "title", "归档"));
		}
		else if(telegram.getSigns()==null || telegram.getSigns().isEmpty()) {
			actions.remove(ListUtils.findObjectByProperty(actions, "title", "签收/清退"));	
		}
		if(telegram==null || telegram.getSigns()==null || telegram.getSigns().isEmpty()) { //不在办理状态
			//显示"保存并转办理",不显示"保存"
			actions.remove(ListUtils.findObjectByProperty(actions, "title", "保存"));	
		}
		else { //办理状态
			//不显示"保存并转办理",显示"保存"
			actions.remove(ListUtils.findObjectByProperty(actions, "title", "保存并转办理"));	
		}
		if(telegram!=null) {
			if(telegram.getArchiveTime()!=null) { //已归档
				//不显示"归档",显示"撤销归档"
				actions.remove(ListUtils.findObjectByProperty(actions, "title", "归档"));
			}
			else { //未归档
				//显示"归档",不显示"撤销归档"
				actions.remove(ListUtils.findObjectByProperty(actions, "title", "撤销归档"));
				//检查是否办理完毕
				boolean complete = true;
				if(telegram.getSigns()!=null && !telegram.getSigns().isEmpty()) {
					for(Iterator iterator = telegram.getSigns().iterator(); iterator.hasNext();) {
						TelegramSign sign = (TelegramSign)iterator.next();
						if(sign.getSignTime()==null || (sign.getNeedReturn()=='1' && sign.getReturnTime()==null)) {
							complete = false;
							break;
						}
					}
				}
				if(!complete) { //未办理完毕
					actions.remove(ListUtils.findObjectByProperty(actions, "title", "归档"));
				}
			}
		}
		//设置TAB页签
		telegramForm.getTabs().addTab(-1, "basic", "基本信息", null, true);
		if(!OPEN_MODE_CREATE.equals(openMode)) {
			telegramForm.getTabs().addTab(-1, "sign", "收报办理", null, true);
			ReceiveTelegram receiveTelegram = (ReceiveTelegram)record;
			if(receiveTelegram.getSigns()!=null && !receiveTelegram.getSigns().isEmpty()) {
				telegramForm.getTabs().addTab(-1, "signOpinion", "办理意见", null, false);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		com.yuanluesoft.telex.receive.base.forms.ReceiveTelegram telegramForm = (com.yuanluesoft.telex.receive.base.forms.ReceiveTelegram)form;
		telegramForm.setCreated(DateTimeUtils.now()); //创建时间
		telegramForm.setCreator(sessionInfo.getUserName()); //创建人
		telegramForm.setReceiveTime(DateTimeUtils.now()); //接收时间
		telegramForm.setReceiverName(sessionInfo.getUserName()); //接收人
		telegramForm.setReceiverId(sessionInfo.getUserId()); //接收人ID
		//设置流水号
		TelexService telexService = (TelexService)getService("telexService");
		telegramForm.setSequence("" + telexService.getNextSequence(false, isCryptic, true));
		//设置意见填写时间
		telegramForm.setOpinionCreated(DateTimeUtils.date());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		com.yuanluesoft.telex.receive.base.forms.ReceiveTelegram telegramForm = (com.yuanluesoft.telex.receive.base.forms.ReceiveTelegram)form;
		//设置意见填写时间
		telegramForm.setOpinionCreated(DateTimeUtils.date());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			ReceiveTelegram telegram = (ReceiveTelegram)record;
			telegram.setCreated(DateTimeUtils.now()); //创建时间
			telegram.setCreator(sessionInfo.getUserName()); //创建人
			telegram.setCreatorId(sessionInfo.getUserId()); //创建人ID
			//设置流水号
			TelexService telexService = (TelexService)getService("telexService");
			telegram.setSequence("" + telexService.getNextSequence(false, isCryptic, false));
			//明/密
			telegram.setIsCryptic(isCryptic ? '1' : '0');
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}