package com.yuanluesoft.jeaf.image.model;

/**
 * 水印
 * @author linchuan
 *
 */
public class WaterMark {
	private String waterMarkImagePath;
	private String waterMarkAlign; //水印显示位置
	private int waterMarkXMargin; //水印图片水平边距
	private int waterMarkYMargin; //水印图片垂直边距
	
	public WaterMark(String waterMarkImagePath, String waterMarkAlign, int waterMarkXMargin, int waterMarkYMargin) {
		super();
		this.waterMarkImagePath = waterMarkImagePath;
		this.waterMarkAlign = waterMarkAlign;
		this.waterMarkXMargin = waterMarkXMargin;
		this.waterMarkYMargin = waterMarkYMargin;
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
	 * @return the waterMarkImagePath
	 */
	public String getWaterMarkImagePath() {
		return waterMarkImagePath;
	}
	/**
	 * @param waterMarkImagePath the waterMarkImagePath to set
	 */
	public void setWaterMarkImagePath(String waterMarkImagePath) {
		this.waterMarkImagePath = waterMarkImagePath;
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
}
