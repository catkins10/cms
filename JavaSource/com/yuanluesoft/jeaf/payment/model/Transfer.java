package com.yuanluesoft.jeaf.payment.model;

/**
 * 转账
 * @author linchuan
 *
 */
public class Transfer {
	private String fromUnit; //汇出单位 文本格式,南平中心为“南平市招标投标服务中心”，各县市不同
	private String fromUnitAcount; //汇出帐号 长数值格式，应生成数字串，不能为科学计数法，南平中心为“35001676107052505220”，各县市不同
	private String fromBankFirstCode; //汇出行一级分行 数值格式，不能为科学计数法，南平及所辖各县市均为“350000000”
	private String toUnitAccount; //收款帐号 长数值格式，应生成数字串，不能为科学计数法，数据从单位网银卸出的流水中提取
	private String toUnit; //收款单位 文本格式 数据从单位网银卸出的流水中提取
	private String toBankFirstCode; //收款单位一级分行 数值格式，可空（不是“0”）
	private String toUnitBank; //收款单位开户行 文本格式，必填项，数据无法从单位网银提取，可从投标单位的备案库中查找提取。投标单位备案库：初次由建行根据历史参加投标的单位数据流水，从建行系统查询其开户行，填写完整后，导入投标单位的备案库中。以后新增的投标单位均由建行工作人员查询其开户行后发给中心工作，添加入投标单位的备案库中。
	private String toBankCode; //收款单位联行号 数值格式，可空（不是“0”）
	private String toUnitOrgCode; //收款单位机构号 数值格式，可空（不是“0”）
	private double money; //金额 长数值格式，应生成数字串，不能为科学计数法，数据从单位网银卸出的流水中提取
	private String currency = "人民币"; //数值格式，为“1”，代表人民币
	private String uses; //用途 文本格式，由系统根据标书名称自行填写
	private Object relationRecord; //相关记录
	//转账结果
	private boolean success = false; //是否成功
	private String error; //失败原因
	
	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	/**
	 * @return the fromBankFirstCode
	 */
	public String getFromBankFirstCode() {
		return fromBankFirstCode;
	}
	/**
	 * @param fromBankFirstCode the fromBankFirstCode to set
	 */
	public void setFromBankFirstCode(String fromBankFirstCode) {
		this.fromBankFirstCode = fromBankFirstCode;
	}
	/**
	 * @return the fromUnit
	 */
	public String getFromUnit() {
		return fromUnit;
	}
	/**
	 * @param fromUnit the fromUnit to set
	 */
	public void setFromUnit(String fromUnit) {
		this.fromUnit = fromUnit;
	}
	/**
	 * @return the fromUnitAcount
	 */
	public String getFromUnitAcount() {
		return fromUnitAcount;
	}
	/**
	 * @param fromUnitAcount the fromUnitAcount to set
	 */
	public void setFromUnitAcount(String fromUnitAcount) {
		this.fromUnitAcount = fromUnitAcount;
	}
	/**
	 * @return the money
	 */
	public double getMoney() {
		return money;
	}
	/**
	 * @param money the money to set
	 */
	public void setMoney(double money) {
		this.money = money;
	}
	/**
	 * @return the toBankCode
	 */
	public String getToBankCode() {
		return toBankCode;
	}
	/**
	 * @param toBankCode the toBankCode to set
	 */
	public void setToBankCode(String toBankCode) {
		this.toBankCode = toBankCode;
	}
	/**
	 * @return the toBankFirstCode
	 */
	public String getToBankFirstCode() {
		return toBankFirstCode;
	}
	/**
	 * @param toBankFirstCode the toBankFirstCode to set
	 */
	public void setToBankFirstCode(String toBankFirstCode) {
		this.toBankFirstCode = toBankFirstCode;
	}
	/**
	 * @return the toUnit
	 */
	public String getToUnit() {
		return toUnit;
	}
	/**
	 * @param toUnit the toUnit to set
	 */
	public void setToUnit(String toUnit) {
		this.toUnit = toUnit;
	}
	/**
	 * @return the toUnitAccount
	 */
	public String getToUnitAccount() {
		return toUnitAccount;
	}
	/**
	 * @param toUnitAccount the toUnitAccount to set
	 */
	public void setToUnitAccount(String toUnitAccount) {
		this.toUnitAccount = toUnitAccount;
	}
	/**
	 * @return the toUnitBank
	 */
	public String getToUnitBank() {
		return toUnitBank;
	}
	/**
	 * @param toUnitBank the toUnitBank to set
	 */
	public void setToUnitBank(String toUnitBank) {
		this.toUnitBank = toUnitBank;
	}
	/**
	 * @return the toUnitOrgCode
	 */
	public String getToUnitOrgCode() {
		return toUnitOrgCode;
	}
	/**
	 * @param toUnitOrgCode the toUnitOrgCode to set
	 */
	public void setToUnitOrgCode(String toUnitOrgCode) {
		this.toUnitOrgCode = toUnitOrgCode;
	}
	/**
	 * @return the uses
	 */
	public String getUses() {
		return uses;
	}
	/**
	 * @param uses the uses to set
	 */
	public void setUses(String uses) {
		this.uses = uses;
	}
	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}
	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}
	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}
	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
	/**
	 * @return the relationRecord
	 */
	public Object getRelationRecord() {
		return relationRecord;
	}
	/**
	 * @param relationRecord the relationRecord to set
	 */
	public void setRelationRecord(Object relationRecord) {
		this.relationRecord = relationRecord;
	}
}