package com.yuanluesoft.aic.barcode.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 产品条码(barcode_barcode)
 * @author linchuan
 *
 */
public class Barcode extends Record {
	private long companyId; //厂商ID
	private String barcode; //条码,企业产品条形码包括前缀码的前7-9位为厂商识别码，从产商信息中直接读取，不用重复录入。厂商识别码后到第12位为产品代码，第13位为校验码，通过固定的算法得出，用于校验前12位数据录入正确，算法附在补充说明中
	private BarcodeCompany company; //厂商
	
	/**
	 * @return the barcode
	 */
	public String getBarcode() {
		return barcode;
	}
	/**
	 * @param barcode the barcode to set
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	/**
	 * @return the companyId
	 */
	public long getCompanyId() {
		return companyId;
	}
	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	/**
	 * @return the company
	 */
	public BarcodeCompany getCompany() {
		return company;
	}
	/**
	 * @param company the company to set
	 */
	public void setCompany(BarcodeCompany company) {
		this.company = company;
	}
}