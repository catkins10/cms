/*
 * Created on 2006-8-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.archives.administrative.pojo;

import java.sql.Date;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 档案管理:文书档案(archives_administrative)
 * @author linchuan
 *
 */
public class AdministrativeArchives extends Record {
	private String subject; //文件题名
	private String docCategory; //公文种类
	private String docWord; //文件字号
	private String keyword; //主题词
	private String responsibilityPerson; //责任者
	private String secureLevel; //文件密级
	private String secureLevelCode; //文件密级编号
	private Date signDate; //成文日期
	private int count; //份数
	private int pageCount; //文件页数
	private String unit; //机构或问题
	private String unitCode; //机构或问题编号
	private Date filingDate; //归档日期
	private String fondsCode; //全宗号
	private String fondsName; //全宗名称
	private int filingYear; //归档年度
	private String rotentionPeriod; //保管期限
	private String rotentionPeriodCode; //保管期限编号
	private int serialNumber; //顺序号
	private String archivesCode; //档号
	private long categoryId; //分类ID
	private String archivesType; //文件类型,收文/发文等
	private String remark; //备注
	private Set visitors;
	private Set bodies;

    /**
     * @return Returns the categoryId.
     */
    public long getCategoryId() {
        return categoryId;
    }
    /**
     * @param categoryId The categoryId to set.
     */
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
    /**
     * @return Returns the count.
     */
    public int getCount() {
        return count;
    }
    /**
     * @param count The count to set.
     */
    public void setCount(int count) {
        this.count = count;
    }
    /**
     * @return Returns the docCategory.
     */
    public java.lang.String getDocCategory() {
        return docCategory;
    }
    /**
     * @param docCategory The docCategory to set.
     */
    public void setDocCategory(java.lang.String docCategory) {
        this.docCategory = docCategory;
    }
    /**
     * @return Returns the docWord.
     */
    public java.lang.String getDocWord() {
        return docWord;
    }
    /**
     * @param docWord The docWord to set.
     */
    public void setDocWord(java.lang.String docWord) {
        this.docWord = docWord;
    }
    /**
     * @return Returns the filingYear.
     */
    public int getFilingYear() {
        return filingYear;
    }
    /**
     * @param filingYear The filingYear to set.
     */
    public void setFilingYear(int filingYear) {
        this.filingYear = filingYear;
    }
    /**
     * @return Returns the keyword.
     */
    public java.lang.String getKeyword() {
        return keyword;
    }
    /**
     * @param keyword The keyword to set.
     */
    public void setKeyword(java.lang.String keyword) {
        this.keyword = keyword;
    }
    /**
     * @return Returns the pageCount.
     */
    public int getPageCount() {
        return pageCount;
    }
    /**
     * @param pageCount The pageCount to set.
     */
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
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
     * @return Returns the responsibilityPerson.
     */
    public java.lang.String getResponsibilityPerson() {
        return responsibilityPerson;
    }
    /**
     * @param responsibilityPerson The responsibilityPerson to set.
     */
    public void setResponsibilityPerson(java.lang.String responsibilityPerson) {
        this.responsibilityPerson = responsibilityPerson;
    }
    /**
     * @return Returns the rotentionPeriod.
     */
    public java.lang.String getRotentionPeriod() {
        return rotentionPeriod;
    }
    /**
     * @param rotentionPeriod The rotentionPeriod to set.
     */
    public void setRotentionPeriod(java.lang.String rotentionPeriod) {
        this.rotentionPeriod = rotentionPeriod;
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
     * @return Returns the serialNumber.
     */
    public int getSerialNumber() {
        return serialNumber;
    }
    /**
     * @param serialNumber The serialNumber to set.
     */
    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
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
     * @return Returns the unit.
     */
    public java.lang.String getUnit() {
        return unit;
    }
    /**
     * @param unit The unit to set.
     */
    public void setUnit(java.lang.String unit) {
        this.unit = unit;
    }
    /**
     * @return Returns the archivesCode.
     */
    public java.lang.String getArchivesCode() {
        return archivesCode;
    }
    /**
     * @param archivesCode The archivesCode to set.
     */
    public void setArchivesCode(java.lang.String archivesCode) {
        this.archivesCode = archivesCode;
    }
	/**
	 * @return Returns the filingDate.
	 */
	public java.sql.Date getFilingDate() {
		return filingDate;
	}
	/**
	 * @param filingDate The filingDate to set.
	 */
	public void setFilingDate(java.sql.Date filingDate) {
		this.filingDate = filingDate;
	}
	/**
	 * @return Returns the signDate.
	 */
	public java.sql.Date getSignDate() {
		return signDate;
	}
	/**
	 * @param signDate The signDate to set.
	 */
	public void setSignDate(java.sql.Date signDate) {
		this.signDate = signDate;
	}
	/**
	 * @return Returns the fondsCode.
	 */
	public java.lang.String getFondsCode() {
		return fondsCode;
	}
	/**
	 * @param fondsCode The fondsCode to set.
	 */
	public void setFondsCode(java.lang.String fondsCode) {
		this.fondsCode = fondsCode;
	}
	/**
	 * @return Returns the fondsName.
	 */
	public java.lang.String getFondsName() {
		return fondsName;
	}
	/**
	 * @param fondsName The fondsName to set.
	 */
	public void setFondsName(java.lang.String fondsName) {
		this.fondsName = fondsName;
	}
	/**
	 * @return Returns the unitCode.
	 */
	public java.lang.String getUnitCode() {
		return unitCode;
	}
	/**
	 * @param unitCode The unitCode to set.
	 */
	public void setUnitCode(java.lang.String unitCode) {
		this.unitCode = unitCode;
	}
	/**
	 * @return Returns the secureLevelCode.
	 */
	public java.lang.String getSecureLevelCode() {
		return secureLevelCode;
	}
	/**
	 * @param secureLevelCode The secureLevelCode to set.
	 */
	public void setSecureLevelCode(java.lang.String secureLevelCode) {
		this.secureLevelCode = secureLevelCode;
	}
	/**
	 * @return Returns the rotentionPeriodCode.
	 */
	public java.lang.String getRotentionPeriodCode() {
		return rotentionPeriodCode;
	}
	/**
	 * @param rotentionPeriodCode The rotentionPeriodCode to set.
	 */
	public void setRotentionPeriodCode(java.lang.String rotentionPeriodCode) {
		this.rotentionPeriodCode = rotentionPeriodCode;
	}
	/**
	 * @return the archivesType
	 */
	public String getArchivesType() {
		return archivesType;
	}
	/**
	 * @param archivesType the archivesType to set
	 */
	public void setArchivesType(String archivesType) {
		this.archivesType = archivesType;
	}
	/**
	 * @return the visitors
	 */
	public Set getVisitors() {
		return visitors;
	}
	/**
	 * @param visitors the visitors to set
	 */
	public void setVisitors(Set visitors) {
		this.visitors = visitors;
	}
	/**
	 * @return the bodies
	 */
	public Set getBodies() {
		return bodies;
	}
	/**
	 * @param bodies the bodies to set
	 */
	public void setBodies(Set bodies) {
		this.bodies = bodies;
	}
}
