package com.yuanluesoft.message.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

public class Personinfo extends Record{
	private String name;	//姓名
	private String sex;	//性别
	private int age;	//年龄
	private Timestamp birthday;	//出生日期
	private String phone;	//联系电话
	private String email;	//邮箱
	private String address;	//家庭住址
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
	
	public Timestamp getBirthday() {
		return birthday;
	}
	public void setBirthday(Timestamp birthday) {
		this.birthday = birthday;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
