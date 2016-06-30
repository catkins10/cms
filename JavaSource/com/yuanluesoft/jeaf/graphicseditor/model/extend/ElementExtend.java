/*
 * Created on 2005-1-4
 *
 */
package com.yuanluesoft.jeaf.graphicseditor.model.extend;

import com.yuanluesoft.jeaf.base.model.Element;

/**
 * 
 * @author linchuan
 *
 */
public class ElementExtend extends Element {
	private int startX = -1;
	private int startY = -1; 
	private int endX = -1;
	private int endY = -1;
	private boolean readOnly; //只读
	private boolean configOnly; //只配置
	private boolean moveDisable; //禁止移动
	private boolean deleteDisable; //禁止删除
	private boolean displayOnly; //是否只显示,不允许配置
		
	/**
	 * @return Returns the endX.
	 */
	public int getEndX() {
		return endX;
	}
	/**
	 * @param endX The endX to set.
	 */
	public void setEndX(int endX) {
		this.endX = endX;
	}
	/**
	 * @return Returns the endY.
	 */
	public int getEndY() {
		return endY;
	}
	/**
	 * @param endY The endY to set.
	 */
	public void setEndY(int endY) {
		this.endY = endY;
	}
	/**
	 * @return Returns the startX.
	 */
	public int getStartX() {
		return startX;
	}
	/**
	 * @param startX The startX to set.
	 */
	public void setStartX(int startX) {
		this.startX = startX;
	}
	/**
	 * @return Returns the startY.
	 */
	public int getStartY() {
		return startY;
	}
	/**
	 * @param startY The startY to set.
	 */
	public void setStartY(int startY) {
		this.startY = startY;
	}
	/**
	 * @return Returns the configOnly.
	 */
	public boolean isConfigOnly() {
		return configOnly;
	}
	/**
	 * @param configOnly The configOnly to set.
	 */
	public void setConfigOnly(boolean configOnly) {
		this.configOnly = configOnly;
	}
	/**
	 * @return Returns the deleteDisable.
	 */
	public boolean isDeleteDisable() {
		return deleteDisable;
	}
	/**
	 * @param deleteDisable The deleteDisable to set.
	 */
	public void setDeleteDisable(boolean deleteDisable) {
		this.deleteDisable = deleteDisable;
	}
	/**
	 * @return Returns the moveDisable.
	 */
	public boolean isMoveDisable() {
		return moveDisable;
	}
	/**
	 * @param moveDisable The moveDisable to set.
	 */
	public void setMoveDisable(boolean moveDisable) {
		this.moveDisable = moveDisable;
	}
	/**
	 * @return Returns the readOnly.
	 */
	public boolean isReadOnly() {
		return readOnly;
	}
	/**
	 * @param readOnly The readOnly to set.
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
    /**
     * @return Returns the displayOnly.
     */
    public boolean isDisplayOnly() {
        return displayOnly;
    }
    /**
     * @param displayOnly The displayOnly to set.
     */
    public void setDisplayOnly(boolean displayOnly) {
        this.displayOnly = displayOnly;
    }
}