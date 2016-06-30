package com.yuanluesoft.microblog.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 微博用户
 * @author linchuan
 *
 */
public class MicroblogUser implements Serializable, Cloneable {
	private String id; //用户UID 
	private String nickname; //用户昵称 
	private String name; //友好显示名称 
	private String location; //用户所在地 
	private String description; //用户个人描述 
	private String url; //用户博客地址 
	private String profileImageUrl; //用户头像地址（中图），50×50像素 
	private String profileLargeImageUrl; //用户头像地址（大图），180×180像素 
	private String profileHdImageUrl; //用户头像地址（高清），高清头像原图 
	private String profileUrl; //用户的微博统一URL地址 
	private String domain; //用户的个性化域名 
	private char sex; //性别，M：男、F：女、N：未知 
	private int followersCount; //粉丝数 
	private int friendsCount; //关注数 
	private int microblogCount; //微博数 
	private int favouritesCount; //收藏数 
	private Timestamp created; //用户创建（注册）时间 
	private boolean anyoneSendPrivateMessage; //是否允许所有人给我发私信，true：是，false：否 
	private boolean geoEnabled; //是否允许标识用户的地理位置，true：是，false：否 
	private boolean verified; //是否是微博认证用户，即加V用户，true：是，false：否 
	private String remark; //用户备注信息，只有在查询用户关系时才返回此字段 
	private Microblog newestMicroblog; //用户的最近一条微博信息字段
	private boolean anyoneComment; //是否允许所有人对我的微博进行评论，true：是，false：否 
	private String verifiedReason; //认证原因 
	private boolean followMe; //该用户是否关注当前登录用户，true：是，false：否 
	private int onlineStatus; //用户的在线状态，0：不在线、1：在线 
	private int eachOtherFollowersCount; //用户的互粉数 
	private String lang; //用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语 
	
	/**
	 * @return the anyoneComment
	 */
	public boolean isAnyoneComment() {
		return anyoneComment;
	}
	/**
	 * @param anyoneComment the anyoneComment to set
	 */
	public void setAnyoneComment(boolean anyoneComment) {
		this.anyoneComment = anyoneComment;
	}
	/**
	 * @return the anyoneSendPrivateMessage
	 */
	public boolean isAnyoneSendPrivateMessage() {
		return anyoneSendPrivateMessage;
	}
	/**
	 * @param anyoneSendPrivateMessage the anyoneSendPrivateMessage to set
	 */
	public void setAnyoneSendPrivateMessage(boolean anyoneSendPrivateMessage) {
		this.anyoneSendPrivateMessage = anyoneSendPrivateMessage;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}
	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}
	/**
	 * @return the eachOtherFollowersCount
	 */
	public int getEachOtherFollowersCount() {
		return eachOtherFollowersCount;
	}
	/**
	 * @param eachOtherFollowersCount the eachOtherFollowersCount to set
	 */
	public void setEachOtherFollowersCount(int eachOtherFollowersCount) {
		this.eachOtherFollowersCount = eachOtherFollowersCount;
	}
	/**
	 * @return the favouritesCount
	 */
	public int getFavouritesCount() {
		return favouritesCount;
	}
	/**
	 * @param favouritesCount the favouritesCount to set
	 */
	public void setFavouritesCount(int favouritesCount) {
		this.favouritesCount = favouritesCount;
	}
	/**
	 * @return the followersCount
	 */
	public int getFollowersCount() {
		return followersCount;
	}
	/**
	 * @param followersCount the followersCount to set
	 */
	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}
	/**
	 * @return the followMe
	 */
	public boolean isFollowMe() {
		return followMe;
	}
	/**
	 * @param followMe the followMe to set
	 */
	public void setFollowMe(boolean followMe) {
		this.followMe = followMe;
	}
	/**
	 * @return the friendsCount
	 */
	public int getFriendsCount() {
		return friendsCount;
	}
	/**
	 * @param friendsCount the friendsCount to set
	 */
	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}
	/**
	 * @return the geoEnabled
	 */
	public boolean isGeoEnabled() {
		return geoEnabled;
	}
	/**
	 * @param geoEnabled the geoEnabled to set
	 */
	public void setGeoEnabled(boolean geoEnabled) {
		this.geoEnabled = geoEnabled;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}
	/**
	 * @param lang the lang to set
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return the microblogCount
	 */
	public int getMicroblogCount() {
		return microblogCount;
	}
	/**
	 * @param microblogCount the microblogCount to set
	 */
	public void setMicroblogCount(int microblogCount) {
		this.microblogCount = microblogCount;
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
	 * @return the newestMicroblog
	 */
	public Microblog getNewestMicroblog() {
		return newestMicroblog;
	}
	/**
	 * @param newestMicroblog the newestMicroblog to set
	 */
	public void setNewestMicroblog(Microblog newestMicroblog) {
		this.newestMicroblog = newestMicroblog;
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
	 * @return the onlineStatus
	 */
	public int getOnlineStatus() {
		return onlineStatus;
	}
	/**
	 * @param onlineStatus the onlineStatus to set
	 */
	public void setOnlineStatus(int onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
	/**
	 * @return the profileHdImageUrl
	 */
	public String getProfileHdImageUrl() {
		return profileHdImageUrl;
	}
	/**
	 * @param profileHdImageUrl the profileHdImageUrl to set
	 */
	public void setProfileHdImageUrl(String profileHdImageUrl) {
		this.profileHdImageUrl = profileHdImageUrl;
	}
	/**
	 * @return the profileImageUrl
	 */
	public String getProfileImageUrl() {
		return profileImageUrl;
	}
	/**
	 * @param profileImageUrl the profileImageUrl to set
	 */
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
	/**
	 * @return the profileLargeImageUrl
	 */
	public String getProfileLargeImageUrl() {
		return profileLargeImageUrl;
	}
	/**
	 * @param profileLargeImageUrl the profileLargeImageUrl to set
	 */
	public void setProfileLargeImageUrl(String profileLargeImageUrl) {
		this.profileLargeImageUrl = profileLargeImageUrl;
	}
	/**
	 * @return the profileUrl
	 */
	public String getProfileUrl() {
		return profileUrl;
	}
	/**
	 * @param profileUrl the profileUrl to set
	 */
	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the verified
	 */
	public boolean isVerified() {
		return verified;
	}
	/**
	 * @param verified the verified to set
	 */
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	/**
	 * @return the verifiedReason
	 */
	public String getVerifiedReason() {
		return verifiedReason;
	}
	/**
	 * @param verifiedReason the verifiedReason to set
	 */
	public void setVerifiedReason(String verifiedReason) {
		this.verifiedReason = verifiedReason;
	}
}