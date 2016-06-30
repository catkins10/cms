package com.yuanluesoft.enterprise.exam.actions.exampaper.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.enterprise.exam.pojo.ExamPaper;
import com.yuanluesoft.enterprise.exam.service.ExamService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;

/**
 * 
 * @author linchuan
 *
 */
public class Release extends ExamPaperAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return executeSaveAction(mapping, form, request, response, false, null, "发布成功", null);
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.enterprise.exam.actions.exampaper.admin.ExamPaperAction#saveRecord(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.database.Record, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, java.lang.String)
	 */
	public Record saveRecord(com.yuanluesoft.jeaf.form.ActionForm form, Record record, String openMode, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		ExamPaper examPaper = (ExamPaper)super.saveRecord(form, record, openMode, request, response, sessionInfo);
		ExamService examService = (ExamService)getService("examService");
		examService.releaseExamPaper(examPaper, sessionInfo); //发布试卷
		return examPaper;
	}
}