package com.yuanluesoft.land.declare.pojo;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 土地登记(fjgtzy_landregister)
 * @author linchuan
 *
 */
public class LandRegister extends Record {
	private String personName; //权利人名称
	private String location; //土地坐落
	private String registerType; //登记类型
	private String certificateNum; //土地证号
	private String oldCertificateNum; //变更前土地证号
	private double area; //使用权面积
	private String userType; //使用权类型
	private Date registerDate; //登记日期
	private Timestamp created; //创建时间
	
	/**
	 * @return area
	 */
	public double getArea() {
		return area;
	}
	/**
	 * @param area 要设置的 area
	 */
	public void setArea(double area) {
		this.area = area;
	}
	/**
	 * @return certificateNum
	 */
	public String getCertificateNum() {
		return certificateNum;
	}
	/**
	 * @param certificateNum 要设置的 certificateNum
	 */
	public void setCertificateNum(String certificateNum) {
		this.certificateNum = certificateNum;
	}
	/**
	 * @return location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location 要设置的 location
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return oldCertificateNum
	 */
	public String getOldCertificateNum() {
		return oldCertificateNum;
	}
	/**
	 * @param oldCertificateNum 要设置的 oldCertificateNum
	 */
	public void setOldCertificateNum(String oldCertificateNum) {
		this.oldCertificateNum = oldCertificateNum;
	}
	/**
	 * @return personName
	 */
	public String getPersonName() {
		return personName;
	}
	/**
	 * @param personName 要设置的 personName
	 */
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	/**
	 * @return registerDate
	 */
	public Date getRegisterDate() {
		return registerDate;
	}
	/**
	 * @param registerDate 要设置的 registerDate
	 */
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	/**
	 * @return registerType
	 */
	public String getRegisterType() {
		return registerType;
	}
	/**
	 * @param registerType 要设置的 registerType
	 */
	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}
	/**
	 * @return userType
	 */
	public String getUserType() {
		return userType;
	}
	/**
	 * @param userType 要设置的 userType
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}

}
