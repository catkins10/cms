/*
 * Created on 2006-6-15
 *
 */
package com.yuanluesoft.j2oa.reimburse.pojo;

import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 费用类别(reimburse_charge_category)
 * @author linchuan
 *
 */
public class ReimburseChargeCategory extends Record {
	private String category; //费用类别
	private double money; //默认标准
	private String unit; //单位
	private Set standards;
    
    /**
     * @return Returns the category.
     */
    public java.lang.String getCategory() {
        return category;
    }
    /**
     * @param category The category to set.
     */
    public void setCategory(java.lang.String category) {
        this.category = category;
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
     * @return Returns the unit.
     */
    public java.lang.String getUnit() {
        return unit;
    }
    /**
     * @param unit The unit to set.
     */
    public void setUnit(java.lang.String unit) {
        this.unit = unit;
    }
    /**
     * @return Returns the standards.
     */
    public java.util.Set getStandards() {
        return standards;
    }
    /**
     * @param standards The standards to set.
     */
    public void setStandards(java.util.Set standards) {
        this.standards = standards;
    }
}
