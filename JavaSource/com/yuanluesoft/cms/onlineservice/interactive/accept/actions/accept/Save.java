package com.yuanluesoft.cms.onlineservice.interactive.accept.actions.accept;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.onlineservice.interactive.accept.forms.Accept;
import com.yuanluesoft.cms.onlineservice.interactive.accept.pojo.AcceptSerialNumberConfig;
import com.yuanluesoft.cms.publicservice.service.PublicService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.numeration.service.NumerationCallback;
import com.yuanluesoft.jeaf.numeration.service.NumerationService;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;

/**
 * 
 * @author linchuan
 *
 */
public class Save extends AcceptAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	final Accept acceptForm = (Accept)form;
    	if("new".equals(acceptForm.getStep())) { //填写基本信息
			List errors = validateForm(acceptForm, forceValidateCode, request); //校验
			if(errors!=null && !errors.isEmpty()) {
				acceptForm.setErrors(errors);
			}
			else {
//				生成编号
				if((acceptForm.getSn()==null || acceptForm.getSn().equals(""))&&acceptForm.getItemName()!=null) {
					

					DatabaseService databaseService = (DatabaseService)getService("databaseService");
					AcceptSerialNumberConfig acceptSerialNumberConfig=(AcceptSerialNumberConfig) databaseService.findRecordByHql("from AcceptSerialNumberConfig  AcceptSerialNumberConfig");
				    if(acceptSerialNumberConfig!=null&&acceptSerialNumberConfig.getContent()!=null&&acceptSerialNumberConfig.getContent().length()!=0){
				    	NumerationCallback numerationCallback = new NumerationCallback() {
							public Object getFieldValue(String fieldName, int fieldLength) {
								if("办事事项".equals(fieldName)) {
									return acceptForm.getItemName();
								}else {
									return null;
								}
							}
						};
						NumerationService numerationService=(NumerationService)getService("numerationService");
						String serialNumber=numerationService.generateNumeration("办事受理", "受理编号", acceptSerialNumberConfig.getContent(), false, numerationCallback);
						serialNumber.trim();
						acceptForm.setSn(serialNumber);
				    }else{
				    	PublicService publicService = (PublicService)getService("publicService");
				    	acceptForm.setSn(publicService.getSN());
				    }
					
				}
				acceptForm.setStep("uploadMaterial"); //上传材料
			}
			try {
				load(form, request, response);
			}
			catch(SessionException se) {
				return redirectToLogin(this, mapping, form, request, response, se, true);
			}
			return mapping.getInputForward();
		}
		return executeSaveAction(mapping, form, request, response, false, null, null, null);
    }
}