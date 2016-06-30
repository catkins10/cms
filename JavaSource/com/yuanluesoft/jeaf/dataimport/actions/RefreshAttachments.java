package com.yuanluesoft.jeaf.dataimport.actions;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.infopublic.pojo.PublicInfo;
import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author yuanluesoft
 *
 */
public class RefreshAttachments extends ImportDataAction {
    
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			checkPrivilege(request, response);
		}
		catch(Exception e) {
			return redirectToLogin(this, mapping, form, request, response, e, false);
		}
		DatabaseService databaseService = (DatabaseService)getService("databaseService");
		//处理文章
		/*SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService");
		String hql = "select SiteResource" +
					 " from SiteResource SiteResource" +
					 " left join SiteResource.lazyBody SiteResourceBody" +
					 " where SiteResourceBody.body like '%wpG_%'" +
					 " order by SiteResource.id";
		for(int i=0; i<10000; i+=100) { //每次更新100条
			List infos = databaseService.findRecordsByHql(hql, ListUtils.generateList("lazyBody,subjections", ","), 0, 100);
			if(infos==null || infos.isEmpty()) {
				break;
			}
			for(Iterator iterator = infos.iterator(); iterator.hasNext();) {
				SiteResource resource = (SiteResource)iterator.next();
				try {
					siteResourceService.update(resource);
				}
				catch(Exception e) {
					
				}
			}
			if(infos.size()<100) {
				break;
			}
		}*/
		String siteId = RequestUtils.getParameterStringValue(request, "siteId");
		if(siteId!=null) {
			SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService");
			String hql = "select SiteResource " +
						 "from SiteResource SiteResource";
			if(!siteId.equals("0")) {
				hql += " left join SiteResource.subjections subjections, WebDirectorySubjection WebDirectorySubjection" +
					   " where subjections.siteId=WebDirectorySubjection.directoryId" +
					   " and WebDirectorySubjection.parentDirectoryId=" +  siteId;
			}
			hql += " order by SiteResource.id";
			for(int i=0; ; i+=100) { //每次更新100条
				List infos = databaseService.findRecordsByHql(hql, ListUtils.generateList("lazyBody,subjections", ","), i, 100);
				if(infos==null || infos.isEmpty()) {
					break;
				}
				for(Iterator iterator = infos.iterator(); iterator.hasNext();) {
					SiteResource resource = (SiteResource)iterator.next();
					try {
						siteResourceService.update(resource);
					}
					catch(Exception e) {
						
					}
				}
			}
		}
		//处理信息公开
		String infoDirectoryId = RequestUtils.getParameterStringValue(request, "infoDirectoryId");
		if(infoDirectoryId!=null) {
			PublicInfoService publicInfoService = (PublicInfoService)getService("publicInfoService");
			String hql = "select PublicInfo" +
						 " from PublicInfo PublicInfo";
			if(!infoDirectoryId.equals("0")) {
				hql += " left join PublicInfo.subjections subjections, PublicDirectorySubjection PublicDirectorySubjection" +
					   " where subjections.directoryId=PublicDirectorySubjection.directoryId" +
					   " and PublicDirectorySubjection.parentDirectoryId=" +  infoDirectoryId;
			}
			hql += " order by PublicInfo.id";
			for(int i=0; ; i+=100) { //每次更新100条
				List infos = databaseService.findRecordsByHql(hql, ListUtils.generateList("lazyBody,subjections", ","), i, 100);
				if(infos==null || infos.isEmpty()) {
					break;
				}
				for(Iterator iterator = infos.iterator(); iterator.hasNext();) {
					PublicInfo info = (PublicInfo)iterator.next();
					try {
						publicInfoService.update(info);
					}
					catch(Exception e) {
						
					}
				}
			}
		}
		String onlineServiceDirectoryId = RequestUtils.getParameterStringValue(request, "onlineServiceDirectoryId");
		if(onlineServiceDirectoryId!=null) {
			OnlineServiceItemService onlineServiceItemService = (OnlineServiceItemService)getService("onlineServiceItemService");
			String hql = "select OnlineServiceItem" +
						 " from OnlineServiceItem OnlineServiceItem";
			if(!onlineServiceDirectoryId.equals("0")) {
				hql += " left join OnlineServiceItem.subjections subjections, OnlineServiceDirectorySubjection OnlineServiceDirectorySubjection" +
					   " where subjections.directoryId=OnlineServiceDirectorySubjection.directoryId" +
					   " and OnlineServiceDirectorySubjection.parentDirectoryId=" +  onlineServiceDirectoryId;
			}
			hql += " order by OnlineServiceItem.id";
			for(int i=0; ; i+=100) { //每次更新100条
				List items = databaseService.findRecordsByHql(hql, ListUtils.generateList("subjections,guide,materials,units,transactors,faqs", ","), i, 100);
				if(items==null || items.isEmpty()) {
					break;
				}
				for(Iterator iterator = items.iterator(); iterator.hasNext();) {
					OnlineServiceItem item = (OnlineServiceItem)iterator.next();
					try {
						onlineServiceItemService.update(item);
					}
					catch(Exception e) {
						
					}
				}
			}
		}
		response.getWriter().write("reupload complete.");
		return null;
    }
}