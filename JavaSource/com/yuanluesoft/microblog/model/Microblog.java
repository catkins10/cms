package com.yuanluesoft.microblog.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.yuanluesoft.jeaf.gps.model.Geo;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 微博
 * @author linchuan
 *
 */
public class Microblog implements Serializable, Cloneable {
	private String id; //微博ID 
	private String content; //微博信息内容 
	private String source; //微博来源 
	private Timestamp created; //微博创建时间 
	private Geo geo; //地理信息字段 详细 
	private MicroblogUser user; //用户
	private Microblog retweetedMicroblog; //被转发的原微博信息字段，当该微博为转发微博时返回 
	private int repostsCount; //转发数 
	private int commentsCount; //评论数 
	private int attitudesCount; //表态数 
	private String visible; //微博的可见性及指定可见分组信息,all/普通微博，private/私密微博，groups/指定分组微博 
	private List images; //微博配图(MicroblogImage)列表
	private String url; //链接
	private String repostUrl; //转发链接 http://weibo.com/1900372975/B61B0FfFQ?type=repost
	private String commentUrl; //评论链接 http://weibo.com/1900372975/B61B0FfFQ?type=comment
	
	/**
	 * 获取转发的原微博信息列表,显示微博时使用
	 * @return
	 */
	public List getRetweetedMicroblogs() {
		return retweetedMicroblog==null ? null : ListUtils.generateList(retweetedMicroblog);
	}
	
	/**
	 * 获取转发数
	 * @return
	 */
	public String getRepostsCountAsText() {
		return "转发(" + repostsCount + ")";
	}
	
	/**
	 * 获取评论数
	 * @return
	 */
	public String getCommentsCountAsText() {
		return "评论(" + commentsCount + ")";
	}
	
	/**
	 * @return the attitudesCount
	 */
	public int getAttitudesCount() {
		return attitudesCount;
	}
	/**
	 * @param attitudesCount the attitudesCount to set
	 */
	public void setAttitudesCount(int attitudesCount) {
		this.attitudesCount = attitudesCount;
	}
	/**
	 * @return the commentsCount
	 */
	public int getCommentsCount() {
		return commentsCount;
	}
	/**
	 * @param commentsCount the commentsCount to set
	 */
	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
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
	 * @return the geo
	 */
	public Geo getGeo() {
		return geo;
	}
	/**
	 * @param geo the geo to set
	 */
	public void setGeo(Geo geo) {
		this.geo = geo;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the retweetedMicroblog
	 */
	public Microblog getRetweetedMicroblog() {
		return retweetedMicroblog;
	}
	/**
	 * @param retweetedMicroblog the retweetedMicroblog to set
	 */
	public void setRetweetedMicroblog(Microblog retweetedMicroblog) {
		this.retweetedMicroblog = retweetedMicroblog;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the user
	 */
	public MicroblogUser getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(MicroblogUser user) {
		this.user = user;
	}
	/**
	 * @return the visible
	 */
	public String getVisible() {
		return visible;
	}
	/**
	 * @param visible the visible to set
	 */
	public void setVisible(String visible) {
		this.visible = visible;
	}
	/**
	 * @return the repostsCount
	 */
	public int getRepostsCount() {
		return repostsCount;
	}
	/**
	 * @param repostsCount the repostsCount to set
	 */
	public void setRepostsCount(int repostsCount) {
		this.repostsCount = repostsCount;
	}
	/**
	 * @return the images
	 */
	public List getImages() {
		return images;
	}
	/**
	 * @param images the images to set
	 */
	public void setImages(List images) {
		this.images = images;
	}
	/**
	 * @return the commentUrl
	 */
	public String getCommentUrl() {
		return "{FINAL}" + commentUrl;
	}
	/**
	 * @param commentUrl the commentUrl to set
	 */
	public void setCommentUrl(String commentUrl) {
		this.commentUrl = commentUrl;
	}
	/**
	 * @return the repostUrl
	 */
	public String getRepostUrl() {
		return "{FINAL}" + repostUrl;
	}
	/**
	 * @param repostUrl the repostUrl to set
	 */
	public void setRepostUrl(String repostUrl) {
		this.repostUrl = repostUrl;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}