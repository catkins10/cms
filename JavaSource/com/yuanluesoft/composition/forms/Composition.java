package com.yuanluesoft.composition.forms;

import com.yuanluesoft.jeaf.workflow.form.WorkflowForm;

public class Composition extends WorkflowForm {
	private String title;	//标题
	private String content;	//正文
	private String writer;
	private String workflowInstanceId; //工作流实例ID
	private String comType;	//所属类型
//	private long correctId;	//批改人id
	private String correctName;	//批改人姓名
	
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
	public String getWorkflowInstanceId() {
		return workflowInstanceId;
	}
	public void setWorkflowInstanceId(String workflowInstanceId) {
		this.workflowInstanceId = workflowInstanceId;
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
}
