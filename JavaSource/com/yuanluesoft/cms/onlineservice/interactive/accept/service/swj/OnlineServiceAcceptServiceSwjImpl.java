package com.yuanluesoft.cms.onlineservice.interactive.accept.service.swj;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.onlineservice.interactive.accept.service.spring.OnlineServiceAcceptServiceImpl;
import com.yuanluesoft.cms.onlineservice.interactive.complaint.pojo.OnlineServiceComplaint;
import com.yuanluesoft.cms.onlineservice.interactive.consult.pojo.OnlineServiceConsult;
import com.yuanluesoft.cms.onlineservice.interactive.pojo.OnlineServiceInteractive;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemTransactor;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService;
import com.yuanluesoft.cms.publicservice.pojo.PublicService;
import com.yuanluesoft.jeaf.base.model.Element;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.directorymanage.pojo.DirectoryPopedom;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.workflow.callback.WorkflowParticipantCallback;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;
import com.yuanluesoft.workflow.client.model.resource.ProgrammingParticipant;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry;

/**
 * 
 * @author zyh
 *
 */
public class OnlineServiceAcceptServiceSwjImpl extends OnlineServiceAcceptServiceImpl {

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
		String workflowId = getOnlineServiceItemService().getWorkflowId(onlineServiceInteractive.getItemId(), workflowType);
		if(workflowId==null) {
			throw new ServiceException("流程未定义，提交失败。");
		}
		
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
					participants = getOnlineServiceItemService().listServiceItemTransactors(onlineServiceInteractive.getItemId());
				}
				else if("manager".equals(programmingParticipantId)) { //管理员
					participants = getOnlineServiceItemService().listServiceItemManagers(onlineServiceInteractive.getItemId());
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
		return getWorkflowExploitService().createWorkflowInstance(workflowEntry.getWorkflowId(), activity.getId(), false, publicService, participantCallback, sessionInfo);
	}
	
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		// TODO 自动生成方法存根
		List acceptStatus = new ArrayList();
		acceptStatus.add(0, new Object[]{"未受理", "未受理"});
		acceptStatus.add(1, new Object[]{"同意受理", "同意受理"});
		acceptStatus.add(2, new Object[]{"不同意受理", "不同意受理"});
		acceptStatus.add(3, new Object[]{"办理完毕", "办理完毕"});
		return acceptStatus;
	}
	
	
}