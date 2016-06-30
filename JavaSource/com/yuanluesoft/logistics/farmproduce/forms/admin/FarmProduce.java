package com.yuanluesoft.logistics.farmproduce.forms.admin;

import java.sql.Timestamp;
import java.text.DecimalFormat;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class FarmProduce extends ActionForm {
	private String name; //农产品名称
	private double quantity; //数量
	private String unit; //单位,吨,米,立方米,车,方,件,个,台,包,辆,箱,瓶,套,盒,支,只
	private double freight; //单价,0表示面议
	private String address; //地点
	private String linkman; //联系人
	private String linkmanTel; //联系电话
	private long creatorId; //登记人ID
	private String creator; //登记人
	private Timestamp created; //登记时间
	private String remark; //备注
	
	/**
	 * 输出单价
	 * @return
	 */
	public String getFreightTitle() {
		return freight==0 ? "面议" :  new DecimalFormat("#.##").format(freight) + "元/" + unit;
	}
	
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
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the freight
	 */
	public double getFreight() {
		return freight;
	}
	/**
	 * @param freight the freight to set
	 */
	public void setFreight(double freight) {
		this.freight = freight;
	}
	/**
	 * @return the linkman
	 */
	public String getLinkman() {
		return linkman;
	}
	/**
	 * @param linkman the linkman to set
	 */
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	/**
	 * @return the linkmanTel
	 */
	public String getLinkmanTel() {
		return linkmanTel;
	}
	/**
	 * @param linkmanTel the linkmanTel to set
	 */
	public void setLinkmanTel(String linkmanTel) {
		this.linkmanTel = linkmanTel;
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
	 * @return the quantity
	 */
	public double getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(double quantity) {
		this.quantity = quantity;
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
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
}