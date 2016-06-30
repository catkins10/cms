package com.yuanluesoft.cms.siteresource.forms;

import com.yuanluesoft.cms.siteresource.pojo.SiteResourceRelationLink;

/**
 * 
 * @author linchuan
 *
 */
public class RelationLink extends Article {
	private SiteResourceRelationLink relationLink = new SiteResourceRelationLink();

	/**
	 * @return the relationLink
	 */
	public SiteResourceRelationLink getRelationLink() {
		return relationLink;
	}

	/**
	 * @param relationLink the relationLink to set
	 */
	public void setRelationLink(SiteResourceRelationLink relationLink) {
		this.relationLink = relationLink;
	}
}