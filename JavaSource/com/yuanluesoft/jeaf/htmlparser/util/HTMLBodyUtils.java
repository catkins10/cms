package com.yuanluesoft.jeaf.htmlparser.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLImageElement;

import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.attachmentmanage.service.TemporaryFileManageService;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.filetransfer.services.FileDownloadService;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.htmlparser.model.HTMLBodyInfo;
import com.yuanluesoft.jeaf.image.service.ImageService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.MsWordUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.video.model.Video;
import com.yuanluesoft.jeaf.video.service.VideoService;
import com.yuanluesoft.jeaf.video.util.VideoPlayerUtils;

/**
 * 
 * @author linchuan
 *
 */
public class HTMLBodyUtils {
	
	/**
	 * html正文分析，如果发现文件不在当前记录的目录中,自动下载
	 * @param applicationName
	 * @param record
	 * @param body
	 * @param callback
	 * @param recordId
	 * @return
	 * @throws Exception
	 */
	public static HTMLBodyInfo analysisHTMLBody(Record record, String body, HTMLBodyParseCallback callback) throws Exception {
		HTMLBodyInfo bodyInfo = new HTMLBodyInfo();
		HTMLParser htmlParser = (HTMLParser)Environment.getService("htmlParser");
		
		//解析正文
		int len = body.length();
		body = body.replaceAll("(?i)<\\\\?\\?xml[^>]*>", "")
				   .replaceAll("(?i)<o:p>\\s*<\\/o:p>", "")
				   .replaceAll("(?i)<o:p>.*?<\\/o:p>", "&nbsp;");
		boolean changed = body.length()!=len;
		HTMLDocument document = htmlParser.parseHTMLString(body, "utf-8");
		//分析图片
		if(analysisImages(bodyInfo, document, record, "images", callback)) {
			changed = true;
		}
		//分析视频
		if(analysisVideos(bodyInfo, document, record, "videos", callback)) {
			changed = true;
		}
		//分析附件
		if(analysisAttachments(bodyInfo, document, record, "attachments", callback)) {
			changed = true;
		}
		if(changed) {
			bodyInfo.setBodyChanged(true);
			bodyInfo.setNewBody(htmlParser.getBodyHTML(document, "utf-8", false));
		}
		return bodyInfo;
	}
	
	/**
	 * 图片分析
	 * @param bodyInfo
	 * @param document
	 * @param record
	 * @param imageType
	 * @param callback
	 * @return
	 * @throws Exception
	 */
	private static boolean analysisImages(HTMLBodyInfo bodyInfo, HTMLDocument document, Record record, String imageType, HTMLBodyParseCallback callback) throws Exception {
		//获取附件配置
		BusinessDefineService businessDefineService = (BusinessDefineService)Environment.getService("businessDefineService");
		BusinessObject businessObject = businessDefineService.getBusinessObject(record.getClass());
		Field field = businessObject.getField(imageType);
		//获取视频服务
		String serviceName = (String)field.getParameter("serviceName");
		//获取图片服务
		ImageService imageService = (ImageService)Environment.getService(serviceName==null ? "imageService" : serviceName);
		
		HTMLCollection images = document.getImages();
		boolean changed = false;
		if(images!=null) {
			for(int i=0; i<images.getLength(); i++) {
				HTMLImageElement image = (HTMLImageElement)images.item(i);
				if("video".equals(image.getId())) { //视频
					continue;
				}
				String imageName = downloadAttachmentIfNeed(record.getId(), image.getSrc(), imageService.getSavePath(businessObject.getApplicationName(), imageType, record.getId(), true), callback);
				if(imageName!=null) { //是图片
					//重新设置图片路径
					String newSrc = imageService.getImage(businessObject.getApplicationName(), imageType, record.getId(), imageName, null).getUrl();
					if(!newSrc.equals(image.getSrc())) {
						image.setSrc(newSrc);
						changed = true;
					}
				}
				bodyInfo.setImageCount(bodyInfo.getImageCount()+1);
				if(bodyInfo.getFirstImageName()==null) {
					bodyInfo.setFirstImageName(imageName);
				}
			}
		}
		return changed;
	}
	
	/**
	 * 视频分析
	 * @param bodyInfo
	 * @param document
	 * @param record
	 * @param videoType
	 * @param callback
	 * @return
	 * @throws Exception
	 */
	private static boolean analysisVideos(HTMLBodyInfo bodyInfo, HTMLDocument document, Record record, String videoType, HTMLBodyParseCallback callback) throws Exception {
		//获取附件配置
		BusinessDefineService businessDefineService = (BusinessDefineService)Environment.getService("businessDefineService");
		BusinessObject businessObject = businessDefineService.getBusinessObject(record.getClass());
		Field field = businessObject.getField(videoType);
		//获取视频服务
		String serviceName = (String)field.getParameter("serviceName");
		VideoService videoService = (VideoService)Environment.getService(serviceName==null ? "videoService" : serviceName);

		boolean changed = false;
		NodeList embeds = document.getElementsByTagName("embed");
		for(int i=(embeds==null ? -1 : embeds.getLength()-1); i>=0; i--) {
			HTMLElement embed = (HTMLElement)embeds.item(i);
			String videoName = null;
			String videoUrl;
			boolean autoStart = false;
			int width = 0, height = 0;
			try {
				width = Integer.parseInt(embed.getAttribute("width"));
				height = Integer.parseInt(embed.getAttribute("height"));
			}
			catch(Exception e) {
				
			}
			if("flvPlayer".equals(embed.getId())) {
				//<embed id="flvPlayer" title="光良第一次.flv" type="application/x-shockwave-flash" flashvars="logo=/cms/jeaf/flvplayer/icons/player.gif&amp;volume=80&amp;file=/cms/cms/pages/420139132400320000/videos/%E5%85%89%E8%89%AF%E7%AC%AC%E4%B8%80%E6%AC%A1.flv&amp;image=/cms/cms/pages/420139132400320000/videos/%E5%85%89%E8%89%AF%E7%AC%AC%E4%B8%80%E6%AC%A1.jpg" allowfullscreen="true" pluginspage="http://www.macromedia.com/go/getflashplayer" height="260" width="320" src="/cms/jeaf/flvplayer/flvplayer.swf?image=/cms/cms/pages/420139132400320000/videos/%E5%85%89%E8%89%AF%E7%AC%AC%E4%B8%80%E6%AC%A1.jpg&amp;file=/cms/cms/pages/420139132400320000/videos/%E5%85%89%E8%89%AF%E7%AC%AC%E4%B8%80%E6%AC%A1.flv"></embed>
				String flashvars = embed.getAttribute("flashvars");
				int index = flashvars.indexOf("&file=");
				if(index==-1) {
					continue;
				}
				index += "&file=".length();
				videoUrl = flashvars.substring(index, flashvars.indexOf('&', index)); //视频URL
				autoStart = flashvars.indexOf("autostart")!=-1;
			}
			else {
				//<EMBED src=uploadfile/wmv/2011-12/20111225.wmv width=360 height=315 type=video/x-ms-wmv loop="-1" autostart="true"></EMBED>
				videoUrl = embed.getAttribute("src");
				if(videoUrl==null || videoUrl.equals("")) {
					continue;
				}
				int index = videoUrl.indexOf("://");
				if(index!=-1 && ((index = videoUrl.indexOf('/', index+3))==-1 || videoUrl.indexOf('.', index+1)==-1)) {
					continue;
				}
				//检查是否视频
				if(!validateFileType(videoUrl, false, true)) {
					continue;
				}
			}
			videoName = downloadAttachmentIfNeed(record.getId(), videoUrl, videoService.getSavePath(businessObject.getApplicationName(), videoType, record.getId(), true), callback);
			if(videoName==null) { //没有重新下载
				videoName = URLDecoder.decode(videoUrl.substring(videoUrl.lastIndexOf('/') + 1), "utf-8");
			}
			//转换为MP4视频
			String videoPath = videoService.processUploadedFile(businessObject.getApplicationName(), videoType, field, record.getId(), videoName);
			videoName = new File(videoPath).getName();
			Video video = videoService.getVideo(businessObject.getApplicationName(), videoType, record.getId(), videoName, null);
			if(width==0 || height==0) {
				width = video.getVideoWidth();
				height = video.getVideoHeight();
			}
			//创建视频播放器
			HTMLImageElement videoElement = (HTMLImageElement)document.createElement("img");
			videoElement.setId("video");
			videoElement.setTitle(videoName);
			videoElement.setAttribute("width", "" + width);
			videoElement.setAttribute("height", "" + height);
			videoElement.setSrc(video.getPreviewImageUrl());
			videoElement.setAttribute("alt", VideoPlayerUtils.generateVideoPlayerProperties(businessObject.getApplicationName(), record.getId(), videoType, videoName, width, height, autoStart, false));
			embed.getParentNode().replaceChild(videoElement, embed);
			changed = true;
		}
		NodeList imgs = document.getElementsByTagName("img");
		for(int i=0; i<(imgs==null ? 0 : imgs.getLength()); i++) {
			HTMLImageElement img = (HTMLImageElement)imgs.item(i);
			if(!"video".equals(img.getId())) {
				continue;
			}
			String videoProperties = img.getAttribute("alt");
			String applicationName = StringUtils.getPropertyValue(videoProperties, "applicationName");
			String videoName = StringUtils.getPropertyValue(videoProperties, "videoName");
			if(applicationName==null || videoName==null) {
				continue;
			}
			long recordId = StringUtils.getPropertyLongValue(videoProperties, "recordId", 0);
			String sourceVideoType = StringUtils.getPropertyValue(videoProperties, "videoType");
			//检查视频是否属于当前记录
			if(recordId!=record.getId() || !applicationName.equals(businessObject.getApplicationName()) || !sourceVideoType.equals(videoType)) {
				//获取源视频
				VideoService sourceVideoService = (VideoService)FieldUtils.getAttachmentService(applicationName, sourceVideoType, recordId);
				Video video = sourceVideoService==null ? null : sourceVideoService.getVideo(applicationName, sourceVideoType, recordId, videoName, null);
				if(video==null) {
					continue;
				}
				//拷贝视频
				videoService.uploadFile(businessObject.getApplicationName(), videoType, field, record.getId(), video.getFilePath());
				//重置视频参数
				videoProperties = "applicationName=" + businessObject.getApplicationName() +
								  "&recordId=" + record.getId() +
								  "&videoType=" + videoType +
								  "&" + StringUtils.removeQueryParameters(videoProperties, "applicationName,recordId,videoType");
				img.setAttribute("alt", videoProperties);
				changed = true;
			}
			bodyInfo.setVideoCount(bodyInfo.getVideoCount()+1);
			if(bodyInfo.getFirstVideoName()==null) {
				bodyInfo.setFirstVideoName(videoName);
			}
		}
		return changed;
	}
	
	/**
	 * 附件分析
	 * @param bodyInfo
	 * @param document
	 * @param record
	 * @param attachmentType
	 * @param callback
	 * @return
	 * @throws Exception
	 */
	private static boolean analysisAttachments(HTMLBodyInfo bodyInfo, HTMLDocument document, Record record, String attachmentType, HTMLBodyParseCallback callback) throws Exception {
		//获取附件配置
		BusinessDefineService businessDefineService = (BusinessDefineService)Environment.getService("businessDefineService");
		BusinessObject businessObject = businessDefineService.getBusinessObject(record.getClass());
		Field field = businessObject.getField(attachmentType);
		//获取视频服务
		String serviceName = (String)field.getParameter("serviceName");
		//获取附件服务
		AttachmentService attachmentService = (AttachmentService)Environment.getService(serviceName==null ? "attachmentService" : serviceName);
		
		boolean changed = false;
		NodeList links = document.getElementsByTagName("a");
		if(links!=null) {
			for(int i=0; i<links.getLength(); i++) {
				HTMLAnchorElement link = (HTMLAnchorElement)links.item(i);
				if(link.getHref()==null || link.getHref().equals("")) {
					continue;
				}
				int index = link.getHref().indexOf("://");
				if(index!=-1) {
					index = link.getHref().indexOf('/', index+3);
					if(index==-1 || link.getHref().indexOf('.', index+1)==-1) {
						continue;
					}
				}
				//检查文件类型
				if(!validateFileType(link.getHref(), false, false)) {
					continue;
				}
				String attachmentName = downloadAttachmentIfNeed(record.getId(), link.getHref(), attachmentService.getSavePath(businessObject.getApplicationName(), attachmentType, record.getId(), true), callback);
				if(attachmentName!=null) {
					//重新设置链接
					String newHref = attachmentService.createDownload(businessObject.getApplicationName(), attachmentType, record.getId(), attachmentName, false, null);
					if(!link.getHref().equals(newHref)) {
						link.setHref(newHref);
						changed = true;
					}
				}
				bodyInfo.setAttachmentCount(bodyInfo.getAttachmentCount() + 1);
			}
		}
		return changed;
	}
	
	/**
	 * 文件类型校验
	 * @param url
	 * @param isImage
	 * @param isVideo
	 * @return
	 */
	private static boolean validateFileType(String url, boolean isImage, boolean isVideo) {
		String fileName = url;
		if(url.indexOf('?')!=-1) { //动态地址
			try {
				fileName = ((FileDownloadService)Environment.getService("fileDownloadService")).getFileName(url);
			}
			catch(Exception e) {
				Logger.exception(e);
				return false;
			}
		}
		if(fileName==null) {
			return false;
		}
		return FileUtils.validateFileExtension(FileUtils.getDefaultFileExtension(isImage, isVideo), fileName);
	}
	
	/**
	 * 如果附件不在当前资源的附件目录中，下载附件，返回文件名称
	 * @param recordId
	 * @param attachmentUrl
	 * @param downloadPath
	 * @param callback
	 * @param htmlBodyAnalysisSupport
	 * @return
	 * @throws ServiceException
	 */
	private static String downloadAttachmentIfNeed(long recordId, String attachmentUrl, String downloadPath, HTMLBodyParseCallback callback) throws ServiceException {
		if(attachmentUrl.indexOf("/" + recordId + "/")!=-1) { //在当前记录的附件目录中
			//检查附件是否存在
			String fileName = attachmentUrl.substring(attachmentUrl.lastIndexOf('/') + 1);
			if(FileUtils.isExists(downloadPath + fileName)) {
				return fileName; //不需要下载
			}
			try {
				fileName = URLDecoder.decode(fileName, "utf-8");
			}
			catch (UnsupportedEncodingException e) {
				
			}
			if(FileUtils.isExists(downloadPath + fileName)) { //附件已经存在
				return fileName;
			}
		}
		if(callback!=null) {
			String fileName = callback.downloadResource(attachmentUrl);
			if(fileName!=null) {
				return fileName;
			}
		}
		String downloadUrl = null;
		if(attachmentUrl.toLowerCase().startsWith("http://") || attachmentUrl.toLowerCase().startsWith("https://")) { //在互联网上
			downloadUrl = attachmentUrl;
		}
		else if(attachmentUrl.indexOf(":")!=-1) { //如：ftp,mailto,javascript,files,c:,d:
			return null;
		}
		else { //在其他记录的附件目录中
			String applicationUrl = Environment.getWebApplicationLocalUrl();
			int index = applicationUrl.indexOf("/", applicationUrl.indexOf("://") + 3);
			downloadUrl = (index==-1 ? applicationUrl : applicationUrl.substring(0, index)) + attachmentUrl;
		}
		//下载文件
		try {
			String fileName = ((FileDownloadService)Environment.getService("fileDownloadService")).downloadFile(downloadUrl, downloadPath);
			return fileName.substring(fileName.lastIndexOf("/") + 1);
		}
		catch(Exception e) {
			
		}
		return null;
	}
	
	/**
	 * 检查正文中是否仅有一个
	 * @param body
	 * @return
	 * @throws Exception
	 */
	public static boolean isMsWordDocumentOnly(String body) {
		return body.replaceFirst("(?i)<a(.*).doc(.*)>[\\s\\S]*?<\\/a>", "").trim().isEmpty();
	}
	
	/**
	 * 解析正文,下载第一个word文件,转换成HTML正文
	 * @param body
	 * @param appendSelfLink 是否追加原文件链接
	 * @return
	 * @throws Exception
	 */
	public static String msWord2body(String body, boolean appendSelfLink) throws Exception {
		try {
			HTMLParser htmlParser = (HTMLParser)Environment.getService("htmlParser");
			//解析正文
			HTMLDocument document = htmlParser.parseHTMLString(body, "utf-8");
			if(document==null) {
				return null;
			}
			NodeList links = document.getElementsByTagName("a");
			if(links==null || links.getLength()!=1) {
				return null;
			}
			HTMLAnchorElement link = (HTMLAnchorElement)links.item(0);
			if(link.getHref() == null || link.getHref().isEmpty()) {
				return null;
			}
			//创建临时目录，下载WORD文件
			TemporaryFileManageService temporaryFileManageService = (TemporaryFileManageService)Environment.getService("temporaryFileManageService");
			long temporaryId = UUIDLongGenerator.generateId();
			String savePath = FileUtils.createDirectory(Environment.getWebAppPath() + "temp/" + temporaryId + "/");
			temporaryFileManageService.registTemporaryFile(savePath, 1); //记录为临时文件
			//下载WORD
			String docFileName = downloadAttachmentIfNeed(temporaryId, link.getHref(), savePath, null);
			if(docFileName==null || !docFileName.toLowerCase().endsWith(".doc")) {
				return null;
			}
			
			if(!MsWordUtils.saveAsHtml(savePath + docFileName, savePath + "word.html")) { //将word文件转为html文件
				return null;
			}
			body = FileUtils.readStringFromFile(savePath + "word.html", "gbk");
			body = StringUtils.removeWordFormat(body, true, false); //清除WORD格式
			body = body.replaceAll("(?i)src=\"word.files/", "src=\"" + Environment.getWebApplicationLocalUrl() + "/temp/" + temporaryId + "/word.files/")
					   .replaceAll("(?i)href=\"word.files/", "src=\"" + Environment.getWebApplicationLocalUrl() + "/temp/" + temporaryId + "/word.files/");
			if(appendSelfLink) {
				body += "<br/><br/><a href=\"" + link.getHref() + "\">原文下载</a>";
			}
			return body;
		}
		catch(Exception e) {
			Logger.exception(e);
			return null;
		}
	}
	
	/**
	 * 重设通过数据交换得到的HTML正文,替换各类资源的URL
	 * @param body
	 * @param charset
	 * @param record
	 * @return
	 * @throws ServiceException
	 */
	public static String resetExchangeHtmlBody(String body, final String charset, Record record) throws ServiceException {
		BusinessDefineService businessDefineService = (BusinessDefineService)Environment.getService("businessDefineService");
		BusinessObject businessObject = businessDefineService.getBusinessObject(record.getClass());
		if(businessObject==null) {
			return body;
		}
		//获取所有的附件列表
		final List allAttachments = new ArrayList();
		for(Iterator iterator = businessObject.getFields().iterator(); iterator.hasNext();) {
			Field field = (Field)iterator.next();
			if(!"image".equals(field.getType()) && !"video".equals(field.getType()) && !"attachment".equals(field.getType())) { //不是图片、视频和附件
				continue;
			}
			String serviceName = (String)field.getParameter("serviceName");
			serviceName = (serviceName==null || serviceName.equals("") ? field.getType() + "Service" : serviceName);
			AttachmentService attachmentService = (AttachmentService)Environment.getService(serviceName);
			List attachments = attachmentService.list(businessObject.getApplicationName(), field.getName(), record.getId(), false, 0, null);
			if(attachments!=null) {
				allAttachments.addAll(attachments);
			}
		}
		if(allAttachments.isEmpty()) {
			return body;
		}
		HTMLBodyParseCallback callback = new HTMLBodyParseCallback() {
			public String downloadResource(String url) {
				//获取文件名
				url = url.replaceAll("\\\\", "/").replaceAll(":", "/");
				int index = url.lastIndexOf('/');
				if(index!=-1) {
					url = url.substring(index + 1);
				}
				if(charset!=null) {
					try {
						url = URLDecoder.decode(url, charset);
					} 
					catch(UnsupportedEncodingException e) {
					
					}
				}
				//检查文件是否在图片、视频或附件目录中
				Attachment attachment = (Attachment)ListUtils.findObjectByProperty(allAttachments, "name", url);
				return attachment==null ? null : attachment.getName();
			}
		};
		HTMLBodyInfo htmlBodyInfo;
		try {
			htmlBodyInfo = analysisHTMLBody(record, body, callback);
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
		if(htmlBodyInfo.isBodyChanged()) {
			body = htmlBodyInfo.getNewBody();
		}
		//检查是否仅有一个WORD正文,如果是,读取WORD的内容作为正文
		if(isMsWordDocumentOnly(body)) {
			try {
				body = msWord2body(body, true);
			}
			catch (Exception e) {
				
			}
		}
		return body;
	}
}