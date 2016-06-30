package com.yuanluesoft.bbs.forum.forms.admin;


/**
 * 
 * @author yuanluesoft
 *
 */
public class Forum extends Directory {
	private int pageArticles; //论坛每页显示主题数
	private int pageReplies; //每贴显示的回复数
	private char anonymousEnabled = '0'; //是否允许匿名访问
	private char anonymousReply = '0'; //是否允许匿名回复
	private char anonymousCreate = '0'; //是否允许匿名发帖
	private char vipOnly = '0'; //是否只允许VIP访问
	private char anonymousDownload = '0'; //是否允许匿名用户下载附件
	private char managerCreateOnly = '0'; //仅允许版主发帖
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
	 * @return the managerCreateOnly
	 */
	public char getManagerCreateOnly() {
		return managerCreateOnly;
	}
	/**
	 * @param managerCreateOnly the managerCreateOnly to set
	 */
	public void setManagerCreateOnly(char managerCreateOnly) {
		this.managerCreateOnly = managerCreateOnly;
	}
	/**
	 * @return the anonymousCreate
	 */
	public char getAnonymousCreate() {
		return anonymousCreate;
	}
	/**
	 * @param anonymousCreate the anonymousCreate to set
	 */
	public void setAnonymousCreate(char anonymousCreate) {
		this.anonymousCreate = anonymousCreate;
	}
	/**
	 * @return the anonymousReply
	 */
	public char getAnonymousReply() {
		return anonymousReply;
	}
	/**
	 * @param anonymousReply the anonymousReply to set
	 */
	public void setAnonymousReply(char anonymousReply) {
		this.anonymousReply = anonymousReply;
	}
}
