package com.yuanluesoft.msa.seafarer.forms.admin;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class ServiceOrg extends ActionForm {
	private String name; //机构名称
	private String serviceContent; //服务内容
	private String address; //联系地址
	private String legalRepresentative; //法人代表
	private String tel; //联系电话
	private String category; //类型
	private String licenseNumber; //许可证号
	private long importId; //导入记录ID
	
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
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the importId
	 */
	public long getImportId() {
		return importId;
	}
	/**
	 * @param importId the importId to set
	 */
	public void setImportId(long importId) {
		this.importId = importId;
	}
	/**
	 * @return the legalRepresentative
	 */
	public String getLegalRepresentative() {
		return legalRepresentative;
	}
	/**
	 * @param legalRepresentative the legalRepresentative to set
	 */
	public void setLegalRepresentative(String legalRepresentative) {
		this.legalRepresentative = legalRepresentative;
	}
	/**
	 * @return the licenseNumber
	 */
	public String getLicenseNumber() {
		return licenseNumber;
	}
	/**
	 * @param licenseNumber the licenseNumber to set
	 */
	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
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
	 * @return the serviceContent
	 */
	public String getServiceContent() {
		return serviceContent;
	}
	/**
	 * @param serviceContent the serviceContent to set
	 */
	public void setServiceContent(String serviceContent) {
		this.serviceContent = serviceContent;
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
}