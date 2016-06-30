/*
 * Created on 2005-1-1
 *
 */
package com.yuanluesoft.jeaf.graphicseditor.model;

import com.yuanluesoft.jeaf.base.model.Element;

/**
 * 连接
 * @author linchuan
 *
 */
public class Transition extends Element {
	private Element fromElement;
	private Element toElement;
	private String fromElementId;
	private String toElementId;

	/**
	 * @return Returns the fromElement.
	 */
	public Element getFromElement() {
		return fromElement;
	}
	/**
	 * @param fromElement The fromElement to set.
	 */
	public void setFromElement(Element fromElement) {
		this.fromElement = fromElement;
	}
	/**
	 * @return Returns the toElement.
	 */
	public Element getToElement() {
		return toElement;
	}
	/**
	 * @param toElement The toElement to set.
	 */
	public void setToElement(Element toElement) {
		this.toElement = toElement;
	}
    /**
     * @return Returns the fromElementId.
     */
    public String getFromElementId() {
        return fromElementId;
    }
    /**
     * @param fromElementId The fromElementId to set.
     */
    public void setFromElementId(String fromElementId) {
        this.fromElementId = fromElementId;
    }
    /**
     * @return Returns the toElementId.
     */
    public String getToElementId() {
        return toElementId;
    }
    /**
     * @param toElementId The toElementId to set.
     */
    public void setToElementId(String toElementId) {
        this.toElementId = toElementId;
    }
}
