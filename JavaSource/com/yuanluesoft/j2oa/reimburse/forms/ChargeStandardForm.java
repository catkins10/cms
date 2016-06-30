package com.yuanluesoft.j2oa.reimburse.forms;

import java.util.List;

import com.yuanluesoft.jeaf.form.ActionForm;


/**
 * 
 * @author linchuan
 *
 */
public class ChargeStandardForm extends ActionForm {
    private long chargeCategoryId;
	private long userId;
	private double money;
	private java.lang.String userName;
	private List chargeCategories; //费用类别列表
	private String selectedCategory; //选中的类别
	private String unit; //单位
		
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
     * @return Returns the chargeCategories.
     */
    public List getChargeCategories() {
        return chargeCategories;
    }
    /**
     * @param chargeCategories The chargeCategories to set.
     */
    public void setChargeCategories(List chargeCategories) {
        this.chargeCategories = chargeCategories;
    }
    /**
     * @return Returns the unit.
     */
    public String getUnit() {
        return unit;
    }
    /**
     * @param unit The unit to set.
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }
    /**
     * @return Returns the selectedCategory.
     */
    public String getSelectedCategory() {
        return selectedCategory;
    }
    /**
     * @param selectedCategory The selectedCategory to set.
     */
    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }
}