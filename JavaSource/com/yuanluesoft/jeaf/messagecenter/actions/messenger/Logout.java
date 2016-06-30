package com.yuanluesoft.jeaf.messagecenter.actions.messenger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.messagecenter.sender.messenger.pojo.MessengerOnline;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;

/**
 * 
 * 
 * @author linchuan
 *
 */
public class Logout extends BaseAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DatabaseService databaseService = (DatabaseService)getBean("databaseService");
        String hql = "from MessengerOnline MessengerOnline";
        hql += " where MessengerOnline.ip='" + JdbcUtils.resetQuot(request.getRemoteHost()) + "'";
        hql += " and MessengerOnline.port=" + RequestUtils.getParameterIntValue(request, "port");
        MessengerOnline pojoMessengerOnline = (MessengerOnline)databaseService.findRecordByHql(hql);
        databaseService.deleteRecord(pojoMessengerOnline);
        return null;
    }
}