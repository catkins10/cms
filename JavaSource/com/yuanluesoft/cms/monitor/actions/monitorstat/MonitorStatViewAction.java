package com.yuanluesoft.cms.monitor.actions.monitorstat;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.monitor.forms.MonitorStat;
import com.yuanluesoft.jeaf.application.action.applicationview.ApplicationViewAction;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * @author linchuan
 *
 */
public class MonitorStatViewAction extends ApplicationViewAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#initForm(com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void initForm(ViewForm viewForm, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		MonitorStat monitorStat = (MonitorStat)viewForm;
		if(monitorStat.getBeginDate()==null && monitorStat.getEndDate()==null) {
			if("true".equals(monitorStat.getViewPackage().getView().getExtendParameter("statByYear"))) { //按年统计
				monitorStat.setBeginDate(DateTimeUtils.getYearBegin());
				monitorStat.setEndDate(DateTimeUtils.getYearEnd());
			}
			else { //按季度
				monitorStat.setBeginDate(DateTimeUtils.getQuarterBegin());
				monitorStat.setEndDate(DateTimeUtils.getQuarterEnd());
			}
		}
		super.initForm(viewForm, request, sessionInfo);
		viewForm.getViewPackage().getView().setExtendParameter("beginDate", monitorStat.getBeginDate());
		viewForm.getViewPackage().getView().setExtendParameter("endDate", monitorStat.getEndDate());
	}	
}