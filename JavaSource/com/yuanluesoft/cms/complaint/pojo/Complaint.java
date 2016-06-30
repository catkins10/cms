package com.yuanluesoft.cms.complaint.pojo;

import java.sql.Timestamp;

import com.yuanluesoft.cms.publicservice.pojo.PublicService;

/**
 * 诉求件(cms_complaint)
 * @author linchuan
 *
 */
public class Complaint extends PublicService {
	private String popedom; //事件辖区
	private String area; //事件地点
	private String type; //诉求类型
	private Timestamp happenTime; //事件时间
	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}
	/**
	 * @return the happenTime
	 */
	public Timestamp getHappenTime() {
		return happenTime;
	}
	/**
	 * @param happenTime the happenTime to set
	 */
	public void setHappenTime(Timestamp happenTime) {
		this.happenTime = happenTime;
	}
	/**
	 * @return the popedom
	 */
	public String getPopedom() {
		return popedom;
	}
	/**
	 * @param popedom the popedom to set
	 */
	public void setPopedom(String popedom) {
		this.popedom = popedom;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
