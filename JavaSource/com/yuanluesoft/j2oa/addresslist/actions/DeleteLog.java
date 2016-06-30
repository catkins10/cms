package com.yuanluesoft.j2oa.addresslist.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.addresslist.forms.AddressListForm;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class DeleteLog extends AddressListAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, true, "log", null, null);
    }
    
    /* (non-Javadoc)
     * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
     */
    public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
        super.saveRecord(form, record, openMode, request, response, sessionInfo);
        BusinessService businessService = (BusinessService)getService("businessService");
        AddressListForm addressListForm = (AddressListForm)form;
        DatabaseService databaseService = (DatabaseService)getService("databaseService");
        businessService.delete((Record)databaseService.findRecordByHql("from AddressLog AddressLog where AddressLog.personId=" + addressListForm.getId() + " and AddressLog.id=" + addressListForm.getLogId()));
        addressListForm.setLogContent(null); //描述
        addressListForm.setLogTime(null); //设置时间
        addressListForm.setLogId(0);
        return record;
    }
}