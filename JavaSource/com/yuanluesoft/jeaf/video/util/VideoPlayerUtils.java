package com.yuanluesoft.jeaf.video.util;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.image.model.Image;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.model.RequestInfo;
import com.yuanluesoft.jeaf.video.model.Video;
import com.yuanluesoft.jeaf.video.service.VideoService;

/**
 * 
 * @author chuan
 *
 */
public class VideoPlayerUtils {

	/**
	 * 生成视频播放器脚本
	 * @param applicationName
	 * @param recordId
	 * @param videoType
	 * @param videoName
	 * @param playerWidth
	 * @param playerHeight
	 * @param autoStart
	 * @param hideControls
	 * @param siteId
	 * @param request
	 * @return
	 */
	public static String generateVideoPlayerScript(String applicationName, long recordId, String videoType, String videoName, int playerWidth, int playerHeight, boolean autoStart, boolean hideControls, long siteId, HttpServletRequest request) {
		try {
			RequestInfo requestInfo = RequestUtils.getRequestInfo(request);
			if(!requestInfo.isClientRequest()) { //不是客户端页面
				String properties = generateVideoPlayerProperties(applicationName, recordId, videoType, videoName, playerWidth, playerHeight, autoStart, hideControls);
				String url = Environment.getContextPath() + "/jeaf/video/video.shtml" +
					  		 "?video=" + Encoder.getInstance().desEncode(properties, "20041227", "utf-8", "DES") +
					  		 (siteId==0 ? "" : "&siteId=" + siteId);
				return "VideoPlayer.loadVideo('" + url + "');";
			}
			//客户端页面
			double density = Math.max(1, RequestUtils.getParameterDoubleValue(request, "client.density")); //密度
			int imageWidth = RequestUtils.getParameterIntValue(request, "client.imageWidth"); //图片宽度
			if(imageWidth > 0 && playerWidth > imageWidth) {
				playerHeight = playerHeight * imageWidth / playerWidth;
				playerWidth = imageWidth;
			}
			//获取视频服务
			VideoService videoService = (VideoService)FieldUtils.getAttachmentService(applicationName, videoType, recordId);
			Video video = videoService.getVideo(applicationName, videoType, recordId, videoName, request);
			//加密视频URL
			String videoUrl = Encoder.getInstance().desEncode(video.getUrl(), "07181341", "utf-8", "DES");
			//获取预览图URL
			int previewImageWidth = density<=0 ? playerWidth : (int)Math.min(video.getVideoWidth(), playerWidth * density);
			int previewImageHeight = playerHeight * previewImageWidth / playerWidth;
			Image previewImage = videoService.getBreviaryImage(applicationName, videoType, recordId, videoName, previewImageWidth, previewImageHeight, request);
			return "window.top.createVideoPlayer(window," +
												 "'" + videoUrl + "'," +
												 "'" + previewImage.getUrl() + (previewImage.getUrl().indexOf('?')==-1 ? '?' : '&') + "client.modified=" + previewImage.getLastModified() + "&client.fileSize=" + previewImage.getSize() + "'," +
												 "'" + request.getContextPath() + "/cms/sitemanage/videoPlayerLogo.shtml" + (siteId==0 ? "" : "?siteId=" + siteId) + "'," +
												 video.getVideoLength() + "," +
												 playerWidth + "," +
												 playerHeight + "," +
												 autoStart + "," +
												 hideControls + ")";
		}
		catch(Exception e) {
			throw new Error(e);
		}
	}
	
	/**
	 * 生成视频播放器属性列表
	 * @param applicationName
	 * @param recordId
	 * @param videoType
	 * @param videoName
	 * @param playerWidth
	 * @param playerHeight
	 * @param autoStart
	 * @param hideControls
	 * @return
	 */
	public static String generateVideoPlayerProperties(String applicationName, long recordId, String videoType, String videoName, int playerWidth, int playerHeight, boolean autoStart, boolean hideControls) {
		return "applicationName=" + applicationName +
		       "&recordId=" + recordId +
		       "&videoType=" + videoType +
		       "&videoName=" + StringUtils.encodePropertyValue(videoName) +
		       "&width=" + playerWidth +
		       "&height=" + playerHeight +
		       "&autoStart=" + autoStart +
		       "&hideControls=" + hideControls;
	}
}