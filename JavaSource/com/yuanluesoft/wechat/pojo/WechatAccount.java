package com.yuanluesoft.wechat.pojo;

import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 微信:公众号配置(wechat_account)
 * @author linchuan
 *
 */
public class WechatAccount extends Record {
	private long unitId; //单位ID
	private String siteUrl; //网址
	private String name; //名称
	private int accountType; //帐号类型,0/服务号,1/订阅号
	private int certificate; //是否认证过
	private String token; //令牌
	private String appId; //用户唯一凭证
	private String appSecret; //用户唯一凭证密钥
	private Set menuItems; //菜单项目
	
	/**
	 * @return the accountType
	 */
	public int getAccountType() {
		return accountType;
	}
	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}
	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}
	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}
	/**
	 * @return the appSecret
	 */
	public String getAppSecret() {
		return appSecret;
	}
	/**
	 * @param appSecret the appSecret to set
	 */
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	/**
	 * @return the certificate
	 */
	public int getCertificate() {
		return certificate;
	}
	/**
	 * @param certificate the certificate to set
	 */
	public void setCertificate(int certificate) {
		this.certificate = certificate;
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
	 * @return the menuItems
	 */
	public Set getMenuItems() {
		return menuItems;
	}
	/**
	 * @param menuItems the menuItems to set
	 */
	public void setMenuItems(Set menuItems) {
		this.menuItems = menuItems;
	}
	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
	/**
	 * @return the siteUrl
	 */
	public String getSiteUrl() {
		return siteUrl;
	}
	/**
	 * @param siteUrl the siteUrl to set
	 */
	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}
}