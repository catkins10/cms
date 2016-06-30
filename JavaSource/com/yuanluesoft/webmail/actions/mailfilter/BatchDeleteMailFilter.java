package com.yuanluesoft.webmail.actions.mailfilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.webmail.actions.view.WebmailViewAction;
import com.yuanluesoft.webmail.pojo.MailFilter;
import com.yuanluesoft.webmail.service.MailFilterService;

/**
 * 
 * @author linchuan
 *
 */
public class BatchDeleteMailFilter extends WebmailViewAction {
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#executeCutsomAction(org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		String selectedIds = viewForm.getViewPackage().getSelectedIds();
        if(selectedIds!=null && !selectedIds.equals("")) { //删除选中的邮件
            String[] ids = selectedIds.split(",");
            MailFilterService mailFilterService = (MailFilterService)getService("mailFilterService");
            try {
                for(int i=0; i<ids.length; i++) { //执行删除操作,TODO 加入删除日志
                    mailFilterService.delete(mailFilterService.load(MailFilter.class, Long.parseLong(ids[i])));
                }
            }
            finally {
                viewForm.getViewPackage().setSelectedIds(null); //置空,必须执行
            }
        }
        return mapping.getInputForward();
    }
}