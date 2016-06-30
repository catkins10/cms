package com.yuanluesoft.jeaf.usermanage.forms.admin;

import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * 
 * @author linchuan
 *
 */
public class PersonView extends ViewForm {
	private long orgId;
	private boolean searchChildren;
	private String webApplicationSafeUrl;
	
	/**
	 * @return Returns the orgId.
	 */
	public long getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId The orgId to set.
	 */
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return Returns the searchChildren.
	 */
	public boolean isSearchChildren() {
		return searchChildren;
	}
	/**
	 * @param searchChildren The searchChildren to set.
	 */
	public void setSearchChildren(boolean searchChildren) {
		this.searchChildren = searchChildren;
	}
	/**
	 * @return the webApplicationSafeUrl
	 */
	public String getWebApplicationSafeUrl() {
		return webApplicationSafeUrl;
	}
	/**
	 * @param webApplicationSafeUrl the webApplicationSafeUrl to set
	 */
	public void setWebApplicationSafeUrl(String webApplicationSafeUrl) {
		this.webApplicationSafeUrl = webApplicationSafeUrl;
	}
}