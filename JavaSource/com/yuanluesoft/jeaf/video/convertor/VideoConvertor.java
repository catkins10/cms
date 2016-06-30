package com.yuanluesoft.jeaf.video.convertor;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.video.model.Video;

/**
 * 视频格式转换
 * @author linchuan
 *
 */
public interface VideoConvertor {

	/**
	 * 视频转换
	 * @param sourceFileName
	 * @param targetFileName
	 * @param videoBitrate 视频比特率
	 * @param videoFps 帧频
	 * @param videoWidth
	 * @param videoHeight
	 * @param audioBitrate 音频码率
	 * @param audioFreq 音频采样率
	 * @return
	 * @throws ServiceException
	 */
	public boolean videoConvert(String sourceFileName, String targetFileName, double videoBitrate, double videoFps, int videoWidth, int videoHeight, double audioBitrate, double audioFreq) throws ServiceException;
	
	/**
	 * 截取视频图片
	 * @param videoFile
	 * @param imageWidth
	 * @param iamgeHeight
	 * @return
	 * @throws ServiceException
	 */
	public boolean extractImage(String videoFileName, String imageFileName, int imageWidth, int iamgeHeight) throws ServiceException;
	
	/**
	 * 获取视频信息
	 * @param videoFileName
	 * @return
	 * @throws ServiceException
	 */
	public Video getVideoInfo(String videoFileName) throws ServiceException;
}