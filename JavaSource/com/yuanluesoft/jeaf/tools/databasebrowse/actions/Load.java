package com.yuanluesoft.jeaf.tools.databasebrowse.actions;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tools.databasebrowse.forms.DatabaseBrowse;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends DatabaseBrowseAction {
    
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
    	if(browseForm.getJdbcURL()==null ||  browseForm.getJdbcUserName()==null || browseForm.getJdbcPassword()==null) {
    		return mapping.findForward("load");
    	}
    	//将JDBC信息写入会话
    	request.getSession().setAttribute("jdbcSession", new String[]{browseForm.getJdbcURL(), browseForm.getJdbcUserName(), browseForm.getJdbcPassword()});
    	//获取表列表
    	try {
    		List tableNames = JdbcUtils.listTableNames(browseForm.getJdbcURL(), browseForm.getJdbcUserName(), browseForm.getJdbcPassword());
    		Tree tableTree = new Tree("tables", "表" + (tableNames==null ? "" : "(" + tableNames.size() + ")"), "tables", Environment.getContextPath() + "/jeaf/tools/databasebrowse/icons/tables.gif");
    		for(Iterator iterator = tableNames==null ? null : tableNames.iterator(); iterator!=null && iterator.hasNext();) {
    			String tableName = (String)iterator.next();
    			tableTree.appendChildNode("table_" + tableName, tableName, "table", Environment.getContextPath() + "/jeaf/tools/databasebrowse/icons/table.gif", false);
    		}
    		browseForm.setTableTree(tableTree);
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		browseForm.setJdbcUserName(null);
    		browseForm.setJdbcPassword(null);
    		browseForm.setError(e.getMessage());
    	}
		browseForm.setDisplayMode("window");
	    return mapping.findForward("load");
    }
}