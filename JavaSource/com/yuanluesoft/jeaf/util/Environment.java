/*
 * Created on 2004-8-8
 *
 */
package com.yuanluesoft.jeaf.util;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;

import com.yuanluesoft.jeaf.database.dialect.DatabaseDialect;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;

/**
 * 
 * @author linchuan
 *
 */
public class Environment {
	private static final String pathWebApp; //web-inf目录的绝对路径
	private static String contextPath = null; //web应用上下文目录
	private static String webApplicationUrl = null; //web应用URL
	private static String webApplicationSafeUrl = null; //web应用安全URL
	private static String webApplicationLocalUrl = null; //web应用本地URL
	private static WebApplicationContext webApplicationContext;
	private static DatabaseDialect databaseDialect; //数据库方言
	private static ServletContext servletContext; //SERVLET上下文
	
	static {
		try {
			//初始化
			String path = Environment.class.getResource("").getPath();
			int index = path.toUpperCase().lastIndexOf("WEB-INF/");
			if(index==-1) {
				pathWebApp = null;
			}
			else {
				pathWebApp = path.substring(0, index).replaceAll("%20", " ");
			}
		}
		catch (Exception ex) {
			throw new RuntimeException("Environment init error: " + ex.getMessage(), ex);
		}
	}
	/**
	 * 获取WEB-INF路径,如: d:/cms/WEB-INF/
	 */
	public static String getWebinfPath() {
		return pathWebApp + "WEB-INF/";
	}
	
	/**
	 * 获取WEB应用路径,如: d:/cms/
	 */
	public static String getWebAppPath() {
		return pathWebApp;
	}
    /**
     * @return Returns the contextPath.
     */
    public static String getContextPath() {
        return contextPath;
    }
    /**
     * @param contextPath The contextPath to set.
     */
    public static void setContextPath(String contextPath) {
        Environment.contextPath = contextPath;
    }
	/**
	 * @return the webApplicationSafeUrl
	 */
	public static String getWebApplicationSafeUrl() {
		return webApplicationSafeUrl;
	}
	/**
	 * @param webApplicationSafeUrl the webApplicationSafeUrl to set
	 */
	public static void setWebApplicationSafeUrl(String webApplicationSafeUrl) {
		Environment.webApplicationSafeUrl = webApplicationSafeUrl;
	}
	/**
	 * @return the webApplicationUrl
	 */
	public static String getWebApplicationUrl() {
		return webApplicationUrl;
	}
	/**
	 * @param webApplicationUrl the webApplicationUrl to set
	 */
	public static void setWebApplicationUrl(String webApplicationUrl) {
		Environment.webApplicationUrl = webApplicationUrl;
	}
	/**
	 * @return the webApplicationLocalUrl
	 */
	public static String getWebApplicationLocalUrl() {
		return webApplicationLocalUrl;
	}
	/**
	 * @param webApplicationLocalUrl the webApplicationLocalUrl to set
	 */
	public static void setWebApplicationLocalUrl(String webApplicationLocalUrl) {
		Environment.webApplicationLocalUrl = webApplicationLocalUrl;
	}
	
	 /**
     * 获取服务
     * @param serviceName
     * @return
     * @throws ServiceException
     */
    public static Object getService(String serviceName) throws ServiceException {
		try {
			return webApplicationContext.getBean(serviceName);
		}
		catch(Exception e) {
			Logger.info("zyh"+e);
			Logger.error("get service " + serviceName + " failed.");
			throw new ServiceException();
		}
    }
    
    /**
     * 设置web应用上下文
     * @param webApplicationContext
     */
    public static void setWebApplicationContext(WebApplicationContext webApplicationContext) {
    	Environment.webApplicationContext = webApplicationContext;
    }
	/**
	 * @return the databaseDialect
	 */
	public static DatabaseDialect getDatabaseDialect() {
		return databaseDialect;
	}
	/**
	 * @param databaseDialect the databaseDialect to set
	 */
	public static void setDatabaseDialect(DatabaseDialect databaseDialect) {
		Environment.databaseDialect = databaseDialect;
	}

	/**
	 * @return the servletContext
	 */
	public static ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * @param servletContext the servletContext to set
	 */
	public static void setServletContext(ServletContext servletContext) {
		Environment.servletContext = servletContext;
	}
}