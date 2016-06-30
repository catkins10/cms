package com.yuanluesoft.jeaf.tools.upgradetemplate.upgrader.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;

import com.yuanluesoft.cms.pagebuilder.PageDefineService;
import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.templatemanage.pojo.Template;
import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.tools.upgradetemplate.upgrader.TemplateUpgrader;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.callback.FileSearchCallback;

/**
 * 升级到page-config.xml,废弃原来的cms-config.xml
 * @author chuan
 *
 */
public class UpgradeToPageConfigXml implements TemplateUpgrader {
	private Map cmsConfigs = new HashMap(); //cms-config.xml中的记录列表字段和页面字段
	private Map recordLists = new HashMap(); //记录列表集合
	private HTMLParser htmlParser = null;
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.tools.upgradetemplate.upgrader.TemplateUpgrader#getCreateDate()
	 */
	public String getCreateDate() {
		return "2010-8-31";
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.tools.upgradetemplate.upgrader.TemplateUpgrader#upgrade(com.yuanluesoft.cms.templatemanage.pojo.Template, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public String upgrade(Template template, String templateHTML, HttpServletRequest request) throws Exception {
		if(htmlParser==null) {
			htmlParser = (HTMLParser)Environment.getService("htmlParser");
			parseCmsConfigs(); //解析配置
		}
		if(template.getApplicationName()==null) {
			return templateHTML;
		}
		//解析模板文件
		HTMLDocument templateDocument = htmlParser.parseHTMLString(templateHTML, "utf-8");
		List fields = (List)cmsConfigs.get("page_" + template.getApplicationName() + "_" + template.getPageName());
		if(template.getApplicationName().startsWith("bidding")) {
			Field field = (Field)ListUtils.findObjectByProperty(fields, "title", "项目名称");
			if(field!=null) {
				fields.add(new Field(field.getName(), "工程名称", null, null, null, false, false));
			}
			field = (Field)ListUtils.findObjectByProperty(fields, "title", "招标编号");
			if(field!=null) {
				fields.add(new Field(field.getName(), "项目编号", null, null, null, false, false));
			}
		}
		PageDefineService pageDefineService = (PageDefineService)Environment.getService("pageDefineService");
		SitePage sitePage = pageDefineService.getSitePage(template.getApplicationName(), template.getPageName());
		//更新HTML文档
		upgradeDocument(templateDocument, fields, request, sitePage);
		return htmlParser.getDocumentHTML(templateDocument, "utf-8");
	}

	/**
	 * 升级HTML文档
	 * @param templateDocument
	 * @param fields
	 * @param request
	 * @throws Exception
	 */
	private void upgradeDocument(HTMLDocument templateDocument, List fields, HttpServletRequest request, SitePage sitePage) throws Exception {
		NodeList nodes = templateDocument.getElementsByTagName("a");
		for(int i=nodes.getLength()-1; i>=0; i--) {
			HTMLAnchorElement a = (HTMLAnchorElement)nodes.item(i);
			if("field".equals(a.getId()) || "infoField".equals(a.getId())) { //处理字段
				upgradeField(a, fields);
			}
			else if(recordLists.get(a.getId())!=null) { //处理记录列表
				upgradeRecordList(a, sitePage);
			}
			else if("tabstripBody".equals(a.getId())) { //TAB
				upgradeTabstrip(a);
			}
			else if("headline".equals(a.getId())) { //头版头条
				List headLineFields = new ArrayList();
				headLineFields.add(new Field("summarize", "概述", null, null, null, false, false));
				updateFormat(a, "format", "format", "", headLineFields);
			}
			else if("systemLink".equals(a.getId())) { //系统链接
				//删除contextPath
				int length = Environment.getContextPath().length();
				if(length>0 && a.getHref().startsWith("/")) {
					a.setHref(a.getHref().substring(length));
				}
			}
			else if("pageLink".equals(a.getId())) { //页面链接
				upgradePageLink(a);
			}
			else if("weather".equals(a.getId())) { //天气
				String urn = a.getAttribute("urn");
				if(request!=null && urn.indexOf("format=")==-1) { //没有"format"属性
					String area = request.getParameter("area");
					if(area!=null && !area.equals("")) {
						a.setAttribute("urn", "format=" + urn + "&days=1&areas=" + request.getParameter("area") + "&separatorMode=blank");
					}
				}
				try {
					updateFormat(a, "format", "format", "days", null);
				}
				catch(Exception e) {
					
				}
			}
			else if(",onlineServiceLocation,sceneLocation,infoDirectoryLocation,siteLocation,".indexOf("," + a.getId() + ",")!=-1) { //网上办事位置、场景服务位置、信息公开目录位置、站点位置
				String urn = a.getAttribute("urn");
				int beginIndex = urn.indexOf("separator=");
				a.setAttribute("urn", urn.substring(0, beginIndex) + "separator=" + StringUtils.encodePropertyValue(getPropertyValue(urn, "separator", "")));
			}
			else if("rssLink".equals(a.getId())) { //RSS
				String urn = a.getAttribute("urn");
				int index = urn.indexOf("&applicationName=");
				if(index>0) {
					String extendProperties = urn.substring(0, index);
					a.setAttribute("urn", "extendProperties=" + StringUtils.encodePropertyValue(extendProperties) + urn.substring(index));
				}
			}
			else if("guideBody".equals(a.getId())) { //信息公开指南正文
				a.setId("field");
				a.setAttribute("urn", "name=guide");
			}
		}
		//处理表单
		nodes = templateDocument.getElementsByTagName("form");
		for(int i=nodes.getLength()-1; i>=0; i--) {
			HTMLFormElement form = (HTMLFormElement)nodes.item(i);
			upgradeForm(form);
		}
	}

	/**
	 * 升级字段配置
	 * @param fieldElement
	 * @param fields
	 * @throws Exception
	 */
	private void upgradeField(HTMLAnchorElement fieldElement, List fields) throws Exception {
		try {
			String fieldTitle = htmlParser.getTextContent(fieldElement);
			if(fieldTitle==null || fieldTitle.length()<3) {
				return;
			}
			String urn = fieldElement.getAttribute("urn");
			if(urn!=null && urn.startsWith("name=")) { //已经配置过
				return;
			}
			fieldTitle = fieldTitle.substring(1, fieldTitle.length() - 1);
			System.out.println("**************** upgrade field " + fieldTitle);
			Field field = (Field)ListUtils.findObjectByProperty(fields, "title", fieldTitle);
			String fieldName = field==null ? null : field.getName();
			if("附件".equals(fieldTitle)) {
				fieldName = "attachment";
			}
			else if("图片".equals(fieldTitle) && (fieldName==null || fieldName.equals(""))) {
				fieldName = "firstImageName";
			}
			else if("视频".equals(fieldTitle) && (fieldName==null || fieldName.equals(""))) {
				fieldName = "firstVideoName";
			}
			if(fieldName==null) {
				return;
			}
			//在URN中加入name属性
			urn = "name=" + fieldName +  (urn==null || urn.equals("") ? "" : (urn.charAt(0)=='&' ? "" : "&") + urn);
			fieldElement.setAttribute("urn", urn);
			fieldElement.setId("field");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 升级记录列表配置
	 * @param recordListElement
	 * @throws Exception
	 */
	private void upgradeRecordList(HTMLAnchorElement recordListElement, SitePage sitePage) throws Exception {
		try {
			String urn = recordListElement.getAttribute("urn").replaceAll("openInCurrentWindow=true", "linkOpenMode=_self").replaceAll("openInCurrentWindow=false", "linkOpenMode=_blank");
			boolean appendApplicationName = false;
			String recordListApplicatonName = getPropertyValue(urn, "applicationName", "next");
			String recordListName = recordListElement.getId();
			
			if(recordListApplicatonName==null) {
				appendApplicationName = true;
				recordListApplicatonName = (String)recordLists.get(recordListName);
			}
			System.out.println("**************** upgrade RecordList " + recordListApplicatonName + "/" + recordListName);
				
			//获取字段列表
			List fields = (List)cmsConfigs.get("recordList_" + recordListApplicatonName + "_" + recordListName);
			recordListElement.setId("recordList"); //修改ID为"recordList"
			
			boolean privateRecordList = "true".equals(recordLists.get("pagePrivate_" + recordListName));
			if("inquiries".equals(recordListName)) {
				String type = getPropertyValue(urn, "type", "next");
				if("processing".equals(type)) {
					recordListName = "processingInquiries";
				}
				else if("completed".equals(type)) {
					recordListName = "completedInquiries";
				}
			}
			else if("adviceTopics".equals(recordListName)) {
				String type = getPropertyValue(urn, "type", "next");
				if("processing".equals(type)) {
					recordListName = "processingAdviceTopics";
				}
				else if("completed".equals(type)) {
					recordListName = "completedAdviceTopics";
				}
			}
			else if("libAgents".equals(recordListName)) {
				recordListName = "biddingAgents";
				privateRecordList = false;
				List newFields = new ArrayList();
				for(Iterator iterator=fields.iterator(); iterator.hasNext();) {
					Field field = (Field)iterator.next();
					newFields.add(new Field(field.getName().startsWith("enterpriseCert.") ? field.getName().substring("enterpriseCert.".length()) : "enterprise." + field.getName(), field.getTitle(), null, null, null, false, false));
				}
				fields = newFields;
			}
			else if("otherBiddingSupplements".equals(recordListName)) {
				privateRecordList = false;
			}
			else if(",leaderMails,advices,complaints,publicOpinions,messageBoards,preapprovals,supervisions,".indexOf("," + recordListName + ",")!=-1) { //公众服务
				fields.add(new Field("creator", "创建人姓名", null, null, null, false, false));
				fields.add(new Field("created", "创建时间", null, null, null, false, false));
				fields.add(new Field("creatorMail", "邮箱", null, null, null, false, false));
				fields.add(0, new Field("publicSubject", "主题", null, null, null, false, false));
			}
			else if("attachments".equals(recordListName)) { //附件
				recordListName = "attachment";
			}
			else if("paymentMethods".equals(recordListName) || "accountTypes".equals(recordListName)) { //支付
				privateRecordList = false;
			}
			else if("onlineServiceItemDownloads".equals(recordListName)) {
				privateRecordList = false;
				recordListName = "onlineServiceDownloads";
			}
			else if("onlineServiceItemMaterials".equals(recordListName)) {
				privateRecordList = false;
				recordListName = "onlineServiceMaterials";
			}
			else if("onlineServiceItemUnits".equals(recordListName)) {
				recordListName = "units";
			}
			else if("cms/interview".equals(recordListApplicatonName)) { //在线访谈
				privateRecordList = false;
				if("interviewImages".equals(recordListName)) { //在线访谈图片
					fields.add(new Field("imageFileName", "记录图片", null, null, null, false, false));
				}
			}
			else if("cms/scene".equals(recordListApplicatonName)) { //场景服务
				privateRecordList = false;
			}
			
			if(recordListApplicatonName.startsWith("bidding")) {
				Field field = (Field)ListUtils.findObjectByProperty(fields, "title", "项目名称");
				if(field!=null) {
					fields.add(new Field(field.getName(), "工程名称", null, null, null, false, false));
				}
				field = (Field)ListUtils.findObjectByProperty(fields, "title", "招标编号");
				if(field!=null) {
					fields.add(new Field(field.getName(), "项目编号", null, null, null, false, false));
				}
			}
			
			
			if(fields!=null) {
				fields.add(new Field("recordNumber", "序号", null, null, null, false, false));
			}
			
			//获取记录格式配置
			String recordFormatSeparator = getPropertyValue(urn, "recordFormatSeparator", "recordFormat");
			String beginWord;
			String endWord;
			if(recordFormatSeparator==null) {
				beginWord = "recordFormat";
				endWord = "separatorMode";
			}
			else {
				beginWord = "begin_" + recordFormatSeparator;
				endWord = "end_" + recordFormatSeparator;
			}
			//更新格式
			updateFormat(recordListElement, "recordFormat", beginWord, endWord, fields);
			//获取新的URN
			urn = recordListElement.getAttribute("urn");
			
			//评论列表:更新引用配置
			if("comments".equals(recordListName)) {
				String citationSeparator = getPropertyValue(urn, "citationSeparator", "citationSeparatorEnd");
				if(citationSeparator!=null) {
					updateFormat(recordListElement, "citation", "&begin_" + citationSeparator, "&end_" + citationSeparator, fields);
				}
			}
			//统计列表:更新配置
			if("totals".equals(recordListName)) {
				int totalCount = Integer.parseInt(getPropertyValue(urn, "totalCount", "totalSort"));
				String totalSeparator = getPropertyValue(urn, "totalSeparator", "totalSeparatorEnd");
				if(totalSeparator!=null) {
					for(int i=0; i<totalCount; i++) {
						int beginIndex = urn.indexOf("begin" + i + "_" + totalSeparator + "=");
						int endIndex = urn.indexOf("&end" + i + "_" + totalSeparator + "=", beginIndex);
						String totalColumnProperties = urn.substring(beginIndex + ("begin" + i + "_" + totalSeparator + "=").length(), endIndex);
						totalColumnProperties = "totalTitle=" + StringUtils.encodePropertyValue(totalColumnProperties.substring(0, totalColumnProperties.indexOf("&totalApplication="))) +
												"&totalApplication=" + getPropertyValue(totalColumnProperties, "totalApplication", "next") +
												"&totalName=" + getPropertyValue(totalColumnProperties, "totalName", "next") +
												"&totalLink=" + StringUtils.encodePropertyValue(getPropertyValue(totalColumnProperties, "totalLink", "totalRecordListProperties")) +
												"&totalRecordListProperties=" + StringUtils.encodePropertyValue(getPropertyValue(totalColumnProperties, "totalRecordListProperties", ""));
						urn = urn.substring(0, beginIndex) + "total" + i + "=" + StringUtils.encodePropertyValue(totalColumnProperties) + urn.substring(endIndex + ("&end" + i + "_" + totalSeparator + "=").length());
					}
					recordListElement.setAttribute("urn", urn);
				}
			}
			
			//设置扩展属性
			urn = recordListElement.getAttribute("urn"); //获取新的URN
			int index = urn.indexOf("applicationName=");
			if(appendApplicationName) { //原来的记录列表没有应用名称属性
				index = urn.indexOf("recordCount=");
				if(index>0) {
					urn = urn.substring(index) + "&extendProperties=" + StringUtils.encodePropertyValue(urn.substring(0, index-1));
				}
			}
			else if(index>0) { //扩展的属性在前面
				urn = urn.substring(index) + "&extendProperties=" + StringUtils.encodePropertyValue(urn.substring(0, index-1));
			}
			else if((index=urn.lastIndexOf("&separatorMode="))!=-1) {  //扩展的属性在后面
				index += "&separatorMode=".length();
				String[] otherPropertyNames = {"separatorHeight", "separatorImage", "areaWidth", "areaHeight", "recordWidth",
											   "recordHeight", "recordIndent", "scrollMode", "hideSwitchNumber", "scrollJoin",
											   "scrollSpeed", "scrollAmount",  "linkOpenMode", "tableMode", "searchResults"};
				for(int i=otherPropertyNames.length-1; i>=0; i--) {
					int next;
					if((next=urn.indexOf("&" + otherPropertyNames[i] + "=", index))!=-1) {
						index = next + ("&" + otherPropertyNames[i] + "=").length();
						break;
					}
				}
				index = urn.indexOf("&", index);
				if(index!=-1) {
					System.out.println("*************extendProperties: " + urn.substring(index + 1));
					urn = urn.substring(0, index) + "&extendProperties=" + StringUtils.encodePropertyValue(urn.substring(index + 1));
				}
			}
			//把linkType转换为linkTitle
			if("columns".equals(recordListName)) {
				String linkType = StringUtils.getPropertyValue(StringUtils.getPropertyValue(urn, "extendProperties"), "linkType");
				if("index".equals(linkType)) {
					urn = "linkTitle=" + "栏目首页&" + urn;
				}
				else if("rssSubscribe".equals(linkType)) {
					urn = "linkTitle=" + "RSS订阅&" + urn;
				}
				else if("rss".equals(linkType)) {
					urn = "linkTitle=" + "RSS频道&" + urn;
				}
			}
			//添加记录列表名称和是否私有列表属性
			urn = "recordListName=" + recordListName +
				  (privateRecordList ? "&privateRecordList=true&recordClassName" + sitePage.getRecordClassName() : "") +
				  (appendApplicationName ? "&applicationName=" + recordListApplicatonName : "") +
				  (StringUtils.getPropertyValue(urn, "linkOpenMode=")==null ? "&linkOpenMode=_blank" : "") +
				  "&" + urn;
			recordListElement.setAttribute("urn", urn);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新格式
	 * @param element
	 * @param propertyName
	 * @param beginWord
	 * @param endWord
	 * @param fields
	 * @throws Exception
	 */
	private void updateFormat(HTMLElement element, String propertyName, String beginWord, String endWord, List fields) throws Exception {
		String urn = element.getAttribute("urn");
		
		String recordFormat = getPropertyValue(urn, beginWord, endWord); //记录显示格式
		if(recordFormat==null) {
			System.out.println("***********urn: " + propertyName + "," + urn);
			return;
		}
		
		//解析格式
		HTMLDocument recordFormatDocument = htmlParser.parseHTMLString(recordFormat, "utf-8");
		upgradeDocument(recordFormatDocument, fields, null, null);
		
		//更新urn
		recordFormat = htmlParser.getElementInnerHTML(recordFormatDocument.getBody(), "utf-8");
		int beginIndex = urn.indexOf(beginWord + "=");
		int endIndex = endWord.equals("") ?  -1 : urn.indexOf("&" + endWord + "=", beginIndex);
		urn = urn.substring(0, beginIndex) + propertyName + "=" + StringUtils.encodePropertyValue(recordFormat) + (endIndex==-1 ? "" : urn.substring(endIndex));
		element.setAttribute("urn", urn);
	}

	/**
	 * 升级TAB
	 * @param tabstripElement
	 * @throws Exception
	 */
	private void upgradeTabstrip(HTMLAnchorElement tabstripElement) throws Exception {
		try {
			String urn = tabstripElement.getAttribute("urn");
			String tabstripName = getPropertyValue(urn, "name", "width");
			System.out.println("**************** upgrade Tabstrip " + tabstripName);
			
			//更新TAB选项
			NodeList elements = ((HTMLDocument)tabstripElement.getOwnerDocument()).getElementsByName("tabstripButton_" + tabstripName);
			for(int i=elements==null ? -1 : elements.getLength()-1; i>=0; i--) {
				HTMLElement element = (HTMLElement)elements.item(i);
				urn = element.getAttribute("urn");
				if(element.getId().startsWith("tabstripButtonList_")) { //更新TAB选项列表
					String recordListApplicatonName = getPropertyValue(urn, "applicationName", "recordListName");
					String recordListName = getPropertyValue(urn, "recordListName", "buttonCount");
					String separator = getPropertyValue(urn, "separatorBegin", "separatorEnd");
					//获取字段列表
					List fields = (List)cmsConfigs.get("recordList_" + recordListApplicatonName + "_" + recordListName);
					updateFormat(element, "tabstripButton", "tabstripButton_" + separator, "tabstripBody_" + separator, fields); //更新按钮配置
					updateFormat(element, "tabstripBody", "tabstripBody_" + separator, "moreLink_" + separator, fields); //更新内容配置
					updateFormat(element, "moreLink", "moreLink_" + separator, "moreLinkEnd_" + separator, fields);
					
					urn = element.getAttribute("urn");
					int index = urn.indexOf("&applicationName=");
					if(index>0) {
						String extendProperties = urn.substring(0, index);
						element.setAttribute("urn", "extendProperties=" + StringUtils.encodePropertyValue(extendProperties) + urn.substring(index));
					}
				}
				else {
					String separator = getPropertyValue(urn, "separatorBegin", "separatorEnd");
					updateFormat(element, "tabstripBody", "tabstripBody_" + separator, "moreLink_" + separator, null);
					updateFormat(element, "moreLink", "moreLink_" + separator, "moreLinkEnd_" + separator, null);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 升级表单
	 * @param form
	 * @throws Exception
	 */
	private void upgradeForm(HTMLFormElement form) throws Exception {
		if(",adviceSearch,complaintSearch,publicOpinionSearch,publicRequestSearch,leaderMailSearch,messageBoardSearch,preapprovalSearch,supervisionSearch,".indexOf("," + form.getId() + ",")!=-1) { //建议搜索
			renameFieldName(form, "beginDate", "createdBegin");
			renameFieldName(form, "endDate", "createdEnd");
			renameFieldName(form, "content", "lazyBody.content");
		}
		else if("siteSearch".equals(form.getId())) { //高级搜索
			renameFieldName(form, "beginDate", "issueTimeBegin");
			renameFieldName(form, "endDate", "issueTimeEnd");
			renameFieldName(form, "body", "lazyBody.body");
		}
		else if("loginForm".equals(form.getId())) { //登录
			String separator = getPropertyValue(form.getAction(), "separatorBegin", "separatorEnd");
			if(separator!=null && !separator.equals("")) {
				String redirect = getPropertyValue(form.getAction(), "redirect_" + separator, "redirectEnd_" + separator);
				int beginIndex = form.getAction().indexOf("redirect_" + separator);
				form.setAction(form.getAction().substring(0, beginIndex) + "redirect=" + StringUtils.encodePropertyValue(redirect));
			}
		}
	}

	/**
	 * 升级页面链接
	 * @param pageLinkElement
	 * @throws Exception
	 */
	private void upgradePageLink(HTMLAnchorElement pageLinkElement) throws Exception {
		String urn = pageLinkElement.getAttribute("urn");
		if("网上缴费".equals(urn)) {
			pageLinkElement.setAttribute("urn", "网上购买标书");
		}
	}

	/**
	 * 重命名字段名称
	 * @param form
	 * @param from
	 * @param to
	 */
	private void renameFieldName(HTMLFormElement form, String from, String to) {
		try {
			HTMLCollection elements = form.getElements();
			for(int i=elements.getLength()-1; i>=0; i--) {
				HTMLElement element = (HTMLElement)elements.item(i);
				if(from.equals(element.getAttribute("name"))) {
					element.setAttribute("name", to);
				}
			}
		}
		catch(Exception e) {
			
		}
	}

	/**
	 * 解析cmx-config.xml中的记录列表字段和页面字段
	 * @throws Exception
	 */
	private void parseCmsConfigs() throws Exception {
		FileUtils.fileSearch(Environment.getWebinfPath(), "cms-config.xml", new FileSearchCallback() {
			public void onFileFound(File file) {
				String applicationName = file.getPath().replace('\\', '/');
				int index = applicationName.toLowerCase().lastIndexOf("web-inf/");
				applicationName = applicationName.substring(index + "web-inf/".length(), applicationName.lastIndexOf('/'));
				if(applicationName.indexOf("deployment")!=-1) {
					return;
				}
				try {
					parseCmsConfig(applicationName);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 解析单个cms-config.xml
	 * @param applicationName
	 * @throws Exception
	 */
	private void parseCmsConfig(String applicationName) throws Exception {
		XmlParser baseParser = new XmlParser();
		Element xmlRoot = baseParser.parseXmlFile(Environment.getWebinfPath() + applicationName + "/cms-config.xml");
		Element xmlPages = xmlRoot.element("pages");
		//解析页面
		for(Iterator iterator = xmlPages==null ? null : xmlPages.elementIterator(); iterator!=null && iterator.hasNext();) {
			Element xmlPage = (Element)iterator.next();
			parsePage(applicationName, xmlPage); //解析页面
		}
		//解析页面私有的记录列表
		for(Iterator iterator = xmlPages==null ? null : xmlPages.elementIterator(); iterator!=null && iterator.hasNext();) {
			Element xmlPage = (Element)iterator.next();
			parsePrivateRecordList(applicationName, xmlPage); //解析页面
		}
		//解析记录列表
		Element xmlRecordLists = xmlRoot.element("recordLists");
		for(Iterator iterator = xmlRecordLists==null ? null : xmlRecordLists.elementIterator(); iterator!=null && iterator.hasNext();) {
			Element xmlRecordList = (Element)iterator.next();
			parseRecordList(applicationName, xmlRecordList, false); //解析记录列表
		}
	}

	/**
	 * 解析页面
	 * @param applicationName
	 * @param xmlPage
	 * @throws Exception
	 */
	private void parsePage(String applicationName, Element xmlPage) throws Exception {
		String pageName = xmlPage.attributeValue("name");
		System.out.println("**************** parse page " + applicationName + "/" + pageName);
		List fields = new ArrayList();
		for(Iterator iterator = xmlPage.elementIterator("field"); iterator!=null && iterator.hasNext();) {
			Element xmlField = (Element)iterator.next();
			fields.add(parseField(xmlField));
		}
		cmsConfigs.put("page_" + applicationName + "_" + pageName, fields);
	}

	/**
	 * 解析页面私有的记录列表
	 * @param applicationName
	 * @param xmlPage
	 * @throws Exception
	 */
	private void parsePrivateRecordList(String applicationName, Element xmlPage) throws Exception {
		for(Iterator iterator = xmlPage.elementIterator("recordList"); iterator!=null && iterator.hasNext();) {
			Element xmlRecordList = (Element)iterator.next();
			parseRecordList(applicationName, xmlRecordList, true); //解析记录列表
		}
	}

	/**
	 * 解析记录列表
	 * @param applicationName
	 * @param xmlRecordList
	 * @param pagePrivate
	 * @throws Exception
	 */
	private void parseRecordList(String applicationName, Element xmlRecordList, boolean pagePrivate) throws Exception {
		String recordListName = xmlRecordList.attributeValue("name");
		System.out.println("**************** parse recordlist " + applicationName + "/" + recordListName);
		//添加到记录列表集合
		recordLists.put(applicationName + "_" + recordListName, recordListName);
		recordLists.put(recordListName, applicationName);
		if(pagePrivate) {
			recordLists.put("pagePrivate_" + recordListName, "true");
		}
		
		//解析字段
		List fields = new ArrayList();
		for(Iterator iterator = xmlRecordList.elementIterator("field"); iterator!=null && iterator.hasNext();) {
			Element xmlField = (Element)iterator.next();
			fields.add(parseField(xmlField));
		}
		if(fields.isEmpty()) {
			//记录列表自己没有配置字段,调用页面的字段列表
			String recordPage = xmlRecordList.attributeValue("recordPage");
			if(recordPage!=null) {
				fields = (List)cmsConfigs.get("page_" + applicationName + "_" + recordPage);
			}
		}
		cmsConfigs.put("recordList_" + applicationName + "_" + recordListName, fields);
	}

	/**
	 * 解析字段
	 * @param xmlField
	 * @return
	 * @throws Exception
	 */
	private Field parseField(Element xmlField) throws Exception {
		Field field = new Field();
		field.setTitle(xmlField.attributeValue("title"));
		field.setName(xmlField.attributeValue("name").replaceAll("意见", ""));
		return field;
	}

	/**
	 * 获取属性值
	 * @param properties 如:name=siteLink&siteId=0&siteName=远略软件
	 * @param propertyName
	 * @param nextPropertyName
	 * @return
	 */
	private String getPropertyValue(String properties, String propertyName, String nextPropertyName) {
		if(properties==null) {
			return null;
		}
		int index = properties.indexOf(propertyName + "=");
		if(index==-1) {
			return null;
		}
		index += propertyName.length() + 1;
		if(nextPropertyName==null || nextPropertyName.equals("")) {
			return properties.substring(index);
		}
		int indexEnd = -1;
		if(!nextPropertyName.equals(propertyName + "End") && !nextPropertyName.equals("next")) {
			indexEnd = properties.indexOf("&" + nextPropertyName + "=", index);
		}
		if(indexEnd==-1) {
			indexEnd = properties.indexOf("&", index);
		}
		String value = indexEnd==-1 ? properties.substring(index) : properties.substring(index, indexEnd);
		return "".equals(value) ? null : value;
	}
}