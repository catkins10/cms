package com.yuanluesoft.bidding.project.ask.actions.ask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.ask.service.BiddingProjectAskService;

/**
 * 
 * @author linchuan
 *
 */
public class Submit extends AskAction {
    
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		anonymousAlways = true;
		BiddingProjectAskService biddingProjectAskService = (BiddingProjectAskService)getService("biddingProjectAskService");
		if(!biddingProjectAskService.isNeedReply()) { //不需要应答
			return executeSaveAction(mapping, form, request, response, false, null, "提交成功", "outerResult");
		}
		else {
			return executeRunAction(mapping, form, request, response, true, "提交成功", "outerResult");
		}
    }
}