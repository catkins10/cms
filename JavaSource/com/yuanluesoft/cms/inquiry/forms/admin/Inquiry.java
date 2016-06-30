package com.yuanluesoft.cms.inquiry.forms.admin;

/**
 * 
 * @author lmiky
 *
 */
public class Inquiry extends InquirySubject {
	private com.yuanluesoft.cms.inquiry.pojo.Inquiry inquiry = new com.yuanluesoft.cms.inquiry.pojo.Inquiry();

	/**
	 * @return the inquiry
	 */
	public com.yuanluesoft.cms.inquiry.pojo.Inquiry getInquiry() {
		return inquiry;
	}

	/**
	 * @param inquiry the inquiry to set
	 */
	public void setInquiry(com.yuanluesoft.cms.inquiry.pojo.Inquiry inquiry) {
		this.inquiry = inquiry;
	}
}