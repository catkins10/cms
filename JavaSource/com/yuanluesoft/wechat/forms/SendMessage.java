package com.yuanluesoft.wechat.forms;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.wechat.pojo.WechatMessageReceive;

/**
 * 
 * @author linchuan
 *
 */
public class SendMessage extends WorkflowForm {
	private long unitId; //单位ID
	private String type; //消息类型,text/voice/video/music/news
	private long creatorId; //创建人ID
	private String creator; //创建人
	private Timestamp created; //创建时间
	private Timestamp sendTime; //发送时间
	private int rangeMode; //发送范围,0/全部,1/指定分组,2/指定用户
	private String groupNames; //分组名称列表
	private String groupIds; //分组ID列表
	private String userNames; //用户列表
	private String userIds; //用户ID列表
	private String title; //消息标题,消息类型为视频时有效
	private String description; //消息描述,消息类型为视频时有效
	private String content; //消息内容
	private int showCoverPic; //是否显示封面,type=news时有效,0/不显示,1/显示
	private String msgId; //微信消息ID,用于检查发送状态
	private String status; //状态
	private int totalCount; //用户数,group_id下粉丝数；或者openid_list中的粉丝数 
	private int filterCount; //过滤后用户数,FilterCount = SentCount + ErrorCount 
	private int sentCount; //发送成功用户数
	private int errorCount; //发送失败用户数
	private long receiveMessageId; //接收消息ID,客服消息,48小时内允许答复
	private Set news; //图文消息
	
	//扩展属性
	private boolean appendNewsDisabled; //是否禁止添加新的图文消息
	private WechatMessageReceive messageReceive; //接收到的消息

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
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the sendTime
	 */
	public Timestamp getSendTime() {
		return sendTime;
	}
	/**
	 * @param sendTime the sendTime to set
	 */
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @return the errorCount
	 */
	public int getErrorCount() {
		return errorCount;
	}
	/**
	 * @param errorCount the errorCount to set
	 */
	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}
	/**
	 * @return the filterCount
	 */
	public int getFilterCount() {
		return filterCount;
	}
	/**
	 * @param filterCount the filterCount to set
	 */
	public void setFilterCount(int filterCount) {
		this.filterCount = filterCount;
	}
	/**
	 * @return the groupIds
	 */
	public String getGroupIds() {
		return groupIds;
	}
	/**
	 * @param groupIds the groupIds to set
	 */
	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}
	/**
	 * @return the groupNames
	 */
	public String getGroupNames() {
		return groupNames;
	}
	/**
	 * @param groupNames the groupNames to set
	 */
	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}
	/**
	 * @return the rangeMode
	 */
	public int getRangeMode() {
		return rangeMode;
	}
	/**
	 * @param rangeMode the rangeMode to set
	 */
	public void setRangeMode(int rangeMode) {
		this.rangeMode = rangeMode;
	}
	/**
	 * @return the receiveMessageId
	 */
	public long getReceiveMessageId() {
		return receiveMessageId;
	}
	/**
	 * @param receiveMessageId the receiveMessageId to set
	 */
	public void setReceiveMessageId(long receiveMessageId) {
		this.receiveMessageId = receiveMessageId;
	}
	/**
	 * @return the sentCount
	 */
	public int getSentCount() {
		return sentCount;
	}
	/**
	 * @param sentCount the sentCount to set
	 */
	public void setSentCount(int sentCount) {
		this.sentCount = sentCount;
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
	 * @return the totalCount
	 */
	public int getTotalCount() {
		return totalCount;
	}
	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/**
	 * @return the userIds
	 */
	public String getUserIds() {
		return userIds;
	}
	/**
	 * @param userIds the userIds to set
	 */
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	/**
	 * @return the userNames
	 */
	public String getUserNames() {
		return userNames;
	}
	/**
	 * @param userNames the userNames to set
	 */
	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}
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
	 * @return the news
	 */
	public Set getNews() {
		return news;
	}
	/**
	 * @param news the news to set
	 */
	public void setNews(Set news) {
		this.news = news;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the appendNewsDisabled
	 */
	public boolean isAppendNewsDisabled() {
		return appendNewsDisabled;
	}
	/**
	 * @param appendNewsDisabled the appendNewsDisabled to set
	 */
	public void setAppendNewsDisabled(boolean appendNewsDisabled) {
		this.appendNewsDisabled = appendNewsDisabled;
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
	 * @return the messageReceive
	 */
	public WechatMessageReceive getMessageReceive() {
		return messageReceive;
	}
	/**
	 * @param messageReceive the messageReceive to set
	 */
	public void setMessageReceive(WechatMessageReceive messageReceive) {
		this.messageReceive = messageReceive;
	}
	/**
	 * @return the showCoverPic
	 */
	public int getShowCoverPic() {
		return showCoverPic;
	}
	/**
	 * @param showCoverPic the showCoverPic to set
	 */
	public void setShowCoverPic(int showCoverPic) {
		this.showCoverPic = showCoverPic;
	}
}