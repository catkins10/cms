package com.yuanluesoft.bidding.project.actions.project.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.forms.admin.Project;
import com.yuanluesoft.bidding.project.pojo.BiddingProject;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectPlan;
import com.yuanluesoft.bidding.project.pojo.BiddingProjectTender;
import com.yuanluesoft.bidding.project.service.BiddingProjectService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 分配开标室并发布招标公告
 * @author linchuan
 *
 */
public class ArrangeBidopeningRoomAndPublicTender extends ProjectAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return executeRunAction(mapping, form, request, response, true, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateBusiness(com.yuanluesoft.jeaf.business.service.BusinessService, org.apache.struts.action.ActionForm, java.lang.String, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void validateBusiness(BusinessService validateService, ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException, SystemUnregistException {
		super.validateBusiness(validateService, form, openMode, record, sessionInfo, request);
		Project projectForm = (Project)form;
		validateBidopeningRoomSchedule(projectForm);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.workflowform.actions.WorkflowAction#beforeWorkitemCompleted(com.yuanluesoft.jeaf.form.workflowform.WorkflowForm, boolean, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	protected void beforeWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceWillComplete, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		Project projectForm = (Project)workflowForm;
		BiddingProject project = (BiddingProject)record;
		BiddingProjectService biddingProjectService = (BiddingProjectService)getService("biddingProjectService");
		//发布开标室安排
		biddingProjectService.publicProjectComponent(projectForm.getBidopeningRoomSchedule(), sessionInfo);
		//发布招标文件
		if(project.getTenders()!=null && !project.getTenders().isEmpty()) {
			BiddingProjectTender tender = (BiddingProjectTender)project.getTenders().iterator().next();
			if(tender.getPublicBeginTime()==null) { //招标公告还没有发布
				try {
					//拷贝招标文件到评标系统
					biddingProjectService.sendBiddingDocumentToEvaluateSystem(project);
				}
				catch(ServiceException e) {
					workflowForm.setErrors(ListUtils.generateList("传输到标书服务器时出错"));
					throw new ValidateException();
				}
				//发布招标公告
				biddingProjectService.publicProjectComponent(tender, sessionInfo);
				//发布时间安排
				if(project.getPlans()!=null && !project.getPlans().isEmpty()) {
					biddingProjectService.publicProjectComponent((BiddingProjectPlan)project.getPlans().iterator().next(), sessionInfo);
				}
			}
		}
		super.beforeWorkitemCompleted(workflowForm, workflowInstanceWillComplete, isReverse, record, openMode, sessionInfo, request);
	}
}