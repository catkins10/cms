package com.yuanluesoft.credit.enterprisecredit.item.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 龙海市安监局事故调查处理情况统计(credit_punish)
 * @author zyh
 *
 */
public class Handle extends Record {
	private String name ; //案件名称
	private String unit;//行政处罚单位或个人名称
	private String reason;//处罚事由
	private String person;//法定代表人（负责人）姓名
	private String bookAndCode;//处罚决定书及其文号
	private String remark;//备注
	
	
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getBookAndCode() {
		return bookAndCode;
	}
	public void setBookAndCode(String bookAndCode) {
		this.bookAndCode = bookAndCode;
	}

	
	
	
	
}