package com.yuanluesoft.job.apply.actions.apply.offer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.job.apply.actions.apply.ApplyAction;
import com.yuanluesoft.job.apply.forms.Offer;
import com.yuanluesoft.job.apply.pojo.JobApply;
import com.yuanluesoft.job.apply.pojo.JobApplyOffer;
import com.yuanluesoft.job.apply.service.JobApplyService;
import com.yuanluesoft.job.company.pojo.JobCompanyMailTemplate;
import com.yuanluesoft.job.company.service.JobParameterService;

/**
 * 
 * @author linchuan
 *
 */
public class OfferAction extends ApplyAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadComponentPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadComponentPrivilege(ActionForm form, HttpServletRequest request, Record record, Record component, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		char level = super.checkLoadComponentPrivilege(form, request, record, component, acl, sessionInfo);
		if(component!=null && !OPEN_MODE_CREATE_COMPONENT.equals(form.getAct())) {
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		return level;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initComponentForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initComponentForm(ActionForm form, Record mainRecord, String componentName, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.initComponentForm(form, mainRecord, componentName, sessionInfo, request);
		Offer offerForm = (Offer)form;
		JobParameterService jobParameterService = (JobParameterService)getService("jobParameterService");
		JobCompanyMailTemplate mailTemplate = jobParameterService.getMailTemplateByCompanyId(sessionInfo.getUnitId());
		if(mailTemplate==null || mailTemplate.getOfferMailTemplate()==null || mailTemplate.getOfferMailTemplate().isEmpty()) {
			mailTemplate = jobParameterService.getMailTemplateByCompanyId(0);
		}
		if(mailTemplate!=null) {
			String mailBody = mailTemplate.getOfferMailTemplate();
			mailBody = mailBody.replaceAll("<求职人姓名>", offerForm.getTalentName())
							   .replaceAll("<职位>", offerForm.getJobName())
							   .replaceAll("<企业名称>", offerForm.getCompany())
							   .replaceAll("<地址>", offerForm.getJob().getCompany().getAddress()==null ? "" : offerForm.getJob().getCompany().getAddress())
							   .replaceAll("<发送时间>", DateTimeUtils.formatDate(DateTimeUtils.date(), "yyyy-MM-dd"));
			offerForm.getOffer().setBody(mailBody);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponentRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.saveComponentRecord(form, mainRecord, component, componentName, foreignKeyProperty, sessionInfo, request);
		JobApply apply = (JobApply)mainRecord;
		JobApplyOffer offer = (JobApplyOffer)component;
		JobApplyService jobApplyService = (JobApplyService)getService("jobApplyService");
		jobApplyService.sendOfferMail(apply.getCompanyId(), apply.getTalent().getEmail(), offer.getBody());
	}
}