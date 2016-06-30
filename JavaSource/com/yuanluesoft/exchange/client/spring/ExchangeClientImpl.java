package com.yuanluesoft.exchange.client.spring;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.exchange.client.exception.ExchangeException;
import com.yuanluesoft.exchange.client.io.ExchangePacketReader;
import com.yuanluesoft.exchange.client.io.ExchangePacketWriter;
import com.yuanluesoft.exchange.client.packet.ExchangeComplete;
import com.yuanluesoft.exchange.client.packet.ExchangePacket;
import com.yuanluesoft.exchange.client.packet.ExchangeRequest;
import com.yuanluesoft.exchange.client.packet.ExchangeRequestAck;
import com.yuanluesoft.exchange.client.packet.Login;
import com.yuanluesoft.exchange.client.packet.LoginAck;
import com.yuanluesoft.exchange.client.packet.data.DatabaseQuery;
import com.yuanluesoft.exchange.client.packet.data.DatabaseQueryResult;
import com.yuanluesoft.exchange.client.packet.data.FileData;
import com.yuanluesoft.exchange.client.packet.data.FileDelete;
import com.yuanluesoft.exchange.client.packet.data.FileSend;
import com.yuanluesoft.exchange.client.packet.data.FileSendAck;
import com.yuanluesoft.exchange.client.packet.data.FileSendComplete;
import com.yuanluesoft.exchange.client.packet.data.FileValidate;
import com.yuanluesoft.exchange.client.packet.data.FileValidateComplete;
import com.yuanluesoft.exchange.client.packet.data.FileValidateResult;
import com.yuanluesoft.exchange.client.packet.data.SendObject;
import com.yuanluesoft.exchange.client.packet.data.SendObjectAck;
import com.yuanluesoft.exchange.client.packet.data.SynchDelete;
import com.yuanluesoft.exchange.client.packet.data.SynchUpdate;
import com.yuanluesoft.exchange.client.pojo.ExchangeTask;
import com.yuanluesoft.exchange.client.sender.ExchangeConnection;
import com.yuanluesoft.exchange.client.sender.ExchangeDataSender;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.attachmentmanage.service.TemporaryFileManageService;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.distribution.service.DistributionService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class ExchangeClientImpl implements ExchangeClient, Runnable {
	private String validateKey = "20050718"; //校验密钥
	private String name; //客户端名称,在同一个数据交换中心中必须唯一
	private String attachmentBaseDirectory; //附件存放的基准路径,如:附件目录是"d:/cms/cms/pages/",对应的基准路径为"d:/cms/"
	private Map permissibleHqls; //允许执行的HQL,key是数据库服务名称,如果需要执行的HQL前部匹配,则允许执行
	private Map permissibleSqls; //允许执行的SQL,key是数据库服务名称,如果需要执行的SQL前部匹配,则允许执行
	private int maxExchangeRequest = 20; //同时执行的最大数据交换请求
	private boolean receiveOnly = false; //是否只接收
	private boolean sendRecordEnabled = true; //是否允许发送数据库记录

	private List exchangeDataSenders; //数据发送器列表
	private BusinessDefineService businessDefineService; //业务逻辑定义服务
	private TemporaryFileManageService temporaryFileManageService; //临时文件管理服务
	private DatabaseService databaseService; //数据库服务
	private DistributionService distributionService; //分布式服务

	//私有属性
	private boolean stopped = false; //是否已经停止
	private List exchangeThreads = new ArrayList(); //交换线程列表
	private Object exchangeMutex = new Object(); //数据交换MUTEX
	private Object exchangeRequestMutex = new Object(); //数据交换请求MUTEX
	private int exchangeRequest = 0; //当前的交换请求个数
	private ExchangeTask nextExchangeTask = null; //下一个需要交换的任务
	
	/**
	 * 启动
	 */
	public void startup() {
		final Runnable client = this;
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				Thread.currentThread().setName(Thread.currentThread().getName() + "-Exchange-" + name);
				timer.cancel();
				//启动交换线程
				for(Iterator iterator = exchangeDataSenders.iterator(); iterator.hasNext();) {
					ExchangeDataSender exchangeDataSender = (ExchangeDataSender)iterator.next();
					ExchangeThread exchangeThread = new ExchangeThread(exchangeDataSender);
					exchangeThread.start();
					exchangeThreads.add(exchangeThread);
				}
				//启动发送线程
				new Thread(client).start();
			}
		}, 30000); //等待30s
	}
	
	/**
	 * 关闭
	 */
	public void shutdown() {
		stopped = true;
		//关闭交换线程
		for(Iterator iterator = exchangeThreads.iterator(); iterator.hasNext();) {
			ExchangeThread exchangeThread = (ExchangeThread)iterator.next();
			exchangeThread.interrupt();
		}
		//唤醒发送线程
		synchronized(exchangeMutex) {
			exchangeMutex.notifyAll();
		}
		synchronized(exchangeRequestMutex) {
			exchangeRequestMutex.notifyAll();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.exchange.client.ExchangeClient#synchUpdate(java.io.Serializable, java.lang.String, int)
	 */
	public void synchUpdate(Serializable object, String receiverNames, int delayMilliseconds) throws ExchangeException {
		//获取当前需要发送的接收者
		receiverNames = retrieveReceivers(receiverNames);
		if(receiverNames==null) {
			return;
		}
		if(Logger.isInfoEnabled()) {
			Logger.info("ExchangeClient: synch update a object, class name is " + object.getClass().getName() + ", id is " + (object instanceof Record ? ((Record)object).getId() : -1) + ".");
		}
		if(delayMilliseconds>0) { //延时发送
			addExchangeTask("updateRecord", object, null, false, receiverNames, delayMilliseconds); //加入队列
		}
		else { //实时发送
			doSynchUpdate(object, receiverNames, null);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.exchange.client.ExchangeClient#synchDelete(java.io.Serializable, java.lang.String, int)
	 */
	public void synchDelete(Serializable object, String receiverNames, int delayMilliseconds) throws ExchangeException {
		//获取当前需要发送的接收者
		receiverNames = retrieveReceivers(receiverNames);
		if(receiverNames==null) {
			return;
		}
		if(Logger.isInfoEnabled()) {
			Logger.info("ExchangeClient: synch delete a record, class name is " + object.getClass().getName() + ", id is " + (object instanceof Record ? ((Record)object).getId() : -1) + ".");
		}
		if(delayMilliseconds>0) { //延时发送
			addExchangeTask("deleteRecord", object, null, false, receiverNames, delayMilliseconds); //加入队列
		}
		else { //实时发送
			doSynchDelete(object, receiverNames, null);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.exchange.client.ExchangeClient#sendFiles(java.util.List, java.lang.String, boolean, boolean)
	 */
	public List sendFiles(List filePaths, String receiverNames, boolean createDirectoryIfNotExists, boolean async) throws ExchangeException {
		//获取当前需要发送的接收者
		receiverNames = retrieveReceivers(receiverNames);
		if(receiverNames==null) {
			return null;
		}
		List remoteFilePaths = new ArrayList();
		for(Iterator iterator = filePaths==null ? null : filePaths.iterator(); iterator!=null && iterator.hasNext();) {
			final String filePath = (String)iterator.next();
			if(Logger.isInfoEnabled()) {
				Logger.info("ExchangeClient: send file " + filePath + ".");
			}
			if(async) { //异步
				addExchangeTask("sendFile", null, filePath, createDirectoryIfNotExists, receiverNames, 10);
			}
			else { //同步
				remoteFilePaths.add(doSendOrDeleteFile(receiverNames, filePath, createDirectoryIfNotExists, true, null));
			}
		}
		return remoteFilePaths.isEmpty() ? null : remoteFilePaths;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.exchange.client.ExchangeClient#deleteFiles(java.util.List, java.lang.String, boolean)
	 */
	public void deleteFiles(List filePaths, String receiverNames, boolean async) throws ExchangeException {
		if(filePaths==null || filePaths.isEmpty()) {
			return;
		}
		//获取当前需要发送的接收者
		receiverNames = retrieveReceivers(receiverNames);
		if(receiverNames==null) {
			return;
		}
		for(Iterator iterator = filePaths.iterator(); iterator.hasNext();) {
			final String filePath = (String)iterator.next();
			if(Logger.isInfoEnabled()) {
				Logger.info("ExchangeClient: delete a file, name is " + filePath + ".");
			}
			if(async) { //异步
				addExchangeTask("deleteFile", null, filePath, false, receiverNames, 10);
			}
			else { //同步
				doSendOrDeleteFile(receiverNames, filePath, false, false, null);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.exchange.client.ExchangeClient#sendObject(java.io.Serializable, java.lang.String)
	 */
	public SendObjectAck sendObject(final Serializable object, String receiverName) throws ExchangeException {
		if(receiveOnly) {
			return null;
		}
		if(Logger.isInfoEnabled()) {
			Logger.info("ExchangeClient: send a object, class name is " + object.getClass().getName() + ".");
		}
		final SendObjectAck[] ack = new SendObjectAck[1];
		exchangeData(new ExchangeCallback() {
			public void send(ExchangeDataSender exchangeDataSender, ExchangeConnection connection) throws Exception {
				//发送请求
				ExchangePacketWriter.writePacket(connection.getOutputStream(), new SendObject(object), validateKey);
				//获取应答
				ack[0] = (SendObjectAck)ExchangePacketReader.readPacket(connection.getInputStream(), validateKey);
			}
		}, receiverName, null);
		return ack[0];
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.exchange.client.ExchangeClient#databaseQuery(java.lang.String, java.lang.String, java.util.List, int, int, java.lang.String)
	 */
	public List databaseQuery(final String databaseServiceName, final String hql, final String sql, final List lazyLoadProperties, final int offset, final int limit, String receiverName) throws ExchangeException {
		if(receiveOnly) {
			return null;
		}
		if(Logger.isInfoEnabled()) {
			Logger.info("ExchangeClient: database query, sql is " + (hql==null ? sql : hql) + ".");
		}
		final DatabaseQueryResult[] queryResults = new DatabaseQueryResult[1];
		exchangeData(new ExchangeCallback() {
			public void send(ExchangeDataSender exchangeDataSender, ExchangeConnection connection) throws Exception {
				//发送请求
				ExchangePacketWriter.writePacket(connection.getOutputStream(), new DatabaseQuery(databaseServiceName, hql, sql, lazyLoadProperties, offset, limit), validateKey);
				//获取应答
				queryResults[0] = (DatabaseQueryResult)ExchangePacketReader.readPacket(connection.getInputStream(), validateKey);
			}
		}, receiverName, null);
		return queryResults[0].getRecords();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.exchange.client.ExchangeClient#listReceiverNames()
	 */
	public List listReceiverNames() throws ExchangeException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.exchange.client.ExchangeClient#isExchangeable()
	 */
	public boolean isExchangeable() {
		return exchangeDataSenders!=null && !exchangeDataSenders.isEmpty();
	}
	
	/**
	 * 执行同步更新
	 * @param object
	 * @param receiverNames
	 * @param exchangeReports
	 * @return
	 * @throws ExchangeException
	 */
	private void doSynchUpdate(final Serializable object, final String receiverNames, final Map exchangeReports) throws ExchangeException {
		if(Logger.isInfoEnabled()) {
			Logger.info("ExchangeClient: startup synch update a object, class name is " + object.getClass().getName() + ", id is " + (object instanceof Record ? ((Record)object).getId() : -1) + ".");
		}
		ExchangeCallback senderCallback;
		if(!(object instanceof Record)) { //不是数据库记录
			senderCallback = new ExchangeCallback() {
				public void send(ExchangeDataSender exchangeDataSender, ExchangeConnection exchangeConnection) throws Exception {
					ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new SynchUpdate(object), validateKey);
				}
			};
		}
		else { //数据库记录
			BusinessObject businessObject;
			BusinessService businessService;
			final Record record;
			try {
				businessObject = businessDefineService.getBusinessObject(object.getClass()); //获取业务逻辑定义
				//获取业务逻辑服务
				businessService = (BusinessService)Environment.getService(businessObject.getBusinessServiceName()==null ? "businessService" : businessObject.getBusinessServiceName());
				record = businessService.load(object.getClass(), ((Record)object).getId()); //重新加载记录,以得到最新的记录
			}
			catch (ServiceException e) {
				Logger.exception(e);
				throw new ExchangeException();
			}
			if(record==null) { //记录已经不存在
				return;
			}
			//发送附件
			try {
				doSendOrDeleteAttachments(receiverNames, record, true, exchangeReports);
			}
			catch(Exception e) {
				if(exchangeReports==null) {
					Logger.exception(e);
					throw new ExchangeException(e.getMessage());
				}
			}
			if(!sendRecordEnabled) {
				return;
			}
			senderCallback = new ExchangeCallback() {
				public void send(ExchangeDataSender exchangeDataSender, ExchangeConnection exchangeConnection) throws Exception {
					ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new SynchUpdate(record), validateKey);
				}
			};
		}
		exchangeData(senderCallback, receiverNames, exchangeReports);
	}

	/**
	 * 执行同步删除
	 * @param object
	 * @param receiverNames
	 * @param exchangeReports
	 * @return
	 * @throws ExchangeException
	 */
	private void doSynchDelete(final Serializable object, final String receiverNames, final Map exchangeReports) throws ExchangeException {
		if(Logger.isInfoEnabled()) {
			Logger.info("ExchangeClient: startup synch delete a record, class name is " + object.getClass().getName() + ", id is " + (object instanceof Record ? ((Record)object).getId() : -1) + ".");
		}
		if(object instanceof Record) { //是数据库记录
			try {
				doSendOrDeleteAttachments(receiverNames, (Record)object, false, exchangeReports);
			}
			catch (Exception e) {
				if(exchangeReports==null) {
					Logger.exception(e);
					throw new ExchangeException(e.getMessage());
				}
			}
			if(!sendRecordEnabled) {
				return;
			}
		}
		exchangeData(new ExchangeCallback() {
			public void send(ExchangeDataSender exchangeDataSender, ExchangeConnection exchangeConnection) throws Exception {
				ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new SynchDelete(object), validateKey);
			}
		}, receiverNames, exchangeReports);
	}

	/**
	 * 获取当前能够发送的通道
	 * @param receiverNames
	 * @return
	 */
	private String retrieveReceivers(String receiverNames) {
		if(receiveOnly || exchangeDataSenders==null || exchangeDataSenders.isEmpty()) {
			return null;
		}
		String sendabled = null;
		for(Iterator iterator = exchangeDataSenders.iterator(); iterator.hasNext();) {
			ExchangeDataSender exchangeDataSender = (ExchangeDataSender)iterator.next();
			String[] names = exchangeDataSender.getReceiverNames().split(",");
			for(int i=0; i<names.length; i++) {
				if(!Thread.currentThread().getName().endsWith("-" + names[i])) { //通过线程名称,检查数据是不是从当前通道发送过来的,如果是,就不允许发送,避免循环发送
					sendabled = (sendabled==null ? "" : sendabled + ",") + names[i];
				}
			}
		}
		return sendabled;
	}
	
	/**
	 * 递归函数:发送附件
	 * @param record
	 * @throws Exception
	 */
	private void doSendOrDeleteAttachments(String receiverNames, Record record, boolean isSend, Map exchangeReports) throws Exception {
		BusinessObject businessObject = businessDefineService.getBusinessObject(record.getClass());
		if(businessObject==null) {
			return;
		}
		//发送附件
		Collections.sort(businessObject.getFields(), new Comparator() {
			public int compare(Object arg0, Object arg1) {
				Field field0 = (Field)arg0;
				Field field1 = (Field)arg1;
				//附件类型是""的排到最前面,避免误删文件
				if("".equals(field0.getParameter("type"))) {
					return -1;
				}
				if("".equals(field1.getParameter("type"))) {
					return 1;
				}
				return 0;
			}
		});
		for(Iterator iteratorField = businessObject.getFields().iterator(); iteratorField.hasNext();) {
			Field fieldDefine = (Field)iteratorField.next();
			if("components".equals(fieldDefine.getType()) && !"false".equals(fieldDefine.getParameter("lazyLoad"))) { //组成部分,发送组成部分的附件
				Collection components;
				try {
					components = (Collection)PropertyUtils.getProperty(record, fieldDefine.getName());
					if(components==null || components.isEmpty()) {
						continue;
					}
				}
				catch (Exception e) {
					continue;
				}
				for(Iterator iterator = components.iterator(); iterator.hasNext();) {
					Object component = iterator.next();
					if(component instanceof Record) {
						doSendOrDeleteAttachments(receiverNames, (Record)component, isSend, exchangeReports); //递归调用
					}
				}
			}
			else if("attachment".equals(fieldDefine.getType()) || "image".equals(fieldDefine.getType()) || "video".equals(fieldDefine.getType())) {
				//获取附件服务
				String serviceName = (String)fieldDefine.getParameter("serviceName");
				serviceName = (serviceName==null || serviceName.equals("") ? fieldDefine.getType() + "Service" : serviceName);
				AttachmentService attachmentService = (AttachmentService)Environment.getService(serviceName);
				//获取附件类型
				String attachmentType = FieldUtils.getAttachmentType(record, null, fieldDefine);
				if(isSend) { //发送,检查是否有附件
					List attachments = attachmentService.list(businessObject.getApplicationName(), attachmentType, record.getId(), false, 1, null);
					if(attachments==null || attachments.isEmpty()) {
						continue;
					}
				}
				//获取附件目录
				String attachmentDirectory = attachmentService.getSavePath(businessObject.getApplicationName(), attachmentType, record.getId(), false);
				//删除目录
				doSendOrDeleteFile(receiverNames, attachmentDirectory, true, false, exchangeReports);
				//发送文件
				if(isSend) {
					doSendOrDeleteFile(receiverNames, attachmentDirectory, true, true, exchangeReports);
				}
			}
		}
	}
	
	/**
	 * 发送或删除文件,如果是发送返回文件在对方服务器上的路径
	 * @param exchangeChannel
	 * @param filePath
	 * @param createDirectoryIfNotExists 目录不存在时,是否自动创建
	 * @param isSend 是否文件发送
	 * @param remoteFilePaths 远程文件路径
	 * @return
	 * @throws Exception
	 */
	private List doSendOrDeleteFile(String receiverNames, String filePath, final boolean createDirectoryIfNotExists, final boolean isSend, Map exchangeReports) throws ExchangeException {
		final File file = new File(filePath);
		if(isSend && !file.exists()) { //发送,且文件不存在
			return null;
		}
		filePath = file.getPath().replace('\\', '/');
		if(!file.isFile() && !filePath.endsWith("/")) {
			filePath += "/";
		}
		if(Logger.isDebugEnabled()) {
			Logger.debug("ExchangeClient: startup " + (isSend ? "send" : "delete") + " file " + filePath + ".");
		}
		//检查文件是否存放在附件基准路径中
		final boolean fileInAttachmentDirectory = attachmentBaseDirectory!=null && filePath.substring(0, Math.min(filePath.length(), attachmentBaseDirectory.length())).equalsIgnoreCase(attachmentBaseDirectory);
		final String localFilePath = filePath;
		final String remoteFilePath = fileInAttachmentDirectory ? localFilePath.substring(attachmentBaseDirectory.length()) : localFilePath; //远程文件存放路径
		final List[] remoteFilePaths = {null};
		ExchangeCallback callback = new ExchangeCallback() {
			public void send(ExchangeDataSender exchangeDataSender, ExchangeConnection exchangeConnection) throws Exception {
				if(!isFileExchangeByCopy(exchangeDataSender, file)) { //不是使用文件拷贝方式交换
					remoteFilePaths[0] = doSendOrDeleteFileByExchange(exchangeConnection, fileInAttachmentDirectory, localFilePath, remoteFilePath, createDirectoryIfNotExists, isSend);
				}
				else { //使用文件拷贝方式交换
					remoteFilePaths[0] = doSendOrDeleteFileByCopy(exchangeConnection, exchangeDataSender, localFilePath, fileInAttachmentDirectory, remoteFilePath, createDirectoryIfNotExists, isSend);
				}
			}
		};
		exchangeData(callback, receiverNames, exchangeReports);
		return remoteFilePaths[0];
	}
	
	/**
	 * 使用交换方式发送或者删除文件
	 * @param exchangeConnection
	 * @param fileInAttachmentDirectory
	 * @param localFilePath
	 * @param remoteFilePath
	 * @param createDirectoryIfNotExists
	 * @param isSend
	 * @return
	 * @throws Exception
	 */
	private List doSendOrDeleteFileByExchange(ExchangeConnection exchangeConnection, boolean fileInAttachmentDirectory, String localFilePath, String remoteFilePath, final boolean createDirectoryIfNotExists, final boolean isSend) throws Exception {
		 //不是使用文件拷贝方式交换
		if(!isSend) { //删除文件
			if(fileInAttachmentDirectory) { //文件在附件基准路径中
				ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new FileDelete(remoteFilePath), validateKey); //发送文件删除命令
			}
			return null;
		}
		File file = new File(localFilePath);
		File[] files = file.isFile() ? new File[]{file} : file.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});
		try {
			List remoteFilePaths = new ArrayList();
			for(int i=0; files!=null && i<files.length; i++) {
				//发送文件发送命令
				ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new FileSend(remoteFilePath + (file.isFile() ? "" : files[i].getName()), files[i].length(), createDirectoryIfNotExists, !fileInAttachmentDirectory), validateKey);
				if(!createDirectoryIfNotExists) { //目录不存在时,不允许创建目录
					//等待应答,如果远程文件名称为空,说明对方不需要接收文件
					FileSendAck fileSendAck = (FileSendAck)ExchangePacketReader.readPacket(exchangeConnection.getInputStream(), validateKey);
					if(fileSendAck.getRemoteFilePath()==null) {
						return null;
					}
				}
				//发送文件内容
				FileInputStream inputStream = null;
				try {
					inputStream = new FileInputStream(files[i]);
					byte[] buffer = new byte[8192]; //缓存
					int readLength;
					long totalRead = 0;
					boolean isLast = false;
					while(!isLast && (readLength=inputStream.read(buffer))!=-1) {
						FileData fileData = new FileData();
						if(readLength>=8192) {
							fileData.setData(buffer);
						}
						else {
							byte[] data = new byte[readLength];
							System.arraycopy(buffer, 0, data, 0, readLength);
							fileData.setData(data);
						}
						totalRead += readLength;
						isLast = (totalRead>=files[i].length());
						fileData.setLast(isLast);
						ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), fileData, validateKey);
					}
				}
				finally {
					try {
						inputStream.close();
					}
					catch(Exception e) {
						
					}
				}
				//读取文件发送应答
				ExchangePacket ack = ExchangePacketReader.readPacket(exchangeConnection.getInputStream(), validateKey);
				if(!(ack instanceof FileSendAck)) {
					throw new ExchangeException("no file send ack received");
				}
				FileSendAck fileSendAck = (FileSendAck)ack;
				remoteFilePaths.add(fileSendAck.getRemoteFilePath());
			}
			return remoteFilePaths.isEmpty() ? null : remoteFilePaths;
		}
		finally {
			ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new FileSendComplete(), validateKey); //输出发送完成包
		}
	}
	
	/**
	 * 使用拷贝方式发送或者删除文件
	 * @param exchangeConnection
	 * @param exchangeDataSender
	 * @param file
	 * @param localFilePath
	 * @param fileInAttachmentDirectory
	 * @param remoteFilePath
	 * @param createDirectoryIfNotExists
	 * @param isSend
	 * @return
	 * @throws Exception
	 */
	private List doSendOrDeleteFileByCopy(ExchangeConnection exchangeConnection, ExchangeDataSender exchangeDataSender, String localFilePath, boolean fileInAttachmentDirectory, String remoteFilePath, final boolean createDirectoryIfNotExists, final boolean isSend) throws Exception {
		if(!fileInAttachmentDirectory) { //文件不在基准目录中
			//将文件保存到交换目录中的"TemporaryFiles"目录
			remoteFilePath = "TemporaryFiles" + (localFilePath.startsWith("/") ? "" : "/") + localFilePath.replaceAll("\\\\", "/").replaceAll(":", "");
		}
		if(isSend && !FileUtils.isExists(localFilePath)) { //文件发送,且文件已经不存在
			return null;
		}
		File file = new File(localFilePath);
		long fileLength = 0;
		List remoteFilePaths = new ArrayList(); 
		if(isSend) {
			if(file.isFile()) {
				fileLength = file.length();
				remoteFilePaths.add(remoteFilePath);
			}
			else {
				File[] files = file.listFiles(new FileFilter() {
					public boolean accept(File pathname) {
						return pathname.isFile();
					}
				});
				for(int i=0; i<(files==null ? 0 : files.length); i++) {
					fileLength += files[i].length();
					remoteFilePaths.add(remoteFilePath + files[i].getName());
				}
			}
			if(fileLength==0) {
				return null;
			}
		}
		try {
			String exchangeFilePath = exchangeDataSender.getFileExchangePath() + remoteFilePath;
			String exchangeDirectory = exchangeFilePath.substring(0, exchangeFilePath.lastIndexOf('/') + 1);
			for(int i=0; i<5; i++) {
				if(!isSend) { //删除文件
					if(!file.isFile()) { //目录
						FileUtils.deleteDirectory(exchangeFilePath); //删除目录
					}
					else {
						FileUtils.deleteFile(exchangeFilePath);
						FileUtils.deleteDirectoryIfEmpty(exchangeDirectory);
					}
				}
				else if(isSend && i==0) { //发送文件
					if(!FileUtils.isExists(exchangeDirectory)) { //检查交换目录中是否有对应的目录存在
						if(!createDirectoryIfNotExists) { //不允许自动创建目录
							return null;
						}
						FileUtils.createDirectory(exchangeDirectory);
					}
					if(file.isFile()) { //单个文件
						FileUtils.copyFile(localFilePath, exchangeFilePath, true, false); //复制到附件目录
					}
					else { //目录
						FileUtils.copyDirectory(localFilePath, exchangeFilePath, false, true);
					}
				}
				if(exchangeDataSender.isReliableCopy()) { //可靠的交换,不需要校验
					break;
				}
				boolean pass = false;
				for(int j=0; j<5 && !pass; j++) { //校验5次
					//发送文件校验命令
					ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new FileValidate(remoteFilePath, !isSend, fileLength), validateKey);
					//接收文件校验结果
					FileValidateResult fileValidateResult = (FileValidateResult)ExchangePacketReader.readPacket(exchangeConnection.getInputStream(), validateKey);
					if(Logger.isDebugEnabled()) {
						Logger.debug("ExchangeClient: validate " + (isSend ? "sent" : "deleted") + " file " + localFilePath + ", result is " + fileValidateResult.isValidatePass() + ".");
					}
					if(pass=fileValidateResult.isValidatePass()) { //交换成功
						break;
					}
					//等待,最少500ms
					Thread.sleep(Math.max(500, !isSend ? 500 : (long)((0.0d + fileLength)/(0.0d + exchangeDataSender.getFileExchangeSpeed()) * 1000)));
				}
				if(pass) { //交换成功
					break;
				}
				if(i==4) { //已经重试过5次
					throw new ExchangeException("file exchange failed");
				}
				if(Logger.isDebugEnabled()) {
					Logger.debug("ExchangeClient: file " + localFilePath + " exchange failed, retry again.");
				}
				//重试
				if(!isSend) { //删除
					if(!file.isFile()) { //删除目录
						FileUtils.createDirectory(exchangeFilePath); //重建目录
					}
					else { //删除文件
						FileUtils.saveStringToFile(exchangeFilePath, " ", "utf-8", true); //重建文件
					}
				}
				else if(file.isFile()) { //发送,重设文件时间
					new File(exchangeFilePath).setLastModified(System.currentTimeMillis());
				}
				else {
					File[] files = new File(exchangeFilePath).listFiles(new FileFilter() {
						public boolean accept(File pathname) {
							return pathname.isFile();
						}
					});
					for(int j=0; j<(files==null ? 0 : files.length); j++) {
						files[j].setLastModified(System.currentTimeMillis());
					}
				}
				Thread.sleep(3000); //等待3秒
			}
			return remoteFilePaths.isEmpty() ? null : remoteFilePaths;
		}
		finally {
			ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new FileValidateComplete(), validateKey); //输出校验完成包
		}
	}
	
	/**
	 * 判断文件交换时是否使用文件拷贝方式
	 * @param exchangeDataSender
	 * @param file
	 * @return
	 */
	private boolean isFileExchangeByCopy(ExchangeDataSender exchangeDataSender, File file) {
		if(!exchangeDataSender.isFileExchangeByCopy()) { //不是使用文件交换方式
			return false;
		}
		if(exchangeDataSender.getPathsCopyDisabled()==null) { //没有设置禁止拷贝的目录
			return true;
		}
		//检查文件是否在禁止拷贝的目录中
		String path = file.getPath().replace('\\', '/');
		if(file.isDirectory()) {
			path += "/";
		}
		String[] pathsCopyDisabled = exchangeDataSender.getPathsCopyDisabled().split(",");
		for(int i=0; i<pathsCopyDisabled.length; i++) {
			pathsCopyDisabled[i] = pathsCopyDisabled[i].trim();
			if(pathsCopyDisabled[i].isEmpty()) {
				continue;
			}
			pathsCopyDisabled[i] = new File(pathsCopyDisabled[i]).getPath().replace('\\', '/');
			if(path.startsWith(pathsCopyDisabled[i].endsWith("/") ? pathsCopyDisabled[i] : pathsCopyDisabled[i] + "/")) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 执行发送
	 * @param callback
	 * @param sendableExchangeChannels
	 * @param realTime 当前任务是否要求实时发送
	 * @return
	 * @throws ExchangeException
	 */
	private void exchangeData(ExchangeCallback callback, String receiverNames, Map exchangeReports) throws ExchangeException {
		String[] names = receiverNames.split(",");
		for(int i=0; i<names.length; i++) {
			ExchangeReport exchangeReport;
			if(exchangeReports!=null && (exchangeReport=(ExchangeReport)exchangeReports.get(names[i]))!=null && !exchangeReport.isSuccess()) { //之前已经发送失败,不继续发送
				continue;
			}
			exchangeReport = new ExchangeReport();
			try {
				exchangeData(callback, names[i]);
				exchangeReport.setSuccess(true); //交换成功
			}
			catch(Exception e) {
				if(exchangeReports==null) {
					if(e instanceof ExchangeException) {
						throw (ExchangeException)e;
					}
					Logger.exception(e);
					throw new ExchangeException();
				}
				exchangeReport.setSuccess(false);
				exchangeReport.setException(e); //交换失败
			}
			if(exchangeReports!=null) {
				exchangeReports.put(names[i], exchangeReport);
			}
		}
	}
	
	/**
	 * 执行发送
	 * @param callback
	 * @param exchangeChannel
	 * @return
	 * @throws ExchangeException
	 */
	private void exchangeData(ExchangeCallback callback, String receiverName) throws Exception {
		//交换过程:连接数据交换中心->发送交换请求包->交换中心转发请求包给接收者->接收者创建数据交换连接->接收者发送数据交换应答->交换中心转发交换应答->开始发送数据->等待交换结束包
		synchronized(exchangeRequestMutex) {
			if(exchangeRequest>=maxExchangeRequest) { //请求数达到上限
				exchangeRequestMutex.wait(30000); //等待30s
			}
			if(exchangeRequest>=maxExchangeRequest) {
				throw new ExchangeException("request overflow");
			}
			exchangeRequest++;
		}
		try {
			//获取接收者对应的发送器
			ExchangeDataSender exchangeDataSender = null;
			for(Iterator iterator = exchangeDataSenders.iterator(); iterator.hasNext();) {
				ExchangeDataSender sender = (ExchangeDataSender)iterator.next();
				if(("," + sender.getReceiverNames() + ",").indexOf("," + receiverName + ",")!=-1) {
					exchangeDataSender = sender;
					break;
				}
			}
			if(exchangeDataSender==null) {
				return;
			}
			for(int i=0; i<3; i++) { //重试3次
				//连接交换服务器
				ExchangeConnection exchangeConnection = exchangeDataSender.connect();
				try {
					//发送数据交换请求
					ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new ExchangeRequest(name, receiverName, UUIDLongGenerator.generateId()), validateKey);
					//等待交换中心发送开始
					ExchangeRequestAck exchangeRequestAck = (ExchangeRequestAck)ExchangePacketReader.readPacket(exchangeConnection.getInputStream(), validateKey);
					if(exchangeRequestAck==null || !exchangeRequestAck.isExchangeAbled()) {
						i = 2;
						throw new ExchangeException();
					}
					//发送数据
					callback.send(exchangeDataSender, exchangeConnection);
					//等待交换结束包
					ExchangeComplete exchangeCompletePacket = (ExchangeComplete)ExchangePacketReader.readPacket(exchangeConnection.getInputStream(), validateKey);
					if(exchangeCompletePacket!=null && exchangeCompletePacket.isSuccess()) {
						break;
					}
					throw new ExchangeException("remote failed");
				}
				catch(Exception e) {
					Logger.exception(e);
					if(i==2) {
						throw e;
					}
					try {
						Thread.sleep(1000); //等待1s
					}
					catch (InterruptedException ie) {
					
					}
				}
				finally {
					try {
						//发送交换结束包到交换中心服务器
						ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new ExchangeComplete(true), validateKey);
					}
					catch(Exception e) {
						
					}
					//关闭连接
					exchangeConnection.close();
				}
			}
		}
		finally {
			synchronized(exchangeRequestMutex) {
				exchangeRequest--;
				exchangeRequestMutex.notifyAll();
			}
		}
	}
	
	/**
	 * 追加一个交换任务
	 * @param taskType 任务类型
	 * @param object 需要同步的对象
	 * @param filePath 文件路径
	 * @param createDirectoryIfNotExists 目录不存在时是否创建
	 * @param receiverNames 通道列表
	 * @param delayMilliseconds 延时时间
	 */
	public void addExchangeTask(String taskType, Serializable object, String filePath, boolean createDirectoryIfNotExists, String receiverNames, int delayMilliseconds) throws ExchangeException {
		try {
			if(distributionService!=null && !distributionService.isMasterServer(false)) { //当前服务器不是主服务器
				if(Logger.isDebugEnabled()) {
					Logger.debug("ExchangeClient: remote append exchage task.");
				}
				try {
					Serializable[] args = {taskType, object, filePath, new Boolean(createDirectoryIfNotExists), receiverNames, new Integer(delayMilliseconds)};
					distributionService.invokeMethodOnMasterServer("exchangeClient", "addExchangeTask", args);
					return;
				}
				catch(Exception e) {
					//发送到远程服务器失败,把当前服务器设置为主服务器,并由自己来发送
					Logger.exception(e);
					if(!distributionService.isMasterServer(true)) { //当前服务器仍然不是主服务器
						throw new ServiceException();
					}
				}
			}
		}
		catch(ServiceException e) {
			Logger.exception(e);
			throw new ExchangeException();
		}
		if(filePath!=null) {
			filePath = filePath.replace('\\', '/');
		}
		ExchangeTask exchangeTask = new ExchangeTask();
		exchangeTask.setId(UUIDLongGenerator.generateId());
		exchangeTask.setReceivers(receiverNames); //接收者列表
		exchangeTask.setCompletedReceivers(null); //完成交换的通道
		exchangeTask.setCreated(DateTimeUtils.now()); //任务创建时间
		exchangeTask.setTaskType(taskType); //任务类型
		exchangeTask.setFilePath(filePath); //文件路径,文件交换时有效
		exchangeTask.setCreateDirectoryIfNeed(createDirectoryIfNotExists ? '1' : '0'); //目录不存在时是否创建
		exchangeTask.setStartupTime(DateTimeUtils.add(exchangeTask.getCreated(), Calendar.MILLISECOND, delayMilliseconds)); //任务启动时间,重试5次后,传输时间加10分钟,超过30次失败加1小时,超过100次,任务终止,避免占用过多的资源
		if(object!=null) {
			BusinessObject businessObject = null;
			try {
				businessObject = businessDefineService.getBusinessObject(object.getClass()); //获取业务逻辑定义
			}
			catch(ServiceException e) {
				Logger.exception(e);
				throw new ExchangeException();
			}
			exchangeTask.setRecordTitle(businessObject==null ? object.getClass().getName() : businessObject.getTitle()); //记录类型,如：文章、政府信息
			exchangeTask.setRecordClassName(object.getClass().getName()); //记录类名称
			exchangeTask.setRecordId(object instanceof Record ? ((Record)object).getId() : 0); //记录ID
			try {
				exchangeTask.setRecordEncoded(Encoder.getInstance().objectBase64Encode(object)); //记录详情
			}
			catch (Exception e) {
				Logger.exception(e);
				throw new ExchangeException(e.getMessage());
			}
		}
		exchangeTask.setFailedCount(0); //交换失败次数
		exchangeTask.setFailedReason(null); //交换失败原因
		synchronized(exchangeMutex) {
			if(object!=null && (object instanceof Record)) { //数据库记录
				Record record = (Record)object;
				if(nextExchangeTask==null || record.getId()!=nextExchangeTask.getRecordId() || !record.getClass().getName().equals(nextExchangeTask.getRecordClassName())) {
					//删除相同记录对应的交换任务
					String hql = "from ExchangeTask ExchangeTask" +
								 " where ExchangeTask.recordClassName='" + record.getClass().getName() + "'" +
								 " and ExchangeTask.recordId=" + record.getId();
					databaseService.deleteRecordsByHql(hql);
				}
			}
			else if(filePath!=null) { //文件交换
				if(nextExchangeTask==null || !filePath.equalsIgnoreCase(nextExchangeTask.getFilePath())) {
					//删除相同文件对应的交换任务
					String hql = "from ExchangeTask ExchangeTask" +
								 " where ExchangeTask.filePath='" + JdbcUtils.resetQuot(filePath) + "'";
					databaseService.deleteRecordsByHql(hql);
				}
			}
			databaseService.saveRecord(exchangeTask);
			//如果当前任务为空或者当前任务的启动时间在本任务之后
			if(nextExchangeTask==null || nextExchangeTask.getStartupTime().after(exchangeTask.getStartupTime())) {
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					public void run() {
						synchronized(exchangeMutex) {
							exchangeMutex.notify(); //唤醒等待线程
						}
					}
				}, delayMilliseconds);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Thread.currentThread().setName(Thread.currentThread().getName() + "-Exchange-" + name);
		while(!stopped) {
			try {
				if(distributionService!=null && !distributionService.isMasterServer(false)) { //当前服务器已经不是主服务器
					while(!distributionService.isMasterServer(true)) {  //每隔60秒检查一次当前服务器是否已经成为主服务器
						Thread.sleep(60000); //等待60秒
					}
				}
				List receivers = null;
				if(nextExchangeTask!=null && !nextExchangeTask.getStartupTime().after(DateTimeUtils.now())) {
					//设置接收者列表
					receivers = ListUtils.generateList(nextExchangeTask.getReceivers(), ",");
					for(int i=receivers.size()-1; i>=0; i--) {
						if(("," + nextExchangeTask.getCompletedReceivers() + ",").indexOf("," + receivers.get(i) + ",")!=-1) {
							receivers.remove(i); //剔除已经成功的通道
						}
					}
				}
				//发送
				Map exchangeReports = new HashMap();
				String failedReason = null;
				if(receivers!=null && !receivers.isEmpty()) {
					String receiverNames = ListUtils.join(receivers, ",", false);
					try {
						//解析对象
						Serializable record = (Serializable)Encoder.getInstance().objectBase64Decode(nextExchangeTask.getRecordEncoded());
						if("updateRecord".equals(nextExchangeTask.getTaskType())) { //同步更新
							doSynchUpdate(record, receiverNames, exchangeReports);
						}
						else if("deleteRecord".equals(nextExchangeTask.getTaskType())) { //同步删除
							doSynchDelete(record, receiverNames, exchangeReports);
						}
						else if("sendFile".equals(nextExchangeTask.getTaskType())) { //发送文件
							doSendOrDeleteFile(receiverNames, nextExchangeTask.getFilePath(), nextExchangeTask.getCreateDirectoryIfNeed()=='1', true, exchangeReports);
						}
						else if("deleteFile".equals(nextExchangeTask.getTaskType())) { //删除文件
							doSendOrDeleteFile(receiverNames, nextExchangeTask.getFilePath(), false, false, exchangeReports);
						}
					}
					catch(Error e) {
						e.printStackTrace();
						Logger.error(e.getMessage());
						failedReason = StringUtils.errorToString(e);
					}
					catch(Exception e) {
						Logger.exception(e);
						failedReason = StringUtils.exceptionToString(e);
					}
				}
				synchronized(exchangeMutex) {
					if(receivers!=null) {
						boolean success = true;
						for(Iterator iterator = receivers.isEmpty() ? null : exchangeReports.keySet().iterator(); iterator!=null && iterator.hasNext();) {
							String receiverName = (String)iterator.next();
							ExchangeReport exchangeReport = (ExchangeReport)exchangeReports.get(receiverName);
							if(exchangeReport.isSuccess()) { //交换成功的通道
								nextExchangeTask.setCompletedReceivers((nextExchangeTask.getCompletedReceivers()==null ? "" : nextExchangeTask.getCompletedReceivers() + ",") + receiverName);
							}
							else { //交换失败的通道
								failedReason = (failedReason==null ? "" : failedReason + "\r\n\r\n") + "发往\"" + receiverName + "\"时失败,出错原因:\r\n" + StringUtils.exceptionToString(exchangeReport.getException());
								success = false;
							}
						}
						if(success) { //交换成功
							try {
								databaseService.deleteRecord(nextExchangeTask); //删除交换任务
							}
							catch(Exception e) {
								Logger.exception(e);
							}
						}
						else {
							nextExchangeTask.setFailedReason(failedReason); //失败原因
							nextExchangeTask.setFailedCount(nextExchangeTask.getFailedCount() + 1); //错误次数加1
							//设置下一次发送时间
							if(nextExchangeTask.getFailedCount()>=100) { //100次以上发送失败
								nextExchangeTask.setStartupTime(null); //不再发送
							}
							else if(nextExchangeTask.getFailedCount()>30) { //30次以上发送失败
								nextExchangeTask.setStartupTime(DateTimeUtils.add(DateTimeUtils.now(), Calendar.HOUR, 2)); //延后2小时
							}
							else if(nextExchangeTask.getFailedCount()>10) { //10次以上发送失败
								nextExchangeTask.setStartupTime(DateTimeUtils.add(DateTimeUtils.now(), Calendar.MINUTE, 30)); //延后30分钟
							}
							else if(nextExchangeTask.getFailedCount()>5) { //5次以内发送失败
								nextExchangeTask.setStartupTime(DateTimeUtils.add(DateTimeUtils.now(), Calendar.MINUTE, 10)); //延后10分钟
							}
							else { //5次以内发送失败
								nextExchangeTask.setStartupTime(DateTimeUtils.add(DateTimeUtils.now(), Calendar.MINUTE, 5)); //延后5分钟
							}
							databaseService.updateRecord(nextExchangeTask); //更新记录
						}
					}
					//获取下一个需要交换的任务
					String hql = "from ExchangeTask ExchangeTask" +
								 " where not ExchangeTask.startupTime is null" +
								 " order by ExchangeTask.startupTime";
					nextExchangeTask = (ExchangeTask)databaseService.findRecordByHql(hql);
					if(nextExchangeTask==null) { //没有发送任务
						exchangeMutex.wait(); //无限期等待
					}
					else {
						long timeMillis = System.currentTimeMillis();
						if(nextExchangeTask.getStartupTime().getTime()>timeMillis) {
							exchangeMutex.wait(nextExchangeTask.getStartupTime().getTime() - timeMillis); //等待到下一个数据交换时间
						}
					}
				}
			}
			catch(Error e) {
				e.printStackTrace();
				Logger.error(e.getMessage());
				try {
					Thread.sleep(10000); //有异常,等待10秒
				}
				catch(InterruptedException ie) {

				}
			}
			catch (Exception e) {
				Logger.exception(e);
				try {
					Thread.sleep(10000); //有异常,等待10秒
				}
				catch(InterruptedException ie) {

				}
			}
		}
	}
	
	/**
	 * 处理数据交换请求
	 * @param exchangeDataSender
	 * @param exchangeRequest
	 */
	private void processExchangeRequest(ExchangeDataSender exchangeDataSender, ExchangeRequest exchangeRequest) throws ExchangeException {
		boolean success = false;
		//创建新的连接
		ExchangeConnection exchangeConnection = exchangeDataSender.connect();
		try {
			//发送数据交换应答包
			ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new ExchangeRequestAck(exchangeRequest.getSenderName(), exchangeRequest.getReceiverName(), exchangeRequest.getRequestId(), true), validateKey);
			//等待数据包
			ExchangePacket packet = ExchangePacketReader.readPacket(exchangeConnection.getInputStream(), validateKey);
			if(packet instanceof SynchUpdate) { //同步更新
				processSynchUpdatePacket((SynchUpdate)packet, exchangeRequest.getSenderName());
			}
			else if(packet instanceof SynchDelete) { //同步删除
				processSynchDeletePacket((SynchDelete)packet, exchangeRequest.getSenderName());
			}
			else if(packet instanceof DatabaseQuery) { //数据库查询
				processDatabaseQueryPacket((DatabaseQuery)packet, exchangeConnection);
			}
			else if(packet instanceof SendObject) { //对象发送
				processSendObjectPacket((SendObject)packet, exchangeConnection, exchangeRequest.getSenderName());
			}
			else if(packet instanceof FileSend) { //文件发送
				processFileSendPacket((FileSend)packet, exchangeConnection);
			}
			else if(packet instanceof FileDelete) { //文件删除
				processFileDeletePacket((FileDelete)packet);
			}
			else if(packet instanceof FileValidate) { //文件校验
				processFileValidatePacket((FileValidate)packet, exchangeConnection);
			}
			success = true;
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		//发送交换完成包
		try {
			ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new ExchangeComplete(success), validateKey);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		//关闭连接
		exchangeConnection.close();
	}
	
	/**
	 * 处理记录同步更新请求
	 * @param synchUpdatePacket
	 * @param senderName
	 * @throws Exception
	 */
	private void processSynchUpdatePacket(SynchUpdate synchUpdatePacket, String senderName) throws Exception {
		if(Logger.isInfoEnabled()) {
			Logger.info("ExchangeClient: update a record, class name is " + synchUpdatePacket.getObject().getClass().getName() + ", id is " + (synchUpdatePacket.getObject() instanceof Record ? ((Record)synchUpdatePacket.getObject()).getId() : -1) + ".");
		}
		//获取业务逻辑定义
		BusinessObject businessObject = businessDefineService.getBusinessObject(synchUpdatePacket.getObject().getClass());
		//获取业务逻辑服务
		BusinessService businessService = (BusinessService)Environment.getService(businessObject.getBusinessServiceName()==null ? "businessService" : businessObject.getBusinessServiceName());
		//更新记录
		businessService.synchUpdate(synchUpdatePacket.getObject(), senderName);
	}
	
	/**
	 *  处理记录同步删除请求
	 * @param synchDeletePacket
	 * @param senderName
	 * @throws Exception
	 */
	private void processSynchDeletePacket(SynchDelete synchDeletePacket, String senderName) throws Exception {
		if(Logger.isInfoEnabled()) {
			Logger.info("ExchangeClient: delete a record, class name is " + synchDeletePacket.getObject().getClass().getName() + ", id is " + (synchDeletePacket.getObject() instanceof Record ? ((Record)synchDeletePacket.getObject()).getId() : -1) + ".");
		}
		//获取业务逻辑定义
		BusinessObject businessObject = businessDefineService.getBusinessObject(synchDeletePacket.getObject().getClass());
		//获取业务逻辑服务
		BusinessService businessService = (BusinessService)Environment.getService(businessObject.getBusinessServiceName()==null ? "businessService" : businessObject.getBusinessServiceName());
		//删除记录
		businessService.synchDelete(synchDeletePacket.getObject(), senderName);
	}
	
	/**
	 * 处理数据查询请求
	 * @param databaseQueryPacket
	 * @param exchangeConnection
	 * @throws Exception
	 */
	private void processDatabaseQueryPacket(DatabaseQuery databaseQueryPacket, ExchangeConnection exchangeConnection) throws Exception {
		//检查SQL是否允许被执行
		List sqls;
		Map permissibleSqlMaps = (databaseQueryPacket.getHql()!=null ? permissibleHqls : permissibleSqls);
		if(permissibleSqlMaps!=null && (sqls=(List)permissibleSqlMaps.get(databaseQueryPacket.getDatabaseServiceName()))!=null) { //没有配置允许执行的语句,则不管什么sql都允许执行
			boolean allow = false;
			for(Iterator iterator = sqls.iterator(); iterator.hasNext();) {
				String permissibleSql = (String)iterator.next();
				if((databaseQueryPacket.getHql()==null ? databaseQueryPacket.getSql() : databaseQueryPacket.getHql()).startsWith(permissibleSql)) { //检查前部是否匹配
					allow = true;
					break;
				}
			}
			if(!allow) {
				throw new Exception("not permissible");
			}
		}
		List records = null;
		if(databaseQueryPacket.getHql()!=null) { //按hql查询数据库
			DatabaseService databaseService = (DatabaseService)Environment.getService(databaseQueryPacket.getDatabaseServiceName());
			records = databaseService.findRecordsByHql(databaseQueryPacket.getHql(), databaseQueryPacket.getLazyLoadProperties(), databaseQueryPacket.getOffset(), databaseQueryPacket.getLimit());
		}
		else if(databaseQueryPacket.getSql()!=null) { //按sql查询数据库
			DatabaseService databaseService = (DatabaseService)Environment.getService(databaseQueryPacket.getDatabaseServiceName());
			records = databaseService.executeQueryBySql(databaseQueryPacket.getSql(), databaseQueryPacket.getOffset(), databaseQueryPacket.getLimit());
		}
		//发送执行结果
		ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new DatabaseQueryResult(records), validateKey);
	}
	
	/**
	 * 处理对象发送请求
	 * @param sendObjectRequest
	 * @param exchangeConnection
	 * @param senderName
	 * @throws Exception
	 */
	private void processSendObjectPacket(SendObject sendObjectRequest, ExchangeConnection exchangeConnection, String senderName) throws Exception {
		//获取业务逻辑定义
		BusinessObject businessObject = businessDefineService.getBusinessObject(sendObjectRequest.getObject().getClass());
		//获取业务逻辑服务
		BusinessService businessService = (BusinessService)Environment.getService(businessObject.getBusinessServiceName()==null ? "businessService" : businessObject.getBusinessServiceName());
		//处理接收到的对象
		Serializable result = businessService.processExchangedObject(sendObjectRequest.getObject(), senderName);
		//发送执行结果
		ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new SendObjectAck(result), validateKey);
	}
	
	/**
	 * 处理文件发送请求
	 * @param fileSendPacket
	 * @param exchangeConnection
	 * @throws ExchangeException
	 */
	private void processFileSendPacket(FileSend fileSendPacket, ExchangeConnection exchangeConnection) throws Exception {
		if(Logger.isInfoEnabled()) {
			Logger.info("ExchangeClient: receive file " + fileSendPacket.getRemoteFilePath() + ".");
		}
		//设置文件路径
		String savePath = fileSendPacket.getRemoteFilePath().replaceAll("\\\\", "/").replaceAll(":", "");
		savePath = (fileSendPacket.isSaveToTemporaryDirectory() ? temporaryFileManageService.createTemporaryDirectory(null) : attachmentBaseDirectory) + savePath.substring(savePath.startsWith("/") ? 1 : 0);
		String dir = savePath.substring(0, savePath.lastIndexOf('/') + 1);
		if(!fileSendPacket.isCreateDirectoryIfNotExists()) { //目录不存在时,不允许自动创建
			boolean exists = FileUtils.isExists(dir);
			//发送文件应当包
			ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new FileSendAck(exists ? savePath : null), validateKey);
			if(!exists) {
				return;
			}
		}
		else if(!FileUtils.isExists(dir)) { //目录不存在
			FileUtils.createDirectory(dir);
		}
		//保存文件
		if(fileSendPacket.getFileLength()>0) {
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(savePath);
				for(;;) {
					FileData fileData = (FileData)ExchangePacketReader.readPacket(exchangeConnection.getInputStream(), validateKey);
					out.write(fileData.getData(), 0, fileData.getData().length);
					if(fileData.isLast()) {
						break;
					}
				}
			}
			finally {
				try {
					out.close();
				}
				catch(Exception e) {
					
				}
			}
		}
		//发送文件发送应答
		ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new FileSendAck(savePath), validateKey);
		//读取新的数据包
		ExchangePacket packet = ExchangePacketReader.readPacket(exchangeConnection.getInputStream(), validateKey);
		if(packet instanceof FileSend) { //下一个文件
			processFileSendPacket((FileSend)packet, exchangeConnection);
		}
	}
	
	/**
	 * 处理文件删除命令
	 * @param command
	 * @param inputStream
	 * @param ouputStream
	 * @throws ExchangeException
	 */
	private void processFileDeletePacket(FileDelete fileDeletePacket) throws Exception {
		if(Logger.isInfoEnabled()) {
			Logger.info("ExchangeClient: delete file " + fileDeletePacket.getRemoteFilePath() + ".");
		}
		File file = new File(fileDeletePacket.getRemoteFilePath());
		if(!file.exists()) { //文件不存在
			return;
		}
		else if(file.isDirectory()) { //目录
			FileUtils.deleteDirectory(fileDeletePacket.getRemoteFilePath());
		}
		else { //文件
			FileUtils.deleteFile(fileDeletePacket.getRemoteFilePath());
		}
	}
	
	/**
	 * 处理文件校验命令
	 * @param command
	 * @param inputStream
	 * @param ouputStream
	 * @return
	 * @throws ExchangeException
	 */
	private void processFileValidatePacket(FileValidate fileValidatePacket, ExchangeConnection exchangeConnection) throws Exception {
		String path = fileValidatePacket.getRemoteFilePath().replace('\\', '/');
		if(!path.startsWith("/") && path.indexOf(':')==-1) {
			path = attachmentBaseDirectory + path;
		}
		if(Logger.isDebugEnabled()) {
			Logger.debug("ExchangeClient: validate " + (fileValidatePacket.isValidateDeleted() ? "deleted " : "") + "file " + path + ".");
		}
		for(;;) {
			boolean result = false;
			if(fileValidatePacket.isValidateDeleted()) { //检查文件是否被删除
				result = !FileUtils.isExists(path);
			}
			else if(FileUtils.isExists(path)) { //检查文件是否更新
				File file = new File(path);
				long length = 0;
				if(file.isFile()) { //单个文件
					length = file.length();
				}
				else { //目录
					File[] files = file.listFiles(new FileFilter() {
						public boolean accept(File pathname) {
							return pathname.isFile();
						}
					});
					for(int j=0; j<(files==null ? 0 : files.length); j++) {
						length += files[j].length();
					}
				}
				result = length==fileValidatePacket.getFileLength();
			}
			ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new FileValidateResult(result), validateKey);
			//读取下一个包,如果是校验完成包,则退出循环
			ExchangePacket exchangePacket = ExchangePacketReader.readPacket(exchangeConnection.getInputStream(), validateKey);
			if(exchangePacket instanceof FileValidateComplete) {
				break;
			}
		}
	}
	
	/**
	 * 数据交换线程,用来接收交换中心转发的数据交换请求
	 * @author linchuan
	 *
	 */
	private class ExchangeThread extends Thread {
		private ExchangeDataSender exchangeDataSender; //发送器

		public ExchangeThread(ExchangeDataSender exchangeDataSender) {
			super();
			this.exchangeDataSender = exchangeDataSender;
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			Thread.currentThread().setName(Thread.currentThread().getName() + "-Exchange-" + name);
			ExchangeConnection exchangeConnection = null;
			while(!stopped) {
				try {
					if(distributionService!=null && !distributionService.isMasterServer(false)) { //当前服务器已经不是主服务器
						while(!distributionService.isMasterServer(true)) {  //每隔60秒检查一次当前服务器是否已经成为主服务器
							Thread.sleep(60000); //等待60秒
						}
					}
					//连接数据交换中心
					if((exchangeConnection=exchangeDataSender.connect())==null) {
						Thread.sleep(30000); //等待30秒再试
						continue;
					}
					//发送登录包
					ExchangePacketWriter.writePacket(exchangeConnection.getOutputStream(), new Login(name), validateKey);
					//等待登录应答应答
					LoginAck loginAck = (LoginAck)ExchangePacketReader.readPacket(exchangeConnection.getInputStream(), validateKey);
					if(loginAck==null) {
						throw new Exception();
					}
					if(Logger.isDebugEnabled()) {
						Logger.debug("ExchangeClient: " + name + " logined.");
					}
					//等待数据发送请求包或者心跳包
					for(;;) {
						final ExchangePacket packet = ExchangePacketReader.readPacket(exchangeConnection.getInputStream(), validateKey);
						if(packet instanceof ExchangeRequest) { //数据发送请求包
							new Thread() { //创建新的线程数量数据发送请求包
								public void run() {
									ExchangeRequest request = (ExchangeRequest)packet;
									Thread.currentThread().setName(Thread.currentThread().getName() + "-Exchange-" + name + "-" + request.getSenderName());
									try {
										processExchangeRequest(exchangeDataSender, request);
									}
									catch(Exception e) {
										Logger.exception(e);
									}
								}
							}.start();
						}
					}
				}
				catch(Exception e) {
					Logger.exception(e);
					try {
						exchangeConnection.close(); //关闭连接,以重新开始下一次连接
					}
					catch(Exception ex) {
						
					}
					try {
						Thread.sleep(30000); //等待30秒
					} 
					catch (InterruptedException ex) {
						
					}
				}
			}
		}
	}

	/**
	 * 发送器回调
	 * @author linchuan
	 *
	 */
	private interface ExchangeCallback {
		//发送数据
		public void send(ExchangeDataSender exchangeDataSender, ExchangeConnection exchangeConnection) throws Exception;
	}
	
	/**
	 * 交换报告
	 * @author linchuan
	 *
	 */
	public class ExchangeReport {
		private boolean success; //是否成功
		private Exception exception; //异常
		
		/**
		 * @return the exception
		 */
		public Exception getException() {
			return exception;
		}
		/**
		 * @param exception the exception to set
		 */
		public void setException(Exception exception) {
			this.exception = exception;
		}
		/**
		 * @return the success
		 */
		public boolean isSuccess() {
			return success;
		}
		/**
		 * @param success the success to set
		 */
		public void setSuccess(boolean success) {
			this.success = success;
		}
	}
	
	/**
	 * @return the exchangeDataSenders
	 */
	public List getExchangeDataSenders() {
		return exchangeDataSenders;
	}

	/**
	 * @param exchangeDataSenders the exchangeDataSenders to set
	 */
	public void setExchangeDataSenders(List exchangeDataSenders) {
		this.exchangeDataSenders = exchangeDataSenders;
	}

	/**
	 * @return the businessDefineService
	 */
	public BusinessDefineService getBusinessDefineService() {
		return businessDefineService;
	}

	/**
	 * @param businessDefineService the businessDefineService to set
	 */
	public void setBusinessDefineService(BusinessDefineService businessDefineService) {
		this.businessDefineService = businessDefineService;
	}

	/**
	 * @return the attachmentBaseDirectory
	 */
	public String getAttachmentBaseDirectory() {
		return attachmentBaseDirectory;
	}

	/**
	 * @param attachmentBaseDirectory the attachmentBaseDirectory to set
	 */
	public void setAttachmentBaseDirectory(String attachmentBaseDirectory) {
		attachmentBaseDirectory = attachmentBaseDirectory.replaceAll("\\x5c", "/");
		if(!attachmentBaseDirectory.endsWith("/")) {
			attachmentBaseDirectory += "/";
		}
		this.attachmentBaseDirectory = attachmentBaseDirectory;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the permissibleHqls
	 */
	public Map getPermissibleHqls() {
		return permissibleHqls;
	}

	/**
	 * @param permissibleHqls the permissibleHqls to set
	 */
	public void setPermissibleHqls(Map permissibleHqls) {
		this.permissibleHqls = permissibleHqls;
	}

	/**
	 * @return the permissibleSqls
	 */
	public Map getPermissibleSqls() {
		return permissibleSqls;
	}

	/**
	 * @param permissibleSqls the permissibleSqls to set
	 */
	public void setPermissibleSqls(Map permissibleSqls) {
		this.permissibleSqls = permissibleSqls;
	}

	/**
	 * @return the temporaryFileManageService
	 */
	public TemporaryFileManageService getTemporaryFileManageService() {
		return temporaryFileManageService;
	}

	/**
	 * @param temporaryFileManageService the temporaryFileManageService to set
	 */
	public void setTemporaryFileManageService(
			TemporaryFileManageService temporaryFileManageService) {
		this.temporaryFileManageService = temporaryFileManageService;
	}

	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	/**
	 * @return the maxExchangeRequest
	 */
	public int getMaxExchangeRequest() {
		return maxExchangeRequest;
	}

	/**
	 * @param maxExchangeRequest the maxExchangeRequest to set
	 */
	public void setMaxExchangeRequest(int maxExchangeRequest) {
		this.maxExchangeRequest = maxExchangeRequest;
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

	/**
	 * @return the receiveOnly
	 */
	public boolean isReceiveOnly() {
		return receiveOnly;
	}

	/**
	 * @param receiveOnly the receiveOnly to set
	 */
	public void setReceiveOnly(boolean receiveOnly) {
		this.receiveOnly = receiveOnly;
	}

	/**
	 * @return the sendRecordEnabled
	 */
	public boolean isSendRecordEnabled() {
		return sendRecordEnabled;
	}

	/**
	 * @param sendRecordEnabled the sendRecordEnabled to set
	 */
	public void setSendRecordEnabled(boolean sendRecordEnabled) {
		this.sendRecordEnabled = sendRecordEnabled;
	}
}