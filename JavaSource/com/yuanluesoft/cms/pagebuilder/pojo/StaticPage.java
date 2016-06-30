package com.yuanluesoft.cms.pagebuilder.pojo;

import java.sql.Timestamp;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 静态页面生成(cms_static_page)
 * @author linchuan
 *
 */
public class StaticPage extends Record {
	private String dynamicUrl; //动态页面URL
	private long dynamicUrlHash; //动态页面URL哈希编码
	private String staticUrl; //静态页面URL
	private char realtimeStaticPage = '0'; //是否实时生成静态页面,如：文章、投诉、评论，有新记录添加时，其他记录页面不更新
	private char staticPageDisabled = '0'; //是否禁止生成静态页面
	private long recordId; //记录ID
	private String recordClassName; //记录类名称
	private String applicationName; //应用名称
	private String pageName; //页面名称
	private long siteId; //隶属站点ID
	private long columnId; //隶属栏目ID,当文章不属于当前站点且使用的是当前站点的模板时,columnId=siteId
	private Timestamp created; //生成日期
	private Timestamp expiresTime; //有效期,不为空时，由定时器自动更新
	
	private Set templates; //使用的模板
	private Set pageElements; //引用的页面元素
	private Set recordLists; //引用的记录列表
	private Set jsElements; //用JS输出的页面元素
	
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
	 * @return the dynamicUrl
	 */
	public String getDynamicUrl() {
		return dynamicUrl;
	}
	/**
	 * @param dynamicUrl the dynamicUrl to set
	 */
	public void setDynamicUrl(String dynamicUrl) {
		this.dynamicUrl = dynamicUrl;
	}
	/**
	 * @return the pageName
	 */
	public String getPageName() {
		return pageName;
	}
	/**
	 * @param pageName the pageName to set
	 */
	public void setPageName(String pageName) {
		this.pageName = pageName;
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
	 * @return the staticUrl
	 */
	public String getStaticUrl() {
		return staticUrl;
	}
	/**
	 * @param staticUrl the staticUrl to set
	 */
	public void setStaticUrl(String staticUrl) {
		this.staticUrl = staticUrl;
	}
	/**
	 * @return the expiresTime
	 */
	public Timestamp getExpiresTime() {
		return expiresTime;
	}
	/**
	 * @param expiresTime the expiresTime to set
	 */
	public void setExpiresTime(Timestamp expiresTime) {
		this.expiresTime = expiresTime;
	}
	/**
	 * @return the templates
	 */
	public Set getTemplates() {
		return templates;
	}
	/**
	 * @param templates the templates to set
	 */
	public void setTemplates(Set templates) {
		this.templates = templates;
	}
	/**
	 * @return the columnId
	 */
	public long getColumnId() {
		return columnId;
	}
	/**
	 * @param columnId the columnId to set
	 */
	public void setColumnId(long columnId) {
		this.columnId = columnId;
	}
	/**
	 * @return the recordLists
	 */
	public Set getRecordLists() {
		return recordLists;
	}
	/**
	 * @param recordLists the recordLists to set
	 */
	public void setRecordLists(Set recordLists) {
		this.recordLists = recordLists;
	}
	/**
	 * @return the recordClassName
	 */
	public String getRecordClassName() {
		return recordClassName;
	}
	/**
	 * @param recordClassName the recordClassName to set
	 */
	public void setRecordClassName(String recordClassName) {
		this.recordClassName = recordClassName;
	}
	/**
	 * @return the realtimeStaticPage
	 */
	public char getRealtimeStaticPage() {
		return realtimeStaticPage;
	}
	/**
	 * @param realtimeStaticPage the realtimeStaticPage to set
	 */
	public void setRealtimeStaticPage(char realtimeStaticPage) {
		this.realtimeStaticPage = realtimeStaticPage;
	}
	/**
	 * @return the jsElements
	 */
	public Set getJsElements() {
		return jsElements;
	}
	/**
	 * @param jsElements the jsElements to set
	 */
	public void setJsElements(Set jsElements) {
		this.jsElements = jsElements;
	}
	/**
	 * @return the staticPageDisabled
	 */
	public char getStaticPageDisabled() {
		return staticPageDisabled;
	}
	/**
	 * @param staticPageDisabled the staticPageDisabled to set
	 */
	public void setStaticPageDisabled(char staticPageDisabled) {
		this.staticPageDisabled = staticPageDisabled;
	}
	/**
	 * @return the pageElements
	 */
	public Set getPageElements() {
		return pageElements;
	}
	/**
	 * @param pageElements the pageElements to set
	 */
	public void setPageElements(Set pageElements) {
		this.pageElements = pageElements;
	}
	/**
	 * @return the dynamicUrlHash
	 */
	public long getDynamicUrlHash() {
		return dynamicUrlHash;
	}
	/**
	 * @param dynamicUrlHash the dynamicUrlHash to set
	 */
	public void setDynamicUrlHash(long dynamicUrlHash) {
		this.dynamicUrlHash = dynamicUrlHash;
	}
}