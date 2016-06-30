package com.yuanluesoft.cms.rsssubscription.actions.rss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.rsssubscription.service.RssSubscriptionService;
import com.yuanluesoft.jeaf.action.BaseAction;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		RssSubscriptionService rssSubscriptionService = (RssSubscriptionService)getService("rssSubscriptionService");
		rssSubscriptionService.writeRssChannel(request, response);
		return null;
    }
}