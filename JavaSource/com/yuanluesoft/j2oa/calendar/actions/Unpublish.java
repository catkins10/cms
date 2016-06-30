package com.yuanluesoft.j2oa.calendar.actions;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.calendar.pojo.Calendar;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.messagecenter.service.MessageService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Unpublish extends CalendarAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, false, null, "撤销成功！", null);
    }
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.core.actions.form.FormAction#saveRecord(com.yuanluesoft.jeaf.core.forms.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
	    Calendar calendar = (Calendar)record;
		calendar.setPublish('0');
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		MessageService messageService = (MessageService)getService("messageService"); //消息服务
		List preReaders = getRecordControlService().listVisitors(calendar.getId(), Calendar.class.getName(), RecordControlService.ACCESS_LEVEL_PREREAD);
		if(preReaders!=null && !preReaders.isEmpty()) {
			messageService.removeMessages(calendar.getId());
			String message = "取消日程：" + calendar.getSubject() + "\r\n地点：" + calendar.getAddress();
			SimpleDateFormat formater = new SimpleDateFormat("MM-dd HH:mm");
			message +=  "\r\n时间：" + formater.format(calendar.getBeginTime()) + " 到 " + formater.format(calendar.getEndTime());
			//发送通知
			messageService.sendMessageToVisitors(preReaders, true, message, sessionInfo.getUserId(), sessionInfo.getUserName(), MessageService.MESSAGE_PRIORITY_NORMAL, calendar.getId(), null, null, null, null, 0, null);
		}
		getRecordControlService().removeVisitors(calendar.getId(), Calendar.class.getName(), RecordControlService.ACCESS_LEVEL_READONLY); //删除记录的读者
		return record;
	}
}