package com.yuanluesoft.msa.seafarer.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 海员证申办单位(msa_seafarer_passport_org)
 * @author linchuan
 *
 */
public class MsaSeafarerPassportOrg extends Record {
	private String name; //单位名称
	private String unitNo; //单位编号
	private String address; //联系地址
	private String legalRepresentative; //法人代表
	private String tel; //联系电话
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
	 * @return the unitNo
	 */
	public String getUnitNo() {
		return unitNo;
	}
	/**
	 * @param unitNo the unitNo to set
	 */
	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}
}