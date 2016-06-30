package com.yuanluesoft.j2oa.calendar.actions;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.calendar.forms.CalendarForm;
import com.yuanluesoft.j2oa.calendar.pojo.Calendar;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.messagecenter.service.MessageService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.service.UserBusyCheckService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Publish extends CalendarAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            return executeSaveAction(mapping, form, request, response, false, null, "发布成功", null);
        }
        catch(Exception e) {
            if("conflict".equals(e.getMessage())) { //日程安排冲突
                CalendarForm calendarForm = (CalendarForm)form;
                calendarForm.setAct(OPEN_MODE_EDIT);
                load(form, request, response);
                //设置对话框
                calendarForm.setInnerDialog("confirmPublich.jsp");
                calendarForm.setFormTitle("日程安排冲突");
                calendarForm.getFormActions().addFormAction(-1, "继续发布", "FormUtils.doAction('publishCalendar', 'forcePublish=true')", true);
                addReloadAction(calendarForm, "取消", request, -1, false);
                return mapping.getInputForward();
            }
            throw e;
        }
    }
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#validateBusiness(com.yuanluesoft.jeaf.business.service.BusinessService, org.apache.struts.action.ActionForm, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void validateBusiness(BusinessService validateService, ActionForm form, String openMode, Record record, SessionInfo sessionInfo, HttpServletRequest request) throws ValidateException, ServiceException, SystemUnregistException {
		super.validateBusiness(validateService, form, openMode, record, sessionInfo, request);
		Calendar calendar = (Calendar)record;
		if(calendar.getBeginTime().before(DateTimeUtils.now())) {
			((com.yuanluesoft.jeaf.form.ActionForm)form).setError("开始时间必须在当前时间以后");
			throw new ValidateException();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.core.actions.form.FormAction#saveRecord(com.yuanluesoft.jeaf.core.forms.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
	    CalendarForm calendarForm = (CalendarForm)form;
	    if(!calendarForm.isForcePublish()) { //不是强制发布
		    //检查领导日程安排是否有冲突
		    UserBusyCheckService userBusyCheckService = (UserBusyCheckService)getService("userBusyCheckService");
		    String[] leaderIds = calendarForm.getLeaders().getVisitorIds().split(",");
		    String[] leaderNames = calendarForm.getLeaders().getVisitorNames().split(",");
		    for(int i=0; i<leaderIds.length; i++) {
		        List checkResult = userBusyCheckService.userBusyCheck(Long.parseLong(leaderIds[i]), calendarForm.getBeginTime(), calendarForm.getEndTime());
		        if(checkResult!=null && !checkResult.isEmpty()) {
		            calendarForm.setConflict((calendarForm.getConflict()==null ? "" : calendarForm.getConflict() + "\r\n\r\n") + leaderNames[i]);
		            for(Iterator iterator = checkResult.iterator(); iterator.hasNext();) {
		                calendarForm.setConflict(calendarForm.getConflict() + "\r\n" + iterator.next());
		            }
		        }
		    }
		    if(calendarForm.getConflict()!=null) {
		        super.saveRecord(form, record, openMode, request, response, sessionInfo);
		        throw new Exception("conflict");
		    }
	    }
	    Calendar calendar = (Calendar)record;
		calendar.setPublish('1');
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		RecordControlService recordControlService = getRecordControlService();
		MessageService messageService = (MessageService)getService("messageService"); //消息服务
		List readers = recordControlService.copyVisitors(calendar.getId(), calendar.getId(), RecordControlService.ACCESS_LEVEL_PREREAD, RecordControlService.ACCESS_LEVEL_READONLY, Calendar.class.getName());
		//发送通知
		messageService.removeMessages(calendar.getId());
		String message = "日程安排：" + calendar.getSubject() + "\r\n地点：" + calendar.getAddress();
		SimpleDateFormat formater = new SimpleDateFormat("MM-dd HH:mm");
		message +=  "\r\n时间：" + formater.format(calendar.getBeginTime()) + " 到 " + formater.format(calendar.getEndTime());
		messageService.sendMessageToVisitors(readers, true, message, sessionInfo.getUserId(), sessionInfo.getUserName(), MessageService.MESSAGE_PRIORITY_NORMAL, calendar.getId(), null, null, null, null, 0, null);
		//会前30分钟再通知一次
		Timestamp messageTime = new Timestamp(calendar.getBeginTime().getTime() - 30 * 60000);
		messageService.sendMessageToVisitors(readers, true, message, sessionInfo.getUserId(), sessionInfo.getUserName(), MessageService.MESSAGE_PRIORITY_NORMAL, calendar.getId(), null, messageTime, null, null, 0, null);
		return record;
	}
}