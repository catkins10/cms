package com.yuanluesoft.cms.onlineservice.faq.actions.faqview.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.onlineservice.faq.forms.admin.FaqView;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewAction;

/**
 * 
 * @author linchuan
 *
 */
public class FaqViewAction extends ViewFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplication(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "cms/onlineservice/faq";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "admin/faq";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		FaqView faqViewForm = (FaqView)viewForm;
	    if(faqViewForm.getDirectoryId()!=0) { //根目录显示全部
	    	String where = "(" +
	    				   " subjections.directoryId in (" +
	    				   "  select OnlineServiceDirectorySubjection.directoryId" +
	    				   "   from OnlineServiceDirectorySubjection OnlineServiceDirectorySubjection" +
	    				   "   where OnlineServiceDirectorySubjection.parentDirectoryId=" +  faqViewForm.getDirectoryId() + ")" +
	    				   ")" +
	    				   "or (" +
	    				   " subjectionItems.itemId in (" +
	    				   "  select OnlineServiceItem.id" +
	    				   "   from OnlineServiceItem OnlineServiceItem" +
	    				   "   left join OnlineServiceItem.subjections OnlineServiceItemSubjection, OnlineServiceDirectorySubjection OnlineServiceDirectorySubjection" +
	    				   "   where OnlineServiceItemSubjection.directoryId=OnlineServiceDirectorySubjection.directoryId" +
	    				   "   and OnlineServiceDirectorySubjection.parentDirectoryId=" +  faqViewForm.getDirectoryId() + ")" +
	    				   ")";
	    	view.addWhere(where);
	    }
	    OnlineServiceDirectoryService onlineServiceDirectoryService = (OnlineServiceDirectoryService)getService("onlineServiceFaqDirectoryService");
		//获取用户对目录的权限
		List popedoms = onlineServiceDirectoryService.listDirectoryPopedoms(faqViewForm.getDirectoryId(), sessionInfo);
		if(popedoms==null) {
			popedoms = new ArrayList();
		}
		List actions = new ArrayList();
		if(popedoms.contains("manager") || popedoms.contains("transactor")) {
			ViewAction viewAction = new ViewAction();
			viewAction.setTitle("新增常见问题");
			viewAction.setExecute("PageUtils.newrecord('cms/onlineservice/faq', 'admin/faq', 'mode=fullscreen', 'directoryId=" + faqViewForm.getDirectoryId() + "')");
			actions.add(viewAction);
			
			viewAction = new ViewAction();
			viewAction.setTitle("复制到其它目录");
			viewAction.setExecute("selectOnlineServiceDirectory(640, 400, true, \"{id},{name|选择目录|100%}\", \"FormUtils.doAction('importFaqs', 'from=" + faqViewForm.getDirectoryId() + "&to={id}')\")");
			actions.add(viewAction);
		}
		if(view.getActions()!=null && !view.getActions().isEmpty()) {
			actions.addAll(view.getActions());
		}
		view.setActions(actions);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetViewLocation(com.yuanluesoft.jeaf.view.forms.ViewForm, java.util.List, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetViewLocation(ViewForm viewForm, List location, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetViewLocation(viewForm, location, view, sessionInfo, request);
		FaqView faqViewForm = (FaqView)viewForm;
		OnlineServiceDirectoryService onlineServiceDirectoryService = (OnlineServiceDirectoryService)getService("onlineServiceFaqDirectoryService");
		location.remove(location.size()-1);
		String fullName = onlineServiceDirectoryService.getDirectoryFullName(faqViewForm.getDirectoryId(), "/", null);
		if(fullName!=null) {
			String[] names = fullName.split("/");
			for(int i=0; i<names.length; i++) {
				location.add(names[i]);
			}
		}
	}
}