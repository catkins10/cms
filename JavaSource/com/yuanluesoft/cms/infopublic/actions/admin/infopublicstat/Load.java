package com.yuanluesoft.cms.infopublic.actions.admin.infopublicstat;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.infopublic.forms.admin.InfoPublicStat;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
        	getSessionInfo(request, response);
        }
        catch(SessionException se) {
        	return redirectToLogin(this, mapping, form, request, response, se, false);
        }
        InfoPublicStat statForm = (InfoPublicStat)form;
        statForm.setBeginDate(DateTimeUtils.set(DateTimeUtils.set(DateTimeUtils.date(), Calendar.MONTH, 0), Calendar.DAY_OF_MONTH, 1));
        statForm.setFormTitle("信息公开统计");
        return mapping.findForward("load");
    }
}