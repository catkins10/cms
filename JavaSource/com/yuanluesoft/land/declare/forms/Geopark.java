package com.yuanluesoft.land.declare.forms;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 地质公园登记(fjgtzy_geopark)
 * @author kangshiwei
 *
 */
public class Geopark extends ActionForm {
	private String parkName; //地质公园名称
	private String level; //级别
	private String location; //公园位置
	private double area; //面积
	private int approvalTime; //批准时间,年份
	private int enableTime; //开元时间,年份
	private String  geologicalHeritage; //地质遗迹
	private Timestamp created; //创建时间
	
	/**
	 * @return approvalTime
	 */
	public int getApprovalTime() {
		return approvalTime;
	}
	/**
	 * @param approvalTime 要设置的 approvalTime
	 */
	public void setApprovalTime(int approvalTime) {
		this.approvalTime = approvalTime;
	}
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
	 * @return enableTime
	 */
	public int getEnableTime() {
		return enableTime;
	}
	/**
	 * @param enableTime 要设置的 enableTime
	 */
	public void setEnableTime(int enableTime) {
		this.enableTime = enableTime;
	}
	/**
	 * @return geologicalHeritage
	 */
	public String getGeologicalHeritage() {
		return geologicalHeritage;
	}
	/**
	 * @param geologicalHeritage 要设置的 geologicalHeritage
	 */
	public void setGeologicalHeritage(String geologicalHeritage) {
		this.geologicalHeritage = geologicalHeritage;
	}
	/**
	 * @return level
	 */
	public String getLevel() {
		return level;
	}
	/**
	 * @param level 要设置的 level
	 */
	public void setLevel(String level) {
		this.level = level;
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
	 * @return parkName
	 */
	public String getParkName() {
		return parkName;
	}
	/**
	 * @param parkName 要设置的 parkName
	 */
	public void setParkName(String parkName) {
		this.parkName = parkName;
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
