package com.yuanluesoft.cms.inquiry.actions.insertinquiry.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.inquiry.pojo.InquirySubject;
import com.yuanluesoft.cms.inquiry.services.InquiryService;
import com.yuanluesoft.jeaf.action.BaseAction;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Insert extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	com.yuanluesoft.cms.inquiry.forms.admin.InquirySubject insertInquiryForm = (com.yuanluesoft.cms.inquiry.forms.admin.InquirySubject)form;
        //获取调查
    	InquiryService inquiryService = (InquiryService)getService("inquiryService");
    	InquirySubject inquirySubject = (InquirySubject)inquiryService.load(InquirySubject.class, insertInquiryForm.getId());
    	PropertyUtils.copyProperties(insertInquiryForm, inquirySubject);
        return mapping.findForward("load");
    }
}