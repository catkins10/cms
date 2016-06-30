package com.yuanluesoft.exchange.client.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 数据交换任务(exchange_task)
 * @author linchuan
 *
 */
public class ExchangeTask extends Record {
	private String receivers; //接收者列表
	private String completedReceivers; //完成交换的接收者
	private Timestamp created; //任务创建时间
	private Timestamp startupTime; //任务启动时间,重试5次后,传输时间加10分钟,超过30次失败加1小时,超过100次,任务终止,避免占用过多的资源
	private String taskType; //任务类型,updateRecord/deleteRecord/sendFile/deleteFile
	private String recordTitle; //记录类型,如：文章、政府信息
	private String recordClassName; //记录类名称
	private long recordId; //记录ID
	private String recordEncoded; //记录详情
	private String filePath; //文件路径
	private char createDirectoryIfNeed; //目录不存在是否创建
	private int failedCount; //交换失败次数
	private String failedReason; //交换失败原因
	
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the failedCount
	 */
	public int getFailedCount() {
		return failedCount;
	}
	/**
	 * @param failedCount the failedCount to set
	 */
	public void setFailedCount(int failedCount) {
		this.failedCount = failedCount;
	}
	/**
	 * @return the failedReason
	 */
	public String getFailedReason() {
		return failedReason;
	}
	/**
	 * @param failedReason the failedReason to set
	 */
	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}
	/**
	 * @return the recordClassName
	 */
	public String getRecordClassName() {
		return recordClassName;
	}
	/**
	 * @param recordClassName the recordClassName to set
	 */
	public void setRecordClassName(String recordClassName) {
		this.recordClassName = recordClassName;
	}
	/**
	 * @return the recordId
	 */
	public long getRecordId() {
		return recordId;
	}
	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}
	/**
	 * @return the recordTitle
	 */
	public String getRecordTitle() {
		return recordTitle;
	}
	/**
	 * @param recordTitle the recordTitle to set
	 */
	public void setRecordTitle(String recordTitle) {
		this.recordTitle = recordTitle;
	}
	/**
	 * @return the startupTime
	 */
	public Timestamp getStartupTime() {
		return startupTime;
	}
	/**
	 * @param startupTime the startupTime to set
	 */
	public void setStartupTime(Timestamp startupTime) {
		this.startupTime = startupTime;
	}
	/**
	 * @return the recordEncoded
	 */
	public String getRecordEncoded() {
		return recordEncoded;
	}
	/**
	 * @param recordEncoded the recordEncoded to set
	 */
	public void setRecordEncoded(String recordEncoded) {
		this.recordEncoded = recordEncoded;
	}
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/**
	 * @return the taskType
	 */
	public String getTaskType() {
		return taskType;
	}
	/**
	 * @param taskType the taskType to set
	 */
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	/**
	 * @return the createDirectoryIfNeed
	 */
	public char getCreateDirectoryIfNeed() {
		return createDirectoryIfNeed;
	}
	/**
	 * @param createDirectoryIfNeed the createDirectoryIfNeed to set
	 */
	public void setCreateDirectoryIfNeed(char createDirectoryIfNeed) {
		this.createDirectoryIfNeed = createDirectoryIfNeed;
	}
	/**
	 * @return the completedReceivers
	 */
	public String getCompletedReceivers() {
		return completedReceivers;
	}
	/**
	 * @param completedReceivers the completedReceivers to set
	 */
	public void setCompletedReceivers(String completedReceivers) {
		this.completedReceivers = completedReceivers;
	}
	/**
	 * @return the receivers
	 */
	public String getReceivers() {
		return receivers;
	}
	/**
	 * @param receivers the receivers to set
	 */
	public void setReceivers(String receivers) {
		this.receivers = receivers;
	}
}