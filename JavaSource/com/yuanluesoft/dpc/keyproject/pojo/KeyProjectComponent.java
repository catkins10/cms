package com.yuanluesoft.dpc.keyproject.pojo;

import java.sql.Timestamp;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.jeaf.database.Record;


/**
 * 项目组成部分
 * @author linchuan
 *
 */
public class KeyProjectComponent extends Record {
	private long projectId; //项目ID
	private char needApproval = '1'; //是否需要审核
	private long approverId; //审核人ID
	private Timestamp approvalTime; //审核时间
	
	/**
	 * 是否需要审核
	 * @return
	 */
	public String getNeedApprovalTitle() {
		if(needApproval=='0') {
			return "";
		}
		else if(needApproval=='1') {
			return "√";
		}
		else if(PropertyUtils.isReadable(this, "completed")) {
			try {
				return "1".equals(PropertyUtils.getProperty(this, "completed") + "") ? "√" : ""; //已经提交办理情况
			}
			catch(Exception e) {
				
			}
		}
		return "";
	}
	
	/**
	 * @return the approvalTime
	 */
	public Timestamp getApprovalTime() {
		return approvalTime;
	}
	/**
	 * @param approvalTime the approvalTime to set
	 */
	public void setApprovalTime(Timestamp approvalTime) {
		this.approvalTime = approvalTime;
	}
	/**
	 * @return the approverId
	 */
	public long getApproverId() {
		return approverId;
	}
	/**
	 * @param approverId the approverId to set
	 */
	public void setApproverId(long approverId) {
		this.approverId = approverId;
	}
	/**
	 * @return the needApproval
	 */
	public char getNeedApproval() {
		return needApproval;
	}
	/**
	 * @param needApproval the needApproval to set
	 */
	public void setNeedApproval(char needApproval) {
		this.needApproval = needApproval;
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
}
