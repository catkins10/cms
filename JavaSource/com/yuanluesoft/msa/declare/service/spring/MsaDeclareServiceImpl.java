package com.yuanluesoft.msa.declare.service.spring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.yuanluesoft.cms.capture.model.CapturedRecord;
import com.yuanluesoft.cms.capture.model.CapturedRecordList;
import com.yuanluesoft.cms.capture.pojo.CmsCaptureTask;
import com.yuanluesoft.cms.capture.service.CaptureService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Base64Decoder;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.ObjectSerializer;
import com.yuanluesoft.msa.declare.model.Bulletin;
import com.yuanluesoft.msa.declare.service.MsaDeclareService;

/**
 * 从福建海事业务申报网(http://61.154.10.90:90)获取今日申报、公告通知的内容
 * @author linchuan
 *
 */
public class MsaDeclareServiceImpl implements MsaDeclareService{
	private CaptureService captureService; //页面抓取服务
	private String declareCaptureUrl = null; //申报抓取页面的URL
	private String bulletinCaptureUrl = null ; //通知公告抓取页面的URL
	
	private int declareCaptureIntervalMinutes = 20; //申报记录抓取间隔分钟数
	private int bulletinCaptureIntervalMinutes = 60; //通知公告记录抓取间隔分钟数
	
	//私有属性
	private CmsCaptureTask declareCaptureTask; //电子申报记录抓取任务
	private CmsCaptureTask bulletinCaptureTask; //通知公告记录抓取任务
	private List capturedDeclares = new ArrayList(); //抓取到的申报记录
	private List capturedBulletins = new ArrayList(); //抓取到的通知公告记录
	private Timestamp declareCaptureTime; //电子申报最后抓取时间
	private Timestamp bulletinCaptureTime; //通知公告最后抓取时间
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.msa.declare.service.MsaDeclareService#listDeclares()
	 */
	public List listDeclares() throws ServiceException {
		return listRecords(false);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.msa.declare.service.MsaDeclareService#listBulletins()
	 */
	public List listBulletins() throws ServiceException {
		return listRecords(true);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.msa.declare.service.MsaDeclareService#getBulletin(long)
	 */
	public Bulletin getBulletin(String sourceUrl) throws ServiceException {
		if(capturedBulletins.isEmpty()) {
			listBulletins();
		}
		Bulletin bulletin = (Bulletin)ListUtils.findObjectByProperty(capturedBulletins, "sourceUrl", sourceUrl);
		if(bulletin==null) {
			return null;
		}
		CapturedRecord captureRecord = captureService.captureContentPage(bulletinCaptureTask, bulletin.getSourceUrl());
		if(captureRecord!=null) {
			Bulletin bulletinDetail = (Bulletin)captureRecord.getRecord();
			bulletin.setBody(bulletinDetail.getBody());
			if(bulletinDetail.getEditor()!=null) {
				bulletin.setEditor(bulletinDetail.getEditor());
			}
		}
		return bulletin;
	}
	
	/**
	 * 获取记录列表
	 * @param isBulletin
	 * @return
	 * @throws ServiceException
	 */
	public synchronized List listRecords(final boolean isBulletin) throws ServiceException {
		final String captureUrl = isBulletin ? bulletinCaptureUrl : declareCaptureUrl;
		if(captureUrl==null || captureUrl.isEmpty()) {
			return null;
		}
		int captureIntervalMinutes = (isBulletin ? bulletinCaptureIntervalMinutes : declareCaptureIntervalMinutes);
		final List capturedList = (isBulletin ? capturedBulletins : capturedDeclares);
		final CmsCaptureTask captureTask = (isBulletin ? bulletinCaptureTask : declareCaptureTask);
		Timestamp captureTime = (isBulletin ? bulletinCaptureTime : declareCaptureTime);
		if(captureTime==null || captureTime.before(DateTimeUtils.add(DateTimeUtils.now(), Calendar.MINUTE, -captureIntervalMinutes + 1))) {
			if(isBulletin) {
				bulletinCaptureTime = DateTimeUtils.now();
			}
			else {
				declareCaptureTime = DateTimeUtils.now();
			}
			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					if(Logger.isDebugEnabled()) {
						Logger.debug("MsaDeclareService: capture page " + captureUrl + ".");
					}
					timer.cancel();
					capturedList.clear();
					CapturedRecordList capturedRecordList;
					try {
						capturedRecordList = captureService.captureListPage(captureTask, captureUrl, 0);
					} 
					catch (ServiceException e) {
						Logger.exception(e);
						return;
					}
					if(capturedRecordList==null || capturedRecordList.getRecords()==null || capturedRecordList.getRecords().isEmpty()) {
						return;
					}
					for(Iterator iterator = capturedRecordList.getRecords().iterator(); iterator.hasNext();) {
						CapturedRecord captureRecord = (CapturedRecord)iterator.next();
						Object record = captureRecord.getRecord();
						if(record instanceof Bulletin) {
							Bulletin bulletin = (Bulletin)record;
							bulletin.setSourceUrl(captureRecord.getUrl());
						}
						capturedList.add(record);
					}
				}
			}, 1);
		}
		return new ArrayList(capturedList);
	}
	
	/**
	 * @param declareCaptureTask the declareCaptureTask to set
	 */
	public void setDeclareCaptureTask(String declareCaptureTask) {
		try {
			this.declareCaptureTask = (CmsCaptureTask)ObjectSerializer.deserialize(new Base64Decoder().decode(declareCaptureTask));
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}

	/**
	 * @param bulletinCaptureTask the bulletinCaptureTask to set
	 */
	public void setBulletinCaptureTask(String bulletinCaptureTask) {
		try {
			this.bulletinCaptureTask = (CmsCaptureTask)ObjectSerializer.deserialize(new Base64Decoder().decode(bulletinCaptureTask));
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}

	/**
	 * @return the captureService
	 */
	public CaptureService getCaptureService() {
		return captureService;
	}

	/**
	 * @param captureService the captureService to set
	 */
	public void setCaptureService(CaptureService captureService) {
		this.captureService = captureService;
	}

	/**
	 * @return the bulletinCaptureIntervalMinutes
	 */
	public int getBulletinCaptureIntervalMinutes() {
		return bulletinCaptureIntervalMinutes;
	}

	/**
	 * @param bulletinCaptureIntervalMinutes the bulletinCaptureIntervalMinutes to set
	 */
	public void setBulletinCaptureIntervalMinutes(int bulletinCaptureIntervalMinutes) {
		this.bulletinCaptureIntervalMinutes = bulletinCaptureIntervalMinutes;
	}

	/**
	 * @return the declareCaptureIntervalMinutes
	 */
	public int getDeclareCaptureIntervalMinutes() {
		return declareCaptureIntervalMinutes;
	}

	/**
	 * @param declareCaptureIntervalMinutes the declareCaptureIntervalMinutes to set
	 */
	public void setDeclareCaptureIntervalMinutes(int declareCaptureIntervalMinutes) {
		this.declareCaptureIntervalMinutes = declareCaptureIntervalMinutes;
	}

	/**
	 * @return the bulletinCaptureUrl
	 */
	public String getBulletinCaptureUrl() {
		return bulletinCaptureUrl;
	}

	/**
	 * @param bulletinCaptureUrl the bulletinCaptureUrl to set
	 */
	public void setBulletinCaptureUrl(String bulletinCaptureUrl) {
		this.bulletinCaptureUrl = bulletinCaptureUrl;
	}

	/**
	 * @return the declareCaptureUrl
	 */
	public String getDeclareCaptureUrl() {
		return declareCaptureUrl;
	}

	/**
	 * @param declareCaptureUrl the declareCaptureUrl to set
	 */
	public void setDeclareCaptureUrl(String declareCaptureUrl) {
		this.declareCaptureUrl = declareCaptureUrl;
	}
}