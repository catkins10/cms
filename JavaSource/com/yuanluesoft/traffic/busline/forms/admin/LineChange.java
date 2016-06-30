package com.yuanluesoft.traffic.busline.forms.admin;

import com.yuanluesoft.traffic.busline.pojo.BusLineChange;


/**
 * 
 * @author lmiky
 *
 */
public class LineChange extends BusLine {
	private BusLineChange change = new BusLineChange();

	/**
	 * @return the change
	 */
	public BusLineChange getChange() {
		return change;
	}

	/**
	 * @param change the change to set
	 */
	public void setChange(BusLineChange change) {
		this.change = change;
	}
}