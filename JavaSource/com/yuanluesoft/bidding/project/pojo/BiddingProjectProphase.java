package com.yuanluesoft.bidding.project.pojo;

import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 招标前期资料备案(bidding_project_prophase)
 * @author linchuan
 *
 */
public class BiddingProjectProphase extends BiddingProjectComponent {
	private String submitted; //已提交资料列表
	
	/**
	 * 获取数组形式的资料列表
	 * @return
	 */
	public String[] getSubmittedFiles() {
		return submitted==null ? null : submitted.split("\\r\\n");
	}
	
	/**
	 * 设置已提交的资料列表
	 * @param submittedFiles
	 */
	public void setSubmittedFiles(String[] submittedFiles) {
		submitted = ListUtils.join(submittedFiles, "\r\n", false);
	}

	/**
	 * @return the submitted
	 */
	public String getSubmitted() {
		return submitted;
	}

	/**
	 * @param submitted the submitted to set
	 */
	public void setSubmitted(String submitted) {
		this.submitted = submitted;
	}
}
