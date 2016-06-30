package com.yuanluesoft.cms.messageboard.actions.message;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.messageboard.forms.Message;
import com.yuanluesoft.cms.messageboard.pojo.MessageBoard;
import com.yuanluesoft.cms.messageboard.pojo.MessageBoardFaq;
import com.yuanluesoft.cms.messageboard.service.MessageBoardService;
import com.yuanluesoft.cms.publicservice.actions.PublicServiceAction;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.Base64Decoder;
import com.yuanluesoft.jeaf.util.Base64Encoder;
import com.yuanluesoft.jeaf.util.ObjectSerializer;

/**
 * 
 * @author linchuan
 *
 */
public class MessageAction extends PublicServiceAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		Message messageForm = (Message)form;
		if(messageForm.isContinueSubmit()) { //继续提交
			record = (MessageBoard)ObjectSerializer.deserialize(new Base64Decoder().decode(messageForm.getSerializableRecord()));
			messageForm.setContinueSubmit(false);
		}
		else {
			//检查是否有匹配的常见问题
			MessageBoardService messageBoardService = (MessageBoardService)getService("messageBoardService");
			MessageBoardFaq faq = messageBoardService.findFaq(messageForm.getSubject());
			if(faq!=null) {
				messageForm.setSerializableRecord(new Base64Encoder().encode(ObjectSerializer.serialize(record)));
				messageForm.setContinueSubmit(true);
				messageForm.setFaqQuestion(faq.getQuestion());
				messageForm.setFaqAnswer(faq.getAnswer());
				return record;
			}
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.actions.PublicServiceAction#executeSubmitAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward executeSubmitAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = super.executeSubmitAction(mapping, form, request, response);
		Message messageForm = (Message)form;
		if(!messageForm.isContinueSubmit()) {
			return forward;
		}
		reloadForm(messageForm, null, OPEN_MODE_CREATE, getAcl("cms/messageboard", form, null, OPEN_MODE_CREATE, SessionService.ANONYMOUS_SESSION), SessionService.ANONYMOUS_SESSION, request, response);
		return mapping.findForward("faq");
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.action.BaseAction#validateForm(com.yuanluesoft.jeaf.form.ActionForm, boolean, javax.servlet.http.HttpServletRequest)
	 */
	public List validateForm(ActionForm formToValidate, boolean forceValidateCode, HttpServletRequest request) throws SystemUnregistException {
		Message messageForm = (Message)formToValidate;
		if(messageForm.isContinueSubmit()) { //继续提交
			return null; //不做校验 
		}
		return super.validateForm(formToValidate, forceValidateCode, request);
	}
}