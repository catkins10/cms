package com.yuanluesoft.cms.onlineservice.interactive.accept.forms;


/**
 * 
 * @author linchuan
 *
 */
public class Accept extends com.yuanluesoft.cms.onlineservice.interactive.accept.forms.admin.Accept {
	private String directoryId; //网上办事目录ID, 用来显示当前位置
	private String step; //操作步骤: new/填写基本信息,uploadMaterial/上传材料,submit/提交

	/**
	 * @return the directoryId
	 */
	public String getDirectoryId() {
		return directoryId;
	}

	/**
	 * @param directoryId the directoryId to set
	 */
	public void setDirectoryId(String directoryId) {
		this.directoryId = directoryId;
	}

	/**
	 * @return the step
	 */
	public String getStep() {
		return step;
	}

	/**
	 * @param step the step to set
	 */
	public void setStep(String step) {
		this.step = step;
	}
}