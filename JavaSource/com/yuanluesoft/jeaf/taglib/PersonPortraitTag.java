package com.yuanluesoft.jeaf.taglib;

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;

/**
 * 
 * @author linchuan
 *
 */
public class PersonPortraitTag extends ImgTag {
	private PersonService personService = null;
	
	private long personId; //用户ID
	private String namePersonId; //动态用户ID
	private String propertyPersonId;
	private String scopePersonId;
	
	/* (non-Javadoc)
	 * @see org.apache.struts.taglib.html.ImgTag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		if(personService==null) {
			WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext());
			personService = (PersonService)webApplicationContext.getBean("personService");
		}
		if(namePersonId!=null || propertyPersonId!=null) {
			if(namePersonId==null) {
				namePersonId = Constants.BEAN_KEY;
			}
			personId = ((Long)TagUtils.getInstance().lookup(pageContext, namePersonId, propertyPersonId, scopePersonId)).longValue();
		}
		if(personId<=0) {
			return SKIP_BODY;
		}
		try {
			imageModel = personService.getPersonPortrait(personId, pageContext.getRequest().isSecure());
		}
		catch (ServiceException e) {
			Logger.exception(e);
			return SKIP_BODY;
		}
		if(imageModel==null) {
			return SKIP_BODY;
		}
		return super.doStartTag();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.taglib.ImgTag#release()
	 */
	public void release() {
		personId = 0; //用户ID
		namePersonId = null; //动态用户ID
		propertyPersonId = null;
		scopePersonId = null;
		super.release();
	}

	/**
	 * @return the personId
	 */
	public long getPersonId() {
		return personId;
	}

	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(long personId) {
		this.personId = personId;
	}

	/**
	 * @return the namePersonId
	 */
	public String getNamePersonId() {
		return namePersonId;
	}

	/**
	 * @param namePersonId the namePersonId to set
	 */
	public void setNamePersonId(String namePersonId) {
		this.namePersonId = namePersonId;
	}

	/**
	 * @return the propertyPersonId
	 */
	public String getPropertyPersonId() {
		return propertyPersonId;
	}

	/**
	 * @param propertyPersonId the propertyPersonId to set
	 */
	public void setPropertyPersonId(String propertyPersonId) {
		this.propertyPersonId = propertyPersonId;
	}

	/**
	 * @return the scopePersonId
	 */
	public String getScopePersonId() {
		return scopePersonId;
	}

	/**
	 * @param scopePersonId the scopePersonId to set
	 */
	public void setScopePersonId(String scopePersonId) {
		this.scopePersonId = scopePersonId;
	}
	
}
