/*
 * Created on 2005-1-1
 *
 */
package com.yuanluesoft.jeaf.graphicseditor.model.extend;

/**
 * 
 * @author linchuan
 *
 */
public class Line extends ElementExtend {
	private int enterIndex; //入口元素的连接点序号
	private int exitIndex; //出口元素的连接点序号
	
	/**
	 * @return Returns the enterIndex.
	 */
	public int getEnterIndex() {
		return enterIndex;
	}
	/**
	 * @param enterIndex The enterIndex to set.
	 */
	public void setEnterIndex(int enterIndex) {
		this.enterIndex = enterIndex;
	}
	/**
	 * @return Returns the exitIndex.
	 */
	public int getExitIndex() {
		return exitIndex;
	}
	/**
	 * @param exitIndex The exitIndex to set.
	 */
	public void setExitIndex(int exitIndex) {
		this.exitIndex = exitIndex;
	}
}
