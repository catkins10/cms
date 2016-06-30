/*
 * Created on 2006-4-28
 *
 */
package com.yuanluesoft.j2oa.databank.pojo;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 资料库:资料(databank_data)
 * @author linchuan
 *
 */
public class DatabankData extends Record {
	private String subject; //标题
	private String docmark; //文件字号
	private String dataType; //文件类型
	private String secureLevel; //密级
	private String fromUnit; //成文单位
	private Date generateDate; //成文日期
	private long creatorId; //创建人ID
	private String creator; //创建人
	private Timestamp created; //登记时间
	private long directoryId; //目录ID
	private String remark; //附注
	private String body; //正文
	
	private Set visitors; //资料自定义的访问者列表
	
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
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}
	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
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
	 * @return the docmark
	 */
	public String getDocmark() {
		return docmark;
	}
	/**
	 * @param docmark the docmark to set
	 */
	public void setDocmark(String docmark) {
		this.docmark = docmark;
	}
	/**
	 * @return the fromUnit
	 */
	public String getFromUnit() {
		return fromUnit;
	}
	/**
	 * @param fromUnit the fromUnit to set
	 */
	public void setFromUnit(String fromUnit) {
		this.fromUnit = fromUnit;
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
	 * @return the secureLevel
	 */
	public String getSecureLevel() {
		return secureLevel;
	}
	/**
	 * @param secureLevel the secureLevel to set
	 */
	public void setSecureLevel(String secureLevel) {
		this.secureLevel = secureLevel;
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
	 * @return the visitors
	 */
	public Set getVisitors() {
		return visitors;
	}
	/**
	 * @param visitors the visitors to set
	 */
	public void setVisitors(Set visitors) {
		this.visitors = visitors;
	}
	/**
	 * @return the generateDate
	 */
	public Date getGenerateDate() {
		return generateDate;
	}
	/**
	 * @param generateDate the generateDate to set
	 */
	public void setGenerateDate(Date generateDate) {
		this.generateDate = generateDate;
	}
}
