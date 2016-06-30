package com.yuanluesoft.dpc.keyproject.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 项目:所属区域(keyproject_area)
 * @author linchuan
 *
 */
public class KeyProjectArea extends Record {
	private long projectId; //项目ID
	private String area; //所属区域,延平区、武夷山市、邵武市、建瓯市、建阳市、顺昌县、浦城县、光泽县、松溪县、政和县
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
	 * @return the projectId
	 */
	public long getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
}