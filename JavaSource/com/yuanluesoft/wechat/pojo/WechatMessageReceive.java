package com.yuanluesoft.wechat.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 微信:接收消息(wechat_message_receive)
 * @author linchuan
 *
 */
public class WechatMessageReceive extends Record {
	private long unitId; //单位ID
	private String toUserName; //开发者微信号
	private String fromUserOpenId; //发送方帐号,一个OpenID
	private String fromUserNickname; //发送方用户昵称
	private long fromUserId; //发送方用户ID
	private Timestamp createTime; //消息创建时间
	private String msgId; //消息ID,64位整型
	private String msgType; //消息类型,text/image/voice/location/link
	private String content; //消息内容
	private String picUrl; //图片链接,消息类型为image时有效
	private String mediaId; //消息媒体ID,可以调用多媒体文件下载接口拉取数据
	private String voiceFormat; //语音格式,amr，speex等
	private String locationX; //地理位置纬度
	private String locationY; //地理位置经度
	private String mapScale; //地图缩放大小
	private String locationLabel; //地理位置信息
	private String linkTitle; //链接消息标题
	private String linkDescription; //链接消息描述
	private String url; //消息链接
	private Timestamp replyTime; //答复时间
	private long replierId; //答复人ID
	private String replier; //答复人
	
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the createTime
	 */
	public Timestamp getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the fromUserId
	 */
	public long getFromUserId() {
		return fromUserId;
	}
	/**
	 * @param fromUserId the fromUserId to set
	 */
	public void setFromUserId(long fromUserId) {
		this.fromUserId = fromUserId;
	}
	/**
	 * @return the linkDescription
	 */
	public String getLinkDescription() {
		return linkDescription;
	}
	/**
	 * @param linkDescription the linkDescription to set
	 */
	public void setLinkDescription(String linkDescription) {
		this.linkDescription = linkDescription;
	}
	/**
	 * @return the linkTitle
	 */
	public String getLinkTitle() {
		return linkTitle;
	}
	/**
	 * @param linkTitle the linkTitle to set
	 */
	public void setLinkTitle(String linkTitle) {
		this.linkTitle = linkTitle;
	}
	/**
	 * @return the locationLabel
	 */
	public String getLocationLabel() {
		return locationLabel;
	}
	/**
	 * @param locationLabel the locationLabel to set
	 */
	public void setLocationLabel(String locationLabel) {
		this.locationLabel = locationLabel;
	}
	/**
	 * @return the locationX
	 */
	public String getLocationX() {
		return locationX;
	}
	/**
	 * @param locationX the locationX to set
	 */
	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}
	/**
	 * @return the locationY
	 */
	public String getLocationY() {
		return locationY;
	}
	/**
	 * @param locationY the locationY to set
	 */
	public void setLocationY(String locationY) {
		this.locationY = locationY;
	}
	/**
	 * @return the mapScale
	 */
	public String getMapScale() {
		return mapScale;
	}
	/**
	 * @param mapScale the mapScale to set
	 */
	public void setMapScale(String mapScale) {
		this.mapScale = mapScale;
	}
	/**
	 * @return the mediaId
	 */
	public String getMediaId() {
		return mediaId;
	}
	/**
	 * @param mediaId the mediaId to set
	 */
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	/**
	 * @return the msgId
	 */
	public String getMsgId() {
		return msgId;
	}
	/**
	 * @param msgId the msgId to set
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	/**
	 * @return the msgType
	 */
	public String getMsgType() {
		return msgType;
	}
	/**
	 * @param msgType the msgType to set
	 */
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	/**
	 * @return the picUrl
	 */
	public String getPicUrl() {
		return picUrl;
	}
	/**
	 * @param picUrl the picUrl to set
	 */
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	/**
	 * @return the replier
	 */
	public String getReplier() {
		return replier;
	}
	/**
	 * @param replier the replier to set
	 */
	public void setReplier(String replier) {
		this.replier = replier;
	}
	/**
	 * @return the replierId
	 */
	public long getReplierId() {
		return replierId;
	}
	/**
	 * @param replierId the replierId to set
	 */
	public void setReplierId(long replierId) {
		this.replierId = replierId;
	}
	/**
	 * @return the replyTime
	 */
	public Timestamp getReplyTime() {
		return replyTime;
	}
	/**
	 * @param replyTime the replyTime to set
	 */
	public void setReplyTime(Timestamp replyTime) {
		this.replyTime = replyTime;
	}
	/**
	 * @return the toUserName
	 */
	public String getToUserName() {
		return toUserName;
	}
	/**
	 * @param toUserName the toUserName to set
	 */
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
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
	 * @return the voiceFormat
	 */
	public String getVoiceFormat() {
		return voiceFormat;
	}
	/**
	 * @param voiceFormat the voiceFormat to set
	 */
	public void setVoiceFormat(String voiceFormat) {
		this.voiceFormat = voiceFormat;
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
	 * @return the fromUserNickname
	 */
	public String getFromUserNickname() {
		return fromUserNickname;
	}
	/**
	 * @param fromUserNickname the fromUserNickname to set
	 */
	public void setFromUserNickname(String fromUserNickname) {
		this.fromUserNickname = fromUserNickname;
	}
	/**
	 * @return the fromUserOpenId
	 */
	public String getFromUserOpenId() {
		return fromUserOpenId;
	}
	/**
	 * @param fromUserOpenId the fromUserOpenId to set
	 */
	public void setFromUserOpenId(String fromUserOpenId) {
		this.fromUserOpenId = fromUserOpenId;
	}
}