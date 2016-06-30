package com.yuanluesoft.j2oa.exchange.forms.admin;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.j2oa.exchange.pojo.ExchangeDocument;
import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 消息反馈
 * @author linchuan
 *
 */
public class ExchangeMessage extends ActionForm {
	private long documentId; //关联的公文ID,从已签收的公文中发起
	private long replyMessageId; //被答复的消息ID
	private String subject; //标题
	private String body; //正文
	private Timestamp created; //创建时间
	private String creator; //创建人
	private long creatorId; //创建人ID
	private String creatorUnit; //发布人所在单位
	private long creatorUnitId; //发布人所在单位ID
	private ExchangeDocument document = new ExchangeDocument(); //公文
	private Set replies; //答复
	
	//扩展属性
	private com.yuanluesoft.j2oa.exchange.pojo.ExchangeMessage reply; //答复
	
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
	 * @return the creatorUnit
	 */
	public String getCreatorUnit() {
		return creatorUnit;
	}
	/**
	 * @param creatorUnit the creatorUnit to set
	 */
	public void setCreatorUnit(String creatorUnit) {
		this.creatorUnit = creatorUnit;
	}
	/**
	 * @return the creatorUnitId
	 */
	public long getCreatorUnitId() {
		return creatorUnitId;
	}
	/**
	 * @param creatorUnitId the creatorUnitId to set
	 */
	public void setCreatorUnitId(long creatorUnitId) {
		this.creatorUnitId = creatorUnitId;
	}
	/**
	 * @return the documentId
	 */
	public long getDocumentId() {
		return documentId;
	}
	/**
	 * @param documentId the documentId to set
	 */
	public void setDocumentId(long documentId) {
		this.documentId = documentId;
	}
	/**
	 * @return the replyMessageId
	 */
	public long getReplyMessageId() {
		return replyMessageId;
	}
	/**
	 * @param replyMessageId the replyMessageId to set
	 */
	public void setReplyMessageId(long replyMessageId) {
		this.replyMessageId = replyMessageId;
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
	 * @return the document
	 */
	public ExchangeDocument getDocument() {
		return document;
	}
	/**
	 * @param document the document to set
	 */
	public void setDocument(ExchangeDocument document) {
		this.document = document;
	}
	/**
	 * @return the reply
	 */
	public com.yuanluesoft.j2oa.exchange.pojo.ExchangeMessage getReply() {
		return reply;
	}
	/**
	 * @param reply the reply to set
	 */
	public void setReply(com.yuanluesoft.j2oa.exchange.pojo.ExchangeMessage reply) {
		this.reply = reply;
	}
	/**
	 * @return the replies
	 */
	public Set getReplies() {
		return replies;
	}
	/**
	 * @param replies the replies to set
	 */
	public void setReplies(Set replies) {
		this.replies = replies;
	}
}