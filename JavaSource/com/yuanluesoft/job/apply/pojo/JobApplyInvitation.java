package com.yuanluesoft.job.apply.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 邀请函(job_apply_invitation)
 * @author linchuan
 *
 */
public class JobApplyInvitation extends Record {
	private long applyId; //应聘ID
	private String body; //正文
	private Timestamp sent; //发送时间
	private JobApply apply; //求职申请
	
	/**
	 * @return the applyId
	 */
	public long getApplyId() {
		return applyId;
	}
	/**
	 * @param applyId the applyId to set
	 */
	public void setApplyId(long applyId) {
		this.applyId = applyId;
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	/**
	 * @return the sent
	 */
	public Timestamp getSent() {
		return sent;
	}
	/**
	 * @param sent the sent to set
	 */
	public void setSent(Timestamp sent) {
		this.sent = sent;
	}
	/**
	 * @return the apply
	 */
	public JobApply getApply() {
		return apply;
	}
	/**
	 * @param apply the apply to set
	 */
	public void setApply(JobApply apply) {
		this.apply = apply;
	}
}