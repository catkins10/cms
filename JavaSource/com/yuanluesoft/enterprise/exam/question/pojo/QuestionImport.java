package com.yuanluesoft.enterprise.exam.question.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 试题:导入日志(exam_question_import)
 * @author linchuan
 *
 */
public class QuestionImport extends Record {
	private String source; //题库来源,如:南昌铁路局
	private String description; //说明,从文件名提取
	private String postIds; //适用的岗位ID
	private String posts; //适用的岗位
	private String knowledgeIds; //知识类别ID
	private String knowledges; //知识类别
	private String itemIds; //项目分类ID
	private String items; //项目分类
	private String creator; //创建人
	private long creatorId; //创建人ID
	private Timestamp created; //创建时间
	private String remark; //备注
	
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
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
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
	 * @return the itemIds
	 */
	public String getItemIds() {
		return itemIds;
	}
	/**
	 * @param itemIds the itemIds to set
	 */
	public void setItemIds(String itemIds) {
		this.itemIds = itemIds;
	}
	/**
	 * @return the items
	 */
	public String getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(String items) {
		this.items = items;
	}
	/**
	 * @return the knowledgeIds
	 */
	public String getKnowledgeIds() {
		return knowledgeIds;
	}
	/**
	 * @param knowledgeIds the knowledgeIds to set
	 */
	public void setKnowledgeIds(String knowledgeIds) {
		this.knowledgeIds = knowledgeIds;
	}
	/**
	 * @return the knowledges
	 */
	public String getKnowledges() {
		return knowledges;
	}
	/**
	 * @param knowledges the knowledges to set
	 */
	public void setKnowledges(String knowledges) {
		this.knowledges = knowledges;
	}
	/**
	 * @return the postIds
	 */
	public String getPostIds() {
		return postIds;
	}
	/**
	 * @param postIds the postIds to set
	 */
	public void setPostIds(String postIds) {
		this.postIds = postIds;
	}
	/**
	 * @return the posts
	 */
	public String getPosts() {
		return posts;
	}
	/**
	 * @param posts the posts to set
	 */
	public void setPosts(String posts) {
		this.posts = posts;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
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
}