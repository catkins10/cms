package com.yuanluesoft.bbs.usermanage.model;

import java.sql.Timestamp;

/**
 * 
 * @author linchuan
 *
 */
public class BbsUser {
	private long id;
	private String nickname; //昵称
	private int point; //积分
	private String status; //状态
	private char vip = '0'; //VIP用户
	private int postCount; //发帖数
	private int replyCount; //回帖数
	
	private char sex = 'M'; //性别
	private Timestamp created; //创建时间
	private String area; //所属省份
	private String company; //工作单位/所在院校
	private String email; //邮箱
	private char hideDetail = '0'; //公开用户资料

	private Timestamp lastLoginTime; //最后登录时间
	
	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}
	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	/**
	 * @return the point
	 */
	public int getPoint() {
		return point;
	}
	/**
	 * @param point the point to set
	 */
	public void setPoint(int point) {
		this.point = point;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the vip
	 */
	public char getVip() {
		return vip;
	}
	/**
	 * @param vip the vip to set
	 */
	public void setVip(char vip) {
		this.vip = vip;
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
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the lastLoginTime
	 */
	public Timestamp getLastLoginTime() {
		return lastLoginTime;
	}
	/**
	 * @param lastLoginTime the lastLoginTime to set
	 */
	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	/**
	 * @return the sex
	 */
	public char getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(char sex) {
		this.sex = sex;
	}
	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}
	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}
	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the hideDetail
	 */
	public char getHideDetail() {
		return hideDetail;
	}
	/**
	 * @param hideDetail the hideDetail to set
	 */
	public void setHideDetail(char hideDetail) {
		this.hideDetail = hideDetail;
	}
	/**
	 * @return the postCount
	 */
	public int getPostCount() {
		return postCount;
	}
	/**
	 * @param postCount the postCount to set
	 */
	public void setPostCount(int postCount) {
		this.postCount = postCount;
	}
	/**
	 * @return the replyCount
	 */
	public int getReplyCount() {
		return replyCount;
	}
	/**
	 * @param replyCount the replyCount to set
	 */
	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}
	
}
