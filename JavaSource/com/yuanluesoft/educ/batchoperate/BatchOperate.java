package com.yuanluesoft.educ.batchoperate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


import com.yuanluesoft.educ.student.pojo.Stude;
import com.yuanluesoft.educ.student.service.StudentService;
import com.yuanluesoft.educ.teach.pojo.Teach;
import com.yuanluesoft.jeaf.application.action.applicationview.ApplicationViewAction;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.view.forms.ViewForm;

public class BatchOperate extends ApplicationViewAction{
	
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		String pojoName = request.getParameter("pojoName");
		String[] ids = viewForm.getViewPackage().getSelectedIds().split(",");
		delete(pojoName, ids);
		return mapping.getInputForward();
	}
	
	public void delete(String pojoName,String[] ids) throws SystemUnregistException{
		BusinessService businessService = (BusinessService)getService("businessService");
		StudentService studentService = (StudentService)getService("studentService");
		if(pojoName.equals("Stude")){//学生信息表
			try{
				for(int i = 0; i<ids.length; i++){
					Stude student  = (Stude)businessService.load(Stude.class, Long.parseLong(ids[i]));
					studentService.delete(student);
				}
			}catch(Exception e){
				Logger.info(e);
			}
		}else if(pojoName.equals("Teach")){//学生信息表
				try{
					for(int i = 0; i<ids.length; i++){
						Teach teach  = (Teach)businessService.load(Teach.class, Long.parseLong(ids[i]));
						studentService.delete(teach);
					}
				}catch(Exception e){
					Logger.info(e);
				}
		}
			
	}
	
}
