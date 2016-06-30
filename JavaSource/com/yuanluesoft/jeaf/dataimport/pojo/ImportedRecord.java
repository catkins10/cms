package com.yuanluesoft.jeaf.dataimport.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 数据导入:导入的记录(dataimport_record)
 * @author linchuan
 *
 */
public class ImportedRecord extends Record {
	private String importDataName; //被导入数据名称,如：投诉、政府信息
	private long siteId; //站点ID
	private String remoteRecordId; //导入的记录ID
	private long localRecordId; //本系统的记录ID
	
	/**
	 * @return the importDataName
	 */
	public String getImportDataName() {
		return importDataName;
	}
	/**
	 * @param importDataName the importDataName to set
	 */
	public void setImportDataName(String importDataName) {
		this.importDataName = importDataName;
	}
	/**
	 * @return the localRecordId
	 */
	public long getLocalRecordId() {
		return localRecordId;
	}
	/**
	 * @param localRecordId the localRecordId to set
	 */
	public void setLocalRecordId(long localRecordId) {
		this.localRecordId = localRecordId;
	}
	/**
	 * @return the remoteRecordId
	 */
	public String getRemoteRecordId() {
		return remoteRecordId;
	}
	/**
	 * @param remoteRecordId the remoteRecordId to set
	 */
	public void setRemoteRecordId(String remoteRecordId) {
		this.remoteRecordId = remoteRecordId;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
}