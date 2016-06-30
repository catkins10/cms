package com.yuanluesoft.jeaf.tools.regeneratestaticpages.actions;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tools.regeneratestaticpages.forms.RegenerateStaticPages;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 * 重新生成所有的静态页面
 * @author linchuan
 *
 */
public class Regenerate extends RegenerateStaticPagesAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
		try {
			sessionInfo = getSessionInfo(request, response);
		}
		catch(SessionException se) {
			return redirectToLogin(this, mapping, form, request, response, se, true);
		}
		//检查用户有没有用户管理的权限
		try {
			checkPrivilege(sessionInfo);
		}
		catch(PrivilegeException pe) {
			return redirectToLogin(this, mapping, form, request, response, pe, true);
		}
		StaticPageBuilder staticPageBuilder = (StaticPageBuilder)getService("staticPageBuilder");
		DatabaseService databaseService = (DatabaseService)getService("databaseService");
		RegenerateStaticPages regenerateForm = (RegenerateStaticPages)form;
		if("重建全部站点和栏目页面".equals(regenerateForm.getRegenerateMode())) {
			//按网站目录重新生成静态页面
			staticPageBuilder.reduildAllStaticPages();
		}
		else if("按URL重建".equals(regenerateForm.getRegenerateMode())) {
			if(regenerateForm.getDynamicUrl()!=null && !regenerateForm.getDynamicUrl().isEmpty()) {
				String hql = "select StaticPage.dynamicUrl" +
							 " from StaticPage StaticPage" +
							 " where StaticPage.dynamicUrl like '" + JdbcUtils.resetQuot(regenerateForm.getDynamicUrl()) + "'" +
							 " order by StaticPage.id";
				for(int i=0; i<100000; i+=100) {
					List urls = databaseService.findRecordsByHql(hql, i, 100);
					if(urls==null || urls.isEmpty()) {
						break;
					}
					for(Iterator iterator = urls.iterator(); iterator.hasNext();) {
						String url = (String)iterator.next();
						staticPageBuilder.buildStaticPage(url);
					}
					if(urls.size()<100) {
						break;
					}
				}
			}
		}
		else if("按记录类名称".equals(regenerateForm.getRegenerateMode())) {
			if(regenerateForm.getRecordClassName()!=null && !regenerateForm.getRecordClassName().isEmpty()) {
				String hql = " from " + regenerateForm.getRecordClassName() + " " + regenerateForm.getRecordClassName();
				for(int i=0; i<200000; i+=100) {
					List records = databaseService.findRecordsByHql(hql, i, 100);
					if(records==null || records.isEmpty()) {
						break;
					}
					for(Iterator iterator = records.iterator(); iterator.hasNext();) {
						Record record = (Record)iterator.next();
						staticPageBuilder.rebuildPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
					}
					if(records.size()<100) {
						break;
					}
				}
			}
		}
		regenerateForm.setActionResult("重建完成");
		regenerateForm.getFormActions().addFormAction(-1, "确定", "DialogUtils.closeDialog();", true);
		return mapping.findForward("result");
    }
}