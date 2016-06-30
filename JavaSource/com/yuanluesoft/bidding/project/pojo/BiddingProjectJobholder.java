package com.yuanluesoft.bidding.project.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 工程项目:人员需求(bidding_project_jobholder)
 * @author linchuan
 *
 */
public class BiddingProjectJobholder extends Record {
	private long projectId; //项目ID
	private String jobholderCategory; //人员类别
	private String qualifications; //资质等级
	private int jobholderNumber; //需求数量
	
	/**
	 * 获取人员描述
	 * @return
	 */
	public String getJobholderTitle() {
		return jobholderCategory + (qualifications==null || qualifications.isEmpty() ? "" : "(" + qualifications + ")");
	}

	/**
	 * 获取人员选择器
	 * @return
	 */
	public String getJobholderSelector() {
		String fieldName = "jobholderNames_" + getId();
		return "<input type=\"hidden\" name=\"jobholderIds_" + getId() + "\">" +
			   "<script>new SelectField('<input type=\"text\" readonly name=\"" + fieldName + "\">', 'selectJobholder(\"" +getId() + "\", \"" + jobholderCategory + "\", \"" + (qualifications==null || qualifications.isEmpty() ? "" : qualifications) + "\", " + jobholderNumber + ")', 'field');</script>";
	}
	
	/**
	 * @return the jobholderCategory
	 */
	public String getJobholderCategory() {
		return jobholderCategory;
	}
	/**
	 * @param jobholderCategory the jobholderCategory to set
	 */
	public void setJobholderCategory(String jobholderCategory) {
		this.jobholderCategory = jobholderCategory;
	}
	/**
	 * @return the jobholderNumber
	 */
	public int getJobholderNumber() {
		return jobholderNumber;
	}
	/**
	 * @param jobholderNumber the jobholderNumber to set
	 */
	public void setJobholderNumber(int jobholderNumber) {
		this.jobholderNumber = jobholderNumber;
	}
	/**
	 * @return the projectId
	 */
	public long getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return the qualifications
	 */
	public String getQualifications() {
		return qualifications;
	}
	/**
	 * @param qualifications the qualifications to set
	 */
	public void setQualifications(String qualifications) {
		this.qualifications = qualifications;
	}
}