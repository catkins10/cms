package com.yuanluesoft.cms.sitemanage.actions.selectphonepage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.sitemanage.model.PhonePageConfig;
import com.yuanluesoft.cms.sitemanage.service.SiteTemplateThemeService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 选择手机页面
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	PageService pageService = (PageService)getService("pageService");
    	SiteTemplateThemeService siteTemplateThemeService = (SiteTemplateThemeService)getService("siteTemplateThemeService");
    	PhonePageConfig phonePageConfig = siteTemplateThemeService.initPhonePageConfig(RequestUtils.getParameterIntValue(request, "screenWidth"), RequestUtils.getParameterIntValue(request, "screenHeight"), RequestUtils.getParameterLongValue(request, "siteId"), request);
    	if(phonePageConfig.getRecommendedThemes()==null || phonePageConfig.getRecommendedThemes().isEmpty()) {
    		//没有可推荐的主题,直接使用电脑页面
    		RequestUtils.savaRequestPageInfo(response, 0, false, -1);
    		response.sendRedirect(RequestUtils.resetRedirectURL(request.getParameter("redirect")));
    		return null;
    	}
    	request.setAttribute("record", phonePageConfig);
    	pageService.writePage("cms/sitemanage", "selectPhonePage", request, response, false);
    	return null;
    }
}