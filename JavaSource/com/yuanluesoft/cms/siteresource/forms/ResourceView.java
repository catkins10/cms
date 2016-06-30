package com.yuanluesoft.cms.siteresource.forms;

import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * 
 * @author linchuan
 *
 */
public class ResourceView extends ViewForm {
	private long siteId;
	private boolean searchChildren;
    

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
	 * @return Returns the siteId.
	 */
	public long getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId The siteId to set.
	 */
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
}