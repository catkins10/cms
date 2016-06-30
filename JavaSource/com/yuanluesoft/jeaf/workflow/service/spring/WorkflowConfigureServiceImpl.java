package com.yuanluesoft.jeaf.workflow.service.spring;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.model.Form;
import com.yuanluesoft.jeaf.form.service.FormDefineService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sessionmanage.service.SessionService;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.workflow.connectionpool.WorkflowConnectionPool;
import com.yuanluesoft.jeaf.workflow.service.WorkflowConfigureService;
import com.yuanluesoft.workflow.client.WorkflowClient;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigurator;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;
import com.yuanluesoft.workflow.client.exception.WorkflowException;
import com.yuanluesoft.workflow.client.model.resource.Action;
import com.yuanluesoft.workflow.client.model.resource.Application;
import com.yuanluesoft.workflow.client.model.resource.DataField;
import com.yuanluesoft.workflow.client.model.resource.Enumeration;
import com.yuanluesoft.workflow.client.model.resource.FormalParameter;
import com.yuanluesoft.workflow.client.model.resource.ProgrammingParticipant;
import com.yuanluesoft.workflow.client.model.resource.SubForm;
import com.yuanluesoft.workflow.client.model.resource.WorkflowResource;

/**
 * 
 * @author linchuan
 *
 */
public class WorkflowConfigureServiceImpl implements WorkflowConfigureService {
	private WorkflowConnectionPool workflowConnectionPool; //工作流连接池
	private String workflowConfigurePassword; //工作流配置密码
	private String workflowConfigureURL; //工作流配置的链接
	private PersonService personService; //用户服务
	private SessionService sessionService; //会话服务
	private FormDefineService formDefineService; //表单定义服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.service.WorkflowConfigureService#createWorkflow(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void createWorkflow(String applicationName, String formName, String workflowResourceFileName, String notifyURL, String returnURL, String testURL, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException {
		WorkflowConfigurator workflowConfigurator = new WorkflowConfigurator();
		WorkflowClient workflowClient = workflowConnectionPool.getWorkflowClient(sessionInfo);
		try {
			workflowConfigurator.createWorkflow(workflowClient, workflowConfigurePassword, getWorkflowResourceXML(applicationName, formName, workflowResourceFileName), getWorkflowConfigureURL(), notifyURL, returnURL, testURL, response);
		}
		catch (WorkflowException e) {
			throw new ServiceException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.service.WorkflowConfigureService#editWorkflow(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void editWorkflow(String applicationName, String formName, String workflowResourceFileName, String workflowId, String notifyURL, String returnURL, String testURL, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException {
		WorkflowConfigurator workflowConfigurator = new WorkflowConfigurator();
		WorkflowClient workflowClient = workflowConnectionPool.getWorkflowClient(sessionInfo);
		try {
			workflowConfigurator.editWorkflow(workflowClient, workflowConfigurePassword, getWorkflowResourceXML(applicationName, formName, workflowResourceFileName), workflowId, getWorkflowConfigureURL(), notifyURL, returnURL, testURL, response);
		}
		catch (WorkflowException e) {
			throw new ServiceException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.service.WorkflowConfigureService#editSubFlow(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void editSubFlow(String applicationName, String formName, String workflowResourceFileName, String workflowId, String subFlowProcessId, String notifyURL, String returnURL, HttpServletResponse response, SessionInfo sessionInfo) throws ServiceException {
		WorkflowConfigurator workflowConfigurator = new WorkflowConfigurator();
		WorkflowClient workflowClient = workflowConnectionPool.getWorkflowClient(sessionInfo);
		try {
			workflowConfigurator.editSubFlow(workflowClient, workflowConfigurePassword, getWorkflowResourceXML(applicationName, formName, workflowResourceFileName), workflowId, subFlowProcessId, getWorkflowConfigureURL(), notifyURL, returnURL, response);
		}
		catch (WorkflowException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 获取工作流资源XML
	 * @param applicationName
	 * @param formName
	 * @param workflowResourceFileName
	 * @return
	 * @throws ServiceException
	 */
	private String getWorkflowResourceXML(String applicationName, String formName, String workflowResourceFileName) throws ServiceException {
		String workflowResourceXML = FileUtils.readStringFromFile(Environment.getWebinfPath() + applicationName + "/" + (workflowResourceFileName==null ? "resource-config.xml" : workflowResourceFileName), "UTF-8");
		//获取表单定义
		List formDefines = formDefineService.listFormDefines(applicationName);
		Form formDefine = (Form)(formName==null ? formDefines.get(0) : ListUtils.findObjectByProperty(formDefines, "name", formName));
		//获取字段列表
		List fields  = FieldUtils.listFormFields(formDefine, "opinion", null, null, null, false, false, false, false, 1);
		//插入意见操作
		String opinionActions = "";
		for(Iterator iterator = fields==null ? null : fields.iterator(); iterator!=null && iterator.hasNext();) {
			Field field = (Field)iterator.next();
			opinionActions += "<Action Prompt=\"" + field.getParameter("prompt") + "\"" + (field.isRequired() ? " Necessary=\"true\"" : "") + " Name=\"填写" + field.getTitle() + "\"/>";
		}
		if(!opinionActions.equals("")) {
			int index = workflowResourceXML.indexOf("</Actions>");
			if(index!=-1) {
				workflowResourceXML = workflowResourceXML.substring(0, index) + opinionActions + workflowResourceXML.substring(index);
			}
			else {
				index = workflowResourceXML.lastIndexOf("</");
				workflowResourceXML = workflowResourceXML.substring(0, index) + "<Actions>" + opinionActions + "</Actions>" + workflowResourceXML.substring(index);
			}
		}
		return workflowResourceXML;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.service.WorkflowConfigureService#processNotifyRequest(com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void processNotifyRequest(WorkflowConfigureCallback configureCallback, HttpServletRequest request) throws ServiceException {
		try {
			//获取用户会话
			Person user = personService.getPerson(RequestUtils.getParameterLongValue(request, "userId"));
			SessionInfo sessionInfo;
			sessionInfo = sessionService.getSessionInfo(user.getLoginName());
			WorkflowClient workflowClient = workflowConnectionPool.getWorkflowClient(sessionInfo);
			//调用工作流配置器处理消息通知
			WorkflowConfigurator workflowConfigurator = new WorkflowConfigurator();
			workflowConfigurator.processNotifyRequest(workflowClient, workflowConfigurePassword, configureCallback, request);
		} 
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.workflow.service.WorkflowConfigureService#saveWorkflowResourceDefine(java.lang.String, com.yuanluesoft.workflow.client.model.resource.WorkflowResource)
	 */
	public void saveWorkflowResourceDefine(String applicationName, WorkflowResource workflowResource) throws ServiceException {
		Document xmlDocument = DocumentHelper.createDocument();
		Element xmlApplication = xmlDocument.addElement("Application");
		//处理操作列表
		if(workflowResource.getActions()!=null && !workflowResource.getActions().isEmpty()) {
			Element xmlActions = xmlApplication.addElement("Actions");
			for(Iterator iterator = workflowResource.getActions().iterator(); iterator!=null && iterator.hasNext();) {
				Action action = (Action)iterator.next();
				Element xmlAction = xmlActions.addElement("Action");
				xmlAction.addAttribute("Name", action.getName()); //名称
				xmlAction.addAttribute("Prompt", action.getPrompt()); //未执行时的提示
				if(action.isNecessary()) { //是否必须执行
					xmlAction.addAttribute("Necessary", "true");
				}
				if(action.isPromptOnCreate()) { //创建时是否需要在未执行时的提示
					xmlAction.addAttribute("PromptOnCreate", "true");
				}
				if(action.isDeleteAction()) { //是否删除操作
					xmlAction.addAttribute("DeleteAction", "true");
				}
				if(action.isSend()) { //操作中是否包含流程发送操作
					xmlAction.addAttribute("Send", "true");
				}
				if(action.isReverse()) { //操作中是否包含流程回退操作
					xmlAction.addAttribute("Reverse", "true");
				}
				if(action.isTransmit()) { //操作中是否包含转办操作
					xmlAction.addAttribute("Transmit", "true");
				}
			}
		}
		//处理编程决定的办理人
		if(workflowResource.getProgrammingParticipants()!=null && !workflowResource.getProgrammingParticipants().isEmpty()) {
			Element xmlProgrammingParticipants = xmlApplication.addElement("ProgrammingParticipants");
			for(Iterator iterator = workflowResource.getProgrammingParticipants().iterator(); iterator!=null && iterator.hasNext();) {
				ProgrammingParticipant programmingParticipant = (ProgrammingParticipant)iterator.next();
				Element xmlProgrammingParticipant = xmlProgrammingParticipants.addElement("ProgrammingParticipant");
				xmlProgrammingParticipant.addAttribute("Id", programmingParticipant.getId()); //ID
				xmlProgrammingParticipant.addAttribute("Name", programmingParticipant.getName()); //名称
			}
		}
		//处理子表单列表
		if(workflowResource.getSubForms()!=null && !workflowResource.getSubForms().isEmpty()) {
			Element xmlSubForms = xmlApplication.addElement("SubForms");
			for(Iterator iterator = workflowResource.getSubForms().iterator(); iterator!=null && iterator.hasNext();) {
				SubForm subForm = (SubForm)iterator.next();
				Element xmlSubForm = xmlSubForms.addElement("SubForm");
				xmlSubForm.addAttribute("Id", subForm.getId()); //ID
				xmlSubForm.addAttribute("Name", subForm.getName()); //名称
			}
		}
		//处理枚举类型列表
		if(workflowResource.getEnumerations()!=null && !workflowResource.getEnumerations().isEmpty()) {
			Element xmlTypeDeclarations = xmlApplication.addElement("TypeDeclarations");
			for(Iterator iterator = workflowResource.getEnumerations().iterator(); iterator!=null && iterator.hasNext();) {
				Enumeration enumeration = (Enumeration)iterator.next();
				Element xmlTypeDeclaration = xmlTypeDeclarations.addElement("TypeDeclaration");
				xmlTypeDeclaration.addAttribute("Id", enumeration.getId()); //ID
				xmlTypeDeclaration.addAttribute("Name", enumeration.getName()); //名称
				Element xsdSchema = xmlTypeDeclaration.addElement("SchemaType").addElement("xsd:schema");
				xsdSchema.addAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
				xsdSchema.addAttribute("elementFormDefault", "qualified");
				xsdSchema.addAttribute("attributeFormDefault", "unqualified");
				Element xsdElement = xsdSchema.addElement("xsd:element");
				xsdElement.addAttribute("name", enumeration.getName());
				Element xsdRestriction = xsdElement.addElement("xsd:simpleType").addElement("xsd:restriction");
				xsdRestriction.addAttribute("base", "xsd:NMTOKEN");
				for(Iterator iteratorValue = enumeration.getValueList().iterator(); iteratorValue.hasNext();) {
					String value = (String)iteratorValue.next();
					xsdRestriction.addElement("xsd:enumeration").addAttribute("value", value);
				}
			}
		}
		//处理字段列表
		if(workflowResource.getDataFields()!=null && !workflowResource.getDataFields().isEmpty()) {
			Element xmlDataFields = xmlApplication.addElement("DataFields");
			for(Iterator iterator = workflowResource.getDataFields().iterator(); iterator.hasNext();) {
				DataField dataField = (DataField)iterator.next();
				Element xmlDataField = xmlDataFields.addElement("DataField");
				xmlDataField.addAttribute("Id", dataField.getId()); //ID
				xmlDataField.addAttribute("Name", dataField.getName()); //名称
				xmlDataField.addElement("DataType").addElement(dataField.isBasicType() ? "BasicType" : "DeclaredType").addAttribute(dataField.isBasicType() ? "Type" : "Id", dataField.getType());
				if(dataField.getInitialValue()!=null && !dataField.getInitialValue().isEmpty()) {
					xmlDataField.addElement("InitialValue").setText(dataField.getInitialValue());
				}
				if(dataField.getLength()>0) {
					xmlDataField.addElement("Length").setText("" + dataField.getLength());
				}
				//扩展属性
				Element xmlExtendedAttributes = xmlDataField.addElement("ExtendedAttributes");
				if(dataField.isSign()) { //是否签名字段
					Element xmlExtendedAttribute = xmlExtendedAttributes.addElement("ExtendedAttribute");
					xmlExtendedAttribute.addAttribute("Name", "sign");
					xmlExtendedAttribute.setText("true");
				}
				if(dataField.isMultiSign()) { //是否多人签名
					addExtendedAttribute("multiSign", "true", xmlExtendedAttributes); 
				}
				addExtendedAttribute("trueTitle", dataField.getTrueTitle(), xmlExtendedAttributes); //当类型为BOOLEAN时,值为true时的标题
				addExtendedAttribute("falseTitle", dataField.getFalseTitle(), xmlExtendedAttributes); //当类型为BOOLEAN时,值为false时的标题
				if(xmlExtendedAttributes.elements()==null || xmlExtendedAttributes.elements().isEmpty()) {
					xmlExtendedAttributes.getParent().remove(xmlExtendedAttributes);
				}
			}
		}
		//处理过程列表
		if(workflowResource.getApplications()!=null && !workflowResource.getApplications().isEmpty()) {
			Element xmlApplications = xmlApplication.addElement("Applications");
			for(Iterator iterator = workflowResource.getApplications().iterator(); iterator!=null && iterator.hasNext();) {
				Application application = (Application)iterator.next();
				Element xmlApp = xmlApplications.addElement("Application");
				xmlApp.addAttribute("Id", application.getId()); //ID
				xmlApp.addAttribute("Name", application.getName()); //名称
				if(application.getFormalParameterList()!=null && !application.getFormalParameterList().isEmpty()) {
					Element xmlFormalParameters = xmlApp.addElement("FormalParameters");
					for(Iterator iteratorParameter = application.getFormalParameterList().iterator(); iteratorParameter.hasNext();) {
						FormalParameter formalParameter = (FormalParameter)iteratorParameter.next();
						Element xmlFormalParameter = xmlFormalParameters.addElement("FormalParameter");
						xmlFormalParameter.addAttribute("id", formalParameter.getId()); //ID
						xmlFormalParameter.addAttribute("index", "" + formalParameter.getIndex()); //参数序号
						xmlFormalParameter.addAttribute("mode", formalParameter.getMode()); //IN,OUT,INOUT
						xmlFormalParameter.addElement("DataType").addElement(formalParameter.isBasicType() ? "BasicType" : "DeclaredType").addAttribute(formalParameter.isBasicType() ? "Type" : "Id", formalParameter.getType());
						if(formalParameter.getDescription()!=null && !formalParameter.getDescription().isEmpty()) { //描述
							xmlFormalParameter.addElement("Description").setText(formalParameter.getDescription());
						}
						//扩展属性
						Element xmlExtendedAttributes = xmlFormalParameter.addElement("ExtendedAttributes");
						addExtendedAttribute("trueTitle", formalParameter.getTrueTitle(), xmlExtendedAttributes); //当类型为BOOLEAN时,值为true时的标题
						addExtendedAttribute("falseTitle", formalParameter.getFalseTitle(), xmlExtendedAttributes); //当类型为BOOLEAN时,值为false时的标题
						if(xmlExtendedAttributes.elements()==null || xmlExtendedAttributes.elements().isEmpty()) {
							xmlExtendedAttributes.getParent().remove(xmlExtendedAttributes);
						}
					}
				}
				Element xmlExtendedAttributes = xmlApp.addElement("ExtendedAttributes");
				addExtendedAttribute("type", application.getType(), xmlExtendedAttributes); //类型:procedure/内部过程,application/外部应用,decision/判断
				addExtendedAttribute("finishMode", application.getFinishMode(), xmlExtendedAttributes); //结束方式:Automatic/自动,Manual/人工
				addExtendedAttribute("service", application.getService(), xmlExtendedAttributes); //服务名称
				addExtendedAttribute("function", application.getFunction(), xmlExtendedAttributes); //函数名称
			}
		}
		//保存XML文件
		try {
			new XmlParser().saveXmlFile(xmlDocument, FileUtils.createDirectory(Environment.getWebinfPath() + applicationName) + "resource-config.xml");
		}
		catch (ParseException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 添加扩展属性
	 * @param name
	 * @param value
	 * @param parentElement
	 */
	private void addExtendedAttribute(String name, String value, Element parentElement) {
		if(value!=null && !value.isEmpty()) { //函数名称
			Element xmlExtendedAttribute = parentElement.addElement("ExtendedAttribute");
			xmlExtendedAttribute.addAttribute("Name", name);
			xmlExtendedAttribute.setText(value);
		}
	}

	/**
	 * @return the workflowConfigurePassword
	 */
	public String getWorkflowConfigurePassword() {
		return workflowConfigurePassword;
	}

	/**
	 * @param workflowConfigurePassword the workflowConfigurePassword to set
	 */
	public void setWorkflowConfigurePassword(String workflowConfigurePassword) {
		this.workflowConfigurePassword = workflowConfigurePassword;
	}

	/**
	 * @return the workflowConnectionPool
	 */
	public WorkflowConnectionPool getWorkflowConnectionPool() {
		return workflowConnectionPool;
	}

	/**
	 * @param workflowConnectionPool the workflowConnectionPool to set
	 */
	public void setWorkflowConnectionPool(
			WorkflowConnectionPool workflowConnectionPool) {
		this.workflowConnectionPool = workflowConnectionPool;
	}

	/**
	 * @return the workflowConfigureURL
	 */
	public String getWorkflowConfigureURL() {
		if(workflowConfigureURL==null) {
			workflowConfigureURL = Environment.getContextPath() + "/workflow/workflowConfigure.shtml"; //默认就是本地
		}
		return workflowConfigureURL;
	}

	/**
	 * @param workflowConfigureURL the workflowConfigureURL to set
	 */
	public void setWorkflowConfigureURL(String workflowConfigureURL) {
		this.workflowConfigureURL = workflowConfigureURL;
	}

	/**
	 * @return the personService
	 */
	public PersonService getPersonService() {
		return personService;
	}

	/**
	 * @param personService the personService to set
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService() {
		return sessionService;
	}

	/**
	 * @param sessionService the sessionService to set
	 */
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
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
}