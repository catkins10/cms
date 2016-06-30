package com.yuanluesoft.chd.evaluation.actions.directory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationDetail;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationDirectory;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPlantDetail;
import com.yuanluesoft.chd.evaluation.pojo.ChdEvaluationPlantRule;
import com.yuanluesoft.chd.evaluation.service.EvaluationDirectoryService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	externalAction = true;
    	SessionInfo sessionInfo;
    	try {
    		sessionInfo = getSessionInfo(request, response);
    	}
    	catch(SessionException se) {
    		return redirectToLogin(this, mapping, form, request, response, se, false);
    	}
    	long directoryId = RequestUtils.getParameterLongValue(request, "directoryId");
		EvaluationDirectoryService evaluationDirectoryService = (EvaluationDirectoryService)getService("chdEvaluationDirectoryService");
		if(!evaluationDirectoryService.checkPopedom(directoryId, "all", sessionInfo)) {
    		return redirectToLogin(this, mapping, form, request, response, new PrivilegeException(), false);
    	}
		ChdEvaluationDirectory evaluationDirectory = (ChdEvaluationDirectory)evaluationDirectoryService.getDirectory(directoryId);
		request.setAttribute("record", evaluationDirectory);
    	String pageName = "managementRule"; //管理评价
    	if("true".equals(request.getParameter("indicatorData"))) {
    		pageName = "indicatorRule";
    	}
    	else if("plantDetail".equals(evaluationDirectory.getDirectoryType())) { //评价细则
    		if("true".equals(request.getParameter("selfEval"))) { //自查
        		pageName = "selfEval";
        	}
    		else {
	    		ChdEvaluationDetail evaluationDetail = (ChdEvaluationDetail)evaluationDirectoryService.getDirectory(evaluationDirectory.getSourceDirectoryId());
	    		((ChdEvaluationPlantDetail)evaluationDirectory).setPoints(evaluationDetail.getPoints()); //设置评价要点列表
	    		pageName = "detail";
    		}
    	}
    	else if("plantRule".equals(evaluationDirectory.getDirectoryType())) { //评价项目
    		if(((ChdEvaluationPlantRule)evaluationDirectory).getIsIndicator()=='1') { //指标评价
    			pageName = "indicatorRule";
    		}
    		else if("true".equals(request.getParameter("selfEval"))) { //自查
        		pageName = "selfEval";
        	}
    	}
    	else if("true".equals(request.getParameter("selfEval"))) { //自查
    		pageName = "selfEval";
    	}
    	PageService pageService = (PageService)getService("plantPageService");
    	pageService.writePage("chd/evaluation", pageName, request, response, false);
        return null;
    }
}