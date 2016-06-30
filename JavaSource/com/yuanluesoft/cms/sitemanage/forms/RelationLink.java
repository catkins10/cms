package com.yuanluesoft.cms.sitemanage.forms;

import com.yuanluesoft.cms.sitemanage.pojo.WebColumnRelationLink;


/**
 * 
 * @author linchuan
 *
 */
public class RelationLink extends Column {
	private WebColumnRelationLink relationLink = new WebColumnRelationLink();

	/**
	 * @return the relationLink
	 */
	public WebColumnRelationLink getRelationLink() {
		return relationLink;
	}

	/**
	 * @param relationLink the relationLink to set
	 */
	public void setRelationLink(WebColumnRelationLink relationLink) {
		this.relationLink = relationLink;
	}
}