package com.yuanluesoft.bbs.article.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 论坛:帖子所属版块(bbs_article_subjection)
 * @author linchuan
 *
 */
public class BbsArticleSubjection extends Record {
	private long articleId; //帖子ID
	private long forumId; //版块ID
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
}
