package com.yuanluesoft.credit.serviceorg.serviceitem.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 办事事项(credit_serviceitem)
 * @author zyh
 *
 */
public class ServiceItem extends Record {
	private String name;//事项名称
	private String condition; //申办条件
	private String workFlow; //办理流程
	private String timeLimit; //办理时限
	private String type;//办理方式
	private String material;//办理材料
	private String chargeStandard; //收费项目及标准标准
	private String promise;//服务承诺
	
	private String person;//联系人
	private String mail;//邮箱
	private String faxes; //传真
	private String tel; //电话
	
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	public String getChargeStandard() {
		return chargeStandard;
	}
	public void setChargeStandard(String chargeStandard) {
		this.chargeStandard = chargeStandard;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
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
	public String getFaxes() {
		return faxes;
	}
	public void setFaxes(String faxes) {
		this.faxes = faxes;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
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
	public String getPromise() {
		return promise;
	}
	public void setPromise(String promise) {
		this.promise = promise;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(String timeLimit) {
		this.timeLimit = timeLimit;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getWorkFlow() {
		return workFlow;
	}
	public void setWorkFlow(String workFlow) {
		this.workFlow = workFlow;
	}
	
	
}