package com.yuanluesoft.cms.inquiry.pojo;

import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 调查选项(cms_inquiry_option)
 * @author linchuan
 *
 */
public class InquiryOption extends Record {
	private long inquiryId;
	private String inquiryOption;
	private int needSupplement = 0; //是否需要补充说明,0/不需要,1/需要
	private String description; //描述
	private float priority; //优先级
	private Inquiry inquiry; //调查
	private Set votes;//投票
	
	/**
	 * 生成选择框
	 * @return
	 */
	public String getInquiryBox() {
		if(inquiry.getIsMultiSelect()=='2' && inquiry.getMaxVote()==1){//填空，票数设置为多票
			inquiry.setMaxVote(2);
		}
		String box = (inquiry.getMaxVote()==1 ? "radio" : "checkbox");
		String title = StringUtils.filterHtmlElement(inquiry.getDescription(), false);
		if(title==null || title.isEmpty()) {
			title = inquiry.getInquirySubject().getSubject();
		}
		else {
			title = StringUtils.slice(title.replace("\"", ""), 100, "...");
		}
		String input= "<input class=\"" + box + "\"" +
			   " id=\"" + getId() + "\"" +
			   " name=\"inquiryOption_" + inquiryId + "\"" +
			   " type=\"" + box + "\"" +
			   " title=\"" + title.replaceAll("\"", "") + "\"" +
			   " value=\"" + inquiry.getMinVote() + "~" + inquiry.getMaxVote() +"~isFillBlank="+ (inquiry.getIsMultiSelect()=='2'?"true":"false")+"\"" +
			   ">";
		return inquiry.getIsMultiSelect()=='2'?"<div style=\"display:none\">"+input+"</div>":input;//填空使用复选框，默认勾选，不显示
	}
	
	/**
	 * 生成补充说明输入框
	 * @return
	 */
	public String getSupplementInputBox() {
		if(needSupplement==0) {
			return null;
		}
//		描述里面本身已经插入说明输入框，不用再另外生成
		if(description==null || description.isEmpty() 
				|| (description.indexOf("id=\"supplement_" + getId())==-1 && description.indexOf("id=supplement_" + getId())==-1)){
			return "<textarea class=\"inquirySupplement\" id=\"supplement_" + getId() +"\""+ (inquiry.getIsMultiSelect()=='2'?" onchange=\"setSupplementInputBoxChecked(this)\"":"")+"></textarea>";	
		}
		else{
			return null;
		}
	}
	/**
	 * @return the description
	 */
	public String getDescription() {//描述里面有插入输入框，并且是填空的，要设置填空的onchange脚本
//		if(description !=null && (description.indexOf("id=\"supplement_" + getId())!=-1 || description.indexOf("id=supplement_" + getId())!=-1)){
//			return description.replaceAll("onchange=[\"']*void\\(0\\)['\"]*", "onchange=\"setSupplementInputBoxChecked(this)\"");	
//		}
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 获取选项明细URL
	 * @return
	 */
	public String getOptionDetailUrl() {
		return description==null || description.isEmpty() ? null : Environment.getContextPath() + "/cms/inquiry/inquiryOption.shtml";
	}
	
	/**
	 * @return the inquiryOption
	 */
	public String getInquiryOption() {
		return inquiryOption;
	}
	/**
	 * @param inquiryOption the inquiryOption to set
	 */
	public void setInquiryOption(String inquiryOption) {
		this.inquiryOption = inquiryOption;
	}
	/**
	 * @return the priority
	 */
	public float getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(float priority) {
		this.priority = priority;
	}

	/**
	 * @return the inquiry
	 */
	public Inquiry getInquiry() {
		return inquiry;
	}

	/**
	 * @param inquiry the inquiry to set
	 */
	public void setInquiry(Inquiry inquiry) {
		this.inquiry = inquiry;
	}

	/**
	 * @return the inquiryId
	 */
	public long getInquiryId() {
		return inquiryId;
	}

	/**
	 * @param inquiryId the inquiryId to set
	 */
	public void setInquiryId(long inquiryId) {
		this.inquiryId = inquiryId;
	}

	/**
	 * @return the needSupplement
	 */
	public int getNeedSupplement() {
		return needSupplement;
	}

	/**
	 * @param needSupplement the needSupplement to set
	 */
	public void setNeedSupplement(int needSupplement) {
		this.needSupplement = needSupplement;
	}

	public Set getVotes() {
		return votes;
	}

	public void setVotes(Set votes) {
		this.votes = votes;
	}
	
	
}