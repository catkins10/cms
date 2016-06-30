package com.yuanluesoft.cms.inquiry.pojo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 调查(cms_inquiry)
 * @author linchuan
 *
 */
public class Inquiry extends Record {
	private long subjectId; //主题ID
	private String description; //描述
	private char isMultiSelect = '0'; //单选/多选
	private int minVote; //最低投票数,0表示不限制
	private int maxVote; //最高投票数,0表示不限制
	private Timestamp created; //创建时间
	private double priority; //优先级
	private InquirySubject inquirySubject; //调查主题
	private Set options; //选项列表
	
	//附加属性
	private List results; //投票结果列表
	private List matchs; //投票匹配情况统计
	
	/**
	 * 获取描述(纯文本)
	 * @return
	 */
	public String getDescriptionText() {
		return StringUtils.filterHtmlElement(description, false);
	}
	
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the inquirySubject
	 */
	public InquirySubject getInquirySubject() {
		return inquirySubject;
	}

	/**
	 * @param inquirySubject the inquirySubject to set
	 */
	public void setInquirySubject(InquirySubject inquirySubject) {
		this.inquirySubject = inquirySubject;
	}

	/**
	 * @return the isMultiSelect
	 */
	public char getIsMultiSelect() {
		return isMultiSelect;
	}

	/**
	 * @param isMultiSelect the isMultiSelect to set
	 */
	public void setIsMultiSelect(char isMultiSelect) {
		this.isMultiSelect = isMultiSelect;
	}

	/**
	 * @return the maxVote
	 */
	public int getMaxVote() {
		return maxVote;
	}

	/**
	 * @param maxVote the maxVote to set
	 */
	public void setMaxVote(int maxVote) {
		this.maxVote = maxVote;
	}

	/**
	 * @return the minVote
	 */
	public int getMinVote() {
		return minVote;
	}

	/**
	 * @param minVote the minVote to set
	 */
	public void setMinVote(int minVote) {
		this.minVote = minVote;
	}

	/**
	 * @return the options
	 */
	public Set getOptions() {
		return options;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(Set options) {
		this.options = options;
	}

	/**
	 * @return the priority
	 */
	public double getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(double priority) {
		this.priority = priority;
	}

	/**
	 * @return the results
	 */
	public List getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(List results) {
		this.results = results;
	}

	/**
	 * @return the subjectId
	 */
	public long getSubjectId() {
		return subjectId;
	}

	/**
	 * @param subjectId the subjectId to set
	 */
	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	/**
	 * @return the matchs
	 */
	public List getMatchs() {
		return matchs;
	}

	/**
	 * @param matchs the matchs to set
	 */
	public void setMatchs(List matchs) {
		this.matchs = matchs;
	}
}