package com.yuanluesoft.cms.evaluation.forms;

import com.yuanluesoft.jeaf.util.DateTimeUtils;


/**
 * 测评
 * @author linchuan
 *
 */
public class Evaluation extends com.yuanluesoft.cms.evaluation.forms.admin.Evaluation {
	
	/**
	 * 是否过期
	 * @return
	 */
	public boolean isExpires() {
		return getTopic().getEndTime()!=null && getTopic().getEndTime().before(DateTimeUtils.now());
	}
}