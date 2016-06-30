package com.yuanluesoft.dpc.keyproject.forms;

import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectStageProgress;

/**
 * 
 * @author linchuan
 *
 */
public class StageProgress extends Project {
	private KeyProjectStageProgress stageProgress = new KeyProjectStageProgress();

	/**
	 * @return the stageProgress
	 */
	public KeyProjectStageProgress getStageProgress() {
		return stageProgress;
	}

	/**
	 * @param stageProgress the stageProgress to set
	 */
	public void setStageProgress(KeyProjectStageProgress stageProgress) {
		this.stageProgress = stageProgress;
	}
}