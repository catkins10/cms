package com.yuanluesoft.chd.evaluation.pojo;

import com.yuanluesoft.jeaf.directorymanage.pojo.DirectorySubjection;

/**
 * 评价体系目录:目录隶属关系(chd_eval_directory_subjection)
 * @author linchuan
 *
 */
public class ChdEvaluationDirectorySubjection extends DirectorySubjection {
	private long sourceDirectoryId; //源目录ID,自动从上级复制的目录，需要加上对应目录的ID

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
}