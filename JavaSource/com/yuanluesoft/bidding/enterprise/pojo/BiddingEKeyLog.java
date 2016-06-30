package com.yuanluesoft.bidding.enterprise.pojo;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * EKey操作记录(bidding_ekey_log)
 * @author linchuan
 *
 */
public class BiddingEKeyLog extends Record {
	private long employeeId; //企业用户ID
	private String ekeyNO; //EKey编号,ekey外壳上的序号
	private String enterpriseName; //企业名称
	private String name; //用户姓名
	private String loginName; //登录用户名,在试用期限到达前或未生效时不允许重复
	private char isPermanent = '0'; //是否永久性用户
	private int tryDays; //试用有效期(天)
	private Date tryEndDate; //试用截止时间
	private String actionType; //操作类型
	private Timestamp actionTime; //操作时间
	private String transactor; //经办人
	private long transactorId; //经办人ID
	private String contractNo; //合同编号
	private String tel; //联系电话
	private double deposit; //已收押金
	private String depositBill; //押金收据号码
	private Timestamp drawTime; //领取软件
	private String damage; //损坏内容
	private double damageMoney; //已收赔偿金额
	private double saleMoney; //已收销售款
	private String saleBill; //发票号码
	private String enterpriseCert; //企业资质
	private String receiptNo; //收据号码
	private String enterpriseTransactor; //企业经办人
	private String remark; //备注
	
	/**
	 * 获取文本方式的试用截止时间
	 * @return
	 */
	public String getTryEnd() {
		return (isPermanent=='1' ? "无限制" : DateTimeUtils.formatDate(tryEndDate, null));
	}
	
	/**
	 * @return the actionTime
	 */
	public Timestamp getActionTime() {
		return actionTime;
	}
	/**
	 * @param actionTime the actionTime to set
	 */
	public void setActionTime(Timestamp actionTime) {
		this.actionTime = actionTime;
	}
	/**
	 * @return the ekeyNO
	 */
	public String getEkeyNO() {
		return ekeyNO;
	}
	/**
	 * @param ekeyNO the ekeyNO to set
	 */
	public void setEkeyNO(String ekeyNO) {
		this.ekeyNO = ekeyNO;
	}
	/**
	 * @return the enterpriseName
	 */
	public String getEnterpriseName() {
		return enterpriseName;
	}
	/**
	 * @param enterpriseName the enterpriseName to set
	 */
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	/**
	 * @return the isPermanent
	 */
	public char getIsPermanent() {
		return isPermanent;
	}
	/**
	 * @param isPermanent the isPermanent to set
	 */
	public void setIsPermanent(char isPermanent) {
		this.isPermanent = isPermanent;
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
	 * @return the tryDays
	 */
	public int getTryDays() {
		return tryDays;
	}
	/**
	 * @param tryDays the tryDays to set
	 */
	public void setTryDays(int tryDays) {
		this.tryDays = tryDays;
	}
	/**
	 * @return the actionType
	 */
	public String getActionType() {
		return actionType;
	}
	/**
	 * @param actionType the actionType to set
	 */
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	/**
	 * @return the tryEndDate
	 */
	public Date getTryEndDate() {
		return tryEndDate;
	}
	/**
	 * @param tryEndDate the tryEndDate to set
	 */
	public void setTryEndDate(Date tryEndDate) {
		this.tryEndDate = tryEndDate;
	}

	/**
	 * @return the contractNo
	 */
	public String getContractNo() {
		return contractNo;
	}

	/**
	 * @param contractNo the contractNo to set
	 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	/**
	 * @return the damage
	 */
	public String getDamage() {
		return damage;
	}

	/**
	 * @param damage the damage to set
	 */
	public void setDamage(String damage) {
		this.damage = damage;
	}

	/**
	 * @return the damageMoney
	 */
	public double getDamageMoney() {
		return damageMoney;
	}

	/**
	 * @param damageMoney the damageMoney to set
	 */
	public void setDamageMoney(double damageMoney) {
		this.damageMoney = damageMoney;
	}

	/**
	 * @return the deposit
	 */
	public double getDeposit() {
		return deposit;
	}

	/**
	 * @param deposit the deposit to set
	 */
	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}

	/**
	 * @return the depositBill
	 */
	public String getDepositBill() {
		return depositBill;
	}

	/**
	 * @param depositBill the depositBill to set
	 */
	public void setDepositBill(String depositBill) {
		this.depositBill = depositBill;
	}

	/**
	 * @return the drawTime
	 */
	public Timestamp getDrawTime() {
		return drawTime;
	}

	/**
	 * @param drawTime the drawTime to set
	 */
	public void setDrawTime(Timestamp drawTime) {
		this.drawTime = drawTime;
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
	 * @return the saleBill
	 */
	public String getSaleBill() {
		return saleBill;
	}

	/**
	 * @param saleBill the saleBill to set
	 */
	public void setSaleBill(String saleBill) {
		this.saleBill = saleBill;
	}

	/**
	 * @return the saleMoney
	 */
	public double getSaleMoney() {
		return saleMoney;
	}

	/**
	 * @param saleMoney the saleMoney to set
	 */
	public void setSaleMoney(double saleMoney) {
		this.saleMoney = saleMoney;
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
	 * @return the transactor
	 */
	public String getTransactor() {
		return transactor;
	}

	/**
	 * @param transactor the transactor to set
	 */
	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}

	/**
	 * @return the transactorId
	 */
	public long getTransactorId() {
		return transactorId;
	}

	/**
	 * @param transactorId the transactorId to set
	 */
	public void setTransactorId(long transactorId) {
		this.transactorId = transactorId;
	}

	/**
	 * @return the employeeId
	 */
	public long getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return the enterpriseCert
	 */
	public String getEnterpriseCert() {
		return enterpriseCert;
	}

	/**
	 * @param enterpriseCert the enterpriseCert to set
	 */
	public void setEnterpriseCert(String enterpriseCert) {
		this.enterpriseCert = enterpriseCert;
	}

	/**
	 * @return the receiptNo
	 */
	public String getReceiptNo() {
		return receiptNo;
	}

	/**
	 * @param receiptNo the receiptNo to set
	 */
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	/**
	 * @return the enterpriseTransactor
	 */
	public String getEnterpriseTransactor() {
		return enterpriseTransactor;
	}

	/**
	 * @param enterpriseTransactor the enterpriseTransactor to set
	 */
	public void setEnterpriseTransactor(String enterpriseTransactor) {
		this.enterpriseTransactor = enterpriseTransactor;
	}
}
