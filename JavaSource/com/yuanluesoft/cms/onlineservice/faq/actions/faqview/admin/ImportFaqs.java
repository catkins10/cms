package com.yuanluesoft.cms.onlineservice.faq.actions.faqview.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.onlineservice.faq.pojo.OnlineServiceFaq;
import com.yuanluesoft.cms.onlineservice.faq.service.OnlineServiceFaqService;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 * 
 * @author linchuan
 *
 */
public class ImportFaqs extends FaqViewAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#executeCutsomAction(org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(viewForm.getViewPackage().getSelectedIds()==null || viewForm.getViewPackage().getSelectedIds().isEmpty()) {
			return mapping.getInputForward();
		}
		String importDirectoryIds = RequestUtils.getParameterStringValue(request, "to");
		if(importDirectoryIds==null || importDirectoryIds.isEmpty()) {
			return mapping.getInputForward();
		}
		OnlineServiceDirectoryService onlineServiceDirectoryService = (OnlineServiceDirectoryService)getService("onlineServiceFaqDirectoryService");
		//检查用户对新栏目的编辑权限
		String[] directoryIds = importDirectoryIds.split(",");
		for(int i=0; i<directoryIds.length; i++) {
			//获取用户对目录的权限
			List popedoms = onlineServiceDirectoryService.listDirectoryPopedoms(Long.parseLong(directoryIds[i]), sessionInfo);
			if(popedoms==null || (!popedoms.contains("manager") && !popedoms.contains("transactor"))) {
				return mapping.getInputForward();
			}
		}
		OnlineServiceFaqService onlineServiceFaqService = (OnlineServiceFaqService)getService("onlineServiceFaqService");
		String[] ids = viewForm.getViewPackage().getSelectedIds().split(",");
		for(int i=0; i<ids.length; i++) {
			try {
				OnlineServiceFaq faq = (OnlineServiceFaq)onlineServiceFaqService.load(OnlineServiceFaq.class, Long.parseLong(ids[i]));
				//写日志
				Logger.action("cms/onlineservice/faq", this.getClass(), "import", faq, faq.getId(), faq.getQuestion(), sessionInfo.getUserId(), sessionInfo.getUserName(), request);
				//更新文章隶属目录
				onlineServiceFaqService.updateFaqSubjectios(faq, false, ListUtils.join(faq.getSubjections(), "directoryId", ",", false) + "," + importDirectoryIds, null, null);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
		viewForm.getViewPackage().setSelectedIds(null); //清空列表
		return mapping.getInputForward();
	}
}