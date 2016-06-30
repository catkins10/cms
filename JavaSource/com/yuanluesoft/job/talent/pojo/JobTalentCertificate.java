package com.yuanluesoft.job.talent.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 人才:证书(job_talent_certificate)
 * @author linchuan
 *
 */
public class JobTalentCertificate extends Record {
	private long talentId; //人才ID
	private Date gained; //获得时间
	private String certificateName; //证书名称
	private String mark; //成绩
	
	/**
	 * @return the certificateName
	 */
	public String getCertificateName() {
		return certificateName;
	}
	/**
	 * @param certificateName the certificateName to set
	 */
	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}
	/**
	 * @return the gained
	 */
	public Date getGained() {
		return gained;
	}
	/**
	 * @param gained the gained to set
	 */
	public void setGained(Date gained) {
		this.gained = gained;
	}
	/**
	 * @return the mark
	 */
	public String getMark() {
		return mark;
	}
	/**
	 * @param mark the mark to set
	 */
	public void setMark(String mark) {
		this.mark = mark;
	}
	/**
	 * @return the talentId
	 */
	public long getTalentId() {
		return talentId;
	}
	/**
	 * @param talentId the talentId to set
	 */
	public void setTalentId(long talentId) {
		this.talentId = talentId;
	}
}