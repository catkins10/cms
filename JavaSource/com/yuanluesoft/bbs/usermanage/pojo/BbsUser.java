/**
 * 
 */
package com.yuanluesoft.bbs.usermanage.pojo;

import com.yuanluesoft.jeaf.database.Record;



/**
 * 论坛:用户(bbs_user)
 * @author linchuan
 *
 */
public class BbsUser extends Record {
	private String nickname; //昵称
	private int point; //积分
	private String status; //状态
	private char vip = '0'; //VIP用户
	private int postCount; //发帖数
	private int replyCount; //回帖数
	private char type = '1'; //用户类型,系统用户/1,网上注册用户/2
	
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
	 * @return the type
	 */
	public char getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(char type) {
		this.type = type;
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
