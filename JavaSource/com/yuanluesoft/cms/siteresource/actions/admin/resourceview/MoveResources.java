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
public class MoveResources extends ResourceViewAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#executeCutsomAction(org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(viewForm.getViewPackage().getSelectedIds()==null || viewForm.getViewPackage().getSelectedIds().isEmpty()) {
			return mapping.getInputForward();
		}
		boolean remove = "true".equals(request.getParameter("remove"));
		boolean physicalDelete = "true".equals(request.getParameter("physicalDelete"));
		String moveToColumnIds = RequestUtils.getParameterStringValue(request, "to");
		if(!remove && !physicalDelete && (moveToColumnIds==null || moveToColumnIds.isEmpty())) {
			return mapping.getInputForward();
		}
		SiteService siteService = (SiteService)getService("siteService"); //站点服务
		SiteResourceService siteResourceService = (SiteResourceService)getService("siteResourceService"); //站点资源服务
		long fromColumnId = RequestUtils.getParameterLongValue(request, "from");
		//获取源栏目的子目录ID列表
		String[] fromColumnIds = fromColumnId==0 ? null : siteService.getAllChildDirectoryIds("" + fromColumnId, null).split(",");
		//检查用户对新栏目的编辑权限
		String[] columnIds = (fromColumnId + (moveToColumnIds==null ? "" : "," + moveToColumnIds)).split(",");
		boolean moveabled = false;
		for(int i=0; i<columnIds.length; i++) {
			moveabled = false;
			List popedoms = siteService.listDirectoryPopedoms(Long.parseLong(columnIds[i]), sessionInfo); //获取用户对栏目的权限
			if(popedoms.contains("manager")) { //管理员
				moveabled = true;
			}
			else if(!physicalDelete && popedoms.contains("editor")) { //编辑
				if(remove) {
					moveabled = siteService.isEditorDeleteable(Long.parseLong(columnIds[i])); //检查是否允许删除
				}
				else {
					moveabled = siteService.isEditorReissueable(Long.parseLong(columnIds[i])); //检查是否允许重新发布
				}
			}
			if(!moveabled) {
				return mapping.getInputForward();
			}
		}
		String[] ids = viewForm.getViewPackage().getSelectedIds().split(",");
		for(int i=0; i<ids.length; i++) {
			try {
				SiteResource resource = (SiteResource)siteResourceService.load(SiteResource.class, Long.parseLong(ids[i]));
				//写日志
				Logger.action("cms/siteresource", this.getClass(), (remove ? "remove" : (physicalDelete ? "delete" : "move")), resource, resource.getId(), resource.getSubject(), sessionInfo.getUserId(), sessionInfo.getUserName(), request);
				if(physicalDelete) { //物理删除
					siteResourceService.delete(resource);
				}
				else {
					//更新文章隶属目录
					String subjectionDirectoryIds = "," + ListUtils.join(resource.getSubjections(), "siteId", ",", false) + ",";
					if(fromColumnIds==null) {
						subjectionDirectoryIds = (moveToColumnIds==null ? "," : "," + moveToColumnIds + ",");
					}
					else {
						for(int j=0; j<fromColumnIds.length; j++) {
							subjectionDirectoryIds = subjectionDirectoryIds.replace("," + fromColumnIds[j] + ",", (moveToColumnIds==null ? "," : "," + moveToColumnIds + ","));
						}
					}
					if(subjectionDirectoryIds.equals(",")) {
						if(siteResourceService.isLogicalDelete()) { //逻辑删除
							resource.setStatus((char)(SiteResourceService.RESOURCE_STATUS_DELETED + (resource.getStatus() - '0')));
							siteResourceService.update(resource);
						}
						else {
							siteResourceService.delete(resource);
						}
					}
					else {
						subjectionDirectoryIds = subjectionDirectoryIds.substring(1, subjectionDirectoryIds.length() - 1);
						siteResourceService.updateSiteResourceSubjections(resource, false, subjectionDirectoryIds);
					}
				}
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
		viewForm.getViewPackage().setSelectedIds(null); //清空列表
		return mapping.getInputForward();
	}
}