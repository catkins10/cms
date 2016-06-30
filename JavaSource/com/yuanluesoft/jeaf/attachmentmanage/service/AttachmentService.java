/*
 * Created on 2005-9-7
 *
 */
package com.yuanluesoft.jeaf.attachmentmanage.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.upload.FormFile;

import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface AttachmentService {
	
	/**
	 * 上传附件,返回上传后的文件路径
	 * @param applicationName
	 * @param type
	 * @param fieldDefine
	 * @param mainRecordId
	 * @param formFile
	 * @return
	 * @throws ServiceException
	 */
	public String upload(String applicationName, String type, Field fieldDefine, long mainRecordId, FormFile formFile) throws ServiceException;
	
	/**
	 * 上传base64编码附件,追加到文件的末尾,返回上传后的文件路径
	 * @param applicationName
	 * @param type
	 * @param fieldDefine
	 * @param mainRecordId
	 * @param fileName
	 * @param base64FileData
	 * @return
	 * @throws ServiceException
	 */
	public String upload(String applicationName, String type, Field fieldDefine, long mainRecordId, String fileName, String base64FileData) throws ServiceException;
	
	/**
	 * 上传附件,文件形式,返回上传后的文件完整路径
	 * @param applicationName
	 * @param type
	 * @param fieldDefine
	 * @param mainRecordId
	 * @param filePath
	 * @return
	 * @throws ServiceException
	 */
	public String uploadFile(String applicationName, String type, Field fieldDefine, long mainRecordId, String filePath) throws ServiceException;
	
	/**
	 * 上传附件,文件形式,多个
	 * @param applicationName
	 * @param type
	 * @param fieldDefine
	 * @param mainRecordId
	 * @param filePaths
	 * @return
	 * @throws ServiceException
	 */
	public List uploadFiles(String applicationName, String type, Field fieldDefine, long mainRecordId, List filePaths) throws ServiceException;
	
	/**
	 * 获取附件列表
	 * @param type
	 * @param mainRecordId
	 * @param iconURLRequired
	 * @param max
	 * @return
	 * @throws ServiceException
	 */
	public List list(String applicationName, String type, long mainRecordId, boolean iconURLRequired, int max, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 获取附件类型列表
	 * @param applicationName
	 * @param mainRecordId
	 * @return
	 * @throws ServiceException
	 */
	public List listTypes(String applicationName, long mainRecordId) throws ServiceException;
	
	/**
	 * 删除一个或多个附件
	 * @param mainRecordId
	 * @param type
	 * @param names 附件名称列表,用\t分隔
	 * @throws ServiceException
	 */
	public void delete(String applicationName, long mainRecordId, String type, String names) throws ServiceException;
	
	/**
	 * 删除全部指定类型附件
	 * @param applicationName
	 * @param type 空时,删除所有类型的附件
	 * @param mainRecordId
	 * @throws ServiceException
	 */
	public void deleteAll(String applicationName, String type, long mainRecordId) throws ServiceException;
	
	/**
	 * 用源记录的附件替换目标记录的附件
	 * @param fromApplicationName
	 * @param fromRecordId
	 * @param fromType
	 * @param toApplicationName
	 * @param toRecordId
	 * @param toType
	 * @throws ServiceException
	 */
	public void replace(String fromApplicationName, long fromRecordId, String fromType, String toApplicationName, long toRecordId, String toType) throws ServiceException;
	
	/**
	 * 获取附件目录
	 * @param applicationName
	 * @param type
	 * @param id
	 * @param mkdir
	 * @return
	 */
	public String getSavePath(String applicationName, String type, long id, boolean mkdir);
	
	/**
	 * 按应用+附件类型+主记录ID生成一个下载,返回URL
	 * @param applicationName
	 * @param type
	 * @param mainRecordId
	 * @param name
	 * @param asAttachment
	 * @param request 允许所有人访问时为null
	 * @return
	 * @throws ServiceException
	 */
	public String createDownload(String applicationName, String type, long mainRecordId, String name, boolean asAttachment, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 创建动态下载链接
	 * @param filePath
	 * @param attachment
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public String createDynamicDownload(String filePath, boolean attachment, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 根据URL获取下载的文件路径
	 * @param downloadFilePath
	 * @return
	 * @throws ServiceException
	 */
	public String getDownloadFilePath(String downloadUrl) throws ServiceException;
	
	/**
	 * 获取文件类型
	 * @param downloadUrl
	 * @return
	 * @throws ServiceException
	 */
	public String getFileType(String downloadUrl) throws ServiceException;
	
	/**
	 * 创建上传许可证
	 * @param applicationName
	 * @param type
	 * @param fieldDefine
	 * @param mainRecordId
	 * @param fileSize
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String createUploadPassport(String applicationName, String type, Field fieldDefine, long mainRecordId, int fileSize, HttpServletRequest request) throws Exception;
	
	/**
	 * 处理上传后的附件,返回完整的文件路径
	 * @param applicationName
	 * @param type
	 * @param fieldDefine
	 * @param mainRecordId
	 * @param name
	 * @throws Exception
	 */
	public String processUploadedFile(String applicationName, String type, Field fieldDefine, long mainRecordId, String name) throws Exception;
}