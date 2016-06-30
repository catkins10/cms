package com.yuanluesoft.jeaf.stat.actions.statistics;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.stat.forms.Statistics;
import com.yuanluesoft.jeaf.stat.service.StatService;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
       
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        StatService statService = (StatService)getService("statService");
        Statistics statisticsForm = (Statistics)form;
        statisticsForm.setStatistics(statService.getStatistics());
        return mapping.findForward("load");
    }
}