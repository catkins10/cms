package com.yuanluesoft.chd.evaluation.data.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 资料:所属目录(chd_eval_data_subjection)
 * @author linchuan
 *
 */
public class ChdEvaluationDataSubjection extends Record {
	private long dataId; //资料ID
	private long directoryId; //目录ID
	
	/**
	 * @return the directoryId
	 */
	public long getDirectoryId() {
		return directoryId;
	}
	/**
	 * @param directoryId the directoryId to set
	 */
	public void setDirectoryId(long directoryId) {
		this.directoryId = directoryId;
	}
	/**
	 * @return the dataId
	 */
	public long getDataId() {
		return dataId;
	}
	/**
	 * @param dataId the dataId to set
	 */
	public void setDataId(long dataId) {
		this.dataId = dataId;
	}
}