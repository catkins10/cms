package com.yuanluesoft.composition.pojo;

import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;

public class Composition extends WorkflowData {
	private String title;	//标题
	private String content;	//正文
	private String writer;
	private String workflowInstanceId; //工作流实例ID
	private String comType;	//所属类型
//	private long correctId;	//批改人id
	private String correctName;	//批改人姓名
//	private String grade;	//教师评级
//	private String comment;	//教师评语
	
	public String getComType() {
		return comType;
	}
	public void setComType(String comType) {
		this.comType = comType;
	}
//	public long getCorrectId() {
//		return correctId;
//	}
//	public void setCorrectId(long correctId) {
//		this.correctId = correctId;
//	}
	public String getCorrectName() {
		return correctName;
	}
	public void setCorrectName(String correctName) {
		this.correctName = correctName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getWorkflowInstanceId() {
		return workflowInstanceId;
	}
	public void setWorkflowInstanceId(String workflowInstanceId) {
		this.workflowInstanceId = workflowInstanceId;
	}
}
