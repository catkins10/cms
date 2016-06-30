package com.yuanluesoft.jeaf.business.model.parameter;

/**
 * 
 * @author linchuan
 *
 */
public class ImageParameter extends AttachmentParameter {
	private int maxSaveSize; //最大存储字节数,超出以后自动裁剪
	private int imageClipWidth = 0; //裁剪后的图片宽度,默认为800
	private int imageClipHeight = 0; //裁剪后的图片高度,默认为600
	private boolean clipEnabled = false; //图片裁剪时是否允许切除图片,默认false
	private double maxMegaPixels = 8.0; //允许上传的最大像素,以百万为单位,默认8.0,超出后图片自动删除
	
	public ImageParameter() {
		super();
		setMaxUploadSize(1000000); //默认1000000(1M)
	}
	
	/**
	 * @return the clipEnabled
	 */
	public boolean isClipEnabled() {
		return clipEnabled;
	}
	/**
	 * @param clipEnabled the clipEnabled to set
	 */
	public void setClipEnabled(boolean clipEnabled) {
		this.clipEnabled = clipEnabled;
	}
	/**
	 * @return the imageClipHeight
	 */
	public int getImageClipHeight() {
		return imageClipHeight;
	}
	/**
	 * @param imageClipHeight the imageClipHeight to set
	 */
	public void setImageClipHeight(int imageClipHeight) {
		this.imageClipHeight = imageClipHeight;
	}
	/**
	 * @return the imageClipWidth
	 */
	public int getImageClipWidth() {
		return imageClipWidth;
	}
	/**
	 * @param imageClipWidth the imageClipWidth to set
	 */
	public void setImageClipWidth(int imageClipWidth) {
		this.imageClipWidth = imageClipWidth;
	}
	/**
	 * @return the maxMegaPixels
	 */
	public double getMaxMegaPixels() {
		return maxMegaPixels;
	}
	/**
	 * @param maxMegaPixels the maxMegaPixels to set
	 */
	public void setMaxMegaPixels(double maxMegaPixels) {
		this.maxMegaPixels = maxMegaPixels;
	}
	/**
	 * @return the maxSaveSize
	 */
	public int getMaxSaveSize() {
		return maxSaveSize;
	}
	/**
	 * @param maxSaveSize the maxSaveSize to set
	 */
	public void setMaxSaveSize(int maxSaveSize) {
		this.maxSaveSize = maxSaveSize;
	}
}