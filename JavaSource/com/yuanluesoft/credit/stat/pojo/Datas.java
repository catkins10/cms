package com.yuanluesoft.credit.stat.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 统计数据
 * @author zyh
 *
 */
public class Datas extends Record {
	private long unitId;//单位ID
	private String unitName;//单位名称
	private String statObj;//统计对象：栏目名称，应用名称
	private int num;//发布数量
	private Timestamp newesIssueTime;//最近跟新时间
	private String flag;//标示：column,app
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getStatObj() {
		return statObj;
	}
	public void setStatObj(String statObj) {
		this.statObj = statObj;
	}
	public long getUnitId() {
		return unitId;
	}
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public Timestamp getNewesIssueTime() {
		return newesIssueTime;
	}
	public void setNewesIssueTime(Timestamp newesIssueTime) {
		this.newesIssueTime = newesIssueTime;
	}
	
}