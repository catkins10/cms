package com.yuanluesoft.wechat.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 微信:用户(wechat_user)
 * @author linchuan
 *
 */
public class WechatUser extends Record {
	private long unitId; //单位ID
	private String openId; //用户标识,对当前公众号唯一
	private String nickname; //用户昵称
	private char sex; //用户的性别,M/F
	private String city; //用户所在城市
	private String province; //用户所在省份
	private String country; //用户所在国家
	private String language; //用户的语言,简体中文为zh_CN
	private String headimgUrl; //用户头像,最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
	private Timestamp subscribeTime; //用户关注时间,为时间戳。如果用户曾多次关注，则取最后关注时间
	private Timestamp unsubscribeTime; //取消关注时间
	private String name; //真实姓名
	private Timestamp created; //创建时间
	private String remark; //备注
	private Set groupMembers; //分组成员,用于同步删除
	
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
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
	 * @return the headimgUrl
	 */
	public String getHeadimgUrl() {
		return headimgUrl;
	}
	/**
	 * @param headimgUrl the headimgUrl to set
	 */
	public void setHeadimgUrl(String headimgUrl) {
		this.headimgUrl = headimgUrl;
	}
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
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
	 * @return the openId
	 */
	public String getOpenId() {
		return openId;
	}
	/**
	 * @param openId the openId to set
	 */
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
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
	 * @return the subscribeTime
	 */
	public Timestamp getSubscribeTime() {
		return subscribeTime;
	}
	/**
	 * @param subscribeTime the subscribeTime to set
	 */
	public void setSubscribeTime(Timestamp subscribeTime) {
		this.subscribeTime = subscribeTime;
	}
	/**
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}
	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}
	/**
	 * @return the unsubscribeTime
	 */
	public Timestamp getUnsubscribeTime() {
		return unsubscribeTime;
	}
	/**
	 * @param unsubscribeTime the unsubscribeTime to set
	 */
	public void setUnsubscribeTime(Timestamp unsubscribeTime) {
		this.unsubscribeTime = unsubscribeTime;
	}
	/**
	 * @return the groupMembers
	 */
	public Set getGroupMembers() {
		return groupMembers;
	}
	/**
	 * @param groupMembers the groupMembers to set
	 */
	public void setGroupMembers(Set groupMembers) {
		this.groupMembers = groupMembers;
	}
}