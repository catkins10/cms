package com.yuanluesoft.cms.siteresource.actions.article;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 * 
 */
public class Load extends BaseAction {
	
	public Load() {
		super();
		externalAction = true; //对外页面
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService"); //站点资源服务
		//获取文章
		SiteResource siteResource = siteResourceService.getSiteResource(RequestUtils.getParameterLongValue(request, "id"));
		if(siteResource!=null && siteResource.getAnonymousLevel()<SiteResourceService.ANONYMOUS_LEVEL_ALL) { //不允许匿名用户访问
			anonymousEnable = false; //不允许匿名
			try {
				getSessionInfo(request, response);
			}
			catch(SessionException se) {
				return redirectToLogin(this, mapping, form, request, response, se, false);
			}
		}
		//设置record属性
		request.setAttribute("record", siteResource);
    	//输出页面
		PageService pageService = (PageService)getService("siteResourcePageService");
    	pageService.writePage("cms/sitemanage", "article", request, response, false);
    	return null;
    }
}