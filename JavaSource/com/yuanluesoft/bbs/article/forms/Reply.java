package com.yuanluesoft.bbs.article.forms;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.bbs.article.pojo.BbsArticle;
import com.yuanluesoft.bbs.forum.pojo.Forum;
import com.yuanluesoft.cms.base.forms.SiteActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Reply extends SiteActionForm {
	private long id; //ID
	private long articleId; //帖子ID
	private long replyId; //被回复的回复ID,0表示是直接对文章的回复
	private String subject; //主题
	private long creatorId; //回复人ID
	private String creatorNickname; //回复人昵称
	private Timestamp created; //回复时间
	private char isDeleted = '0'; //是否被管理员删除
	private String deleteReason; //删除原因
	private Set author;
	private String body;
	
	private BbsArticle article;
	private Forum forum;
	//验证码
	private String validateCode;

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
	 * @return the author
	 */
	public Set getAuthor() {
		return author;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(Set author) {
		this.author = author;
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
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
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
	/**
	 * @return the article
	 */
	public BbsArticle getArticle() {
		return article;
	}
	/**
	 * @param article the article to set
	 */
	public void setArticle(BbsArticle article) {
		this.article = article;
	}
	/**
	 * @return the forum
	 */
	public Forum getForum() {
		return forum;
	}
	/**
	 * @param forum the forum to set
	 */
	public void setForum(Forum forum) {
		this.forum = forum;
	}


}