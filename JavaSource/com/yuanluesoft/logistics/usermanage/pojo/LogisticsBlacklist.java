package com.yuanluesoft.logistics.usermanage.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 物流联盟用户:黑名单(logistics_blacklist)
 * @author linchuan
 *
 */
public class LogisticsBlacklist extends Record {
	private long userId; //用户ID
	private String userName; //公司名称/个人姓名
	private Timestamp blacklistBegin; //列入黑名单时间
	private Timestamp blacklistEnd; //黑名单解禁时间
	private String reason; //列入原因
	private String remark; //备注
	
	/**
	 * @return the blacklistBegin
	 */
	public Timestamp getBlacklistBegin() {
		return blacklistBegin;
	}
	/**
	 * @param blacklistBegin the blacklistBegin to set
	 */
	public void setBlacklistBegin(Timestamp blacklistBegin) {
		this.blacklistBegin = blacklistBegin;
	}
	/**
	 * @return the blacklistEnd
	 */
	public Timestamp getBlacklistEnd() {
		return blacklistEnd;
	}
	/**
	 * @param blacklistEnd the blacklistEnd to set
	 */
	public void setBlacklistEnd(Timestamp blacklistEnd) {
		this.blacklistEnd = blacklistEnd;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
}