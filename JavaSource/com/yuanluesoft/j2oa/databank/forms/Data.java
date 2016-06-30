/*
 * Created on 2006-4-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.j2oa.databank.forms;

import java.sql.Date;
import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.security.model.RecordVisitorList;

/**
 *
 * @author linchuan
 *
 */
public class Data extends ActionForm {
	private String subject; //标题
	private String docmark; //文件字号
	private String dataType; //文件类型
	private String secureLevel; //密级
	private String fromUnit; //成文单位
	private Date generateDate; //成文日期
	private long creatorId; //创建人ID
	private String creator; //创建人
	private Timestamp created; //登记时间
	private long directoryId; //目录ID
	private String remark; //附注
	private String body; //正文
	
	private String directoryName; //所在目录名称
	
	//指定访问者
	private RecordVisitorList dataVisitors = new RecordVisitorList();
	
    /**
     * @return Returns the directoryId.
     */
    public long getDirectoryId() {
        return directoryId;
    }
    /**
     * @param directoryId The directoryId to set.
     */
    public void setDirectoryId(long directoryId) {
        this.directoryId = directoryId;
    }
    /**
     * @return Returns the docmark.
     */
    public java.lang.String getDocmark() {
        return docmark;
    }
    /**
     * @param docmark The docmark to set.
     */
    public void setDocmark(java.lang.String docmark) {
        this.docmark = docmark;
    }
    /**
     * @return Returns the fromUnit.
     */
    public java.lang.String getFromUnit() {
        return fromUnit;
    }
    /**
     * @param fromUnit The fromUnit to set.
     */
    public void setFromUnit(java.lang.String fromUnit) {
        this.fromUnit = fromUnit;
    }
    /**
	 * @return the generateDate
	 */
	public Date getGenerateDate() {
		return generateDate;
	}
	/**
	 * @param generateDate the generateDate to set
	 */
	public void setGenerateDate(Date generateDate) {
		this.generateDate = generateDate;
	}
	/**
     * @return Returns the remark.
     */
    public java.lang.String getRemark() {
        return remark;
    }
    /**
     * @param remark The remark to set.
     */
    public void setRemark(java.lang.String remark) {
        this.remark = remark;
    }
    /**
     * @return Returns the secureLevel.
     */
    public java.lang.String getSecureLevel() {
        return secureLevel;
    }
    /**
     * @param secureLevel The secureLevel to set.
     */
    public void setSecureLevel(java.lang.String secureLevel) {
        this.secureLevel = secureLevel;
    }
    /**
     * @return Returns the subject.
     */
    public java.lang.String getSubject() {
        return subject;
    }
    /**
     * @param subject The subject to set.
     */
    public void setSubject(java.lang.String subject) {
        this.subject = subject;
    }
    /**
     * @return Returns the body.
     */
    public String getBody() {
        return body;
    }
    /**
     * @param body The body to set.
     */
    public void setBody(String body) {
        this.body = body;
    }
    /**
     * @return Returns the directoryName.
     */
    public String getDirectoryName() {
        return directoryName;
    }
    /**
     * @param directoryName The directoryName to set.
     */
    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}
	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	/**
	 * @return the dataVisitors
	 */
	public RecordVisitorList getDataVisitors() {
		return dataVisitors;
	}
	/**
	 * @param dataVisitors the dataVisitors to set
	 */
	public void setDataVisitors(RecordVisitorList dataVisitors) {
		this.dataVisitors = dataVisitors;
	}
}