package com.yuanluesoft.cms.inquiry.actions.inquiry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.inquiry.forms.Inquiry;
import com.yuanluesoft.cms.inquiry.services.InquiryService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.validatecode.service.ValidateCodeService;

/**
 * 
 * @author linchuan
 *
 */
public class Submit extends BaseAction {
   
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		InquiryService inquiryService = (InquiryService)getService("inquiryService");
		Inquiry inquiryForm = (Inquiry)form;
		
		//获取调查主题ID列表
		String inquiryIds = null;
		String[] results = inquiryForm.getInquiryResult().split(";"); //格式:[调查ID];[选中的选项ID1]@补充说明1,[选中的选项ID2]@补充说明2;...
		for(int i=0; i<results.length; i+=2) {
			inquiryIds = (inquiryIds==null ? "" : inquiryIds + ",") + results[i];
		}
		
		//会话检查
		SessionInfo sessionInfo = null;
		try {
			sessionInfo = getSessionInfo(request, response);
		}
		catch(SessionException se) {
			//会话异常,检查是否实名调查,如果是,重定向到登录界面
			if(!inquiryService.isAnonymous(inquiryIds)) { //检查是否匿名调查
				return redirectToLogin(this, mapping, form, request, response, se, true);
			}
		}
		
		if(!RequestUtils.getRequestInfo(request).isClientRequest()) { //不是客户端请求
			//检查验证码
			String validateCode = request.getParameter("validateCode");
			if(validateCode==null) { //没有验证码
				if(inquiryService.isForceValidateCode()) { //强制验证码校验
					return mapping.getInputForward();
				}
			}
			else {
				//检查校验码
				ValidateCodeService validateCodeService = (ValidateCodeService)getService("validateCodeService");
				if(!validateCodeService.validateCode(validateCode, request)) {
					inquiryForm.setError("校验码输入错误");
					return mapping.getInputForward();
				}
				validateCodeService.cleanCode(request, response);
			}
		}
		
		//保存调查结果
		for(int i=0; i<results.length; i+=2) {
			inquiryService.submitInquiry(Long.parseLong(results[i]), results[i+1], RequestUtils.getRemoteAddress(request), sessionInfo);
		}
		
		//判断是否允许直接显示调查结果,如果允许,重定向到调查结果页面
		if(!inquiryService.isHideResult(inquiryIds)) {
			response.sendRedirect("inquiryResult.shtml?inquiryIds=" + inquiryIds + "&siteId=" + RequestUtils.getParameterLongValue(request, "siteId"));
			return null;
		}
		return mapping.findForward("result");
    }
}