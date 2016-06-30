package com.yuanluesoft.chd.evaluation.selfeval.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 自评:所属目录(chd_eval_self_subjection)
 * @author linchuan
 *
 */
public class ChdEvaluationSelfSubjection extends Record {
	private long evalId; //自评ID
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
	 * @return the evalId
	 */
	public long getEvalId() {
		return evalId;
	}
	/**
	 * @param evalId the evalId to set
	 */
	public void setEvalId(long evalId) {
		this.evalId = evalId;
	}
}