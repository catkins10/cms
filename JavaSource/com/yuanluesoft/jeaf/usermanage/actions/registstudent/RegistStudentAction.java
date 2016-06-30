package com.yuanluesoft.jeaf.usermanage.actions.registstudent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.actions.registperson.RegistPersonAction;
import com.yuanluesoft.jeaf.usermanage.pojo.RegistStudent;

/**
 * 
 * @author linchuan
 *
 */
public class RegistStudentAction extends RegistPersonAction {
	
	public RegistStudentAction() {
		super();
		forceValidateCode = true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		//不保存,写入共享缓存
		Cache cache = (Cache)getService("shareCache");
		RegistStudent registStudent = (RegistStudent)record;
		cache.put(new Long(registStudent.getId()), registStudent);
		//把ID写入内存,给问卷使用
		request.setAttribute("STUDENT_REGIST_ID", new Long(registStudent.getId()));
		return record;
	}
}
