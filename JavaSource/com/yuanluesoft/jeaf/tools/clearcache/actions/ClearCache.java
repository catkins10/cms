package com.yuanluesoft.jeaf.tools.clearcache.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class ClearCache extends com.yuanluesoft.jeaf.action.BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ((Cache)getService("templateCache")).clear();
        ((Cache)getService("modelCache")).clear();
        ((Cache)getService("eaiCache")).clear();
        ((Cache)getService("workflowCache")).clear();
        ((Cache)getService("sessionInfoCache")).clear();
        ((Cache)getService("recordCache")).clear();
        ((Cache)getService("portletCache")).clear();
        ((Cache)getService("portletDefinitionCache")).clear();
        ((Cache)getService("advertCache")).clear();
        ((Cache)getService("microblogCache")).clear();
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("<html>" +
        						   "	<head>" +
        						   "		<title>缓存清除成功</title>" +
        						   "		<script language=\"JavaScript\" charset=\"utf-8\" src=\"" + Environment.getContextPath() + "/jeaf/common/js/common.js\"></script>" +
        						   "	</head>" +
        						   "	<body>" +
        						   "		<script language=\"JavaScript\" charset=\"utf-8\">" +
        						   "			DialogUtils.openMessageDialog('清除缓存', '缓存清除成功！', '确定', 'info', 430, 120);" +
        						   "		</script>" +
        						   "	</body>" +
        						   "</html>");
        return null;
    }
}