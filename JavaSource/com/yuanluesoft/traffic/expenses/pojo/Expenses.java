package com.yuanluesoft.traffic.expenses.pojo;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Expenses {
	private String fullPlateNumber; //车号
	private String type;//厂牌型号
	private String halt;//是否报停
	private Date expensesDate; //缴费有效期限
	private String collection; //所属稽征所
	private Timestamp lastUpdated; //数据最后更新日期为: 2008-4-28 20:02:00
	
	/**
	 * @return the collection
	 */
	public String getCollection() {
		return collection;
	}
	/**
	 * @param collection the collection to set
	 */
	public void setCollection(String collection) {
		this.collection = collection;
	}
	/**
	 * @return the expensesDate
	 */
	public Date getExpensesDate() {
		return expensesDate;
	}
	/**
	 * @param expensesDate the expensesDate to set
	 */
	public void setExpensesDate(Date expensesDate) {
		this.expensesDate = expensesDate;
	}
	/**
	 * @return the fullPlateNumber
	 */
	public String getFullPlateNumber() {
		return fullPlateNumber;
	}
	/**
	 * @param fullPlateNumber the fullPlateNumber to set
	 */
	public void setFullPlateNumber(String fullPlateNumber) {
		this.fullPlateNumber = fullPlateNumber;
	}
	/**
	 * @return the halt
	 */
	public String getHalt() {
		return halt;
	}
	/**
	 * @param halt the halt to set
	 */
	public void setHalt(String halt) {
		this.halt = halt;
	}
	/**
	 * @return the lastUpdated
	 */
	public Timestamp getLastUpdated() {
		return lastUpdated;
	}
	/**
	 * @param lastUpdated the lastUpdated to set
	 */
	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
