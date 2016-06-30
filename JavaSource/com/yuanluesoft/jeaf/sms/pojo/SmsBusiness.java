package com.yuanluesoft.jeaf.sms.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 短信服务:业务配置(sms_business)
 * @author linchuan
 *
 */
public class SmsBusiness extends Record {
	private String name; //业务名称,如:农政通、党务村务、短信订阅、系统通知，其中短信订阅、系统通知是系统预设类型
	private int chargeMode; //默认的计费方式,固定费用/按条计费
	private double price; //默认的价格
	private double discount; //默认的折扣,如:9.5,默认:10
	private String postfix; //附加信息格式,如:[<单位名称>农政通]
	
	/**
	 * @return the chargeMode
	 */
	public int getChargeMode() {
		return chargeMode;
	}
	/**
	 * @param chargeMode the chargeMode to set
	 */
	public void setChargeMode(int chargeMode) {
		this.chargeMode = chargeMode;
	}
	/**
	 * @return the discount
	 */
	public double getDiscount() {
		return discount;
	}
	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(double discount) {
		this.discount = discount;
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
	 * @return the postfix
	 */
	public String getPostfix() {
		return postfix;
	}
	/**
	 * @param postfix the postfix to set
	 */
	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
}