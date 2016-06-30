package com.yuanluesoft.portal.server.parser.dom4j;

import java.util.ArrayList;
import java.util.Iterator;

import javax.portlet.WindowState;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.portal.server.model.Portal;
import com.yuanluesoft.portal.server.model.PortalPage;
import com.yuanluesoft.portal.server.model.PortletInstance;
import com.yuanluesoft.portal.server.parser.PortalParser;

/**
 * 
 * @author linchuan
 *
 */
public class PortalParserImpl extends XmlParser implements PortalParser {

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.portal.container.parser.PortletCustomiseParser#parsePortal(java.lang.String)
	 */
	public Portal parsePortal(String portalXml) throws ParseException {
		Element xmlRoot = parseXmlString(portalXml);
		Portal portal = new Portal();
		portal.setPortalPages(new ArrayList());
		for(Iterator iterator = xmlRoot.elementIterator("portal-page"); iterator.hasNext();) {
			Element xmlPage = (Element)iterator.next();
			portal.getPortalPages().add(parsePortalPage(xmlPage));
		}
		return portal;
	}
	
	/**
	 * 解析PORTAL页面
	 * @param xmlPage
	 * @return
	 */
	private PortalPage parsePortalPage(Element xmlPage) {
		PortalPage portalPage = new PortalPage();
		portalPage.setId(Long.parseLong(xmlPage.elementText("id"))); //ID
		portalPage.setStyle(xmlPage.elementText("style")); //样式名称
		portalPage.setLayout(xmlPage.elementText("layout")); //布局,2column_40_60/2column_50_50/2column_60_40/3column_25_25_50/3column_25_50_25/3column_33_33_33/3column_40_30_30/3column_50_25_25/4column_25_25_25_25
		portalPage.setAlwaysDisplayPortletButtons("true".equals(xmlPage.elementText("always-display-portlet-buttons"))); //是否总是显示PORTLET按钮
		portalPage.setTitle(xmlPage.elementText("title")); //标题
		portalPage.setSelected("true".equals(xmlPage.elementText("selected"))); //是否选中
		//解析PORTLET实例列表
		portalPage.setPortletInstances(new ArrayList()); //PORTLET实例列表
		for(Iterator iteratorPortlet = xmlPage.elementIterator("portlet-instance"); iteratorPortlet.hasNext();) {
			Element xmlPortletInstance = (Element)iteratorPortlet.next();
			portalPage.getPortletInstances().add(parsePortletInstance(xmlPortletInstance));
		}
		return portalPage;
	}
	
	/**
	 * 解析PORTAL实例
	 * @param xmlPortletInstance
	 * @return
	 */
	private PortletInstance parsePortletInstance(Element xmlPortletInstance) {
		PortletInstance portletInstance = new PortletInstance();
		portletInstance.setId(Long.parseLong(xmlPortletInstance.elementText("id"))); //ID
		portletInstance.setPortletTitle(xmlPortletInstance.elementText("portlet-title")); //PORTLET标题
		portletInstance.setWsrpProducerId(xmlPortletInstance.elementText("wsrp-producer-id")); //WSRP生产者ID
		if(portletInstance.getWsrpProducerId()!=null && portletInstance.getWsrpProducerId().isEmpty()) {
			portletInstance.setWsrpProducerId(null);
		}
		portletInstance.setPortletHandle(xmlPortletInstance.elementText("portlet-handle")); //PORTLET句柄
		portletInstance.setPortletStyle(xmlPortletInstance.elementText("portlet-style")); //PORTLET风格
		portletInstance.setColumnIndex(Integer.parseInt(xmlPortletInstance.elementText("column-index"))); //列号
		portletInstance.setState(xmlPortletInstance.elementText("state")); //状态,minimize|最小化,normal|默认
		if(portletInstance.getState()==null) {
			portletInstance.setState(WindowState.NORMAL.toString());
		}
		return portletInstance;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.portal.container.parser.PortletCustomiseParser#generatePortalXml(com.yuanluesoft.portal.server.model.Portal)
	 */
	public String generatePortalXml(Portal portal) throws ParseException {
		Document xmlDoc = DocumentHelper.createDocument();
		Element xmlRoot = xmlDoc.addElement("portal");
		for(Iterator iterator = portal==null || portal.getPortalPages()==null ? null : portal.getPortalPages().iterator(); iterator!=null && iterator.hasNext();) {
			PortalPage portalPage = (PortalPage)iterator.next();
			generatePortalPageXml(xmlRoot, portalPage);
		}
		return asXML(xmlRoot);
	}
	
	/**
	 * 生成PORTAL页面XML
	 * @param xmlRoot
	 * @param portalPage
	 * @throws ParseException
	 */
	public void generatePortalPageXml(Element xmlRoot, PortalPage portalPage) throws ParseException {
		Element xmlPortalPage = xmlRoot.addElement("portal-page");
		xmlPortalPage.addElement("id").setText("" + portalPage.getId()); //ID
		xmlPortalPage.addElement("style").setText(portalPage.getStyle()); //样式名称
		xmlPortalPage.addElement("layout").setText(portalPage.getLayout()); //布局,2column_40_60/2column_50_50/2column_60_40/3column_25_25_50/3column_25_50_25/3column_33_33_33/3column_40_30_30/3column_50_25_25/4column_25_25_25_25
		if(portalPage.isAlwaysDisplayPortletButtons()) {
			xmlPortalPage.addElement("always-display-portlet-buttons").setText("true"); //是否总是显示PORTLET按钮
		}
		xmlPortalPage.addElement("title").setText(portalPage.getTitle()); //标题
		if(portalPage.isSelected()) {
			xmlPortalPage.addElement("selected").setText("true"); //是否选中
		}
		//生成PORTLET实例XML
		for(Iterator iterator = portalPage.getPortletInstances()==null ? null : portalPage.getPortletInstances().iterator(); iterator!=null && iterator.hasNext();) {
			PortletInstance portletInstance = (PortletInstance)iterator.next();
			generatePortletInstanceXml(xmlPortalPage, portletInstance);
		}
	}
	
	/**
	 * 生成PORTLET实例XML
	 * @param xmlPortalPage
	 * @param portletInstance
	 * @throws ParseException
	 */
	public void generatePortletInstanceXml(Element xmlPortalPage, PortletInstance portletInstance) throws ParseException {
		Element xmlPortletInstance = xmlPortalPage.addElement("portlet-instance");
		xmlPortletInstance.addElement("id").setText("" + portletInstance.getId()); //ID
		if(portletInstance.getPortletTitle()!=null) {
			xmlPortletInstance.addElement("portlet-title").setText(portletInstance.getPortletTitle()); //portlet标题
		}
		if(portletInstance.getWsrpProducerId()!=null && !portletInstance.getWsrpProducerId().isEmpty()) {
			xmlPortletInstance.addElement("wsrp-producer-id").setText(portletInstance.getWsrpProducerId()); //WSRP生产者ID
		}
		if(portletInstance.getPortletHandle()!=null) {
			xmlPortletInstance.addElement("portlet-handle").setText(portletInstance.getPortletHandle()); //portlet句柄
		}
		if(portletInstance.getPortletStyle()!=null) {
			xmlPortletInstance.addElement("portlet-style").setText(portletInstance.getPortletStyle()); //PORTLET风格
		}
		xmlPortletInstance.addElement("column-index").setText("" + portletInstance.getColumnIndex()); //列号
		if(portletInstance.getState()!=null && !WindowState.NORMAL.toString().equals(portletInstance.getState())) {
			xmlPortletInstance.addElement("state").setText(portletInstance.getState()); //状态,minimized|最小化,normal|默认
		}
	}
}