package com.yuanluesoft.j2oa.book.actions.book.borrow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.j2oa.book.pojo.BookBorrow;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class CompleteBorrow extends BorrowAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveComponentAction(mapping, form, "borrow", "borrows", "bookId", "refreshBook", false, request, response);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveComponent(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.lang.Object, java.lang.String, java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void saveComponentRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record mainRecord, Record component, String componentName, String foreignKeyProperty, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		BookBorrow borrow = (BookBorrow)component;
		borrow.setBorrowTime(DateTimeUtils.now());
		super.saveComponentRecord(form, mainRecord, component, componentName, foreignKeyProperty, sessionInfo, request);
	}
}