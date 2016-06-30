package com.yuanluesoft.jeaf.tools.upgradetemplate.actions;

import java.io.File;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.cms.templatemanage.pojo.Template;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.security.service.AccessControlService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tools.upgradetemplate.upgrader.TemplateUpgrader;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.callback.FileSearchCallback;

/**
 * 
 * @author linchuan
 *
 */
public class Upgrade extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	SessionInfo sessionInfo;
		try {
			sessionInfo = getSessionInfo(request, response);
		}
		catch(SessionException se) {
			return redirectToLogin(this, mapping, form, request, response, se, false);
		}
		response.setCharacterEncoding("utf-8");
		//检查用户有没有用户管理的权限
		AccessControlService accessControlService = (AccessControlService)getService("accessControlService");
		List acl = accessControlService.getAcl("jeaf/usermanage", sessionInfo);
		if(!acl.contains("application_manager")) {
			return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
		}
		//获取升级器列表
		final Date lastUpdate = RequestUtils.getParameterDateValue(request, "lastUpdate");
		if(lastUpdate==null) {
			response.getWriter().println("参数系统最后更新时间[lastUpdate]为空或者格式不正确");
			return null;
		}
		final List upgraders = new ArrayList();
		String classesPath = Environment.getWebinfPath() + "classes/com/yuanluesoft/jeaf/tools/upgradetemplate/upgrader/impl";
		FileUtils.fileSearch(classesPath, null, new FileSearchCallback() {
			public void onFileFound(File file) {
				if(file.getName().endsWith(".class") && file.getName().indexOf('$')==-1) {
					String className = file.getPath();
					className = className.substring(className.indexOf("classes") + "classes".length() + 1, className.length() - ".class".length()).replaceAll("[\\\\/]", ".");
					try {
						TemplateUpgrader upgrader = (TemplateUpgrader)Class.forName(className).newInstance();
						if(DateTimeUtils.parseDate(upgrader.getCreateDate(), null).after(lastUpdate)) {
							upgraders.add(upgrader);
						}
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		//排序
		Collections.sort(upgraders, new Comparator() {
			/* (non-Javadoc)
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			public int compare(Object arg0, Object arg1) {
				try {
					Date createDate0 = DateTimeUtils.parseDate(((TemplateUpgrader)arg0).getCreateDate(), null);
					Date createDate1 = DateTimeUtils.parseDate(((TemplateUpgrader)arg1).getCreateDate(), null);
					return createDate0.compareTo(createDate1);
				}
				catch(ParseException e) {
					return 0;
				}
			}
		});
		for(Iterator iteratorUpgrader = upgraders.iterator(); iteratorUpgrader.hasNext();) {
			TemplateUpgrader upgrader = (TemplateUpgrader)iteratorUpgrader.next();
			System.out.println("TemplateUpgrade: found upgrader " + upgrader);
		}
		//升级模板
		StaticPageBuilder staticPageBuilder = (StaticPageBuilder)Environment.getService("staticPageBuilder");
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		for(int i=0; ; i+=200) {
			List templates = databaseService.findRecordsByHql("from Template Template order by Template.id", i, 200);
			if(templates==null) {
				break;
			}
			for(Iterator iterator = templates.iterator(); iterator.hasNext();) {
				Template template = (Template)iterator.next();
				String templateFilePath = Environment.getWebAppPath() + "cms/templates/" + template.getId() + "/template.html";
				if(!FileUtils.isExists(templateFilePath)) {
					continue;
				}
				System.out.println("TemplateUpgrade: upgrade template " + template.getApplicationName() + "/" + template.getPageName() + "/" + template.getTemplateName() + "," + templateFilePath);
				String templateHTML = FileUtils.readStringFromFile(templateFilePath, "UTF-8");
				if(templateHTML==null || templateHTML.isEmpty()) {
					continue;
				}
				String newHTML = templateHTML;
				try {
					for(Iterator iteratorUpgrader = upgraders.iterator(); iteratorUpgrader.hasNext();) {
						TemplateUpgrader upgrader = (TemplateUpgrader)iteratorUpgrader.next();
						newHTML = upgrader.upgrade(template, newHTML, request);
					}
				}
				catch(Exception e) {
					e.printStackTrace();
					continue;
				}
				//保存升级后的模板
				if(newHTML.length()!=templateHTML.length() || !newHTML.equals(templateHTML)) {
					System.out.println("TemplateUpgrade: save template " + template.getApplicationName() + "/" + template.getPageName() + "/" + template.getTemplateName() + "," + templateFilePath);
					FileUtils.saveStringToFile(templateFilePath, newHTML, "UTF-8", true);
					if(template.getIsSelected()=='1') { //默认模板
						staticPageBuilder.rebuildPageForTemplate(template.getApplicationName(), template.getPageName(), template.getId(), template.getSiteId(), false);
					}
				}
			}
			if(templates.size()<200) {
				break;
			}
		}
		response.getWriter().println("completed.");
		return null;
    }
}