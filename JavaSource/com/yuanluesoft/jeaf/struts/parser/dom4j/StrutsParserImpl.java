package com.yuanluesoft.jeaf.struts.parser.dom4j;

import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.ModuleConfig;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultDocumentType;

import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.struts.parser.StrutsParser;

/**
 * 
 * @author linchuan
 *
 */
public class StrutsParserImpl extends XmlParser implements StrutsParser {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.struts.parser.StrutsParser#saveStrutsModuleConfig(org.apache.struts.config.ModuleConfig, java.lang.String)
	 */
	public void saveStrutsModuleConfig(ModuleConfig moduleConfig, String defineFileName) throws ParseException {
		Document xmlDocument = DocumentHelper.createDocument();
		xmlDocument.setDocType(new DefaultDocumentType("struts-config", "-//Apache Software Foundation//DTD Struts Configuration 1.1//EN",  "http://jakarta.apache.org/struts/dtds/struts-config_1_1.dtd"));
		Element xmlStrutsConfig = xmlDocument.addElement("struts-config");
		xmlStrutsConfig.addElement("data-sources");
		//处理form bean列表
		Element xmlFormBeans = xmlStrutsConfig.addElement("form-beans");
		FormBeanConfig[] formBeanConfigs = moduleConfig.findFormBeanConfigs();
		for(int i=0; i<(formBeanConfigs==null ? 0 : formBeanConfigs.length); i++) {
			Element xmlFormBean = xmlFormBeans.addElement("form-bean");
			xmlFormBean.addAttribute("name", formBeanConfigs[i].getName());
			xmlFormBean.addAttribute("type", formBeanConfigs[i].getType());
		}
		//处理全局异常
		Element xmlGlobalExceptions = xmlStrutsConfig.addElement("global-exceptions");
		ExceptionConfig[] globalExceptions = moduleConfig.findExceptionConfigs();
		for(int i=0; i<(globalExceptions==null ? 0 : globalExceptions.length); i++) {
			Element xmlException = xmlGlobalExceptions.addElement("exception");
			xmlException.addAttribute("key", globalExceptions[i].getKey());
			xmlException.addAttribute("path", globalExceptions[i].getPath());
			xmlException.addAttribute("scope", globalExceptions[i].getScope()==null ? "request" : globalExceptions[i].getScope());
			xmlException.addAttribute("type", globalExceptions[i].getType()==null ? "java.lang.Exception" : globalExceptions[i].getType());
		}
		//处理全局FORWARD
		Element xmlGlobalForwards = xmlStrutsConfig.addElement("global-forwards");
		ForwardConfig[] globalForwards = moduleConfig.findForwardConfigs();
		for(int i=0; i<(globalForwards==null ? 0 : globalForwards.length); i++) {
			Element xmlForward = xmlGlobalForwards.addElement("forward");
			xmlForward.addAttribute("name", globalForwards[i].getName());
			xmlForward.addAttribute("path", globalForwards[i].getPath());
		}
		/*
		<action input="/templateConfig.jsp" name="templateConfig"
			   path="/saveTemplateConfig" scope="request"
			   type="com.yuanluesoft.j2oa.dispatch.actions.templateconfig.Save" validate="false"/>
			 */
		//处理action列表
		Element xmlActionMappings = xmlStrutsConfig.addElement("action-mappings");
		ActionConfig[] actionConfigs = moduleConfig.findActionConfigs();
		for(int i=0; i<(actionConfigs==null ? 0 : actionConfigs.length); i++) {
			Element xmlAction = xmlActionMappings.addElement("action");
			xmlAction.addAttribute("input", actionConfigs[i].getInput());
			xmlAction.addAttribute("name", actionConfigs[i].getName());
			xmlAction.addAttribute("path", actionConfigs[i].getPath());
			xmlAction.addAttribute("scope", actionConfigs[i].getScope()==null ? "request" : actionConfigs[i].getScope());
			xmlAction.addAttribute("type", actionConfigs[i].getType());
			xmlAction.addAttribute("validate", "" + actionConfigs[i].getValidate());
			ForwardConfig[] actionForwardConfigs = actionConfigs[i].findForwardConfigs();
			for(int j=0; j<(actionForwardConfigs==null ? 0 : actionForwardConfigs.length); j++) {
				Element xmlForward = xmlAction.addElement("forward");
				xmlForward.addAttribute("name", actionForwardConfigs[j].getName());
				xmlForward.addAttribute("path", actionForwardConfigs[j].getPath());
			}
		}
		//保存XML文件
		saveXmlFile(xmlDocument, defineFileName);
	}
}