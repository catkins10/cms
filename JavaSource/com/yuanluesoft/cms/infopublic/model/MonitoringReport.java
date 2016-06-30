package com.yuanluesoft.cms.infopublic.model;

/**
 * 监察报表
 * @author linchuan
 *
 */
public class MonitoringReport {
	private int guideVisits; //政府信息公开指南
	private int publicDirectoryVisits; //政府信息公开目录
	private int publicRequestVisits; //依申请公开
	private int reportVisits; //政府信息公开年度报告
	private int lawsVisits; //政府信息公开制度规定
	private int publicOpinionVisits; //政府信息公开意见箱
	
	/**
	 * @return the guideVisits
	 */
	public int getGuideVisits() {
		return guideVisits;
	}
	/**
	 * @param guideVisits the guideVisits to set
	 */
	public void setGuideVisits(int guideVisits) {
		this.guideVisits = guideVisits;
	}
	/**
	 * @return the lawsVisits
	 */
	public int getLawsVisits() {
		return lawsVisits;
	}
	/**
	 * @param lawsVisits the lawsVisits to set
	 */
	public void setLawsVisits(int lawsVisits) {
		this.lawsVisits = lawsVisits;
	}
	/**
	 * @return the publicDirectoryVisits
	 */
	public int getPublicDirectoryVisits() {
		return publicDirectoryVisits;
	}
	/**
	 * @param publicDirectoryVisits the publicDirectoryVisits to set
	 */
	public void setPublicDirectoryVisits(int publicDirectoryVisits) {
		this.publicDirectoryVisits = publicDirectoryVisits;
	}
	/**
	 * @return the publicOpinionVisits
	 */
	public int getPublicOpinionVisits() {
		return publicOpinionVisits;
	}
	/**
	 * @param publicOpinionVisits the publicOpinionVisits to set
	 */
	public void setPublicOpinionVisits(int publicOpinionVisits) {
		this.publicOpinionVisits = publicOpinionVisits;
	}
	/**
	 * @return the publicRequestVisits
	 */
	public int getPublicRequestVisits() {
		return publicRequestVisits;
	}
	/**
	 * @param publicRequestVisits the publicRequestVisits to set
	 */
	public void setPublicRequestVisits(int publicRequestVisits) {
		this.publicRequestVisits = publicRequestVisits;
	}
	/**
	 * @return the reportVisits
	 */
	public int getReportVisits() {
		return reportVisits;
	}
	/**
	 * @param reportVisits the reportVisits to set
	 */
	public void setReportVisits(int reportVisits) {
		this.reportVisits = reportVisits;
	}
}