package com.yuanluesoft.cms.sitemanage.actions.videoplayerlogo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Download extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SiteService siteService = (SiteService)getService("siteService");
    	siteService.downloadVideoPlayerLogo(RequestUtils.getParameterLongValue(request, "siteId"), request, response);
        return null;
    }
}