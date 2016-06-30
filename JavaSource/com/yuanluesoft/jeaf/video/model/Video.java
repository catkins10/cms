package com.yuanluesoft.jeaf.video.model;

import java.io.Serializable;

import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Video extends Attachment implements Serializable {
	private String videoFormat; //视频格式
	private double videoBitrate; //视频比特率
	private double videoFps; //帧频
	private int videoWidth; //视频宽度
	private int videoHeight; //视频高度
	private double videoLength; //视频长度,秒为单位
	private double audioBitrate; //音频码率
	private double audioFreq; //音频采样率
	private String url; //视频URL
	private String previewImageUrl; //预览图URL
	
	/**
	 * 获取视频时间
	 * @return
	 */
	public String getVideoTime() {
		int hour = (int)videoLength/3600;
		int minute = (int)((videoLength - hour*3600)/60);
		int second = (int)videoLength % 60;
		return StringUtils.formatNumber(hour, 2, false) + ":" + StringUtils.formatNumber(minute, 2, false) + ":" + StringUtils.formatNumber(second, 2, false);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.model.Attachment#getDescription()
	 */
	public String getDescription() {
		return getName() + "\r\n" +
		      "尺寸：" + videoWidth +"×" + videoHeight + "\r\n" +
		      "大小：" + getFileSize() + "\r\n" +
		      "长度：" + getVideoTime();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.model.Attachment#getUrlAttachment()
	 */
	public String getUrlAttachment() {
		return url;
	}
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.model.Attachment#getUrlInline()
	 */
	public String getUrlInline() {
		return url;
	}
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.model.Attachment#setUrlAttachment(java.lang.String)
	 */
	public void setUrlAttachment(String urlAttachment) {
		this.url = urlAttachment;
	}
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.model.Attachment#setUrlInline(java.lang.String)
	 */
	public void setUrlInline(String urlInline) {
		this.url = urlInline;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the videoBitrate
	 */
	public double getVideoBitrate() {
		return videoBitrate;
	}
	/**
	 * @param videoBitrate the videoBitrate to set
	 */
	public void setVideoBitrate(double videoBitrate) {
		this.videoBitrate = videoBitrate;
	}
	/**
	 * @return the videoFormat
	 */
	public String getVideoFormat() {
		return videoFormat;
	}
	/**
	 * @param videoFormat the videoFormat to set
	 */
	public void setVideoFormat(String videoFormat) {
		this.videoFormat = videoFormat;
	}
	/**
	 * @return the videoFps
	 */
	public double getVideoFps() {
		return videoFps;
	}
	/**
	 * @param videoFps the videoFps to set
	 */
	public void setVideoFps(double videoFps) {
		this.videoFps = videoFps;
	}
	/**
	 * @return the videoHeight
	 */
	public int getVideoHeight() {
		return videoHeight;
	}
	/**
	 * @param videoHeight the videoHeight to set
	 */
	public void setVideoHeight(int videoHeight) {
		this.videoHeight = videoHeight;
	}
	/**
	 * @return the videoWidth
	 */
	public int getVideoWidth() {
		return videoWidth;
	}
	/**
	 * @param videoWidth the videoWidth to set
	 */
	public void setVideoWidth(int videoWidth) {
		this.videoWidth = videoWidth;
	}
	/**
	 * @return the videoLength
	 */
	public double getVideoLength() {
		return videoLength;
	}
	/**
	 * @param videoLength the videoLength to set
	 */
	public void setVideoLength(double videoLength) {
		this.videoLength = videoLength;
	}

	/**
	 * @return the previewImageUrl
	 */
	public String getPreviewImageUrl() {
		return previewImageUrl;
	}

	/**
	 * @param previewImageUrl the previewImageUrl to set
	 */
	public void setPreviewImageUrl(String previewImageUrl) {
		this.previewImageUrl = previewImageUrl;
	}

	/**
	 * @return the audioBitrate
	 */
	public double getAudioBitrate() {
		return audioBitrate;
	}

	/**
	 * @param audioBitrate the audioBitrate to set
	 */
	public void setAudioBitrate(double audioBitrate) {
		this.audioBitrate = audioBitrate;
	}

	/**
	 * @return the audioFreq
	 */
	public double getAudioFreq() {
		return audioFreq;
	}

	/**
	 * @param audioFreq the audioFreq to set
	 */
	public void setAudioFreq(double audioFreq) {
		this.audioFreq = audioFreq;
	}
}
