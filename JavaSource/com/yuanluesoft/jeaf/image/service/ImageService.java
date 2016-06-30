package com.yuanluesoft.jeaf.image.service;

import java.awt.Dimension;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.image.model.Image;
import com.yuanluesoft.jeaf.image.model.WaterMark;

/**
 * 
 * @author linchuan
 *
 */
public interface ImageService extends AttachmentService {
	
	/**
	 * 添加水印
	 * @param imagePath
	 * @param waterMark
	 * @throws ServiceException
	 */
	public String addWaterMark(String imagePath, WaterMark waterMark) throws ServiceException;

	/**
	 * 获取图片
	 * @param applicationName
	 * @param imageType
	 * @param mainRecordId
	 * @param imageName
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public Image getImage(String applicationName, String imageType, long mainRecordId, String imageName, HttpServletRequest request) throws ServiceException;

	/**
	 * 获取缩略图
	 * @param applicationName
	 * @param imageType
	 * @param mainRecordId
	 * @param imageName
	 * @param breviaryWidth
	 * @param breviaryHeight
	 * @param clipEnabled
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public Image getBreviaryImage(String applicationName, String imageType, long mainRecordId, String imageName, int breviaryWidth, int breviaryHeight, boolean clipEnabled, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 获取图片尺寸
	 * @param imageFilePath
	 * @return
	 * @throws ServiceException
	 */
	public Dimension getImageDimension(String imageFilePath) throws ServiceException;

	/**
	 * 转换图片类型
	 * @param imageFilePath
	 * @param newImageFilePath
	 * @param newImageType
	 * @return
	 */
	public void convertImage(String imageFilePath, String newImageFilePath, String newImageType);
}