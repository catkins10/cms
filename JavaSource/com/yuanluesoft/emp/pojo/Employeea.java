package com.yuanluesoft.emp.pojo;

import java.sql.Date;

import com.yuanluesoft.jeaf.database.Record;

public class Employeea extends Record {
	private String empname;
	private String sex;
	private long departId;	//所在部门id
	private int age;
	private Date birthday;
	private String phone;
	private String address;
	private String email;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public long getDepartId() {
		return departId;
	}
	public void setDepartId(long departId) {
		this.departId = departId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmpname() {
		return empname;
	}
	public void setEmpname(String empname) {
		this.empname = empname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
}
