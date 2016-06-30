package com.yuanluesoft.portal.container.parser.dom4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.portal.container.model.PortalApplication;
import com.yuanluesoft.portal.container.model.PortletDefinition;
import com.yuanluesoft.portal.container.model.PortletParameter;
import com.yuanluesoft.portal.container.model.PortletPreference;
import com.yuanluesoft.portal.container.model.PortletSupport;
import com.yuanluesoft.portal.container.parser.PortletDefinitionParser;

/**
 * 
 * @author linchuan
 *
 */
public class PortletDefinitionParserImpl extends XmlParser implements PortletDefinitionParser {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.portal.container.parser.PortletDefinitionParser#parsePortlets(java.lang.String, java.lang.String)
	 */
	public PortalApplication parsePortlets(String applicationName, String defineFileName) throws ParseException {
		try {
			return parsePortalApplication(applicationName, parseXmlFile(defineFileName));
		} 
		catch (ParseException e) {
		    return null;
		}
	}
	
	/**
	 * 根据XML配置解析portlet定义
	 * @param applicationName
	 * @param xmlDefineRoot
	 * @return
	 * @throws ParseException
	 */
	private PortalApplication parsePortalApplication(String applicationName, final Element xmlDefineRoot) throws ParseException {
		PortalApplication portalApplication = new PortalApplication();
		List portletDefinitions = new ArrayList();
		for(Iterator iterator = xmlDefineRoot.elementIterator(); iterator.hasNext();) {
			Element xmlElement = (Element)iterator.next();
			portletDefinitions.add(parsePortletDefinition(applicationName, xmlElement));
		}
		portalApplication.setPortletDefinitions(portletDefinitions);
		return portalApplication;
	}
	
	/**
	 * 解析portlet
	 * @param xmlElement
	 * @return
	 * @throws ParseException
	 */
	private PortletDefinition parsePortletDefinition(String applicationName, Element xmlElement) throws ParseException {
		PortletDefinition portletDefinition = new PortletDefinition();
		portletDefinition.setDescription(xmlElement.elementText("description"));  //描述
		portletDefinition.setPortletName(xmlElement.elementText("portlet-name")); //名称
		portletDefinition.setDisplayName(xmlElement.elementText("display-name")); //显示名称
		portletDefinition.setPortletClass(xmlElement.elementText("portlet-class")); //类名称
		portletDefinition.setResourceBundle(xmlElement.elementText("resource-bundle")); //国际化资源文件

		//解析初始化参数列表
		List initParameters = new ArrayList();
		for(Iterator iterator = xmlElement.elementIterator("init-param"); iterator.hasNext();) {
			Element paramElement = (Element)iterator.next();
			PortletParameter parameter = new PortletParameter();
			parameter.setDescription(paramElement.elementText("description"));
			parameter.setName(paramElement.elementText("name"));
			parameter.setValue(paramElement.elementText("value"));
			initParameters.add(parameter);
		}
		portletDefinition.setInitParameters(initParameters.isEmpty() ? null : initParameters); //初始化参数列表
		
		//解析支持的模式列表
		List supports = new ArrayList();
		for(Iterator iterator = xmlElement.elementIterator("supports"); iterator.hasNext();) {
			Element xmlSupports = (Element)iterator.next();
			PortletSupport portletSupport = new PortletSupport();
			//解析支持的MIME类型
			portletSupport.setMimeType(xmlSupports.elementText("mime-type"));
			//解析支持的模式
			portletSupport.setPortletModes(new ArrayList());
			for(Iterator iteratorMode = xmlSupports.elementIterator("portlet-mode"); iteratorMode.hasNext();) {
				Element modeElement = (Element)iteratorMode.next();
				portletSupport.getPortletModes().add(modeElement.getText().toLowerCase());
			}
			supports.add(portletSupport);
		}
		portletDefinition.setSupports(supports.isEmpty() ? null : supports); //支持的模式
		
		//解析支持的地区
		List supportedLocales = new ArrayList();
		for(Iterator iterator = xmlElement.elementIterator("supported-locale"); iterator.hasNext();) {
			Element xmlLocale = (Element)iterator.next();
			supportedLocales.add(xmlLocale.getText());
		}
		portletDefinition.setSupportedLocales(supportedLocales.isEmpty() ? null : supportedLocales); //支持的区域
		
		//解析个性化设置参数列表
		Element xmlPreferences = xmlElement.element("portlet-preferences");
		if(xmlPreferences!=null) {
			List preferences = new ArrayList();
			for(Iterator iterator = xmlPreferences.elementIterator("preference"); iterator.hasNext();) {
				Element preferenceElement = (Element)iterator.next();
				PortletPreference preference = new PortletPreference(preferenceElement.elementText("name"), preferenceElement.elementText("value"), "true".equals(preferenceElement.elementText("read-only")));
				preferences.add(preference);
			}
			portletDefinition.setPreferences(preferences.isEmpty() ? null : preferences); //个性化设置参数列表
		}
		return portletDefinition;
	}
}