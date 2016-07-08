package com.yuanluesoft.educ.student.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.cms.publicservice.pojo.PublicService;

public class Stude extends PublicService {
	private String name;	//姓名
	private char sex='M';	//性别
	private String idcardNumber;	//身份证号码,即账号
	private String imageName;	//照片名称
	private String nation;	//民族
	private String studentId;	//学号
	private String department;	//系部
	private String speciality;	//专业
	private String stuclass;	//班级
	private String grade;	//年级
	private String idcardAddress;	//身份证地址
	private String houseAddress;	//家庭地址
	private long phone;	//联系电话
	private char isOurStudent = '0';	//是否我们学院学生,默认不是
	private String password;	//密码
	private String remark;	//备注
	private Timestamp created;	//创建时间
	private char isValid = '0'; //学生信息是否生效,注册完成后置1
	private char isAlter = '0'; //是否变更审批记录
	private String alterDescription;	//学生修改资料详情
	private long alterStudentId; //变更学生ID

	/**
	 * 获取状态名称
	 * @return
	 */
	public String getStatusText() {
		return isAlter=='1' ? "学生变更" : (isValid=='1' ? "学生" : "学生注册");
	}
	
	public long getAlterStudentId() {
		return alterStudentId;
	}

	public void setAlterStudentId(long alterStudentId) {
		this.alterStudentId = alterStudentId;
	}

	public char getIsAlter() {
		return isAlter;
	}

	public void setIsAlter(char isAlter) {
		this.isAlter = isAlter;
	}

	public char getIsValid() {
		return isValid;
	}

	public void setIsValid(char isValid) {
		this.isValid = isValid;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getAlterDescription() {
		return alterDescription;
	}

	public void setAlterDescription(String alterDescription) {
		this.alterDescription = alterDescription;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getHouseAddress() {
		return houseAddress;
	}

	public void setHouseAddress(String houseAddress) {
		this.houseAddress = houseAddress;
	}

	public String getIdcardAddress() {
		return idcardAddress;
	}

	public void setIdcardAddress(String idcardAddress) {
		this.idcardAddress = idcardAddress;
	}

	public String getIdcardNumber() {
		return idcardNumber;
	}

	public void setIdcardNumber(String idcardNumber) {
		this.idcardNumber = idcardNumber;
	}

	public char getIsOurStudent() {
		return isOurStudent;
	}

	public void setIsOurStudent(char isOurStudent) {
		this.isOurStudent = isOurStudent;
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getPhone() {
		return phone;
	}

	public void setPhone(long phone) {
		this.phone = phone;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public char getSex() {
		return sex;
	}

	public void setSex(char sex) {
		this.sex = sex;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getStuclass() {
		return stuclass;
	}

	public void setStuclass(String stuclass) {
		this.stuclass = stuclass;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	
	
}
