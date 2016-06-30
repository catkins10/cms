package com.yuanluesoft.eai.client.processor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.eai.client.EAIClient;
import com.yuanluesoft.eai.client.exception.EAIException;
import com.yuanluesoft.eai.client.model.Application;
import com.yuanluesoft.eai.client.model.EAI;
import com.yuanluesoft.eai.client.model.Group;
import com.yuanluesoft.eai.client.model.Link;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * EAI视图服务
 * @author linchuan
 *
 */
public class EAIProcessor extends RecordListProcessor {
	private EAIClient eaiClient; //EAI客户端

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#readRecordListData(com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList, java.util.List, int, boolean, boolean, com.yuanluesoft.cms.sitemanage.pojo.WebDirectory, com.yuanluesoft.cms.sitemanage.pojo.WebSite, com.yuanluesoft.cms.pagebuilder.model.page.SitePage, javax.servlet.http.HttpServletRequest)
	 */
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		SessionInfo sessionInfo = RequestUtils.getSessionInfo(request);
		if(sessionInfo==null) {
			return null;
		}
		EAI eai = (EAI)request.getAttribute("eai");
		if(eai==null) {
			try {
				eai = eaiClient.getEAI(sessionInfo.getLoginName());
			}
			catch (EAIException e) {
				Logger.exception(e);
				throw new ServiceException(e.getMessage());
			}
			request.setAttribute("eai", eai);
		}
		Link parentLink = (Link)sitePage.getAttribute("parentRecord");
		if(parentLink==null) { //没有父记录
			//直接获取第一级的记录列表
			return new RecordListData(generateLinks(eai.getChildren(), true));
		}
		else if(parentLink.getId().startsWith("group")) {
			//查找子元素
			Group group = (Group)ListUtils.findObjectByProperty(eai.getChildren(), "id", parentLink.getId());
			return new RecordListData(generateLinks(group.getChildren(), false));
		}
		return null;
	}
	
	/**
	 * 转换为链接列表
	 * @param eaiChildren
	 * @param topLevel
	 * @return
	 */
	private List generateLinks(List eaiChildren, boolean topLevel) {
		if(eaiChildren==null || eaiChildren.isEmpty()) {
			return null;
		}
		List links = new ArrayList();
		for(Iterator iterator = eaiChildren.iterator(); iterator.hasNext();) {
			Object child = iterator.next();
			if(child instanceof Application) { //应用
				Application application = (Application)child;
				try {
					application = (Application)application.clone();
				} 
				catch (CloneNotSupportedException e) {
					
				}
				if(application.isNavigateDisabled()) {
					continue;
				}
				if(application.getIconUrl()==null || application.getIconUrl().equals("")) {
					//设置图标
					application.setIconUrl(Environment.getContextPath() + "/jeaf/application/icons/application.gif");
				}
				if(application.getUrl()==null || application.getUrl().equals("")) { //没有指定URL
					//设置URL
					application.setUrl(Environment.getContextPath() + "/jeaf/application/application.shtml?applicationName=" + application.getName());
				}
				application.setUrl("{FINAL}" + application.getUrl());
				links.add(application);
			}
			else if(child instanceof Link) { //链接
				Link link = (Link)child;
				try {
					link = (Link)link.clone();
				}
				catch (CloneNotSupportedException e) {
					
				}
				if(link.getIconUrl()==null || link.getIconUrl().equals("")) {
					//设置图标
					link.setIconUrl(Environment.getContextPath() + "/jeaf/application/icons/link.gif");
				}
				link.setUrl("{FINAL}" + link.getUrl());
				links.add(link);
			}
			else if(child instanceof Group) { //分组
				Group group = (Group)child;
				List childLinks = generateLinks(group.getChildren(), false);
				if(childLinks!=null && !childLinks.isEmpty()) {
					if(topLevel) {
						Link link = new Link();
						link.setId(group.getId());
						link.setTitle(group.getName()); //标题
						link.setUrl("javascript:void(0)"); //链接地址
						link.setIconUrl(Environment.getContextPath() + "/jeaf/application/icons/group.gif"); //图标
						links.add(link);
					}
					else {
						links.addAll(childLinks);
					}
				}
			}
		}
		return links;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor#listStaticPageForModifiedObject(java.lang.Object, boolean, java.sql.Timestamp, com.yuanluesoft.jeaf.database.DatabaseService, int)
	 */
	public List listStaticPageForModifiedObject(Object object, String modifyAction, Timestamp baseTime, DatabaseService databaseService, int max) throws ServiceException {
		return null;
	}

	/**
	 * @return the eaiClient
	 */
	public EAIClient getEaiClient() {
		return eaiClient;
	}

	/**
	 * @param eaiClient the eaiClient to set
	 */
	public void setEaiClient(EAIClient eaiClient) {
		this.eaiClient = eaiClient;
	}
}