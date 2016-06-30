/*
 * Created on 2005-6-6
 *
 */
package com.yuanluesoft.jeaf.filetransfer.services;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.upload.FormFile;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.filetransfer.exception.FileTransferException;

/**
 * 
 * @author root
 *
 */
public interface FileUploadService {
	public static final String PARTIAL_FILE_POSTFIX = ".partial";
	
	/**
	 * 上传文件到文件夹,返回文件名
	 * @param formFile
	 * @param uploadDirectory
	 * @param overwrite 是否覆盖
	 * @return
	 * @throws FileTransferException
	 */
	public String httpUpload(FormFile formFile, String uploadDirectory, boolean overwrite) throws FileTransferException;
	
	/**
	 * 在指定的目录生成一个下载许可证并返回
	 * @param uploadPath
	 * @param fileSize
	 * @param maxUploadSize
	 * @param fileExtension 指定允许上传的文件类型,格式: 图片|*.jpg|视频|*.avi|
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public String createUploadPassport(String uploadPath, int fileSize, int maxUploadSize, String fileExtension, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 上传文件,由fileUploadServlet调用
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws ServiceException, IOException;
}
