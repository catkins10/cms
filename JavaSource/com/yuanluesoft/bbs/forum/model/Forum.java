package com.yuanluesoft.bbs.forum.model;

import java.util.List;

import com.yuanluesoft.bbs.article.pojo.BbsArticle;
import com.yuanluesoft.bbs.article.pojo.BbsReply;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Forum {
	private long id;
	private String name; //版块名称
	private String description; //描述
	private int pageArticles; //论坛每页显示主题数
	private int pageReplies; //每贴显示的回复数
	private char anonymousEnabled = '0'; //是否允许匿名访问
	private char vipOnly = '0'; //是否只允许VIP访问
	private char anonymousDownload = '0'; //是否允许匿名用户下载附件
	private List managers; //版主列表
	private int articleTotal; //文章总数
	private int replyTotal; //回复总数
	private int articleToday; //今天的文章总数
	private int replyToday; //今天的回复总数
	private BbsArticle lastArticle; //最后发表的文章
	private BbsReply lastReply; //最后发表的回复
	private List childForums; //子论坛列表,仅第一级
	
	/**
	 * @return the articleToday
	 */
	public int getArticleToday() {
		return articleToday;
	}
	/**
	 * @param articleToday the articleToday to set
	 */
	public void setArticleToday(int articleToday) {
		this.articleToday = articleToday;
	}
	/**
	 * @return the articleTotal
	 */
	public int getArticleTotal() {
		return articleTotal;
	}
	/**
	 * @param articleTotal the articleTotal to set
	 */
	public void setArticleTotal(int articleTotal) {
		this.articleTotal = articleTotal;
	}
	/**
	 * @return the lastArticle
	 */
	public BbsArticle getLastArticle() {
		return lastArticle;
	}
	/**
	 * @param lastArticle the lastArticle to set
	 */
	public void setLastArticle(BbsArticle lastArticle) {
		this.lastArticle = lastArticle;
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
	 * @return the managers
	 */
	public List getManagers() {
		return managers;
	}
	/**
	 * @param managers the managers to set
	 */
	public void setManagers(List managers) {
		this.managers = managers;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the replyToday
	 */
	public int getReplyToday() {
		return replyToday;
	}
	/**
	 * @param replyToday the replyToday to set
	 */
	public void setReplyToday(int replyToday) {
		this.replyToday = replyToday;
	}
	/**
	 * @return the replyTotal
	 */
	public int getReplyTotal() {
		return replyTotal;
	}
	/**
	 * @param replyTotal the replyTotal to set
	 */
	public void setReplyTotal(int replyTotal) {
		this.replyTotal = replyTotal;
	}
	/**
	 * @return the childForums
	 */
	public List getChildForums() {
		return childForums;
	}
	/**
	 * @param childForums the childForums to set
	 */
	public void setChildForums(List childForums) {
		this.childForums = childForums;
	}
	/**
	 * @return the anonymousDownload
	 */
	public char getAnonymousDownload() {
		return anonymousDownload;
	}
	/**
	 * @param anonymousDownload the anonymousDownload to set
	 */
	public void setAnonymousDownload(char anonymousDownload) {
		this.anonymousDownload = anonymousDownload;
	}
	/**
	 * @return the anonymousEnabled
	 */
	public char getAnonymousEnabled() {
		return anonymousEnabled;
	}
	/**
	 * @param anonymousEnabled the anonymousEnabled to set
	 */
	public void setAnonymousEnabled(char anonymousEnabled) {
		this.anonymousEnabled = anonymousEnabled;
	}
	/**
	 * @return the pageArticles
	 */
	public int getPageArticles() {
		return pageArticles;
	}
	/**
	 * @param pageArticles the pageArticles to set
	 */
	public void setPageArticles(int pageArticles) {
		this.pageArticles = pageArticles;
	}
	/**
	 * @return the pageReplies
	 */
	public int getPageReplies() {
		return pageReplies;
	}
	/**
	 * @param pageReplies the pageReplies to set
	 */
	public void setPageReplies(int pageReplies) {
		this.pageReplies = pageReplies;
	}
	/**
	 * @return the vipOnly
	 */
	public char getVipOnly() {
		return vipOnly;
	}
	/**
	 * @param vipOnly the vipOnly to set
	 */
	public void setVipOnly(char vipOnly) {
		this.vipOnly = vipOnly;
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
	
}
