/*
 * Created on 2005-1-20
 *
 */
package com.yuanluesoft.jeaf.graphicseditor.model.extend;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 * 
 * @author linchuan
 *
 */
public class ConfigureExtend extends CloneableObject {
	private int nextElementId;
	private String creator; //创建者
	private Date createDate; //创建时间
	private List modifyHistory;	//修改历史
	private List elements;
	
	/**
	 * @return Returns the createDate.
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate The createDate to set.
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return Returns the creator.
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator The creator to set.
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return Returns the elements.
	 */
	public List getElements() {
		if(elements==null) {
			elements = new ArrayList();
		}
		return elements;
	}
	/**
	 * @param elements The elements to set.
	 */
	public void setElements(List elements) {
		this.elements = elements;
	}
	/**
	 * @return Returns the nextElementId.
	 */
	public int getNextElementId() {
		return nextElementId;
	}
	/**
	 * @param nextElementId The nextElementId to set.
	 */
	public void setNextElementId(int nextElementId) {
		this.nextElementId = nextElementId;
	}
	/**
	 * @return Returns the modifyHistory.
	 */
	public List getModifyHistory() {
		return modifyHistory;
	}
	/**
	 * @param modifyHistory The modifyHistory to set.
	 */
	public void setModifyHistory(List modifyHistory) {
		this.modifyHistory = modifyHistory;
	}
}
