package com.yuanluesoft.jeaf.taglib;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.taglib.logic.IterateTag;

import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public class IterateAttachmentTag extends IterateTag {
	private AttachmentService attachmentService = null;
	
	//从服务中获取图片模型
	private String applicationName; //应用名称
	private String attachmentType; //图片类型
	private long recordId; //记录ID
	
	private String attachmentsId;
	private String sizeId; //输出记录数
	
	//动态记录ID
	private String nameRecordId;
	private String propertyRecordId;
	private String scopeRecordId;
	
	//动态应用名称
	private String nameApplicationName;
	private String propertyApplicationName;
	private String scopeApplicationName;

	//动态图像类型
	private String nameAttachmentType;
	private String propertyAttachmentType;
	private String scopeAttachmentType;
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.struts.taglib.logic.IterateTag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		if(nameApplicationName!=null || propertyApplicationName!=null) { //动态获取应用名称
			if(nameApplicationName==null) {
				nameApplicationName = Constants.BEAN_KEY;
			}
			applicationName = (String)TagUtils.getInstance().lookup(pageContext, nameApplicationName, propertyApplicationName, scopeApplicationName); 
		}
		if(nameAttachmentType!=null || propertyAttachmentType!=null) { //动态获取附件类型
			if(nameAttachmentType==null) {
				nameAttachmentType = Constants.BEAN_KEY;
			}
			attachmentType = (String)TagUtils.getInstance().lookup(pageContext, nameAttachmentType, propertyAttachmentType, scopeAttachmentType); 
		}
		if(attachmentType==null || attachmentType.equals("")) {
			attachmentType = "attachment";
		}
		if(nameRecordId!=null || propertyRecordId!=null) {
			if(nameRecordId==null) {
				nameRecordId = Constants.BEAN_KEY;
			}
			recordId = ((Long)TagUtils.getInstance().lookup(pageContext, nameRecordId, propertyRecordId, scopeRecordId)).longValue();
		}
		try {
			attachmentService = FieldUtils.getAttachmentService(applicationName, attachmentType, recordId);
			collection = attachmentService==null ? null : attachmentService.list(applicationName, attachmentType, recordId, true, 0, (HttpServletRequest)pageContext.getRequest());
			if(collection==null) {
				collection = new ArrayList();
			}
		}
		catch (ServiceException e) {
			return SKIP_BODY;
		}
		if(attachmentsId!=null && !attachmentsId.equals("")) {
			pageContext.setAttribute(attachmentsId, collection);
		}
		if(sizeId!=null && !sizeId.equals("")) {
			pageContext.setAttribute(sizeId, new Integer(((List)collection).size()));
		}
		return super.doStartTag();
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.taglib.logic.IterateTag#release()
	 */
	public void release() {
		applicationName = null; //应用名称
		attachmentType = null; //图片类型
		recordId = 0; //记录ID
		nameRecordId = null; //动态记录ID
		propertyRecordId = null;
		scopeRecordId = null;
		collection = null;
		attachmentsId = null;
		sizeId = null; //输出记录数
		
		attachmentService = null;
		
		//动态应用名称
		nameApplicationName = null;
		propertyApplicationName = null;
		scopeApplicationName = null;

		//动态图像类型
		nameAttachmentType = null;
		propertyAttachmentType = null;
		scopeAttachmentType = null;
		super.release();
	}

	/**
	 * @return the attachmentType
	 */
	public String getAttachmentType() {
		return attachmentType;
	}

	/**
	 * @param attachmentType the attachmentType to set
	 */
	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
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
	 * @return the attachmentService
	 */
	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	/**
	 * @param attachmentService the attachmentService to set
	 */
	public void setAttachmentService(
			AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	/**
	 * @return the nameApplicationName
	 */
	public String getNameApplicationName() {
		return nameApplicationName;
	}

	/**
	 * @param nameApplicationName the nameApplicationName to set
	 */
	public void setNameApplicationName(String nameApplicationName) {
		this.nameApplicationName = nameApplicationName;
	}

	/**
	 * @return the nameAttachmentType
	 */
	public String getNameAttachmentType() {
		return nameAttachmentType;
	}

	/**
	 * @param nameAttachmentType the nameAttachmentType to set
	 */
	public void setNameAttachmentType(String nameAttachmentType) {
		this.nameAttachmentType = nameAttachmentType;
	}

	/**
	 * @return the propertyApplicationName
	 */
	public String getPropertyApplicationName() {
		return propertyApplicationName;
	}

	/**
	 * @param propertyApplicationName the propertyApplicationName to set
	 */
	public void setPropertyApplicationName(String propertyApplicationName) {
		this.propertyApplicationName = propertyApplicationName;
	}

	/**
	 * @return the propertyAttachmentType
	 */
	public String getPropertyAttachmentType() {
		return propertyAttachmentType;
	}

	/**
	 * @param propertyAttachmentType the propertyAttachmentType to set
	 */
	public void setPropertyAttachmentType(String propertyAttachmentType) {
		this.propertyAttachmentType = propertyAttachmentType;
	}

	/**
	 * @return the scopeApplicationName
	 */
	public String getScopeApplicationName() {
		return scopeApplicationName;
	}

	/**
	 * @param scopeApplicationName the scopeApplicationName to set
	 */
	public void setScopeApplicationName(String scopeApplicationName) {
		this.scopeApplicationName = scopeApplicationName;
	}

	/**
	 * @return the scopeAttachmentType
	 */
	public String getScopeAttachmentType() {
		return scopeAttachmentType;
	}

	/**
	 * @param scopeAttachmentType the scopeAttachmentType to set
	 */
	public void setScopeAttachmentType(String scopeAttachmentType) {
		this.scopeAttachmentType = scopeAttachmentType;
	}

	/**
	 * @return the attachmentsId
	 */
	public String getAttachmentsId() {
		return attachmentsId;
	}

	/**
	 * @param attachmentsId the attachmentsId to set
	 */
	public void setAttachmentsId(String attachmentsId) {
		this.attachmentsId = attachmentsId;
	}

	/**
	 * @return the sizeId
	 */
	public String getSizeId() {
		return sizeId;
	}

	/**
	 * @param sizeId the sizeId to set
	 */
	public void setSizeId(String sizeId) {
		this.sizeId = sizeId;
	}

}