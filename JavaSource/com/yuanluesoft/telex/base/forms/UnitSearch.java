package com.yuanluesoft.telex.base.forms;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class UnitSearch extends ActionForm {
	private String key;
	private List units; //搜索结果

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the units
	 */
	public List getUnits() {
		return units;
	}

	/**
	 * @param units the units to set
	 */
	public void setUnits(List units) {
		this.units = units;
	}
}