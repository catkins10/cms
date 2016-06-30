package com.yuanluesoft.jeaf.taglib;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.taglib.logic.IterateTag;

import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.video.model.Video;
import com.yuanluesoft.jeaf.video.service.VideoService;

/**
 * 视频遍历
 * @author linchuan
 *
 */
public class IterateVideoTag extends IterateTag {
	private VideoService videoService = null;
	
	private String applicationName; //应用名称
	private String videoType; //视频类型
	private long recordId; //记录ID
	
	//动态应用名称
	private String nameApplicationName;
	private String propertyApplicationName;
	private String scopeApplicationName;

	//动态视频类型
	private String nameVideoType;
	private String propertyVideoType;
	private String scopeVideoType;
	
	//缩略图属性
	private String breviaryId;
	private int breviaryWidth;
	private int breviaryHeight;

	//动态记录ID
	private String nameRecordId;
	private String propertyRecordId;
	private String scopeRecordId;
	
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
		if(nameVideoType!=null || propertyVideoType!=null) { //动态获取视频类型
			if(nameVideoType==null) {
				nameVideoType = Constants.BEAN_KEY;
			}
			videoType = (String)TagUtils.getInstance().lookup(pageContext, nameVideoType, propertyVideoType, scopeVideoType); 
		}
		if(videoType==null || videoType.equals("")) {
			videoType = "video";
		}
		if(nameRecordId!=null || propertyRecordId!=null) { //动态获取记录ID
			if(nameRecordId==null) {
				nameRecordId = Constants.BEAN_KEY;
			}
			recordId = ((Long)TagUtils.getInstance().lookup(pageContext, nameRecordId, propertyRecordId, scopeRecordId)).longValue();
		}
		try {
			videoService = (VideoService)FieldUtils.getAttachmentService(applicationName, videoType, recordId);
			collection = videoService==null ? null : videoService.list(applicationName, videoType, recordId, true, 0, (HttpServletRequest)pageContext.getRequest());
		}
		catch (ServiceException e) {
			e.printStackTrace();
			return SKIP_BODY;
		}
        if(collection == null) {
        	 return (SKIP_BODY);
        }

        // Construct an iterator for this collection
        iterator = ((List)collection).iterator();
        
        // Calculate the starting offset
        if (offset == null) {
        	offsetValue = 0;
        }
        else {
        	try {
        		offsetValue = Integer.parseInt(offset);
        	}
        	catch (NumberFormatException e) {
        		Integer offsetObject =
        			(Integer) TagUtils.getInstance().lookup(pageContext, offset, null);

        		if (offsetObject == null) {
        			offsetValue = 0;
        		} else {
        			offsetValue = offsetObject.intValue();
        		}
        	}
        }

        if (offsetValue < 0) {
            offsetValue = 0;
        }

        // Calculate the rendering length
        if (length == null) {
            lengthValue = 0;
        } else {
            try {
                lengthValue = Integer.parseInt(length);
            } catch (NumberFormatException e) {
                Integer lengthObject = (Integer) TagUtils.getInstance().lookup(pageContext, length, null);

                if (lengthObject == null) {
                    lengthValue = 0;
                } else {
                    lengthValue = lengthObject.intValue();
                }
            }
        }
        if (lengthValue < 0) {
            lengthValue = 0;
        }
        lengthCount = 0;

        // Skip the leading elements up to the starting offset
        for (int i = 0; i < offsetValue; i++) {
            if (iterator.hasNext()) {
                iterator.next();
            }
        }

        // Store the first value and evaluate, or skip the body if none
        if (iterator.hasNext()) {
            Object element = iterator.next();

            if(element == null) {
            	if(id!=null) {
            		pageContext.removeAttribute(id);
            	}
                if(breviaryId!=null) {
                	pageContext.removeAttribute(breviaryId);
                }
            }
            else {
            	if(id!=null) {
            		pageContext.setAttribute(id, element);
            	}
                if(breviaryId!=null) { //生成缩略图
                	try {
						pageContext.setAttribute(breviaryId, videoService.getBreviaryImage(applicationName, videoType, recordId, ((Video)element).getName(), breviaryWidth, breviaryHeight, (HttpServletRequest)pageContext.getRequest()));
					}
                	catch (ServiceException e) {
						Logger.exception(e);
					}
                }
            }
            lengthCount++;
            started = true;

            if (indexId != null) {
                pageContext.setAttribute(indexId, new Integer(getIndex()));
            }
            return (EVAL_BODY_AGAIN);
        }
        else {
            return (SKIP_BODY);
        }
	}
	
	/* (non-Javadoc)
	 * @see org.apache.struts.taglib.logic.IterateTag#doAfterBody()
	 */
	public int doAfterBody() throws JspException {
//		 Render the output from this iteration to the output stream
        if (bodyContent != null) {
            TagUtils.getInstance().writePrevious(pageContext, bodyContent.getString());
            bodyContent.clearBody();
        }

        // Decide whether to iterate or quit
        if ((lengthValue > 0) && (lengthCount >= lengthValue)) {
            return (SKIP_BODY);
        }

        if (iterator.hasNext()) {
            Object element = iterator.next();
            if(element == null) {
            	if(id!=null) {
            		pageContext.removeAttribute(id);
            	}
                if(breviaryId!=null) {
                	pageContext.removeAttribute(breviaryId);
                }
            }
            else {
            	if(id!=null) {
            		pageContext.setAttribute(id, element);
            	}
                if(breviaryId!=null) { //生成缩略图
                	try {
						pageContext.setAttribute(breviaryId, videoService.getBreviaryImage(applicationName, videoType, recordId, ((Video)element).getName(), breviaryWidth, breviaryHeight, (HttpServletRequest)pageContext.getRequest()));
					}
                	catch (ServiceException e) {
						Logger.exception(e);
					}
                }
            }
            lengthCount++;

            if (indexId != null) {
                pageContext.setAttribute(indexId, new Integer(getIndex()));
            }

            return (EVAL_BODY_AGAIN);
        }
        else {
            return (SKIP_BODY);
        }
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.taglib.logic.IterateTag#release()
	 */
	public void release() {
		applicationName = null; //应用名称
		videoType = null; //视频类型
		recordId = 0; //记录ID
		//动态应用名称
		nameApplicationName = null;
		propertyApplicationName = null;
		scopeApplicationName = null;
		//动态视频类型
		nameVideoType = null;
		propertyVideoType = null;
		scopeVideoType = null;
		nameRecordId = null; //动态记录ID
		propertyRecordId = null;
		scopeRecordId = null;
		videoService = null;
		collection = null;
		//缩略图属性
		breviaryId = null;
		breviaryWidth = 0;
		breviaryHeight = 0;
		super.release();
	}

	/**
	 * @return the videoType
	 */
	public String getVideoType() {
		return videoType;
	}
	/**
	 * @param videoType the videoType to set
	 */
	public void setVideoType(String videoType) {
		this.videoType = videoType;
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
	 * @return the nameVideoType
	 */
	public String getNameVideoType() {
		return nameVideoType;
	}

	/**
	 * @param nameVideoType the nameVideoType to set
	 */
	public void setNameVideoType(String nameVideoType) {
		this.nameVideoType = nameVideoType;
	}

	/**
	 * @return the propertyVideoType
	 */
	public String getPropertyVideoType() {
		return propertyVideoType;
	}

	/**
	 * @param propertyVideoType the propertyVideoType to set
	 */
	public void setPropertyVideoType(String propertyVideoType) {
		this.propertyVideoType = propertyVideoType;
	}

	/**
	 * @return the scopeVideoType
	 */
	public String getScopeVideoType() {
		return scopeVideoType;
	}

	/**
	 * @param scopeVideoType the scopeVideoType to set
	 */
	public void setScopeVideoType(String scopeVideoType) {
		this.scopeVideoType = scopeVideoType;
	}

	/**
	 * @return the breviaryHeight
	 */
	public int getBreviaryHeight() {
		return breviaryHeight;
	}

	/**
	 * @param breviaryHeight the breviaryHeight to set
	 */
	public void setBreviaryHeight(int breviaryHeight) {
		this.breviaryHeight = breviaryHeight;
	}
	/**
	 * @return the breviaryWidth
	 */
	public int getBreviaryWidth() {
		return breviaryWidth;
	}

	/**
	 * @param breviaryWidth the breviaryWidth to set
	 */
	public void setBreviaryWidth(int breviaryWidth) {
		this.breviaryWidth = breviaryWidth;
	}

	/**
	 * @return the breviaryId
	 */
	public String getBreviaryId() {
		return breviaryId;
	}

	/**
	 * @param breviaryId the breviaryId to set
	 */
	public void setBreviaryId(String breviaryId) {
		this.breviaryId = breviaryId;
	}
}
