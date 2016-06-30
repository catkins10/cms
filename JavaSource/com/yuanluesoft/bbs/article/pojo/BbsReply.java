package com.yuanluesoft.bbs.article.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.bbs.usermanage.model.BbsUser;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.LazyBodyUtils;

/**
 * 论坛:回复(bbs_reply)
 * @author linchuan
 *
 */
public class BbsReply extends Record {
	private long articleId; //帖子ID
	private long replyId; //被回复的回复ID,0表示是直接对文章的回复
	private String subject; //主题
	private long creatorId; //回复人ID
	private String creatorNickname; //回复人昵称
	private Timestamp created; //回复时间
	private char isDeleted = '0'; //是否被管理员删除
	private String deleteReason; //删除原因
	private Set lazyBody;

	private BbsUser author;

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
	 * @return the articleId
	 */
	public long getArticleId() {
		return articleId;
	}
	/**
	 * @param articleId the articleId to set
	 */
	public void setArticleId(long articleId) {
		this.articleId = articleId;
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
	 * @return the replyId
	 */
	public long getReplyId() {
		return replyId;
	}
	/**
	 * @param replyId the replyId to set
	 */
	public void setReplyId(long replyId) {
		this.replyId = replyId;
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

}
