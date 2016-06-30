package com.yuanluesoft.msa.fees.forms;

import com.yuanluesoft.msa.fees.pojo.MsaFeesItem;

/**
 * 
 * @author linchuan
 *
 */
public class FeesItem extends Fees {
	private MsaFeesItem item = new MsaFeesItem();

	/**
	 * @return the item
	 */
	public MsaFeesItem getItem() {
		return item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(MsaFeesItem item) {
		this.item = item;
	}
}