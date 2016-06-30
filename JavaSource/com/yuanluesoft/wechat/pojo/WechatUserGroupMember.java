package com.yuanluesoft.wechat.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 微信:用户分组成员(wechat_user_group_member)
 * @author linchuan
 *
 */
public class WechatUserGroupMember extends Record {
	private long groupId; //分组ID
	private long memberId; //成员ID
	private String memberNickname; //成员昵称
	
	/**
	 * @return the groupId
	 */
	public long getGroupId() {
		return groupId;
	}
	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	/**
	 * @return the memberId
	 */
	public long getMemberId() {
		return memberId;
	}
	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}
	/**
	 * @return the memberNickname
	 */
	public String getMemberNickname() {
		return memberNickname;
	}
	/**
	 * @param memberNickname the memberNickname to set
	 */
	public void setMemberNickname(String memberNickname) {
		this.memberNickname = memberNickname;
	}
}