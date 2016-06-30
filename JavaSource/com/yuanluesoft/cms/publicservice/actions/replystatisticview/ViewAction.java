package com.yuanluesoft.cms.publicservice.actions.replystatisticview;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.publicservice.forms.admin.ReplyStatisticView;
import com.yuanluesoft.jeaf.application.action.applicationview.ApplicationViewAction;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class ViewAction extends ApplicationViewAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		String pojoClassName = view.getPojoClassName();
		pojoClassName = pojoClassName.substring(pojoClassName.lastIndexOf('.') + 1);
		ReplyStatisticView replyStatisticViewForm = (ReplyStatisticView)viewForm;
		if(replyStatisticViewForm.getCategory()==null || replyStatisticViewForm.getCategory().isEmpty()) { //没有指定分类
			view.addWhere(pojoClassName + ".id=-1");
		}
		else {
			if(replyStatisticViewForm.getBeginDate()!=null) {
				view.addWhere(pojoClassName + ".created>=DATE(" + DateTimeUtils.formatDate(replyStatisticViewForm.getBeginDate(), null) + ")");
			}
			if(replyStatisticViewForm.getEndDate()!=null) {
				view.addWhere(pojoClassName + ".created<DATE(" + DateTimeUtils.formatDate(DateTimeUtils.add(replyStatisticViewForm.getEndDate(), Calendar.DAY_OF_MONTH, 1), null) + ")");
			}
			//按时答复|ontime\0超时答复|timeout\0没有答复|noreply
			view.addWhere("(select Min(PublicServiceOpinion.created)" +
						  " from PublicServiceOpinion PublicServiceOpinion" +
						  " where PublicServiceOpinion.mainRecordId=" + pojoClassName + ".id" +
						  " and PublicServiceOpinion.opinionType='部门办理')" + ("noreply".equals(replyStatisticViewForm.getCategory()) ? " is null" : ("ontime".equals(replyStatisticViewForm.getCategory()) ? "<=" : ">") + pojoClassName + ".workingDate"));
		}
	}
}