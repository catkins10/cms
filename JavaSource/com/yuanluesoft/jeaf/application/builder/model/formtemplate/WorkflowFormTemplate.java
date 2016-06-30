package com.yuanluesoft.jeaf.application.builder.model.formtemplate;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.jeaf.application.builder.actions.standardforms.workflow.WorkflowFormAction;
import com.yuanluesoft.jeaf.application.builder.model.Form;
import com.yuanluesoft.jeaf.application.builder.model.StrutsAction;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationField;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationForm;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationView;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 工作流表单模板
 * @author linchuan
 *
 */
public class WorkflowFormTemplate extends FormTemplate {

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.FormTemplate#getName()
	 */
	public String getName() {
		return "workflow";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.FormTemplate#getLabel()
	 */
	public String getLabel() {
		return "工作流";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.FormTemplate#getParentRecordClass()
	 */
	public Class getParentRecordClass() {
		return WorkflowData.class;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.FormTemplate#isUniqueInApplicaton()
	 */
	public boolean isUniqueInApplicaton() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.formtemplate.FormTemplate#getStrutsActionPackageName()
	 */
	protected String getStrutsActionPackageName() {
		return WorkflowFormAction.class.getPackage().getName();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.formtemplate.FormTemplate#getParentFormClass()
	 */
	protected Class getParentFormClass() {
		return WorkflowForm.class;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.formtemplate.NormalFormTemplate#getForms(com.yuanluesoft.jeaf.application.builder.pojo.ApplicationForm, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List getForms(ApplicationForm applicationForm, String applicationName, String applicationPackageName, String recordClassName, String jspPathPrefix, String editPrivilege, String visitPrivilege, String deletePrivilege) {
		List forms = super.getForms(applicationForm, applicationName, applicationPackageName, recordClassName, jspPathPrefix, editPrivilege, visitPrivilege, deletePrivilege);
		String formEnglishName = applicationForm.getEnglishName();
		String upperFormEnglishName = StringUtils.capitalizeFirstLetter(applicationForm.getEnglishName());
		Form form = (Form)forms.get(0);
		//STRUTS操作列表
		String jspFile = "/" + applicationForm.getEnglishName() + ".jsp";
		String packageName = getStrutsActionPackageName();
		form.getStrutsActions().add(new StrutsAction("/run" + upperFormEnglishName, formEnglishName, packageName + ".Run", jspFile, null, null));
		form.getStrutsActions().add(new StrutsAction("/writeOpinion", formEnglishName, packageName + ".WriteOpinion", jspFile, null, null));
		return forms;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.FormTemplate#generateDefaultViews(com.yuanluesoft.jeaf.application.builder.pojo.ApplicationForm, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List generateApplicationViews(ApplicationForm applicationForm, String viewFieldIds, String viewFieldNames, String sortFieldIds, String sortFieldNames, String sortFieldDirections) {
		String[][] filterModes = {{"TODO", "待办", "todo"}, {"INPROCESS", "在办", "inprocess"}, {"COMPLETED", "办结", "completed"}, {"READABLE", "所有", "all"}};
		List views = new ArrayList();
		for(int i=0; i<filterModes.length; i++) {
			ApplicationView view = new ApplicationView();
			view.setId(UUIDLongGenerator.generateId()); //ID
			view.setFormId(applicationForm.getId()); //表单ID
			view.setName(filterModes[i][1] + applicationForm.getName()); //视图名称
			view.setEnglishName(filterModes[i][2] + StringUtils.capitalizeFirstLetter(applicationForm.getEnglishName())); //视图英文名称
			//自动添加发送时间、状态等字段
			String fieldIds = viewFieldIds;
			String fieldNames = viewFieldNames;
			String[] toAppend = null;
			if("TODO".equals(filterModes[i][0])) {
				toAppend = new String[] {"workflowSendTime", "workflowSender", "workflowStatus"};
			}
			else if("INPROCESS".equals(filterModes[i][0])) {
				toAppend = new String[] {"workflowSendTime", "workflowParticipants", "workflowStatus"};
			}
			for(int j=0; j<(toAppend==null ? 0 : toAppend.length); j++) {
				ApplicationField field = (ApplicationField)ListUtils.findObjectByProperty(applicationForm.getFields(), "englishName", toAppend[j]);
				if(field!=null && ("," + fieldIds + ",").indexOf("," + field.getId() + ",")==-1) {
					fieldIds += "," + field.getId();
					fieldNames += "," + field.getName();
				}
			}
			view.setViewFieldIds(fieldIds); //视图字段ID
			view.setViewFieldNames(fieldNames); //视图字段名称
			view.setSortFieldIds(sortFieldIds); //排序字段ID
			view.setSortFieldNames(sortFieldNames); //排序字段名称
			view.setSortFieldDirections(sortFieldDirections); //排序字段排序方式,asc/desc
			view.setFilterMode(filterModes[i][0]); //过滤方式
			views.add(view);
		}
		return views;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.formtemplate.FormTemplate#isWorkflowConfigureInEAI()
	 */
	public boolean isWorkflowConfigureInEAI() {
		return true;
	}
}