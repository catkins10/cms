package com.yuanluesoft.cms.inquiry.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Inquiry extends ActionForm {
	private String inquiryIds; //调查ID列表
	private String inquiryResult; //用户选择的结果,格式:主题ID1;选中的项目1,选中的项目2;主题ID2;选中的项目1,选中的项目2

	/**
	 * @return the inquiryResult
	 */
	public String getInquiryResult() {
		return inquiryResult;
	}
	/**
	 * @param inquiryResult the inquiryResult to set
	 */
	public void setInquiryResult(String inquiryResult) {
		this.inquiryResult = inquiryResult;
	}
	/**
	 * @return the inquiryIds
	 */
	public String getInquiryIds() {
		return inquiryIds;
	}
	/**
	 * @param inquiryIds the inquiryIds to set
	 */
	public void setInquiryIds(String inquiryIds) {
		this.inquiryIds = inquiryIds;
	}
}