package com.yuanluesoft.cms.capture.forms;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class Task extends ActionForm {
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
	private int enabled = 1; //是否启用
	private int schedule = 1; //抓取安排,0/手动,1/指定时间,2/指定时间间隔
	private String captureTime; //抓取时间
	private int captureInterval; //抓取时间间隔,指定时间间隔时有效，以分钟为单位
	private Timestamp nextCaptureTime; //下一次抓取时间
	private Timestamp created; //创建时间
	private String creator; //创建人
	private long creatorId; //创建人ID
	private Set fields; //字段配置
	
	//列表配置
	private String listPageURL; //列表页面URL
	private String listPageHtml; //列表页面HTML
	private List listPageFields; //列表页面字段配置
	private String listFieldBegin; //字段开始位置
	private String listFieldEnd; //字段结束位置
	private String listFieldValue; //指定字段的值
	private String listArraySeparator; //数组分隔符
	private String listFieldFormat; //字段格式,日期格式化
	//内容页面配置
	private String contentPageURL; //内容页面URL
	private String contentPageHtml; //内容页面HTML
	private List contentPageFields; //内容页面字段配置
	private String contentFieldBegin; //字段开始位置
	private String contentFieldEnd; //字段结束位置
	private String contentFieldValue; //指定字段的值
	private String contentArraySeparator; //数组分隔符
	private String contentFieldFormat; //字段格式,日期格式化
	
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
	 * @return the contentPageHtml
	 */
	public String getContentPageHtml() {
		return contentPageHtml;
	}
	/**
	 * @param contentPageHtml the contentPageHtml to set
	 */
	public void setContentPageHtml(String contentPageHtml) {
		this.contentPageHtml = contentPageHtml;
	}
	/**
	 * @return the contentPageURL
	 */
	public String getContentPageURL() {
		return contentPageURL;
	}
	/**
	 * @param contentPageURL the contentPageURL to set
	 */
	public void setContentPageURL(String contentPageURL) {
		this.contentPageURL = contentPageURL;
	}
	/**
	 * @return the listPageHtml
	 */
	public String getListPageHtml() {
		return listPageHtml;
	}
	/**
	 * @param listPageHtml the listPageHtml to set
	 */
	public void setListPageHtml(String listPageHtml) {
		this.listPageHtml = listPageHtml;
	}
	/**
	 * @return the listPageURL
	 */
	public String getListPageURL() {
		return listPageURL;
	}
	/**
	 * @param listPageURL the listPageURL to set
	 */
	public void setListPageURL(String listPageURL) {
		this.listPageURL = listPageURL;
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
	 * @return the contentPageFields
	 */
	public List getContentPageFields() {
		return contentPageFields;
	}
	/**
	 * @param contentPageFields the contentPageFields to set
	 */
	public void setContentPageFields(List contentPageFields) {
		this.contentPageFields = contentPageFields;
	}
	/**
	 * @return the listPageFields
	 */
	public List getListPageFields() {
		return listPageFields;
	}
	/**
	 * @param listPageFields the listPageFields to set
	 */
	public void setListPageFields(List listPageFields) {
		this.listPageFields = listPageFields;
	}
	/**
	 * @return the contentFieldBegin
	 */
	public String getContentFieldBegin() {
		return contentFieldBegin;
	}
	/**
	 * @param contentFieldBegin the contentFieldBegin to set
	 */
	public void setContentFieldBegin(String contentFieldBegin) {
		this.contentFieldBegin = contentFieldBegin;
	}
	/**
	 * @return the contentFieldEnd
	 */
	public String getContentFieldEnd() {
		return contentFieldEnd;
	}
	/**
	 * @param contentFieldEnd the contentFieldEnd to set
	 */
	public void setContentFieldEnd(String contentFieldEnd) {
		this.contentFieldEnd = contentFieldEnd;
	}
	/**
	 * @return the contentFieldFormat
	 */
	public String getContentFieldFormat() {
		return contentFieldFormat;
	}
	/**
	 * @param contentFieldFormat the contentFieldFormat to set
	 */
	public void setContentFieldFormat(String contentFieldFormat) {
		this.contentFieldFormat = contentFieldFormat;
	}
	/**
	 * @return the listFieldBegin
	 */
	public String getListFieldBegin() {
		return listFieldBegin;
	}
	/**
	 * @param listFieldBegin the listFieldBegin to set
	 */
	public void setListFieldBegin(String listFieldBegin) {
		this.listFieldBegin = listFieldBegin;
	}
	/**
	 * @return the listFieldEnd
	 */
	public String getListFieldEnd() {
		return listFieldEnd;
	}
	/**
	 * @param listFieldEnd the listFieldEnd to set
	 */
	public void setListFieldEnd(String listFieldEnd) {
		this.listFieldEnd = listFieldEnd;
	}
	/**
	 * @return the listFieldFormat
	 */
	public String getListFieldFormat() {
		return listFieldFormat;
	}
	/**
	 * @param listFieldFormat the listFieldFormat to set
	 */
	public void setListFieldFormat(String listFieldFormat) {
		this.listFieldFormat = listFieldFormat;
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
	 * @return the contentFieldValue
	 */
	public String getContentFieldValue() {
		return contentFieldValue;
	}
	/**
	 * @param contentFieldValue the contentFieldValue to set
	 */
	public void setContentFieldValue(String contentFieldValue) {
		this.contentFieldValue = contentFieldValue;
	}
	/**
	 * @return the contentArraySeparator
	 */
	public String getContentArraySeparator() {
		return contentArraySeparator;
	}
	/**
	 * @param contentArraySeparator the contentArraySeparator to set
	 */
	public void setContentArraySeparator(String contentArraySeparator) {
		this.contentArraySeparator = contentArraySeparator;
	}
	/**
	 * @return the listArraySeparator
	 */
	public String getListArraySeparator() {
		return listArraySeparator;
	}
	/**
	 * @param listArraySeparator the listArraySeparator to set
	 */
	public void setListArraySeparator(String listArraySeparator) {
		this.listArraySeparator = listArraySeparator;
	}
	/**
	 * @return the listFieldValue
	 */
	public String getListFieldValue() {
		return listFieldValue;
	}
	/**
	 * @param listFieldValue the listFieldValue to set
	 */
	public void setListFieldValue(String listFieldValue) {
		this.listFieldValue = listFieldValue;
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