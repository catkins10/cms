package com.yuanluesoft.bbs.article.forms;

import com.yuanluesoft.bbs.article.pojo.BbsArticle;
import com.yuanluesoft.bbs.base.forms.BbsViewForm;
import com.yuanluesoft.bbs.forum.pojo.Forum;

/**
 * 
 * @author yuanluesoft
 *
 */
public class ViewArticle extends BbsViewForm {
	private Forum forum; //所在论坛版块
	private BbsArticle article; //主题
	private BbsArticle previousArticle; //上一主题
	private BbsArticle nextArticle; //下一主题
	private boolean isManager; //是不是版主

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

	/**
	 * @return the nextArticle
	 */
	public BbsArticle getNextArticle() {
		return nextArticle;
	}

	/**
	 * @param nextArticle the nextArticle to set
	 */
	public void setNextArticle(BbsArticle nextArticle) {
		this.nextArticle = nextArticle;
	}

	/**
	 * @return the previousArticle
	 */
	public BbsArticle getPreviousArticle() {
		return previousArticle;
	}

	/**
	 * @param previousArticle the previousArticle to set
	 */
	public void setPreviousArticle(BbsArticle previousArticle) {
		this.previousArticle = previousArticle;
	}

	/**
	 * @return the isManager
	 */
	public boolean isManager() {
		return isManager;
	}

	/**
	 * @param isManager the isManager to set
	 */
	public void setManager(boolean isManager) {
		this.isManager = isManager;
	}
	
}