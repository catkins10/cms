package com.yuanluesoft.cms.pagebuilder.pojo;

/**
 * 静态页面生成任务:按模板(cms_page_task_by_templete)
 * @author linchuan
 *
 */
public class StaticPageTaskByTemplate extends StaticPageTask {
	private String applicationName; //应用名称
	private String pageName; //页面名称
	private long templateId; //模板ID
	private long webDirectoryId; //站点目录ID
	private int includeSubdirectory; //是否更新子目录

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "rebuild by template, application name is " + applicationName + ", page name is " + pageName + ", template id is " + templateId + ", web directory id is " + webDirectoryId + ", include subdirectory is " + includeSubdirectory;
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
	 * @return the includeSubdirectory
	 */
	public int getIncludeSubdirectory() {
		return includeSubdirectory;
	}
	/**
	 * @param includeSubdirectory the includeSubdirectory to set
	 */
	public void setIncludeSubdirectory(int includeSubdirectory) {
		this.includeSubdirectory = includeSubdirectory;
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
	 * @return the templateId
	 */
	public long getTemplateId() {
		return templateId;
	}
	/**
	 * @param templateId the templateId to set
	 */
	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}
	/**
	 * @return the webDirectoryId
	 */
	public long getWebDirectoryId() {
		return webDirectoryId;
	}
	/**
	 * @param webDirectoryId the webDirectoryId to set
	 */
	public void setWebDirectoryId(long webDirectoryId) {
		this.webDirectoryId = webDirectoryId;
	}
}