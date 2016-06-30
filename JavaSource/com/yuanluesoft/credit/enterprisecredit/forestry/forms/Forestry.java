package com.yuanluesoft.credit.enterprisecredit.forestry.forms;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author zyh
 *
 */
public class Forestry extends ActionForm {
	private String person ; //被处罚人（单位）
	private String cardNum; //身份证号或其他证件名称：
	private String personAddr;//被处罚人地址
	private String bookNum;//处罚决定书号
	private String addr;//违法地点
	private String summary;//违法情况
	private String result;//处罚情况
	private String other;//其他
	private String remark;//备注
	
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getBookNum() {
		return bookNum;
	}
	public void setBookNum(String bookNum) {
		this.bookNum = bookNum;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
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
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getPersonAddr() {
		return personAddr;
	}
	public void setPersonAddr(String personAddr) {
		this.personAddr = personAddr;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	
	
	
}