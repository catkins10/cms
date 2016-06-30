package com.yuanluesoft.cms.pagebuilder.model.page;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 * 页面元素
 * @author linchuan
 *
 */
public class SitePageElement extends CloneableObject {
	private String name; //名称
	private boolean staticPageSupport; //是否支持生成静态页面,默认支持,不支持的是更新频率很快的元素,如：排行榜、天气预报
	private String processor; //元素处理器
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the processor
	 */
	public String getProcessor() {
		return processor;
	}
	/**
	 * @param processor the processor to set
	 */
	public void setProcessor(String processor) {
		this.processor = processor;
	}
	/**
	 * @return the staticPageSupport
	 */
	public boolean isStaticPageSupport() {
		return staticPageSupport;
	}
	/**
	 * @param staticPageSupport the staticPageSupport to set
	 */
	public void setStaticPageSupport(boolean staticPageSupport) {
		this.staticPageSupport = staticPageSupport;
	}
}
