package com.yuanluesoft.jeaf.usermanage.actions.placename;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;

/**
 * http://localhost/jeaf/usermanage/admin/importPlaceName.shtml
 * @author linchuan
 *
 */
public class Import extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(!request.getServerName().equals("localhost")) {
    		response.getWriter().print("必须允许在服务上执行");
    		return null;
    	}
    	OrgService orgService = (OrgService)getService("orgService");
    	orgService.importPlaceName("true".equals(request.getParameter("global")));
    	response.getWriter().write("import completed");
        return null;
    }
}