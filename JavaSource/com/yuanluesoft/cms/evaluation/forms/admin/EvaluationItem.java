package com.yuanluesoft.cms.evaluation.forms.admin;


/**
 * 
 * @author linchuan
 *
 */
public class EvaluationItem extends EvaluationTopic {
	private com.yuanluesoft.cms.evaluation.pojo.EvaluationItem item = new com.yuanluesoft.cms.evaluation.pojo.EvaluationItem();

	/**
	 * @return the item
	 */
	public com.yuanluesoft.cms.evaluation.pojo.EvaluationItem getItem() {
		return item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(com.yuanluesoft.cms.evaluation.pojo.EvaluationItem item) {
		this.item = item;
	}
}