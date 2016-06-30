package com.yuanluesoft.jeaf.application.builder.model.formtemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.publicservice.forms.PublicServiceAdminForm;
import com.yuanluesoft.cms.publicservice.forms.PublicServiceForm;
import com.yuanluesoft.cms.publicservice.pojo.PublicService;
import com.yuanluesoft.jeaf.application.builder.actions.standardforms.publicservice.PublicServiceFormAction;
import com.yuanluesoft.jeaf.application.builder.model.Form;
import com.yuanluesoft.jeaf.application.builder.model.StrutsAction;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationForm;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationView;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.form.model.FormAction;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.view.model.Link;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class PublicServiceFormTemplate extends WorkflowFormTemplate {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.NormalFormTemplate#getName()
	 */
	public String getName() {
		return "publicService";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.NormalFormTemplate#getLabel()
	 */
	public String getLabel() {
		return "公众服务";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.NormalFormTemplate#getParentRecordClass()
	 */
	public Class getParentRecordClass() {
		return PublicService.class;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.formtemplate.NormalFormTemplate#getForms(com.yuanluesoft.jeaf.application.builder.pojo.ApplicationForm, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List getForms(ApplicationForm applicationForm, String applicationName, String applicationPackageName, String recordClassName, String jspPathPrefix, String editPrivilege, String visitPrivilege, String deletePrivilege) {
		String formEnglishName = applicationForm.getEnglishName();
		String upperFormEnglishName = StringUtils.capitalizeFirstLetter(applicationForm.getEnglishName());
	
		//外部表单
		Form form = new Form();
		form.setParentClass(PublicServiceForm.class); //父类
		form.setStrutsFormBeanName(applicationForm.getEnglishName()); //Struts表单名称
		form.setApplicationName(applicationName); //系统名称
		form.setName(formEnglishName); //表单名称
		form.setTitle(applicationForm.getName()); //标题
		form.setClassName(applicationPackageName + ".forms." + upperFormEnglishName); //表单类名称
		form.setRecordClassName(recordClassName); //数据库映射类名称
		form.setMethod("post"); //表单方法,默认为get
		form.setAction("/" + applicationName + "/save" + upperFormEnglishName + ".shtml"); //表单操作
		form.setActions(new ArrayList()); //表单操作列表
		form.getActions().add(new FormAction("提交", "submit", null, null, null, true));
		//STRUTS操作列表
		String jspFile = "/" + applicationForm.getEnglishName() + ".jsp";
		form.setStrutsActions(new ArrayList());
		String packageName = PublicServiceFormAction.class.getPackage().getName();
		form.getStrutsActions().add(new StrutsAction("/" + formEnglishName, formEnglishName, packageName + ".Load", null, "load", jspFile));
		form.getStrutsActions().add(new StrutsAction("/save" + upperFormEnglishName, formEnglishName, packageName + ".Save", jspFile, "result", "/" + applicationForm.getEnglishName() + "Submitted.jsp"));
		form.getStrutsActions().add(new StrutsAction("/" + formEnglishName + "AttachmentEditor", formEnglishName, packageName + ".SelectAttachment", null, "load", jspPathPrefix + "/jeaf/attachment/attachment.jsp"));
		form.getStrutsActions().add(new StrutsAction("/select" + upperFormEnglishName + "Attachment", formEnglishName, packageName + ".SelectAttachment", null, "load", jspPathPrefix + "/jeaf/attachment/selectAttachment.jsp"));
		
		//搜索表单
		Form searchForm = new Form();
		searchForm.setParentClass(null); //父类
		searchForm.setApplicationName(applicationName); //系统名称
		searchForm.setName(formEnglishName + "Search"); //表单名称
		searchForm.setTitle("搜索"); //标题
		searchForm.setClassName(null); //表单类名称
		searchForm.setRecordClassName(recordClassName); //数据库映射类名称
		searchForm.setMethod("get"); //表单方法,默认为get
		searchForm.setAction("/cms/sitemanage/applicationSearch.shtml?applicationName=" + applicationName + "&pageName=" + formEnglishName + "Search"); //表单操作
		searchForm.setActions(new ArrayList()); //表单操作列表
		searchForm.getActions().add(new FormAction("搜索", "submit", null, null, null, true));
		searchForm.setExtendedParameter("staticPageSupport", "true");
		
		//办理情况查询表单
		Form approvalQueryForm = new Form();
		approvalQueryForm.setParentClass(null); //父类
		approvalQueryForm.setApplicationName(applicationName); //系统名称
		approvalQueryForm.setName("approvalQuery"); //表单名称
		approvalQueryForm.setTitle("办理情况查询"); //标题
		approvalQueryForm.setClassName(null); //表单类名称
		approvalQueryForm.setRecordClassName(null); //数据库映射类名称
		approvalQueryForm.setMethod("post"); //表单方法,默认为post
		approvalQueryForm.setAction("/" + applicationName + "/" + formEnglishName + ".shtml?id={PARAMETER:id}"); //表单操作
		approvalQueryForm.setActions(new ArrayList()); //表单操作列表
		approvalQueryForm.getActions().add(new FormAction("查询", "submit", null, null, null, true));
		approvalQueryForm.setExtendedParameter("staticPageSupport", "true");
		approvalQueryForm.setFields(new ArrayList()); //字段列表
		approvalQueryForm.getFields().add(new Field("approvalQuerySN", "编号", null, null, "text", false, false));
		approvalQueryForm.getFields().add(new Field("approvalQueryPassword", "密码", null, null, "password", false, false));
		
		//内部表单
		Form adminForm = new Form();
		adminForm.setParentClass(PublicServiceAdminForm.class); //父类
		String formBeanName = "admin" + upperFormEnglishName;
		adminForm.setStrutsFormBeanName(formBeanName); //Struts表单名称
		adminForm.setApplicationName(applicationName); //系统名称
		adminForm.setName("admin/" + formEnglishName); //表单名称
		adminForm.setTitle(applicationForm.getName()); //标题
		adminForm.setClassName(applicationPackageName + ".forms.admin." + upperFormEnglishName); //表单类名称
		adminForm.setRecordClassName(recordClassName); //数据库映射类名称
		//adminForm.setMethod("get"); //表单方法,默认为get
		//adminForm.setAction(action); //表单操作
		adminForm.setActions(new ArrayList()); //表单操作列表
		adminForm.getActions().add(new FormAction("关闭", null, null, null, "window.close()", false));
		adminForm.getActions().add(new FormAction("保存", null, null, "isRead()", "FormUtils.submitForm()", true));
		adminForm.getActions().add(new FormAction("批转", null, null, "workflow()", "FormUtils.doAction('run" + upperFormEnglishName + "', 'workflowAction=send')", false, true, false, null, false, false));
		adminForm.getActions().add(new FormAction("公布到网站", null, null, "workflow() and noPrivilege(application_manager) and noPrivilege(site_manager)", "FormUtils.doAction('setPublishOption')", false));
		adminForm.getActions().add(new FormAction("删除", null, null, "deleteDisable()", "if(confirm('是否确定要删除？'))FormUtils.doAction('delete" + upperFormEnglishName + "');", true, false, false, null, false, false));
		adminForm.getActions().add(new FormAction("永久删除", null, null, "isNew()", "if(confirm('删除后不可恢复，是否确定要删除？'))FormUtils.doAction('deleteComplaint', 'physical=true');", false));
		adminForm.getActions().add(new FormAction("撤销删除", null, null, "isNew()", "FormUtils.doAction('undeleteComplaint')", false));
		adminForm.getActions().add(new FormAction("打印办理单", null, null, null, "window.open('print" + upperFormEnglishName + ".shtml?id={PARAMETER:id}&amp;siteId={PARAMETER:siteId}');", false));
		//STRUTS操作列表
		jspFile = "/admin/" + applicationForm.getEnglishName() + ".jsp";
		adminForm.setStrutsActions(new ArrayList());
		packageName = com.yuanluesoft.jeaf.application.builder.actions.standardforms.publicservice.admin.PublicServiceFormAction.class.getPackage().getName();
		adminForm.getStrutsActions().add(new StrutsAction("/admin/" + formEnglishName, formBeanName, packageName + ".Load", null, "load", jspFile));
		adminForm.getStrutsActions().add(new StrutsAction("/admin/save" + upperFormEnglishName, formBeanName, packageName + ".Save", jspFile, null, null));
		adminForm.getStrutsActions().add(new StrutsAction("/admin/delete" + upperFormEnglishName, formBeanName, packageName + ".Delete", jspFile, null, null));
		adminForm.getStrutsActions().add(new StrutsAction("/admin/run" + upperFormEnglishName, formBeanName, packageName + ".Run", jspFile, null, null));
		adminForm.getStrutsActions().add(new StrutsAction("/admin/writeOpinion", formBeanName, packageName + ". WriteOpinion", jspFile, null, null));
		adminForm.getStrutsActions().add(new StrutsAction("/admin/setPublishOption", formBeanName, packageName + ".SetPublishOption", jspFile, null, null));
		adminForm.getStrutsActions().add(new StrutsAction("/admin/publish", formBeanName, packageName + ".Publish", jspFile, null, null));
		adminForm.getStrutsActions().add(new StrutsAction("/admin/undelete" + upperFormEnglishName, formBeanName, packageName + ".Undelete", jspFile, null, null));
		adminForm.getStrutsActions().add(new StrutsAction("/admin/print" + upperFormEnglishName, formBeanName, packageName + ".Print", null, "load", jspFile));
		//处理编辑权限
		if(editPrivilege!=null && !editPrivilege.isEmpty()) {
			adminForm.setExtendedParameter("editPrivilege", editPrivilege);
		}
		//处理访问权限
		if(visitPrivilege!=null && !visitPrivilege.isEmpty()) {
			adminForm.setExtendedParameter("visitPrivilege", visitPrivilege);
		}
		//处理删除权限
		if(deletePrivilege!=null && !deletePrivilege.isEmpty()) {
			adminForm.setExtendedParameter("deletePrivilege", deletePrivilege);
		}
		List forms = new ArrayList();
		forms.add(form);
		forms.add(searchForm);
		forms.add(approvalQueryForm);
		forms.add(adminForm);
		return forms;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.formtemplate.FormTemplate#createJspFiles(com.yuanluesoft.jeaf.application.builder.pojo.ApplicationForm, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void createJspFiles(ApplicationForm applicationForm, String applicationName, String editSubFormJsp, String readSubFormJsp) {
		String jsp = "<%@ page contentType=\"text/html; charset=UTF-8\" %>\n" +
					 "<%@ taglib uri=\"/WEB-INF/struts-ext\" prefix=\"ext\" %>\n";
		String savePath = FileUtils.createDirectory(Environment.getWebAppPath() + applicationName);
		
		//生成对外输出的表单
		String body = "\n<ext:page applicationName=\"" + applicationName + "\" namePageName=\"" + applicationForm.getEnglishName() + "\" propertyPageName=\"subForm\"/>";
		FileUtils.saveStringToFile(savePath + applicationForm.getEnglishName() + ".jsp", jsp + body, "utf-8", false);
		
		//生成表单提交完成页面
		body = "\n<ext:page applicationName=\"" + applicationName + "\" pageName=\"" + applicationForm.getEnglishName() + "Submitted\"/>";
		FileUtils.saveStringToFile(savePath + applicationForm.getEnglishName() + "Submitted.jsp", jsp + body, "utf-8", false);
		
		//生成后台页面
		body = "\n" +
			   "<ext:form action=\"/admin/save" + StringUtils.capitalizeFirstLetter(applicationForm.getEnglishName()) + "\">\n" +
			   "	<ext:tab/>" + "\n" +
			   "</ext:form>";
		savePath = FileUtils.createDirectory(Environment.getWebAppPath() + applicationName + "/admin");
		FileUtils.saveStringToFile(savePath + applicationForm.getEnglishName() + ".jsp", jsp + body, "utf-8", false);
		
		//生成子表单
		FileUtils.saveStringToFile(savePath + applicationForm.getEnglishName() + "Edit.jsp", jsp + editSubFormJsp, "utf-8", false);
		FileUtils.saveStringToFile(savePath + applicationForm.getEnglishName() + "Read.jsp", jsp + readSubFormJsp, "utf-8", false);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.formtemplate.WorkflowFormTemplate#generateApplicationViews(com.yuanluesoft.jeaf.application.builder.pojo.ApplicationForm, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List generateApplicationViews(ApplicationForm applicationForm, String viewFieldIds, String viewFieldNames, String sortFieldIds, String sortFieldNames, String sortFieldDirections) {
		List views = super.generateApplicationViews(applicationForm, viewFieldIds, viewFieldNames, sortFieldIds, sortFieldNames, sortFieldDirections);
		for(Iterator iterator = views.iterator(); iterator.hasNext();) {
			ApplicationView view = (ApplicationView)iterator.next();
			view.setEnglishName("admin/" + view.getEnglishName());
		}
		ApplicationView view = new ApplicationView();
		view.setId(UUIDLongGenerator.generateId()); //ID
		view.setFormId(applicationForm.getId()); //表单ID
		view.setName("回收站"); //视图名称
		view.setEnglishName("admin/deleted" + StringUtils.capitalizeFirstLetter(applicationForm.getEnglishName())); //视图英文名称
		view.setAccessPrivilege("{管理员}");
		view.setViewFieldIds(viewFieldIds); //视图字段ID
		view.setViewFieldNames(viewFieldNames); //视图字段名称
		view.setSortFieldIds(sortFieldIds); //排序字段ID
		view.setSortFieldNames(sortFieldNames); //排序字段名称
		view.setSortFieldDirections(sortFieldDirections); //排序字段排序方式,asc/desc
		view.setFilterMode(null); //过滤方式
		view.setHqlWhere("isDeleted=1"); //WHERE条件
		views.add(view);
		return views;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.formtemplate.FormTemplate#resetViews(java.util.List, com.yuanluesoft.jeaf.application.builder.pojo.ApplicationForm, java.lang.String, java.lang.String)
	 */
	public void resetViews(List views, ApplicationForm applicationForm, String applicationName, String recordClassName) {
		for(Iterator iterator = views.iterator(); iterator.hasNext();) {
			View view = (View)iterator.next();
			view.setActions(null); //清空操作列表
		}
		String shortPojoClassName = recordClassName.substring(recordClassName.lastIndexOf('.') + 1);
		View view = new View();
		view.setApplicationName(applicationName);
		view.setName(applicationForm.getEnglishName() + (applicationForm.getEnglishName().endsWith("s") || applicationForm.getEnglishName().endsWith("x") ? "es" : "s")); //视图名称
		view.setTitle(applicationForm.getName()); //标题
		view.setPojoClassName(recordClassName); //记录类名称
		view.setWhere(shortPojoClassName + ".siteId={PARAMETER:siteId} and " + shortPojoClassName + ".publicPass='1' and " + shortPojoClassName + ".isDeleted=0");
		view.setOrderBy(shortPojoClassName + "created DESC");
		view.setQuickFilter(shortPojoClassName + ".subject like '%{KEY}%' or " + shortPojoClassName + ".creator like '%{KEY}%'");
		view.setNewestCheckBy("created");
		view.setLinks(new ArrayList());
		Link link = new Link(applicationForm.getName(), "/" + applicationName + "/" + applicationForm.getEnglishName() + ".shtml");
		view.getLinks().add(link);
		link = new Link(null, "//cms/sitemanage/applicationIndex.shtml?applicationName=" + applicationName);
		link.setType("hostLink");
		view.getLinks().add(link);
		view.setExtendParameter("templateExtendURL", "/cms/publicservice/templatemanage/insertPublicServices.shtml");
		views.add(view);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.formtemplate.FormTemplate#generateSitePages(com.yuanluesoft.jeaf.application.builder.pojo.ApplicationForm, java.lang.String, java.lang.String)
	 */
	public List generateSitePages(ApplicationForm applicationForm, String applicationName, String recordClassName) {
		List sitePages = new ArrayList();
		String formEnglishName = applicationForm.getEnglishName();
		String upperFormEnglishName = StringUtils.capitalizeFirstLetter(applicationForm.getEnglishName());
		sitePages.add(new SitePage(formEnglishName, "填写" + applicationForm.getName(), null, "/" + applicationName + "/" + formEnglishName + ".shtml", true, false, null, false, false, false, true));
		sitePages.add(new SitePage(formEnglishName + "Submitted", applicationForm.getName() + "提交成功", null, null, false, false, null, false, false, false, false));
		sitePages.add(new SitePage(formEnglishName + "Failed", applicationForm.getName() + "查询失败", null, null, false, false, null, false, false, false, false));
		sitePages.add(new SitePage("poor" + upperFormEnglishName, applicationForm.getName() + "查看(公开主题)", recordClassName, null, false, false, null, false, false, false, false));
		sitePages.add(new SitePage("original" + upperFormEnglishName, applicationForm.getName() + "查看(公开正文)", recordClassName, null, false, false, null, false, false, false, false));
		sitePages.add(new SitePage("processing" + upperFormEnglishName, applicationForm.getName() + "查看(公开办理情况)", recordClassName, null, false, false, null, false, false, false, false));
		sitePages.add(new SitePage("fully" + upperFormEnglishName, applicationForm.getName() + "查看(完全公开)", recordClassName, null, false, false, null, false, false, false, false));
		sitePages.add(new SitePage(formEnglishName + "Print", "打印", recordClassName, null, false, false, null, false, false, false, false));
		sitePages.add(new SitePage(formEnglishName + "Search", "搜索", null, "/cms/sitemanage/applicationSearch.shtml?applicationName=" + applicationName + "&pageName=" + formEnglishName + "Search", false, true, formEnglishName + "s", false, false, false, false));
		return sitePages;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.formtemplate.FormTemplate#resetNavigatorItems(java.util.List, com.yuanluesoft.jeaf.application.builder.pojo.ApplicationForm, java.lang.String)
	 */
	public void resetNavigatorItems(List navigatorItems, ApplicationForm applicationForm, String applicationName) {
		com.yuanluesoft.jeaf.application.model.navigator.definition.Link link = new com.yuanluesoft.jeaf.application.model.navigator.definition.Link();
		link.setTitle("办理流程"); //标题
		link.setHideCondition("noPrivilege(application_manager) and isNotSiteManager()");
		link.setHref("/cms/sitemanage/siteApplicationConfigView.shtml?applicationName=" + applicationName + "&viewApplicationName=cms/publicservice&viewName=admin/workflowSetting");
		navigatorItems.add(link);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.formtemplate.WorkflowFormTemplate#isWorkflowConfigureInEAI()
	 */
	public boolean isWorkflowConfigureInEAI() {
		return false;
	}
}