package com.yuanluesoft.cms.capture.actions.task;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.cms.base.actions.SiteApplicationConfigAction;
import com.yuanluesoft.cms.capture.forms.Task;
import com.yuanluesoft.cms.capture.model.SourcePage;
import com.yuanluesoft.cms.capture.pojo.CmsCaptureTask;
import com.yuanluesoft.cms.capture.service.CaptureService;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.Tab;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class TaskAction extends SiteApplicationConfigAction {
	protected boolean loadPage = true; //是否需要加载页面

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		Task taskForm = (Task)form;
		CmsCaptureTask task = (CmsCaptureTask)record;
		taskForm.getTabs().addTab(-1, "basic", "基本信息", "taskBasic.jsp", true);
		if(task!=null && task.getCaptureURL()!=null && !task.getCaptureURL().equals("")) {
			boolean nextStep = request.getRequestURI().indexOf("nextStep")!=-1;
			taskForm.getTabs().addTab(-1, "listRule", "列表配置", "taskListRule.jsp", nextStep);
			//检查是否已经下载了正文页面,如果有显示字段配置页签
			if(task.getFields()!=null && !task.getFields().isEmpty()) {
				taskForm.getTabs().addTab(-1, "fieldRule", "字段配置", "taskFieldRule.jsp", nextStep);
				//删除"下一步"按钮
				ListUtils.removeObjectByProperty(taskForm.getFormActions(), "title", "下一步");
			}
		}
		if(task==null || task.getFields()==null || task.getFields().isEmpty()) {
			//删除"抓取测试"、"启动抓取"按钮
			ListUtils.removeObjectByProperty(taskForm.getFormActions(), "title", "抓取测试");
			ListUtils.removeObjectByProperty(taskForm.getFormActions(), "title", "启动抓取");
		}
		//设置扩展配置的jsp路径
		String businessClassName = taskForm.getBusinessClassName();
		if((businessClassName==null || businessClassName.equals("")) && task!=null) {
			businessClassName = task.getBusinessClassName();
		}
		if(businessClassName!=null && !businessClassName.equals("")) {
			BusinessDefineService businessDefineService = (BusinessDefineService)getService("businessDefineService");
			BusinessObject businessObject = businessDefineService.getBusinessObject(businessClassName);
			String captureConfigure = (String)businessObject.getExtendParameter("captureConfigure");
			if(captureConfigure!=null) {
				taskForm.getTabs().addTab(1, "extendConfigure", "参数配置", captureConfigure, false);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#initForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void initForm(ActionForm form, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.initForm(form, acl, sessionInfo, request, response);
		Task task = (Task)form;
		task.setCreated(DateTimeUtils.now());
		task.setCreator(sessionInfo.getUserName());
		task.setCaptureTime("02:30");
		task.setCaptureInterval(60);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		if(!loadPage) {
			return;
		}
		Task taskForm = (Task)form;
		CmsCaptureTask task = (CmsCaptureTask)record;
		CaptureService captureService = (CaptureService)getService("captureService");
		//获取列表页面的URL和html内容
		SourcePage sourcePage = captureService.loadSourcePage(task);
		if(sourcePage==null) {
			return;
		}
		taskForm.setListPageURL(sourcePage.getListPageURL());
		taskForm.setListPageHtml(sourcePage.getListPageHTML());
		taskForm.setContentPageURL(sourcePage.getContentPageURL());
		taskForm.setContentPageHtml(sourcePage.getContentPageHTML());
		//加载字段配置
		taskForm.setListPageFields(captureService.loadCaptureFieldConfig(task, true));
		if(sourcePage.getContentPageURL()!=null) { //内容页面已加载
			//加载字段配置
			taskForm.setContentPageFields(captureService.loadCaptureFieldConfig(task, false));
		}
		else { //内容页面未加载
			Tab lastTab = (Tab)taskForm.getTabs().get(taskForm.getTabs().size()-1);
			if(lastTab.getId().equals("fieldRule")) {
				taskForm.getTabs().remove(taskForm.getTabs().size()-1);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		CmsCaptureTask task = (CmsCaptureTask)record;
		//保存字段配置
		CaptureService captureService = (CaptureService)getService("captureService");
		captureService.saveCaptureFieldConfig(task, request);
		return record;
	}
}