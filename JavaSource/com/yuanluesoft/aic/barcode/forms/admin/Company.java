package com.yuanluesoft.aic.barcode.forms.admin;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Company extends ActionForm {
	private String loginName; //登录用户名
	private String password; //登录密码
	private char isHalt = '0'; //是否停用
	private String code; //厂商识别代码,企业产品条形码由13位组成，前1-3位为前缀码，表示分配代码的国家或地区，我们国家大陆地区的是690-695；包括前缀码的前7-9位为厂商识别码。如山东银鹭食品有限公司的产商识别码为：69456957
	private String name; //名称
	private String registrationNumber; //企业注册号
	private String address; //地址
	private String tel; //联系电话
	private long creatorId; //注册人ID
	private String creator; //注册人姓名
	private Timestamp created; //注册时间
	private String remark; //备注
	private Set barcodes; //条码列表
	
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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
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
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the isHalt
	 */
	public char getIsHalt() {
		return isHalt;
	}
	/**
	 * @param isHalt the isHalt to set
	 */
	public void setIsHalt(char isHalt) {
		this.isHalt = isHalt;
	}
	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}
	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the registrationNumber
	 */
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	/**
	 * @param registrationNumber the registrationNumber to set
	 */
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
	/**
	 * @return the barcodes
	 */
	public Set getBarcodes() {
		return barcodes;
	}
	/**
	 * @param barcodes the barcodes to set
	 */
	public void setBarcodes(Set barcodes) {
		this.barcodes = barcodes;
	}
}