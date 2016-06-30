package com.yuanluesoft.cms.onlineservice.faq.forms.admin;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

/**
 * 
 * @author linchuan
 *
 */
public class Faq extends WorkflowForm {
	private String question; //问题
	private String questionSpell; //问题的拼音,只保留50个字母
	private String answer; //解答
	private String creator; //创建人
	private long creatorId; //创建人ID
	private Timestamp created; //创建时间
	private Timestamp issueTime; //发布时间
	private long issuePersonId; //发布人ID
	private char status = '1'; //状态,0/撤销发布,1/待处理,2/退回、取回修改,3/已发布,4/办结未发布,5/已删除
	private int askTimes; //被提问次数
	private Set subjections; //隶属目录列表
	private Set subjectionItems; //隶属项目列表
	private Set relations; //相关问题
	private Set beRelations; //被关联问题
	private Set accessStats; //访问统计
	
	//扩展属性
	private long directoryId; //所属目录ID
	private String directoryName; //所属目录名称
	private String OtherDirectoryIds; //所属的其他目录ID
	private String OtherDirectoryNames; //所属的其他目录名称
	private String itemIds; //隶属项目ID列表
	private String itemNames; //隶属项目名称列表
	private String sourceRecordClass; //源记录类名称,把投诉或者咨询设为常见问题时有效
	private long sourceRecordId; //源记录类ID,把投诉或者咨询设为常见问题时有效
	private List relationFaqs; //关联问题列表
	private String newRelationFaqIds; //新增的相关问题ID
	
	/**
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}
	/**
	 * @param answer the answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
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
	 * @return the directoryId
	 */
	public long getDirectoryId() {
		return directoryId;
	}
	/**
	 * @param directoryId the directoryId to set
	 */
	public void setDirectoryId(long directoryId) {
		this.directoryId = directoryId;
	}
	/**
	 * @return the directoryName
	 */
	public String getDirectoryName() {
		return directoryName;
	}
	/**
	 * @param directoryName the directoryName to set
	 */
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
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
	 * @return the itemNames
	 */
	public String getItemNames() {
		return itemNames;
	}
	/**
	 * @param itemNames the itemNames to set
	 */
	public void setItemNames(String itemNames) {
		this.itemNames = itemNames;
	}
	/**
	 * @return the otherDirectoryIds
	 */
	public String getOtherDirectoryIds() {
		return OtherDirectoryIds;
	}
	/**
	 * @param otherDirectoryIds the otherDirectoryIds to set
	 */
	public void setOtherDirectoryIds(String otherDirectoryIds) {
		OtherDirectoryIds = otherDirectoryIds;
	}
	/**
	 * @return the otherDirectoryNames
	 */
	public String getOtherDirectoryNames() {
		return OtherDirectoryNames;
	}
	/**
	 * @param otherDirectoryNames the otherDirectoryNames to set
	 */
	public void setOtherDirectoryNames(String otherDirectoryNames) {
		OtherDirectoryNames = otherDirectoryNames;
	}
	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}
	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	/**
	 * @return the subjectionItems
	 */
	public Set getSubjectionItems() {
		return subjectionItems;
	}
	/**
	 * @param subjectionItems the subjectionItems to set
	 */
	public void setSubjectionItems(Set subjectionItems) {
		this.subjectionItems = subjectionItems;
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
	 * @return the accessStats
	 */
	public Set getAccessStats() {
		return accessStats;
	}
	/**
	 * @param accessStats the accessStats to set
	 */
	public void setAccessStats(Set accessStats) {
		this.accessStats = accessStats;
	}
	/**
	 * @return the sourceRecordClass
	 */
	public String getSourceRecordClass() {
		return sourceRecordClass;
	}
	/**
	 * @param sourceRecordClass the sourceRecordClass to set
	 */
	public void setSourceRecordClass(String sourceRecordClass) {
		this.sourceRecordClass = sourceRecordClass;
	}
	/**
	 * @return the sourceRecordId
	 */
	public long getSourceRecordId() {
		return sourceRecordId;
	}
	/**
	 * @param sourceRecordId the sourceRecordId to set
	 */
	public void setSourceRecordId(long sourceRecordId) {
		this.sourceRecordId = sourceRecordId;
	}
	/**
	 * @return the status
	 */
	public char getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(char status) {
		this.status = status;
	}
	/**
	 * @return the issuePersonId
	 */
	public long getIssuePersonId() {
		return issuePersonId;
	}
	/**
	 * @param issuePersonId the issuePersonId to set
	 */
	public void setIssuePersonId(long issuePersonId) {
		this.issuePersonId = issuePersonId;
	}
	/**
	 * @return the issueTime
	 */
	public Timestamp getIssueTime() {
		return issueTime;
	}
	/**
	 * @param issueTime the issueTime to set
	 */
	public void setIssueTime(Timestamp issueTime) {
		this.issueTime = issueTime;
	}
	/**
	 * @return the askTimes
	 */
	public int getAskTimes() {
		return askTimes;
	}
	/**
	 * @param askTimes the askTimes to set
	 */
	public void setAskTimes(int askTimes) {
		this.askTimes = askTimes;
	}
	/**
	 * @return the relations
	 */
	public Set getRelations() {
		return relations;
	}
	/**
	 * @param relations the relations to set
	 */
	public void setRelations(Set relations) {
		this.relations = relations;
	}
	/**
	 * @return the beRelations
	 */
	public Set getBeRelations() {
		return beRelations;
	}
	/**
	 * @param beRelations the beRelations to set
	 */
	public void setBeRelations(Set beRelations) {
		this.beRelations = beRelations;
	}
	/**
	 * @return the relationFaqs
	 */
	public List getRelationFaqs() {
		return relationFaqs;
	}
	/**
	 * @param relationFaqs the relationFaqs to set
	 */
	public void setRelationFaqs(List relationFaqs) {
		this.relationFaqs = relationFaqs;
	}
	/**
	 * @return the newRelationFaqIds
	 */
	public String getNewRelationFaqIds() {
		return newRelationFaqIds;
	}
	/**
	 * @param newRelationFaqIds the newRelationFaqIds to set
	 */
	public void setNewRelationFaqIds(String newRelationFaqIds) {
		this.newRelationFaqIds = newRelationFaqIds;
	}
	/**
	 * @return the questionSpell
	 */
	public String getQuestionSpell() {
		return questionSpell;
	}
	/**
	 * @param questionSpell the questionSpell to set
	 */
	public void setQuestionSpell(String questionSpell) {
		this.questionSpell = questionSpell;
	}
}