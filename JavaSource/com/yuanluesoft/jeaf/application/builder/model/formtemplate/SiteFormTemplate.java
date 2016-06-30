package com.yuanluesoft.jeaf.application.builder.model.formtemplate;

import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.cms.sitemanage.forms.SiteForm;
import com.yuanluesoft.cms.sitemanage.pojo.SiteData;
import com.yuanluesoft.jeaf.application.builder.actions.standardforms.site.SiteFormAction;
import com.yuanluesoft.jeaf.application.builder.pojo.ApplicationForm;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewAction;

/**
 * 
 * @author linchuan
 *
 */
public class SiteFormTemplate extends FormTemplate {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.NormalFormTemplate#getName()
	 */
	public String getName() {
		return "site";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.NormalFormTemplate#getLabel()
	 */
	public String getLabel() {
		return "参数配置(网站应用)";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.NormalFormTemplate#getParentRecordClass()
	 */
	public Class getParentRecordClass() {
		return SiteData.class;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.formtemplate.FormTemplate#getStrutsActionPackageName()
	 */
	protected String getStrutsActionPackageName() {
		return SiteFormAction.class.getPackage().getName();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.formtemplate.FormTemplate#getParentFormClass()
	 */
	protected Class getParentFormClass() {
		return SiteForm.class;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.builder.model.formtemplate.FormTemplate#resetViews(java.util.List, com.yuanluesoft.jeaf.application.builder.pojo.ApplicationForm, java.lang.String, java.lang.String)
	 */
	public void resetViews(List views, ApplicationForm applicationForm, String applicationName, String recordClassName) {
		super.resetViews(views, applicationForm, applicationName, recordClassName);
		//重试视图操作
		for(Iterator iterator = views.iterator(); iterator.hasNext();) {
			View view = (View)iterator.next();
			if(view.getActions()!=null) {
				((ViewAction)view.getActions().get(0)).setExecute("PageUtils.newrecord('" + applicationName + "', '" + applicationForm.getEnglishName() + "', 'mode=fullscreen', 'siteId={PARAMETER:siteId}')"); //执行的操作
			}
			view.setHideCondition("noPrivilege(application_manager) and isNotSiteManager()");
			view.setUrl("/cms/sitemanage/siteApplicationConfigView.shtml?applicationName=" + applicationName + "&viewName=" + view.getName());
		}
	}
}