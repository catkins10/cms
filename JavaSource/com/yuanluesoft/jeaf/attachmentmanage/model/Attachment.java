/*
 * Created on 2005-9-8
 *
 */
package com.yuanluesoft.jeaf.attachmentmanage.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 * 
 */
public class Attachment implements Serializable {
	private String filePath; //路径
	private String name; //附件名称
	private long size; //附件大小
	private long lastModified; //文件修改时间
	private String urlAttachment; //下载附件的URL
	private String urlInline; //下载附件的URL
	private String applicationName; //应用名称
	private long recordId; //记录ID
	private String type; //附件类型
	private String iconURL; //图标URL
	private AttachmentService service; //附件服务
	
	/**
	 * 文件最后修改时间
	 * @return
	 */
	public Timestamp getLastModifiedTime() {
		return new Timestamp(lastModified);
	}
	
	/**
	 * 获取文本格式的文件大小
	 * @return
	 */
	public String getFileSize() {
		return StringUtils.getFileSize(size);
	}
	
	/**
	 * 获取附件标题,名称+大小
	 * @return
	 */
	public String getTitle() {
		return name + "(" + getFileSize() + ")";
	}

	/**
	 * 获取附件名称,不带扩展名
	 * @return
	 */
	public String getShortName() {
		int index = name.lastIndexOf('.');
	    return index==-1 ? name : name.substring(0, index);
	}

	/**
	 * 获取描述
	 * @return
	 */
	public String getDescription() {
		return name + "\r\n大小：" + getFileSize();
	}
	
	/**
	 * @return Returns the filePath.
	 */
	public String getFilePath() {
		return filePath;
	}
	
	/**
	 * @param filePath The filePath to set.
	 */
	public void setFilePath(String filePath) {
		if(filePath!=null) {
			filePath = filePath.replace('\\', '/');
		}
		if(name==null && filePath!=null) {
			name = filePath.substring(filePath.lastIndexOf('/') + 1);
		}
		this.filePath = filePath;
	}
	
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = FileUtils.retrieveFileName(name);
	}
	
	/**
	 * @return Returns the size.
	 */
	public long getSize() {
		return size;
	}
	/**
	 * @param size The size to set.
	 */
	public void setSize(long size) {
		this.size = size;
	}
    /**
     * @return Returns the urlAttachment.
     */
    public String getUrlAttachment() {
        return urlAttachment;
    }
    /**
     * @param urlAttachment The urlAttachment to set.
     */
    public void setUrlAttachment(String urlAttachment) {
        this.urlAttachment = urlAttachment;
    }
    /**
     * @return Returns the urlInline.
     */
    public String getUrlInline() {
        return urlInline;
    }
    /**
     * @param urlInline The urlInline to set.
     */
    public void setUrlInline(String urlInline) {
        this.urlInline = urlInline;
    }

	/**
	 * @return the lastModified
	 */
	public long getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the service
	 */
	public AttachmentService getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(AttachmentService service) {
		this.service = service;
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
	 * @return the iconURL
	 */
	public String getIconURL() {
		return iconURL;
	}

	/**
	 * @param iconURL the iconURL to set
	 */
	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}
}
