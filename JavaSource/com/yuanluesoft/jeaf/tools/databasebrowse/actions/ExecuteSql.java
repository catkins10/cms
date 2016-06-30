package com.yuanluesoft.jeaf.tools.databasebrowse.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.SqlResultList;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ExecuteSql extends DatabaseBrowseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
    	try {
    		sessionInfo = getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, true);
    	}
    	try {
    		checkPrivilege(sessionInfo);
    	}
    	catch(PrivilegeException pe) {
    		return redirectToLogin(this, mapping, form, request, response, pe, true);
    	}
    	com.yuanluesoft.jeaf.tools.databasebrowse.forms.ExecuteSql executeSqlForm = (com.yuanluesoft.jeaf.tools.databasebrowse.forms.ExecuteSql)form;
    	//从会话中读取JDBC信息
    	String[] jdbcSession = (String[])request.getSession().getAttribute("jdbcSession");
    	try {
    		String sql = executeSqlForm.getSql();
    		if(sql.startsWith("listTableFields ")) { //获取字段列表
    			executeSqlForm.setResultList(JdbcUtils.listTableFields(jdbcSession[0], jdbcSession[1], jdbcSession[2], sql.substring("listTableFields ".length())));
    		}
    		else {
    			Object result = JdbcUtils.executeSql(jdbcSession[0], jdbcSession[1], jdbcSession[2], sql, executeSqlForm.getLimit(), false);
    			if(result instanceof SqlResultList) {
    				executeSqlForm.setResultList((SqlResultList)result);
    			}
    			else if(result instanceof String) {
    				executeSqlForm.setExecuteResult((String)result);
    			}
    		}
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		executeSqlForm.setExecuteResult(e.getMessage()); //执行结果
    	}
		return mapping.findForward("load");
    }
}