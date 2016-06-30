package com.yuanluesoft.educ.train.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * @author cbb
 *
 */
public class TrainConfigForm extends ActionForm {

	private String profession;	//鉴定职业
	private String type;	//鉴定类型
	private String level;	//鉴定级别
	
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
