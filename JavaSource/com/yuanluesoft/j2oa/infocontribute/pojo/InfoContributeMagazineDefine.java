package com.yuanluesoft.j2oa.infocontribute.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 投稿:刊物(info_contribute_magazine_define)
 * @author linchuan
 *
 */
public class InfoContributeMagazineDefine extends Record {
	private String name; //名称
	private String unitName; //单位名称

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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