package com.yuanluesoft.credit.stat.forms;

import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 发布统计
 * @author zyh
 *
 */
public class CreditStat extends ActionForm {
	private String unitName;//单位名称
	private Set data;//统计数据列表
	public Set getData() {
		return data;
	}
	public void setData(Set data) {
		this.data = data;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	
}