package com.yuanluesoft.wechat.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

/**
 * 微信:消息发送(wechat_message_send)
 * @author linchuan
 *
 */
public class WechatMessageSend extends WorkflowData {
	private long unitId; //单位ID
	private String type; //消息类型,text/image/voice/video/news
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
	
	/**
	 * 获取主题
	 * @return
	 */
	public String getSubject() {
		String subject = null;
		if("video".equals(type)) { //视频
			subject = title;
		}
		else if("text".equals(type)) { //文字
			subject = content;
		}
		else if("news".equals(type)) { //图文消息
			subject = ListUtils.join(news, "title",  " ", false);
		}
		else if("voice".equals(type)) {
			subject = "语音消息";
		}
		else if("image".equals(type)) {
			subject = "图片消息";
		}
		return subject==null || subject.isEmpty() ? "无标题" : subject;
	}
	
	/**
	 * 获取HTML格式的正文,用于对外显示
	 * @return
	 */
	public String getHtmlContent() {
		String html = null;
		if("video".equals(type)) { //视频
			//TODO 生成播放页面
		}
		else if("text".equals(type)) { //文字
			html = StringUtils.escape(content);
		}
		else if("news".equals(type)) { //图文消息
			//TODO: jsp内容改在这里生成
		}
		else if("voice".equals(type)) {
			//TODO 生成播放页面
		}
		else if("image".equals(type)) {
			//TODO 显示图片
		}
		return html;
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