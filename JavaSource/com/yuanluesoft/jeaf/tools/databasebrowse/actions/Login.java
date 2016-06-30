package com.yuanluesoft.jeaf.tools.databasebrowse.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tools.databasebrowse.forms.DatabaseBrowse;

/**
 * 
 * @author chuan
 *
 */
public class Login extends DatabaseBrowseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionInfo sessionInfo;
        try {
        	sessionInfo = getSessionInfo(request, response);
        }
        catch(SessionException se) {
        	return redirectToLogin(this, mapping, form, request, response, se, false);
        }
        try {
        	checkPrivilege(sessionInfo);
        }
        catch(PrivilegeException pe) {
        	return redirectToLogin(this, mapping, form, request, response, pe, false);
        }
        DatabaseBrowse browseForm = (DatabaseBrowse)form;
        DatabaseService databaseService = (DatabaseService)getService("databaseService");
        browseForm.setJdbcURL(databaseService.getJdbcURL()); //设置为当前连接URL
        browseForm.setFormTitle("数据库登录");
		browseForm.getFormActions().addFormAction(-1, "确定", "FormUtils.doAction('databaseBrowse', '', false, '', '_top');", true);
        return mapping.findForward("load");
    }
}