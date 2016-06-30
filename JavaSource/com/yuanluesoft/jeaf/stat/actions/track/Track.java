package com.yuanluesoft.jeaf.stat.actions.track;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.stat.service.StatService;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Track extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	StatService statService = (StatService)getService("statService");
       	com.yuanluesoft.jeaf.stat.forms.Track trackForm = (com.yuanluesoft.jeaf.stat.forms.Track)form;
    	
        //设置输出内容为脚本
        response.setContentType("text/javascript");
        response.setCharacterEncoding("UTF-8");
    	//获取访问记录
        long recordId = trackForm.getRecordId()>0 ? trackForm.getRecordId() : trackForm.getSiteId();
        long accessTimes;
        if(trackForm.isCountDisable()) {
        	accessTimes = statService.getAccessTimes(trackForm.getApplicationName(), trackForm.getPageName(), recordId);
        }
        else {
        	accessTimes = statService.access(trackForm.getApplicationName(), trackForm.getPageName(), recordId);
        }
        if(trackForm.isWriteAccessCount()) {
        	response.getWriter().write("document.write('" + accessTimes + "')");
        }
    	return null;
    }
}