package com.yuanluesoft.jeaf.video.service;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.image.model.Image;
import com.yuanluesoft.jeaf.video.model.Video;

/**
 * 
 * @author linchuan
 *
 */
public interface VideoService extends AttachmentService {
	
	/**
	 * 获取视频
	 * @param applicationName
	 * @param videoType
	 * @param mainRecordId
	 * @param videoName
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public Video getVideo(String applicationName, String videoType, long mainRecordId, String videoName, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 获取视频的URL
	 * @param applicationName
	 * @param videoType
	 * @param mainRecordId
	 * @param videoName
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public String getVideoURL(String applicationName, String videoType, long mainRecordId, String videoName, HttpServletRequest request) throws ServiceException;

	/**
	 * 获取缩略图
	 * @param applicationName
	 * @param videoType
	 * @param mainRecordId
	 * @param videoName
	 * @param breviaryWidth
	 * @param breviaryHeight
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public Image getBreviaryImage(String applicationName, String videoType, long mainRecordId, String videoName, int breviaryWidth, int breviaryHeight, HttpServletRequest request) throws ServiceException;
}