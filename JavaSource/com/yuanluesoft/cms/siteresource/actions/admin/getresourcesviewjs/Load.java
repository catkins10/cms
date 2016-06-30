package com.yuanluesoft.cms.siteresource.actions.admin.getresourcesviewjs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebViewReference;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	response.setCharacterEncoding("UTF-8");
		response.setContentType("text/javascript"); //设置输出内容为脚本

		//获取WEB目录
		SiteService siteService = (SiteService)getService("siteService");
    	WebDirectory webDirectory = (WebDirectory)siteService.getDirectory(RequestUtils.getParameterLongValue(request, "siteId"));
		
    	String resourcesApplicationName = "";
		String resourcesViewName = "";
		if(webDirectory!=null && (webDirectory instanceof WebViewReference)) { //引用
			WebViewReference reference = (WebViewReference)webDirectory;
	    	ViewDefineService viewDefineService = (ViewDefineService)getService("viewDefineService");
			View view = viewDefineService.getView(reference.getApplicationName(), reference.getViewName(), RequestUtils.getSessionInfo(request));
			resourcesViewName = (String)view.getExtendParameter("siteReferenceRecordList");
			if(resourcesViewName==null) {
				resourcesViewName = reference.getViewName();
			}
			resourcesApplicationName = reference.getApplicationName();
		}
		response.getWriter().write("parent.setTimeout(\"try{redirectToNewView('" + resourcesApplicationName + "', '" + resourcesViewName + "')}catch(e){}\", 1)");
    	return null;
    }
}