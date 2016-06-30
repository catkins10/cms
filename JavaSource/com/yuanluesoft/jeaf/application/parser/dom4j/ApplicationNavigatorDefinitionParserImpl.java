package com.yuanluesoft.jeaf.application.parser.dom4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.yuanluesoft.jeaf.application.model.navigator.definition.ApplicationNavigatorDefinition;
import com.yuanluesoft.jeaf.application.model.navigator.definition.Link;
import com.yuanluesoft.jeaf.application.model.navigator.definition.ViewLink;
import com.yuanluesoft.jeaf.application.parser.ApplicationNavigatorDefinitionParser;
import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class ApplicationNavigatorDefinitionParserImpl extends XmlParser  implements ApplicationNavigatorDefinitionParser {
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.parser.ApplicationNavigatorDefinitionParser#parse(java.lang.String)
	 */
	public ApplicationNavigatorDefinition parse(final String defineFileName) throws ParseException {
		return parse(parseXmlFile(defineFileName));
	}
	
	/**
	 * 解析导航配置
	 * @param xmlDefineRoot
	 * @return
	 * @throws ParseException
	 */
	public ApplicationNavigatorDefinition parse(final Element xmlDefineRoot) throws ParseException {
		ApplicationNavigatorDefinition navigatorDefine = new ApplicationNavigatorDefinition();
		navigatorDefine.setNavigatorServiceName(xmlDefineRoot.attributeValue("service")); //服务名称
		navigatorDefine.setNavigatorURL(xmlDefineRoot.attributeValue("navigatorURL")); //自定义导航页面的URL
		//解析链接列表
		List links = new ArrayList();
		for(Iterator iterator = xmlDefineRoot.elementIterator(); iterator.hasNext();) {
			Element xmlItem = (Element)iterator.next(); 
			String name = xmlItem.getName();
			if(name.equals("link")) {
				links.add(parseLink(xmlItem));
			}
			else if(name.equals("viewLink")) {
				links.add(parseViewLink(xmlItem));
			}
		}
		navigatorDefine.setLinks(links);
		return navigatorDefine;
	}

	/**
	 * 根据链接XML配置解析链接定义
	 * @param group
	 * @param xmlLink
	 * @return
	 * @throws ParseException
	 */
	private Link parseLink(Element xmlLink) throws ParseException {
		Link link = new Link();
		link.setTitle(xmlLink.attributeValue("title")); //标题
		link.setHideCondition(xmlLink.attributeValue("hide")); //隐藏条件
		link.setIconURL(xmlLink.attributeValue("iconURL")); //图标URL
		String href = xmlLink.getText().trim();
        href = href.replaceAll("\\x7bCONTEXTPATH\\x7d", Environment.getContextPath()).replaceAll("\\x7bWEBAPPLICATIONURL\\x7d", Environment.getWebApplicationUrl()).replaceAll("\\x7bWEBAPPLICATIONSAFEURL\\x7d", Environment.getWebApplicationSafeUrl());
	    if(!href.toLowerCase().startsWith("http") && !href.toLowerCase().startsWith("javascript")) {
        	href = Environment.getWebApplicationUrl() + href;
        }
        link.setHref(href); //链接地址
        if(href.startsWith("javascript")) {
        	link.setTarget("_self");
        }
        else {
        	link.setTarget(xmlLink.attributeValue("target"));
        }
		return link;
	}
	
	/**
	 * 解析视图链接
	 * @param xmlViewLink
	 * @return
	 * @throws BaseParseException
	 */
	public ViewLink parseViewLink(Element xmlViewLink) throws ParseException {
		ViewLink viewLink = new ViewLink();
		viewLink.setLocal(xmlViewLink.attributeValue("local"));
		viewLink.setApplication(xmlViewLink.attributeValue("application"));
		viewLink.setIconURL(xmlViewLink.attributeValue("iconURL")); //图标URL
		viewLink.setView(viewLink.getLocal()==null ? xmlViewLink.attributeValue("view") : viewLink.getLocal());
		viewLink.setTitle(xmlViewLink.attributeValue("title"));
		return viewLink;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.parser.ApplicationNavigatorDefinitionParser#saveApplicationNavigatorDefinition(com.yuanluesoft.jeaf.application.model.navigator.definition.ApplicationNavigatorDefinition, java.lang.String)
	 */
	public void saveApplicationNavigatorDefinition(ApplicationNavigatorDefinition navigatorDefinition, String defineFileName) throws ParseException {
		Document xmlDocument = DocumentHelper.createDocument();
		Element xmlApplication = xmlDocument.addElement("application");
		xmlApplication.addAttribute("service", navigatorDefinition.getNavigatorServiceName()); //服务名称
		xmlApplication.addAttribute("navigatorURL", navigatorDefinition.getNavigatorURL()); //自定义导航页面的URL
		for(Iterator iterator = navigatorDefinition.getLinks()==null ? null : navigatorDefinition.getLinks().iterator(); iterator!=null && iterator.hasNext();) {
			Object navigatorLink = iterator.next();
			if(navigatorLink instanceof ViewLink) {
				generateViewLinkXML((ViewLink)navigatorLink, xmlApplication);
			}
			else if(navigatorLink instanceof Link) {
				parseLink((Link)navigatorLink, xmlApplication);
			}
		}
		//保存XML文件
		saveXmlFile(xmlDocument, defineFileName);
	}
	
	/**
	 * 生成视图链接XML
	 * @param viewLink
	 * @param parentElement
	 * @return
	 * @throws ParseException
	 */
	public Element generateViewLinkXML(ViewLink viewLink, Element parentElement) throws ParseException {
		Element xmlViewLink = parentElement.addElement("viewLink");
		xmlViewLink.addAttribute("local", viewLink.getLocal()); //从本系统中引入的视图
		xmlViewLink.addAttribute("iconURL", viewLink.getIconURL());  //视图图标URL
		xmlViewLink.addAttribute("application", viewLink.getApplication()); //应用名称
		xmlViewLink.addAttribute("view", viewLink.getView()); //视图名称
		xmlViewLink.addAttribute("title", viewLink.getTitle()); //标题
		return xmlViewLink;
	}
	
	/**
	 * 生成链接XML
	 * @param link
	 * @param parentElement
	 * @return
	 * @throws ParseException
	 */
	private Element parseLink(Link link, Element parentElement) throws ParseException {
		Element xmlLink = parentElement.addElement("link");
		xmlLink.addAttribute("title", link.getTitle()); //标题
		xmlLink.addAttribute("hide", link.getHideCondition()); //隐藏条件
		xmlLink.addAttribute("iconURL", link.getIconURL()); //图标URL
		xmlLink.addAttribute("target", link.getTarget()); //链接目标窗口
		xmlLink.setText(link.getHref());
		return xmlLink;
	}
}