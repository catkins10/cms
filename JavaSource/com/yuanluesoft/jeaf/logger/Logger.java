/*
 * Created on 2004-8-12
 *
 */
package com.yuanluesoft.jeaf.logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.xml.DOMConfigurator;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.logger.pojo.ActionLog;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class Logger {
	static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("com.yuanluesoft");
	static {
		try {
			if(new File(Environment.getWebinfPath() + "log4j.xml").exists()) {
				DOMConfigurator.configure(Environment.getWebinfPath() + "log4j.xml");
			}
			else {
				logger = org.apache.log4j.Logger.getRootLogger();
			}
		} 
		catch (Exception ex) {
			throw new RuntimeException("Logger init error: " + ex.getMessage(), ex);
		}
	}
	
	/**
	 * debug是否有效
	 * @return
	 */
	public static boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}
	
	/**
	 * info是否有效
	 * @return
	 */
	public static boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}
	
	/**
	 * trace是否有效
	 * @return
	 */
	public static boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}
	
	/**
	 * 
	 * @param obj
	 */
	public static void trace(Object obj) {
		logger.trace(obj);
	}
	
	/**
	 * 
	 * @param obj
	 */
	public static void debug(Object obj) {
		logger.debug(obj);
	}
	/**
	 * 
	 * @param obj
	 */
	public static void info(Object obj)	{
		logger.info(obj);
	}
	/**
	 * 
	 * @param obj
	 */
	public static void warn(Object obj)	{
		logger.warn(obj);
	}
	/**
	 * 
	 * @param obj
	 */
	public static void error(Object obj) {
		logger.error(obj);
	}
	/**
	 * 
	 * @param obj
	 */
	public static void fatal(Object obj) {
		logger.fatal(obj);
	}
	/**
	 * 
	 * @param e
	 */
	public static void exception(Exception e) {
		ByteArrayOutputStream out = null;
		PrintStream ps = null;
		try {
			out = new java.io.ByteArrayOutputStream();
			ps = new PrintStream(out, true, "UTF-8");
			e.printStackTrace(ps);
			logger.error(out.toString("UTF-8"));
		}
		catch(Exception ex){
			
		}
		finally {
			try {
				out.close();
			}
			catch(Exception ex) {
				
			}
			try {
				ps.close();
			}
			catch(Exception ex) {
				
			}
		}
	}
	/**
	 * 操作日志
	 * @param applicationName
	 * @param actionClass
	 * @param pojo
	 * @param recordId
	 * @param recordContent
	 * @param personId
	 * @param personName
	 * @param request
	 * @throws Exception
	 */
	public static void action(String applicationName, Class actionClass, String actionType, Object pojo, long recordId, String recordContent, long personId, String personName, HttpServletRequest request) throws Exception {
		if(personId<=0) { //匿名用户不做日志
			return;
		}
		ActionLog actionLog = new ActionLog();
		actionLog.setId(UUIDLongGenerator.generateId());
		actionLog.setApplicationName(applicationName);
		if(pojo!=null) {
			actionLog.setRecordType(pojo.getClass().getName());
		}
		actionLog.setRecordId(recordId);
		actionLog.setContent(recordContent);
		actionLog.setPersonId(personId);
		if(personName!=null && personName.length()>30) {
			personName = personName.substring(0, 30);
		}
		actionLog.setPersonName(personName);
		actionLog.setActionTime(DateTimeUtils.now());
		actionLog.setActionName(actionClass.getName());
		actionLog.setActionType(actionType);
		actionLog.setIp(RequestUtils.getRemoteAddress(request) + ":" + RequestUtils.getRemotePort(request));
		((DatabaseService)Environment.getService("databaseService")).saveRecord(actionLog);
		info("Action: application name is " + applicationName + "," +
			 " action class name is " + actionClass.getName() + "," +
			 " record class name is " + actionLog.getRecordType() + "," +
			 " action type is " + actionType + "," +
			 " record id is " + recordId + "," +
			 " record content is " + recordContent + "," +
			 " person id is " + personId + "," +
			 " person name is " + personName + "," +
			 " ip is " + actionLog.getIp());
	}
}