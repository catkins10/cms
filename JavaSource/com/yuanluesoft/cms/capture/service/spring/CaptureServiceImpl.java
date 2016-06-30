package com.yuanluesoft.cms.capture.service.spring;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.html.HTMLDocument;

import com.yuanluesoft.cms.capture.model.CapturedField;
import com.yuanluesoft.cms.capture.model.CapturedRecord;
import com.yuanluesoft.cms.capture.model.CapturedRecordList;
import com.yuanluesoft.cms.capture.model.SourcePage;
import com.yuanluesoft.cms.capture.pojo.CmsCaptureField;
import com.yuanluesoft.cms.capture.pojo.CmsCaptureLog;
import com.yuanluesoft.cms.capture.pojo.CmsCaptureTask;
import com.yuanluesoft.cms.capture.service.CaptureRecordService;
import com.yuanluesoft.cms.capture.service.CaptureService;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.distribution.service.DistributionService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.HTMLParser;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.Base64Decoder;
import com.yuanluesoft.jeaf.util.Base64Encoder;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.HttpUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.ObjectSerializer;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.model.HttpResponse;

/**
 * 
 * @author linchuan
 *
 */
public class CaptureServiceImpl extends BusinessServiceImpl implements CaptureService, Runnable {
	//抓取内容对应的业务对象
	private String captureBusinessClasses = "com.yuanluesoft.cms.siteresource.pojo.SiteResource," + //文章
											"com.yuanluesoft.cms.infopublic.pojo.PublicInfo," + //政府信息
											"com.yuanluesoft.cms.infopublic.opinion.pojo.PublicOpinion," + //信息公开意见箱
											"com.yuanluesoft.cms.infopublic.request.pojo.PublicRequest," + //依申请公开
											"com.yuanluesoft.cms.complaint.pojo.Complaint," + //投诉
											"com.yuanluesoft.cms.leadermail.pojo.LeaderMail," + //领导信箱
											"com.yuanluesoft.cms.messageboard.pojo.MessageBoard," + //留言
											"com.yuanluesoft.cms.supervision.pojo.Supervision," + //效能监督 
											"com.yuanluesoft.jeaf.weather.model.WeatherCapture," + //天气
											"com.yuanluesoft.traffic.busline.pojo.BusLine," + //公交线路
											"com.yuanluesoft.msa.declare.model.Declare," + //海事局电子申报
											"com.yuanluesoft.msa.declare.model.Bulletin"; //海事局公告
	private AttachmentService attachmentService;
	private HTMLParser htmlParser; //HTML解析器
	private DistributionService distributionService; //分布式服务
	
	private boolean isCapuring = false; //是否正在抓取
	private boolean stopped = false; //是否停止
	
	/**
	 * 启动
	 *
	 */
	public void startup() {
		new Thread(this).start();
	}
	
	/**
	 * 停止
	 *
	 */
	public void shutdown() {
		stopped = true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			Thread.sleep(60000);  //60秒后启动
		}
		catch (InterruptedException e) {
		
		}
		if(Logger.isTraceEnabled()) {
			Logger.trace("CaptureService: startup.");
		}
		synchronized(this) {
			while(!stopped) {
				try {
					isCapuring = true;
					if(!distributionService.isMasterServer(false)) { //当前服务器已经不是主服务器
						while(!distributionService.isMasterServer(true)) {  //每隔120秒检查一次当前服务器是否已经成为主服务器
							isCapuring = false;
							wait(120000); //等待120秒
						}
					}
					Timestamp now = DateTimeUtils.add(DateTimeUtils.now(), Calendar.SECOND, 1);
					//获取抓取时间已经到达的任务
					String hql = "from CmsCaptureTask CmsCaptureTask" +
								 " where CmsCaptureTask.enabled=1" + //启用的任务
								 " and CmsCaptureTask.schedule>0" + //抓取安排,0/手动,1/指定时间,2/指定时间间隔
								 " and CmsCaptureTask.nextCaptureTime<=TIMESTAMP(" + DateTimeUtils.formatTimestamp(now, null) + ")" +
								 " order by CmsCaptureTask.nextCaptureTime";
					for(int i=0; ; i+=200) {
						List tasks = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("fields", ","), i, 200);
						if(tasks==null || tasks.isEmpty()) {
							break;
						}
						for(Iterator iterator = tasks.iterator(); iterator.hasNext();) {
							CmsCaptureTask task = (CmsCaptureTask)iterator.next();
							try {
								startupCapture(task, false);
							}
							catch(Exception e) {
								Logger.exception(e);
							}
						}
						if(tasks.size()<200) {
							break;
						}
					}
					//获取下一次要抓取的任务
					hql = "from CmsCaptureTask CmsCaptureTask" +
						  " where CmsCaptureTask.enabled=1" + //启用的任务
						  " and CmsCaptureTask.schedule>0" + //抓取安排,0/手动,1/指定时间,2/指定时间间隔
						  " and CmsCaptureTask.nextCaptureTime>TIMESTAMP(" + DateTimeUtils.formatTimestamp(now, null) + ")" +
						  " order by CmsCaptureTask.nextCaptureTime";
					CmsCaptureTask nextTask = (CmsCaptureTask)getDatabaseService().findRecordByHql(hql);
					if(nextTask==null) { //没有任务
						isCapuring = false;
						wait(); //一直等待
					}
					else { //等待到下一个任务
						if(Logger.isTraceEnabled()) {
							Logger.trace("CaptureService: task " + nextTask.getDescription() + " will startup at " + DateTimeUtils.formatTimestamp(nextTask.getNextCaptureTime(), null) + ".");
						}
						long wait = nextTask.getNextCaptureTime().getTime() - System.currentTimeMillis();
						if(wait > 0) {
							isCapuring = false;
							wait(wait);
						}
					}
				}
				catch (Error e) {
					isCapuring = false;
					e.printStackTrace();
					Logger.error(e.getMessage());
					try {
						wait(300000); //等待300秒
					}
					catch (InterruptedException ie) {
						
					}
				}
				catch (Exception e) {
					isCapuring = false;
					Logger.exception(e);
					try {
						wait(300000); //等待300秒
					}
					catch (InterruptedException ie) {
						
					}
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		Record record = super.load(recordClass, id);
		if(record instanceof CmsCaptureTask) {
			//序列化对象,并用base64编码
			try {
				System.out.println(new Base64Encoder().encode(ObjectSerializer.serialize(record)));
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof CmsCaptureTask) {
			CmsCaptureTask task = (CmsCaptureTask)record;
			if(task.getCaptureURL().indexOf("://")==-1) {
				task.setCaptureURL("http://" + task.getCaptureURL());
			}
			setNextCaptureTime(task); //设置抓取时间
		}
		super.save(record);
		if((record instanceof CmsCaptureTask) && !isCapuring) {
			synchronized(this) {
				notifyAll(); //唤醒抓取线程
			}
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof CmsCaptureTask) {
			CmsCaptureTask task = (CmsCaptureTask)record;
			if(task.getCaptureURL().indexOf("://")==-1) {
				task.setCaptureURL("http://" + task.getCaptureURL());
			}
			if(task.getNextCaptureTime()==null) {
				setNextCaptureTime(task); //设置抓取时间
			}
		}
		super.update(record);
		if((record instanceof CmsCaptureTask) && !isCapuring) {
			synchronized(this) {
				notifyAll(); //唤醒抓取线程
			}
		}
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.capture.service.CaptureService#listCaptureTasks(java.lang.String)
	 */
	public List listCaptureTasks(String taskIds) throws ServiceException {
		String hql = "from CmsCaptureTask CmsCaptureTask" +
					 " where CmsCaptureTask.id in (" + JdbcUtils.validateInClauseNumbers(taskIds) + ")" +
					 " order by CmsCaptureTask.description";
		return getDatabaseService().findRecordsByHql(hql, listLazyLoadProperties(CmsCaptureTask.class));
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.capture.service.CaptureService#decodeCaptureTask(java.lang.String)
	 */
	public CmsCaptureTask decodeCaptureTask(String taskText) {
		try {
			return (CmsCaptureTask)ObjectSerializer.deserialize(new Base64Decoder().decode(taskText));
		}
		catch (Exception e) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.capture.service.CaptureService#loadSourcePage(com.yuanluesoft.cms.capture.pojo.CmsCaptureTask)
	 */
	public SourcePage loadSourcePage(CmsCaptureTask task) throws ServiceException {
		if(task.getCaptureURL()==null || task.getCaptureURL().equals("")) {
			return null;
		}
		SourcePage sourcePage = new SourcePage();
		//下载列表页面
		try {
			HttpResponse httpResponse = downloadPage(task.getCaptureURL(), task.getWebsiteCharset());
			String listPageContent = httpResponse.getResponseBody();
			sourcePage.setListPageHTML(listPageContent); //列表页面HTML
			String listPagePath = attachmentService.getSavePath("cms/capture", "listPage", task.getId(), true);
			FileUtils.saveStringToFile(listPagePath + "index.html", disableScript(StringUtils.resetResorcePath(httpResponse.getUrl(), listPageContent)), "utf-8", true);
			sourcePage.setListPageURL(((Attachment)attachmentService.list("cms/capture", "listPage", task.getId(), false, 1, null).get(0)).getUrlInline());
			//解析列表页面
			String contentPageURL; //第一个链接的URL
			CapturedRecordList capturedRecordList = captureListPage(task, httpResponse.getUrl(), 0);
			if(capturedRecordList!=null && capturedRecordList.getRecords()!=null && !capturedRecordList.getRecords().isEmpty() && (contentPageURL = ((CapturedRecord)capturedRecordList.getRecords().get(0)).getUrl())!=null) {
				//下载内容页面
				httpResponse = downloadPage(contentPageURL, task.getWebsiteCharset());
				String pageContent = httpResponse.getResponseBody();
				sourcePage.setContentPageHTML(pageContent); //内容页面HTML
				listPagePath = attachmentService.getSavePath("cms/capture", "contentPage", task.getId(), true);
				FileUtils.saveStringToFile(listPagePath + "index.html", disableScript(StringUtils.resetResorcePath(httpResponse.getUrl(), pageContent)), "utf-8", true);
				sourcePage.setContentPageURL(((Attachment)attachmentService.list("cms/capture", "contentPage", task.getId(), false, 1, null).get(0)).getUrlInline());
			}
		}
		catch (Exception e) {
			Logger.exception(e);
		}
		return sourcePage;
	}
	
	/**
	 * 禁用脚本
	 * @param htmlContent
	 * @return
	 */
	private String disableScript(String htmlContent) {
		return htmlContent.replaceAll("(?i)<script[^>]*>[\\s\\S]*?<\\/script[^>]*>", "");
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.capture.service.CaptureService#loadCaptureFieldConfig(com.yuanluesoft.cms.capture.pojo.CmsCaptureTask, boolean)
	 */
	public List loadCaptureFieldConfig(CmsCaptureTask task, boolean listPage) throws ServiceException {
		return listCaptureFields(task, listPage, false);
	}
	
	/**
	 * 获取字段列表
	 * @param task
	 * @param listPage
	 * @param validOnly
	 * @return
	 * @throws ServiceException
	 */
	private List listCaptureFields(CmsCaptureTask task, boolean listPage, boolean validOnly) throws ServiceException {
		//获取记录字段
		List fields = FieldUtils.listRecordFields(task.getBusinessClassName(), null, "attachment,image,video,opinion", null, "none,hidden", true, true, true, false, 0);
		String removedComponentName = null;
		for(int i = 0; i < fields.size(); i++) {
			Field field = (Field)fields.get(i);
			if(removedComponentName!=null && field.getName().startsWith(removedComponentName + ".")) {
				fields.remove(i);
				i--;
				continue;
			}
			if(!"component".equalsIgnoreCase(field.getType()) && !"components".equalsIgnoreCase(field.getType())) {
				continue;
			}
			removedComponentName = !"true".equals(field.getParameter("capture")) ? field.getName() : null; //检查组成部分是否需要抓取
			if(removedComponentName!=null || "component".equalsIgnoreCase(field.getType())) {
				fields.remove(i);
				i--;
			}
			else if("components".equalsIgnoreCase(field.getType())) {
				fields.add(i + 1, new Field(field.getName() + "_captureRecord", field.getTitle() + "_记录", null, null, null, true, false));
				i++;
			}
		}
		if(listPage) { //列表页面，添加虚构字段:列表、下一页链接、记录链接
			String[] names = {"captureList", "记录列表", "nextCapturePageLink", "下一页URL", "captureRecord", "记录", "captureRecordLink", "记录URL", "captureRecordId", "记录ID"};
			for(int i=0; i<names.length; i+=2) {
				fields.add(i/2, new Field(names[i], names[i+1], null, null, null, true, false));
			}
		}
		List captureFields = new ArrayList();
		for(Iterator iterator = task.getFields()==null ? null : task.getFields().iterator(); iterator!=null && iterator.hasNext();) {
			CmsCaptureField captureField = (CmsCaptureField)iterator.next();
			if(listPage!=(captureField.getIsListPage()==1)) {
				continue;
			}
			Field field = (Field)ListUtils.removeObjectByProperty(fields, "name", captureField.getFieldName());
			if(field==null) { //字段已经不存在
				continue;
			}
			resetCaptureField(captureField, field);
			captureFields.add(captureField);
		}
		if(validOnly) {
			return captureFields;
		}
		for(Iterator iterator = fields.iterator(); iterator.hasNext();) {
			Field field = (Field)iterator.next();
			CmsCaptureField captureField = new CmsCaptureField();
			captureField.setFieldName(field.getName());
			resetCaptureField(captureField, field);
			captureFields.add(captureField);
		}
		return captureFields;
	}
	
	/**
	 * 重置抓取字段
	 * @param captureField
	 * @param field
	 */
	private void resetCaptureField(CmsCaptureField captureField, Field field) {
		captureField.setFieldTitle(field.getTitle().replaceAll("列表_", "_"));
		captureField.setFieldType(field.getType());
		captureField.setComponentClassName((String)field.getParameter("class"));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.capture.service.CaptureService#saveCaptureFieldConfig(com.yuanluesoft.cms.capture.pojo.CmsCaptureTask, javax.servlet.http.HttpServletRequest)
	 */
	public void saveCaptureFieldConfig(CmsCaptureTask task, HttpServletRequest request) throws ServiceException {
		saveCaptureFieldConfig(task, request, true);
		saveCaptureFieldConfig(task, request, false);
	}
	
	/**
	 * 保存字段配置
	 * @param task
	 * @param request
	 * @param isListPage 是否列表页面
	 * @throws ServiceException
	 */
	private void saveCaptureFieldConfig(CmsCaptureTask task, HttpServletRequest request, boolean isListPage) throws ServiceException {
		String[] fieldNames = request.getParameterValues((isListPage ? "list" : "content" ) + "FieldName");
		if(fieldNames==null || fieldNames.length==0) { //没有配置
			return;
		}
		//删除原来的配置
		String hql = "from CmsCaptureField CmsCaptureField" +
					 " where CmsCaptureField.taskId=" + task.getId() +
					 " and CmsCaptureField.isListPage" + (isListPage ? "=1" : "!=1");
		getDatabaseService().deleteRecordsByHql(hql);
		//保存新的字段配置
		for(int i=0; i<fieldNames.length; i++) {
			String fieldBegin = request.getParameter((isListPage ? "list" : "content" ) + "Field_" + fieldNames[i] + "_begin");
			String fieldEnd = request.getParameter((isListPage ? "list" : "content" ) + "Field_" + fieldNames[i] + "_end");
			String arraySeparator = request.getParameter((isListPage ? "list" : "content" ) + "Field_" + fieldNames[i] + "_arraySeparator");
			String fieldValue = request.getParameter((isListPage ? "list" : "content" ) + "Field_" + fieldNames[i] + "_value");
			if((fieldBegin==null || fieldBegin.isEmpty()) && (fieldEnd==null || fieldEnd.isEmpty()) && (fieldValue==null || fieldValue.isEmpty())) {
				continue;
			}
			CmsCaptureField captureField = new CmsCaptureField();
			captureField.setId(UUIDLongGenerator.generateId()); //ID
			captureField.setTaskId(task.getId()); //任务ID
			captureField.setFieldName(fieldNames[i]); //字段名称
			captureField.setFieldBegin(fieldBegin.replaceAll("\r\n", "\n")); //开始位置
			captureField.setFieldEnd(fieldEnd.replaceAll("\r\n", "\n")); //结束位置
			captureField.setArraySeparator(arraySeparator.replaceAll("\r\n", "\n")); //数组分隔符
			captureField.setValue(fieldValue); //指定值
			captureField.setFieldFormat(request.getParameter((isListPage ? "list" : "content" ) + "Field_" + fieldNames[i] + "_format")); //格式,日期格式化
			captureField.setPriority(i); //优先级
			captureField.setIsListPage(isListPage ? 1 : 0); //是否列表页面
			getDatabaseService().saveRecord(captureField);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.capture.service.CaptureService#copyTask(com.yuanluesoft.cms.capture.pojo.CmsCaptureTask, java.lang.String, java.lang.String)
	 */
	public CmsCaptureTask copyTask(CmsCaptureTask task, String description, String captureURL) throws ServiceException {
		CmsCaptureTask newTask = new CmsCaptureTask();
		try {
			PropertyUtils.copyProperties(newTask, task);
		} 
		catch (Exception e) {
			
		}
		//保存主记录
		newTask.setId(UUIDLongGenerator.generateId()); //ID
		newTask.setDescription(description); //描述
		newTask.setCaptureURL(captureURL); //URL
		getDatabaseService().saveRecord(newTask);
		
		//保存字段配置
		newTask.setFields(new LinkedHashSet());
		for(Iterator iterator = task.getFields()==null ? null : task.getFields().iterator(); iterator!=null && iterator.hasNext();) {
			CmsCaptureField field = (CmsCaptureField)iterator.next();
			CmsCaptureField newField = new CmsCaptureField();
			try {
				PropertyUtils.copyProperties(newField, field);
			} 
			catch (Exception e) {
				
			}
			newField.setId(UUIDLongGenerator.generateId()); //ID
			newField.setTaskId(newTask.getId()); //任务ID
			getDatabaseService().saveRecord(newField);
			newTask.getFields().add(newField);
		}
		return newTask;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.capture.service.CaptureService#captureContentPage(com.yuanluesoft.cms.capture.pojo.CmsCaptureTask, java.lang.String)
	 */
	public CapturedRecord captureContentPage(CmsCaptureTask task, String pageURL) throws ServiceException {
		CapturedRecord capturedRecord = new CapturedRecord();
		try {
			capturedRecord.setRecord(Class.forName(task.getBusinessClassName()).newInstance());
		}
		catch(Exception e) {
			
		}
		captureRecordPage(task, pageURL, capturedRecord);
		return capturedRecord;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.cms.capture.service.CaptureService#captureListPage(com.yuanluesoft.cms.capture.pojo.CmsCaptureTask, java.lang.String, int)
	 */
	public CapturedRecordList captureListPage(CmsCaptureTask task, String pageURL, int pageIndex) throws ServiceException {
		if(pageURL==null || pageURL.isEmpty()) {
			return null;
		}
		List fields = listCaptureFields(task, true, true); //获取字段配置
		if(fields==null || fields.isEmpty()) {
			return null;
		}
		//下载页面
		if(Logger.isTraceEnabled()) {
			Logger.trace("CaptureService: capture list page " + pageURL);
		}
		HttpResponse httpResponse = downloadPage(pageURL, task.getWebsiteCharset());
		if(httpResponse==null) {
			return null;
		}
		String[] pageHTML = {httpResponse.getResponseBody()};
		int range[] = {0, pageHTML[0].length()};
		//解析页面
		CapturedRecordList capturedRecordList = new CapturedRecordList();
		try {
			parsePage(task, capturedRecordList, fields, 0, pageURL, pageHTML, range);
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
		//获取下一页的URL
		int beginIndex, endIndex;
        if(task.getNextPageDirection()=='1' && task.getNextPageURL()!=null && (beginIndex = task.getNextPageURL().indexOf("<页码"))!=-1 && (endIndex = task.getNextPageURL().indexOf(">", beginIndex + 3))!=-1) { //下一页定位方式:不抓取其它页|0\0URL上加页码方式|1\0链接|2
			String numberLength = task.getNextPageURL().substring(beginIndex + 3, endIndex);
			capturedRecordList.setNextPageURL(task.getNextPageURL().substring(0, beginIndex) + StringUtils.formatNumber(pageIndex + task.getBeginPage() + 1, (numberLength.startsWith("*") ? Integer.parseInt(numberLength.substring(1)) : 0), false) + task.getNextPageURL().substring(endIndex + 1));
	    }
		return capturedRecordList;
	}
	
	/**
	 * 解析记录页面
	 * @param task
	 * @param pageURL
	 * @param recordPage
	 * @return
	 * @throws ServiceException
	 */
	protected void captureRecordPage(CmsCaptureTask task, String pageURL, CapturedRecord capturedRecord) throws ServiceException {
		if(pageURL==null || pageURL.isEmpty()) {
			return;
		}
		List fields = listCaptureFields(task, false, true); //获取字段配置
		if(fields==null || fields.isEmpty()) {
			return;
		}
		capturedRecord.setUrl(pageURL); //URL
		//下载页面
		if(Logger.isTraceEnabled()) {
			Logger.trace("CaptureService: capture record page " + pageURL);
		}
		HttpResponse httpResponse = downloadPage(pageURL, task.getWebsiteCharset());
		if(httpResponse==null) {
			return;
		}
		String[] pageHTML = {httpResponse.getResponseBody()};
		int range[] = {0, pageHTML[0].length()};
		//解析页面
		try {
			parsePage(task, capturedRecord, fields, 0, pageURL, pageHTML, range);
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * 解析页面
	 * @param task
	 * @param record
	 * @param fields
	 * @param fieldIndex
	 * @param pageURL
	 * @param pageHTML
	 * @param range
	 * @throws Exception
	 */
	private void parsePage(CmsCaptureTask task, Object record, List fields, int fieldIndex, String pageURL, String[] pageHTML, int[] range) throws Exception {
		if(fieldIndex >= fields.size()) {
			return;
		}
		CmsCaptureField field = (CmsCaptureField)fields.get(fieldIndex);
		List recordListFields = listRecordListFields(fields, fieldIndex); //获取记录列表字段
		int[] fieldRange = indexField(field, pageHTML, range);
		range[0] = fieldRange[0] < 0 || fieldRange[1] < 0 ? range[0] : fieldRange[1] + (field.getFieldEnd()==null ? 0 : field.getFieldEnd().length());
		if(recordListFields==null) { //不是记录列表
			setRecordFieldValue(task, record, pageURL, field, fieldRange[0] < 0 || fieldRange[1] < 0 ? null : pageHTML[0].substring(fieldRange[0], fieldRange[1]));
		}
		else if(fieldRange[0] >= 0 && fieldRange[1] >= 0) { //记录列表,且定位成功
			parseList(task, record, field, recordListFields, pageURL, pageHTML, fieldRange);
		}
		parsePage(task, record, fields, fieldIndex + (recordListFields==null ? 1 : recordListFields.size() + 1), pageURL, pageHTML, range);
	}
	
	/**
	 * 解析记录列表
	 * @param task
	 * @param parentRecord
	 * @param recordListField
	 * @param listFields
	 * @param pageURL
	 * @param pageHTML
	 * @param range
	 * @throws Exception
	 */
	private void parseList(CmsCaptureTask task, Object parentRecord, CmsCaptureField recordListField, List listFields, String pageURL, String[] pageHTML, int[] range) throws Exception {
		CmsCaptureField captureRecordField = (CmsCaptureField)ListUtils.removeObjectByProperty(listFields, "fieldName", "captureList".equals(recordListField.getFieldName()) ? "captureRecord" : recordListField.getFieldName() + "_captureRecord");
		int[] recordRange = {range[0], range[1]};
		for(;;) {
			if(captureRecordField!=null && ((recordRange = indexField(captureRecordField, pageHTML, range))[0] < 0 || recordRange[1] < 0)) { //找不到记录
				break;
			}
			//创建抓取记录
			CapturedRecord capturedRecord = new CapturedRecord();
			String recordClassName = "captureList".equals(recordListField.getFieldName()) ? task.getBusinessClassName() : (String)recordListField.getComponentClassName();
			capturedRecord.setRecord(Class.forName(recordClassName).newInstance());
			if(capturedRecord.getRecord() instanceof Record) {
				((Record)capturedRecord.getRecord()).setId(UUIDLongGenerator.generateId());
			}
			//解析字段
			parsePage(task, capturedRecord, listFields, 0, pageURL, pageHTML, recordRange); //解析字段
			boolean recordValid = capturedRecord.getFields()!=null && !capturedRecord.getFields().isEmpty(); //判断记录是否有效
			if(captureRecordField!=null) {
				range[0] = recordRange[1] + (captureRecordField.getFieldEnd()==null ? 0 : captureRecordField.getFieldEnd().length());
			}
			else if(!recordValid) { //记录无效
				break;
			}
			if(!recordValid) {
				continue;
			}
			//添加到列表
			if(parentRecord instanceof CapturedRecordList) {
				CapturedRecordList capturedRecordList = (CapturedRecordList)parentRecord;
				if(capturedRecordList.getRecords()==null) {
					capturedRecordList.setRecords(new ArrayList());
				}
				if(capturedRecord.getUrl()==null || capturedRecord.getUrl().isEmpty() || ListUtils.findObjectByProperty(capturedRecordList.getRecords(), "url", capturedRecord.getUrl())==null) {
					capturedRecordList.getRecords().add(capturedRecord);
				}
			}
			else {
				//更新业务记录
				CapturedRecord parentCaptureRecord = (CapturedRecord)parentRecord;
				String recordListName = recordListField.getFieldName().substring(recordListField.getFieldName().lastIndexOf('.') + 1);
				Collection recordList = (Collection)PropertyUtils.getProperty(parentCaptureRecord.getRecord(), recordListName);
				if(recordList==null) {
					Class type = PropertyUtils.getPropertyType(parentCaptureRecord.getRecord(), recordListName);
					recordList = List.class.isAssignableFrom(type) ? (Collection)new ArrayList() : new LinkedHashSet();
					PropertyUtils.setProperty(parentCaptureRecord.getRecord(), recordListName, recordList);
				}
				recordList.add(capturedRecord.getRecord());
				//修改对应的抓取字段
				CapturedField captureField = addCaptureField(parentCaptureRecord, recordListField, new ArrayList());
				((List)captureField.getFieldValue()).add(capturedRecord);
			}
		}
	}
	
	/**
	 * 设置字段值
	 * @param task
	 * @param captureRecord
	 * @param pageURL
	 * @param field
	 * @param fieldValue
	 * @throws ServiceException
	 */
	private void setRecordFieldValue(CmsCaptureTask task, Object record, String pageURL, CmsCaptureField field, String fieldValue) throws ServiceException {
		if(record instanceof CapturedRecordList) {
			if("nextCapturePageLink".equals(field.getFieldName()) && fieldValue!=null && !fieldValue.isEmpty() && task.getNextPageDirection()=='2') { //下一页定位方式:不抓取其它页|0\0URL上加页码方式|1\0链接|2
				((CapturedRecordList)record).setNextPageURL(resetURL(pageURL, fieldValue));
			}
			return;
		}
		CapturedRecord capturedRecord = (CapturedRecord)record;
		if("captureRecordLink".equals(field.getFieldName())) {
			if(fieldValue!=null && !fieldValue.isEmpty() && task.getRecordPageURLDirection()=='0') { //记录页面URL获取方式: 0/从页面获取,1/根据记录ID生成
				capturedRecord.setUrl(resetURL(pageURL, fieldValue));
			}
			return;
		}
		if("captureRecordId".equals(field.getFieldName())) { //记录ID
			if(fieldValue!=null && !fieldValue.isEmpty() && task.getRecordPageURLDirection()=='1') { //记录页面URL获取方式: 0/从页面获取,1/根据记录ID生成
				capturedRecord.setUrl(resetURL(pageURL, task.getRecordPageURL().replaceAll("<记录ID>", fieldValue)));
			}
			return;
		}
		if(fieldValue!=null && !fieldValue.isEmpty()) { //字段值不为空
	    	if("html".equals(field.getFieldType())) { //字段类型是HTML
	    		fieldValue = fieldValue.replaceAll("(?i)<\\?xml[^>]+>", "");
				HTMLDocument htmlDocument = htmlParser.parseHTMLString(fieldValue, "utf-8");
				if(htmlDocument!=null) {
					fieldValue = htmlParser.getBodyHTML(htmlDocument, "utf-8", false).replaceAll("(?i)(<[^>]*) class=\"[^\"]*\"", "$1"); //清除class属性
					fieldValue = StringUtils.resetResorcePath(pageURL, fieldValue);
				}
			}
	    	else if(field.getFieldType()==null || !field.getFieldType().endsWith("[]")) {
	    		fieldValue = StringUtils.filterHtmlElement(fieldValue, false).trim();
	    	}
    	}
		String arraySeparator = field.getArraySeparator();
    	if(fieldValue==null || fieldValue.isEmpty()) { //字段值为空
    		fieldValue = field.getValue(); //使用默认值
    		if(fieldValue==null || fieldValue.isEmpty()) { //字段值为空
    			return;
    		}
    		arraySeparator = ",";
    	}
    	Object propertyValue = fieldValue;
    	if(field.getFieldType()!=null && field.getFieldType().endsWith("[]")) {
    		arraySeparator = arraySeparator==null || arraySeparator.isEmpty() ? "," : StringUtils.resetRegexMetacharacters(arraySeparator);
    		propertyValue = fieldValue.split(arraySeparator);
    		fieldValue = ListUtils.join((Object[])propertyValue, ", ", false);
    	}
		//加入抓取字段列表
    	CapturedField captureField = addCaptureField(capturedRecord, field, fieldValue);
		//设置记录属性
		try {
			BeanUtils.setPropertyValue(capturedRecord.getRecord(), captureField.getFieldName(), propertyValue, field.getFieldFormat());
		}
		catch(Exception e) {
			
		}
		catch(Error e) {
			
		}
	}
	
	/**
	 * 加入抓取字段列表
	 * @param captureRecord
	 * @param field
	 * @param fieldValue
	 * @return
	 */
	private CapturedField addCaptureField(CapturedRecord capturedRecord, CmsCaptureField field, Object fieldValue) {
		if(capturedRecord.getFields()==null) {
			capturedRecord.setFields(new ArrayList()); //字段列表
    	}
		String fieldName = field.getFieldName().substring(field.getFieldName().lastIndexOf('.') + 1);
		CapturedField captureField = (CapturedField)ListUtils.findObjectByProperty(capturedRecord.getFields(), "fieldName", fieldName);
		if(captureField==null) {
			captureField = new CapturedField();
			captureField.setFieldName(fieldName);
			captureField.setFieldTitle(field.getFieldTitle().substring(field.getFieldTitle().lastIndexOf('_') + 1));
			captureField.setFieldType(field.getFieldType());
			capturedRecord.getFields().add(captureField);
			captureField.setFieldValue(fieldValue);
		}
		else if(!(fieldValue instanceof List)) {
			captureField.setFieldValue(fieldValue);
		}
		return captureField;
	}
	
	/**
	 * 定位一个字段
	 * @param field
	 * @param pageHTML
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	private int[] indexField(CmsCaptureField field, String[] pageHTML, int[] range) {
		if((field.getFieldBegin()==null || field.getFieldBegin().isEmpty()) && (field.getFieldEnd()==null || field.getFieldEnd().isEmpty())) {
			return new int[]{-1, -1};
		}
		int fieldBegin = field.getFieldBegin()==null || field.getFieldBegin().isEmpty() ? range[0] : pageHTML[0].indexOf(field.getFieldBegin(), range[0]);
		int fieldEnd = -1;
		if(fieldBegin > range[1]) {
			fieldBegin = -1;
		}
		if(fieldBegin!=-1) { //找到开始位置
			fieldBegin += field.getFieldBegin()==null ? 0 : field.getFieldBegin().length();
			fieldEnd = field.getFieldEnd()==null || field.getFieldEnd().isEmpty() ? range[1] : pageHTML[0].indexOf(field.getFieldEnd(), fieldBegin);
		}
		if(fieldEnd > range[1]) {
			fieldEnd = -1;
		}
		return new int[]{fieldBegin, fieldEnd};
	}
	
	/**
	 * 获取列表字段
	 * @param fields
	 * @param fromIndex
	 * @return
	 */
	private List listRecordListFields(List fields, int recordListFieldIndex) {
		CmsCaptureField recordListField = (CmsCaptureField)fields.get(recordListFieldIndex);
		if("captureList".equals(recordListField.getFieldName())) {
			List recordListFields = new ArrayList(fields.subList(recordListFieldIndex + 1, fields.size()));
			ListUtils.removeObjectByProperty(recordListFields, "fieldName", "nextCapturePageLink"); //下一页链接配置
			return recordListFields;
		}
		else if(!"components".equals(recordListField.getFieldType())) {
			return null;
		}
		List recordListFields = new ArrayList();
		for(int i = recordListFieldIndex + 1; i < fields.size(); i++) {
			CmsCaptureField field = (CmsCaptureField)fields.get(i);
			if(!field.getFieldName().startsWith(recordListField.getFieldName() + "_") && !field.getFieldName().startsWith(recordListField.getFieldName() + ".")) {
				break;
			}
			recordListFields.add(field);
		}
		return recordListFields;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("captureBusinessObjects".equals(itemsName)) { //抓取内容对应的业务对象
			String[] classNames = captureBusinessClasses.split(",");
			List captureBusinessObjects = new ArrayList();
			for(int i=0; i<classNames.length; i++) {
				BusinessObject businessObject = getBusinessDefineService().getBusinessObject(classNames[i]);
				if(businessObject!=null) {
					captureBusinessObjects.add(new String[]{businessObject.getTitle(), classNames[i]});
				}
			}
			return captureBusinessObjects;
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}
	
	/**
	 * 下载页面
	 * @param pageURL
	 * @param resetPage 是否需要更新页面内容,包括
	 * @return
	 * @throws ServiceException
	 */
	private HttpResponse downloadPage(String pageURL, String charset) throws ServiceException {
		HttpResponse httpResponse;
		try {
			httpResponse = HttpUtils.getHttpContent(pageURL, charset, true, null, 30000, 3, 1000);
		}
		catch(Exception e) {
			Logger.error("CaptureService: download page " + pageURL + " failed, exception is " + e.getMessage() + ".");
			return null;
		}
		String htmlContent = httpResponse.getResponseBody();
		httpResponse.setResponseBody(htmlContent.indexOf("\r\n")!=-1 ? htmlContent.replaceAll("\r\n", "\n") : htmlContent);
		return httpResponse;
	}
	
	/**
	 * 重设URL
	 * @param currentPageURL
	 * @param url
	 * @return
	 */
	private String resetURL(String currentPageURL, String url) {
		url = url.replaceAll("&amp;", "&");
		if(url.indexOf(':')!=-1) {
			return url;
		}
		String pagePath; //页面路径
		String pageRootPath; //页面根路径
		int index = currentPageURL.indexOf("/", currentPageURL.indexOf("://") + 3);
		if(index==-1) {
			pageRootPath = currentPageURL;
			pagePath = currentPageURL + "/"; //追加“/”
		}
		else {
			pageRootPath = currentPageURL.substring(0, index);
			pagePath = currentPageURL.substring(0, currentPageURL.lastIndexOf("/") + 1);
		}
		return StringUtils.removeDotFromURL((url.startsWith("/") ? pageRootPath : pagePath) + url);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.capture.service.CaptureService#startupCapture(com.yuanluesoft.cms.capture.pojo.CmsCaptureTask)
	 */
	public void startupCapture(CmsCaptureTask task) throws ServiceException {
		startupCapture(task, true);
	}
	
	/**
	 * 按任务抓取页面
	 * @param task
	 * @param fullCapture 是否全部重新抓取
	 * @throws ServiceException
	 */
	public void startupCapture(CmsCaptureTask task, boolean fullCapture) throws ServiceException {
		if(Logger.isTraceEnabled()) {
			Logger.trace("CaptureService: task " + task.getDescription() + " startup.");
		}
		//设置下一次抓取的时间
		setNextCaptureTime(task);
		getDatabaseService().updateRecord(task);
		
		//获取业务对象
		BusinessObject businessObject = getBusinessDefineService().getBusinessObject(task.getBusinessClassName());
		if(businessObject==null) {
			return;
		}
		//获取业务逻辑服务
		BusinessService businessService = (BusinessService)Environment.getService(businessObject.getBusinessServiceName()==null ? "businessService" : businessObject.getBusinessServiceName());
		if(!(businessService instanceof CaptureRecordService)) {
			return;
		}
		CaptureRecordService captureRecordService = (CaptureRecordService)businessService;
		Timestamp now = DateTimeUtils.now(); //当前时间
		String pageURL = task.getCaptureURL();
		CapturedRecordList previousPageRecordList = null; //前一页
		for(int pageIndex = 0; pageIndex<10000 ; pageIndex++) { //最多抓取10000页
			//解析列表页面
			CapturedRecordList captureRecordList = captureListPage(task, pageURL, pageIndex);
			if(captureRecordList==null || captureRecordList.getRecords()==null) {
				return;
			}
			//解析记录页面
			List processedLinks = new ArrayList(); //处理过的链接
			boolean isLastPage = true;
			for(Iterator iterator = captureRecordList.getRecords().iterator(); iterator.hasNext();) {
				CapturedRecord capturedRecord = (CapturedRecord)iterator.next();
				String recordUrl = capturedRecord.getUrl();
				if(recordUrl==null || recordUrl.isEmpty()) { //没有记录URL
					continue;
				}
				//检查前一页是否有相同的URL，如果有说明已经抓取过，注意：有的网站，翻到不存在的页时，显示的是最后一页
				if(previousPageRecordList==null || ListUtils.findObjectByProperty(previousPageRecordList.getRecords(), "url", recordUrl)==null) {
					isLastPage = false;
				}
				//检查链接是否在处理过的链接中
				if(processedLinks.indexOf(recordUrl)!=-1) {
					continue;
				}
				//检查链接是否在抓取历史中
				CmsCaptureLog captureLog = (CmsCaptureLog)getDatabaseService().findRecordByHql("from CmsCaptureLog CmsCaptureLog where CmsCaptureLog.captureUrl='" + JdbcUtils.resetQuot(recordUrl) + "'");
				if(captureLog!=null) { //抓取过
					if(!fullCapture && captureLog.getCaptureTime().before(now)) { //不需要全部重新抓取,且在本次任务运行前已经抓取过,退出抓取
						return;
					}
					if(captureLog.getCaptureSuccess()=='1') { //抓取成功
						continue;
					}
				}
				//解析记录页面
				boolean captureSuccess = false;
				try {
					captureRecordPage(task, recordUrl, capturedRecord);
					captureRecordService.processCapturedRecord(task, capturedRecord);
					captureSuccess = true;
				}
				catch(Error e) {
					Logger.error(e.getMessage());
				}
				catch(Exception e) {
					Logger.error(e.getClass() + (e.getMessage()==null ? "" : ": " + e.getMessage()));
				}
				//记录到列表
				processedLinks.add(recordUrl);
				//记录到日志
				boolean isNew = (captureLog==null);
				if(isNew) {
					captureLog = new CmsCaptureLog();
					captureLog.setId(UUIDLongGenerator.generateId()); //ID
					captureLog.setCaptureUrl(recordUrl); //抓取的URL
				}
				captureLog.setCaptureTaskId(task.getId()); //任务ID
				captureLog.setCaptureTaskDescription(task.getDescription()); //任务描述
				captureLog.setCaptureTime(DateTimeUtils.now()); //抓取时间
				captureLog.setCaptureHourMinute(DateTimeUtils.formatTimestamp(captureLog.getCaptureTime(), "HH:mm")); //抓取时段
				captureLog.setCaptureSuccess(captureSuccess ? '1' : '0'); //抓取是否成功
				if(isNew) {
					getDatabaseService().saveRecord(captureLog);
				}
				else {
					getDatabaseService().updateRecord(captureLog);
				}
			}
			//如果已经是最后一页或者一页都没有处理一条记录,则退出抓取
			if(isLastPage || processedLinks.isEmpty()) {
				return;
			}
			//抓取下一页
			pageURL = captureRecordList.getNextPageURL();
			if(pageURL==null) {
				return;
			}
			previousPageRecordList = captureRecordList;
		}
	}
	
	/**
	 * 设置下一次抓取的时间
	 * @param task
	 */
	private void setNextCaptureTime(CmsCaptureTask task) {
		if(task.getSchedule()==1) { //指定时间
			if(task.getCaptureTime()==null || task.getCaptureTime().isEmpty()) {
				task.setCaptureTime("02:30");
			}
			Timestamp nextCaptureTime = null;
			try {
				nextCaptureTime = DateTimeUtils.parseTimestamp(DateTimeUtils.formatDate(DateTimeUtils.date(), "yyyy-MM-dd") + " " + task.getCaptureTime(), "yyyy-MM-dd HH:mm");
			}
			catch (ParseException e) {
				
			}
			if(nextCaptureTime.before(DateTimeUtils.now())) {
				nextCaptureTime = DateTimeUtils.add(nextCaptureTime, Calendar.DAY_OF_MONTH, 1);
			}
			task.setNextCaptureTime(nextCaptureTime);
		}
		else if(task.getSchedule()==2) { //指定时间间隔
			if(task.getCaptureInterval()<=0) {
				task.setCaptureInterval(720); //12小时
			}
			task.setNextCaptureTime(DateTimeUtils.add(DateTimeUtils.now(), Calendar.MINUTE, task.getCaptureInterval()));
		}
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
	 * @return the htmlParser
	 */
	public HTMLParser getHtmlParser() {
		return htmlParser;
	}

	/**
	 * @param htmlParser the htmlParser to set
	 */
	public void setHtmlParser(HTMLParser htmlParser) {
		this.htmlParser = htmlParser;
	}

	/**
	 * @return the distributionService
	 */
	public DistributionService getDistributionService() {
		return distributionService;
	}

	/**
	 * @param distributionService the distributionService to set
	 */
	public void setDistributionService(DistributionService distributionService) {
		this.distributionService = distributionService;
	}
}