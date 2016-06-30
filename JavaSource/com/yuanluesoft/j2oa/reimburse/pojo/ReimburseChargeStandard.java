/*
 * Created on 2006-6-15
 *
 */
package com.yuanluesoft.j2oa.reimburse.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 费用标准(reimburse_charge_standard)
 * @author linchuan
 */
public class ReimburseChargeStandard extends Record {
	private String userName; //用户名称
	private long userId; //用户ID
	private long chargeCategoryId; //费用类别ID
	private double money; //金额
	private ReimburseChargeCategory chargeCategory;
   
    /**
     * @return Returns the chargeCategoryId.
     */
    public long getChargeCategoryId() {
        return chargeCategoryId;
    }
    /**
     * @param chargeCategoryId The chargeCategoryId to set.
     */
    public void setChargeCategoryId(long chargeCategoryId) {
        this.chargeCategoryId = chargeCategoryId;
    }
    /**
     * @return Returns the userId.
     */
    public long getUserId() {
        return userId;
    }
    /**
     * @param userId The userId to set.
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }
    /**
     * @return Returns the money.
     */
    public double getMoney() {
        return money;
    }
    /**
     * @param money The money to set.
     */
    public void setMoney(double money) {
        this.money = money;
    }
    /**
     * @return Returns the userName.
     */
    public java.lang.String getUserName() {
        return userName;
    }
    /**
     * @param userName The userName to set.
     */
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }
    /**
     * @return Returns the chargeCategory.
     */
    public ReimburseChargeCategory getChargeCategory() {
        return chargeCategory;
    }
    /**
     * @param chargeCategory The chargeCategory to set.
     */
    public void setChargeCategory(ReimburseChargeCategory chargeCategory) {
        this.chargeCategory = chargeCategory;
    }
}
