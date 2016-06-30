package com.yuanluesoft.jeaf.application.builder.model.formtemplate;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.jeaf.application.builder.actions.standardforms.normal.NormalFormAction;
import com.yuanluesoft.jeaf.application.builder.model.Form;
import com.yuanluesoft.jeaf.application.builder.model.StrutsAction;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationForm;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationView;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.model.FormAction;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 普通表单模板
 * @author linchuan
 *
 */
public class FormTemplate {

	/**
	 * 获取名称
	 * @return
	 */
	public String getName() {
		return "normal";
	}
	
	/**
	 * 获取标题
	 * @return
	 */
	public String getLabel() {
		return "普通";
	}
	
	/**
	 * 获取记录父类
	 * @return
	 */
	public Class getParentRecordClass() {
		return Record.class;
	}

	/**
	 * 是否一个应用只能有一个同类型的表单
	 * @return
	 */
	public boolean isUniqueInApplicaton() {
		return false;
	}

	/**
	 * 获取Struts操作类包名称
	 * @return
	 */
	protected String getStrutsActionPackageName() {
		return NormalFormAction.class.getPackage().getName();
	}
	
	/**
	 * 获取父表单类名称
	 * @return
	 */
	protected Class getParentFormClass() {
		return ActionForm.class;
	}
	
	/**
	 * 获取表单(Form)列表,用于后台管理的放到列表的最后
	 * @param applicationForm
	 * @param applicationName
	 * @param applicationPackageName
	 * @param recordClassName
	 * @param jspPathPrefix 如 /../..
	 * @param editPrivilege
	 * @param visitPrivilege
	 * @param deletePrivilege
	 * @return
	 */
	public List getForms(ApplicationForm applicationForm, String applicationName, String applicationPackageName, String recordClassName, String jspPathPrefix, String editPrivilege, String visitPrivilege, String deletePrivilege) {
		Form form = new Form();
		form.setParentClass(getParentFormClass()); //父类
		form.setStrutsFormBeanName(applicationForm.getEnglishName()); //Struts表单名称
		form.setApplicationName(applicationName); //系统名称
		String formEnglishName = applicationForm.getEnglishName();
		String upperFormEnglishName = StringUtils.capitalizeFirstLetter(applicationForm.getEnglishName());
		form.setName(formEnglishName); //表单名称
		form.setTitle(applicationForm.getName()); //标题
		form.setClassName(applicationPackageName + ".forms." + upperFormEnglishName); //表单类名称
		form.setRecordClassName(recordClassName); //数据库映射类名称
		//form.setMethod("get"); //表单方法,默认为get
		//form.setAction(action); //表单操作
		form.setActions(new ArrayList()); //表单操作列表
		form.getActions().add(new FormAction("关闭", null, null, null, "window.close()", false));
		form.getActions().add(new FormAction("保存", null, null, "isRead()", "FormUtils.submitForm()", true));
		form.getActions().add(new FormAction("删除", null, null, "deleteDisable()", "if(confirm('删除后不可恢复，是否确定要删除？'))FormUtils.doAction('delete" + upperFormEnglishName + "');", true, false, false, null, false, false));
		//STRUTS操作列表
		String jspFile = "/" + applicationForm.getEnglishName() + ".jsp";
		form.setStrutsActions(new ArrayList());
		String packageName = getStrutsActionPackageName();
		form.getStrutsActions().add(new StrutsAction("/" + formEnglishName, formEnglishName, packageName + ".Load", null, "load", jspFile));
		form.getStrutsActions().add(new StrutsAction("/save" + upperFormEnglishName, formEnglishName, packageName + ".Save", jspFile, null, null));
		form.getStrutsActions().add(new StrutsAction("/delete" + upperFormEnglishName, formEnglishName, packageName + ".Delete", jspFile, null, null));
		form.getStrutsActions().add(new StrutsAction("/" + formEnglishName + "AttachmentEditor", formEnglishName, packageName + ".SelectAttachment", null, "load", jspPathPrefix + "/jeaf/attachment/attachment.jsp"));
		form.getStrutsActions().add(new StrutsAction("/select" + upperFormEnglishName + "Attachment", formEnglishName, packageName + ".SelectAttachment", null, "load", jspPathPrefix + "/jeaf/attachment/selectAttachment.jsp"));
		//处理编辑权限
		if(editPrivilege!=null && !editPrivilege.isEmpty()) {
			form.setExtendedParameter("editPrivilege", editPrivilege);
		}
		//处理访问权限
		if(visitPrivilege!=null && !visitPrivilege.isEmpty()) {
			form.setExtendedParameter("visitPrivilege", visitPrivilege);
		}
		//处理删除权限
		if(deletePrivilege!=null && !deletePrivilege.isEmpty()) {
			form.setExtendedParameter("deletePrivilege", deletePrivilege);
		}
		return ListUtils.generateList(form);
	}

	/**
	 * 创建JSP页面
	 * @param applicationForm
	 * @param applicationName
	 * @param editSubFormJsp
	 * @param readSubFormJsp
	 */
	public void createJspFiles(ApplicationForm applicationForm, String applicationName, String editSubFormJsp, String readSubFormJsp) {
		String jsp = "<%@ page contentType=\"text/html; charset=UTF-8\" %>\n" +
					 "<%@ taglib uri=\"/WEB-INF/struts-ext\" prefix=\"ext\" %>\n";
		String body = "\n" +
					  "<ext:form action=\"/save" + StringUtils.capitalizeFirstLetter(applicationForm.getEnglishName()) + "\">\n" +
					  "	" + (isWorkflowSupport() ? "<ext:tab/>" : "<ext:subForm/>") + "\n" +
					  "</ext:form>";
		String savePath = FileUtils.createDirectory(Environment.getWebAppPath() + applicationName);
		FileUtils.saveStringToFile(savePath + applicationForm.getEnglishName() + ".jsp", jsp + body, "utf-8", false);
		
		//生成子表单
		FileUtils.saveStringToFile(savePath + applicationForm.getEnglishName() + "Edit.jsp", jsp + editSubFormJsp, "utf-8", false);
		FileUtils.saveStringToFile(savePath + applicationForm.getEnglishName() + "Read.jsp", jsp + readSubFormJsp, "utf-8", false);
	}

	/**
	 * 生成默认的视图(ApplicationView)列表
	 * @param applicationForm
	 * @param viewFieldIds
	 * @param viewFieldNames
	 * @param sortFieldIds
	 * @param sortFieldNames
	 * @param sortFieldDirections
	 * @return
	 */
	public List generateApplicationViews(ApplicationForm applicationForm, String viewFieldIds, String viewFieldNames, String sortFieldIds, String sortFieldNames, String sortFieldDirections) {
		ApplicationView view = new ApplicationView();
		view.setId(UUIDLongGenerator.generateId()); //ID
		view.setFormId(applicationForm.getId()); //表单ID
		view.setName(applicationForm.getName()); //视图名称
		view.setEnglishName(applicationForm.getEnglishName()); //视图英文名称
		view.setViewFieldIds(viewFieldIds); //视图字段ID
		view.setViewFieldNames(viewFieldNames); //视图字段名称
		view.setSortFieldIds(sortFieldIds); //排序字段ID
		view.setSortFieldNames(sortFieldNames); //排序字段名称
		view.setSortFieldDirections(sortFieldDirections); //排序字段排序方式,asc/desc
		return ListUtils.generateList(view);
	}
	
	/**
	 * 重试视图列表
	 * @param applicationForm
	 * @param applicationName
	 * @param recordClassName
	 * @return
	 */
	public void resetViews(List views, ApplicationForm applicationForm, String applicationName, String recordClassName) {
		
	}
	
	/**
	 * 重设导航题目
	 * @param navigatorDefinition
	 */
	public void resetNavigatorItems(List navigatorItems, ApplicationForm applicationForm, String applicationName) {
		
	}
	
	/**
	 * 生成站点页面(SitePage)列表
	 * @param applicationForm
	 * @param applicationName
	 * @param recordClassName
	 * @return
	 */
	public List generateSitePages(ApplicationForm applicationForm, String applicationName, String recordClassName) {
		return null;
	}
	
	/**
	 * 是否支持工作流
	 * @return
	 */
	public boolean isWorkflowSupport() {
		return WorkflowData.class.isAssignableFrom(getParentRecordClass());
	}
	
	/**
	 * 是否在EAI中配置流程
	 * @return
	 */
	public boolean isWorkflowConfigureInEAI() {
		return false;
	}
}