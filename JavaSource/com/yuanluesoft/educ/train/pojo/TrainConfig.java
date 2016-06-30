package com.yuanluesoft.educ.train.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * @author cbb
 *
 */
public class TrainConfig extends Record {

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
