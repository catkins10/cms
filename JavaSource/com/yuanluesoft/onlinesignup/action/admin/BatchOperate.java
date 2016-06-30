package com.yuanluesoft.onlinesignup.action.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.application.action.applicationview.ApplicationViewAction;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.onlinesignup.pojo.admin.SignUp;

public class BatchOperate extends ApplicationViewAction{
	
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		String[] ids = viewForm.getViewPackage().getSelectedIds().split(",");
		delete(ids);
		return mapping.getInputForward();
	}
	
	public void delete(String[] ids) throws SystemUnregistException{
		BusinessService businessService = (BusinessService)getService("businessService");
			try{
				for(int i = 0; i<ids.length; i++){
					SignUp signUp  = (SignUp)businessService.load(SignUp.class, Long.parseLong(ids[i]));
					businessService.delete(signUp);
				}
			}catch(Exception e){
				Logger.info(e);
			}
			
	}
	
}
