package com.yuanluesoft.msa.seafarer.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 培训机构(msa_train_org)
 * @author linchuan
 *
 */
public class MsaTrainOrg extends Record {
	private String name; //名称
	private String address; //地址
	private String tel; //联系电话
	private String fax; //传真
	private String postalcode; //邮编
	private String legalRepresentative; //法定代表人
	private String kind; //性质
	private String licenseNumber; //许可证号
	private String summarize; //详细介绍
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
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}
	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
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
	 * @return the kind
	 */
	public String getKind() {
		return kind;
	}
	/**
	 * @param kind the kind to set
	 */
	public void setKind(String kind) {
		this.kind = kind;
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
	 * @return the postalcode
	 */
	public String getPostalcode() {
		return postalcode;
	}
	/**
	 * @param postalcode the postalcode to set
	 */
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	/**
	 * @return the summarize
	 */
	public String getSummarize() {
		return summarize;
	}
	/**
	 * @param summarize the summarize to set
	 */
	public void setSummarize(String summarize) {
		this.summarize = summarize;
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