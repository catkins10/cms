package com.yuanluesoft.jeaf.usermanage.actions.admin.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.forms.admin.Student;
import com.yuanluesoft.jeaf.usermanage.pojo.Genearch;
import com.yuanluesoft.jeaf.usermanage.service.PersonService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class AddGenearch extends StudentAction {
	
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if(!isSecureURL(request)) {
    		throw new Exception();
    	}
    	return executeSaveAction(mapping, form, request, response, true, "genearch", null, null);
    }
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.actions.person.PersonAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		super.saveRecord(form, record, openMode, request, response, sessionInfo);
		PersonService personService = (PersonService)getService("personService");
		//增加家长
		Student studentForm = (Student)form;
		long genearchId = studentForm.getSelectedGenearchId();
		if(studentForm.getRegistNewGenearch()=='1') { //注册新的家长帐号
			Genearch genearch = new Genearch();
			PropertyUtils.copyProperties(genearch, studentForm.getGenearch());
			genearch.setId(UUIDLongGenerator.generateId()); //ID
			genearch.setCreated(DateTimeUtils.now()); //创建时间
			genearch.setCreatorId(sessionInfo.getUserId()); //创建人ID
			genearch.setCreator(sessionInfo.getUserName()); //创建人姓名
			personService.save(genearch); //保存家长
			genearchId = genearch.getId();
		}
		//添加学生家长
		personService.addStudentGenearch(studentForm.getId(), genearchId, studentForm.getGenearchTitle());
		//清空页面字段
		studentForm.setGenearch(new Genearch());
		studentForm.setRegistNewGenearch('1');
		studentForm.setGenearchTitle(null);
		studentForm.setSelectedGenearchId(0);
		studentForm.setSelectedGenearchName(null);
		return record;
	}
}
