package com.yuanluesoft.cms.inquiry.actions.inquiryoption.admin;


import com.yuanluesoft.cms.inquiry.actions.inquirysubject.admin.InquirySubjectAction;
import com.yuanluesoft.cms.inquiry.forms.admin.InquiryOption;
import com.yuanluesoft.jeaf.form.ActionForm;
/**
 * 
 * @author lmiky
 *
 */
public class InquiryOptionAction extends InquirySubjectAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#inheritComponentProperties(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.form.ActionForm)
	 */
	public void inheritComponentProperties(ActionForm newForm, ActionForm oldForm) throws Exception {
		super.inheritComponentProperties(newForm, oldForm);
		InquiryOption newInquiryOption = (InquiryOption)newForm;
		InquiryOption oldInquiryOption = (InquiryOption)oldForm;
		newInquiryOption.getOption().setInquiryId(oldInquiryOption.getOption().getInquiryId());
		newInquiryOption.setSiteId(oldInquiryOption.getSiteId());
		newInquiryOption.setIsQuestionnaire(oldInquiryOption.getIsQuestionnaire());
	}
}