package com.yuanluesoft.bidding.project.actions.projectinfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;

/**
 * 项目报建,匿名用户
 * @author linchuan
 *
 */
public class Load extends BaseAction {
   
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	PageService pageService = (PageService)getService("biddingProjectInfoPageService");
    	pageService.writePage("bidding/project", "biddingProjectInfo", request, response, false);
    	return null;
    }
}