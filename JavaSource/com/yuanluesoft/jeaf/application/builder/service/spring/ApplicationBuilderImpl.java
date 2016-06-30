package com.yuanluesoft.jeaf.application.builder.service.spring;

import java.io.File;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.impl.ModuleConfigImpl;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.PageDefineService;
import com.yuanluesoft.cms.pagebuilder.model.page.SiteApplication;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.jeaf.application.builder.model.Form;
import com.yuanluesoft.jeaf.application.builder.model.StrutsAction;
import com.yuanluesoft.jeaf.application.builder.model.formtemplate.FormTemplate;
import com.yuanluesoft.jeaf.application.builder.model.formtemplate.PublicServiceFormTemplate;
import com.yuanluesoft.jeaf.application.builder.model.formtemplate.SiteFormTemplate;
import com.yuanluesoft.jeaf.application.builder.model.formtemplate.WorkflowFormTemplate;
import com.yuanluesoft.jeaf.application.builder.pojo.Application;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationField;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationFieldParameter;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationForm;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationIndex;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationNavigator;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationView;
import com.yuanluesoft.jeaf.application.builder.service.ApplicationBuilder;
import com.yuanluesoft.jeaf.application.model.navigator.definition.ApplicationNavigatorDefinition;
import com.yuanluesoft.jeaf.application.model.navigator.definition.Link;
import com.yuanluesoft.jeaf.application.model.navigator.definition.ViewLink;
import com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.database.DatabaseDefineService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.model.Table;
import com.yuanluesoft.jeaf.database.model.TableColumn;
import com.yuanluesoft.jeaf.database.model.TableIndex;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.model.FormAction;
import com.yuanluesoft.jeaf.form.service.FormDefineService;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.struts.service.StrutsDefineService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.ProcessUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.view.model.Column;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.viewaction.NewWorkflowInstanceAction;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewAction;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.jeaf.workflow.service.WorkflowConfigureService;
import com.yuanluesoft.workflow.client.model.resource.Action;
import com.yuanluesoft.workflow.client.model.resource.DataField;
import com.yuanluesoft.workflow.client.model.resource.SubForm;
import com.yuanluesoft.workflow.client.model.resource.WorkflowResource;

/**
 * 
 * @author linchuan
 *
 */
public class ApplicationBuilderImpl implements ApplicationBuilder {
	private String javacExeFilePath = "javac.exe"; //javac.exe路径
	private List formTemplates; //表单模板列表
	private DatabaseService databaseService; //数据库服务
	private BusinessDefineService businessDefineService; //业务逻辑定义服务
	private DatabaseDefineService databaseDefineService; //数据库定义服务
	private FormDefineService formDefineService; //表单定义服务
	private ViewDefineService viewDefineService; //视图定义服务
	private ApplicationNavigatorService applicationNavigatorService; //导航服务
	private WorkflowConfigureService workflowConfigureService; //工作流配置服务
	private StrutsDefineService strutsDefineService; //STRUTS配置服务
	private PageDefineService pageDefineService; //页面定义服务
	private HTMLParser htmlParser; //HTML解析器
	private Cache modelCache; //模型缓存
	
	public ApplicationBuilderImpl() {
		super();
		formTemplates = new ArrayList();
		formTemplates.add(new FormTemplate());
		formTemplates.add(new WorkflowFormTemplate());
		formTemplates.add(new SiteFormTemplate());
		formTemplates.add(new PublicServiceFormTemplate());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.service.ApplicationBuilder#build(long)
	 */
	public void build(long applicationId) throws ServiceException {
		//获取表单列表
		List applicationForms = getDatabaseService().findRecordsByHql("from ApplicationForm ApplicationForm where ApplicationForm.applicationId=" + applicationId + " order by ApplicationForm.id", ListUtils.generateList("fields,views,indexes", ","));
		if(applicationForms==null) {
			return;
		}
		//排序,支持流程的排前面
		Collections.sort(applicationForms, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				ApplicationForm applicationForm0 = (ApplicationForm)arg0;
				ApplicationForm applicationForm1 = (ApplicationForm)arg1;
				FormTemplate formTemplate0 = getFormTemplate(applicationForm0.getTemplateName());
				FormTemplate formTemplate1 = getFormTemplate(applicationForm1.getTemplateName());
				return formTemplate0.isWorkflowSupport()==formTemplate1.isWorkflowSupport() ? 0 : (formTemplate0.isWorkflowSupport() ? -1 : 1);
			}
		});
		
		//获取应用
		Application application = (Application)getDatabaseService().findRecordById(Application.class.getName(), applicationId, ListUtils.generateList("navigators"));
		String applicationName = "customise/" + application.getEnglishName().toLowerCase();
		String applicationPackageName = "com.yuanluesoft." + applicationName.replace('/', '.').toLowerCase();
	
		List tables = new ArrayList(); //表列表
		List businessObjects = new ArrayList(); //业务对象列表(business-config.xml)
		List formConfigs = new ArrayList(); //表单(font-config.xml)
		List viewConfigs = new ArrayList(); //视图列表(view-config.xml)
		ModuleConfig strutsConfig = createStrutsConfig(applicationName); //STRUTS配置(struts-config.xml)
		SiteApplication siteApplication = new SiteApplication(applicationName); //站点应用(page-config.xml)
		siteApplication.setPages(new ArrayList()); //页面列表
		
		for(int i=0; i<applicationForms.size(); i++) {
			ApplicationForm applicationForm = (ApplicationForm)applicationForms.get(i);
			//获取模板
			FormTemplate formTemplate = getFormTemplate(applicationForm.getTemplateName());
			//创建POJO
			String pojoClassName = createBean(applicationForm, formTemplate.getParentRecordClass(), applicationPackageName + ".pojo", "Customise" + StringUtils.capitalizeFirstLetter(applicationForm.getEnglishName()));
			//生成表
			Table table = generateTable(application, applicationForm, pojoClassName);
			tables.add(table);
			List componentsFields = FieldUtils.listRecordFields(formTemplate.getParentRecordClass().getName(), "components", null, null, null, true, true, true, true, 0);
			for(Iterator iterator = componentsFields==null ? null : componentsFields.iterator(); iterator!=null && iterator.hasNext();) {
				Field componentsField = (Field)iterator.next();
				Table componentsTable = generateComponentsTable(table, componentsField); //生成表
				if(componentsTable==null) {
					continue;
				}
				tables.add(componentsTable);
				createTable(componentsTable); //创建表
				createComponentsBean(applicationForm, componentsField, (String)componentsField.getParameter("class"), applicationPackageName + ".pojo", componentsTable.getPojoClassName().substring(componentsTable.getPojoClassName().lastIndexOf('.') + 1)); //创建类
			}
			//创建表
			createTable(table);
		
			//添加业务逻辑对象
			businessObjects.add(generateBusinessObject(pojoClassName, applicationForm));
			
			//创建表单
			List forms = formTemplate.getForms(applicationForm, applicationName, applicationPackageName, pojoClassName, generateJspPathPrefix(applicationName), getPrivilegeName(applicationForm.getEditPrivilege()), getPrivilegeName(applicationForm.getVisitPrivilege()), getPrivilegeName(applicationForm.getDeletePrivilege()));
			for(Iterator iterator = forms.iterator(); iterator.hasNext();) {
				Form form = (Form)iterator.next();
				if(form.getClassName()!=null) { //类名称不为空
					//创建表单类
					int index = form.getClassName().lastIndexOf('.');
					createBean(applicationForm, form.getParentClass(), form.getClassName().substring(0, index), form.getClassName().substring(index + 1));
				}
				//添加表单配置
				formConfigs.add(form);

				//创建STRUTS表单和操作
				createStrutsFormAndAction(strutsConfig, form);
			
				//生成工作流资源配置
				if(form.getParentClass()!=null && WorkflowForm.class.isAssignableFrom(form.getParentClass())) { //工作流
					generateWorkflowResourceConfig(applicationForm, applicationName, form.getActions());
				}
			}
			
			//添加视图配置
			List formViews = generateViews(applicationName, pojoClassName, applicationForm, ((Form)forms.get(forms.size()-1)).getName());
			if(formViews!=null && !formViews.isEmpty()) {
				viewConfigs.addAll(formViews);
			}
		
			//创建JSP
			generateJspFile(applicationForm, applicationName);
			
			//创建站点页面
			List sitePages = formTemplate.generateSitePages(applicationForm, applicationName, pojoClassName);
			if(sitePages!=null) {
				siteApplication.getPages().addAll(sitePages);
			}
			
			//更新生成时间
			applicationForm.setBuildTime(DateTimeUtils.now());
			getDatabaseService().updateRecord(applicationForm);
		}
		//保存hibernate配置文件
		databaseDefineService.saveDatabaseDefine(applicationName, tables);
		
		//保存业务逻辑配置文件
		getBusinessDefineService().saveBusinessDefine(applicationName, businessObjects);
		
		//保存表单配置文件
		formDefineService.saveFormDefine(applicationName, formConfigs);

		//保存视图配置文件
		viewDefineService.saveViewDefine(applicationName, viewConfigs);
		
		//保存STRUTS配置文件
		strutsDefineService.saveStrutsConfig(strutsConfig, applicationName);
		
		//更新applications-config.xml
		updateApplicationsConfig(application, applicationName, applicationForms);
		
		//创建navigator-config.xml
		generateNavigatorConfig(application, applicationName, applicationForms);
		
		//更新page-config.xml
		if(!siteApplication.getPages().isEmpty()) {
			siteApplication.getPages().add(new SitePage("index", "首页", null, "/cms/sitemanage/applicationIndex.shtml?applicationName=" + applicationName, true, false, null, false, false, false, true));
			pageDefineService.savePageDefine(siteApplication);
		}
		
		//更新生成时间
		application.setBuildTime(DateTimeUtils.now());
		getDatabaseService().updateRecord(application);
		
		//清缓存
		try {
			modelCache.clear();
		}
		catch (CacheException e) {
			
		}
	}
	
	/**
	 * 生成表
	 * @param application
	 * @param applicationForm
	 * @param pojoClassName
	 * @return
	 * @throws ServiceException
	 */
	private Table generateTable(Application application, ApplicationForm applicationForm, String pojoClassName) throws ServiceException {
		Table table = new Table();
		table.setPojoClassName(pojoClassName); //POJO类名称
		table.setTableName("_" + application.getEnglishName().toLowerCase() + "_" + applicationForm.getEnglishName().toLowerCase()); //表名称
		table.setPrimaryKey("id"); //主键
		table.setDescription(applicationForm.getName());
		for(Iterator iterator = applicationForm.getFields().iterator(); iterator.hasNext();) {
			ApplicationField field = (ApplicationField)iterator.next();
			if(field.getIsPersistence()==1 && ",varchar,char,text,number,date,timestamp,".indexOf("," + field.getFieldType() + ",")!=-1) {
				table.addTableColumn(field.getEnglishName(), null, field.getName(), field.getFieldType(), field.getFieldLength());
			}
		}
		//生成索引
		for(Iterator iterator = applicationForm.getIndexes()==null ? null : applicationForm.getIndexes().iterator(); iterator!=null && iterator.hasNext();) {
			ApplicationIndex applicationIndex = (ApplicationIndex)iterator.next();
			if(applicationIndex.getFieldIds()==null || applicationIndex.getFieldIds().isEmpty()) {
				continue;
			}
			String[] fieldIds = applicationIndex.getFieldIds().split(",");
			String[] fieldDirections = applicationIndex.getFieldDirections().split(",");
			String indexColumns = null;
			for(int i=0; i<fieldIds.length; i++) {
				ApplicationField field = (ApplicationField)ListUtils.findObjectByProperty(applicationForm.getFields(), "id", new Long(fieldIds[i]));
				if(field!=null) {
					indexColumns = (indexColumns==null ? "" : indexColumns + ",") + field.getEnglishName() + " " + fieldDirections[i];
				}
			}
			if(indexColumns==null) {
				continue;
			}
			table.addTableIndex("index_" + applicationIndex.getId(), indexColumns);
		}
		return table;
	}
	
	/**
	 * 生成组成元素表
	 * @param table
	 * @return
	 * @throws ServiceException
	 */
	private Table generateComponentsTable(Table table, Field componentsField) throws ServiceException {
		Class componentsClass;
		try {
			componentsClass = Class.forName((String)componentsField.getParameter("class"));
		}
		catch (ClassNotFoundException e) {
			throw new ServiceException(e);
		}
		String componentsName = componentsClass.getSimpleName();
		for(int i=componentsName.length()-1; i>=0; i--) {
			if(componentsName.charAt(i)>='A' && componentsName.charAt(i)<='Z') {
				componentsName = componentsName.substring(i);
				break;
			}
		}
		BusinessObject businessObject = getBusinessDefineService().getBusinessObject(componentsClass);
		String tableName = (String)businessObject.getExtendParameter("tableName");
		boolean generateNewTable = (tableName==null); //是否需要创建新表
		String newClassName = null;
		if(generateNewTable) {
			tableName = table.getTableName() + "_" + componentsName.toLowerCase();
			tableName = tableName.length()<=30 ? tableName : tableName.substring(0, 30);
			newClassName = table.getPojoClassName() + componentsName;
		}
		table.addTableOneToManyColumn(componentsField.getName(), componentsField.getTitle(), !"false".equals(componentsField.getParameter("lazyLoad")), tableName, (String)componentsField.getParameter("orderBy"), (String)componentsField.getParameter("foreignKey"), newClassName==null ? componentsClass.getName() : newClassName);
		if(!generateNewTable) { //不需要创建新表
			return null;
		}
		
		Table componentsTable = new Table();
		componentsTable.setPrimaryKey("id"); //主键
		componentsTable.setTableName(tableName);
		componentsTable.setDescription(table.getDescription() + ":" + componentsField.getTitle());
		componentsTable.setPojoClassName(newClassName);
		
		//添加列
		List businessFields = FieldUtils.listRecordFields(componentsClass.getName(), "string,char,html,number,date,timestamp", null, null, null, false, false, false, true, 0);
		for(Iterator iterator = businessFields.iterator(); iterator.hasNext();) {
			Field field = (Field)iterator.next();
			String fieldType = field.getType();
			if(",string,string[],html,".indexOf("," + fieldType + ",")!=-1) {
				fieldType =  (field.getLength()==null || Integer.parseInt(field.getLength())>4000) ? "text" : "varchar"; 
			}
			componentsTable.addTableColumn(field.getName(), null, field.getTitle(), fieldType, field.getLength());
		}
		//创建索引
		for(int i=1; i<=20; i++) {
			String indexColums = (String)businessObject.getExtendParameter("index" + i);
			if(indexColums==null) {
				break;
			}
			String indexName = "index" + (componentsTable.getTableName().length()>25 ? componentsTable.getTableName().substring(0, 25) : componentsTable.getTableName()) + i;
			componentsTable.addTableIndex(indexName + "1", indexColums);
		}
		return componentsTable;
	}
	
	/**
	 * 创建表
	 * @param application
	 * @param applicationForm
	 * @param pojoClassName
	 * @return
	 * @throws ServiceException
	 */
	private void createTable(Table table) throws ServiceException {
		try {
			getDatabaseService().createTable(table); //创建表
			return;
		}
		catch(Exception e) {
			
		}
		//表未创建成功,删除索引
		for(Iterator iterator = table.getIndexes()==null ? null : table.getIndexes().iterator(); iterator!=null && iterator.hasNext();) {
			TableIndex index = (TableIndex)iterator.next();
			try {
				getDatabaseService().dropIndex(table.getTableName(), index.getIndexName());
			}
			catch(Exception e) {
				
			}
		}
		
		//修改表字段
		for(Iterator iterator = table.getColumns().iterator(); iterator!=null && iterator.hasNext();) {
			TableColumn column = (TableColumn)iterator.next();
			try {
				getDatabaseService().addOrModifyTableColumn(table.getTableName(), column.getName(), column.getType(), column.getLength(), true);
			}
			catch(Exception e) {
				Logger.exception(e);
				throw new ValidateException("修改表单[" + table.getDescription() + "]字段[" + column.getDescription() + "]时出错");
			}
		}
					
		//重建索引
		for(Iterator iterator = table.getIndexes()==null ? null : table.getIndexes().iterator(); iterator!=null && iterator.hasNext();) {
			TableIndex index = (TableIndex)iterator.next();
			try {
				getDatabaseService().createIndex(table.getTableName(), index.getIndexName(), index.getIndexColumns());
			}
			catch(Exception e) {
				Logger.exception(e);
				throw new ValidateException("重建索引时出错");
			}
		}
	}
	
	/**
	 * 创建STRUTS配置
	 * @param applicationName
	 * @return
	 */
	private ModuleConfig createStrutsConfig(String applicationName) throws ServiceException {
		//STRUTS配置
		ModuleConfig strutsConfig = new ModuleConfigImpl(applicationName);
		String pathPrefix = generateJspPathPrefix(applicationName);
		
		//添加全局异常
		ExceptionConfig exceptionConfig = new ExceptionConfig();
		exceptionConfig.setKey("exception");
		exceptionConfig.setPath(pathPrefix + "/jeaf/form/error.jsp");
		exceptionConfig.setScope("request");
		exceptionConfig.setType("java.lang.Exception");
		strutsConfig.addExceptionConfig(exceptionConfig);
		
		//添加全局FORWARD
		ForwardConfig forwardConfig = new ForwardConfig();
		forwardConfig.setName("result");
		forwardConfig.setPath(pathPrefix + "/jeaf/form/result.jsp");
		strutsConfig.addForwardConfig(forwardConfig);
		return strutsConfig;
	}
	
	/**
	 * 创建STRUTS表单和操作
	 * @param strutsConfig
	 * @param form
	 * @throws ServiceException
	 */
	private void createStrutsFormAndAction(ModuleConfig strutsConfig, com.yuanluesoft.jeaf.application.builder.model.Form form) throws ServiceException {
		//添加FORM BEAN
		if(form.getStrutsFormBeanName()!=null) {
			FormBeanConfig formBeanConfig = new FormBeanConfig();
			formBeanConfig.setName(form.getStrutsFormBeanName());
			formBeanConfig.setType(form.getClassName());
			strutsConfig.addFormBeanConfig(formBeanConfig);
		}
		
		//创建ACTION
		for(Iterator iterator = form.getStrutsActions()==null ? null : form.getStrutsActions().iterator(); iterator!=null && iterator.hasNext();) {
			StrutsAction strutsAction = (StrutsAction)iterator.next();
			ActionConfig actionConfig = new ActionConfig();
			actionConfig.setName(strutsAction.getFormBeanName());
			actionConfig.setPath(strutsAction.getPath());
			actionConfig.setScope("request");
			actionConfig.setType(strutsAction.getActionClassName());
			actionConfig.setValidate(false);
			actionConfig.setInput(strutsAction.getInput());
			for(int i=0; i<(strutsAction.getForwardName()==null ? 0 : strutsAction.getForwardName().length); i++) {
				ForwardConfig actionForwardConfig = new ForwardConfig();
				actionForwardConfig.setName(strutsAction.getForwardName()[i]);
				actionForwardConfig.setPath(strutsAction.getForwardPath()[i]);
				actionConfig.addForwardConfig(actionForwardConfig);
			}
			strutsConfig.addActionConfig(actionConfig);
		}
	}
	
	/**
	 * 生成JSP路径前缀
	 * @param applicationName
	 * @return
	 */
	private String generateJspPathPrefix(String applicationName) {
		String pathPrefix = "";
		for(int i=applicationName.split("/").length-1; i>=0; i--) {
			pathPrefix += "/..";
		}
		return pathPrefix;
	}
	
	/**
	 * 生成类,返回类名称
	 * @param applicationName
	 * @param applicationForm
	 * @param parentClass
	 * @param adminForm
	 * @return
	 * @throws ServiceException
	 */
	private String createBean(ApplicationForm applicationForm, Class parentClass, String packageName, String className) throws ServiceException {
		String code = "package " + packageName + ";\n\n" +
					  "import " + parentClass.getName() + ";\n" +
					  (ListUtils.findObjectByProperty(ListUtils.getSubListByProperty(applicationForm.getFields(), "isPresetting", new Integer(0)), "fieldType", TableColumn.COLUMN_TYPE_DATE)==null ? "" : "import " + Date.class.getName() + ";\n") +
					  (ListUtils.findObjectByProperty(ListUtils.getSubListByProperty(applicationForm.getFields(), "isPresetting", new Integer(0)), "fieldType", TableColumn.COLUMN_TYPE_TIMESTAMP)==null ? "" : "import " + Timestamp.class.getName() + ";\n") +
					  "\n" +
					  "/**\n" +
					  "  * " + applicationForm.getName() + "\n" +
					  "  * @author " + System.getProperty("user.name") + "\n" +
					  "  *\n" +
					  "  */\n" +
					  "public class " + className + " extends " + parentClass.getSimpleName() + " {\n";
		//生成属性列表
		for(Iterator iterator = (applicationForm.getFields()==null ? null : applicationForm.getFields().iterator()); iterator!=null && iterator.hasNext();) {
			ApplicationField field = (ApplicationField)iterator.next();
			if(field.getIsPresetting()==1) { //预设字段
				continue;
			}
			String dataType = getDataType(field); //获取数据类型
			if(dataType!=null) {
				code += "\tprivate " + dataType + " " + field.getEnglishName() + "; //" + field.getName() + "\n";
			}
		}
		//生成get/set方法列表
		for(Iterator iterator = (applicationForm.getFields()==null ? null : applicationForm.getFields().iterator()); iterator!=null && iterator.hasNext();) {
			ApplicationField field = (ApplicationField)iterator.next();
			if(field.getIsPresetting()==1) { //预设字段
				continue;
			}
			String dataType = getDataType(field); //获取数据类型
			if(dataType==null) {
				continue;
			}
			code += "\n" +
					"\t/**\n" +
					"\t  * @return the " + field.getEnglishName() + "\n" +
					"\t  */\n" +
					"\tpublic " + dataType + " get" + StringUtils.capitalizeFirstLetter(field.getEnglishName()) + "() {\n" +
					"\t\treturn " + field.getEnglishName() + ";\n" +
					"\t}\n" +
					"\n" +
					"\t/**\n" +
					"\t  * @param " + field.getEnglishName() + " the " + field.getEnglishName() + " to set\n" +
					"\t  */\n" +
					"\tpublic void set" + StringUtils.capitalizeFirstLetter(field.getEnglishName()) + "(" + dataType + " " + field.getEnglishName() + ") {\n" +
					"\t\tthis." + field.getEnglishName() + " = " + field.getEnglishName() + ";\n" +
					"\t}\n";
		}
		code += "}";
		String sourceFilePath = getSourceFilePath(packageName);
		FileUtils.saveStringToFile(sourceFilePath + className + ".java", code, "utf-8", false);
		//编译
		compileJavaClass(sourceFilePath + className + ".java");
		return packageName + "." + className;
	}
	
	/**
	 * 创建组成部分类
	 * @param applicationForm
	 * @param componentsField
	 * @param parentClassName
	 * @param packageName
	 * @param className
	 * @throws ServiceException
	 */
	private void createComponentsBean(ApplicationForm applicationForm, Field componentsField, String parentClassName, String packageName, String className) throws ServiceException {
		String code = "package " + packageName + ";\n\n" +
		  	   "import " + parentClassName + ";\n" +
		  	   "\n" +
		  	   "/**\n" +
		  	   "  * " + applicationForm.getName() + ":" + componentsField.getTitle() + "\n" +
		  	   "  * @author " + System.getProperty("user.name") + "\n" +
		  	   "  *\n" +
		  	   "  */\n" +
		  	   "public class " + className + " extends " + parentClassName.substring(parentClassName.lastIndexOf('.') + 1) + " {\n" +
		  	   "\n" +
		  	   "}";
		String sourceFilePath = getSourceFilePath(packageName);
		FileUtils.saveStringToFile(sourceFilePath + className + ".java", code, "utf-8", false);
		//编译
		compileJavaClass(sourceFilePath + className + ".java");
	}
	
	/**
	 * 获取源文件存放路径
	 * @param packageName
	 * @return
	 */
	private String getSourceFilePath(String packageName) {
		String sourceFilePath = null;
		String webAppPath = Environment.getWebAppPath();
		if(webAppPath.endsWith("/WebContent/")) {
			sourceFilePath = webAppPath.substring(0, webAppPath.length() - "WebContent/".length()) + "JavaSource/";
			if(!FileUtils.isExists(sourceFilePath)) {
				sourceFilePath = null;
			}
		}
		sourceFilePath = FileUtils.createDirectory((sourceFilePath==null ? Environment.getWebinfPath() + "jeaf/application/builder/src/" : sourceFilePath) + packageName.replace('.', '/'));
		return sourceFilePath;
	}
	
	/**
	 * 编译
	 * @param sourceFile
	 */
	private void compileJavaClass(String sourceFile) {
		//D:\>javac -classpath "D:\workspace\cms\WebContent\WEB-INF\classes";D:\workspace\exchangetest\src -encoding utf-8 D:\workspace\exchangetest\src\JavacTest.java
		List commands = new ArrayList();
		commands.add(javacExeFilePath.replace("/", File.separator));
		commands.add("-classpath");
		commands.add("\"" + new File(Environment.getWebinfPath() + "classes").getPath() + ";" + new File(Environment.getWebinfPath() + "lib/struts.jar").getPath() + "\"");
		commands.add("-encoding");
		commands.add("utf-8");
		commands.add("-d");
		commands.add("\"" + new File(Environment.getWebinfPath() + "classes").getPath() + "\"");
		commands.add(new File(sourceFile).getPath());
		ProcessUtils.executeCommand(commands, null, null);
	}
	
	/**
	 * 生成业务逻辑对象
	 * @param pojoClassName
	 * @param applicationForm
	 * @return
	 * @throws ServiceException
	 */
	private BusinessObject generateBusinessObject(String pojoClassName, ApplicationForm applicationForm) throws ServiceException {
		FormTemplate formTemplate = getFormTemplate(applicationForm.getTemplateName());
		BusinessObject businessObject = new BusinessObject();
		businessObject.setTitle(applicationForm.getName()); //标题
		businessObject.setClassName(pojoClassName); //类名称
		businessObject.setBusinessServiceName(getBusinessDefineService().getBusinessObject(formTemplate.getParentRecordClass()).getBusinessServiceName()); //业务逻辑服务名称
		businessObject.setFields(new ArrayList()); //字段列表
		boolean opinionProcessed = false; //是否已经处理过意见字段
		boolean containsHtmlEditor = false; //是否有HTML编辑器
		List parentFields = FieldUtils.listRecordFields(formTemplate.getParentRecordClass().getName(), null, null, null, null, false, false, false, false, 0);
		for(Iterator iterator = (applicationForm.getFields()==null ? null : applicationForm.getFields().iterator()); iterator!=null && iterator.hasNext();) {
			ApplicationField applicationField = (ApplicationField)iterator.next();
			if(applicationField.getIsPresetting()==1) { //预设字段,不写入business-config
				continue;
			}
			//文本|varchar\0长文本|text\0数字|number\0日期|date\0时间|timestamp\0附件|attachment\0图片|image\0视频|video\0办理意见|opinion
			if("opinion".equals(applicationField.getFieldType())) { //意见字段
				if(opinionProcessed) {
					continue;
				}
				opinionProcessed = true;
				Field field = new Field();
				field.setName("opinions"); //名称
				field.setTitle("办理意见");
				field.setType("components"); //类型
				field.setPersistence(true); //持久层属性
				field.setParameter("class", pojoClassName + "Opinion");
				String presettingOpinionTypes = null; //预置的意见类型
				List opinionFields = ListUtils.getSubListByProperty(applicationForm.getFields(), "fieldType", "opinion");
				for(int i=0; i<opinionFields.size(); i++) {
					ApplicationField opinionField = (ApplicationField)opinionFields.get(i);
					String prompt = applicationField.getParameterValue("prompt"); //未填写时的提示
					presettingOpinionTypes = (presettingOpinionTypes==null ? "" : presettingOpinionTypes + ",") +
											 (opinionField.getName().endsWith("意见") ? opinionField.getName().substring(0, opinionField.getName().length()-2) : opinionField.getName()) +
											 (opinionField.getRequired()==1 ? "|required" : "") + //是否必填
											 (prompt==null ? "" : (opinionField.getRequired()==1 ? "" : "|") + "|" + prompt); //未填写时的提示
				}
				field.setParameter("presettingOpinionTypes", presettingOpinionTypes);
				businessObject.getFields().add(field);
				continue;
			}
			String fieldType = applicationField.getFieldType();
			if(TableColumn.COLUMN_TYPE_VARCHAR.equals(fieldType)) {
				fieldType = "string";
			}
			else if(TableColumn.COLUMN_TYPE_TEXT.equals(fieldType)) {
				fieldType = "htmleditor".equals(applicationField.getInputMode()) ? "html" : "string";
			}
			Field field = new Field();
			field.setName(applicationField.getEnglishName()); //名称
			field.setTitle(applicationField.getName());
			field.setType(fieldType); //类型
			if(applicationField.getInputMode()!=null && !applicationField.getInputMode().isEmpty()) {
				field.setInputMode(applicationField.getInputMode()); //输入方式
			}
			if(applicationField.getFieldLength()!=null && !applicationField.getFieldLength().isEmpty()) {
				field.setLength(applicationField.getFieldLength()); //长度
			}
			field.setRequired(applicationField.getRequired()==1); //是否必填
			field.setPersistence(true); //持久层属性
			//参数
			for(Iterator iteratorParameter = applicationField.getParameters()==null ? null : applicationField.getParameters().iterator(); iteratorParameter!=null && iteratorParameter.hasNext();) {
				ApplicationFieldParameter parameter = (ApplicationFieldParameter)iteratorParameter.next();
				if(parameter.getParameterValue()==null || parameter.getParameterValue().isEmpty()) {
					continue;
				}
				String parameterValue = parameter.getParameterValue();
				if("itemsText".equals(parameter.getParameterName())) {
					parameterValue = parameterValue.replaceAll("[,]", "\\\\0");
				}
				field.setParameter(("className".equals(parameter.getParameterName()) ? "class" : parameter.getParameterName()), parameterValue);
			}
			//附件
			if(",attachment,image,video,".indexOf("," + applicationField.getFieldType() + ",")!=-1) {
				if(field.getParameter("attachmentEditor")==null) {
					field.setParameter("attachmentEditor", applicationForm.getEnglishName() + "AttachmentEditor.shtml");
				}
			}
			else if("htmleditor".equals(applicationField.getInputMode())) { //HTML编辑器
				if(field.getParameter("attachmentSelector")==null) {
					field.setParameter("attachmentSelector", "select" + StringUtils.capitalizeFirstLetter(applicationForm.getEnglishName()) + "Attachment.shtml");
				}
				if(field.getParameter("height")==null) {
					field.setParameter("height", "160px");
				}
				containsHtmlEditor = true;
			}
			if(applicationField.getIsPresetting()==1) { //预设字段
				Field parentField = (Field)ListUtils.findObjectByProperty(parentFields, "name", field.getName());
				if(parentField!=null && parentField.equals(field)) { //字段没有做修改,不需要加入business-config.xml
					continue;
				}
			}
			businessObject.getFields().add(field);
		}
		if(containsHtmlEditor) { //有HTML编辑器
			//检查是否有attachments字段,如果没有,则自动添加
			if(ListUtils.findObjectByProperty(businessObject.getFields(), "name", "attachments")==null) {
				Field field = new Field();
				field.setName("attachments"); //名称
				field.setTitle("附件");
				field.setType("attachment"); //类型
				businessObject.getFields().add(field);
			}
			//检查是否有images字段,如果没有,则自动添加
			if(ListUtils.findObjectByProperty(businessObject.getFields(), "name", "images")==null) {
				Field field = new Field();
				field.setName("images"); //名称
				field.setTitle("图片");
				field.setType("image"); //类型
				businessObject.getFields().add(field);
			}
			//检查是否有flashs字段,如果没有,则自动添加
			if(ListUtils.findObjectByProperty(businessObject.getFields(), "name", "flashs")==null) {
				Field field = new Field();
				field.setName("flashs"); //名称
				field.setTitle("FLASH");
				field.setType("attachment"); //类型
				field.setParameter("maxUploadSize", "2000000");
				field.setParameter("fileExtension", "FLASH文件|*.swf|");
				businessObject.getFields().add(field);
			}
			//检查是否有videos字段,如果没有,则自动添加
			if(ListUtils.findObjectByProperty(businessObject.getFields(), "name", "videos")==null) {
				Field field = new Field();
				field.setName("videos"); //名称
				field.setTitle("视频");
				field.setType("video"); //类型
				field.setParameter("maxUploadSize", "500000000");
				field.setParameter("videoBitrate", "473");
				field.setParameter("videoFps", "25");
				field.setParameter("videoWidth", "400");
				field.setParameter("videoHeight", "300");
				field.setParameter("audioBitrate", "56");
				field.setParameter("audioFreq", "22050");
				businessObject.getFields().add(field);
			}
		}
		return businessObject;
	}
	
	/**
	 * 创建视图配置
	 * @param applicationName
	 * @param pojoClassName
	 * @param applicationForm
	 * @param formName
	 * @return
	 * @throws ServiceException
	 */
	private List generateViews(String applicationName, String pojoClassName, ApplicationForm applicationForm, String formName) throws ServiceException {
		FormTemplate formTemplate = getFormTemplate(applicationForm.getTemplateName());
		List views = new ArrayList();
		for(Iterator iterator = applicationForm.getViews()==null ? null : applicationForm.getViews().iterator(); iterator!=null && iterator.hasNext();) {
			ApplicationView applicationView = (ApplicationView)iterator.next();
			View view = new View();
			//生成列列表
			view.setColumns(new ArrayList());
			String quickFilter = null;
			String shortPojoClassName = pojoClassName.substring(pojoClassName.lastIndexOf('.') + 1);
			//添加序号
			Column column = new Column(null, "序号", Column.COLUMN_TYPE_ROWNUM);
			column.setHideTitle(true);
			column.setWidth("32");
			column.setAlign("center");
			view.getColumns().add(column);
			//添加字段
			String[] viewFieldIds = applicationView.getViewFieldIds().split(",");
			Column maxWidthColumn = null;
			double widthLeft = 800;
			double stringWidth = 0;
			for(int i=0; i<viewFieldIds.length; i++) {
				ApplicationField field = (ApplicationField)ListUtils.findObjectByProperty(applicationForm.getFields(), "id", new Long(viewFieldIds[i]));
				if(field==null) {
					continue;
				}
				column = new Column(field.getEnglishName(), field.getName(), Column.COLUMN_TYPE_FIELD);
				int width = 0;
				if(TableColumn.COLUMN_TYPE_TIMESTAMP.equals(field.getFieldType())) {
					width = 110;
					widthLeft -= width;
					column.setAlign("center");
				}
				else if(TableColumn.COLUMN_TYPE_DATE.equals(field.getFieldType())) {
					width = 70;
					widthLeft -= width;
					column.setAlign("center");
				}
				else if(TableColumn.COLUMN_TYPE_NUMBER.equals(field.getFieldType())) {
					width = Integer.parseInt(field.getFieldLength().split(",")[0])*6 + 12;
					widthLeft -= width;
					column.setAlign("center");
				}
				else if(TableColumn.COLUMN_TYPE_CHAR.equals(field.getFieldType()) || TableColumn.COLUMN_TYPE_VARCHAR.equals(field.getFieldType()) || TableColumn.COLUMN_TYPE_TEXT.equals(field.getFieldType())) {
					int length = TableColumn.COLUMN_TYPE_TEXT.equals(field.getFieldType()) ? 10000 : (TableColumn.COLUMN_TYPE_CHAR.equals(field.getFieldType()) ? 8 : Integer.parseInt(field.getFieldLength()));
					column.setAlign(length>30 ? "left" : "center");
					if(length>200) {
						column.setMaxCharCount(200);
						column.setEllipsis("...");
					}
					width = Math.min(length, 100)*6 + 6;
					quickFilter = (quickFilter==null ? "" : quickFilter + " or ") + shortPojoClassName + "." + column.getName() + " like '%{KEY}%'";
				}
				width = Math.max(column.getTitle().getBytes().length*6+10, width);
				column.setWidth("" + width);
				if(TableColumn.COLUMN_TYPE_CHAR.equals(field.getFieldType()) || TableColumn.COLUMN_TYPE_VARCHAR.equals(field.getFieldType()) || TableColumn.COLUMN_TYPE_TEXT.equals(field.getFieldType())) {
					stringWidth += width;
				}
				if(maxWidthColumn==null || Integer.parseInt(maxWidthColumn.getWidth())<width) {
					maxWidthColumn = column;
				}
				view.getColumns().add(column);
			}
			for(Iterator iteratorColumn = view.getColumns().iterator(); iteratorColumn.hasNext();) {
				column = (Column)iteratorColumn.next();
				ApplicationField field = (ApplicationField)ListUtils.findObjectByProperty(applicationForm.getFields(), "englishName", column.getName());
				if(field!=null && (TableColumn.COLUMN_TYPE_CHAR.equals(field.getFieldType()) || TableColumn.COLUMN_TYPE_VARCHAR.equals(field.getFieldType()) || TableColumn.COLUMN_TYPE_TEXT.equals(field.getFieldType()))) { //文本列
					int width = Integer.parseInt(column.getWidth());
					column.setWidth("" + (int)Math.min(width, width/stringWidth * widthLeft));
				}
			}
			maxWidthColumn.setWidth("100%");
			
			view.setName(applicationView.getEnglishName()); //名称
			view.setTitle(applicationView.getName()); //标题
			view.setForm(formName); //表单
			view.setOpenFeatures("mode=fullscreen"); //打开方式
			String privilegeName = getPrivilegeName(applicationView.getAccessPrivilege());
			view.setHideCondition(privilegeName==null ? null : "noPrivilege(" + privilegeName + ")"); //隐藏条件
			view.setPageRows(20); //每页行数
			view.setPojoClassName(pojoClassName); //POJO
			view.setQuickFilter(quickFilter); //快速过滤
			view.setFilter(applicationView.getFilterMode()); //过滤方式
			if(applicationView.getHqlWhere()!=null && !applicationView.getHqlWhere().isEmpty()) {
				view.setWhere(applicationView.getHqlWhere());
			}
			
			//生成排序列
			if(applicationView.getSortFieldIds()!=null && !applicationView.getSortFieldIds().isEmpty()) {
				String[] fieldIds = applicationView.getSortFieldIds().split(",");
				String[] fieldDirections = applicationView.getSortFieldDirections().split(",");
				String orderBy = null;
				for(int i=0; i<fieldIds.length; i++) {
					ApplicationField field = (ApplicationField)ListUtils.findObjectByProperty(applicationForm.getFields(), "id", new Long(fieldIds[i]));
					if(field!=null) {
						orderBy = (orderBy==null ? "" : orderBy + ",") + shortPojoClassName + "." + field.getEnglishName() + " " + fieldDirections[i].toUpperCase();
					}
				}
				view.setOrderBy(orderBy);
			}
			
			//生成操作列表
			ViewAction viewAction = (formTemplate.isWorkflowSupport() ? new NewWorkflowInstanceAction() : new ViewAction());
			viewAction.setTitle(applicationForm.getNewActionName()); //标题
			privilegeName = getPrivilegeName(applicationForm.getEditPrivilege());
			viewAction.setHideCondition(privilegeName==null ? null : "noPrivilege(" + privilegeName + ")"); //隐藏条件
			if(!formTemplate.isWorkflowSupport()) { //非工作流应用
				viewAction.setExecute("PageUtils.newrecord('" + applicationName + "', '" + applicationForm.getEnglishName() + "', 'mode=fullscreen')"); //执行的操作
			}
			else {
				NewWorkflowInstanceAction workflowInstanceAction = (NewWorkflowInstanceAction)viewAction;
				workflowInstanceAction.setApplicationName(applicationName);
				workflowInstanceAction.setForm(applicationForm.getEnglishName());
			}
			view.setActions(ListUtils.generateList(viewAction));
			views.add(view);
		}
		//重试视图列表
		formTemplate.resetViews(views, applicationForm, applicationName, pojoClassName);
		return views;
	}
	
	/**
	 * 生成导航栏
	 * @param application
	 * @param applicationName
	 * @param forms
	 * @throws ServiceException
	 */
	private void generateNavigatorConfig(Application application, String applicationName, List applicationForms) throws ServiceException {
		ApplicationNavigatorDefinition navigatorDefinition = new ApplicationNavigatorDefinition();
		navigatorDefinition.setLinks(new ArrayList());
		for(Iterator iterator = application.getNavigators()==null ? null : application.getNavigators().iterator(); iterator!=null && iterator.hasNext();) {
			ApplicationNavigator navigator = (ApplicationNavigator)iterator.next();
			String privilegeName = getPrivilegeName(navigator.getAccessPrivilege());
			if(navigator.getViewId()==0) { //链接
				Link link = new Link();
				link.setTitle(navigator.getLabel()); //标题
				link.setHideCondition(privilegeName==null ? null : "noPrivilege(" + privilegeName + ")");
				link.setHref(navigator.getUrl());
				navigatorDefinition.getLinks().add(link);
			}
			else { //视图
				//按ID查找视图
				ApplicationView view = null;
				for(Iterator iteratorForm = applicationForms==null ? null : applicationForms.iterator(); iteratorForm!=null && iteratorForm.hasNext();) {
					ApplicationForm applicationForm = (ApplicationForm)iteratorForm.next();
					if((view=(ApplicationView)ListUtils.findObjectByProperty(applicationForm.getViews(), "id", new Long(navigator.getViewId())))!=null) {
						break;
					}
				}
				if(view!=null) {
					ViewLink viewLink = new ViewLink();
					viewLink.setLocal(view.getEnglishName());
					viewLink.setTitle(navigator.getLabel()); //标题
					navigatorDefinition.getLinks().add(viewLink);
				}
			}
		}
		for(Iterator iteratorForm = applicationForms==null ? null : applicationForms.iterator(); iteratorForm!=null && iteratorForm.hasNext();) {
			ApplicationForm applicationForm = (ApplicationForm)iteratorForm.next();
			FormTemplate formTemplate = getFormTemplate(applicationForm.getTemplateName());
			formTemplate.resetNavigatorItems(navigatorDefinition.getLinks(), applicationForm, applicationName);
		}
		applicationNavigatorService.saveApplicationNavigatorDefinition(applicationName, navigatorDefinition);
	}
	
	/**
	 * 生成工作流资源配置
	 * @param applicationForm
	 * @param applicationName
	 * @throws ServiceException
	 */
	private void generateWorkflowResourceConfig(ApplicationForm applicationForm, String applicationName, List formActions) throws ServiceException {
		WorkflowResource workflowResource = new WorkflowResource();
		//操作列表
		workflowResource.setActions(new ArrayList());
		//添加STRUTS操作
		for(Iterator iterator = formActions.iterator(); iterator.hasNext();) {
			FormAction formAction = (FormAction)iterator.next();
			if(formAction.getHideCondition()!=null &&
			   (formAction.getHideCondition().indexOf("deleteDisable()")!=-1 || formAction.getHideCondition().indexOf("workflow()")!=-1) ) {
				workflowResource.getActions().add(new Action(formAction.getTitle(), formAction.getActionPrompt(), formAction.isNecessaryAction(), formAction.isPromptOnCreate(), formAction.isDeleteAction(), formAction.isSendAction(), formAction.isReverseAction()));
			}
		}
		
		//子表单列表
		workflowResource.setSubForms(new ArrayList());
		workflowResource.getSubForms().add(new SubForm("Edit", "编辑"));
		workflowResource.getSubForms().add(new SubForm("Read", "只读"));
		//字段列表
		workflowResource.setDataFields(new ArrayList());
		for(Iterator iterator = applicationForm.getFields()==null ? null : applicationForm.getFields().iterator(); iterator!=null && iterator.hasNext();) {
			ApplicationField applicationField = (ApplicationField)iterator.next();
			if(",varchar,number,date,timestamp,".indexOf("," + applicationField.getFieldType() + ",")!=-1) {
				DataField dataField = new DataField();
				dataField.setId(applicationField.getEnglishName());
				dataField.setName(applicationField.getName());
				dataField.setBasicType(true);
				if("varchar".equals(applicationField.getFieldType())) {
					dataField.setType("STRING");
				}
				else if("date".equals(applicationField.getFieldType()) || "timestamp".equals(applicationField.getFieldType())) {
					dataField.setType("DATETIME");
				}
				else if("number".equals(applicationField.getFieldType())) {
					dataField.setType(applicationField.getFieldLength().indexOf(',')==-1 || applicationField.getFieldLength().equals(",0") ? "INTEGER" : "FLOAT");
				}
				workflowResource.getDataFields().add(dataField);
			}
		}
		workflowConfigureService.saveWorkflowResourceDefine(applicationName, workflowResource);
	}
	
	/**
	 * 获取权限名称
	 * @param privilege
	 * @return
	 */
	private String getPrivilegeName(String privilege) {
		if(privilege==null || privilege.isEmpty()) {
			return null;
		}
		else if("{管理员}".equals(privilege)) {
			return AccessControlService.ACL_APPLICATION_MANAGER;
		}
		else if("{访问者}".equals(privilege)) {
			return AccessControlService.ACL_APPLICATION_VISITOR;
		}
		else {
			return "manageUnit_" + privilege;
		}
	}
	
	/**
	 * 更新applications-config.xml
	 * @param application
	 * @param applicationName
	 * @param forms
	 * @throws ServiceException
	 */
	private void updateApplicationsConfig(Application application, String applicationName, List applicationForms) throws ServiceException {
		Map manageUnits = new HashMap(); //获取管理单元列表
		boolean isWorkflowConfigureInEAI = false;
		for(int i=0; i<(applicationForms==null ? 0 : applicationForms.size()); i++) {
			ApplicationForm applicationForm = (ApplicationForm)applicationForms.get(i);
			manageUnits.put(applicationForm.getEditPrivilege()==null ? "" : applicationForm.getEditPrivilege(), "");
			manageUnits.put(applicationForm.getDeletePrivilege()==null ? "" : applicationForm.getDeletePrivilege(), "");
			manageUnits.put(applicationForm.getVisitPrivilege()==null ? "" : applicationForm.getVisitPrivilege(), "");
			//获取视图访问权限
			for(Iterator iterator = applicationForm.getViews()==null ? null : applicationForm.getViews().iterator(); iterator!=null && iterator.hasNext();) {
				ApplicationView view = (ApplicationView)iterator.next();
				manageUnits.put(view.getAccessPrivilege()==null ? "" : view.getAccessPrivilege(), "");
			}
			FormTemplate formTemplate = getFormTemplate(applicationForm.getTemplateName());
			if(formTemplate.isWorkflowConfigureInEAI()) {
				isWorkflowConfigureInEAI = true;
			}
		}
		//获取导航栏访问权限
		for(Iterator iterator = application.getNavigators()==null ? null : application.getNavigators().iterator(); iterator!=null && iterator.hasNext();) {
			ApplicationNavigator navigator = (ApplicationNavigator)iterator.next();
			manageUnits.put(navigator.getAccessPrivilege()==null ? "" : navigator.getAccessPrivilege(), "");
		}
		String manageUnitConfig = null;
		for(Iterator iterator = manageUnits.keySet().iterator(); iterator.hasNext();) {
			String manageUnit = (String)iterator.next();
			if(manageUnit!=null && !manageUnit.isEmpty() && "{管理员}{访问者}".indexOf(manageUnit)==-1) {
				manageUnitConfig = (manageUnitConfig==null ? "" : manageUnitConfig + "\n") + "\t\t\t\t<ManageUnit Id=\"" + manageUnit + "\" Name=\"" + manageUnit + "\"/>";
			}
		}
		
		//把应用加入到applications-config.xml
		String applicationsConfig = FileUtils.readStringFromFile(Environment.getWebinfPath() + "applications-config.xml", "utf-8");
		//清理原来的配置
		applicationsConfig = applicationsConfig.replaceFirst("<Application(.*) Name=\"" + applicationName + "\"\\/>\n\t\t", "");
		applicationsConfig = applicationsConfig.replaceFirst("<Application(.*) Name=\"" + applicationName + "\">[\\s\\S]*?<\\/Application>\n\t\t", "");
		//更新配置
		String applicationConfig = "<Application Title=\"" + application.getName() + "\"" + (isWorkflowConfigureInEAI ? " WorkflowSupport=\"true\"" : "") + " Name=\"" + applicationName + "\"";
		if(manageUnitConfig==null) {
			applicationConfig += "/>";
		}
		else {
			applicationConfig += ">" +
								 "\n\t\t\t<ManageUnits>\n" +
								 manageUnitConfig +
								 "\n\t\t\t</ManageUnits>\n" +
								 "\t\t</Application>";
		}
		applicationsConfig = applicationsConfig.replace("<!-- application build end -->", applicationConfig + "\n\t\t<!-- application build end -->");
		FileUtils.saveStringToFile(Environment.getWebinfPath() + "applications-config.xml", applicationsConfig, "utf-8", false);
	}
	
	/**
	 * 生成JSP
	 * @param applicationForm
	 * @param applicationName
	 * @throws ServiceException
	 */
	protected void generateJspFile(ApplicationForm applicationForm, String applicationName) throws ServiceException {
		FormTemplate formTemplate = getFormTemplate(applicationForm.getTemplateName());
		//解析表单定制内容
		HTMLDocument htmlDocument = htmlParser.parseHTMLString(applicationForm.getCustom(), "utf-8");
		NodeList htmlFields = htmlDocument.getElementsByTagName("a");
		for(int i=htmlFields==null ? -1 : htmlFields.getLength()-1; i>=0; i--) {
			HTMLAnchorElement htmlField = (HTMLAnchorElement)htmlFields.item(i);
			if(!"formField".equals(htmlField.getId())) {
				continue;
			}
			ApplicationField applicationField = (ApplicationField)ListUtils.findObjectByProperty(applicationForm.getFields(), "id", new Long(htmlField.getAttribute("urn")));
			if(applicationField==null) {
				htmlField.getParentNode().removeChild(htmlField);
			}
			else {
				HTMLElement extField = (HTMLElement)htmlDocument.createElement("ext:field");
				extField.setAttribute("property", applicationField.getEnglishName());
				htmlField.getParentNode().replaceChild(extField, htmlField);
			}
		}
		String editSubFormJsp = htmlParser.getBodyHTML(htmlDocument, "utf-8", true).replaceAll("></ext:field>", "/>");
		String readSubFormJsp =  editSubFormJsp.replaceAll("<ext:field", "<ext:field writeonly=\"true\"");
		formTemplate.createJspFiles(applicationForm, applicationName, editSubFormJsp, readSubFormJsp);
	}
	
	/**
	 * 获取数据类型
	 * @param field
	 * @return
	 */
	private String getDataType(ApplicationField field) {
		//文本|varchar\0长文本|text\0数字|number\0日期|date\0时间|timestamp\0附件|attachment\0图片|image\0视频|video\0办理意见|opinion
		if(TableColumn.COLUMN_TYPE_VARCHAR.equals(field.getFieldType()) || TableColumn.COLUMN_TYPE_TEXT.equals(field.getFieldType())) { //文本
			return "String";
		}
		else if(TableColumn.COLUMN_TYPE_CHAR.equals(field.getFieldType())) { //字符
			return "char";
		}
		else if(TableColumn.COLUMN_TYPE_DATE.equals(field.getFieldType())) { //日期
			return "Date";
		}
		else if(TableColumn.COLUMN_TYPE_TIMESTAMP.equals(field.getFieldType())) { //时间
			return "Timestamp";
		}
		else if(TableColumn.COLUMN_TYPE_NUMBER.equals(field.getFieldType())) { //数字
			String type = "int";
			if(field.getFieldLength()!=null && field.getFieldLength().indexOf(',')!=-1 && !field.getFieldLength().endsWith(",0")) {
				type = "double";
			}
			else if(field.getFieldLength()!=null && Integer.parseInt(field.getFieldLength())>10) {
				type = "long";
			}
			return type;
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.service.ApplicationBuilder#getFormTemplate(java.lang.String)
	 */
	public FormTemplate getFormTemplate(String templateName) {
		return (FormTemplate)ListUtils.findObjectByProperty(formTemplates, "name", templateName);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.service.ApplicationBuilder#redeployment()
	 */
	public void redeployment() throws ServiceException {
		//获取已经生成过的应用
		String hql = "from Application Application where not Application.buildTime is null";
		List applications = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("navigators")); 
		if(applications==null || applications.isEmpty()) {
			return;
		}
		for(Iterator iterator = applications.iterator(); iterator.hasNext();) {
			//获取应用
			Application application = (Application)iterator.next();
			String applicationName = "customise/" + application.getEnglishName().toLowerCase();
			//获取表单列表
			List applicationForms = getDatabaseService().findRecordsByHql("from ApplicationForm ApplicationForm where ApplicationForm.applicationId=" + application.getId() + " order by ApplicationForm.id", ListUtils.generateList("fields,views,indexes", ","));
			if(applicationForms==null) {
				continue;
			}
			//更新WEB-INF/applications-config.xml
			updateApplicationsConfig(application, applicationName, applicationForms);
			//更新WEB-INF/hibernate-config.xml
			databaseDefineService.registApplication(applicationName);
			//更新WEB-INF/web.xml
			strutsDefineService.registApplication(applicationName);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.service.ApplicationBuilder#listFormTemplates()
	 */
	public List listFormTemplates() {
		return formTemplates;
	}
	
	/**
	 * @return the javacExeFilePath
	 */
	public String getJavacExeFilePath() {
		return javacExeFilePath;
	}

	/**
	 * @param javacExeFilePath the javacExeFilePath to set
	 */
	public void setJavacExeFilePath(String javacExeFilePath) {
		this.javacExeFilePath = javacExeFilePath;
	}

	/**
	 * @return the applicationNavigatorService
	 */
	public ApplicationNavigatorService getApplicationNavigatorService() {
		return applicationNavigatorService;
	}

	/**
	 * @param applicationNavigatorService the applicationNavigatorService to set
	 */
	public void setApplicationNavigatorService(
			ApplicationNavigatorService applicationNavigatorService) {
		this.applicationNavigatorService = applicationNavigatorService;
	}

	/**
	 * @return the formDefineService
	 */
	public FormDefineService getFormDefineService() {
		return formDefineService;
	}

	/**
	 * @param formDefineService the formDefineService to set
	 */
	public void setFormDefineService(FormDefineService formDefineService) {
		this.formDefineService = formDefineService;
	}

	/**
	 * @return the viewDefineService
	 */
	public ViewDefineService getViewDefineService() {
		return viewDefineService;
	}

	/**
	 * @param viewDefineService the viewDefineService to set
	 */
	public void setViewDefineService(ViewDefineService viewDefineService) {
		this.viewDefineService = viewDefineService;
	}

	/**
	 * @return the databaseDefineService
	 */
	public DatabaseDefineService getDatabaseDefineService() {
		return databaseDefineService;
	}

	/**
	 * @param databaseDefineService the databaseDefineService to set
	 */
	public void setDatabaseDefineService(DatabaseDefineService databaseDefineService) {
		this.databaseDefineService = databaseDefineService;
	}

	/**
	 * @return the workflowConfigureService
	 */
	public WorkflowConfigureService getWorkflowConfigureService() {
		return workflowConfigureService;
	}

	/**
	 * @param workflowConfigureService the workflowConfigureService to set
	 */
	public void setWorkflowConfigureService(
			WorkflowConfigureService workflowConfigureService) {
		this.workflowConfigureService = workflowConfigureService;
	}

	/**
	 * @return the modelCache
	 */
	public Cache getModelCache() {
		return modelCache;
	}

	/**
	 * @param modelCache the modelCache to set
	 */
	public void setModelCache(Cache modelCache) {
		this.modelCache = modelCache;
	}

	/**
	 * @return the htmlParser
	 */
	public HTMLParser getHtmlParser() {
		return htmlParser;
	}

	/**
	 * @param htmlParser the htmlParser to set
	 */
	public void setHtmlParser(HTMLParser htmlParser) {
		this.htmlParser = htmlParser;
	}

	/**
	 * @return the strutsDefineService
	 */
	public StrutsDefineService getStrutsDefineService() {
		return strutsDefineService;
	}

	/**
	 * @param strutsDefineService the strutsDefineService to set
	 */
	public void setStrutsDefineService(StrutsDefineService strutsDefineService) {
		this.strutsDefineService = strutsDefineService;
	}

	/**
	 * @return the formTemplates
	 */
	public List getFormTemplates() {
		return formTemplates;
	}

	/**
	 * @param formTemplates the formTemplates to set
	 */
	public void setFormTemplates(List formTemplates) {
		this.formTemplates = formTemplates;
	}

	/**
	 * @return the businessDefineService
	 */
	public BusinessDefineService getBusinessDefineService() {
		return businessDefineService;
	}

	/**
	 * @param businessDefineService the businessDefineService to set
	 */
	public void setBusinessDefineService(BusinessDefineService businessDefineService) {
		this.businessDefineService = businessDefineService;
	}

	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	/**
	 * @return the pageDefineService
	 */
	public PageDefineService getPageDefineService() {
		return pageDefineService;
	}

	/**
	 * @param pageDefineService the pageDefineService to set
	 */
	public void setPageDefineService(PageDefineService pageDefineService) {
		this.pageDefineService = pageDefineService;
	}
}