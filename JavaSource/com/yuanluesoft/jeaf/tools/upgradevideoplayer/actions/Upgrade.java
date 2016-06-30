package com.yuanluesoft.jeaf.tools.upgradevideoplayer.actions;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Upgrade extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
		try {
			sessionInfo = getSessionInfo(request, response);
		}
		catch(SessionException se) {
			return redirectToLogin(this, mapping, form, request, response, se, false);
		}
		//检查用户有没有用户管理的权限
		AccessControlService accessControlService = (AccessControlService)getService("accessControlService");
		List acl = accessControlService.getAcl("jeaf/usermanage", sessionInfo);
		if(!acl.contains("application_manager")) {
			return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
		}
		response.setCharacterEncoding("utf-8");
		SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService");
		//处理含视频的文章
		DatabaseService databaseService = (DatabaseService)getService("databaseService");
		for(int i=0; ; i+=200) {
			String hql = "select SiteResource.id" +
						 " from SiteResource SiteResource" +
						 " where SiteResource.videoCount>0" +
						 " order by SiteResource.id";
			List resourceIds = databaseService.findRecordsByHql(hql, i, 200);
			for(Iterator iterator = resourceIds==null ? null : resourceIds.iterator(); iterator!=null && iterator.hasNext();) {
				Number id = (Number)iterator.next();
				try {
					SiteResource siteResource = (SiteResource)siteResourceService.load(SiteResource.class, id.longValue());
					response.getWriter().println(siteResource.getId() + "," + siteResource.getSubject() + "<br>");
					siteResourceService.update(siteResource);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			if(resourceIds==null || resourceIds.size()<200) {
				break;
			}
		}
		response.getWriter().write("complted!");
		return null;
    }
}