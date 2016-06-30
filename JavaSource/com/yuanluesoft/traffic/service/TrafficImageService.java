package com.yuanluesoft.traffic.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.image.service.ImageService;

/**
 * 
 * @author linchuan
 *
 */
public interface TrafficImageService extends ImageService {

	/**
	 * 下载照片
	 * @param request
	 * @param response
	 */
	public void downloadImage(HttpServletRequest request, HttpServletResponse response);
}