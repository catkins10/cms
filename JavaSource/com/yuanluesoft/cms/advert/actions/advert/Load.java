package com.yuanluesoft.cms.advert.actions.advert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.advert.service.AdvertService;
import com.yuanluesoft.jeaf.action.BaseAction;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AdvertService advertService = (AdvertService)getService("advertService");
        response.sendRedirect(advertService.openAdvert(RequestUtils.getParameterLongValue(request, "advertPutId")));
    	return null;
    }
}