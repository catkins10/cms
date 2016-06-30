package com.yuanluesoft.cms.siteresource.actions.admin.resourceview;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.service.SiteResourceService;
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
public class ImportResources extends ResourceViewAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#executeCutsomAction(org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(viewForm.getViewPackage().getSelectedIds()==null || viewForm.getViewPackage().getSelectedIds().isEmpty()) {
			return mapping.getInputForward();
		}
		String importColumnIds = RequestUtils.getParameterStringValue(request, "to");
		if(importColumnIds==null || importColumnIds.isEmpty()) {
			return mapping.getInputForward();
		}
		SiteService siteService = (SiteService)getService("siteService"); //站点服务
		SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService"); //站点资源服务
		//检查用户对新栏目的编辑权限
		String[] columnIds = importColumnIds.split(",");
		boolean editable = false;
		for(int i=0; i<columnIds.length; i++) {
			editable = false;
			List popedoms = siteService.listDirectoryPopedoms(Long.parseLong(columnIds[i]), sessionInfo); //获取用户对栏目的权限
			if(popedoms.contains("manager")) { //管理员
				editable = true;
			}
			else if(popedoms.contains("editor")) { //编辑
				editable = siteService.isEditorReissueable(Long.parseLong(columnIds[i])); //检查是否允许重新发布
			}
			if(!editable) {
				return mapping.getInputForward();
			}
		}
		String[] ids = viewForm.getViewPackage().getSelectedIds().split(",");
		for(int i=0; i<ids.length; i++) {
			try {
				SiteResource resource = (SiteResource)siteResourceService.load(SiteResource.class, Long.parseLong(ids[i]));
				//写日志
				Logger.action("cms/siteresource", this.getClass(), "import", resource, resource.getId(), resource.getSubject(), sessionInfo.getUserId(), sessionInfo.getUserName(), request);
				//更新文章隶属目录
				siteResourceService.updateSiteResourceSubjections(resource, false, ListUtils.join(resource.getSubjections(), "siteId", ",", false) + "," + importColumnIds);
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
		viewForm.getViewPackage().setSelectedIds(null); //清空列表
		return mapping.getInputForward();
	}
}