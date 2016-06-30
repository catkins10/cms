/*
 * Created on 2005-1-1
 *
 */
package com.yuanluesoft.jeaf.graphicseditor.model.extend;

import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public class BrokenLine extends Line {
	private List turningXPoints; //转折点X坐标
	private List turningYPoints; //转折点Y坐标
	
	/**
	 * @return the turningXPoints
	 */
	public List getTurningXPoints() {
		return turningXPoints;
	}
	/**
	 * @param turningXPoints the turningXPoints to set
	 */
	public void setTurningXPoints(List turningXPoints) {
		this.turningXPoints = turningXPoints;
	}
	/**
	 * @return the turningYPoints
	 */
	public List getTurningYPoints() {
		return turningYPoints;
	}
	/**
	 * @param turningYPoints the turningYPoints to set
	 */
	public void setTurningYPoints(List turningYPoints) {
		this.turningYPoints = turningYPoints;
	}	
}