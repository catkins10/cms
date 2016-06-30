package com.yuanluesoft.cms.sitemanage.pojo;

import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 站点管理:栏目(cms_column)
 * @author linchuan
 *
 */
public class WebColumn extends WebDirectory {
	private Set relationLinks; //相关链接
	private Set relatedLinks; //被引用的记录
	
	/**
	 * 获取有效的相关链接
	 * @return
	 */
	public List getValidRelationLinks() {
		return ListUtils.getSubListByProperty(relationLinks, "halt", new Integer(0)); //相关链接
	}
	
	/**
	 * @return the relatedLinks
	 */
	public Set getRelatedLinks() {
		return relatedLinks;
	}
	/**
	 * @param relatedLinks the relatedLinks to set
	 */
	public void setRelatedLinks(Set relatedLinks) {
		this.relatedLinks = relatedLinks;
	}
	/**
	 * @return the relationLinks
	 */
	public Set getRelationLinks() {
		return relationLinks;
	}
	/**
	 * @param relationLinks the relationLinks to set
	 */
	public void setRelationLinks(Set relationLinks) {
		this.relationLinks = relationLinks;
	}
}