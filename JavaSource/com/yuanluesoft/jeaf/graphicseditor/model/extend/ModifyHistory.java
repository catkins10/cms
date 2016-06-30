/*
 * Created on 2005-1-4
 *
 */
package com.yuanluesoft.jeaf.graphicseditor.model.extend;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author linchuan
 *
 */
public class ModifyHistory implements Serializable {
	private long modifierId; //修改者ID
	private String userName; //修改者姓名
	private Date modifyDate; //修改时间
	
	/**
	 * 获取显示的标题
	 * @return
	 */
	public String getTitle() {
		return new SimpleDateFormat("yyyy-MM-dd").format(modifyDate) + " " + userName;
	}

	/**
	 * @return Returns the modifierId.
	 */
	public long getModifierId() {
		return modifierId;
	}
	/**
	 * @param modifierId The modifierId to set.
	 */
	public void setModifierId(long modifierId) {
		this.modifierId = modifierId;
	}
	/**
	 * @return Returns the modifyDate.
	 */
	public Date getModifyDate() {
		return modifyDate;
	}
	/**
	 * @param modifyDate The modifyDate to set.
	 */
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName The userName to set.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
