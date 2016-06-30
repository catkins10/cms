package com.yuanluesoft.cms.inquiry.actions.inquiryresult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.inquiry.services.InquiryService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author chuan
 *
 */
public class WriteChart extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        InquiryService inquiryService = (InquiryService)getService("inquiryService");
        inquiryService.writeInquiryResultChart(RequestUtils.getParameterLongValue(request, "inquiryId"), RequestUtils.getParameterIntValue(request, "chartWidth"), RequestUtils.getParameterIntValue(request, "chartHeight"), RequestUtils.getParameterStringValue(request, "chartMode"), response);
        return null;
    }
}