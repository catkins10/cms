package com.yuanluesoft.jeaf.application.action.applicationnavigator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.eai.client.EAIClient;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.application.forms.ApplicationNavigator;
import com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.ViewDefineService;
import com.yuanluesoft.jeaf.view.util.ViewUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
    	try {
    		sessionInfo = getSessionInfo(request, response);
        }
        catch(SessionException se) {
        	return redirectToLogin(this, mapping, form, request, response, se, false);
        }
        ApplicationNavigator navigatorForm = (ApplicationNavigator)form;
        //获取应用导航
        ApplicationNavigatorService applicationNavigatorService = (ApplicationNavigatorService)getService("applicationNavigatorService");
        com.yuanluesoft.jeaf.application.model.navigator.ApplicationNavigator applicationNavigator = applicationNavigatorService.getApplicationNavigator(navigatorForm.getApplicationName(), request, sessionInfo);
    	if(applicationNavigator.getRedirect()!=null) { //需要重定向
    		response.sendRedirect(applicationNavigator.getRedirect());
    		return null;
    	}
    	navigatorForm.setApplicationNavigator(applicationNavigator);
    	//设置应用程序标题
    	EAIClient eaiClient = (EAIClient)getService("eaiClient");
    	navigatorForm.setApplicationTitle(eaiClient.getApplicationTitle(navigatorForm.getApplicationName()));
    	//设置视图URL
    	if(navigatorForm.getViewName()!=null && !navigatorForm.getViewName().isEmpty()) {
    		ViewDefineService viewDefineService = (ViewDefineService)getService("viewDefineService");
    		View view = viewDefineService.getView(navigatorForm.getApplicationName(), navigatorForm.getViewName(), sessionInfo);
    		if(view!=null) {
    			navigatorForm.setViewUrl(ViewUtils.getViewURL(view, request));
    		}
    	}
        return mapping.findForward("load");
    }
}