package com.yuanluesoft.cms.pagebuilder.pojo;

/**
 * 静态页面生成任务:按URL(cms_page_task_by_url)
 * @author linchuan
 *
 */
public class StaticPageTaskByUrl extends StaticPageTask {
	private String url; //URL
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "rebuild by url " + url;
	}
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}