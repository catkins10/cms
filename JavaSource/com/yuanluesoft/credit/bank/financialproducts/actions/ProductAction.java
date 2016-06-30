package com.yuanluesoft.credit.bank.financialproducts.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.credit.bank.financialproducts.pojo.Product;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;

/**
 * 
 * @author zyh
 *
 */
public class ProductAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(record!=null ){
			Product product = (Product)record;
			if(product.getCreatorId()!=0 && product.getCreatorId()!=sessionInfo.getUserId() ){
				return RecordControlService.ACCESS_LEVEL_READONLY;
			}
		}
		return RecordControlService.ACCESS_LEVEL_EDITABLE;
	}
	
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		// TODO 自动生成方法存根
		AttachmentService attachmentService=(AttachmentService) getService("attachmentService");
		try{
			Product product = (Product)record;
			List attachments=attachmentService.list("credit/bank", "logoImages", form.getId(),false, 1, request);
			if(attachments!=null&&!attachments.isEmpty()){
				Attachment attachment=(Attachment) attachments.get(0);
				product.setLogoImage(attachment.getName());
			}
		}catch(Exception e){
			if(e instanceof ValidateException){
				throw e;
			}
			Logger.exception(e);
		}
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}
	

	
}