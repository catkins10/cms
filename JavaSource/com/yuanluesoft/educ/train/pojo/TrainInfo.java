package com.yuanluesoft.educ.train.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;

public class TrainInfo extends Record {

	private String name;	//课程名称
	private String simpleIntro;	//简介
	private char personType='S';	//培训人员,S代表学生，T代表教师，默认为学生
	private Timestamp signupStart;	//报名开始日期
	private Timestamp signupStop;	//报名截止日期
	private Timestamp trainStart;	//培训开始日期
	private Timestamp trainStop;	//培训截止日期
	private String profession;	//鉴定职业
	private String type;	//鉴定类别
	private String level;	//鉴定级别
	
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public char getPersonType() {
		return personType;
	}
	public void setPersonType(char personType) {
		this.personType = personType;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public Timestamp getSignupStart() {
		return signupStart;
	}
	public void setSignupStart(Timestamp signupStart) {
		this.signupStart = signupStart;
	}
	public Timestamp getSignupStop() {
		return signupStop;
	}
	public void setSignupStop(Timestamp signupStop) {
		this.signupStop = signupStop;
	}
	public String getSimpleIntro() {
		return simpleIntro;
	}
	public void setSimpleIntro(String simpleIntro) {
		this.simpleIntro = simpleIntro;
	}
	public Timestamp getTrainStart() {
		return trainStart;
	}
	public void setTrainStart(Timestamp trainStart) {
		this.trainStart = trainStart;
	}
	public Timestamp getTrainStop() {
		return trainStop;
	}
	public void setTrainStop(Timestamp trainStop) {
		this.trainStop = trainStop;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
