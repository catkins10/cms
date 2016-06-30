package com.yuanluesoft.jeaf.taglib;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.image.service.ImageService;

/**
 * 
 * @author linchuan
 *
 */
public class EmptyImageTag extends BodyTagSupport {
	private ImageService imageService = null;
	//从服务中获取图片模型
	private String applicationName; //应用名称
	private String imageType; //图片类型
	private long recordId; //记录ID
	private String nameRecordId; //动态记录ID
	private String propertyRecordId;
	private String scopeRecordId;
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
    public int doStartTag() throws JspException {
        if (condition())
            return (EVAL_BODY_INCLUDE);
        else
            return (SKIP_BODY);
    }
    
    /* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
	 */
	public void release() {
		applicationName = null; //应用名称
		imageType = null; //图片类型
		recordId = 0; //记录ID
		nameRecordId = null; //动态记录ID
		propertyRecordId = null;
		scopeRecordId = null;
		super.release();
	}

	/**
     * 
     * @return
     * @throws JspException
     */
	protected boolean condition() throws JspException {
		if(imageService==null) {
			WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext());
			imageService = (ImageService)webApplicationContext.getBean("imageService");
		}
		if(nameRecordId!=null || propertyRecordId!=null) {
			if(nameRecordId==null) {
				nameRecordId = Constants.BEAN_KEY;
			}
			recordId = ((Long)TagUtils.getInstance().lookup(pageContext, nameRecordId, propertyRecordId, scopeRecordId)).longValue();
		}
		try {
			List images = imageService.list(applicationName, imageType, recordId, false, 1, null);
			return images==null || images.isEmpty();
		} catch (ServiceException e) {
			return true;
		}
	}

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/**
	 * @return the imageType
	 */
	public String getImageType() {
		return imageType;
	}

	/**
	 * @param imageType the imageType to set
	 */
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	/**
	 * @return the recordId
	 */
	public long getRecordId() {
		return recordId;
	}

	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	/**
	 * @return the recordIdName
	 */
	public String getNameRecordId() {
		return nameRecordId;
	}

	/**
	 * @param recordIdName the recordIdName to set
	 */
	public void setNameRecordId(String recordIdName) {
		this.nameRecordId = recordIdName;
	}

	/**
	 * @return the recordIdProperty
	 */
	public String getPropertyRecordId() {
		return propertyRecordId;
	}

	/**
	 * @param recordIdProperty the recordIdProperty to set
	 */
	public void setPropertyRecordId(String recordIdProperty) {
		this.propertyRecordId = recordIdProperty;
	}

	/**
	 * @return the recordIdScope
	 */
	public String getScopeRecordId() {
		return scopeRecordId;
	}

	/**
	 * @param recordIdScope the recordIdScope to set
	 */
	public void setScopeRecordId(String recordIdScope) {
		this.scopeRecordId = recordIdScope;
	}
}
