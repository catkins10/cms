package com.yuanluesoft.cms.capture.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.capture.model.CapturedRecord;
import com.yuanluesoft.cms.capture.model.CapturedRecordList;
import com.yuanluesoft.cms.capture.model.SourcePage;
import com.yuanluesoft.cms.capture.pojo.CmsCaptureTask;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public interface CaptureService extends BusinessService {
	public final static String ATTRIBUTE_STARTUP_TASK_IDS = "TOSTARTUPTASKIDS"; //需要启动的任务ID

	/**
	 * 获取源页面
	 * @param task
	 * @return
	 * @throws ServiceException
	 */
	public SourcePage loadSourcePage(CmsCaptureTask task) throws ServiceException;
	
	/**
	 * 加载字段配置
	 * @param task
	 * @param listPage 是否列表页面
	 * @return
	 * @throws ServiceException
	 */
	public List loadCaptureFieldConfig(CmsCaptureTask task, boolean listPage) throws ServiceException;
	
	/**
	 * 保存抓取字段配置
	 * @param task
	 * @param request
	 * @throws ServiceException
	 */
	public void saveCaptureFieldConfig(CmsCaptureTask task, HttpServletRequest request) throws ServiceException;
	
	/**
	 * 拷贝抓取任务
	 * @param task
	 * @param description
	 * @param captureURL
	 * @return
	 * @throws ServiceException
	 */
	public CmsCaptureTask copyTask(CmsCaptureTask task, String description, String captureURL) throws ServiceException;
	
	/**
	 * 抓取列表页面
	 * @param task
	 * @param pageURL
	 * @param pageIndex
	 * @return
	 * @throws ServiceException
	 */
	public CapturedRecordList captureListPage(CmsCaptureTask task, String pageURL, int pageIndex) throws ServiceException;
	
	/**
	 * 抓取内容页面
	 * @param task
	 * @param pageURL
	 * @return
	 * @throws ServiceException
	 */
	public CapturedRecord captureContentPage(CmsCaptureTask task, String pageURL) throws ServiceException;
	
	/**
	 * 抓取任务解码
	 * @param taskText
	 * @return
	 */
	public CmsCaptureTask decodeCaptureTask(String taskText);
	
	/**
	 * 启动抓取
	 * @param task
	 * @return
	 * @throws ServiceException
	 */
	public void startupCapture(CmsCaptureTask task) throws ServiceException;
	
	/**
	 * 按ID获取抓取任务列表
	 * @param taskIds
	 * @return
	 * @throws ServiceException
	 */
	public List listCaptureTasks(String taskIds) throws ServiceException;
}