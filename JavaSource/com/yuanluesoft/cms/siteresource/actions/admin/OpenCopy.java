package com.yuanluesoft.cms.siteresource.actions.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 打开站点资源的拷贝
 * @author linchuan
 *
 */
public class OpenCopy extends BaseAction {
   
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService");
        SiteResource siteResourceCopy = siteResourceService.getSiteResourceCopy(RequestUtils.getParameterLongValue(request, "id"), RequestUtils.getParameterLongValue(request, "columnId"));
        if(siteResourceCopy!=null) {
        	response.sendRedirect(request.getContextPath() + "/cms/siteresource/admin/" + SiteResourceService.RESOURCE_TYPE_NAMES[siteResourceCopy.getType()] + ".shtml?act=edit&id=" + siteResourceCopy.getId());
        }
    	return null;
    }
}