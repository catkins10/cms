package com.yuanluesoft.bidding.project.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 资料清单配置:资料条目(bidding_project_file_item)
 * @author linchuan
 *
 */
public class BiddingProjectFileItem extends Record {
	private long configId; //配置ID
	private String fileType; //资料类型,前期备案资料/招标投标档案
	private String fileCategory; //资料分类,如:第一阶段，前期准备
	private double sn; //序号
	private String name; //名称
	private String remark; //备注
	/**
	 * @return the configId
	 */
	public long getConfigId() {
		return configId;
	}
	/**
	 * @param configId the configId to set
	 */
	public void setConfigId(long configId) {
		this.configId = configId;
	}
	/**
	 * @return the fileCategory
	 */
	public String getFileCategory() {
		return fileCategory;
	}
	/**
	 * @param fileCategory the fileCategory to set
	 */
	public void setFileCategory(String fileCategory) {
		this.fileCategory = fileCategory;
	}
	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}
	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
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
	 * @return the sn
	 */
	public double getSn() {
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(double sn) {
		this.sn = sn;
	}
}
