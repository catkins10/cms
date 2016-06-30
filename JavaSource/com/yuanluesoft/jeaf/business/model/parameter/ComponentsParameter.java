package com.yuanluesoft.jeaf.business.model.parameter;

/**
 * 参数:组成部分列表
 * @author linchuan
 *
 */
public class ComponentsParameter {
	private String className; //类名称
	private String presettingOpinionTypes; //预置的意见类型,仅对options有效,如：部门意见,批示意见|required
	private boolean lazyLoad; //是否延迟加载,默认true,记录很多时应该设为false
	private String url; //显示组成元素详细信息的URL
	private String listFields; //TODO: 列表字段,输出组成部分jsp时使用

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the lazyLoad
	 */
	public boolean isLazyLoad() {
		return lazyLoad;
	}

	/**
	 * @param lazyLoad the lazyLoad to set
	 */
	public void setLazyLoad(boolean lazyLoad) {
		this.lazyLoad = lazyLoad;
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

	/**
	 * @return the listFields
	 */
	public String getListFields() {
		return listFields;
	}

	/**
	 * @param listFields the listFields to set
	 */
	public void setListFields(String listFields) {
		this.listFields = listFields;
	}

	/**
	 * @return the presettingOpinionTypes
	 */
	public String getPresettingOpinionTypes() {
		return presettingOpinionTypes;
	}

	/**
	 * @param presettingOpinionTypes the presettingOpinionTypes to set
	 */
	public void setPresettingOpinionTypes(String presettingOpinionTypes) {
		this.presettingOpinionTypes = presettingOpinionTypes;
	}
}