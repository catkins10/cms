package com.yuanluesoft.cms.sitemanage.forms;


/**
 * 
 * 
 * @author linchuan
 *
 */
public class Site extends WebDirectory {
	private String hostName; //主机名,仅对站点有效
	private char useSiteTemplate = '0'; //当文章不属于本站的时,是否使用本站的文章模板
	private String waterMarkAlign; //水印显示位置
	private int waterMarkXMargin; //水印图片水平边距
	private int waterMarkYMargin; //水印图片垂直边距
	private char isInternal = '0'; //是否内部网站
	private long ownerUnitId; //隶属单位ID
	private String ownerUnitName; //隶属单位名称
	
	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}
	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	/**
	 * @return the useSiteTemplate
	 */
	public char getUseSiteTemplate() {
		return useSiteTemplate;
	}
	/**
	 * @param useSiteTemplate the useSiteTemplate to set
	 */
	public void setUseSiteTemplate(char useSiteTemplate) {
		this.useSiteTemplate = useSiteTemplate;
	}
	/**
	 * @return the waterMarkAlign
	 */
	public String getWaterMarkAlign() {
		return waterMarkAlign;
	}
	/**
	 * @param waterMarkAlign the waterMarkAlign to set
	 */
	public void setWaterMarkAlign(String waterMarkAlign) {
		this.waterMarkAlign = waterMarkAlign;
	}
	/**
	 * @return the waterMarkXMargin
	 */
	public int getWaterMarkXMargin() {
		return waterMarkXMargin;
	}
	/**
	 * @param waterMarkXMargin the waterMarkXMargin to set
	 */
	public void setWaterMarkXMargin(int waterMarkXMargin) {
		this.waterMarkXMargin = waterMarkXMargin;
	}
	/**
	 * @return the waterMarkYMargin
	 */
	public int getWaterMarkYMargin() {
		return waterMarkYMargin;
	}
	/**
	 * @param waterMarkYMargin the waterMarkYMargin to set
	 */
	public void setWaterMarkYMargin(int waterMarkYMargin) {
		this.waterMarkYMargin = waterMarkYMargin;
	}
	/**
	 * @return the isInternal
	 */
	public char getIsInternal() {
		return isInternal;
	}
	/**
	 * @param isInternal the isInternal to set
	 */
	public void setIsInternal(char isInternal) {
		this.isInternal = isInternal;
	}
	/**
	 * @return the ownerUnitId
	 */
	public long getOwnerUnitId() {
		return ownerUnitId;
	}
	/**
	 * @param ownerUnitId the ownerUnitId to set
	 */
	public void setOwnerUnitId(long ownerUnitId) {
		this.ownerUnitId = ownerUnitId;
	}
	/**
	 * @return the ownerUnitName
	 */
	public String getOwnerUnitName() {
		return ownerUnitName;
	}
	/**
	 * @param ownerUnitName the ownerUnitName to set
	 */
	public void setOwnerUnitName(String ownerUnitName) {
		this.ownerUnitName = ownerUnitName;
	}
}