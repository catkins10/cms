package com.yuanluesoft.credit.enterprisecredit.trafficcredit.forms;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author zyh
 *
 */
public class TrafficCredit extends ActionForm {
	private String person ; //当事人姓名
	private String idCard; //身份证号码
	private String nature;//性质
	private String summary;//交通肇事简要情况
	private String remark;//备注
	private String deparment;//填报单位
	private String writer;//填报人
	private String auditor;//审核人
	
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
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
	public long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	public String getDeparment() {
		return deparment;
	}
	public void setDeparment(String deparment) {
		this.deparment = deparment;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	
	
	
}