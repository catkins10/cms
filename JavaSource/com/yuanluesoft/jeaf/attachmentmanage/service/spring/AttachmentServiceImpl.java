/*
 * Created on 2005-9-7
 *
 */
package com.yuanluesoft.jeaf.attachmentmanage.service.spring;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.upload.FormFile;

import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.filetransfer.exception.FileTransferException;
import com.yuanluesoft.jeaf.filetransfer.services.FileUploadService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Base64Decoder;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDStringGenerator;

/**
 *
 * @author LinChuan
 * 
 */
public class AttachmentServiceImpl implements AttachmentService {
	private String baseDirectory = "/attachments/"; //附件目录
	//下载相关属性
	private Cache fileDownloadCache; //文件下载缓存,缓存下载路径
	
	//上传相关属性
	private FileUploadService fileUploadService;
	private boolean renameUploadedFile; //是否自动修改上传文件的名称
	
	//附件名称过滤，过滤掉系统文件、缩略图等
	private String[] attachmentNameFilter = {"Thumbs.db", "_breviary_", FileUploadService.PARTIAL_FILE_POSTFIX};
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#list(java.lang.String, java.lang.String, long, int, javax.servlet.http.HttpServletRequest)
	 */
	public List list(String applicationName, String type, long mainRecordId, boolean iconURLRequired, int max, HttpServletRequest request) throws ServiceException {
        String path = getSavePath(applicationName, type, mainRecordId, false);
		File directory = new File(path);
		if(!directory.exists()) {
			return null;
		}
		File[] files = directory.listFiles();
		List  attachmentList = new ArrayList();
		for(int i=0; i<files.length && (max==0 || attachmentList.size()<max); i++) {
			String fileName = files[i].getName();
			if(files[i].isDirectory()) { //目录
				continue;
			}
		    if(attachmentNameFilter(fileName)) { //被过滤
		        continue;
		    }
		    Attachment attachment = new Attachment();
			attachment.setName(fileName);
			attachment.setFilePath(files[i].getPath());
			attachment.setSize(files[i].length());
			attachment.setLastModified(files[i].lastModified());
			attachment.setType(type);
			attachment.setApplicationName(applicationName);
			attachment.setRecordId(mainRecordId);
			attachment.setService(this);
		    attachment.setUrlInline(createDownload(applicationName, type, mainRecordId, attachment.getName(), false, request));
            attachment.setUrlAttachment(createDownload(applicationName, type, mainRecordId, attachment.getName(), true, request));
            if(iconURLRequired) {
            	attachment.setIconURL(FileUtils.getIconURL(fileName));
            }
        	attachmentList.add(attachment);
        }
		return attachmentList.isEmpty() ? null : attachmentList;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#listAttachmentTypes(java.lang.String, long)
	 */
	public List listTypes(String applicationName, long mainRecordId) throws ServiceException {
		File path = new File(getSavePath(applicationName, null, mainRecordId, false));
		if(!path.exists() || !path.isDirectory()) {
			return null;
		}
		File[] files = path.listFiles();
		if(files==null || files.length==0) {
			return null;
		}
		List attachmentTypes = new ArrayList();
		for(int i=0; i<files.length; i++) {
			if(!files[i].isDirectory()) { //附件目录下有文件
				if(attachmentNameFilter(files[i].getName())) { //被过滤掉
			        continue;
			    }
				if(attachmentTypes.indexOf("")==-1) {
					attachmentTypes.add(""); //添加附件类型""
				}
				continue;
			}
			File[] attachments = files[i].listFiles();
			if(attachments!=null && attachments.length>0) {
				attachmentTypes.add(files[i].getName());
			}
		}
		return attachmentTypes.isEmpty() ? null : attachmentTypes;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.core.service.AttachmentService#deleteAttachment(long, java.lang.String)
	 */
	public void delete(String applicationName, long mainRecordId, String type, String names) throws ServiceException {
		Logger.warn("AttachmentService: delete attachment " + applicationName + "/" + mainRecordId + "/" + type + "/" + names + ".");
		String savePath = getSavePath(applicationName, type, mainRecordId, false);
		String[] fileNames = names.split("\\*");
        for(int i=0; i<fileNames.length; i++) {
            new File(savePath + fileNames[i]).delete();
        }
		File directory = new File(savePath);
		if(directory.list().length==0) { //附件已经全部被删除
			deleteAll(applicationName, type, mainRecordId);
		}
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.erp.core.service.AttachmentService#deleteAllAttachments(java.lang.String, long)
	 */
	public void deleteAll(String applicationName, String type, long mainRecordId) throws ServiceException {
		FileUtils.deleteDirectory(getSavePath(applicationName, type, mainRecordId, false));
		//检查是否所有附件类型都已删除
		String recordAttachmentPath = getSavePath(applicationName, null, mainRecordId, false);
		File directory = new File(recordAttachmentPath);
		if(directory.exists() && directory.list().length==0) { //没有任何附件,删除主记录的附件目录
			FileUtils.deleteDirectory(recordAttachmentPath);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.core.service.AttachmentService#uploadAttachment(long, org.apache.struts.upload.FormFile)
	 */
	public String upload(String applicationName, String type, Field fieldDefine, long mainRecordId, FormFile formFile) throws ServiceException {
		int maxUploadSize = StringUtils.parseInt(fieldDefine==null ? null : (String)fieldDefine.getParameter("maxUploadSize"), getDefaultMaxUpload());
		if(formFile.getFileSize()>maxUploadSize) {
			throw new ServiceException("文件大小不能超过" + StringUtils.getFileSize(maxUploadSize));
		}
		//检查附件类型是否被允许上传
		String fileExtension = (String)fieldDefine.getParameter("fileExtension");
		if(fileExtension==null) {
			fileExtension = FileUtils.getDefaultFileExtension("image".equals(fieldDefine.getType()), "video".equals(fieldDefine.getType()));
		}
		if(!FileUtils.validateFileExtension(fileExtension, formFile.getFileName())) {
			throw new ServiceException("该类型文件不被允许上传");
		}
		String path = getSavePath(applicationName, type, mainRecordId, true);
		String fileName;
		try {
			fileName = fileUploadService.httpUpload(formFile, path, false);
		}
		catch (FileTransferException e) {
			Logger.exception(e);
			throw new ServiceException("上传失败");
		}
		if(renameUploadedFile) { //重命名
			int index = fileName.lastIndexOf('.');
			String newFileName = UUIDStringGenerator.generateId().substring(0, 32) + (index==-1 ? "" : fileName.substring(index));
			FileUtils.renameFile(path + fileName, path + newFileName, true, true);
			fileName = newFileName;
		}
		if(fieldDefine!=null) {
			deleteSpare(applicationName, type, fieldDefine, mainRecordId); //删除多余的附件
		}
		return path + fileName;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#upload(java.lang.String, java.lang.String, long, java.lang.String, java.lang.String)
	 */
	public String upload(String applicationName, String type, Field fieldDefine, long mainRecordId, String fileName, String base64FileData) throws ServiceException {
		String attachmentPath = getSavePath(applicationName, type, mainRecordId, true);
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(attachmentPath + fileName, true);
			Base64Decoder base64Decoder = new Base64Decoder();
			output.write(base64Decoder.decode(base64FileData));
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
		finally {
			try {
				output.close();
			}
			catch(Exception e) {
				
			}
		}
		return attachmentPath + fileName;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#uploadAttachment(java.lang.String, java.lang.String, long, java.lang.String)
	 */
	public String uploadFile(String applicationName, String type, Field fieldDefine, long mainRecordId, String filePath) throws ServiceException {
		//拷贝文件到附件目录
		return FileUtils.copyFile(filePath, getSavePath(applicationName, type, mainRecordId, true), true, true);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#upload(java.lang.String, java.lang.String, long, java.util.List)
	 */
	public List uploadFiles(String applicationName, String type, Field fieldDefine, long mainRecordId, List filePaths) throws ServiceException {
		if(filePaths==null) {
			return null;
		}
		List paths = new ArrayList();
		for(Iterator iterator = filePaths.iterator(); iterator.hasNext();) {
			String filePath = (String)iterator.next();
			paths.add(uploadFile(applicationName, type, null, mainRecordId, filePath));
		}
		return paths;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#processUploadedFile(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.business.model.Field, long, java.lang.String)
	 */
	public String processUploadedFile(String applicationName, String type, Field fieldDefine, long mainRecordId, String name) throws Exception {
		String path = getSavePath(applicationName, type, mainRecordId, false) + name;
		deleteSpare(applicationName, type, fieldDefine, mainRecordId); //删除多余的附件
		return path;
	}
	
	/**
	 * 删除多余的附件,fieldDefine.length>0时有效
	 * @param applicationName
	 * @param fieldDefine
	 * @param mainRecordId
	 * @throws Exception
	 */
	private void deleteSpare(String applicationName, String type, Field fieldDefine, long mainRecordId) throws ServiceException {
		int length = fieldDefine==null ? 0 : StringUtils.parseInt(fieldDefine.getLength(), 0);
		if(length<=0) {
			return; //没有指定附件数量,不需要删除多余的附件
		}
		String serviceName = (String)fieldDefine.getParameter("serviceName");
		AttachmentService attachmentService = (AttachmentService)Environment.getService(serviceName==null || serviceName.equals("") ? fieldDefine.getType() + "Service" : serviceName);
		//获取附件列表
		List attachments = attachmentService.list(applicationName, type, mainRecordId, false, 0, null);
		if(attachments.size()<=length) {
			return; //没有超出限制
		}
		//按最后修改时间排序
		Collections.sort(attachments, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				long time = ((Attachment)arg0).getLastModified() - ((Attachment)arg1).getLastModified();
				return time == 0 ? 0 : (time>0 ? -1 : 1);
			}
		});
		//删除多余的附件
		for(int i=attachments.size()-1; i>=length; i--) {
			Attachment attachment = (Attachment)attachments.get(i);
			attachmentService.delete(applicationName, mainRecordId, type, attachment.getName());
		}
	}
	
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#replaceAttachmentWith(java.lang.String, long, long, java.lang.String)
     */
	public void replace(String fromApplicationName, long fromRecordId, String fromType, String toApplicationName, long toRecordId, String toType) throws ServiceException {
	    if(fromType==null || fromType.equals("")) {
            fromType = "attachment";
        }
	    if(toType==null || toType.equals("")) {
            toType = "attachment";
        }
	    if(fromApplicationName.equals(toApplicationName) &&
	       fromRecordId==toRecordId &&
	       fromType.equals(toType)) { //源记录和目标记录相同,不做替换
	        return;
	    }
	    //获取源记录的附件
		File directory = new File(getSavePath(fromApplicationName, fromType, fromRecordId, false));
		if(!directory.exists()) { //源记录没有附件,则删除目标记录的附件
		    deleteAll(toApplicationName, toType, toRecordId);
		}
		else {
			File[] files = directory.listFiles();
			if(files.length==0) { //源记录没有附件,则删除目标记录的附件
			    deleteAll(toApplicationName, toType, toRecordId);
			}
			else {
			    //获取目标记录的附件目录
			    directory = new File(getSavePath(toApplicationName, toType, toRecordId, true));
			    File[] toFiles = directory.listFiles();
			    for(int i=toFiles.length - 1; i>=0; i--) {
			        boolean found = false;
					for(int j = files.length - 1; j>=0; j--) {
					    if(files[j].getName().equals(toFiles[i].getName())) { //文件名相同
					        if(files[j].lastModified()==toFiles[i].lastModified()) { //最后修改时间相同,则不需要再复制
					            found = true;
					        }
					        break;
					    }
					}
					if(!found) { //删除不需要的文件
					    toFiles[i].delete();
					}
				}
			    //复制文件
			    for(int i=files.length - 1; i>=0; i--) {
			        FileUtils.copyFile(files[i].getPath(), directory.getPath() + "/" + files[i].getName(), true, true);
			    }
			}
		}
    }

	/**
	 * 获取附件目录
	 * @param applicationName
	 * @param type
	 * @param id
	 * @param mkdir
	 * @return
	 */
	public String getSavePath(String applicationName, String type, long id, boolean mkdir) {
		String path = applicationName + "/" + id + "/"  + (type==null || type.equals("") ? "" : type + "/");
		if(!mkdir) {
			return baseDirectory + path;
		}
		return FileUtils.createDirectory(baseDirectory + path);
	}
		
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#generateDownloadURL(java.lang.String, java.lang.String, long, java.lang.String, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public String createDownload(String applicationName, String type, long mainRecordId, String name, boolean asAttachment, HttpServletRequest request) throws ServiceException {
     	if(isDynamicUrl(applicationName, mainRecordId, type, name)) { //需要生成动态的下载地址
        	return createDynamicDownload(getSavePath(applicationName, type, mainRecordId, false) + name, asAttachment, request);
       	}
        else { //不需要生成动态的下载地址
        	return Environment.getContextPath() + "/attachments/" + applicationName + "/" + type + "/" + mainRecordId + "/" + FileUtils.encodeFileName(name, "utf-8") + (asAttachment ? "?attachment=true" : "");
        }
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.filetransfer.services.FileDownloadService#createDynamicDownload(java.lang.String, boolean, javax.servlet.http.HttpServletRequest)
	 */
	public String createDynamicDownload(String filePath, boolean attachment, HttpServletRequest request) throws ServiceException {
		//缓存文件
    	String cacheKey = UUIDStringGenerator.generateId().toLowerCase();
    	try {
			fileDownloadCache.put(cacheKey, filePath);
	    }
		catch (Exception e) {
			throw new ServiceException(e);
		}
    	int index = filePath.lastIndexOf("/");
    	if(index==-1) {
    		index = filePath.lastIndexOf("\\");
    	}
    	try {
    		filePath = URLEncoder.encode(filePath.substring(index + 1), "utf-8");
		} 
		catch (UnsupportedEncodingException e) {
			
		}
		return Environment.getWebApplicationUrl() + "/attachments/dynamic/" + cacheKey + "/" + filePath + (attachment ? "?attachment=true" : "");
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#getDownloadFilePath(java.lang.String)
	 */
	public String getDownloadFilePath(String downloadUrl) throws ServiceException {
		int index = downloadUrl.indexOf("/dynamic/");
		if(index!=-1) { //动态URL
			index += "/dynamic/".length();
			String cacheKey = downloadUrl.substring(index, downloadUrl.indexOf('/', index)); //解析下载Cache Key
			//从缓存获取文件路径
			try {
				return (String)fileDownloadCache.get(cacheKey);
			}
			catch(CacheException e) {
				throw new ServiceException(e);
			}
		}
		//静态URL
		index = downloadUrl.indexOf('?');
		downloadUrl = downloadUrl.substring(downloadUrl.indexOf("/attachments/") + "/attachments/".length(), index==-1 ? downloadUrl.length() : index);
		int fileNameIndex = downloadUrl.lastIndexOf("/");
		int recordIdIndex = downloadUrl.lastIndexOf("/", fileNameIndex - 1);
		int attachmentTypeIndex = downloadUrl.lastIndexOf("/", recordIdIndex - 1);
		String attachmentType = downloadUrl.substring(attachmentTypeIndex + 1, recordIdIndex);
		String applicationName = downloadUrl.substring(0, attachmentTypeIndex);
		long recordId = Long.parseLong(downloadUrl.substring(recordIdIndex + 1, fileNameIndex));
		String name = downloadUrl.substring(fileNameIndex + 1);
		//获取附件路径
		String filePath = getSavePath(applicationName, attachmentType, recordId, false);
		name = decodeFileName(filePath, name);
		//检查文件类型是否需要生成动态下载地址
		if(isDynamicUrl(applicationName, recordId, attachmentType, name)) {
			throw new ServiceException();
		}
		return filePath + name;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#getFileType(java.lang.String)
	 */
	public String getFileType(String downloadUrl) throws ServiceException {
		int fileNameIndex = downloadUrl.lastIndexOf("/");
		int recordIdIndex = downloadUrl.lastIndexOf("/", fileNameIndex - 1);
		int attachmentTypeIndex = downloadUrl.lastIndexOf("/", recordIdIndex - 1);
		return downloadUrl.substring(attachmentTypeIndex + 1, recordIdIndex);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService#createUploadPassport(java.lang.String, com.yuanluesoft.jeaf.business.model.Field, long, int, javax.servlet.http.HttpServletRequest)
	 */
	public String createUploadPassport(String applicationName, String type, Field fieldDefine, long mainRecordId, int fileSize, HttpServletRequest request) throws Exception {
		//检查上传的文件或目录大小是否超出限制
		int maxUploadSize = StringUtils.parseInt((String)fieldDefine.getParameter("maxUploadSize"), 100000000); //默认100M
		//获取文件扩展名
		String fileExtension = (String)fieldDefine.getParameter("fileExtension");
		if(fileExtension==null) {
			fileExtension = FileUtils.getDefaultFileExtension("image".equals(fieldDefine.getType()), "video".equals(fieldDefine.getType()));
		}
		//创建附件上传许可证
		String savePath = getSavePath(applicationName, type, mainRecordId, false);
		//获取最大上传线程数
		return fileUploadService.createUploadPassport(savePath, fileSize, maxUploadSize, fileExtension, request);
	}

	/**
	 * 文件名解码
	 * @param dir
	 * @param fileNameEncoded
	 * @return
	 * @throws ServiceException
	 */
	protected String decodeFileName(String dir, String fileNameEncoded) throws ServiceException {
		if(FileUtils.isExists(dir + fileNameEncoded)) {
			return fileNameEncoded;
		}
		try {
			//UTF-8解码
			String fileNameDecoded = URLDecoder.decode(fileNameEncoded, "UTF-8");
			if(FileUtils.isExists(dir + fileNameDecoded)) {
				return fileNameDecoded;
			}
			//GBK解码
			fileNameDecoded = URLDecoder.decode(fileNameEncoded, "GBK");
			if(FileUtils.isExists(dir + fileNameDecoded)) {
				return fileNameDecoded;
			}
			//字符集转换
			fileNameDecoded = URLDecoder.decode(new String(fileNameEncoded.getBytes("iso8859-1"), "GBK"), "GBK");
			return fileNameDecoded;
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
	}

	/**
	 * 是否动态URL
	 * @param applicationName
	 * @param recordId
	 * @param attachmentType
	 * @param name
	 * @return
	 * @throws ServiceException
	 */
	private boolean isDynamicUrl(String applicationName, long recordId, String attachmentType, String name) throws ServiceException {
		Field field = FieldUtils.getAttachmentField(applicationName, attachmentType, recordId);
		if(field==null) {
			return true;
		}
		boolean dynamicUrl = "true".equals(field.getParameter("dynamicUrl"));
		if(dynamicUrl && "video".equals(field.getType()) && name.endsWith(".jpg")) { //动态地址、视频、获取预览图URL
			dynamicUrl = "true".equals(field.getParameter("previewImageDynamicUrl"));
		}
		return dynamicUrl;
	}
	
	/**
	 * 获取默认的最大上传字节数
	 * @return
	 */
	protected int getDefaultMaxUpload() {
		return 100000000; //100M
	}
	
	/**
	 * 附件名称过滤
	 * @param fileName
	 * @return
	 */
	protected boolean attachmentNameFilter(String fileName) {
		for(int i=0; i<attachmentNameFilter.length; i++) {
			if(fileName.indexOf(attachmentNameFilter[i])!=-1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return Returns the baseDirectory.
	 */
	public String getBaseDirectory() {
		return baseDirectory;
	}
	
	/**
	 * @param baseDirectory The baseDirectory to set.
	 */
	public void setBaseDirectory(String baseDirectory) {
		baseDirectory = baseDirectory.replaceAll("\\x5c", "/");
		if(!baseDirectory.endsWith("/")) {
			baseDirectory += "/";
		}
		try {
			baseDirectory = FileUtils.createDirectory(baseDirectory);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		this.baseDirectory = baseDirectory;
	}
	
	/**
	 * @return the renameUploadedFile
	 */
	public boolean isRenameUploadedFile() {
		return renameUploadedFile;
	}

	/**
	 * @param renameUploadedFile the renameUploadedFile to set
	 */
	public void setRenameUploadedFile(boolean renameUploadedFile) {
		this.renameUploadedFile = renameUploadedFile;
	}
	/**
	 * @return the fileUploadService
	 */
	public FileUploadService getFileUploadService() {
		return fileUploadService;
	}

	/**
	 * @param fileUploadService the fileUploadService to set
	 */
	public void setFileUploadService(FileUploadService fileUploadService) {
		this.fileUploadService = fileUploadService;
	}

	/**
	 * @return the fileDownloadCache
	 */
	public Cache getFileDownloadCache() {
		return fileDownloadCache;
	}

	/**
	 * @param fileDownloadCache the fileDownloadCache to set
	 */
	public void setFileDownloadCache(Cache fileDownloadCache) {
		this.fileDownloadCache = fileDownloadCache;
	}
}