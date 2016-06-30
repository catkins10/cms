package com.yuanluesoft.jeaf.usermanage.util;

import com.yuanluesoft.jeaf.util.Environment;

/**
 * 
 * @author linchuan
 *
 */
public class PersonUtils {
	
	/**
	 * 获取用户头像URL
	 * @param personId
	 * @return
	 */
	public static String getPortraitURL(long personId) {
		return Environment.getContextPath() + "/jeaf/usermanage/portrait.shtml?personId=" + personId;
	}
}