package com.yuanluesoft.bidding.project.signup.actions.admin.uploadtransactions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.bidding.project.signup.forms.admin.UploadTransactions;
import com.yuanluesoft.bidding.project.signup.service.BiddingService;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Process extends UploadTransactionsAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, false, null, "处理完毕", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		AttachmentService attachmentService = (AttachmentService)getService("attachmentService");
		List attachments = attachmentService.list("bidding/project/signup", "attachment", form.getId(), false, 0, request);
		if(attachments!=null && !attachments.isEmpty()) {
			Attachment attachment = (Attachment)attachments.get(0);
			BiddingService biddingService = (BiddingService)getService("biddingService");
			try {
				biddingService.processBankTransactions(((UploadTransactions)form).getPaymentMethod(), attachment.getFilePath(), sessionInfo);
			}
			finally {
				attachmentService.deleteAll("bidding/project/signup", "attachment", form.getId());
			}
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
}