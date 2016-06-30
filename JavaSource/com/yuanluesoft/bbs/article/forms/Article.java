package com.yuanluesoft.bbs.article.forms;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.cms.base.forms.SiteActionForm;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Article extends SiteActionForm {
	private String subject; //主题
	private String body; //正文
	private long creatorId; //发帖人ID
	private String creatorNickname; //发帖人昵称
	private Timestamp created; //发帖时间
	private float priority; //优先级
	private char quint = '0'; //是否精华贴
	private char top = '0'; //是否置顶
	private char systemMessage = '0'; //是否系统公告
	private int accessTimes; //访问次数
	private long lastReplyId; //最后的回复ID
	private char isDeleted = '0'; //是否被管理员删除
	private String deleteReason; //删除原因
	private Set subjections; //隶属列表

	private long forumId; //论坛ID

	//验证码
	private String validateCode;
	
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
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
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
	 * @return the lastReplyId
	 */
	public long getLastReplyId() {
		return lastReplyId;
	}
	/**
	 * @param lastReplyId the lastReplyId to set
	 */
	public void setLastReplyId(long lastReplyId) {
		this.lastReplyId = lastReplyId;
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
	 * @return the forumId
	 */
	public long getForumId() {
		return forumId;
	}
	/**
	 * @param forumId the forumId to set
	 */
	public void setForumId(long forumId) {
		this.forumId = forumId;
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
	 * @return the validateCode
	 */
	public String getValidateCode() {
		return validateCode;
	}
	/**
	 * @param validateCode the validateCode to set
	 */
	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
}