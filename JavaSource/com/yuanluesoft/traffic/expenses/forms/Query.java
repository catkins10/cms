package com.yuanluesoft.traffic.expenses.forms;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Query extends ActionForm {
	private String plateNumberPrefix; //车牌前缀,闽A
	private String plateNumber; //车牌号
	private String vehicleType; //车子类型,小车等
	
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
	 * @return the plateNumber
	 */
	public String getPlateNumber() {
		return plateNumber;
	}
	/**
	 * @param plateNumber the plateNumber to set
	 */
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	/**
	 * @return the plateNumberPrefix
	 */
	public String getPlateNumberPrefix() {
		return plateNumberPrefix;
	}
	/**
	 * @param plateNumberPrefix the plateNumberPrefix to set
	 */
	public void setPlateNumberPrefix(String plateNumberPrefix) {
		this.plateNumberPrefix = plateNumberPrefix;
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
	/**
	 * @return the vehicleType
	 */
	public String getVehicleType() {
		return vehicleType;
	}
	/**
	 * @param vehicleType the vehicleType to set
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
}