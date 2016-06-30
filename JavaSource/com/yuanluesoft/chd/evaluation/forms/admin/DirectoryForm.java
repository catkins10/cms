package com.yuanluesoft.chd.evaluation.forms.admin;


/**
 * 
 * @author linchuan
 *
 */
public class DirectoryForm extends com.yuanluesoft.jeaf.directorymanage.forms.DirectoryForm {
	private long sourceDirectoryId; //源目录ID,自动从上级复制的目录，需要加上对应目录的ID
	private String dataWorkflowId; //资料上传流程ID
	private String dataWorkflowName; //资料上传流程名称
	private String selfEvalWorkflowId; //自评流程ID
	private String selfEvalWorkflowName; //自评流程名称
	private char dataCycle = '0'; //资料提交频率,按周/按月/按年/不定期
	private String dataCycleEnd; //资料提交时间点,星期/每月的几日/每年的哪一天
	private String dataUrgency; //资料提交催办时间点,星期/每月的几日/每年的哪一天
	private char selfEvalCurrentMonth = '0'; //当月自查
	private String selfEvalEnd; //自评时间点,每月的几日
	private String selfEvalUrgency; //自查催办时间点,每月的几日

	/**
	 * @return the sourceDirectoryId
	 */
	public long getSourceDirectoryId() {
		return sourceDirectoryId;
	}

	/**
	 * @param sourceDirectoryId the sourceDirectoryId to set
	 */
	public void setSourceDirectoryId(long sourceDirectoryId) {
		this.sourceDirectoryId = sourceDirectoryId;
	}

	/**
	 * @return the dataCycle
	 */
	public char getDataCycle() {
		return dataCycle;
	}

	/**
	 * @param dataCycle the dataCycle to set
	 */
	public void setDataCycle(char dataCycle) {
		this.dataCycle = dataCycle;
	}

	/**
	 * @return the dataCycleEnd
	 */
	public String getDataCycleEnd() {
		return dataCycleEnd;
	}

	/**
	 * @param dataCycleEnd the dataCycleEnd to set
	 */
	public void setDataCycleEnd(String dataCycleEnd) {
		this.dataCycleEnd = dataCycleEnd;
	}

	/**
	 * @return the dataWorkflowId
	 */
	public String getDataWorkflowId() {
		return dataWorkflowId;
	}

	/**
	 * @param dataWorkflowId the dataWorkflowId to set
	 */
	public void setDataWorkflowId(String dataWorkflowId) {
		this.dataWorkflowId = dataWorkflowId;
	}

	/**
	 * @return the dataWorkflowName
	 */
	public String getDataWorkflowName() {
		return dataWorkflowName;
	}

	/**
	 * @param dataWorkflowName the dataWorkflowName to set
	 */
	public void setDataWorkflowName(String dataWorkflowName) {
		this.dataWorkflowName = dataWorkflowName;
	}

	/**
	 * @return the selfEvalEnd
	 */
	public String getSelfEvalEnd() {
		return selfEvalEnd;
	}

	/**
	 * @param selfEvalEnd the selfEvalEnd to set
	 */
	public void setSelfEvalEnd(String selfEvalEnd) {
		this.selfEvalEnd = selfEvalEnd;
	}

	/**
	 * @return the selfEvalWorkflowId
	 */
	public String getSelfEvalWorkflowId() {
		return selfEvalWorkflowId;
	}

	/**
	 * @param selfEvalWorkflowId the selfEvalWorkflowId to set
	 */
	public void setSelfEvalWorkflowId(String selfEvalWorkflowId) {
		this.selfEvalWorkflowId = selfEvalWorkflowId;
	}

	/**
	 * @return the selfEvalWorkflowName
	 */
	public String getSelfEvalWorkflowName() {
		return selfEvalWorkflowName;
	}

	/**
	 * @param selfEvalWorkflowName the selfEvalWorkflowName to set
	 */
	public void setSelfEvalWorkflowName(String selfEvalWorkflowName) {
		this.selfEvalWorkflowName = selfEvalWorkflowName;
	}

	/**
	 * @return the dataUrgency
	 */
	public String getDataUrgency() {
		return dataUrgency;
	}

	/**
	 * @param dataUrgency the dataUrgency to set
	 */
	public void setDataUrgency(String dataUrgency) {
		this.dataUrgency = dataUrgency;
	}

	/**
	 * @return the selfEvalUrgency
	 */
	public String getSelfEvalUrgency() {
		return selfEvalUrgency;
	}

	/**
	 * @param selfEvalUrgency the selfEvalUrgency to set
	 */
	public void setSelfEvalUrgency(String selfEvalUrgency) {
		this.selfEvalUrgency = selfEvalUrgency;
	}

	/**
	 * @return the selfEvalCurrentMonth
	 */
	public char getSelfEvalCurrentMonth() {
		return selfEvalCurrentMonth;
	}

	/**
	 * @param selfEvalCurrentMonth the selfEvalCurrentMonth to set
	 */
	public void setSelfEvalCurrentMonth(char selfEvalCurrentMonth) {
		this.selfEvalCurrentMonth = selfEvalCurrentMonth;
	}
}