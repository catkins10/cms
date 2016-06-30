package com.yuanluesoft.j2oa.reimburse.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class ChargeCategoryForm extends ActionForm {
    private String category;
	private double money;
	private String unit;

    /**
     * @return Returns the category.
     */
    public String getCategory() {
        return category;
    }
    /**
     * @param category The category to set.
     */
    public void setCategory(String category) {
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
    public String getUnit() {
        return unit;
    }
    /**
     * @param unit The unit to set.
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }
}