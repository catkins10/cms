package com.yuanluesoft.jeaf.usermanage.actions.myschool.registcode;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.jeaf.usermanage.forms.SchoolRegistCode;
import com.yuanluesoft.jeaf.usermanage.service.RegistPersonService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.lock.service.LockException;
import com.yuanluesoft.jeaf.lock.service.LockService;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;

/**
 * 
 * @author linchuan
 *
 */
public class SchoolRegistCodeAction extends FormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		throw new PrivilegeException(); //不需要删除
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		return RecordControlService.ACCESS_LEVEL_EDITABLE; //权限控制交给load方法
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkSavePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkSavePrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		try {
			if(getSchoolId(sessionInfo)!=((com.yuanluesoft.jeaf.usermanage.pojo.SchoolRegistCode)record).getSchoolId()) {
				throw new PrivilegeException(); //不是学校管理员
			}
		} catch (Exception e) {
			throw new PrivilegeException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#load(org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public Record load(org.apache.struts.action.ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//会话检查
		SessionInfo sessionInfo = getSessionInfo(request, response);
		long schoolId = getSchoolId(sessionInfo);
		if(schoolId==0) {
			throw new PrivilegeException();
		}
		SchoolRegistCode registCodeForm = (SchoolRegistCode)form;
		com.yuanluesoft.jeaf.usermanage.pojo.SchoolRegistCode registCode = ((RegistPersonService)getService("registPersonService")).retireveSchoolRegistCode(schoolId);
		if(registCode==null) { //初始化表单
			registCodeForm.setSchoolId(schoolId);
			initForm(registCodeForm, null, sessionInfo, request, response);
		}
		else { //填充数据库记录到表单
			fillForm(registCodeForm, registCode, RecordControlService.ACCESS_LEVEL_EDITABLE, null, sessionInfo, request, response);
			registCodeForm.setAct("edit");
			//加锁记录
			LockService lockService = (LockService)getService("lockService");
			try {
				lockService.lock("" + registCodeForm.getId(), sessionInfo.getUserId(), sessionInfo.getUserName());
				registCodeForm.setLocked(true);
			}
			catch(LockException rle) {
				rle.printStackTrace();
				//加锁不成功
				registCodeForm.setPrompt(lockService.getLockPersonName("" + registCodeForm.getId()) + "正在处理当前记录！");
			}
		}
		return registCode;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		com.yuanluesoft.jeaf.usermanage.pojo.SchoolRegistCode registCode = (com.yuanluesoft.jeaf.usermanage.pojo.SchoolRegistCode)record;
		registCode.setAuthorId(sessionInfo.getUserId());
		registCode.setAuthorName(sessionInfo.getUserName());
		return super.saveRecord(form, record, openMode, request, response, sessionInfo);
	}

	/**
	 * 获取用户任管理员的学校的ID
	 * @param sessionInfo
	 * @return
	 * @throws Exception
	 */
	private long getSchoolId(SessionInfo sessionInfo) throws Exception {
		//获取用户任管理员的学校
		List schools = getOrgService().listManagedSchools(sessionInfo.getUserId());
		if(schools==null || schools.isEmpty()) {
			return 0;
		}
		return ((Org)schools.get(0)).getId();
	}
}
