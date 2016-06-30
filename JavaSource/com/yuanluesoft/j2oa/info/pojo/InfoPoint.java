package com.yuanluesoft.j2oa.info.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 信息采编:积分和稿酬(info_point)
 * @author linchuan
 *
 */
public class InfoPoint extends Record {
	private int item; //得分项目,0/录用,1/领导批示,2/报县(区)办,3/县(区)办录用,4/县(区)领导批示,5/报市办,6/市办录用,7/市领导批示,8/报省办,9/省办录用,10/省领导批示,11/报国办,12/国办录用,13/国办领导批示
	private String magazineIds; //刊物ID列表
	private String magazineNames; //刊物名称列表
	private double point; //积分
	private double remuneration; //稿酬
	private double briefPoint; //简讯积分
	private double briefRemuneration; //简讯稿酬
	
	/**
	 * @return the briefPoint
	 */
	public double getBriefPoint() {
		return briefPoint;
	}
	/**
	 * @param briefPoint the briefPoint to set
	 */
	public void setBriefPoint(double briefPoint) {
		this.briefPoint = briefPoint;
	}
	/**
	 * @return the briefRemuneration
	 */
	public double getBriefRemuneration() {
		return briefRemuneration;
	}
	/**
	 * @param briefRemuneration the briefRemuneration to set
	 */
	public void setBriefRemuneration(double briefRemuneration) {
		this.briefRemuneration = briefRemuneration;
	}
	/**
	 * @return the item
	 */
	public int getItem() {
		return item;
	}
	/**
	 * @param item the item to set
	 */
	public void setItem(int item) {
		this.item = item;
	}
	/**
	 * @return the magazineIds
	 */
	public String getMagazineIds() {
		return magazineIds;
	}
	/**
	 * @param magazineIds the magazineIds to set
	 */
	public void setMagazineIds(String magazineIds) {
		this.magazineIds = magazineIds;
	}
	/**
	 * @return the magazineNames
	 */
	public String getMagazineNames() {
		return magazineNames;
	}
	/**
	 * @param magazineNames the magazineNames to set
	 */
	public void setMagazineNames(String magazineNames) {
		this.magazineNames = magazineNames;
	}
	/**
	 * @return the point
	 */
	public double getPoint() {
		return point;
	}
	/**
	 * @param point the point to set
	 */
	public void setPoint(double point) {
		this.point = point;
	}
	/**
	 * @return the remuneration
	 */
	public double getRemuneration() {
		return remuneration;
	}
	/**
	 * @param remuneration the remuneration to set
	 */
	public void setRemuneration(double remuneration) {
		this.remuneration = remuneration;
	}
}