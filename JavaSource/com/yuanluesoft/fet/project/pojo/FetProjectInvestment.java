package com.yuanluesoft.fet.project.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 项目管理:资金到达情况(fet_project_investment)
 * @author linchuan
 *
 */
public class FetProjectInvestment extends Record {
	private long projectId; //项目ID
	private Date receiveTime; //时间
	private double money; //金额
	private double moneyChecked; //已验资资金,万美元

	/**
	 * @return the money
	 */
	public double getMoney() {
		return money;
	}
	/**
	 * @param money the money to set
	 */
	public void setMoney(double money) {
		this.money = money;
	}
	/**
	 * @return the projectId
	 */
	public long getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return the receiveTime
	 */
	public Date getReceiveTime() {
		return receiveTime;
	}
	/**
	 * @param receiveTime the receiveTime to set
	 */
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	/**
	 * @return the moneyChecked
	 */
	public double getMoneyChecked() {
		return moneyChecked;
	}
	/**
	 * @param moneyChecked the moneyChecked to set
	 */
	public void setMoneyChecked(double moneyChecked) {
		this.moneyChecked = moneyChecked;
	}

}
