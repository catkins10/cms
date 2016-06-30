/*
 * Created on 2005-2-11
 *
 */
package com.yuanluesoft.jeaf.workflow.actions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.form.model.Tab;
import com.yuanluesoft.jeaf.form.model.TabList;
import com.yuanluesoft.jeaf.formula.service.FormulaService;
import com.yuanluesoft.jeaf.lock.service.LockException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.opinionmanage.model.OpinionPackage;
import com.yuanluesoft.jeaf.opinionmanage.pojo.Opinion;
import com.yuanluesoft.jeaf.opinionmanage.service.OpinionService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.JsonUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.callback.WorkflowHideConditionCheckCallback;
import com.yuanluesoft.jeaf.workflow.callback.WorkflowParticipantCallback;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.jeaf.workflow.model.WorkflowEntry;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.workflow.client.model.instance.ActivityInstance;
import com.yuanluesoft.workflow.client.model.instance.ProcessInstance;
import com.yuanluesoft.workflow.client.model.instance.WorkItem;
import com.yuanluesoft.workflow.client.model.resource.Action;
import com.yuanluesoft.workflow.client.model.runtime.ActivityExit;
import com.yuanluesoft.workflow.client.model.runtime.BaseExit;
import com.yuanluesoft.workflow.client.model.runtime.CompleteExit;
import com.yuanluesoft.workflow.client.model.runtime.EndExit;
import com.yuanluesoft.workflow.client.model.runtime.ProcedureExit;
import com.yuanluesoft.workflow.client.model.runtime.ReverseExit;
import com.yuanluesoft.workflow.client.model.runtime.SplitExit;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowExit;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowInterface;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowMessage;
import com.yuanluesoft.workflow.client.model.runtime.WorkflowUndo;
import com.yuanluesoft.workflow.client.model.wapi.ProcessInstanceState;

/**
 *
 * @author linchuan
 *
 */
public abstract class WorkflowAction extends FormAction {
	//流程操作:发送相关
	protected final String WORKFLOW_ACTION_SEND = "send"; //发送
	protected final String WORKFLOW_ACTION_GET_WORKFLOW_EXIT = "getWorkflowExit"; //发送:获取流程出口
	protected final String WORKFLOW_ACTION_AFTER_SELECT_EXIT = "afterSelectExit"; //发送:选择完流程出口
	protected final String WORKFLOW_ACTION_SELECT_PARTICIPANT = "selectParticipant"; //显示选择办理人
	protected final String WORKFLOW_ACTION_SELECT_EXIT = "selectExit"; //选择流程出口
	protected final String WORKFLOW_ACTION_CONFIRM_COMPLETE = "confirmComplete"; //显示确认办理完毕
	protected final String WORKFLOW_ACTION_NO_EXIT = "promptNoExit"; //提示没有流程出口
	protected final String WORKFLOW_ACTION_DOSEND = "doSend"; //发送:执行发送
	//流程操作:批量处理相关
	protected final String WORKFLOW_ACTION_BATCH_SEND = "batchSend"; //批量发送
	//流程操作:审核相关
	protected final String WORKFLOW_ACTION_APPROVAL = "approval"; //审核
	protected final String WORKFLOW_ACTION_DO_APPROVAL = "doApproval"; //执行审核
	protected final String WORKFLOW_ACTION_COMPLETE_APPROVAL = "completeApproval"; //完成审核
	//流程操作:回退相关
	protected final String WORKFLOW_ACTION_REVERSE = "reverse"; //回退
	protected final String WORKFLOW_ACTION_SELECT_REVERSE_INSTANCE = "selectReverseInstance"; //选择回退环节
	protected final String WORKFLOW_ACTION_DOREVERSE = "doReverse"; //执行回退操作
	//流程操作:取回相关
	protected final String WORKFLOW_ACTION_UNDO = "undo"; //取回
	protected final String WORKFLOW_ACTION_SELECT_UNDO = "selectWorkflowUndo"; //选择取回环节
	protected final String WORKFLOW_ACTION_DOUNDO = "doUndo"; //执行取回操作
	//流程操作:办理人相关
	protected final String WORKFLOW_ACTION_TRANSMIT = "transmit"; //转办
	protected final String WORKFLOW_ACTION_ADD_PARTICIPANTS = "addParticipants"; //增加办理人

	//Action选项
	protected boolean alwaysAutoSend = false; //是否总是自动发送
	
	/**
	 * 获取流程操作名称
	 * @param workflowForm
	 * @return
	 */
	public abstract String getWorkflowActionName(WorkflowForm workflowForm);
	
	/**
	 * 获取工作流利用服务
	 * @return
	 */
	protected WorkflowExploitService getWorkflowExploitService() throws SystemUnregistException {
		return (WorkflowExploitService)getService("workflowExploitService");
	}
	
	/**
	 * 获取化流程入口
	 * @param workflowForm
	 * @param request
	 * @param record
	 * @param openMode
	 * @param forCreateWorkflowInstance
	 * @param participantCallback
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	protected WorkflowEntry getWorkflowEntry(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, boolean forCreateWorkflowInstance, WorkflowActionParticipantCallback participantCallback, SessionInfo sessionInfo) throws Exception {
		if(workflowForm.getWorkflowId()==null || workflowForm.getWorkflowId().equals("")) {
			return null;
		}
		return new WorkflowEntry(workflowForm.getWorkflowId(), workflowForm.getActivityId());
	}
	
	/**
	 * 获取流程界面
	 * @param workflowForm
	 * @param record
	 * @param openMode
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	protected WorkflowInterface getWorkflowInterface(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		WorkflowInterface workflowInterface = (WorkflowInterface)request.getAttribute("workflowInterface");
		if(workflowInterface!=null) {
			return workflowInterface;
		}
		WorkflowExploitService workflowExploitService = getWorkflowExploitService();
		if(openMode.equals(OPEN_MODE_CREATE)) { //新记录
			WorkflowActionParticipantCallback participantCallback = new WorkflowActionParticipantCallback(workflowForm, request, this);
			WorkflowEntry workflowEntry = getWorkflowEntry(workflowForm, request, record, openMode, false, participantCallback, sessionInfo);
			if(workflowEntry==null) { //入口为空
				return null;
			}
			workflowForm.setWorkflowId(workflowEntry.getWorkflowDefinitionId());
			workflowForm.setActivityId(workflowEntry.getActivityDefinitionId());
			workflowInterface = workflowExploitService.previewWorkflowInterface(workflowEntry.getWorkflowDefinitionId(), workflowEntry.getActivityDefinitionId(), workflowForm.isWorkflowTest(), getWorkflowSessionInfo(workflowForm, sessionInfo));
		}
		else {
			if(record==null) {
				return null;
			}
		    String workflowInstanceId = ((WorkflowData)record).getWorkflowInstanceId();
		    if(workflowInstanceId==null || workflowInstanceId.isEmpty()) {
		    	return null;
		    }
		    try {
		    	workflowInterface = workflowExploitService.getWorkflowInterface(workflowInstanceId, workflowForm.getWorkItemId(), getWorkflowSessionInfo(workflowForm, sessionInfo));
		    }
		    catch(Exception e) {
		    	return null;
		    }
		    if(!workflowInterface.isWorkflowTest() && workflowForm.isWorkflowTest()) { //非测试流程实例,但用户想以测试用户访问本实例
		    	throw new Exception();
	        }
		}
		request.setAttribute("workflowInterface", workflowInterface);
		return workflowInterface;
	}

	/**
	 * 检查流程测试权限
	 * @param workflowForm
	 * @param request
	 * @param record
	 * @param openMode
	 * @param acl
	 * @param sessionInfo
	 * @throws PrivilegeException
	 * @throws SystemUnregistException
	 */
	public void checkWorkflowTestPrivilege(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(acl.contains("application_manager")) {
			return;
		}
		throw new PrivilegeException();
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#getLockPersonName(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public String getLockPersonName(ActionForm form, Record record, HttpServletRequest request, String openMode, SessionInfo sessionInfo) throws LockException, SystemUnregistException {
		WorkflowData workflowData = (WorkflowData)record;
		if(workflowData!=null && workflowData.getWorkflowInstanceId()!=null && !workflowData.getWorkflowInstanceId().isEmpty()) {
			try {
				String lockPersonName = getWorkflowExploitService().getWorkflowInstanceLockPersonName(workflowData.getWorkflowInstanceId(), getWorkflowSessionInfo(form, sessionInfo));
				if(lockPersonName!=null && !lockPersonName.isEmpty()) {
					return lockPersonName;
				}
			}
			catch(Exception e) {
				throw new LockException();
			}
		}
		return super.getLockPersonName(form, record, request, openMode, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#isLockByPerson(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, javax.servlet.http.HttpServletRequest, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public boolean isLockByMe(ActionForm form, Record record, HttpServletRequest request, String openMode, SessionInfo sessionInfo) throws LockException, SystemUnregistException {
		WorkflowData workflowData = (WorkflowData)record;
		if(workflowData!=null && workflowData.getWorkflowInstanceId()!=null && !workflowData.getWorkflowInstanceId().isEmpty()) {
			try {
				if(getWorkflowExploitService().isLockWorkflowInstance(workflowData.getWorkflowInstanceId(), getWorkflowSessionInfo(form, sessionInfo))) {
					return true;
				}
			}
			catch (Exception e) {
				
			}
		}
		return super.isLockByMe(form, record, request, openMode, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#lock(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, javax.servlet.http.HttpServletRequest, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void lock(ActionForm form, Record record, HttpServletRequest request, String openMode, SessionInfo sessionInfo) throws LockException, SystemUnregistException {
		super.lock(form, record, request, openMode, sessionInfo);
		WorkflowData workflowData = (WorkflowData)record;
		if(workflowData!=null && workflowData.getWorkflowInstanceId()!=null && !workflowData.getWorkflowInstanceId().isEmpty()) {
			try {
				getWorkflowExploitService().lockWorkflowInstance(workflowData.getWorkflowInstanceId(), getWorkflowSessionInfo(form, sessionInfo));
			}
			catch(Exception e) {
				
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#unlock(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void unlock(ActionForm form, HttpServletRequest request, String openMode, SessionInfo sessionInfo) throws LockException, SystemUnregistException {
		WorkflowForm workflowForm = (WorkflowForm)form;
		if(workflowForm.getWorkflowInstanceId()!=null && !workflowForm.getWorkflowInstanceId().isEmpty()) {
			try {
				getWorkflowExploitService().unlockWorkflowInstance(workflowForm.getWorkflowInstanceId(), getWorkflowSessionInfo(form, sessionInfo));
			}
			catch (Exception e) {
				
			}
		}
		super.unlock(form, request, openMode, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#generateUnlockUrl(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public String generateUnlockUrl(ActionForm form, Record record, HttpServletRequest request, SessionInfo sessionInfo) {
		WorkflowData workflowData = (WorkflowData)record;
		WorkflowForm workflowForm = (WorkflowForm)form;
		if((workflowData.getWorkflowInstanceId()!=null && !workflowData.getWorkflowInstanceId().isEmpty())) {
			return request.getContextPath() + "/jeaf/lock/unlockWorkflowInstance.shtml" +
				   "?id=" + workflowData.getId() +
				   "&workflowInstanceId=" + workflowData.getWorkflowInstanceId() +
				   "&personId=" + (workflowForm.isWorkflowTest() && workflowForm.getTestUserId()!=0 ? workflowForm.getTestUserId() : sessionInfo.getUserId());
		}
		return super.generateUnlockUrl(form, record, request, sessionInfo);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		WorkflowForm workflowForm = (WorkflowForm)form;
        try {
        	WorkflowInterface workflowInterface = getWorkflowInterface(workflowForm, request, record, openMode, sessionInfo);
        	if(workflowInterface==null) { //没有工作流接口
        		return getRecordControlService().getAccessLevel(record.getId(), record.getClass().getName(), sessionInfo); //检查权限控制表
        	}
    		if(workflowInterface.isWorkflowTest()) { //工作流测试
    			workflowForm.setWorkflowTest(true);
    			checkWorkflowTestPrivilege(workflowForm, request, record, openMode, acl, sessionInfo);
            }
        	return workflowInterface.getWorkItemId()!=null || workflowInterface.isUndoEnable() ? RecordControlService.ACCESS_LEVEL_EDITABLE : RecordControlService.ACCESS_LEVEL_READONLY;
        }
        catch(Exception e) {
        	Logger.exception(e);
        	throw new PrivilegeException(e);
        }
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		if(form.getDisplayMode()==null || form.getDisplayMode().isEmpty()) {
			form.setDisplayMode("window");
		}
		WorkflowForm workflowForm = (WorkflowForm)form;
		WorkflowInterface workflowInterface = null;
		try {
			workflowInterface = getWorkflowInterface(workflowForm, request, record, openMode, sessionInfo);
		}
		catch(Exception e) {
			
		}
		if(workflowInterface==null) {
			super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		}
		else {
			if(!workflowInterface.isLocked() && workflowInterface.getLockPersonName()!=null && !workflowInterface.getLockPersonName().isEmpty()) { //没有锁定流程实例
				workflowForm.setPrompt(workflowInterface.getLockPersonName() + "正在处理当前记录！");
			}
			//设置解锁URL
			form.setUnlockUrl(form.isLocked() && (!form.isInternalForm() || !"dialog".equals(form.getDisplayMode())) ? generateUnlockUrl(form, record, request, sessionInfo) : null);
			//设置重新加载页面的URL
			form.setReloadPageURL(generateReloadURL(form, request));
			//设置流程信息
			workflowForm.setWorkflowId(workflowInterface.getWorkflowDefinitionId()); //流程ID
			workflowForm.setWorkflowName(workflowInterface.getWorkflowName()); //流程名称
			workflowForm.setActivityName(workflowInterface.getActivityName()); //环节名称
			workflowForm.setActivityId(workflowInterface.getActivityId()); //环节ID
			workflowForm.setWorkItemId(workflowInterface.getWorkItemId()); //工作项ID
			workflowForm.setWorkflowTest(workflowInterface.isWorkflowTest()); //是否流程测试
			workflowForm.setParticipant(workflowInterface.getParticipant()); //办理人
			workflowForm.setParticipantId(workflowInterface.getParticipantId()==null ? 0 : Long.parseLong(workflowInterface.getParticipantId())); //办理人ID
			workflowForm.setAgent(workflowInterface.isAgent()); //是否代理人
			//设置子表单
			String subForm = form.getSubForm();
			if(subForm==null || subForm.equals("")) {
				subForm = SUBFORM_READ;
				if(openMode.equals(OPEN_MODE_CREATE) || openMode.equals(OPEN_MODE_EDIT)) {
					subForm = workflowInterface.getSubForm();
				}
			}
			if(!subForm.endsWith(".jsp")) {
				String formName = form.getFormDefine().getName();
				form.setSubForm(formName.substring(formName.lastIndexOf('/') + 1) + subForm + ".jsp");
			}
			
			//设置表单操作
			form.getFormActions().clear();
			if(form.getFormDefine().getActions()!=null) {
				FormulaService formulaService = (FormulaService)getService("formulaService"); //获取公式服务
				for(Iterator iterator = form.getFormDefine().getActions().iterator(); iterator.hasNext();) {
					com.yuanluesoft.jeaf.form.model.FormAction formAction = (com.yuanluesoft.jeaf.form.model.FormAction)iterator.next();
					WorkflowHideConditionCheckCallback callback = new WorkflowHideConditionCheckCallback(accessLevel, deleteEnable, openMode, subForm, workflowInterface.getActions(), formAction.getTitle()); //表单按钮隐藏条件判断回调
					if(!formulaService.checkCondition(formAction.getHideCondition(), callback, form.getFormDefine().getApplicationName(), request, acl, sessionInfo)) {
						form.getFormActions().add(formAction);
						//设置命令中的参数值
						formAction.setExecute(StringUtils.fillParameters(formAction.getExecute(), false, true, false, "utf-8", record, request, null));
					}
				}
			}
			if(accessLevel>=RecordControlService.ACCESS_LEVEL_EDITABLE) {
				if(workflowInterface.isUndoEnable()) { //添加撤消操作
					form.getFormActions().addFormAction(-1, "取回", "FormUtils.doAction(\"" + getWorkflowActionName(workflowForm) + "\", \"workflowAction=" + WORKFLOW_ACTION_UNDO + "\")", false);
				}
				if(workflowInterface.isReverseEnable()) { //添加退回操作
					form.getFormActions().addFormAction(-1, "回退", "FormUtils.doAction(\"" + getWorkflowActionName(workflowForm) + "\", \"workflowAction=" + WORKFLOW_ACTION_REVERSE + "\")", false);
				}
				if(workflowInterface.isSendEnable()) { //添加发送操作
					form.getFormActions().addFormAction(-1, "发送", "FormUtils.doAction(\"" + getWorkflowActionName(workflowForm) + "\", \"workflowAction=" + WORKFLOW_ACTION_SEND + "\")", false);
				}
				if(workflowInterface.isCompleteEnable()) { //添加办理完毕操作
					form.getFormActions().addFormAction(-1, "办理完毕", "FormUtils.doAction(\"" + getWorkflowActionName(workflowForm) + "\", \"workflowAction=" + WORKFLOW_ACTION_SEND + "\")", false);
				}
				if(workflowInterface.isTransmitEnable()) { //添加转办操作
					form.getFormActions().addFormAction(-1, "转办", "DialogUtils.selectPerson(550, 360, false, \"workflowParticipantIds{id},workflowParticipantNames{name}\", \"FormUtils.doAction('" + getWorkflowActionName(workflowForm) + "', 'workflowAction=transmit')\")", false);
				}
				if(workflowInterface.isAddParticipantsEnable()) { //添加增加办理人操作
					form.getFormActions().addFormAction(-1, "增加办理人", "DialogUtils.selectPerson(640, 400, true, \"workflowParticipantIds{id},workflowParticipantNames{name|增加办理人|100%}\", \"FormUtils.doAction('" + getWorkflowActionName(workflowForm) + "', 'workflowAction=addParticipants')\")", false);
				}
			}
			if(workflowForm.isWorkflowTest() && !OPEN_MODE_CREATE.equals(openMode)) { //切换测试用户操作
				form.getFormActions().addFormAction(-1, "切换测试用户", "DialogUtils.selectPerson(550, 360, false, '', 'window.submitting=true; window.location=\"?act=edit&id=" + workflowForm.getId() + "&workflowTest=true&testUserId={id}\"');", false);
			}
			//设置窗口标题
			setFormTitle(form, record, request, sessionInfo);
		}
		//意见处理
		OpinionService opinionService = (OpinionService)getService("opinionService");
		try {
			//设置意见,TODO:配置回退或收回后的意见是否被替换
			if(isFillOpinionByActivity(workflowForm)) {
				opinionService.fillOpinionPackageByActivityId(workflowForm.getOpinionPackage(), (Set)PropertyUtils.getProperty(record, "opinions"), form.getFormDefine().getRecordClassName(), workflowForm.getActivityId(), workflowForm.getParticipantId(), sessionInfo);
			}
			else {
				opinionService.fillOpinionPackageByWorkItemId(workflowForm.getOpinionPackage(), (Set)PropertyUtils.getProperty(record, "opinions"), form.getFormDefine().getRecordClassName(), workflowForm.getWorkItemId(), workflowForm.getParticipantId(), sessionInfo);
			}
		}
		catch(Exception e) {
			
		}
		//设置意见类型列表
		List opinionFields = FieldUtils.listFormFields(form.getFormDefine(), "opinion", null, null, null, false, false, false, false, 1);
		workflowForm.getOpinionPackage().setOpinionTypes(ListUtils.generatePropertyList(opinionFields, "name"));
		//设置当前填写的意见类型
		if(workflowInterface!=null && workflowInterface.getActions()!=null) {
			for(Iterator iterator = workflowInterface.getActions().iterator(); iterator.hasNext();) {
				String actionName = ((Action)iterator.next()).getName();
				if(actionName.startsWith("填写") && actionName.endsWith("意见")) {
					workflowForm.getOpinionPackage().setOpinionType(actionName.substring(2, actionName.length() - 2));
					break;
				}
			}
		}	
		//设置TAB列表
		workflowForm.getTabs().addTab(-1, "basic", "基本信息", "SUBFORM", true);
		addOpinionTab(workflowForm); //办理意见TAB
		if(workflowInterface!=null && !OPEN_MODE_CREATE.equals(openMode)) {
			addWorkflowLogTab(workflowForm); //流程记录TAB
		}
	}
	
	/**
	 * 添加办理意见TAB
	 * @param workflowForm
	 */
	protected Tab addOpinionTab(WorkflowForm workflowForm) {
		if(!workflowForm.getOpinionPackage().isDisable()) {
			return workflowForm.getTabs().addTab(-1, "opinion", "办理意见", "/jeaf/opinionmanage/" + (isFillOpinionByActivity(workflowForm) ? "opinionByType.jsp" : "opinionByTime.jsp"), false);
		}
		return null;
	}
	
	/**
	 * 判断是否按环节来保存意见,默认true
	 * @param workflowForm
	 * @return
	 * @throws ServiceException
	 */
	private boolean isFillOpinionByActivity(WorkflowForm workflowForm) {
		try {
			BusinessDefineService businessDefineService = (BusinessDefineService)getService("businessDefineService");
			BusinessObject businessObject = businessDefineService.getBusinessObject(workflowForm.getFormDefine().getRecordClassName());
			return businessObject==null || !"false".equals(businessObject.getExtendParameter("fillOpinionByActivity"));
		}
		catch(Exception e) {
			return true;
		}
	}
	
	/**
	 * 添加流程记录TAB
	 * @param workflowForm
	 */
	protected Tab addWorkflowLogTab(WorkflowForm workflowForm) {
		return workflowForm.getTabs().addTab(-1, "workflowLog", "流程记录", "/jeaf/workflow/workflowLog.jsp", false);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		WorkflowForm workflowForm = (WorkflowForm)form;
		if(openMode.equals(OPEN_MODE_CREATE)) { //创建工作流实例
			WorkflowData workflowData = (WorkflowData)record;
			WorkflowActionParticipantCallback participantCallback = new WorkflowActionParticipantCallback(workflowForm, request, this);
			WorkflowEntry workflowEntry = getWorkflowEntry(workflowForm, request, record, openMode, true, participantCallback, sessionInfo); //初始化流程入口
			if(workflowEntry!=null) { //有工作流入口
				String workflowInstanceId = getWorkflowExploitService().createWorkflowInstance(workflowEntry.getWorkflowDefinitionId(), workflowEntry.getActivityDefinitionId(), workflowForm.isWorkflowTest(), workflowData, participantCallback, getWorkflowSessionInfo(form, sessionInfo));
	    		//更新工作流实例ID,使得与流程实例同步
	    		workflowForm.setWorkflowInstanceId(workflowInstanceId);
	    		workflowData.setWorkflowInstanceId(workflowInstanceId);
	    		//设置工作项
	    		List workItems = getWorkflowExploitService().listRunningWorkItems(workflowInstanceId, false, sessionInfo);
				WorkItem workItem = (WorkItem)workItems.get(0);
				workflowForm.setWorkItemId(workItem.getId());
	    		request.removeAttribute("workflowInterface");
			}
    	}
		try {
			return super.saveRecord(form, record, openMode, request, response, sessionInfo);
		}
    	catch(Exception e) {
        	if(openMode.equals(OPEN_MODE_CREATE)) { //新纪录,删除工作流实例
        		getWorkflowExploitService().removeWorkflowInstance(workflowForm.getWorkflowInstanceId(), (WorkflowData)record, getWorkflowSessionInfo(form, sessionInfo));
        	}
        	throw e;
    	}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#deleteRecord(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.model.Form, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void deleteRecord(ActionForm form, Form formDefine, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.deleteRecord(form, formDefine, record, request, response, sessionInfo);
		WorkflowForm workflowForm = (WorkflowForm)form;
		//删除流程实例
		String workflowInstanceId = workflowForm.getWorkflowInstanceId();
		if(workflowInstanceId!=null && !workflowInstanceId.equals("")) {
			getWorkflowExploitService().removeWorkflowInstance(workflowInstanceId, (WorkflowData)record, sessionInfo);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		WorkflowForm workflowForm = (WorkflowForm)form;
		WorkflowInterface workflowInterface;
		try {
			workflowInterface = getWorkflowInterface(workflowForm, request, record, OPEN_MODE_EDIT, sessionInfo);
		}
		catch (Exception e) {
			throw new PrivilegeException();
		}
		if(workflowInterface==null) { //没有工作流界面
			super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
			return;
		}
		if(workflowInterface.isWorkflowTest()) { //测试流程,总是允许删除
			return;
		}
		if(workflowInterface.getWorkflowInstanceState()==ProcessInstanceState.CLOSED_ABORTED ||
		   workflowInterface.getWorkflowInstanceState()==ProcessInstanceState.CLOSED_COMPLETED ||
		   workflowInterface.getWorkflowInstanceState()==ProcessInstanceState.CLOSED_TERMINATED) {
			throw new PrivilegeException();
		}
		else if(ListUtils.findObjectByProperty(workflowInterface.getActions(), "deleteAction", new Boolean(true))==null) { 
			throw new PrivilegeException(); //流程还在运行中,操作列表中没有删除操作,禁止删除
		}
	}
	
	/**
	 * 流程运转
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param actionResult
	 * @throws Exception
	 */
	public void run(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response, String actionResult) throws Exception {
		WorkflowForm workflowForm = (WorkflowForm)form;
        workflowForm.setSubForm(null);
        workflowForm.setCurrentAction(mapping.getPath().substring(mapping.getPath().lastIndexOf("/") + 1)); //记录当前struts操作
        if(workflowForm.getWorkflowAction()==null || workflowForm.getWorkflowAction().equals("")) {
        	workflowForm.setWorkflowAction(WORKFLOW_ACTION_SEND);
        }
        loadFormDefine(workflowForm, request);
        String openMode = getOpenMode(workflowForm, request);
        Record record;
        //判断是否需要先保存主记录
        if((workflowForm.getBatchIds()==null || workflowForm.getBatchIds().isEmpty()) && //不是批量发送
           ("," + WORKFLOW_ACTION_SEND + "," + WORKFLOW_ACTION_APPROVAL + "," + WORKFLOW_ACTION_REVERSE + "," + WORKFLOW_ACTION_TRANSMIT + ",").indexOf("," + workflowForm.getWorkflowAction() + ",")!=-1) {
        	record = save(mapping, form, request, response, true, null, null); //保存主记录
        }
        else {
        	record = load(form, request, response); //加载主记录
        }
        //批量处理
        if(WORKFLOW_ACTION_BATCH_SEND.equals(workflowForm.getWorkflowAction())) {
        	WorkflowInterface workflowInterface = (WorkflowInterface)request.getAttribute("workflowInterface"); //获取流程界面
        	if(workflowInterface!=null) {
	        	//检查操作列表中是否有Send="true"的操作
	        	Action action = (Action)ListUtils.findObjectByProperty(workflowInterface.getActions(), "send", new Boolean(true));
	        	if(action!=null) { //有Send="true"的操作
	        		//执行操作对应的脚本
	        		com.yuanluesoft.jeaf.form.model.FormAction formAction = (com.yuanluesoft.jeaf.form.model.FormAction)ListUtils.findObjectByProperty(workflowForm.getFormActions(), "title", action.getName());
	        		request.setAttribute("batchSendScript", formAction.getExecute());
	        	}
	        	else if(workflowInterface.isCompleteEnable() || workflowInterface.isSendEnable()) {
	        		workflowForm.setWorkflowAction(WORKFLOW_ACTION_SEND);
	        	}
        	}
        }
        SessionInfo sessionInfo = getSessionInfo(request, response); //获取会话
        //处理意见操作
        if("post".equalsIgnoreCase(request.getMethod())) { //提交操作，保存意见
        	writeOpinion(workflowForm, request, response, sessionInfo);
        }
        //判断是否需要校验必须执行的操作,同时判断是否需要显示意见输入页面、是否必填
        if(("," + WORKFLOW_ACTION_SEND + "," + WORKFLOW_ACTION_APPROVAL + ",").indexOf("," + workflowForm.getWorkflowAction() + ",")!=-1) {
        	workflowForm.setUndoneActions(listUndoneActions(workflowForm, record, openMode, request, sessionInfo));
    	}
        //流程运转前
        beforeWorkflowRun(workflowForm, request, response, record, sessionInfo);
        //执行流程操作
        openMode = OPEN_MODE_EDIT;
        if(WORKFLOW_ACTION_SEND.equals(workflowForm.getWorkflowAction())) { //发送
        	runSend(workflowForm, request, response, record, openMode, sessionInfo);
        }
        else if(WORKFLOW_ACTION_GET_WORKFLOW_EXIT.equals(workflowForm.getWorkflowAction())) { //发送:获取流程出口列表
        	runGetWorkflowExit(workflowForm, request, response, record, openMode, sessionInfo);
        }
        else if(WORKFLOW_ACTION_AFTER_SELECT_EXIT.equals(workflowForm.getWorkflowAction())) { //发送:获取办理人列表
        	runAfterSelectExit(workflowForm, request, response, record, openMode, sessionInfo);
        }
        else if(WORKFLOW_ACTION_DOSEND.equals(workflowForm.getWorkflowAction())) { //发送:执行发送
        	runDoSend(workflowForm, request, response, record, openMode, sessionInfo);
        }
        else if(WORKFLOW_ACTION_APPROVAL.equals(workflowForm.getWorkflowAction())) { //审核
        	runApproval(workflowForm, record, openMode, request, sessionInfo);
        }
        else if(WORKFLOW_ACTION_DO_APPROVAL.equals(workflowForm.getWorkflowAction())) { //审核
        	runDoApproval(workflowForm, record, openMode, request, sessionInfo);
        }
        else if(WORKFLOW_ACTION_COMPLETE_APPROVAL.equals(workflowForm.getWorkflowAction())) { //完成审核
        	runCompleteApproval(workflowForm, request, response, record, openMode, sessionInfo);
        }
        else if(WORKFLOW_ACTION_REVERSE.equals(workflowForm.getWorkflowAction())) { //回退
        	runReverse(workflowForm, record, request, response, sessionInfo);
        }
        else if(WORKFLOW_ACTION_SELECT_REVERSE_INSTANCE.equals(workflowForm.getWorkflowAction())) { //选择回退出口
        	runSelectReverseInstance(workflowForm, record, request, response, sessionInfo);
        }
        else if(WORKFLOW_ACTION_DOREVERSE.equals(workflowForm.getWorkflowAction())) { //执行回退操作
        	runDoReverse(workflowForm, record, request, response, sessionInfo);
        }
        else if(WORKFLOW_ACTION_UNDO.equals(workflowForm.getWorkflowAction())) { //取回
        	runUndo(workflowForm, record, request, sessionInfo);
        }
        else if(WORKFLOW_ACTION_SELECT_UNDO.equals(workflowForm.getWorkflowAction())) { //选择取回环节
        	runSelectUndo(workflowForm, record, request, response, sessionInfo);
        }
        else if(WORKFLOW_ACTION_DOUNDO.equals(workflowForm.getWorkflowAction())) { //执行取回操作
        	runDoUndo(workflowForm, record, request, response, sessionInfo);
        }
        else if(WORKFLOW_ACTION_TRANSMIT.equals(workflowForm.getWorkflowAction())) { //执行转办操作
        	runTransmit(workflowForm, record, sessionInfo);
        }
        else if(WORKFLOW_ACTION_ADD_PARTICIPANTS.equals(workflowForm.getWorkflowAction())) { //执行增加办理人操作
        	runAddParticipants(workflowForm, record, sessionInfo);
        }
        setWorkflowActionDialog(workflowForm, request); //设置内嵌的对话框
      	if(workflowForm.getWorkflowAction()==null) { //流程操作已经执行完毕
			//设置刷新父窗口的脚本
	        workflowForm.setRefeshOpenerScript(generateRefreshOpenerScript(workflowForm, record, openMode, "run", actionResult, request, sessionInfo));
	        //设置操作结果页面
	        workflowForm.setWorkItemId(null);
	        if(actionResult!=null) {
	        	workflowForm.setActionResult(actionResult);
	        }
        	setResultPage(workflowForm, record, openMode, "run", actionResult, request, sessionInfo);
        	unlock(workflowForm, request, openMode, sessionInfo);
        	workflowForm.setLocked(false);
        }
    }
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#setResultPage(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void setResultPage(ActionForm form, Record record, String openMode, String currentAction, String actionResult, HttpServletRequest request, SessionInfo sessionInfo) {
		super.setResultPage(form, record, openMode, currentAction, actionResult, request, sessionInfo);
		WorkflowForm workflowForm = (WorkflowForm)form;
		if(workflowForm.getBatchIds()!=null && !workflowForm.getBatchIds().isEmpty()) { //批量处理，不允许“返回”
			workflowForm.getFormActions().removeFormAction("返回");
		}
	}

	/**
	 * 获取未执行的操作
	 * @param workflowForm
	 * @param record
	 * @param openMode
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	protected List listUndoneActions(WorkflowForm workflowForm, Record record, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		if(openMode.equals(OPEN_MODE_CREATE)) {
			return getWorkflowExploitService().previewUndoneActions(workflowForm.getWorkflowId(), workflowForm.getActivityId(), getWorkflowSessionInfo(workflowForm, sessionInfo));
    	}
    	else {
    		return getWorkflowExploitService().listUndoneActions(workflowForm.getWorkflowInstanceId(), workflowForm.getWorkItemId(), getWorkflowSessionInfo(workflowForm, sessionInfo));
    	}
	}

	/**
	 * 设置流程操作对话框
	 * @param workflowForm
	 * @param request
	 */
	protected void setWorkflowActionDialog(WorkflowForm workflowForm, HttpServletRequest request) {
        if(workflowForm.getWorkflowAction()==null || workflowForm.getWorkflowAction().isEmpty()) {
        	return;
        }
        addReloadAction(workflowForm, "取消", request, -1, true); //添加“取消”操作
        //检查是否有必须执行的操作，并获取其中的意见按钮
        Action writeOpinionAction = null;
        if(workflowForm.getUndoneActions()!=null && !workflowForm.getUndoneActions().isEmpty()) { //还有需要执行的操作
        	Action action = (Action)workflowForm.getUndoneActions().get(0);
        	if(workflowForm.getUndoneActions().size()==1 && action.getName().startsWith("填写") && action.getName().endsWith("意见")) {
	        	writeOpinionAction = action;
			}
        	else {
        		//显示操作未执行提醒页面
         		String script = "FormUtils.doAction('" + workflowForm.getCurrentAction() + "', 'workflowAction=" + (WORKFLOW_ACTION_SEND.equals(workflowForm.getWorkflowAction()) ? WORKFLOW_ACTION_GET_WORKFLOW_EXIT : WORKFLOW_ACTION_DO_APPROVAL) + "');";
	    		setUndoneActionsPromptDialog(workflowForm, request, script);
	    		return;
        	}
    	}
        if(WORKFLOW_ACTION_SEND.equals(workflowForm.getWorkflowAction())) { //发送
        	workflowForm.setInnerDialog("/jeaf/workflow/writeOpinion.jsp"); //显示意见填写页面
			workflowForm.setFormTitle("填写办理意见");
			String script = "FormUtils.doAction('" + workflowForm.getCurrentAction() + "', 'workflowAction=" + WORKFLOW_ACTION_GET_WORKFLOW_EXIT + "');";
			if(writeOpinionAction!=null && writeOpinionAction.isNecessary()) {
				script = "if(document.getElementsByName('opinionPackage.opinion')[0].value=='') {alert('办理意见不能为空。')} else {" + script + "}";
			}
			workflowForm.getFormActions().addFormAction(0, "下一步", script, false);
    	}
        else if(WORKFLOW_ACTION_SELECT_EXIT.equals(workflowForm.getWorkflowAction())) { //选择流程出口
    		workflowForm.setInnerDialog("/jeaf/workflow/" + workflowForm.getWorkflowAction() + ".jsp");
    		workflowForm.setFormTitle("选择流程出口");
    		workflowForm.getFormActions().addFormAction(0, "下一步", "doOK();", false);
    	}
    	else if(WORKFLOW_ACTION_SELECT_PARTICIPANT.equals(workflowForm.getWorkflowAction())) { //选择办理人
    		workflowForm.setInnerDialog("/jeaf/workflow/" + workflowForm.getWorkflowAction() + ".jsp");
    		BaseExit exit = (BaseExit)ListUtils.findObjectByProperty(workflowForm.getWorkflowExit().getExits(), "selected", new Boolean(true));
    		List selectedExits = new ArrayList();
    		if(exit instanceof ActivityExit) {
    			selectedExits.add(exit);
    		}
    		else if(exit instanceof SplitExit) {
    			for(java.util.Iterator iterator = ((SplitExit)exit).getSplitWorkflowExit().getExits().iterator(); iterator.hasNext();) {
    				BaseExit splitExit = (BaseExit)iterator.next();
    				if((splitExit instanceof ActivityExit) && splitExit.isSelected()) {
    					selectedExits.add(splitExit);
    				}
    			}
    		}
    		//设置对话框标题
    		workflowForm.setFormTitle("办理人" + (selectedExits.size()!=1 ? "" : " - " + ((BaseExit)selectedExits.get(0)).getName()));
    		//创建TAB列表
    		TabList tabs = new TabList();
    		for(Iterator iterator = selectedExits.iterator(); iterator.hasNext();) {
    			BaseExit selectedExit = (BaseExit)iterator.next();
    			Tab tab = tabs.addTab(-1, selectedExit.getId(), selectedExit.getName(), null, tabs.size()==0);
    			tab.setAttribute("selectedExit", selectedExit);
    		}
    		request.setAttribute("selectedExits", selectedExits);
    		request.setAttribute("selectedParticipantTabs", tabs);
    		//添加操作
    		workflowForm.getFormActions().addFormAction(0, "完成", "doOk()", false);
    	}
    	else if(WORKFLOW_ACTION_DO_APPROVAL.equals(workflowForm.getWorkflowAction())) { //执行审核
    		workflowForm.setInnerDialog("/jeaf/workflow/writeOpinion.jsp"); //显示意见填写页面
    		workflowForm.setFormTitle(workflowForm.getWorkflowApprovalDialogTitle()==null ? "审核" : workflowForm.getWorkflowApprovalDialogTitle());
    		String[] approvalOptions = (workflowForm.getWorkflowApprovalOptions()==null ? "同意,不同意" : workflowForm.getWorkflowApprovalOptions()).split(",");
    		for(int i=0; i<approvalOptions.length; i++) {
    			try {
    				String script = "FormUtils.doAction('" + workflowForm.getCurrentAction() + "', 'workflowAction=completeApproval&workflowApprovalResult=" + URLEncoder.encode(approvalOptions[i], "utf-8") + "');";
        			if(writeOpinionAction!=null && writeOpinionAction.isNecessary()) {
        				script = "if(document.getElementsByName('opinionPackage.opinion')[0].value=='') {alert('办理意见不能为空。')} else {" + script + "}";
        			}
					workflowForm.getFormActions().addFormAction(i, approvalOptions[i], script, false);
				}
    			catch (UnsupportedEncodingException e) {
				
				}
    		}
    	}
    	else if(WORKFLOW_ACTION_CONFIRM_COMPLETE.equals(workflowForm.getWorkflowAction())) { //提示办理完毕
    		workflowForm.setInnerDialog("/jeaf/workflow/workflowPrompt.jsp"); //显示消息提示页面
    		workflowForm.setFormTitle(workflowForm.getWorkflowApprovalDialogTitle()==null ? "系统提示" : workflowForm.getWorkflowApprovalDialogTitle());
    		workflowForm.getFormActions().addFormAction(0, "完成", "FormUtils.doAction('" + workflowForm.getCurrentAction() + "', 'workflowAction=doSend');", false);
    		BaseExit exit = (BaseExit)workflowForm.getWorkflowExit().getExits().get(0);
    		String workflowPrompt = null;
    		if(exit instanceof CompleteExit) {
    			workflowPrompt = "您是否确定已经完成办理过程？";
    		}
    		else if(exit instanceof EndExit) {
    			workflowPrompt = "您是否确定已经完成办理过程，并结束流程？";
    		}
    		else if(exit instanceof ProcedureExit) {
    			workflowPrompt = "您是否确定已经完成办理过程，并执行操作" + exit.getName() + "？";
    		}
			request.setAttribute("workflowPrompt", workflowPrompt);
    	}
    	else if(WORKFLOW_ACTION_NO_EXIT.equals(workflowForm.getWorkflowAction())) { //没有流程出口
    		workflowForm.setInnerDialog("/jeaf/workflow/workflowPrompt.jsp"); //显示消息提示页面
    		request.setAttribute("workflowPrompt", "没有流程出口，请通知管理员检查流程配置是否正确！");
    		workflowForm.setFormTitle("系统提示");
    	}
    	else if(WORKFLOW_ACTION_SELECT_REVERSE_INSTANCE.equals(workflowForm.getWorkflowAction())) { //选择回退环节
    		workflowForm.setInnerDialog("/jeaf/workflow/" + workflowForm.getWorkflowAction() + ".jsp");
    		workflowForm.setFormTitle("选择回退环节");
    		workflowForm.getFormActions().addFormAction(0, "完成", "doOK()", false);
    	}
    	else if(WORKFLOW_ACTION_REVERSE.equals(workflowForm.getWorkflowAction())) { //回退
    		workflowForm.setInnerDialog("/jeaf/workflow/writeOpinion.jsp"); //显示意见填写页面
    		workflowForm.setFormTitle(workflowForm.getReverseActivityInstances().size()!=1 ? "回退" : "回退到" + ListUtils.join(workflowForm.getReverseActivityInstances(), "name", "、", false));
    		workflowForm.getFormActions().addFormAction(0, "回退", "FormUtils.doAction('" + workflowForm.getCurrentAction() + "', 'workflowAction=selectReverseInstance');", false);
    	}
    	else if(WORKFLOW_ACTION_SELECT_UNDO.equals(workflowForm.getWorkflowAction())) { //选择取回环节
    		workflowForm.setInnerDialog("/jeaf/workflow/" + workflowForm.getWorkflowAction() + ".jsp");
    		workflowForm.setFormTitle("选择取回环节");
    		workflowForm.getFormActions().addFormAction(0, "完成", "doOK();", false);
    	}
    	else if(WORKFLOW_ACTION_UNDO.equals(workflowForm.getWorkflowAction())) { //取回
    		workflowForm.setInnerDialog("/jeaf/workflow/" + workflowForm.getWorkflowAction() + ".jsp");
    		workflowForm.setFormTitle(workflowForm.getWorkflowUndos().size()!=1 ? "取回" : ListUtils.join(workflowForm.getWorkflowUndos(), "title", "、", false));
    		workflowForm.getFormActions().addFormAction(0, "取回", "FormUtils.doAction('" + workflowForm.getCurrentAction() + "', 'workflowAction=selectWorkflowUndo');", false);
    	}
    	else if(WORKFLOW_ACTION_BATCH_SEND.equals(workflowForm.getWorkflowAction())) { //批量处理
    		workflowForm.setInnerDialog("/jeaf/workflow/" + workflowForm.getWorkflowAction() + ".jsp");
    		workflowForm.setWorkflowAction(WORKFLOW_ACTION_SEND);
    	}
	}
	
	/**
	 * 输出操作未执行提醒页面
	 * @param workflowForm
	 * @param request
	 * @param scriptForContinue
	 */
	protected void setUndoneActionsPromptDialog(WorkflowForm workflowForm, HttpServletRequest request, String scriptForContinue) {
		addReloadAction(workflowForm, "取消", request, -1, true); //添加“取消”操作
 		//显示操作未执行提醒页面
    	workflowForm.setFormTitle("系统提示");
		String workflowPrompt = ListUtils.join(workflowForm.getUndoneActions(), "prompt", "，", false) + "，";
		workflowForm.setInnerDialog("/jeaf/workflow/workflowPrompt.jsp");
		if(ListUtils.findObjectByProperty(workflowForm.getUndoneActions(), "necessary", new Boolean(true))!=null) { //有必须执行的操作
			workflowPrompt += "操作不能完成！";
		}
		else {
			workflowPrompt += "是否继续？";
	   		workflowForm.getFormActions().addFormAction(0, "继续", scriptForContinue, false);
		}
		request.setAttribute("workflowPrompt", workflowPrompt);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#addReloadAction(com.yuanluesoft.jeaf.form.ActionForm, java.lang.String, javax.servlet.http.HttpServletRequest, int, boolean)
	 */
	public void addReloadAction(ActionForm form, String actionTitle, HttpServletRequest request, int actionIndex, boolean firstAction) {
		WorkflowForm workflowForm = (WorkflowForm)form;
		if(workflowForm.getBatchIds()!=null && !workflowForm.getBatchIds().isEmpty()) {
			workflowForm.getFormActions().addFormAction(actionIndex, actionTitle==null ? "取消" : actionTitle, "DialogUtils.closeDialog();", firstAction);
		}
		else {
			super.addReloadAction(form, actionTitle, request, actionIndex, firstAction);
		}
 	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#generateReloadURL(com.yuanluesoft.jeaf.form.ActionForm, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public String generateReloadURL(ActionForm form, HttpServletRequest request) {
		WorkflowForm workflowForm = (WorkflowForm)form;
		String url =  super.generateReloadURL(form, request);
		return url==null ? null : url + (!workflowForm.isWorkflowTest() ? "" : "&workflowTest=true&testUserId=" + workflowForm.getTestUserId());
	}

	/**
	 * 流程运转前
	 * @param workflowForm
	 * @param request
	 * @param response
	 * @param record
	 * @param sessionInfo
	 * @throws Exception
	 */
	protected void beforeWorkflowRun(WorkflowForm workflowForm, HttpServletRequest request, HttpServletResponse response, Record record, SessionInfo sessionInfo) throws Exception {
		
	}
	
	/**
	 * 发送:获取流程出口列表
	 * @param workflowForm
	 * @param response
	 * @param record
	 * @throws Exception
	 */
	protected void runSend(WorkflowForm workflowForm, HttpServletRequest request, HttpServletResponse response, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		if(workflowForm.getUndoneActions()!=null && !workflowForm.getUndoneActions().isEmpty() && //有需要执行的操作
		   (!workflowForm.isPrompted() || //没有提示过
			ListUtils.findObjectByProperty(workflowForm.getUndoneActions(), "necessary", new Boolean(true))!=null)) { //或者有必须执行的操作
			return;
		}
		if((workflowForm.getBatchIds()!=null && !workflowForm.getBatchIds().isEmpty()) && //批量发送
		   workflowForm.getOpinionPackage().getOpinionType()!=null) { //需要填写意见
			return;
		}
		runGetWorkflowExit(workflowForm, request, response, record, openMode, sessionInfo);
	}
	
	/**
	 * 审核:判断是否有需要执行的操作,显示审核页面
	 * @param workflowForm
	 * @param record
	 * @param openMode
	 * @param request
	 * @param sessionInfo
	 * @throws Exception
	 */
	protected void runApproval(WorkflowForm workflowForm, Record record, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		if(workflowForm.getUndoneActions()!=null && !workflowForm.getUndoneActions().isEmpty()) { //还有需要执行的操作
        	Action action = (Action)workflowForm.getUndoneActions().get(0);
        	if(workflowForm.getUndoneActions().size()>1 || !(action.getName().startsWith("填写") && action.getName().endsWith("意见"))) { //不是填写意见
	        	return;
			}
		}
		workflowForm.setWorkflowAction(WORKFLOW_ACTION_DO_APPROVAL);
		runDoApproval(workflowForm, record, openMode, request, sessionInfo);
	}
	
	/**
	 * 执行审核
	 * @param workflowForm
	 * @param record
	 * @param openMode
	 * @param request
	 * @param sessionInfo
	 * @throws Exception
	 */
	protected void runDoApproval(WorkflowForm workflowForm, Record record, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		beforeApproval(workflowForm, request, record, openMode, sessionInfo); //审核以前
	}
	
	/**
	 * 完成审核
	 * @param workflowForm
	 * @param response
	 * @param record
	 * @param openMode
	 * @param sessionInfo
	 * @throws Exception
	 */
	protected void runCompleteApproval(WorkflowForm workflowForm, HttpServletRequest request, HttpServletResponse response, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
	    workflowForm.setPrompted(true);
	    runGetWorkflowExit(workflowForm, request, response, record, openMode, sessionInfo);
	    workflowForm.setActionResult("审核完成");
	}
	
	/**
	 * 审核前
	 * @param workflowForm
	 * @param request
	 * @param record
	 * @param openMode
	 * @param sessionInfo
	 * @throws Exception
	 */
	protected void beforeApproval(WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		
	}
	
	/**
	 * 审核页面提交后,继承者可以继承本方法,实现自己的业务逻辑
	 * @param approvalResult
	 * @param workflowForm
	 * @param request
	 * @param record
	 * @param openMode
	 * @param sessionInfo
	 * @throws Exception
	 */
	protected void afterApproval(String approvalResult, WorkflowForm workflowForm, HttpServletRequest request, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		
	}
	
	/**
	 * 选择流程出口
	 * @param workflowForm
	 * @param response
	 * @param record
	 * @param openMode
	 * @throws Exception
	 */
	protected WorkflowExit runGetWorkflowExit(WorkflowForm workflowForm, HttpServletRequest request, HttpServletResponse response, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		WorkflowActionParticipantCallback participantCallback = new WorkflowActionParticipantCallback(workflowForm, request, this);
		WorkflowExit workflowExit = getWorkflowExploitService().getWorkflowExit(workflowForm.getWorkflowInstanceId(), workflowForm.getWorkItemId(), workflowForm.isWorkflowTest(), (WorkflowData)record, workflowForm.getWorkflowApprovalResult(), participantCallback, getWorkflowSessionInfo(workflowForm, sessionInfo));
		if(workflowExit.getExits()==null || workflowExit.getExits().isEmpty()) { //没有流程出口
		    workflowForm.setWorkflowAction(WORKFLOW_ACTION_NO_EXIT);
		    return workflowExit;
		}
		workflowForm.setWorkflowExit(workflowExit);
		BaseExit exit = (BaseExit)workflowExit.getExits().get(0);
		if(workflowExit.getExits().size()>1) { //多个出口,显示选择出口
    		workflowForm.setWorkflowAction(WORKFLOW_ACTION_SELECT_EXIT);
    	}
    	else if(exit instanceof ActivityExit) { //单一环节出口,显示选择办理人
			ActivityExit activityExit = (ActivityExit)exit;
			if(alwaysAutoSend) {
				activityExit.setAutoSend(true); //设置为自动发送
				activityExit.setSelected(true); //设置为选中
			}
    		if(workflowForm.isPrompted() && ((ActivityExit)exit).isAutoSend()) { //已经提示过,且自动发送
    		    runDoSend(workflowForm, request, response, record, openMode, sessionInfo);
    		    return workflowExit;
    		}
    		workflowForm.setWorkflowAction(WORKFLOW_ACTION_SELECT_PARTICIPANT);
    	}
		else if(exit instanceof SplitExit) { //单一出口,同时开始,显示选择出口
			workflowForm.setWorkflowAction(WORKFLOW_ACTION_SELECT_EXIT);
		}
		else if(exit instanceof ReverseExit) { //回退出口
		    List reverseActivityInstances = ((ReverseExit)exit).getReverseActivityInstances();
		    if(workflowForm.isPrompted() && reverseActivityInstances!=null && reverseActivityInstances.size()==1) { //已经提示过,且仅有一个回退环节
		        workflowForm.setSelectedReverseInstanceId(((ActivityInstance)reverseActivityInstances.get(0)).getId());
    		    runDoReverse(workflowForm, record, request, response, sessionInfo);
    		    return workflowExit;
    		}
		    workflowForm.setReverseActivityInstances(reverseActivityInstances);
		    workflowForm.setWorkflowAction(WORKFLOW_ACTION_SELECT_REVERSE_INSTANCE);
		}
		else { //单一出口, 流程结束或执行过程, 显示确认办理完毕
			//TODO:修改提示信息,exit instanceof ProcedureExit办理结束改为执行过程...
			if(workflowForm.isPrompted()) { //已经提示过
    		    runDoSend(workflowForm, request, response, record, openMode, sessionInfo);
    		    return workflowExit;
    		}
			workflowForm.setWorkflowAction(WORKFLOW_ACTION_CONFIRM_COMPLETE);
		}
    	workflowForm.setWorkflowExitText(JsonUtils.generateJSONObject(workflowExit).toJSONString());
    	return workflowExit;
	}
	
	/**
	 * 办理人回调
	 * @author linchuan
	 *
	 */
	public class WorkflowActionParticipantCallback implements WorkflowParticipantCallback {
		private WorkflowForm workflowForm;
		private HttpServletRequest request;
		private WorkflowAction workflowAction;
		
		public WorkflowActionParticipantCallback(WorkflowForm workflowForm, HttpServletRequest request, WorkflowAction workflowAction) {
			super();
			this.workflowForm = workflowForm;
			this.request = request;
			this.workflowAction = workflowAction;
		}
		
		/* (non-Javadoc)
		 * @see com.yuanluesoft.jeaf.workflow.service.WorkflowParticipantCallback#listProgrammingParticipants(com.yuanluesoft.workflow.client.model.runtime.ActivityExit, java.lang.String, java.lang.String)
		 */
		public List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
			try {
				return workflowAction.listProgrammingParticipants(programmingParticipantId, programmingParticipantName, workflowForm, workflowData, request, sessionInfo);
			}
			catch (Exception e) {
				Logger.exception(e);
				throw new ServiceException(e.getMessage());
			}
		}
		
		/*
		 * (non-Javadoc)
		 * @see com.yuanluesoft.jeaf.workflow.service.WorkflowParticipantCallback#resetParticipants(java.util.List, boolean)
		 */
		public List resetParticipants(List participants, boolean anyoneParticipate, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
			try {
				return workflowAction.resetParticipants(participants, anyoneParticipate, workflowForm, workflowData, request, sessionInfo);
			}
			catch (Exception e) {
				throw new ServiceException(e.getMessage());
			}
		}

		/* (non-Javadoc)
		 * @see com.yuanluesoft.jeaf.workflow.service.WorkflowParticipantCallback#isMemberOfProgrammingParticipants(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
		 */
		public boolean isMemberOfProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
			try {
				return workflowAction.isMemberOfProgrammingParticipants(programmingParticipantId, programmingParticipantName, workflowForm, workflowData, request, sessionInfo);
			}
			catch (Exception e) {
				throw new ServiceException(e.getMessage());
			}
		}
	}
	
	/**
	 * 获取由程序决定的办理人列表
	 * @param programmingParticipantId
	 * @param programmingParticipantName
	 * @param workflowForm
	 * @param record
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	protected List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		return null;
	}
	
	/**
	 * 判断用户是否属于编程决定的办理人
	 * @param programmingParticipantId
	 * @param programmingParticipantName
	 * @param workflowForm
	 * @param record
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	protected boolean isMemberOfProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		return false;
	}

	/**
	 * 重设办理人列表
	 * @param participants
	 * @param anyoneParticipate
	 * @param workflowForm
	 * @param record
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	protected List resetParticipants(List participants, boolean anyoneParticipate, WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		return participants;
	}
	
	/**
	 * 发送:选择出口后
	 * @param workflowForm
	 * @param response
	 * @param record
	 * @throws Exception
	 */
	protected void runAfterSelectExit(WorkflowForm workflowForm, HttpServletRequest request, HttpServletResponse response, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		WorkflowExit workflowExit = (WorkflowExit)JsonUtils.generateJavaObject(workflowForm.getWorkflowExitText());
		workflowForm.setWorkflowExit(workflowExit);
		BaseExit exit = (BaseExit)ListUtils.findObjectByProperty(workflowExit.getExits(), "selected", new Boolean(true));
		boolean selectParticaipant = false;
		if(exit instanceof SplitExit) {
		    selectParticaipant = (ListUtils.findObjectByType(((SplitExit)exit).getSplitWorkflowExit().getExits(), ActivityExit.class)!=null);
		}
		else {
		    selectParticaipant = (exit instanceof ActivityExit);
		}
		if(selectParticaipant) { //选择办理人
		    workflowForm.setWorkflowAction(WORKFLOW_ACTION_SELECT_PARTICIPANT);
		}
		else { //执行发送
		    runDoSend(workflowForm, request, response, record, openMode, sessionInfo);
		}
	}
	
	/**
	 * 发送:执行发送
	 * @param workflowForm
	 * @param response
	 * @param record
	 * @param openMode
	 * @throws Exception
	 */
	protected void runDoSend(final WorkflowForm workflowForm, final HttpServletRequest request, final HttpServletResponse response, Record record, final String openMode, final SessionInfo sessionInfo) throws Exception {
		final WorkflowExit workflowExit = workflowForm.getWorkflowExit()!=null ? workflowForm.getWorkflowExit() : (WorkflowExit)JsonUtils.generateJavaObject(workflowForm.getWorkflowExitText());
		final BaseExit exit = (BaseExit)ListUtils.findObjectByProperty(workflowExit.getExits(), "selected", new Boolean(true));
		final boolean workflowInstanceCompleted = (exit instanceof EndExit);
		final boolean isReverse = (exit instanceof ReverseExit);
		if(workflowForm.getBatchIds()==null || workflowForm.getBatchIds().equals("")) { //不是批量处理
			//完成工作项前
			beforeWorkitemCompleted(workflowForm, workflowInstanceCompleted, isReverse, record, openMode, sessionInfo, request);
			//完成工作项
			WorkflowActionParticipantCallback participantCallback = new WorkflowActionParticipantCallback(workflowForm, request, this);
			getWorkflowExploitService().completeWorkItem(workflowForm.getWorkflowInstanceId(), workflowForm.getWorkItemId(), workflowForm.isWorkflowTest(), createWorklfowMessage(record, workflowForm), workflowExit, (WorkflowData)record, workflowForm.getWorkflowApprovalResult(), participantCallback, getWorkflowSessionInfo(workflowForm, sessionInfo));
			//完成工作项后
			afterWorkitemCompleted(workflowForm, workflowInstanceCompleted, isReverse, record, openMode, sessionInfo, request);
			//完成审核后
			if(workflowForm.getWorkflowApprovalResult()!=null && !workflowForm.getWorkflowApprovalResult().isEmpty()) {
				afterApproval(workflowForm.getWorkflowApprovalResult(), workflowForm, request, record, openMode, sessionInfo);
			}
			//保存
			saveRecord(workflowForm, record, OPEN_MODE_EDIT, request, response, sessionInfo);
			workflowForm.setReloadPageURL(generateReloadURL(workflowForm, request)); //设置重新打开页面的URL
		}
		else { //批量处理
			final WorkflowAction workflowAction = this;
			doBatch(workflowForm, request, response, sessionInfo, new BatchCallback() {
				public void run(Record record, WorkflowInterface workflowInterface) throws Exception {
					WorkflowExit clonedWorkflowExit = (WorkflowExit)workflowExit.clone();
					BaseExit clonedExit = (BaseExit)ListUtils.findObjectByProperty(clonedWorkflowExit.getExits(), "selected", new Boolean(true));
					//替换出口中的processInstanceId,不支持子流程
					replaceExitProcessInstanceId(clonedExit, ((ProcessInstance)workflowInterface.getWorkflowInstance().getProcessInstanceList().get(0)).getId());
					//完成工作项前
					beforeWorkitemCompleted(workflowForm, workflowInstanceCompleted, isReverse, record, openMode, sessionInfo, request);
					//完成工作项
					WorkflowActionParticipantCallback participantCallback = new WorkflowActionParticipantCallback(workflowForm, request, workflowAction);
					getWorkflowExploitService().completeWorkItem(workflowForm.getWorkflowInstanceId(), workflowForm.getWorkItemId(), workflowForm.isWorkflowTest(), createWorklfowMessage(record, workflowForm), clonedWorkflowExit, (WorkflowData)record, workflowForm.getWorkflowApprovalResult(), participantCallback, getWorkflowSessionInfo(workflowForm, sessionInfo));
					//完成工作项后
					afterWorkitemCompleted(workflowForm, workflowInstanceCompleted, isReverse, record, openMode, sessionInfo, request);
					//完成审核后
					if(workflowForm.getWorkflowApprovalResult()!=null && !workflowForm.getWorkflowApprovalResult().isEmpty()) {
						afterApproval(workflowForm.getWorkflowApprovalResult(), workflowForm, request, record, openMode, sessionInfo);
					}
					//保存
					saveRecord(workflowForm, record, OPEN_MODE_EDIT, request, response, sessionInfo);
				}
			});
		}
		workflowForm.setActionResult("办理完毕");
	    workflowForm.setWorkflowAction(null);
	}
	
	/**
	 * 递归函数：替换出口中的过程实例ID
	 * @param exit
	 * @param processInstanceId
	 */
	private void replaceExitProcessInstanceId(BaseExit exit, String processInstanceId) {
		if(!(exit instanceof SplitExit)) {
			exit.setId(processInstanceId + exit.getId().substring(exit.getId().lastIndexOf(".")));
		}
		else {
			SplitExit splitExit = (SplitExit)exit;
			if(splitExit.getSplitWorkflowExit().getExits()==null) {
				return;
			}
		    for(Iterator iterator = splitExit.getSplitWorkflowExit().getExits().iterator(); iterator.hasNext();) {
		    	exit = (BaseExit)iterator.next();
		    	//递归调用
		    	replaceExitProcessInstanceId(exit, processInstanceId);
		    }
		}
	}
	
	/**
	 * 工作项完成前调用,用户可修改记录,不需要调用保存
	 * @param workflowForm
	 * @param workflowInstanceWillComplete
	 * @param isReverse
	 * @param record
	 * @param openMode
	 * @param sessionInfo
	 * @param request
	 * @throws Exception
	 */
	protected void beforeWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceWillComplete, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		
	}
	
	/**
	 * 工作项完成后调用,用户可修改记录,不需要调用保存
	 * @param workflowForm
	 * @param workflowInstanceCompleted
	 * @param isReverse
	 * @param record
	 * @param openMode
	 * @param sessionInfo
	 * @param request
	 * @throws Exception
	 */
	protected void afterWorkitemCompleted(WorkflowForm workflowForm, boolean workflowInstanceCompleted, boolean isReverse, Record record, String openMode, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		
	}
	
	/**
	 * 回退
	 * @param workflowForm
	 * @param record
	 * @param request
	 * @param response
	 * @param sessionInfo
	 * @throws Exception
	 */
	protected void runReverse(WorkflowForm workflowForm, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
	    //设置允许回退的环节
		WorkflowExploitService workflowExploitService = getWorkflowExploitService();
	    List reverseActivityInstances = workflowExploitService.listReverseActivityInstances(workflowForm.getWorkflowInstanceId(), "" + workflowForm.getWorkItemId(), getWorkflowSessionInfo(workflowForm, sessionInfo));
		workflowForm.setReverseActivityInstances(reverseActivityInstances);
	}
	
	/**
	 * 选择回退实例
	 * @param workflowForm
	 * @param record
	 * @param request
	 * @param response
	 * @param sessionInfo
	 * @throws Exception
	 */
	protected void runSelectReverseInstance(WorkflowForm workflowForm, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
	    //保存回退意见
		WorkflowExploitService workflowExploitService = getWorkflowExploitService();
	    String opinion = workflowForm.getOpinionPackage().getOpinion();
	    if(opinion!=null && !opinion.equals("")) {
		    //把回退原因记录到流程
		    workflowExploitService.writeTransactLog("" + workflowForm.getWorkflowInstanceId(), workflowForm.getWorkItemId(), "回退原因:" + opinion, sessionInfo);
	    }
	    workflowForm.setPrompted(true);
	    List reverseActivityInstances = workflowExploitService.listReverseActivityInstances(workflowForm.getWorkflowInstanceId(), "" + workflowForm.getWorkItemId(), getWorkflowSessionInfo(workflowForm, sessionInfo));
		workflowForm.setReverseActivityInstances(reverseActivityInstances);
		if(reverseActivityInstances!=null && reverseActivityInstances.size()==1) {
		    //仅一个回退出口,直接执行回退
		    workflowForm.setSelectedReverseInstanceId(((ActivityInstance)reverseActivityInstances.get(0)).getId());
		    runDoReverse(workflowForm, record, request, response, sessionInfo);
		}
		else {
		    workflowForm.setWorkflowAction(WORKFLOW_ACTION_SELECT_REVERSE_INSTANCE);
		}
	}
	
	/**
	 * 执行回退操作
	 * @param workflowForm
	 * @param record
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void runDoReverse(WorkflowForm workflowForm, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		getWorkflowExploitService().reverse(workflowForm.getWorkflowInstanceId(), workflowForm.getWorkItemId(), workflowForm.isWorkflowTest(), workflowForm.getSelectedReverseInstanceId(), createWorklfowMessage(record, workflowForm), (WorkflowData)record, null, getWorkflowSessionInfo(workflowForm, sessionInfo));
		workflowForm.setActionResult("回退成功");
		workflowForm.setWorkflowAction(null);
	}

	/**
	 * 取回
	 * @param workflowForm
	 * @param record
	 * @param request
	 * @param sessionInfo
	 * @throws Exception
	 */
	protected void runUndo(WorkflowForm workflowForm, Record record, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		//设置允许取回的环节
		List workflowUndos = getWorkflowExploitService().listWorkflowUndos(workflowForm.getWorkflowInstanceId(), getWorkflowSessionInfo(workflowForm, sessionInfo));
		workflowForm.setWorkflowUndos(workflowUndos);
	}
	
	/**
	 * 选择取回环节
	 * @param workflowForm
	 * @param record
	 * @param request
	 * @param response
	 * @param sessionInfo
	 * @throws Exception
	 */
	protected void runSelectUndo(WorkflowForm workflowForm, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		List workflowUndos = getWorkflowExploitService().listWorkflowUndos(workflowForm.getWorkflowInstanceId(), getWorkflowSessionInfo(workflowForm, sessionInfo));
		workflowForm.setWorkflowUndos(workflowUndos);
		if(workflowUndos!=null && workflowUndos.size()==1) { //仅一个取回环节,直接执行取回
		    workflowForm.setSelectedWorkflowUndoId(((WorkflowUndo)workflowUndos.get(0)).getId());
		    runDoUndo(workflowForm, record, request, response, sessionInfo);
		}
	}
	
	/**
	 * 执行取回操作
	 * @param workflowForm
	 * @param record
	 * @param request
	 * @param response
	 * @param sessionInfo
	 * @throws Exception
	 */
	protected void runDoUndo(WorkflowForm workflowForm, Record record, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		getWorkflowExploitService().undo(workflowForm.getWorkflowInstanceId(), workflowForm.getSelectedWorkflowUndoId(), workflowForm.isWorkflowTest(), workflowForm.getOpinionPackage().getOpinion(), createWorklfowMessage(record, workflowForm), (WorkflowData)record, getWorkflowSessionInfo(workflowForm, sessionInfo));
		workflowForm.setActionResult("成功取回");
		workflowForm.setWorkflowAction(null);
	}
	
	/**
	 * 转办
	 * @param workflowForm
	 * @param record
	 * @throws Exception
	 */
	protected void runTransmit(WorkflowForm workflowForm, Record record, SessionInfo sessionInfo) throws Exception {
		getWorkflowExploitService().transmitToPerson(workflowForm.getWorkflowInstanceId(), "" + workflowForm.getWorkItemId(), workflowForm.isWorkflowTest(), workflowForm.getWorkflowParticipantIds(), workflowForm.getWorkflowParticipantNames(), createWorklfowMessage(record, workflowForm), (WorkflowData)record, getWorkflowSessionInfo(workflowForm, sessionInfo));
		workflowForm.setActionResult("转办完成");
		workflowForm.setWorkflowAction(null);
	}
	
	/**
	 * 增加办理人
	 * @param workflowForm
	 * @param record
	 * @param sessionInfo
	 * @throws Exception
	 */
	protected void runAddParticipants(WorkflowForm workflowForm, Record record, SessionInfo sessionInfo) throws Exception {
		getWorkflowExploitService().addParticipants(workflowForm.getWorkflowInstanceId(), "" + workflowForm.getWorkItemId(), workflowForm.isWorkflowTest(), workflowForm.getWorkflowParticipantIds(), workflowForm.getWorkflowParticipantNames(), createWorklfowMessage(record, workflowForm), (WorkflowData)record, getWorkflowSessionInfo(workflowForm, sessionInfo));
		workflowForm.setActionResult("办理人增加成功");
		workflowForm.setWorkflowAction(null);
	}
	
	/**
	 * 创建流程消息
	 * @param record
	 * @param workflowForm
	 * @return
	 * @throws Exception
	 */
	protected WorkflowMessage createWorklfowMessage(Record record, WorkflowForm workflowForm) throws Exception {
		WorkflowMessage workflowMessage = new WorkflowMessage();
		workflowMessage.setType(workflowForm.getFormDefine().getTitle()); //消息类型
		workflowMessage.setContent(StringUtils.getBeanTitle(record));
		//链接
		workflowMessage.setHref(workflowForm.getContextPath() + "/" + workflowForm.getFormDefine().getApplicationName() + "/" + workflowForm.getFormDefine().getName() + ".shtml?act=edit&id=" + workflowForm.getId());
		return workflowMessage;
	}
	
	/**
	 * 获取工作流客户端
	 * @param workflowForm
	 * @param sessionInfo
	 * @return
	 * @throws SystemUnregistException
	 * @throws Exception
	 */
	protected SessionInfo getWorkflowSessionInfo(ActionForm form, SessionInfo sessionInfo) throws SystemUnregistException {
		WorkflowForm workflowForm = (WorkflowForm)form;
		if(!workflowForm.isWorkflowTest() || workflowForm.getTestUserId()==0) { //不是测试状态
			return sessionInfo;
		}
		//获取测试用户会话信息
		SessionService sessionService = (SessionService)getService("sessionService");
		try {
			if(workflowForm.getTestUserName()==null || workflowForm.getTestUserName().equals("")) {
				//设置测试用户名
				workflowForm.setTestUserName(((PersonService)getService("personService")).getPerson(workflowForm.getTestUserId()).getLoginName());
			}
			return sessionService.getSessionInfo(workflowForm.getTestUserName());
		}
		catch (Exception e) {
			throw new Error(e.getMessage());
		}
	}
	
	/**
	 * 保存意见
	 * @param workflowForm
	 * @param request
	 * @param response
	 * @param sessionInfo
	 * @throws Exception
	 */
	protected void writeOpinion(final WorkflowForm workflowForm, HttpServletRequest request, HttpServletResponse response, final SessionInfo sessionInfo) throws Exception {
		//添加常用意见
		if(OpinionPackage.OPINION_ACTION_APPEND_OFTEN_USE.equals(workflowForm.getOpinionPackage().getOpinionAction())) { 
			OpinionService opinionService = getOpinionService();
			String opinion = workflowForm.getOpinionPackage().getOpinion();
			if(opinion!=null && !opinion.equals("")) {
				opinionService.appendOftenUseOpinion(sessionInfo.getUserId(), workflowForm.getFormDefine().getApplicationName(), opinion);
				workflowForm.getOpinionPackage().setSelectedOftenUseOpinion(opinion);
	    	}
			return;
		}
		//删除常用意见
		if(OpinionPackage.OPINION_ACTION_DELETE_OFTEN_USE.equals(workflowForm.getOpinionPackage().getOpinionAction())) {
			OpinionService opinionService = getOpinionService();
			String opinion = workflowForm.getOpinionPackage().getSelectedOftenUseOpinion();
			if(opinion!=null && !opinion.equals("")) {
				opinionService.deleteOftenUseOpinion(sessionInfo.getUserId(), workflowForm.getFormDefine().getApplicationName(), opinion);
				workflowForm.getOpinionPackage().setSelectedOftenUseOpinion(null);
	    	}
			return;
		}
		//保存意见
		final String opinion = workflowForm.getOpinionPackage().getOpinion();
	    if(opinion==null || opinion.isEmpty()) { //没有填写意见
	    	return;
	    }
	    if(workflowForm.getWorkItemId()==null || workflowForm.getWorkItemId().isEmpty()) { //不在办理状态,如“取回”
	    	return;
	    }
	    workflowForm.getOpinionPackage().setSelectedOftenUseOpinion(null);
		final OpinionService opinionService = getOpinionService();
		if(workflowForm.getBatchIds()==null || workflowForm.getBatchIds().equals("")) { //不是批量处理
			Opinion savedOpinion = opinionService.saveOpinion(
										workflowForm.getFormDefine().getRecordClassName(),
										workflowForm.getOpinionPackage().getOpinionId(),
										workflowForm.getId(), //主记录ID
										opinion, //意见内容
										workflowForm.getOpinionPackage().getOpinionType(), //意见类型
										workflowForm.getParticipantId(), //用户ID
										workflowForm.getParticipant(), //用户名
										(workflowForm.isAgent() ? sessionInfo.getUserId() : 0), //代理人ID
										(workflowForm.isAgent() ? sessionInfo.getUserName() : null), //代理人姓名
										workflowForm.getActivityId(), //环节ID
										workflowForm.getActivityName(),
										workflowForm.getWorkItemId(), //工作项ID
										null);
			workflowForm.getOpinionPackage().setOpinionId(savedOpinion.getId());
			getWorkflowExploitService().completeAction("" + workflowForm.getWorkflowInstanceId(), workflowForm.getWorkItemId(), "填写" + workflowForm.getOpinionPackage().getOpinionType() + "意见", getWorkflowSessionInfo(workflowForm, sessionInfo));
		}
		else {
			doBatch(workflowForm, request, response, sessionInfo, new BatchCallback() {
				public void run(Record record, WorkflowInterface workflowInterface) throws Exception {
					Opinion savedOpinion = opinionService.saveOpinion(workflowForm.getFormDefine().getRecordClassName(),
							   workflowForm.getOpinionPackage().getOpinionId(),
							   workflowForm.getId(), //主记录ID
							   opinion, //意见内容
							   workflowForm.getOpinionPackage().getOpinionType(), //意见类型
							   workflowForm.getParticipantId(), //用户ID
							   workflowForm.getParticipant(), //用户名
							   (workflowForm.isAgent() ? sessionInfo.getUserId() : 0), //代理人ID
							   (workflowForm.isAgent() ? sessionInfo.getUserName() : null), //代理人姓名
							   workflowForm.getActivityId(), //环节ID
							   workflowForm.getActivityName(),
							   workflowForm.getWorkItemId(), //工作项ID
							   null);
					workflowForm.getOpinionPackage().setOpinionId(savedOpinion.getId());
					getWorkflowExploitService().completeAction("" + workflowForm.getWorkflowInstanceId(), workflowForm.getWorkItemId(), "填写" + workflowForm.getOpinionPackage().getOpinionType() + "意见", getWorkflowSessionInfo(workflowForm, sessionInfo));
				}
			});
		}
    }
	
	/**
	 * 执行批处理
	 * @param workflowForm
	 * @param request
	 * @param response
	 * @param callback
	 */
	private void doBatch(WorkflowForm workflowForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo, BatchCallback callback) {
		String activityId = workflowForm.getActivityId();
		String workflowId = workflowForm.getWorkflowId();
		String[] ids = workflowForm.getBatchIds().split(",");
		for(int i=ids.length - 1; i>=0; i--) {
			try {
				request.removeAttribute("workflowInterface");
				workflowForm.setWorkItemId(null);
				workflowForm.setId(Long.parseLong(ids[i]));
				workflowForm.getOpinionPackage().setOpinion(null); //清空意见,由意见服务重新加载
				Record record = load(workflowForm, request, response);
				WorkflowInterface workflowInterface = (WorkflowInterface)request.getAttribute("workflowInterface");
				if(workflowInterface.isLocked()) {
					if(workflowId.equals(workflowForm.getWorkflowId()) && activityId.equals(workflowForm.getActivityId())) { //检查流程ID、环节ID是否相同
						callback.run(record, workflowInterface);
					}
					unlock(workflowForm, request, OPEN_MODE_EDIT, sessionInfo);
				}
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
	}
	
	/**
	 * 批处理回调
	 * @author linchuan
	 *
	 */
	private interface BatchCallback {
		//执行
		public void run(Record record, WorkflowInterface workflowInterface) throws Exception;
	}
	
	/**
	 * 获取意见服务
	 * @return
	 * @throws SystemUnregistException
	 */
	protected OpinionService getOpinionService() throws SystemUnregistException {
		return (OpinionService)getService("opinionService");
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#executeLoadAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward executeLoadAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		WorkflowForm workflowForm = (WorkflowForm)form;
		if(!"viewWorkflowInstance".equals(workflowForm.getWorkflowAction())) { 
			return super.executeLoadAction(mapping, form, request, response);
		}
		//查看流程实例图
		try {
			SessionInfo sessionInfo = getSessionInfo(request, response); //会话检查
			Form formDefine = loadFormDefine(workflowForm, request);
			WorkflowData record = (WorkflowData)loadRecord(workflowForm, formDefine, workflowForm.getId(), sessionInfo, request); //加载记录
			List acl = getAcl(formDefine.getApplicationName(), form, record, OPEN_MODE_EDIT, sessionInfo);
			char accessLevel = checkLoadPrivilege(workflowForm, request, record, OPEN_MODE_EDIT, acl, sessionInfo);
			if(accessLevel<RecordControlService.ACCESS_LEVEL_READONLY) {
				throw new PrivilegeException();
			}
			WorkflowInterface workflowInterface = getWorkflowInterface(workflowForm, request, record, OPEN_MODE_EDIT, sessionInfo);
			String passport = getWorkflowExploitService().createWorkflowInstanceViewPassport(record.getWorkflowInstanceId(), sessionInfo);
			String url = request.getContextPath() + "/workflow/workflowInstanceViewer.shtml" +
						 "?passport=" + passport + 
						 "&currentWorkItemId=" + workflowInterface.getWorkItemId() +
						 "&seq=" + UUIDLongGenerator.generateId();
			response.sendRedirect(url);
			return null;
		}
		catch(Exception e) {
			return transactException(e, mapping, form, request, response, false);
		}
	}

	/**
	 * 执行流程操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param isPrompted 是否使用自己的提示信息
	 * @param actionResult
	 * @param forwardName 指定跳转页面
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeRunAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response, boolean isPrompted, String actionResult, String forwardName) throws Exception {
		if(isSecureAction && !isSecureURL(request)){
			throw new Exception();
		}
		WorkflowForm workflowForm = (WorkflowForm)form;
		if(isPrompted) {
			workflowForm.setPrompted(isPrompted);
		}
		try {
			run(mapping, form, request, response, actionResult);
		}
    	catch(Exception e) {
        	workflowForm.setWorkflowAction(null);
        	return transactException(e, mapping, form, request, response, true);
        }
        if(workflowForm.getWorkflowAction()!=null) { //还有需要执行的流程操作
        	return mapping.getInputForward();
        }
        else { //流程操作执行完毕
        	return mapping.findForward(forwardName==null ? "result" : forwardName);
        }
    }
	
	/**
	 * 执行提交意见操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward executeWriteOpinionAction(ActionMapping mapping, org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(isSecureAction && !isSecureURL(request)){
			throw new Exception();
		}
		try {
			WorkflowForm workflowForm = (WorkflowForm)form;
			SessionInfo sessionInfo = getSessionInfo(request, response);
			save(mapping, form, request, response, true, "opinion", null);
	    	writeOpinion(workflowForm, request, response, sessionInfo);
			//重新加载表单,以显示新意见列表
			load(form, request, response);
    	}
		catch(Exception e) {
        	return transactException(e, mapping, form, request, response, true);
        }
    	return mapping.getInputForward();
	}
}