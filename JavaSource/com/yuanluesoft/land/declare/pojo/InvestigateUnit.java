package com.yuanluesoft.land.declare.pojo;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 地址勘察单位资质
 * @author kangshiwei
 *
 */
public class InvestigateUnit extends Record {
	private String certificateNum; //证书编号
	private String unitName; //单位名称
	private String applyType;//申请类型
	private Date validFrom; //有效期起
	private Date validEnd; //有效期止
	private String residence; //住所
	private String leader; //法定代表人
	private String postal; //邮编
	private String tel; //联系电话
	private String qualificationTypeLeve; //资质类别和等级
	private String issuingUnit; //发证机关
	private String provinces; //所属省份
	private Timestamp created; //创建时间
	
	/**
	 * @return certificateNum
	 */
	public String getCertificateNum() {
		return certificateNum;
	}
	/**
	 * @param certificateNum 要设置的 certificateNum
	 */
	public void setCertificateNum(String certificateNum) {
		this.certificateNum = certificateNum;
	}
	/**
	 * @return issuingUnit
	 */
	public String getIssuingUnit() {
		return issuingUnit;
	}
	/**
	 * @param issuingUnit 要设置的 issuingUnit
	 */
	public void setIssuingUnit(String issuingUnit) {
		this.issuingUnit = issuingUnit;
	}
	/**
	 * @return leader
	 */
	public String getLeader() {
		return leader;
	}
	/**
	 * @param leader 要设置的 leader
	 */
	public void setLeader(String leader) {
		this.leader = leader;
	}
	/**
	 * @return postal
	 */
	public String getPostal() {
		return postal;
	}
	/**
	 * @param postal 要设置的 postal
	 */
	public void setPostal(String postal) {
		this.postal = postal;
	}
	/**
	 * @return provinces
	 */
	public String getProvinces() {
		return provinces;
	}
	/**
	 * @param provinces 要设置的 provinces
	 */
	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}
	/**
	 * @return qualificationTypeLeve
	 */
	public String getQualificationTypeLeve() {
		return qualificationTypeLeve;
	}
	/**
	 * @param qualificationTypeLeve 要设置的 qualificationTypeLeve
	 */
	public void setQualificationTypeLeve(String qualificationTypeLeve) {
		this.qualificationTypeLeve = qualificationTypeLeve;
	}
	/**
	 * @return residence
	 */
	public String getResidence() {
		return residence;
	}
	/**
	 * @param residence 要设置的 residence
	 */
	public void setResidence(String residence) {
		this.residence = residence;
	}
	/**
	 * @return tel
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * @param tel 要设置的 tel
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
	/**
	 * @return unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * @param unitName 要设置的 unitName
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	/**
	 * @return validEnd
	 */
	public Date getValidEnd() {
		return validEnd;
	}
	/**
	 * @param validEnd 要设置的 validEnd
	 */
	public void setValidEnd(Date validEnd) {
		this.validEnd = validEnd;
	}
	/**
	 * @return validFrom
	 */
	public Date getValidFrom() {
		return validFrom;
	}
	/**
	 * @param validFrom 要设置的 validFrom
	 */
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}
	/**
	 * @return applyType
	 */
	public String getApplyType() {
		return applyType;
	}
	/**
	 * @param applyType 要设置的 applyType
	 */
	public void setApplyType(String applyType) {
		this.applyType = applyType;
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

}
