package com.yuanluesoft.bbs.forum.model;

import java.util.List;

import com.yuanluesoft.jeaf.usermanage.member.pojo.Member;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Bbs {
	private long id;
	private String name; //论坛名称
	private String description; //描述
	private List managers; //版主列表
	private List categories; //版块分类列表,仅一层,超过一层的自动加入到第一层中
	private int articleTotal; //文章总数
	private int replyTotal; //回复总数
	private int articleToday; //今天的文章总数
	private int replyToday; //今天的回复总数
	private int articleYesterday; //昨天的文章总数
	private int replyYesterday; //昨天的回复总数
	private int memberTotal; //用户总数
	private Member lastMember; //最后注册的用户
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
	 * @return the categories
	 */
	public List getCategories() {
		return categories;
	}
	/**
	 * @param categories the categories to set
	 */
	public void setCategories(List categories) {
		this.categories = categories;
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
	 * @return the articleYesterday
	 */
	public int getArticleYesterday() {
		return articleYesterday;
	}
	/**
	 * @param articleYesterday the articleYesterday to set
	 */
	public void setArticleYesterday(int articleYesterday) {
		this.articleYesterday = articleYesterday;
	}
	/**
	 * @return the replyYesterday
	 */
	public int getReplyYesterday() {
		return replyYesterday;
	}
	/**
	 * @param replyYesterday the replyYesterday to set
	 */
	public void setReplyYesterday(int replyYesterday) {
		this.replyYesterday = replyYesterday;
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
	/**
	 * @return the lastMember
	 */
	public Member getLastMember() {
		return lastMember;
	}
	/**
	 * @param lastMember the lastMember to set
	 */
	public void setLastMember(Member lastMember) {
		this.lastMember = lastMember;
	}
	/**
	 * @return the memberTotal
	 */
	public int getMemberTotal() {
		return memberTotal;
	}
	/**
	 * @param memberTotal the memberTotal to set
	 */
	public void setMemberTotal(int memberTotal) {
		this.memberTotal = memberTotal;
	}
}
