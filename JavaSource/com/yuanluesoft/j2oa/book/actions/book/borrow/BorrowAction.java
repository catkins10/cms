package com.yuanluesoft.j2oa.book.actions.book.borrow;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.book.actions.book.BookAction;
import com.yuanluesoft.j2oa.book.forms.Borrow;
import com.yuanluesoft.j2oa.book.pojo.BookBorrow;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class BorrowAction extends BookAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadComponentResource(com.yuanluesoft.jeaf.form.ActionForm, org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.database.Record, java.lang.String, java.lang.String, java.util.List, char, boolean, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadComponentResource(ActionForm form, ActionMapping mapping, Record record, Record component, String componentName, List acl, char accessLevel, boolean deleteEnable, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadComponentResource(form, mapping, record, component, componentName, acl, accessLevel, deleteEnable, request, sessionInfo);
		Borrow borrowForm = (Borrow)form;
		BookBorrow borrow = (BookBorrow)component;
		if(borrow!=null && borrow.getBorrowTime()!=null) {
			borrowForm.getFormActions().removeFormAction("借阅");
		}
		if(borrow==null || borrow.getBorrowTime()==null || borrow.getIsReturned()=='1') {
			borrowForm.getFormActions().removeFormAction("归还");
		}
	}
}