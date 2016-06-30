package com.yuanluesoft.traffic.expressway.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Query extends ActionForm {
	private String entry; //人口
	private String exit; //出口
	private double space; //距离
	private String road; //路线
	private double type1Price; //车辆一型价格
	private double type2Price; //车辆二型价格
	private double type3Price; //车辆三型价格
	private double type4Price; //车辆四型价格
	private double type5Price; //车辆五型价格
	
	/**
	 * @return the entry
	 */
	public String getEntry() {
		return entry;
	}
	/**
	 * @param entry the entry to set
	 */
	public void setEntry(String entry) {
		this.entry = entry;
	}
	/**
	 * @return the exit
	 */
	public String getExit() {
		return exit;
	}
	/**
	 * @param exit the exit to set
	 */
	public void setExit(String exit) {
		this.exit = exit;
	}
	/**
	 * @return the space
	 */
	public double getSpace() {
		return space;
	}
	/**
	 * @param space the space to set
	 */
	public void setSpace(double space) {
		this.space = space;
	}
	/**
	 * @return the type1Price
	 */
	public double getType1Price() {
		return type1Price;
	}
	/**
	 * @param type1Price the type1Price to set
	 */
	public void setType1Price(double type1Price) {
		this.type1Price = type1Price;
	}
	/**
	 * @return the type2Price
	 */
	public double getType2Price() {
		return type2Price;
	}
	/**
	 * @param type2Price the type2Price to set
	 */
	public void setType2Price(double type2Price) {
		this.type2Price = type2Price;
	}
	/**
	 * @return the type3Price
	 */
	public double getType3Price() {
		return type3Price;
	}
	/**
	 * @param type3Price the type3Price to set
	 */
	public void setType3Price(double type3Price) {
		this.type3Price = type3Price;
	}
	/**
	 * @return the type4Price
	 */
	public double getType4Price() {
		return type4Price;
	}
	/**
	 * @param type4Price the type4Price to set
	 */
	public void setType4Price(double type4Price) {
		this.type4Price = type4Price;
	}
	/**
	 * @return the type5Price
	 */
	public double getType5Price() {
		return type5Price;
	}
	/**
	 * @param type5Price the type5Price to set
	 */
	public void setType5Price(double type5Price) {
		this.type5Price = type5Price;
	}
	/**
	 * @return the road
	 */
	public String getRoad() {
		return road;
	}
	/**
	 * @param road the road to set
	 */
	public void setRoad(String road) {
		this.road = road;
	}
}