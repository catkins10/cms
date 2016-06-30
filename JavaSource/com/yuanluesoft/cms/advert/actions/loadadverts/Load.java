package com.yuanluesoft.cms.advert.actions.loadadverts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.advert.forms.LoadAdverts;
import com.yuanluesoft.cms.advert.service.AdvertService;
import com.yuanluesoft.jeaf.action.BaseAction;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends BaseAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	LoadAdverts loadAdvertsForm = (LoadAdverts)form;
    	AdvertService advertService = (AdvertService)getService("advertService");
    	advertService.loadFloatAdvertsAsJs(loadAdvertsForm.getSiteId(), loadAdvertsForm.getApplicationName(), loadAdvertsForm.getPageName(), request, response);
    	return null;
    }
}