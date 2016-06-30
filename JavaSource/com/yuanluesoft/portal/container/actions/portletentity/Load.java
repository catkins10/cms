package com.yuanluesoft.portal.container.actions.portletentity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.portal.container.model.PortletDefinition;

/**
 * 
 * @author linchuan
 *
 */
public class Load extends PortletEntityAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward = executeLoadAction(mapping, form, request, response);
        if(forward==null || !"load".equals(forward.getName())) {
        	return forward;
        }
        //获取PORTLET定义
		com.yuanluesoft.portal.container.forms.PortletEntity portletEntityForm = (com.yuanluesoft.portal.container.forms.PortletEntity)form;
		PortletDefinition portletDefinition = getPortletDefinition(portletEntityForm, null);
		//检查是否有定义PORTLET实体URL
		String portletEntityURL = portletDefinition.getInitParameterValue("portletEntityURL");
		if(portletEntityURL!=null) {
			response.sendRedirect(request.getContextPath() + portletEntityURL + (portletEntityURL.indexOf('?')==-1 ? "?" : "&") + request.getQueryString());
			return null;
		}
		return forward;
    }
}