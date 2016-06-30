package com.yuanluesoft.cms.capture.service;

import com.yuanluesoft.cms.capture.model.CapturedRecord;
import com.yuanluesoft.cms.capture.pojo.CmsCaptureTask;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 抓取记录处理服务
 * @author linchuan
 *
 */
public interface CaptureRecordService {

	/**
	 * 处理抓取到的记录
	 * @param captureTask
	 * @param capturedRecord
	 * @throws ServiceException
	 */
	public void processCapturedRecord(CmsCaptureTask captureTask, CapturedRecord capturedRecord) throws ServiceException;
}