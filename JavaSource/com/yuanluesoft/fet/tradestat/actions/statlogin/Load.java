package com.yuanluesoft.fet.tradestat.actions.statlogin;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.fet.tradestat.forms.StatLogin;
import com.yuanluesoft.jeaf.util.CookieUtils;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Load extends com.yuanluesoft.jeaf.sso.actions.login.Load {
        
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	StatLogin loginForm = (StatLogin)form;
    	loginForm.setUserType(CookieUtils.getCookie(request, "lastUserType")); //用户类型,企业|company/区县|county/开发区|developmentArea
    	if(loginForm.getUserType()==null || loginForm.getUserType().isEmpty()) {
    		loginForm.setUserType("company");
    	}
    	String lastLoginName = CookieUtils.getCookie(request, "lastLoginFetUser");
    	if(lastLoginName!=null && !lastLoginName.isEmpty()) {
	    	try {
				lastLoginName = URLDecoder.decode(lastLoginName, "utf-8");
			}
			catch(Exception e) {
				lastLoginName = null;
			}
	    	if("company".equals(loginForm.getUserType())) {
	    		loginForm.setCompanyCode(lastLoginName);
	    	}
	    	else if("county".equals(loginForm.getUserType())) {
	    		loginForm.setCounty(lastLoginName);
	    	}
	    	else if("developmentArea".equals(loginForm.getUserType())) {
	    		loginForm.setDevelopmentArea(lastLoginName);
	    	}
    	}
    	return super.execute(mapping, form, request, response);
    }
}