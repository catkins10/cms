package com.yuanluesoft.cms.capture.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 抓取任务(cms_capture_task)
 * @author linchuan
 *
 */
public class CmsCaptureTask extends Record {
	private long siteId; //站点ID
	private String description; //描述
	private String category; //分类
	private String businessClassName; //业务对象类名称
	private String businessTitle; //业务对象标题,如:文章、政府信息
	private String websiteCharset; //字符集
	private String captureURL; //列表页面URL
	private char recordPageURLDirection = '0'; //记录页面URL获取方式
	private String recordPageURL; //记录页面URL格式
	private char nextPageDirection = '0'; //下一页链接方式,0/不抓取其它页,1/URL上加页码方式,2/链接方式
	private String nextPageURL; //分页URL格式
	private int beginPage; //分页URL起始页码,从0开始或者从1开始
	private String extendedParameters; //扩展参数配置,由业务对象自定义
	private int enabled; //是否启用
	private int schedule = 1; //抓取安排,0/手动,1/指定时间,2/指定时间间隔
	private String captureTime; //抓取时间
	private int captureInterval; //抓取时间间隔,指定时间间隔时有效，以分钟为单位
	private Timestamp nextCaptureTime; //下一次抓取时间
	private Timestamp created; //创建时间
	private long creatorId; //创建人ID
	private String creator; //创建人
	private Set fields; //字段配置
	
	/**
	 * @return the beginPage
	 */
	public int getBeginPage() {
		return beginPage;
	}
	/**
	 * @param beginPage the beginPage to set
	 */
	public void setBeginPage(int beginPage) {
		this.beginPage = beginPage;
	}
	/**
	 * @return the captureURL
	 */
	public String getCaptureURL() {
		return captureURL;
	}
	/**
	 * @param captureURL the captureURL to set
	 */
	public void setCaptureURL(String captureURL) {
		this.captureURL = captureURL;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the extendedParameters
	 */
	public String getExtendedParameters() {
		return extendedParameters;
	}
	/**
	 * @param extendedParameters the extendedParameters to set
	 */
	public void setExtendedParameters(String extendedParameters) {
		this.extendedParameters = extendedParameters;
	}
	/**
	 * @return the nextPageDirection
	 */
	public char getNextPageDirection() {
		return nextPageDirection;
	}
	/**
	 * @param nextPageDirection the nextPageDirection to set
	 */
	public void setNextPageDirection(char nextPageDirection) {
		this.nextPageDirection = nextPageDirection;
	}
	/**
	 * @return the nextPageURL
	 */
	public String getNextPageURL() {
		return nextPageURL;
	}
	/**
	 * @param nextPageURL the nextPageURL to set
	 */
	public void setNextPageURL(String nextPageURL) {
		this.nextPageURL = nextPageURL;
	}
	/**
	 * @return the fields
	 */
	public Set getFields() {
		return fields;
	}
	/**
	 * @param fields the fields to set
	 */
	public void setFields(Set fields) {
		this.fields = fields;
	}
	/**
	 * @return the businessClassName
	 */
	public String getBusinessClassName() {
		return businessClassName;
	}
	/**
	 * @param businessClassName the businessClassName to set
	 */
	public void setBusinessClassName(String businessClassName) {
		this.businessClassName = businessClassName;
	}
	/**
	 * @return the businessTitle
	 */
	public String getBusinessTitle() {
		return businessTitle;
	}
	/**
	 * @param businessTitle the businessTitle to set
	 */
	public void setBusinessTitle(String businessTitle) {
		this.businessTitle = businessTitle;
	}
	/**
	 * @return the enabled
	 */
	public int getEnabled() {
		return enabled;
	}
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	/**
	 * @return the recordPageURL
	 */
	public String getRecordPageURL() {
		return recordPageURL;
	}
	/**
	 * @param recordPageURL the recordPageURL to set
	 */
	public void setRecordPageURL(String recordPageURL) {
		this.recordPageURL = recordPageURL;
	}
	/**
	 * @return the recordPageURLDirection
	 */
	public char getRecordPageURLDirection() {
		return recordPageURLDirection;
	}
	/**
	 * @param recordPageURLDirection the recordPageURLDirection to set
	 */
	public void setRecordPageURLDirection(char recordPageURLDirection) {
		this.recordPageURLDirection = recordPageURLDirection;
	}
	/**
	 * @return the captureInterval
	 */
	public int getCaptureInterval() {
		return captureInterval;
	}
	/**
	 * @param captureInterval the captureInterval to set
	 */
	public void setCaptureInterval(int captureInterval) {
		this.captureInterval = captureInterval;
	}
	/**
	 * @return the captureTime
	 */
	public String getCaptureTime() {
		return captureTime;
	}
	/**
	 * @param captureTime the captureTime to set
	 */
	public void setCaptureTime(String captureTime) {
		this.captureTime = captureTime;
	}
	/**
	 * @return the schedule
	 */
	public int getSchedule() {
		return schedule;
	}
	/**
	 * @param schedule the schedule to set
	 */
	public void setSchedule(int schedule) {
		this.schedule = schedule;
	}
	/**
	 * @return the siteId
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the nextCaptureTime
	 */
	public Timestamp getNextCaptureTime() {
		return nextCaptureTime;
	}
	/**
	 * @param nextCaptureTime the nextCaptureTime to set
	 */
	public void setNextCaptureTime(Timestamp nextCaptureTime) {
		this.nextCaptureTime = nextCaptureTime;
	}
	/**
	 * @return the websiteCharset
	 */
	public String getWebsiteCharset() {
		return websiteCharset;
	}
	/**
	 * @param websiteCharset the websiteCharset to set
	 */
	public void setWebsiteCharset(String websiteCharset) {
		this.websiteCharset = websiteCharset;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
}