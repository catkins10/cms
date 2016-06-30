package com.yuanluesoft.cms.inquiry.actions.inquiry.admin.outputExcel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.inquiry.forms.admin.InquirySubject;
import com.yuanluesoft.cms.inquiry.services.spring.InquiryOutputExcelServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.util.Environment;


public class OutputExcel extends FormAction {


	public ActionForward execute(ActionMapping mapp, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO 自动生成方法存根
		InquirySubject inquirySubject = (InquirySubject)form;
        InquiryOutputExcelServiceImpl inquiryOutputExcelService;
		try {
			inquiryOutputExcelService = (InquiryOutputExcelServiceImpl) Environment.getService("inquiryOutputExcelService");
			inquiryOutputExcelService.createExcel(inquirySubject.getId(), request, response);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
