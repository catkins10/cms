package com.yuanluesoft.cms.inquiry.actions.inquirysubject.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.yuanluesoft.cms.base.actions.SiteApplicationConfigAction;
import com.yuanluesoft.cms.inquiry.forms.admin.InquirySubject;
import com.yuanluesoft.cms.inquiry.pojo.Inquiry;
import com.yuanluesoft.cms.inquiry.services.InquiryService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.TabList;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class InquirySubjectAction extends SiteApplicationConfigAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		InquirySubject inquirySubjectForm = (InquirySubject)form;
		inquirySubjectForm.setCreated(DateTimeUtils.now());
		inquirySubjectForm.setCreator(sessionInfo.getUserName());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InquirySubject inquirySubjectForm = (InquirySubject)form;
		com.yuanluesoft.cms.inquiry.pojo.InquirySubject inquirySubject = (com.yuanluesoft.cms.inquiry.pojo.InquirySubject)record;
		InquiryService inquiryService = (InquiryService)getService("inquiryService");
		//统计投票结果
		if(ListUtils.findObjectByProperty(inquirySubjectForm.getTabs(), "id", "result")!=null) {
			inquiryService.retrieveInquiryResults(inquirySubject);
		}
		//统计投票匹配排行
		if(ListUtils.findObjectByProperty(inquirySubjectForm.getTabs(), "id", "match")!=null) {
			inquiryService.retrieveInquiryMatchs(inquirySubject);
		}
		//统计参与情况
		if(ListUtils.findObjectByProperty(inquirySubjectForm.getTabs(), "id", "voter")!=null) {
			inquirySubjectForm.setVoterTotal(inquiryService.totalInquiryVoter(inquirySubjectForm.getId()));
		}
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		if(inquirySubjectForm.getIsQuestionnaire()=='0') { //不是问卷
			inquirySubjectForm.setInquiry((Inquiry)inquirySubjectForm.getInquiries().iterator().next());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		InquirySubject inquirySubjectForm = (InquirySubject)form;
		com.yuanluesoft.cms.inquiry.pojo.InquirySubject inquirySubject = (com.yuanluesoft.cms.inquiry.pojo.InquirySubject)record;
		inquirySubjectForm.setManager(accessLevel>=RecordControlService.ACCESS_LEVEL_EDITABLE);
		boolean editabled = accessLevel>=RecordControlService.ACCESS_LEVEL_EDITABLE && (inquirySubject==null || inquirySubject.getIsPublic()=='0');
		if(editabled) {
			inquirySubjectForm.getFormActions().removeFormAction("撤销发布");
		}
		else {
			inquirySubjectForm.getFormActions().removeFormAction("发布");
			inquirySubjectForm.setSubForm(inquirySubjectForm.getSubForm().replace("Edit", "Read"));
		}
		if(inquirySubject!=null && inquirySubject.getEndTime()!=null && !inquirySubject.getEndTime().before(DateTimeUtils.now())) { //投票未结束
			inquirySubjectForm.getFormActions().removeFormAction("结果反馈");
		}
		//设置TAB页签
		form.getTabs().addTab(-1, "basic", "基本信息", form.getSubForm(), true);
		if((inquirySubject==null ? inquirySubjectForm.getIsQuestionnaire() : inquirySubject.getIsQuestionnaire())=='1') { //问卷调查
			form.getTabs().addTab(-1, "inquiries", "调查", "inquiries" + (editabled ? "Edit" : "Read") + ".jsp", false);
			form.setFormTitle("调查问卷");
		}
		else {
			form.getTabs().addTab(-1, "options", "调查选项", "inquiryOptions" + (editabled ? "Edit" : "Read") + ".jsp", false);
			form.setFormTitle("调查");
		}
		if(!OPEN_MODE_CREATE.equals(openMode)) {
			if(inquirySubject.getPublishResult()=='2' || inquirySubject.getEndTime()==null || inquirySubject.getEndTime().before(DateTimeUtils.now())) { //总是公开、或者投票已经结束
				form.getTabs().addTab(-1, "result", "投票结果", "inquiryResult.jsp", false);
				if(inquirySubject.getIsAnonymous()!='1') { //实名投票
					form.getTabs().addTab(-1, "match", "投票匹配排行", "inquiryMatch.jsp", false);
				}
				inquirySubjectForm.setResultTabList(new TabList());
				inquirySubjectForm.getResultTabList().addTab(-1, "resultAsTable", "列表", null, true);
				inquirySubjectForm.getResultTabList().addTab(-1, "resultAsChart", "图表", null, false);
			}
			if(inquirySubject.getIsAnonymous()!='1') { //实名投票
				form.getTabs().addTab(2, "voter", "参与情况", "inquiryVoter.jsp", false);
			}
			if(inquirySubject.getFeedbacks()!=null && !inquirySubject.getFeedbacks().isEmpty()) { //有结果反馈
				form.getTabs().addTab(-1, "feedbackTab", "结果反馈", "feedback.jsp", false);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.cms.inquiry.pojo.InquirySubject inquirySubject = (com.yuanluesoft.cms.inquiry.pojo.InquirySubject)record;
		InquirySubject inquirySubjectForm = (InquirySubject)form;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			if(inquirySubject.getCreated()==null) {
				inquirySubject.setCreated(DateTimeUtils.now());
			}
			inquirySubject.setCreator(sessionInfo.getUserName());
			inquirySubject.setCreatorId(sessionInfo.getUserId());
		}
		record = super.saveRecord(form, record, openMode, request, response, sessionInfo);
		if(inquirySubjectForm.getIsQuestionnaire()=='0') { //不是问卷
			InquiryService inquiryService = (InquiryService)getService("inquiryService");
			//如果是单选,则最小最大投票数都改为1
			if(inquirySubjectForm.getInquiry().getIsMultiSelect()=='0') {
				inquirySubjectForm.getInquiry().setMinVote(1);
				inquirySubjectForm.getInquiry().setMaxVote(1);
			}
			if(OPEN_MODE_CREATE.equals(openMode)) {
				inquirySubjectForm.getInquiry().setId(form.getId()); //将调查ID设置为主题ID
				inquirySubjectForm.getInquiry().setSubjectId(form.getId()); //主题ID
				inquiryService.save(inquirySubjectForm.getInquiry());
			}
			else {
				inquiryService.update(inquirySubjectForm.getInquiry());
			}
		}
		return record;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateForm(org.apache.struts.action.ActionForm, com.yuanluesoft.jeaf.dao.Record, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
    public void validateForm(org.apache.struts.action.ActionForm formToValidate, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, SystemUnregistException {
    	List errors = validateForm((ActionForm)formToValidate, forceValidateCode, request);
    	if(errors == null) {
    		errors = new ArrayList();
    	}
		InquirySubject inquirySubjectForm = (InquirySubject)formToValidate;
    	if(inquirySubjectForm.getIsQuestionnaire()=='0') { //不是问卷
			if(inquirySubjectForm.getInquiry().getIsMultiSelect()=='1') {
				if(inquirySubjectForm.getInquiry().getMinVote()==0) {
					errors.add("最小投票数不能为零");
				}
				if(inquirySubjectForm.getInquiry().getMaxVote()==0) {
					errors.add("最大投票数不能为零");
				}
				if(inquirySubjectForm.getInquiry().getMinVote() > inquirySubjectForm.getInquiry().getMaxVote()) {
					errors.add("最小投票数不能大于最大投票数");
				}
			}
    	}
		if(errors!=null && !errors.isEmpty()) {
			inquirySubjectForm.setErrors(errors);
			throw new ValidateException();
		}
    }
}