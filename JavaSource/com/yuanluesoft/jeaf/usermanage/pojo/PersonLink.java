package com.yuanluesoft.jeaf.usermanage.pojo;

import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 用户:引用(user_person_link)
 * @author linchuan
 *
 */
public class PersonLink extends Person {
	private String userClassName; //用户类名称

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.pojo.Person#getUrl()
	 */
	public String getUrl() {
		BusinessObject businessObject = null;
		try {
			businessObject = ((BusinessDefineService)Environment.getService("businessDefineService")).getBusinessObject(userClassName);
		}
		catch(ServiceException e) {
			
		}
		return StringUtils.fillParameters("" + businessObject.getExtendParameter("recordUrl"), true, false, false, "utf-8", this, null, null);
	}

	/**
	 * @return the userClassName
	 */
	public String getUserClassName() {
		return userClassName;
	}

	/**
	 * @param userClassName the userClassName to set
	 */
	public void setUserClassName(String userClassName) {
		this.userClassName = userClassName;
	}
}