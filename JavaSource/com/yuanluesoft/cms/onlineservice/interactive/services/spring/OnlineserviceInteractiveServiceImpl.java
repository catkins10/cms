package com.yuanluesoft.cms.onlineservice.interactive.services.spring;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.cms.onlineservice.interactive.complaint.pojo.OnlineServiceComplaint;
import com.yuanluesoft.cms.onlineservice.interactive.consult.pojo.OnlineServiceConsult;
import com.yuanluesoft.cms.onlineservice.interactive.pojo.OnlineServiceInteractive;
import com.yuanluesoft.cms.onlineservice.interactive.services.OnlineserviceInteractiveService;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemTransactor;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService;
import com.yuanluesoft.cms.publicservice.pojo.PublicService;
import com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.directorymanage.pojo.DirectoryPopedom;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.workflow.callback.WorkflowParticipantCallback;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;
import com.yuanluesoft.workflow.client.model.resource.ProgrammingParticipant;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowMessage;

/**
 * 
 * @author linchuan
 *
 */
public class OnlineserviceInteractiveServiceImpl extends PublicServiceImpl implements OnlineserviceInteractiveService {
	private OnlineServiceItemService onlineServiceItemService; //办理事项服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#createWorkflowInstance(com.yuanluesoft.cms.publicservice.pojo.PublicService)
	 */
	protected String createWorkflowInstance(PublicService publicService) throws ServiceException {
		final OnlineServiceInteractive onlineServiceInteractive = (OnlineServiceInteractive)publicService;
		String workflowType;
		if(onlineServiceInteractive instanceof OnlineServiceComplaint) { //投诉
			workflowType = OnlineServiceItemService.WORKFLOW_TYPE_COMPLAINT;
		}
		else if(onlineServiceInteractive instanceof OnlineServiceConsult) { //投诉
			workflowType = OnlineServiceItemService.WORKFLOW_TYPE_CONSULT;
		}
		else { //受理
			workflowType = OnlineServiceItemService.WORKFLOW_TYPE_ACCEPT;
		}
		//按办理事项获取流程配置
		String workflowId = onlineServiceItemService.getWorkflowId(onlineServiceInteractive.getItemId(), workflowType);
		if(workflowId==null) {
			throw new ServiceException("流程未定义，提交失败。");
		}
		
		//获取业务对象定义
		BusinessObject businessObject = getBusinessDefineService().getBusinessObject(publicService.getClass());
		//获取流程入口
		SessionInfo sessionInfo;
		try {
			sessionInfo = getSessionService().getSessionInfo(onlineServiceInteractive.getLoginName()==null ? SessionService.ANONYMOUS : onlineServiceInteractive.getLoginName());
		} 
		catch (SessionException e) {
			throw new ServiceException(e.getMessage());
		}
		if(publicService.getCreator()!=null && !publicService.getCreator().equals("")) {
			sessionInfo.setUserName(publicService.getCreator());
		}
		WorkflowEntry workflowEntry = getWorkflowExploitService().getWorkflowEntry(workflowId, null, publicService, sessionInfo);
		if(workflowEntry==null) {
			throw new ServiceException("流程未定义，提交失败。");
		}
		
		//创建流程实例并自动发送
		String message = (publicService.getSubject()!=null ? publicService.getSubject() : (publicService.getContent()==null ? businessObject.getTitle() : StringUtils.slice(StringUtils.filterHtmlElement(publicService.getContent(), false), 30, "...")));
		WorkflowMessage workflowMessage = new WorkflowMessage(businessObject.getTitle(), message, null);
		Element activity = (Element)workflowEntry.getActivityEntries().get(0);
		
		WorkflowParticipantCallback participantCallback = new WorkflowParticipantCallback() {

			public List resetParticipants(List participants, boolean anyoneParticipate, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
				if(anyoneParticipate) { //任何人都可办理,把当前事项的经办人作为流程办理人
					participants = new ArrayList();
					participants.add(new ProgrammingParticipant("transactor", "网上办事经办"));
				}
				return participants;
			}
			
			public List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
				List participants = null;
				if("transactor".equals(programmingParticipantId)) { //经办
					participants = onlineServiceItemService.listServiceItemTransactors(onlineServiceInteractive.getItemId());
				}
				else if("manager".equals(programmingParticipantId)) { //管理员
					participants = onlineServiceItemService.listServiceItemManagers(onlineServiceInteractive.getItemId());
				}
				if(participants==null || participants.isEmpty()) {
					return null;
				}
				//转换为办理人
				for(int i=0; i<participants.size(); i++) {
					Object participant = participants.get(i);
					if(participant instanceof OnlineServiceItemTransactor) {
						OnlineServiceItemTransactor transactor = (OnlineServiceItemTransactor)participant;
						participants.set(i, new Element("" + transactor.getUserId(), transactor.getUserName()));
					}
					else {
						DirectoryPopedom popedom  = (DirectoryPopedom)participant;
						participants.set(i, new Element("" + popedom.getUserId(), popedom.getUserName()));
					}
				}
				return participants;
			}

			public boolean isMemberOfProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
				return false;
			}
		};
		return getWorkflowExploitService().createWorkflowInstanceAndSend(workflowEntry.getWorkflowId(), activity.getId(), publicService, workflowMessage, participantCallback, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.interactive.services.OnlineserviceInteractiveService#listRecentCompaints(long, int)
	 */
	public List listRecentCompaints(long itemId, int limit) throws ServiceException {
		String hql = "from OnlineServiceComplaint OnlineServiceComplaint" +
					 " where OnlineServiceComplaint.itemId=" + itemId +
					 " order by OnlineServiceComplaint.created DESC";
		return getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("lazyBody,workItems,opinions", ","), 0, limit);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.interactive.services.OnlineserviceInteractiveService#listRecentConsults(long, int)
	 */
	public List listRecentConsults(long itemId, int limit) throws ServiceException {
		String hql = "from OnlineServiceConsult OnlineServiceConsult" +
					 " where OnlineServiceConsult.itemId=" + itemId +
					 " order by OnlineServiceConsult.created DESC";
		return getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("lazyBody,workItems,opinions", ","), 0, limit);
	}

	/**
	 * @return the onlineServiceItemService
	 */
	public OnlineServiceItemService getOnlineServiceItemService() {
		return onlineServiceItemService;
	}

	/**
	 * @param onlineServiceItemService the onlineServiceItemService to set
	 */
	public void setOnlineServiceItemService(
			OnlineServiceItemService onlineServiceItemService) {
		this.onlineServiceItemService = onlineServiceItemService;
	}
}