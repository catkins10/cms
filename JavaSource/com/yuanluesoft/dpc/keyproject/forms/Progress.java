package com.yuanluesoft.dpc.keyproject.forms;

import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectProgress;

/**
 * 
 * @author linchuan
 *
 */
public class Progress extends Project {
	private KeyProjectProgress progress = new KeyProjectProgress();

	/**
	 * @return the progress
	 */
	public KeyProjectProgress getProgress() {
		return progress;
	}

	/**
	 * @param progress the progress to set
	 */
	public void setProgress(KeyProjectProgress progress) {
		this.progress = progress;
	}
}