package com.yuanluesoft.j2oa.infocontribute.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 投稿:接收单位(info_contribute_receive_unit)
 * @author linchuan
 *
 */
public class InfoContributeReceiveUnit extends Record {
	private long infoId; //稿件ID
	private String unitName; //单位名称
	
	/**
	 * @return the infoId
	 */
	public long getInfoId() {
		return infoId;
	}
	/**
	 * @param infoId the infoId to set
	 */
	public void setInfoId(long infoId) {
		this.infoId = infoId;
	}
	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
}