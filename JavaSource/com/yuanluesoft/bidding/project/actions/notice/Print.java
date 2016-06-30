package com.yuanluesoft.bidding.project.actions.notice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;

/**
 * 中标通知书打印
 * @author linchuan
 *
 */
public class Print extends BaseAction {
   
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	PageService pageService = (PageService)getService("biddingProjectComponentPageService");
    	pageService.writePage("bidding/project", "biddingNoticePrint", request, response, false);
    	return null;
    }
}