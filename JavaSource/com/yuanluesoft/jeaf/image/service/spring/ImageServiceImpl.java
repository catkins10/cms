package com.yuanluesoft.jeaf.image.service.spring;

import java.awt.Dimension;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.upload.FormFile;

import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.filetransfer.services.FileUploadService;
import com.yuanluesoft.jeaf.image.generator.ImageGenerator;
import com.yuanluesoft.jeaf.image.model.Image;
import com.yuanluesoft.jeaf.image.model.WaterMark;
import com.yuanluesoft.jeaf.image.service.ImageService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ImageServiceImpl implements ImageService {
	private final String BREVIART_IMAGE_FILE_EXT = "_breviary"; //缩略图文件后缀名
	private ImageGenerator imageGenerator; //图像生成器
	private AttachmentService attachmentService; //附件管理服务
	private ExchangeClient exchangeClient; //数据交换客户端
	private String imageFileExtensionNames = "jpg,bmp,tga,vst,pcd,pct,gif,ai,fpx,img,cal,wi,png,tiff,psd,eps,raw,pdf,png,pxr,mac,eps,ai,sct,pdp,dxf";

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#upload(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.business.model.Field, long, org.apache.struts.upload.FormFile)
	 */
	public String upload(String applicationName, String imageType, Field fieldDefine, long mainRecordId, FormFile formFile) throws ServiceException {
		String imagePath = attachmentService.upload(applicationName, imageType, fieldDefine, mainRecordId, formFile);
		//检查图片像素
		checkPixels(imagePath, applicationName, imageType, fieldDefine, mainRecordId);
		return adjustImage(imagePath, fieldDefine);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.spring.AttachmentServiceImpl#processUploadedFile(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.business.model.Field, long, java.lang.String)
	 */
	public String processUploadedFile(String applicationName, String type, Field fieldDefine, long mainRecordId, String name) throws Exception {
		String imagePath = attachmentService.processUploadedFile(applicationName, type, fieldDefine, mainRecordId, name);
		checkPixels(imagePath, applicationName, type, fieldDefine, mainRecordId);
		return adjustImage(imagePath, fieldDefine);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#upload(java.lang.String, java.lang.String, long, java.lang.String, java.lang.String)
	 */
	public String upload(String applicationName, String type, Field fieldDefine, long mainRecordId, String fileName, String base64FileData) throws ServiceException {
		String imagePath = attachmentService.upload(applicationName, type, fieldDefine, mainRecordId, fileName, base64FileData);
		return adjustImage(imagePath, fieldDefine);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#upload(java.lang.String, java.lang.String, long, java.lang.String)
	 */
	public String uploadFile(String applicationName, String type, Field fieldDefine, long mainRecordId, String filePath) throws ServiceException {
		String imagePath = attachmentService.uploadFile(applicationName, type, fieldDefine, mainRecordId, filePath);
		return adjustImage(imagePath, fieldDefine);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#upload(java.lang.String, java.lang.String, long, java.util.List)
	 */
	public List uploadFiles(String applicationName, String type, Field fieldDefine, long mainRecordId, List filePaths) throws ServiceException {
		List imagePaths = attachmentService.uploadFiles(applicationName, type, fieldDefine, mainRecordId, filePaths);
		for(int i=0; i<(imagePaths==null ? 0 : imagePaths.size()); i++) {
			String imagePath = (String)imagePaths.get(i);
			imagePaths.set(i, adjustImage(imagePath, fieldDefine));
		}
		return imagePaths;
	}

	/**
	 * 调整图片
	 * @param imagePath
	 * @param fieldDefine
	 * @return
	 * @throws ServiceException
	 */
	public String adjustImage(String imagePath, Field fieldDefine) throws ServiceException {
		if(fieldDefine==null) {
			return imagePath;
		}
		//获取裁剪宽度和高度
		int imageClipWidth = StringUtils.parseInt((String)fieldDefine.getParameter("imageClipWidth"), 0);
		int imageClipHeight = StringUtils.parseInt((String)fieldDefine.getParameter("imageClipHeight"), 0);
		if(imageClipWidth>0 && imageClipHeight>0) { //指定了裁剪尺寸
			return adjustImage(imagePath, imageClipWidth, imageClipHeight, "true".equals(fieldDefine.getParameter("clipEnabled")));
		}
		String fileExtension = (String)fieldDefine.getParameter("fileExtension");
		if(fileExtension==null) {
			fileExtension = FileUtils.getDefaultFileExtension(true, false);
		}
		//获取存储大小限制
		String maxSaveSizeParameter = (String)fieldDefine.getParameter("maxSaveSize");
		int maxSaveSize = StringUtils.parseInt(maxSaveSizeParameter, 1000000); //默认1M
		if(new File(imagePath).length() <= maxSaveSize && FileUtils.validateFileExtension(fileExtension, imagePath)) { //文件大小小于上限、且文件类型校验通过
			return imagePath;
		}
		//缩小图片
		Dimension dimension = getImageDimension(imagePath);
		imageClipWidth = (int)dimension.getWidth();
		imageClipHeight = (int)dimension.getHeight();
		int pixels = imageClipWidth * imageClipHeight;
		if(pixels > 8000000) { //超过8百万像素
			imageClipWidth = (int)(imageClipWidth * Math.sqrt(8000000.0 / pixels));
			imageClipHeight = (int)(imageClipHeight * Math.sqrt(8000000.0 / pixels));
		}
		for(int i=0; i<(maxSaveSizeParameter==null ? 10 : 100) && imageClipWidth>0 && imageClipHeight>0; i++) {
			imagePath = adjustImage(imagePath, imageClipWidth, imageClipHeight, false);
			long fileSize = new File(imagePath).length();
			if(fileSize <= maxSaveSize) {
				break;
			}
			double scale = Math.min(0.95, Math.sqrt(0.95 * maxSaveSize / fileSize));
			imageClipWidth = (int)(imageClipWidth * scale);
			imageClipHeight = (int)(imageClipHeight * scale);
		}
		return imagePath;
	}

	/**
	 * 调整图片
	 * @param imagePath
	 * @param toWidth
	 * @param toHeight
	 * @param clipEnabled
	 * @return
	 * @throws ServiceException
	 */
	private String adjustImage(String imagePath, int toWidth, int toHeight, boolean clipEnabled) throws ServiceException {
		try {
			if(toWidth<=0 || toHeight<=0) {
				return imagePath;
			}
			//调整图片尺寸
			String newImagePath = imagePath + ".jpg";
			
			Dimension dimension = getImageDimension(imagePath);
			dimension = scaleImage((int)dimension.getWidth(), (int)dimension.getHeight(), toWidth, toHeight, clipEnabled); //缩放
			resizeImage(imagePath, newImagePath, dimension.width, dimension.height, clipEnabled ? toWidth : -1, clipEnabled ? toHeight : -1);
			//删除原文件
			FileUtils.deleteFile(imagePath);
			//替换原文件
			return FileUtils.renameFile(newImagePath, FileUtils.replaceExtensionName(imagePath, "jpg"), true, false);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		return imagePath;
	}
	
	/**
	 * 检查像素是否过大,如果超过自动删除
	 * @param imagePath
	 * @return
	 * @throws ServiceException
	 */
	private void checkPixels(String imagePath, String applicationName, String type, Field fieldDefine, long mainRecordId) throws ServiceException {
		String maxMegaPixels = (String)fieldDefine.getParameter("maxMegaPixels");
		double max = maxMegaPixels==null ? imageGenerator.getMaxMegaPixels() : Double.parseDouble(maxMegaPixels);
		if(max<=0) {
			return;
		}
		Dimension dimension = getImageDimension(imagePath);
		if(dimension.getWidth() * dimension.getHeight() > max  * 1000000) {
			delete(applicationName, mainRecordId, type, imagePath.substring(imagePath.lastIndexOf('/')+1));
			throw new ServiceException("图片分辨率不允许超过" + (int)(max * 100) + "万像素");
		}
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
	
	/**
	 * 按指定尺寸缩放和裁剪图片
	 * @param imagePath
	 * @param newImagePath
	 * @param resizeWidth
	 * @param resizeHeight
	 * @param cropWidth
	 * @param cropHeight
	 * @throws ServiceException
	 */
	private void resizeImage(String imagePath, String newImagePath, int resizeWidth, int resizeHeight, int cropWidth, int cropHeight) throws ServiceException {
		int cropLeft = 0, cropTop = 0;
		if(cropWidth!=-1) {
			cropLeft = Math.max(0, (resizeWidth - cropWidth) / 2);
			cropTop = Math.max(0, (resizeHeight - cropHeight) / 2);
		}
		imageGenerator.convertImage(imagePath, newImagePath, resizeWidth, resizeHeight, cropWidth, cropHeight, cropLeft, cropTop);
	}
	
	/**
	 * 获取缩放后发尺寸
	 * @param originalWidth
	 * @param originalHeight
	 * @param newWidth
	 * @param newHeight
	 * @param clipEnabled
	 * @return
	 */
	private Dimension scaleImage(int originalWidth, int originalHeight, int newWidth, int newHeight, boolean clipEnabled) {
		double scaleWidth = (newWidth + 0.0) / originalWidth;
		double scaleHeight = (newHeight + 0.0) / originalHeight;
		//允许裁剪时，按大的比例来调整尺寸，否则按小的比例调整，以保证图片大小不会超过所需要的尺寸
		double scale = (clipEnabled ? Math.max(scaleWidth, scaleHeight) : Math.min(scaleWidth, scaleHeight));
		return new Dimension((int)(originalWidth * scale), (int)(originalHeight * scale));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.service.ImageService#convertImage(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void convertImage(String imageFilePath, String newImageFilePath, String newImageType) {
		try {
			imageGenerator.convertImage(imageFilePath, newImageFilePath, -1, -1, -1, -1, 0, 0);
		}
		catch (ServiceException e) {
			
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.service.ImageService#addWaterMark(java.lang.String, com.yuanluesoft.jeaf.image.model.WaterMark)
	 */
	public String addWaterMark(String imagePath, WaterMark waterMark) throws ServiceException {
		Dimension imageSize = getImageDimension(imagePath);
		Dimension waterMarkImageSize = getImageDimension(waterMark.getWaterMarkImagePath());
		if(waterMarkImageSize.width<=0 || waterMarkImageSize.height<=0 ||
		   waterMarkImageSize.width>imageSize.width/3 || waterMarkImageSize.height>imageSize.height/3) { //水印比原图的1/3大
			return imagePath;
		}
		int x = -1, y = -1;
		if("左上".equals(waterMark.getWaterMarkAlign())) { //左上
			x = waterMark.getWaterMarkXMargin();
			y = waterMark.getWaterMarkYMargin();
		}
		else if("左下".equals(waterMark.getWaterMarkAlign())) { //左下
			x = waterMark.getWaterMarkXMargin();
			y = imageSize.height - waterMark.getWaterMarkYMargin() - waterMarkImageSize.height;
		}
		else if("右上".equals(waterMark.getWaterMarkAlign())) { //右上
			x = imageSize.width - waterMark.getWaterMarkXMargin() - waterMarkImageSize.width;
			y = waterMark.getWaterMarkYMargin();
		}
		else if("右下".equals(waterMark.getWaterMarkAlign())) { //右下
			x = imageSize.width - waterMark.getWaterMarkXMargin() - waterMarkImageSize.width;
			y = imageSize.height - waterMark.getWaterMarkYMargin() - waterMarkImageSize.height;
		}
		if(x<0 || y<0) {
			return imagePath;
		}
		String newImagePath = FileUtils.replaceExtensionName(imagePath, "jpg");
		imageGenerator.pressImage(imagePath, newImagePath, waterMark.getWaterMarkImagePath(), x, y);
		if(!imagePath.equalsIgnoreCase(newImagePath)) {
			FileUtils.deleteFile(imagePath);
		}
		return newImagePath;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.service.ImageService#deleteImage(java.lang.String, long, java.lang.String, java.lang.String)
	 */
	public void delete(String applicationName, long mainRecordId, String imageType, String imageNames) throws ServiceException {
		String dir = getSavePath(applicationName, imageType, mainRecordId, false);
		String[] names = imageNames.split("\\*");
		for(int i=0; i<names.length; i++) {
			//删除本身
			FileUtils.deleteFile(dir + names[i]);
			//删除缩略图
			String breviaryImageName = getBreviaryImageName(names[i], 0, 0);
			breviaryImageName = breviaryImageName.substring(0, breviaryImageName.lastIndexOf("."));
			File directory = new File(dir);
			final String imageNameFilter = breviaryImageName;
			//获取缩略图列表
			File[] files = directory.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.startsWith(imageNameFilter);
				}
			});
			if(files!=null && files.length>0) {
				for(int j=0; j<files.length; j++) {
					FileUtils.deleteFile(dir + files[j].getName());
				}
			}
		}
		if(new File(dir).listFiles().length==0) {
			FileUtils.deleteDirectory(dir);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.service.ImageService#getImageDimension(java.lang.String)
	 */
	public Dimension getImageDimension(String imageFilePath) throws ServiceException {
		return imageGenerator.getDimension(imageFilePath);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.service.ImageService#getBreviaryImage(java.lang.String, java.lang.String, long, java.lang.String, int, int, boolean, javax.servlet.http.HttpServletRequest)
	 */
	public Image getBreviaryImage(String applicationName, String imageType, long mainRecordId, String imageName, int breviaryWidth, int breviaryHeight, boolean clipEnabled, HttpServletRequest request) throws ServiceException {
    	if(breviaryWidth==0 && breviaryHeight==0) { //不需要裁剪
    		return getImage(applicationName, imageType, mainRecordId, imageName, request); 
    	}
    	//获取图片
    	Image image = getImage(applicationName, imageType, mainRecordId, imageName, request);
    	if(breviaryWidth > 0 && breviaryHeight > 0 && image.getWidth() <= breviaryWidth && image.getHeight() <= breviaryHeight) { //所需要的缩略图尺寸超过当前图片尺寸
			return image;
		}
    	if(breviaryWidth==0 || breviaryHeight==0) { //缩略图尺寸中，有一个未指定
    		clipEnabled = false;
			if(breviaryWidth==0) {
				breviaryWidth = (int)(image.getWidth() * (breviaryHeight + 0.0) / (image.getHeight() + 0.0));
			}
			else {
				breviaryHeight = (int)(image.getHeight() * (breviaryWidth + 0.0) / (image.getWidth() + 0.0));
			}
		}
    	//获取缩放后的尺寸
    	Dimension resizeDimension = scaleImage(image.getWidth(), image.getHeight(), breviaryWidth, breviaryHeight, clipEnabled);
    	//检查是否存在所指定尺寸的缩略图
    	String breviaryFileName = null;
    	if(clipEnabled) {
    		breviaryFileName = getBreviaryImageName(imageName, breviaryWidth, breviaryHeight);
    	}
    	else {
    		breviaryFileName = getBreviaryImageName(imageName, resizeDimension.width, resizeDimension.height);
    	}
		String path = getSavePath(applicationName, imageType, mainRecordId, false);
		if(!FileUtils.isExists(path + breviaryFileName) || new File(path + breviaryFileName).length() < 5) { //不存在,或者字节数小于5
			try {
				//创建缩略图
				resizeImage(path + imageName, path + breviaryFileName, (int)resizeDimension.getWidth(), (int)resizeDimension.getHeight(), clipEnabled ? breviaryWidth : -1, clipEnabled ? breviaryHeight : -1);
				//数据交换
				exchangeClient.sendFiles(ListUtils.generateList(path + breviaryFileName), null, false, true);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
		return getImage(applicationName, imageType, mainRecordId, breviaryFileName, request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.service.ImageService#getImage(java.lang.String, java.lang.String, long, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Image getImage(String applicationName, String imageType, long mainRecordId, String imageName, HttpServletRequest request) throws ServiceException {
        String path = getSavePath(applicationName, imageType, mainRecordId, false);
        File imageFile = new File(path + imageName);
        Image image = new Image();
		image.setName(imageName);
		image.setFilePath(imageFile.getPath());
		image.setSize(imageFile.length());
		image.setLastModified(imageFile.lastModified());
		image.setType(imageType);
		image.setApplicationName(applicationName);
		image.setRecordId(mainRecordId);
		image.setService(this);
		if(isImageFile(image.getFilePath())) { //是图片
			try {
				//获取图片尺寸
				Dimension dimension = imageGenerator.getDimension(image.getFilePath()); //获取尺寸
				if(dimension!=null) {
					image.setWidth((int)dimension.getWidth());
					image.setHeight((int)dimension.getHeight());
				}
			}
			catch(Exception e) {
				
			}
		}
		image.setUrl(attachmentService.createDownload(applicationName, imageType, mainRecordId, imageName, false, request));
		return image;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.service.ImageService#listImages(java.lang.String, java.lang.String, long, int, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List list(String applicationName, String imageType, long mainRecordId, boolean iconURLRequired, int max, HttpServletRequest request) throws ServiceException {
		String path = getSavePath(applicationName, imageType, mainRecordId, false);
        if(!FileUtils.isExists(path)) {
        	return null;
        }
    	File[] files = FileUtils.listFilesSortByName(path);
		List  imageList = new ArrayList();
		for(int i=0; i<files.length; i++) {
			if(files[i].isDirectory()) {
				 continue;
			}
		    if("Thumbs.db".equals(files[i].getName()) || files[i].getName().endsWith(FileUploadService.PARTIAL_FILE_POSTFIX)) { //windows 系统文件,或者未完成上传的文件
		        continue;
		    }
		    if(files[i].getName().indexOf(BREVIART_IMAGE_FILE_EXT)!=-1) { //不获取缩略图
		    	continue;
		    }
		    Image image = getImage(applicationName, imageType, mainRecordId,  files[i].getName(), request);
		    if(iconURLRequired) {
		    	image.setIconURL(getBreviaryImage(applicationName, imageType, mainRecordId, files[i].getName(), 100, 100, false, request).getUrl());
		    }
		    imageList.add(image);
			if(max>0 && imageList.size()==max) { //最大图像数量
				return imageList;
			}
		}
		return imageList.isEmpty() ? null : imageList;
	}
	
	/**
	 * 获取缩略图文件名
	 * @param imageName
	 * @return
	 */
	protected String getBreviaryImageName(String imageName, int breviaryWidth, int breviaryHeight) {
		int index = imageName.lastIndexOf('.');
		if(index==-1) {
			imageName += BREVIART_IMAGE_FILE_EXT;
		}
		else {
			imageName = imageName.substring(0, index) + BREVIART_IMAGE_FILE_EXT;
		}
		if(breviaryWidth > 0 || breviaryHeight > 0) {
			imageName += "_" + breviaryWidth + "_" + breviaryHeight;
		}
		return imageName + ".jpg";
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.service.ImageService#getSavePath(java.lang.String, java.lang.String, long, boolean)
	 */
	public String getSavePath(String applicationName, String dataCategory, long id, boolean mkdir) {
		return attachmentService.getSavePath(applicationName, dataCategory, id, mkdir);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.spring.AttachmentServiceImpl#getDefaultMaxUpload()
	 */
	protected int getDefaultMaxUpload() {
		return 50000000; //50M
	}


	/**
	 * 判断是否图片文件
	 * @param videoFileName
	 * @return
	 */
	private boolean isImageFile(String imageoFileName) {
		String videoFileExtensionName = imageoFileName.substring(imageoFileName.lastIndexOf(".") + 1).toLowerCase();
		return (("," + imageFileExtensionNames + ",").indexOf("," + videoFileExtensionName + ",")!=-1);
	}
	
	/**
	 * @return the imageGenerator
	 */
	public ImageGenerator getImageGenerator() {
		return imageGenerator;
	}

	/**
	 * @param imageGenerator the imageGenerator to set
	 */
	public void setImageGenerator(ImageGenerator imageGenerator) {
		this.imageGenerator = imageGenerator;
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
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
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