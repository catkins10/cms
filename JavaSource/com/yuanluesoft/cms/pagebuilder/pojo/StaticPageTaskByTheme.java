package com.yuanluesoft.cms.pagebuilder.pojo;

/**
 * 静态页面生成任务:按主题(cms_page_task_by_theme)
 * @author linchuan
 *
 */
public class StaticPageTaskByTheme extends StaticPageTask {
	private long themeId; //主题ID
	private long siteId; //站点ID
	private int notUseThemePage; //重建没有使用主题的
	private int realtimeStaticPageOnly; //只更新实时页面
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "rebuild by theme, id is " + themeId + ", siteId is " + siteId + ", notUseThemePage is " + notUseThemePage + ", realtimeStaticPageOnly is " + realtimeStaticPageOnly;
	}
	
	/**
	 * @return the notUseThemePage
	 */
	public int getNotUseThemePage() {
		return notUseThemePage;
	}
	/**
	 * @param notUseThemePage the notUseThemePage to set
	 */
	public void setNotUseThemePage(int notUseThemePage) {
		this.notUseThemePage = notUseThemePage;
	}
	/**
	 * @return the realtimeStaticPageOnly
	 */
	public int getRealtimeStaticPageOnly() {
		return realtimeStaticPageOnly;
	}
	/**
	 * @param realtimeStaticPageOnly the realtimeStaticPageOnly to set
	 */
	public void setRealtimeStaticPageOnly(int realtimeStaticPageOnly) {
		this.realtimeStaticPageOnly = realtimeStaticPageOnly;
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
	 * @return the themeId
	 */
	public long getThemeId() {
		return themeId;
	}
	/**
	 * @param themeId the themeId to set
	 */
	public void setThemeId(long themeId) {
		this.themeId = themeId;
	}
}