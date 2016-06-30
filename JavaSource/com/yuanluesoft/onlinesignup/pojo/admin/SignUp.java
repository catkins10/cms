/*
 * Created on 2006-5-31
 *
 */
package com.yuanluesoft.onlinesignup.pojo.admin;

import java.sql.Timestamp;

import com.yuanluesoft.cms.publicservice.pojo.PublicService;

/**
 * 报名信息（signup_signup）
 * @author zyh
 *
 */
public class SignUp extends PublicService {
	private String name;//姓名
	private String sex;//性别
	private String school;//毕业学校
	private String idCard;//身份证号
	private double score;//中考成绩（应届生填）
	private String candidateNo;//准考证号（应届生填）
	private String province;//省
	private String city;//市
	private String country;//县
	private String addr;//家庭住址
	private String postalCode;//邮政编码
	private String phone;//联系电话
	private String major;//报读专业
	private int status;//状态 0：新建 1：审核通过 2：审核不通过  4：待审 
	private String remark;//备注
	private String unAuditRemark;//撤销备注
	private long auditorId;//审核人ID
	private String auditor;//审核人
	private Timestamp audited;//审核时间
	private String creator;//创建人
	private Timestamp created;//创建时间
	private String unAuditor;//撤销人
	private Timestamp unAudited;//撤销时间
	
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getCandidateNo() {
		return candidateNo;
	}
	public void setCandidateNo(String candidateNo) {
		this.candidateNo = candidateNo;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Timestamp getAudited() {
		return audited;
	}
	public void setAudited(Timestamp audited) {
		this.audited = audited;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public long getAuditorId() {
		return auditorId;
	}
	public void setAuditorId(long auditorId) {
		this.auditorId = auditorId;
	}
	public String getUnAuditRemark() {
		return unAuditRemark;
	}
	public void setUnAuditRemark(String unAuditRemark) {
		this.unAuditRemark = unAuditRemark;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Timestamp getUnAudited() {
		return unAudited;
	}
	public void setUnAudited(Timestamp unAudited) {
		this.unAudited = unAudited;
	}
	public String getUnAuditor() {
		return unAuditor;
	}
	public void setUnAuditor(String unAuditor) {
		this.unAuditor = unAuditor;
	}
	
	
	
	
	
	
}
