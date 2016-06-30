package com.yuanluesoft.qualification.pojo;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

public class Qualification extends WorkflowData {
	private String name;	//证书名称
	private String level;	//证书等级
	private String authority;	//颁发机构
	private Date obtainTime;	//获得证书时间
	private long stuId;	//所属学生id
	private char approvalPass; //是否审批通过
	private Timestamp created;	//登记时间
	private String stuName;
	private String workflowInstanceId; //工作流实例ID
	
	public char getApprovalPass() {
		return approvalPass;
	}
	public void setApprovalPass(char approvalPass) {
		this.approvalPass = approvalPass;
	}
	public String getWorkflowInstanceId() {
		return workflowInstanceId;
	}
	public void setWorkflowInstanceId(String workflowInstanceId) {
		this.workflowInstanceId = workflowInstanceId;
	}
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getObtainTime() {
		return obtainTime;
	}
	public void setObtainTime(Date obtainTime) {
		this.obtainTime = obtainTime;
	}
	
	public long getStuId() {
		return stuId;
	}
	public void setStuId(long stuId) {
		this.stuId = stuId;
	}
}
