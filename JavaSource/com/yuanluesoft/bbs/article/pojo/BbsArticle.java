package com.yuanluesoft.bbs.article.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.bbs.usermanage.model.BbsUser;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.LazyBodyUtils;

/**
 * 论坛:帖子(bbs_article)
 * @author linchuan
 *
 */
public class BbsArticle extends Record {
	private String subject; //主题
	private long creatorId; //发帖人ID
	private String creatorNickname; //发帖人昵称
	private Timestamp created; //发帖时间
	private float priority; //优先级
	private char quint = '0'; //是否精华贴
	private char top = '0'; //是否置顶
	private char systemMessage; //是否系统公告
	private int accessTimes; //访问次数
	private char isDeleted = '0'; //是否被管理员删除
	private String deleteReason; //删除原因
	private Set subjections; //隶属列表
	private Set lazyBody; //正文
	private Set replies; //回帖列表

	//以下由程序设置
	private BbsUser author;
	private int replyCount; //回帖数
	private BbsReply lastReply; //最后回复
	private boolean newReply; //是否有新回复(一天内的回复)

	/**
	 * 获取正文
	 */
	public String getBody() {
		return LazyBodyUtils.getBody(this);
	}
	
	/**
	 * 设置正文
	 * @param body
	 */
	public void setBody(String body) {
		LazyBodyUtils.setBody(this, body);
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
	 * @return the creatorNickname
	 */
	public String getCreatorNickname() {
		return creatorNickname;
	}
	/**
	 * @param creatorNickname the creatorNickname to set
	 */
	public void setCreatorNickname(String creatorNickname) {
		this.creatorNickname = creatorNickname;
	}
	/**
	 * @return the priority
	 */
	public float getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(float priority) {
		this.priority = priority;
	}
	/**
	 * @return the quint
	 */
	public char getQuint() {
		return quint;
	}
	/**
	 * @param quint the quint to set
	 */
	public void setQuint(char quint) {
		this.quint = quint;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the accessTimes
	 */
	public int getAccessTimes() {
		return accessTimes;
	}
	/**
	 * @param accessTimes the accessTimes to set
	 */
	public void setAccessTimes(int accessTimes) {
		this.accessTimes = accessTimes;
	}
	/**
	 * @return the deleteReason
	 */
	public String getDeleteReason() {
		return deleteReason;
	}
	/**
	 * @param deleteReason the deleteReason to set
	 */
	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}
	/**
	 * @return the isDeleted
	 */
	public char getIsDeleted() {
		return isDeleted;
	}
	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(char isDeleted) {
		this.isDeleted = isDeleted;
	}
	/**
	 * @return the systemMessage
	 */
	public char getSystemMessage() {
		return systemMessage;
	}
	/**
	 * @param systemMessage the systemMessage to set
	 */
	public void setSystemMessage(char systemMessage) {
		this.systemMessage = systemMessage;
	}
	/**
	 * @return the top
	 */
	public char getTop() {
		return top;
	}
	/**
	 * @param top the top to set
	 */
	public void setTop(char top) {
		this.top = top;
	}
	/**
	 * @return the subjections
	 */
	public Set getSubjections() {
		return subjections;
	}
	/**
	 * @param subjections the subjections to set
	 */
	public void setSubjections(Set subjections) {
		this.subjections = subjections;
	}
	/**
	 * @return the lazyBody
	 */
	public Set getLazyBody() {
		return lazyBody;
	}
	/**
	 * @param lazyBody the lazyBody to set
	 */
	public void setLazyBody(Set lazyBody) {
		this.lazyBody = lazyBody;
	}

	/**
	 * @return the author
	 */
	public BbsUser getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(BbsUser author) {
		this.author = author;
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

	/**
	 * @return the lastReply
	 */
	public BbsReply getLastReply() {
		return lastReply;
	}

	/**
	 * @param lastReply the lastReply to set
	 */
	public void setLastReply(BbsReply lastReply) {
		this.lastReply = lastReply;
	}

	/**
	 * @return the newReply
	 */
	public boolean isNewReply() {
		return newReply;
	}

	/**
	 * @param newReply the newReply to set
	 */
	public void setNewReply(boolean newReply) {
		this.newReply = newReply;
	}

	/**
	 * @return the replies
	 */
	public Set getReplies() {
		return replies;
	}

	/**
	 * @param replies the replies to set
	 */
	public void setReplies(Set replies) {
		this.replies = replies;
	}
}
