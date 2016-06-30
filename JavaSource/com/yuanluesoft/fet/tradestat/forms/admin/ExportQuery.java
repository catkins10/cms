package com.yuanluesoft.fet.tradestat.forms.admin;

import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * @author linchuan
 *
 */
public class ExportQuery extends ViewForm {
	private boolean categoryByCounty;

	/**
	 * @return the categoryByCounty
	 */
	public boolean isCategoryByCounty() {
		return categoryByCounty;
	}

	/**
	 * @param categoryByCounty the categoryByCounty to set
	 */
	public void setCategoryByCounty(boolean categoryByCounty) {
		this.categoryByCounty = categoryByCounty;
	}
}