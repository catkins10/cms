package com.yuanluesoft.jeaf.video.service.spring;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.upload.FormFile;

import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.filetransfer.services.FileUploadService;
import com.yuanluesoft.jeaf.image.model.Image;
import com.yuanluesoft.jeaf.image.service.ImageService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDStringGenerator;
import com.yuanluesoft.jeaf.video.convertor.VideoConvertor;
import com.yuanluesoft.jeaf.video.model.Video;
import com.yuanluesoft.jeaf.video.service.VideoService;

/**
 * 
 * @author linchuan
 *
 */
public class VideoServiceImpl implements VideoService {
	private VideoConvertor videoConvertor; //视频生成器
	private AttachmentService attachmentService; //附件管理服务
	private ImageService imageService; //图像服务
	private String videoFileExtensionNames = "flv,avi,mov,mpg,dat,rm,rmvb,wmv,asf,mpeg,vob,mpe,bik,mp4,3gp";
	private ExchangeClient exchangeClient; //数据交换客户端
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.video.VideoService#uploadVideo(java.lang.String, java.lang.String, long, org.apache.struts.upload.FormFile)
	 */
	public String upload(String applicationName, String videoType, Field fieldDefine, long mainRecordId, FormFile formFile) throws ServiceException {
		String videoPath = attachmentService.upload(applicationName, videoType, fieldDefine, mainRecordId, formFile);
		adjustVideo(videoPath, fieldDefine); //视频转换
		return videoPath;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.spring.AttachmentServiceImpl#processUploadedFile(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.business.model.Field, long, java.lang.String)
	 */
	public String processUploadedFile(String applicationName, String type, Field fieldDefine, long mainRecordId, String name) throws Exception {
		String videoPath = attachmentService.processUploadedFile(applicationName, type, fieldDefine, mainRecordId, name);
		return adjustVideo(videoPath, fieldDefine); //视频转换
	}
	
	/**
	 * 视频转换
	 * @param sourceFileName
	 * @param fieldDefine
	 * @throws ServiceException
	 */
	private String adjustVideo(String sourceFilePath, Field fieldDefine) throws ServiceException {
		try {
			double videoBitrate = StringUtils.parseInt((String)fieldDefine.getParameter("videoBitrate"), 200);
			double videoFps = StringUtils.parseInt((String)fieldDefine.getParameter("videoFps"), 25);
			int videoWidth = StringUtils.parseInt((String)fieldDefine.getParameter("videoWidth"), 0);
			int videoHeight = StringUtils.parseInt((String)fieldDefine.getParameter("videoHeight"), 0);
			double audioBitrate = StringUtils.parseInt((String)fieldDefine.getParameter("audioBitrate"), 56);
			double audioFreq = StringUtils.parseInt((String)fieldDefine.getParameter("audioFreq"), 22050);
			boolean convertMp4 = !"false".equals(fieldDefine.getParameter("convertMp4"));
			
			//检查当前视频的比特率,如果低于设置值,则使用当前视频的比特率
			Video video = videoConvertor.getVideoInfo(sourceFilePath);
			videoBitrate = video.getVideoBitrate()<=0 ? videoBitrate : Math.min(video.getVideoBitrate(), videoBitrate);
			//检查当前视频的帧频,如果低于设置值,则使用当前视频的帧频
			videoFps = video.getVideoFps()<=0 ? videoFps : Math.min(video.getVideoFps(), videoFps);
			
			//检查当前音频的比特率,如果低于设置值,则使用当前视频的比特率
			audioBitrate = video.getAudioBitrate()<=0 ? audioBitrate : Math.min(video.getAudioBitrate(), audioBitrate);
			//检查当前音频的采样率,如果低于设置值,则使用当前音频的采样率
			audioFreq = video.getAudioFreq()<=0 ? audioFreq : Math.min(video.getAudioFreq(), audioFreq);
			
			//检查当前视频的尺寸,如果小于设置值,则使用当前视频的尺寸
			if(video.getVideoWidth()>0 && video.getVideoHeight()>0 && video.getVideoWidth()<videoWidth && video.getVideoHeight()<videoHeight) {
				videoWidth = video.getVideoWidth(); 
				videoHeight = video.getVideoHeight();
			}
			//转换视频
			if(convertMp4) {
				String mp4FileName = sourceFilePath.substring(0, sourceFilePath.replace('\\', '/').lastIndexOf('/') + 1) + UUIDStringGenerator.generateId() + ".mp4";
				if(!videoConvertor.videoConvert(sourceFilePath, mp4FileName, videoBitrate, videoFps, videoWidth, videoHeight, audioBitrate, audioFreq)) { //转换失败
					FileUtils.deleteFile(mp4FileName);
				}
				else { //转换成功
					FileUtils.deleteFile(sourceFilePath);
					sourceFilePath = FileUtils.renameFile(mp4FileName, FileUtils.replaceExtensionName(sourceFilePath, "mp4"), true, false); //替换原文件
				}
			}
			//导出视频图片
			String imageFileName = FileUtils.replaceExtensionName(sourceFilePath, "jpg");
			videoConvertor.extractImage(sourceFilePath, imageFileName, (convertMp4 ? videoWidth : 0), (convertMp4 ? videoHeight : 0));
			return sourceFilePath;
		}
		catch(Exception e) {
			Logger.exception(e);
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.video.VideoService#deleteVideos(java.lang.String, long, java.lang.String, java.lang.String)
	 */
	public void delete(String applicationName, long mainRecordId, String videoType, String videoNames) throws ServiceException {
		//删除截图和缩略图
		String[] names = videoNames.split("\\*");
		for(int i=0; i<names.length; i++) {
			names[i] = FileUtils.replaceExtensionName(names[i], "jpg");
		}
		imageService.delete(applicationName, mainRecordId, videoType, ListUtils.join(names, "\t", false));
		//删除视频文件
		attachmentService.delete(applicationName, mainRecordId, videoType, videoNames);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#createDownload(java.lang.String, java.lang.String, long, java.lang.String, boolean, javax.servlet.http.HttpServletRequest)
	 */
	public String createDownload(String applicationName, String type, long mainRecordId, String name, boolean asAttachment, HttpServletRequest request) throws ServiceException {
		return attachmentService.createDownload(applicationName, type, mainRecordId, name, asAttachment, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#createUploadPassport(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.business.model.Field, long, int, javax.servlet.http.HttpServletRequest)
	 */
	public String createUploadPassport(String applicationName, String type, Field fieldDefine, long mainRecordId, int fileSize, HttpServletRequest request) throws Exception {
		return attachmentService.createUploadPassport(applicationName, type, fieldDefine, mainRecordId, fileSize, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#deleteAll(java.lang.String, java.lang.String, long)
	 */
	public void deleteAll(String applicationName, String type, long mainRecordId) throws ServiceException {
		attachmentService.deleteAll(applicationName, type, mainRecordId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#getDownloadFilePath(java.lang.String)
	 */
	public String getDownloadFilePath(String downloadUrl) throws ServiceException {
		return attachmentService.getDownloadFilePath(downloadUrl);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#createDynamicDownload(java.lang.String, boolean, javax.servlet.http.HttpServletRequest)
	 */
	public String createDynamicDownload(String filePath, boolean attachment, HttpServletRequest request) throws ServiceException {
		return attachmentService.createDynamicDownload(filePath, attachment, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#getFileType(java.lang.String)
	 */
	public String getFileType(String downloadUrl) throws ServiceException {
		return attachmentService.getFileType(downloadUrl);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#listTypes(java.lang.String, long)
	 */
	public List listTypes(String applicationName, long mainRecordId) throws ServiceException {
		return attachmentService.listTypes(applicationName, mainRecordId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#replace(java.lang.String, long, java.lang.String, java.lang.String, long, java.lang.String)
	 */
	public void replace(String fromApplicationName, long fromRecordId, String fromType, String toApplicationName, long toRecordId, String toType) throws ServiceException {
		attachmentService.replace(fromApplicationName, fromRecordId, fromType, toApplicationName, toRecordId, toType);	
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#upload(java.lang.String, java.lang.String, long, java.lang.String, java.lang.String)
	 */
	public String upload(String applicationName, String type, Field fieldDefine, long mainRecordId, String fileName, String base64FileData) throws ServiceException {
		return attachmentService.upload(applicationName, type, fieldDefine, mainRecordId, fileName, base64FileData);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#upload(java.lang.String, java.lang.String, long, java.lang.String)
	 */
	public String uploadFile(String applicationName, String type, Field fieldDefine, long mainRecordId, String filePath) throws ServiceException {
		return attachmentService.uploadFile(applicationName, type, fieldDefine, mainRecordId, filePath);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#upload(java.lang.String, java.lang.String, long, java.util.List)
	 */
	public List uploadFiles(String applicationName, String type, Field fieldDefine, long mainRecordId, List filePaths) throws ServiceException {
		return attachmentService.uploadFiles(applicationName, type, fieldDefine, mainRecordId, filePaths);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.video.VideoService#getSavePath(java.lang.String, java.lang.String, long, boolean)
	 */
	public String getSavePath(String applicationName, String videoType, long id, boolean mkdir) {
		return attachmentService.getSavePath(applicationName, videoType, id, mkdir);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.video.VideoService#getVideo(java.lang.String, java.lang.String, long, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public Video getVideo(String applicationName, String videoType, long mainRecordId, String videoName, HttpServletRequest request) throws ServiceException {
        String path = getSavePath(applicationName, videoType, mainRecordId, false);
        File videoFile = new File(path + videoName);
        Video video;
        if(!isVideoFile(videoFile.getPath())) { //不是视频文件
        	video = new Video();
		}
        else {
	        try {
	        	video = videoConvertor.getVideoInfo(videoFile.getPath());
	        }
	        catch(Exception e) {
	        	video = new Video();
	        }
        }
        video.setName(videoName);
        video.setFilePath(videoFile.getPath());
        video.setSize(videoFile.length());
        video.setLastModified(videoFile.lastModified());
		video.setUrl(createDownload(applicationName, videoType, mainRecordId, videoName, false, request));
		video.setPreviewImageUrl(getBreviaryImage(applicationName, videoType, mainRecordId, videoName, 0, 0, request).getUrlInline());
		video.setType(videoType);
		video.setApplicationName(applicationName);
		video.setRecordId(mainRecordId);
		video.setService(this);
		return video;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.video.service.VideoService#getVideoURL(java.lang.String, java.lang.String, long, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	public String getVideoURL(String applicationName, String videoType, long mainRecordId, String videoName, HttpServletRequest request) throws ServiceException {
		return attachmentService.createDownload(applicationName, videoType, mainRecordId, videoName, false, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.video.VideoService#listVideos(java.lang.String, java.lang.String, long, int, javax.servlet.http.HttpServletRequest)
	 */
	public List list(String applicationName, String videoType, long mainRecordId, boolean iconURLRequired, int max, HttpServletRequest request) throws ServiceException {
        String path = getSavePath(applicationName, videoType, mainRecordId, false);
        if(!FileUtils.isExists(path)) {
        	return null;
        }
    	File[] files = FileUtils.listFilesSortByName(path);
		List  videoList = new ArrayList();
		for(int i=0; i<files.length; i++) {
			if(files[i].isDirectory()) {
				 continue;
			}
		    if("Thumbs.db".equals(files[i].getName()) || files[i].getName().endsWith(FileUploadService.PARTIAL_FILE_POSTFIX)) { //windows 系统文件,或者未完成上传的文件
		        continue;
		    }
		    if(files[i].getName().endsWith("jpg")) { //图片
		    	continue;
		    }
		    Video video = getVideo(applicationName, videoType, mainRecordId,  files[i].getName(), request);
		    if(iconURLRequired) {
		    	video.setIconURL(getBreviaryImage(applicationName, videoType, mainRecordId, files[i].getName(), 100, 100, request).getUrl());
		    }
		    videoList.add(video);
			if(max>0 && videoList.size()==max) { //最大视频数量
				return videoList;
			}
		}
		return videoList.isEmpty() ? null : videoList;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.video.VideoService#getBreviaryImage(java.lang.String, java.lang.String, long, java.lang.String, int, int, javax.servlet.http.HttpServletRequest)
	 */
	public Image getBreviaryImage(String applicationName, String videoType, long mainRecordId, String videoName, int breviaryWidth, int breviaryHeight, HttpServletRequest request) throws ServiceException {
		String imageName = FileUtils.replaceExtensionName(videoName, "jpg");
		String savePath = getSavePath(applicationName, videoType, mainRecordId, false);
		String imagePath = savePath + imageName;
		if(!FileUtils.isExists(imagePath)) { //截图不存在
			try {
				videoConvertor.extractImage(savePath + videoName, imagePath, 0, 0);
				exchangeClient.sendFiles(ListUtils.generateList(imagePath), null, false, true); //数据交换
			}
			catch(Exception e) {
				return null;
			}
		}
		if(breviaryWidth==0 || breviaryHeight==0) {
			return imageService.getImage(applicationName, videoType, mainRecordId, imageName, request);
		}
		else {
			return imageService.getBreviaryImage(applicationName, videoType, mainRecordId, imageName, breviaryWidth, breviaryHeight, false, request);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.spring.AttachmentServiceImpl#getDefaultMaxUpload()
	 */
	protected int getDefaultMaxUpload() {
		return 100000000; //100M
	}

	/**
	 * 判断是否视频文件
	 * @param videoFileName
	 * @return
	 */
	private boolean isVideoFile(String videoFileName) {
		String videoFileExtensionName = videoFileName.substring(videoFileName.lastIndexOf(".") + 1).toLowerCase();
		return (("," + videoFileExtensionNames + ",").indexOf("," + videoFileExtensionName + ",")!=-1);
	}
	
	/**
	 * @return the attachmentService
	 */
	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	/**
	 * @param attachmentService the attachmentService to set
	 */
	public void setAttachmentService(
			AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	/**
	 * @return the videoConvertor
	 */
	public VideoConvertor getVideoConvertor() {
		return videoConvertor;
	}

	/**
	 * @param videoConvertor the videoConvertor to set
	 */
	public void setVideoConvertor(VideoConvertor videoConvertor) {
		this.videoConvertor = videoConvertor;
	}

	/**
	 * @return the imageService
	 */
	public ImageService getImageService() {
		return imageService;
	}

	/**
	 * @param imageService the imageService to set
	 */
	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	/**
	 * @return the exchangeClient
	 */
	public ExchangeClient getExchangeClient() {
		return exchangeClient;
	}

	/**
	 * @param exchangeClient the exchangeClient to set
	 */
	public void setExchangeClient(ExchangeClient exchangeClient) {
		this.exchangeClient = exchangeClient;
	}
}