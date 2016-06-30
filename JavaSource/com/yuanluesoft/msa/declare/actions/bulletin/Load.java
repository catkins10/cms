package com.yuanluesoft.msa.declare.actions.bulletin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.msa.declare.model.Bulletin;
import com.yuanluesoft.msa.declare.service.MsaDeclareService;

/**
 * 读取福建海事局业务申请通告内容
 * @author lmiky
 * 
 */
public class Load extends BaseAction {
	
	public Load() {
		super();
		externalAction = true; //对外页面
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MsaDeclareService msaDeclareService = (MsaDeclareService)getService("msaDeclareService");
		Bulletin bulletin = msaDeclareService.getBulletin(RequestUtils.getParameterStringValue(request, "sourceUrl"));
		if(bulletin==null) {
			response.getWriter().write("This page does not exist or has been deleted.");
			return null;
		}
		request.setAttribute("record", bulletin);
		PageService pageService = (PageService)getService("pageService");
    	pageService.writePage("msa/declare", "bulletin", request, response, false);
    	return null;
    }
}