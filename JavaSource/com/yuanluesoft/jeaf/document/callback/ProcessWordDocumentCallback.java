package com.yuanluesoft.jeaf.document.callback;

import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 处理WORD文档回调
 * @author chuan
 *
 */
public interface ProcessWordDocumentCallback {
	
	/**
	 * 处理WORD文档
	 * @param documentPath 文件路径
	 * @param pdfDocumentPath PDF文件路径
	 * @param officalDocumentPath 正式文件路径
	 * @param htmlPagePath HTML页面路径
	 * @param htmlFilesPath HTML页面附件路径
	 * @param pageCount 页数
	 * @param pageWidth 页面宽度
	 * @param recordListChanges 记录列表变动情况
	 * @throws ServiceException
	 */
	public void process(String documentPath, String pdfDocumentPath, String officalDocumentPath, String htmlPagePath, String htmlFilesPath, int pageCount, double pageWidth, List recordListChanges) throws ServiceException;
	
}