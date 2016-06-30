/*
 * Created on 2005-2-11
 *
 */
package com.yuanluesoft.jeaf.workflow.form;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.opinionmanage.model.OpinionPackage;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowExit;

/**
 * 
 * @author linchuan
 *
 */
public class WorkflowForm extends ActionForm {
    private String workflowInstanceId; //工作流实例ID
    
	private String workflowId; //URL参数:新建流程的流程ID
	private String activityId; //URL参数:新建流程的环节ID
	private String workflowAction; //URL参数:流程操作
	private String workItemId;  //URL参数:工作项ID
	private boolean workflowTest; //URL参数:流程测试
	private long testUserId;  //URL参数:流程测试时的用户ID
	
	private String participant; //办理人
	private long participantId; //办理人ID
	private boolean isAgent; //是否代理人

	private String currentAction; //当前struts操作名称
	private String testUserName; //流程测试时的用户名
	
	private List undoneActions; //未执行的操作列表
	
	private WorkflowExit workflowExit; //流程出口
	private String workflowExitText; //转换成文本形式的流程出口
	
	private List reverseActivityInstances; //允许回退的环节实例列表
	private String selectedReverseInstanceId; //用户选择的流程回退实例ID
	private List workflowUndos; //流程取回列表
	private String selectedWorkflowUndoId; //用户选择的流程取回实例ID
	
	private String workflowParticipantNames; //被转办/或新增的办理人
	private String workflowParticipantIds; //被转办/或新增的办理人ID
	
	private String activityName; //当前环节名称
	private String workflowName; //当前流程名称
	
	private String workflowApprovalDialogTitle; //审批对话框标题,默认：审批
	private String workflowApprovalOptions; //审批选项列表,默认：同意/不同意
	private String workflowApprovalResult; //审批结果
	private OpinionPackage opinionPackage = new OpinionPackage(); //办理意见
	
	private boolean isPrompted; //是否已经提示过
	private String contextPath; //web应用名称
	
	private String batchIds; //批量处理的记录ID列表
	
	private Set workItems; //工作项列表
	private Set opinions; //意见列表
	
	
    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        contextPath = request.getContextPath();
    }
	/**
	 * @return Returns the activityId.
	 */
	public String getActivityId() {
		return activityId;
	}
	/**
	 * @param activityId The activityId to set.
	 */
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	/**
	 * @return Returns the virtualUserId.
	 */
	public long getTestUserId() {
		return testUserId;
	}
	/**
	 * @param virtualUserId The virtualUserId to set.
	 */
	public void setTestUserId(long virtualUserId) {
		this.testUserId = virtualUserId;
	}
	/**
	 * @return Returns the workflowId.
	 */
	public String getWorkflowId() {
		return workflowId;
	}
	/**
	 * @param workflowId The workflowId to set.
	 */
	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}
	/**
	 * @return Returns the workflowTest.
	 */
	public boolean isWorkflowTest() {
		return workflowTest;
	}
	/**
	 * @param workflowTest The workflowTest to set.
	 */
	public void setWorkflowTest(boolean workflowTest) {
		this.workflowTest = workflowTest;
	}
	
	/**
	 * @return Returns the workflowAction.
	 */
	public String getWorkflowAction() {
		return workflowAction;
	}
	/**
	 * @param workflowAction The workflowAction to set.
	 */
	public void setWorkflowAction(String workflowAction) {
		this.workflowAction = workflowAction;
	}
	/**
	 * @return Returns the workItemId.
	 */
	public String getWorkItemId() {
		return workItemId;
	}
	/**
	 * @param workItemId The workItemId to set.
	 */
	public void setWorkItemId(String workItemId) {
		this.workItemId = workItemId;
	}
	/**
	 * @return Returns the activityName.
	 */
	public String getActivityName() {
		return activityName;
	}
	/**
	 * @param activityName The activityName to set.
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	/**
	 * @return Returns the workflowName.
	 */
	public String getWorkflowName() {
		return workflowName;
	}
	/**
	 * @param workflowName The workflowName to set.
	 */
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}
	/**
	 * @return Returns the reverseActivityInstances.
	 */
	public List getReverseActivityInstances() {
		return reverseActivityInstances;
	}
	/**
	 * @param reverseActivityInstances The reverseActivityInstances to set.
	 */
	public void setReverseActivityInstances(List reverseActivityInstances) {
		this.reverseActivityInstances = reverseActivityInstances;
	}
	/**
	 * @return Returns the workflowUndos.
	 */
	public List getWorkflowUndos() {
		return workflowUndos;
	}
	/**
	 * @param workflowUndos The workflowUndos to set.
	 */
	public void setWorkflowUndos(List workflowUndos) {
		this.workflowUndos = workflowUndos;
	}
	/**
	 * @return Returns the virtualUserName.
	 */
	public String getTestUserName() {
		return testUserName;
	}
	/**
	 * @param virtualUserName The virtualUserName to set.
	 */
	public void setTestUserName(String virtualUserName) {
		this.testUserName = virtualUserName;
	}
	/**
	 * @return Returns the undoneActions.
	 */
	public List getUndoneActions() {
		return undoneActions;
	}
	/**
	 * @param undoneActions The undoneActions to set.
	 */
	public void setUndoneActions(List undoneActions) {
		this.undoneActions = undoneActions;
	}
	/**
	 * @return Returns the currentAction.
	 */
	public String getCurrentAction() {
		return currentAction;
	}
	/**
	 * @param currentAction The currentAction to set.
	 */
	public void setCurrentAction(String currentAction) {
		this.currentAction = currentAction;
	}
	/**
	 * @return Returns the workflowExit.
	 */
	public WorkflowExit getWorkflowExit() {
		return workflowExit;
	}
	/**
	 * @param workflowExit The workflowExit to set.
	 */
	public void setWorkflowExit(WorkflowExit workflowExit) {
		this.workflowExit = workflowExit;
	}
    /**
     * @return Returns the workflowInstanceId.
     */
    public String getWorkflowInstanceId() {
        return workflowInstanceId;
    }
    /**
     * @param workflowInstanceId The workflowInstanceId to set.
     */
    public void setWorkflowInstanceId(String workflowInstanceId) {
        this.workflowInstanceId = workflowInstanceId;
    }
    /**
     * @return Returns the opinion.
     */
    public OpinionPackage getOpinionPackage() {
        return opinionPackage;
    }
    /**
     * @param opinion The opinion to set.
     */
    public void setOpinionPackage(OpinionPackage opinion) {
        this.opinionPackage = opinion;
    }
    /**
     * @return Returns the isPrompted.
     */
    public boolean isPrompted() {
        return isPrompted;
    }
    /**
     * @param isPrompted The isPrompted to set.
     */
    public void setPrompted(boolean isPrompted) {
        this.isPrompted = isPrompted;
    }
    /**
     * @return Returns the isAgent.
     */
    public boolean isAgent() {
        return isAgent;
    }
    /**
     * @param isAgent The isAgent to set.
     */
    public void setAgent(boolean isAgent) {
        this.isAgent = isAgent;
    }
    /**
     * @return Returns the participant.
     */
    public String getParticipant() {
        return participant;
    }
    /**
     * @param participant The participant to set.
     */
    public void setParticipant(String participant) {
        this.participant = participant;
    }
    /**
     * @return Returns the participantId.
     */
    public long getParticipantId() {
        return participantId;
    }
    /**
     * @param participantId The participantId to set.
     */
    public void setParticipantId(long participantId) {
        this.participantId = participantId;
    }
    /**
     * @return Returns the contextPath.
     */
    public String getContextPath() {
        return contextPath;
    }
    /**
     * @param contextPath The contextPath to set.
     */
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }
    /**
     * @return Returns the workflowExitText.
     */
    public String getWorkflowExitText() {
        return workflowExitText;
    }
    /**
     * @param workflowExitText The workflowExitText to set.
     */
    public void setWorkflowExitText(String workflowExitText) {
        this.workflowExitText = workflowExitText;
    }
    /**
     * @return Returns the selectedReverseInstanceId.
     */
    public String getSelectedReverseInstanceId() {
        return selectedReverseInstanceId;
    }
    /**
     * @param selectedReverseInstanceId The selectedReverseInstanceId to set.
     */
    public void setSelectedReverseInstanceId(String selectedReverseInstanceId) {
        this.selectedReverseInstanceId = selectedReverseInstanceId;
    }
    /**
     * @return Returns the selectedWorkflowUndoId.
     */
    public String getSelectedWorkflowUndoId() {
        return selectedWorkflowUndoId;
    }
    /**
     * @param selectedWorkflowUndoId The selectedWorkflowUndoId to set.
     */
    public void setSelectedWorkflowUndoId(String selectedWorkflowUndoId) {
        this.selectedWorkflowUndoId = selectedWorkflowUndoId;
    }

	/**
	 * @return the opinions
	 */
	public Set getOpinions() {
		return opinions;
	}

	/**
	 * @param opinions the opinions to set
	 */
	public void setOpinions(Set opinions) {
		this.opinions = opinions;
	}

	/**
	 * @return the workItems
	 */
	public Set getWorkItems() {
		return workItems;
	}

	/**
	 * @param workItems the workItems to set
	 */
	public void setWorkItems(Set workItems) {
		this.workItems = workItems;
	}

	/**
	 * @return the batchIds
	 */
	public String getBatchIds() {
		return batchIds;
	}

	/**
	 * @param batchIds the batchIds to set
	 */
	public void setBatchIds(String batchIds) {
		this.batchIds = batchIds;
	}
	/**
	 * @return the workflowParticipantIds
	 */
	public String getWorkflowParticipantIds() {
		return workflowParticipantIds;
	}

	/**
	 * @param workflowParticipantIds the workflowParticipantIds to set
	 */
	public void setWorkflowParticipantIds(String workflowParticipantIds) {
		this.workflowParticipantIds = workflowParticipantIds;
	}

	/**
	 * @return the workflowParticipantNames
	 */
	public String getWorkflowParticipantNames() {
		return workflowParticipantNames;
	}

	/**
	 * @param workflowParticipantNames the workflowParticipantNames to set
	 */
	public void setWorkflowParticipantNames(String workflowParticipantNames) {
		this.workflowParticipantNames = workflowParticipantNames;
	}

	/**
	 * @return the workflowApprovalDialogTitle
	 */
	public String getWorkflowApprovalDialogTitle() {
		return workflowApprovalDialogTitle;
	}

	/**
	 * @param workflowApprovalDialogTitle the workflowApprovalDialogTitle to set
	 */
	public void setWorkflowApprovalDialogTitle(String workflowApprovalDialogTitle) {
		this.workflowApprovalDialogTitle = workflowApprovalDialogTitle;
	}

	/**
	 * @return the workflowApprovalOptions
	 */
	public String getWorkflowApprovalOptions() {
		return workflowApprovalOptions;
	}

	/**
	 * @param workflowApprovalOptions the workflowApprovalOptions to set
	 */
	public void setWorkflowApprovalOptions(String workflowApprovalOptions) {
		this.workflowApprovalOptions = workflowApprovalOptions;
	}

	/**
	 * @return the workflowApprovalResult
	 */
	public String getWorkflowApprovalResult() {
		return workflowApprovalResult;
	}

	/**
	 * @param workflowApprovalResult the workflowApprovalResult to set
	 */
	public void setWorkflowApprovalResult(String workflowApprovalResult) {
		this.workflowApprovalResult = workflowApprovalResult;
	}
}