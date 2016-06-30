package com.yuanluesoft.j2oa.exchange.actions.message.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.exchange.forms.admin.ExchangeMessage;
import com.yuanluesoft.j2oa.exchange.pojo.ExchangeDocument;
import com.yuanluesoft.j2oa.exchange.service.ExchangeDocumentService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class MessageAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		ExchangeMessage messageForm = (ExchangeMessage)form;
		com.yuanluesoft.j2oa.exchange.pojo.ExchangeMessage message = (com.yuanluesoft.j2oa.exchange.pojo.ExchangeMessage)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			ExchangeDocumentService exchangeDocumentService = (ExchangeDocumentService)getService("exchangeDocumentService");
			try {
				if(messageForm.getReplyMessageId()>0) { //答复
					//允许发文单位的发送人创建
					message = (com.yuanluesoft.j2oa.exchange.pojo.ExchangeMessage)exchangeDocumentService.load(com.yuanluesoft.j2oa.exchange.pojo.ExchangeMessage.class, messageForm.getReplyMessageId());
					if(message.getDocument().getCreatorUnitId()==sessionInfo.getUnitId() &&
					   getOrgService().checkPopedom(sessionInfo.getUnitId(), "sendDocument", sessionInfo)) {
						return RecordControlService.ACCESS_LEVEL_EDITABLE;
					}
				}
				else { //反馈
					//允许接收单位的接收人创建
					ExchangeDocument document = (ExchangeDocument)exchangeDocumentService.load(ExchangeDocument.class, messageForm.getDocumentId());
					if(ListUtils.findObjectByProperty(document.getExchangeUnits(), "unitId", new Long(sessionInfo.getUnitId()))!=null &&
					   getOrgService().checkPopedom(sessionInfo.getUnitId(), "receiveDocument", sessionInfo)) {
						return RecordControlService.ACCESS_LEVEL_EDITABLE;
					}
				}
			}
			catch (ServiceException e) {
			
			}
			throw new PrivilegeException();
		}
		if(message.getReplyMessageId()>0) { //答复
			//允许发文单位的发送人编辑
			if(message.getDocument().getCreatorUnitId()==sessionInfo.getUnitId() &&
			   getOrgService().checkPopedom(sessionInfo.getUnitId(), "sendDocument", sessionInfo)) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
		}
		else { //反馈
			//允许发文单位的发送人查看
			if(message.getDocument().getCreatorUnitId()==sessionInfo.getUnitId() &&
			   getOrgService().checkPopedom(sessionInfo.getUnitId(), "sendDocument", sessionInfo)) {
				return RecordControlService.ACCESS_LEVEL_READONLY;
			}
			//允许接收单位的接收人查看
			if(message.getCreatorUnitId()==sessionInfo.getUnitId() &&
			   getOrgService().checkPopedom(sessionInfo.getUnitId(), "receiveDocument", sessionInfo)) {
				return RecordControlService.ACCESS_LEVEL_READONLY;
			}
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		com.yuanluesoft.j2oa.exchange.pojo.ExchangeMessage message = (com.yuanluesoft.j2oa.exchange.pojo.ExchangeMessage)record;
		//允许接收单位的接收人删除
		if(message.getCreatorUnitId()==sessionInfo.getUnitId() &&
		   getOrgService().checkPopedom(sessionInfo.getUnitId(), "receiveDocument", sessionInfo)) {
			return;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		com.yuanluesoft.j2oa.exchange.pojo.ExchangeMessage message = (com.yuanluesoft.j2oa.exchange.pojo.ExchangeMessage)record;
		if(message==null || //新记录
		   message.getReplyMessageId()>0 || //已经是答复
		   message.getDocument().getCreatorUnitId()!=sessionInfo.getUnitId()) { //不是发文单位
			form.getFormActions().removeFormAction("答复");
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public Record loadRecord(ActionForm form, Form formDefine, long id, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		ExchangeMessage messageForm = (ExchangeMessage)form;
		if(messageForm.getReplyMessageId()>0) { //答复
			ExchangeDocumentService exchangeDocumentService = (ExchangeDocumentService)getService("exchangeDocumentService");
			return exchangeDocumentService.getReplyMessage(messageForm.getReplyMessageId());
		}
		return super.loadRecord(form, formDefine, id, sessionInfo, request);
	}
}