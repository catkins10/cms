package com.yuanluesoft.bidding.base;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import com.yuanluesoft.bidding.enterprise.model.BiddingSessionInfo;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.base.model.Link;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class BiddingBaseAction extends BaseAction {
	
    public BiddingBaseAction() {
		super();
		sessionInfoClass = BiddingSessionInfo.class; //设置会话类型
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getLoginPageURL()
	 */
	protected Link getLoginPageLink(ActionForm form, HttpServletRequest request) throws SystemUnregistException {
		long siteId = RequestUtils.getParameterLongValue(request, "siteId");
		return new Link(Environment.getWebApplicationSafeUrl() + "/bidding/enterprise/login.shtml" + (siteId>0 ? "?siteId=" + siteId : ""), "utf-8"); 
	}
}