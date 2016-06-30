package com.yuanluesoft.customise.complaint.pojo;

import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;
import java.sql.Date;
import java.sql.Timestamp;

/**
  * 投诉单
  * @author linchuan
  *
  */
public class CustomiseComplaint extends WorkflowData {
	private String TEST; //TEST
	private Date complaintTime; //投诉时间
	private int workDay; //办理时间
	private String unit; //投诉人单位
	private String name; //投诉人姓名
	private String address; //地点
	private String returnContent; //回访内容
	private String returnPerson; //回访人
	private Timestamp returnTime; //回访时间
	private String a; //a
	private String b; //b

	/**
	  * @return the TEST
	  */
	public String getTEST() {
		return TEST;
	}

	/**
	  * @param TEST the TEST to set
	  */
	public void setTEST(String TEST) {
		this.TEST = TEST;
	}

	/**
	  * @return the complaintTime
	  */
	public Date getComplaintTime() {
		return complaintTime;
	}

	/**
	  * @param complaintTime the complaintTime to set
	  */
	public void setComplaintTime(Date complaintTime) {
		this.complaintTime = complaintTime;
	}

	/**
	  * @return the workDay
	  */
	public int getWorkDay() {
		return workDay;
	}

	/**
	  * @param workDay the workDay to set
	  */
	public void setWorkDay(int workDay) {
		this.workDay = workDay;
	}

	/**
	  * @return the unit
	  */
	public String getUnit() {
		return unit;
	}

	/**
	  * @param unit the unit to set
	  */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	  * @return the name
	  */
	public String getName() {
		return name;
	}

	/**
	  * @param name the name to set
	  */
	public void setName(String name) {
		this.name = name;
	}

	/**
	  * @return the address
	  */
	public String getAddress() {
		return address;
	}

	/**
	  * @param address the address to set
	  */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	  * @return the returnContent
	  */
	public String getReturnContent() {
		return returnContent;
	}

	/**
	  * @param returnContent the returnContent to set
	  */
	public void setReturnContent(String returnContent) {
		this.returnContent = returnContent;
	}

	/**
	  * @return the returnPerson
	  */
	public String getReturnPerson() {
		return returnPerson;
	}

	/**
	  * @param returnPerson the returnPerson to set
	  */
	public void setReturnPerson(String returnPerson) {
		this.returnPerson = returnPerson;
	}

	/**
	  * @return the returnTime
	  */
	public Timestamp getReturnTime() {
		return returnTime;
	}

	/**
	  * @param returnTime the returnTime to set
	  */
	public void setReturnTime(Timestamp returnTime) {
		this.returnTime = returnTime;
	}

	/**
	  * @return the a
	  */
	public String getA() {
		return a;
	}

	/**
	  * @param a the a to set
	  */
	public void setA(String a) {
		this.a = a;
	}

	/**
	  * @return the b
	  */
	public String getB() {
		return b;
	}

	/**
	  * @param b the b to set
	  */
	public void setB(String b) {
		this.b = b;
	}
}