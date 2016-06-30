package com.yuanluesoft.bbs.article.actions.myreplyarticles;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.bbs.base.actions.BbsViewFormAction;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class MyReplyArticlesAction extends BbsViewFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "bbs/article";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "myReplyArticles";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		//获取模板配置信息
		/*MyReplyArticles myReplyArticlesForm = (MyReplyArticles)viewForm;
    	TemplateService templateService = (TemplateService)getService("templateService");
    	myReplyArticlesForm.setApplicationPage(templateService.getSubPageConfigure("bbs", "personalPanel", myReplyArticlesForm.getSiteId(), "personalPanelConfigure"));
    	*/
	}
}