package com.yuanluesoft.microblog.forms;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;
import com.yuanluesoft.microblog.pojo.MicroblogPrivateMessage;

/**
 * 
 * @author linchuan
 *
 */
public class Microblog extends WorkflowForm {
	private long unitId; //单位ID
	private String accountIds; //帐号ID
	private String range; //发布范围,微博的可见性，all/所有人能看，self/仅自己可见，groups/指定分组可见，默认为all
	private String groupNames; //分组名称
	private String groupIds; //分组ID
	private long creatorId; //创建人ID
	private String creator; //创建人
	private Timestamp created; //创建时间
	private Timestamp sendTime; //发送时间
	private String content; //微博内容
	private String blogIds; //微博ID,微博在各平台上的ID,用于检查转发、评论情况
	private long privateMessageId; //私信ID,答复私信时有效
	
	//扩展属性
	private MicroblogPrivateMessage privateMessage; //私信
	private String referenceUrl; //引用记录的链接
	private String referenceTitle; //引用记录的标题
	private int shortUrlMaxLength; //短链接最大长度
	
	/**
	 * 获取微博帐号ID列表
	 * @return
	 */
	public String[] getAccountIdArray() {
		return accountIds==null || accountIds.isEmpty() ? null : accountIds.split(",");
	}
	
	/**
	 * 设置微博帐号ID列表
	 * @param accountIdArray
	 */
	public void setAccountIdArray(String[] accountIdArray) {
		accountIds = accountIdArray==null || accountIdArray.length==0 ? null : ListUtils.join(accountIdArray, ",", false);
	}
	
	/**
	 * @return the accountIds
	 */
	public String getAccountIds() {
		return accountIds;
	}
	/**
	 * @param accountIds the accountIds to set
	 */
	public void setAccountIds(String accountIds) {
		this.accountIds = accountIds;
	}
	/**
	 * @return the blogIds
	 */
	public String getBlogIds() {
		return blogIds;
	}
	/**
	 * @param blogIds the blogIds to set
	 */
	public void setBlogIds(String blogIds) {
		this.blogIds = blogIds;
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
	 * @return the privateMessageId
	 */
	public long getPrivateMessageId() {
		return privateMessageId;
	}
	/**
	 * @param privateMessageId the privateMessageId to set
	 */
	public void setPrivateMessageId(long privateMessageId) {
		this.privateMessageId = privateMessageId;
	}
	/**
	 * @return the range
	 */
	public String getRange() {
		return range;
	}
	/**
	 * @param range the range to set
	 */
	public void setRange(String range) {
		this.range = range;
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
	 * @return the privateMessage
	 */
	public MicroblogPrivateMessage getPrivateMessage() {
		return privateMessage;
	}
	/**
	 * @param privateMessage the privateMessage to set
	 */
	public void setPrivateMessage(MicroblogPrivateMessage privateMessage) {
		this.privateMessage = privateMessage;
	}

	/**
	 * @return the referenceUrl
	 */
	public String getReferenceUrl() {
		return referenceUrl;
	}

	/**
	 * @param referenceUrl the referenceUrl to set
	 */
	public void setReferenceUrl(String referenceUrl) {
		this.referenceUrl = referenceUrl;
	}

	/**
	 * @return the referenceTitle
	 */
	public String getReferenceTitle() {
		return referenceTitle;
	}

	/**
	 * @param referenceTitle the referenceTitle to set
	 */
	public void setReferenceTitle(String referenceTitle) {
		this.referenceTitle = referenceTitle;
	}

	/**
	 * @return the shortUrlMaxLength
	 */
	public int getShortUrlMaxLength() {
		return shortUrlMaxLength;
	}

	/**
	 * @param shortUrlMaxLength the shortUrlMaxLength to set
	 */
	public void setShortUrlMaxLength(int shortUrlMaxLength) {
		this.shortUrlMaxLength = shortUrlMaxLength;
	}
}