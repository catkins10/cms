package com.yuanluesoft.cms.messageboard.actions.faq.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.base.actions.SiteApplicationConfigAction;
import com.yuanluesoft.cms.messageboard.forms.admin.Faq;
import com.yuanluesoft.cms.messageboard.pojo.MessageBoardFaq;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class FaqAction extends SiteApplicationConfigAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		MessageBoardFaq faq = (MessageBoardFaq)record;
		Faq faqForm = (Faq)form;
		faqForm.setKeywords(faq.getFirstKeyword() + (faq.getOtherKeywords()==null || faq.getOtherKeywords().isEmpty() ? "" : " " + faq.getOtherKeywords()));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		MessageBoardFaq faq = (MessageBoardFaq)record;
		Faq faqForm = (Faq)form;
		int index = faqForm.getKeywords().indexOf(' ');
		if(index==-1) {
			faq.setFirstKeyword(faqForm.getKeywords());
			faq.setOtherKeywords(null);
		}
		else {
			faq.setFirstKeyword(faqForm.getKeywords().substring(0, index));
			faq.setOtherKeywords(faqForm.getKeywords().substring(index + 1));
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}