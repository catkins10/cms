/*
 * Created on 2005-6-6
 *
 */
package com.yuanluesoft.jeaf.filetransfer.services;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.filetransfer.exception.FileTransferException;

/**
 * 
 * @author root
 *
 */
public interface FileDownloadService {

	/**
	 * 文件下载
	 * @param request
	 * @param response
	 * @param filePath
	 * @param mimeType null则系统自动设置
	 * @param attachment
	 * @param outputFileName 空则使用原来的文件名
	 * @throws FileTransferException
	 */
	public void httpDownload(HttpServletRequest request, HttpServletResponse response, String filePath, String mimeType, boolean attachment, String outputFileName) throws FileTransferException;

	/**
	 * 下载文件,文件内容存放在缓存中
	 * @param request
	 * @param response
	 * @param bytes
	 * @param fileName
	 * @param mimeType
	 * @param attachment
	 * @throws FileTransferException
	 */
	public void httpDownload(HttpServletRequest request, HttpServletResponse response, byte[] bytes, String fileName, String mimeType, boolean attachment) throws FileTransferException;

	/**
	 * 下载文件到下载目录
	 * @param url
	 * @param downloadDirectory
	 * @return
	 * @throws FileTransferException
	 */
	public String downloadFile(String url, String downloadDirectory) throws FileTransferException;
	
	/**
	 * 获取文件名称
	 * @param url
	 * @return
	 * @throws FileTransferException
	 */
	public String getFileName(String url) throws FileTransferException;
	
	/**
	 * 下载附件,由fileDownloadServlet调用
	 * @param request
	 * @param response
	 * @return
	 * @throws ServiceException
	 */
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws ServiceException, IOException;
}