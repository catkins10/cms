package com.yuanluesoft.cms.siteresource.actions.admin.article;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.siteresource.forms.Resource;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;

/**
 * 
 * @author yuanluesoft
 *
 */
public class Run extends ArticleAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Resource resourceForm = (Resource)form;
    	if("get".equalsIgnoreCase(request.getMethod()) && resourceForm.getBatchIds()!=null && !resourceForm.getBatchIds().isEmpty()) { //批量处理
    		//获取资源类型
    		SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService");
    		Map resourceTypes = siteResourceService.getResourceTypes(resourceForm.getBatchIds());
    		//按第一个资源的类型,筛选资源ID,并决定是否跳转到链接处理页面
    		String[] resourceIds = resourceForm.getBatchIds().split(",");
    		int resourceType = ((Number)resourceTypes.get(new Long(resourceIds[0]))).intValue();
    		String batchIds = resourceIds[0];
    		for(int i=1; i<resourceIds.length; i++) {
    			if(((Number)resourceTypes.get(new Long(resourceIds[i]))).intValue()==resourceType) {
    				batchIds += "," + resourceIds[i];
    			}
    		}
    		String redirectUrl = null;
    		if(SiteResourceService.RESOURCE_TYPE_LINK==resourceType) { //链接
    			redirectUrl = "runLinkApproval.shtml";
    		}
    		if(redirectUrl!=null) {
    			response.sendRedirect(redirectUrl + "?workflowAction=batchSend&displayMode=dialog&id=" + resourceForm.getId() + "&batchIds=" + batchIds);
				return null;
    		}
    		resourceForm.setBatchIds(batchIds);
    	}
    	return executeRunAction(mapping, form, request, response, false, null, null);
    }
}