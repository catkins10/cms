package com.yuanluesoft.cms.pagebuilder.util;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class PageUtils {
	
	/**
	 * 设置页面的有效时间
	 * @param expiresTime
	 * @param request
	 */
	public static void setPageExpiresTime(Date expiresTime, HttpServletRequest request) {
		if(expiresTime==null || !expiresTime.after(DateTimeUtils.now())) {
			return;
		}
		Timestamp savedExpiresTime = (Timestamp)request.getAttribute("expiresTime");
		if(savedExpiresTime==null || savedExpiresTime.after(expiresTime)) {
			request.setAttribute("expiresTime", (expiresTime instanceof Timestamp ? expiresTime : new Timestamp(expiresTime.getTime())));
		}
	}
}