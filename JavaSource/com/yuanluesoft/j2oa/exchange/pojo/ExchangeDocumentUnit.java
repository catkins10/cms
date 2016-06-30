package com.yuanluesoft.j2oa.exchange.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 公文:接收单位(exchange_document_unit)
 * @author linchuan
 *
 */
public class ExchangeDocumentUnit extends Record {
	private long documentId; //公文ID
	private long unitId; //单位ID
	private String unitName; //单位名称
	private String unitType; //单位类别,1/主送,2/抄送,3/其他接收单位
	private Timestamp signTime; //签收时间
	private String signPerson; //签收人
	private long signPersonId; //签收人ID
	
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
	 * @return the signPerson
	 */
	public String getSignPerson() {
		return signPerson;
	}
	/**
	 * @param signPerson the signPerson to set
	 */
	public void setSignPerson(String signPerson) {
		this.signPerson = signPerson;
	}
	/**
	 * @return the signPersonId
	 */
	public long getSignPersonId() {
		return signPersonId;
	}
	/**
	 * @param signPersonId the signPersonId to set
	 */
	public void setSignPersonId(long signPersonId) {
		this.signPersonId = signPersonId;
	}
	/**
	 * @return the signTime
	 */
	public Timestamp getSignTime() {
		return signTime;
	}
	/**
	 * @param signTime the signTime to set
	 */
	public void setSignTime(Timestamp signTime) {
		this.signTime = signTime;
	}
	/**
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}
	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}
	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	/**
	 * @return the unitType
	 */
	public String getUnitType() {
		return unitType;
	}
	/**
	 * @param unitType the unitType to set
	 */
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
}
