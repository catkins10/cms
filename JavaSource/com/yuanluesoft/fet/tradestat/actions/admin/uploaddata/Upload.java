package com.yuanluesoft.fet.tradestat.actions.admin.uploaddata;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.fet.tradestat.forms.admin.UploadData;
import com.yuanluesoft.fet.tradestat.pojo.UploadTradeData;
import com.yuanluesoft.fet.tradestat.service.TradeStatService;
import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Upload extends UploadDataAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, false, null, null, null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		UploadData uploadDataForm = (UploadData)form;
		UploadTradeData uploadTradeData = (UploadTradeData)record;
		if(uploadDataForm.getUpload()!=null && uploadDataForm.getUpload().getFileSize()>0) {
			uploadTradeData.setTransactor(sessionInfo.getUserName());
			uploadTradeData.setTransactorId(sessionInfo.getUserId());
			uploadTradeData.setUploadTime(DateTimeUtils.now());
		}
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		//上传和解析文件
		if(uploadDataForm.getUpload()!=null && uploadDataForm.getUpload().getFileSize()>0) {
			AttachmentService attachmentService = (AttachmentService)getService("attachmentService");
			//删除原有文件
			attachmentService.deleteAll(form.getFormDefine().getApplicationName(), "data", uploadDataForm.getId());
			//上传新文件
			String dataFileName = attachmentService.upload(form.getFormDefine().getApplicationName(), "data", null, uploadDataForm.getId(), uploadDataForm.getUpload());
			//解析数据文件
			TradeStatService tradeStatService = (TradeStatService)getService("tradeStatService");
			tradeStatService.importData(dataFileName, uploadDataForm.getIsExport()=='1', uploadDataForm.getDataYear(), uploadDataForm.getDataMonth());
		}
		return record;
	}
}