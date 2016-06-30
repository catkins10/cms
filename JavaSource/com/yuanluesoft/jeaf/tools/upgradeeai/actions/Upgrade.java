package com.yuanluesoft.jeaf.tools.upgradeeai.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;

/**
 * 
 * @author linchuan
 *
 */
public class Upgrade extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	try {
			getSessionInfo(request, response);
		}
		catch(SessionException se) {
			return redirectToLogin(this, mapping, form, request, response, se, false);
		}
		DatabaseService databaseService = (DatabaseService)getService("databaseService");
		EAIConfigure eaiConfigure = (EAIConfigure)databaseService.findRecordByHql("from EAIConfigure EAIConfigure");
		String configure = eaiConfigure.getConfigure();
		if(configure.indexOf("Path=")==-1) {
			response.getWriter().write("configure has been upgrade.");
			return null;
		}
		configure = configure.replaceAll("<Application([^>]*) Name=\"", "<Application$1 Title=\"");
		configure = configure.replaceAll("<Application([^>]*) Path=\"", "<Application$1 Name=\"");
		configure = configure.replaceAll("<Link([^>]*) Href=\"", "<Link$1 URL=\"");
		configure = configure.replaceAll("/jeaf/eai", "/eai");
		eaiConfigure.setConfigure(configure);
		databaseService.updateRecord(eaiConfigure);
		
		//清除缓存
        ((Cache)getService("eaiCache")).clear();
        ((Cache)getService("sessionInfoCache")).clear();
        
        response.getWriter().write("completed.");
		return null;
    }
}