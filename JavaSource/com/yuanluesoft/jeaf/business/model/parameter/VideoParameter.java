package com.yuanluesoft.jeaf.business.model.parameter;

/**
 * 
 * @author linchuan
 *
 */
public class VideoParameter extends AttachmentParameter {
	private boolean convertMp4 = true; //是否转换为mp4格式,默认为true
	private int videoBitrate = 473; //视频比特率,默认为473
	private int videoFps = 25; //帧频,默认为25
	private int videoWidth = 400; //视频宽度,默认400,0表示不调整
	private int videoHeight = 300; //视频高度,默认300,0表示不调整
	private int audioBitrate = 56; //音频码率,单位kbps,默认56
	private int audioFreq = 22050; //音频采样率,默认22050
	
	public VideoParameter() {
		super();
		setMaxUploadSize(100000000); //默认100000000(100M)
	}
	
	/**
	 * @return the audioBitrate
	 */
	public int getAudioBitrate() {
		return audioBitrate;
	}
	/**
	 * @param audioBitrate the audioBitrate to set
	 */
	public void setAudioBitrate(int audioBitrate) {
		this.audioBitrate = audioBitrate;
	}
	/**
	 * @return the audioFreq
	 */
	public int getAudioFreq() {
		return audioFreq;
	}
	/**
	 * @param audioFreq the audioFreq to set
	 */
	public void setAudioFreq(int audioFreq) {
		this.audioFreq = audioFreq;
	}
	/**
	 * @return the videoBitrate
	 */
	public int getVideoBitrate() {
		return videoBitrate;
	}
	/**
	 * @param videoBitrate the videoBitrate to set
	 */
	public void setVideoBitrate(int videoBitrate) {
		this.videoBitrate = videoBitrate;
	}
	/**
	 * @return the videoFps
	 */
	public int getVideoFps() {
		return videoFps;
	}
	/**
	 * @param videoFps the videoFps to set
	 */
	public void setVideoFps(int videoFps) {
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
	 * @return the convertMp4
	 */
	public boolean isConvertMp4() {
		return convertMp4;
	}

	/**
	 * @param convertMp4 the convertMp4 to set
	 */
	public void setConvertMp4(boolean convertMp4) {
		this.convertMp4 = convertMp4;
	}
}