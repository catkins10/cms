package com.yuanluesoft.cms.onlineservice.actions.admin.serviceitemview;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceDirectoryService;
import com.yuanluesoft.cms.onlineservice.service.OnlineServiceItemService;
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
public class MoveServiceItems extends ServiceItemViewAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#executeCutsomAction(org.apache.struts.action.ActionMapping, com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ActionForward executeCutsomAction(ActionMapping mapping, ViewForm viewForm, HttpServletRequest request, HttpServletResponse response, SessionInfo sessionInfo) throws Exception {
		if(viewForm.getViewPackage().getSelectedIds()==null || viewForm.getViewPackage().getSelectedIds().isEmpty()) {
			return mapping.getInputForward();
		}
		boolean remove = "true".equals(request.getParameter("remove"));
		boolean physicalDelete = "true".equals(request.getParameter("physicalDelete"));
		String moveToDirectoryIds = RequestUtils.getParameterStringValue(request, "to");
		if(!remove && !physicalDelete && (moveToDirectoryIds==null || moveToDirectoryIds.isEmpty())) {
			return mapping.getInputForward();
		}
		OnlineServiceDirectoryService onlineServiceDirectoryService = (OnlineServiceDirectoryService)getService("onlineServiceDirectoryService"); //目录服务
		OnlineServiceItemService onlineServiceItemService = (OnlineServiceItemService)getService("onlineServiceItemService"); //办理事项服务
		long fromDirectoryId = RequestUtils.getParameterLongValue(request, "from");
		//获取源目录的子目录ID列表
		String[] fromDirectoryIds = fromDirectoryId==0 ? null : onlineServiceDirectoryService.getAllChildDirectoryIds("" + fromDirectoryId, null).split(",");
		//检查用户对新目录的编辑权限
		String[] directoryIds = (fromDirectoryId + (moveToDirectoryIds==null ? "" : "," + moveToDirectoryIds)).split(",");
		boolean moveabled = false;
		for(int i=0; i<directoryIds.length; i++) {
			moveabled = false;
			List popedoms = onlineServiceDirectoryService.listDirectoryPopedoms(Long.parseLong(directoryIds[i]), sessionInfo); //获取用户对栏目的权限
			if(popedoms.contains("manager") || (!physicalDelete && popedoms.contains("transactor"))) { //管理员或者经办
				moveabled = true;
			}
			if(!moveabled) {
				return mapping.getInputForward();
			}
		}
		String[] ids = viewForm.getViewPackage().getSelectedIds().split(",");
		for(int i=0; i<ids.length; i++) {
			try {
				OnlineServiceItem serviceItem = (OnlineServiceItem)onlineServiceItemService.load(OnlineServiceItem.class, Long.parseLong(ids[i]));
				//写日志
				Logger.action("cms/onlineservice", this.getClass(), (remove ? "remove" : (physicalDelete ? "delete" : "move")), serviceItem, serviceItem.getId(), serviceItem.getName(), sessionInfo.getUserId(), sessionInfo.getUserName(), request);
				if(physicalDelete) { //物理删除
					onlineServiceItemService.delete(serviceItem);
				}
				else {
					//更新隶属目录
					String subjectionDirectoryIds = "," + ListUtils.join(serviceItem.getSubjections(), "directoryId", ",", false) + ",";
					if(fromDirectoryIds==null) {
						subjectionDirectoryIds = (moveToDirectoryIds==null ? "," : "," + moveToDirectoryIds + ",");
					}
					else {
						for(int j=0; j<fromDirectoryIds.length; j++) {
							subjectionDirectoryIds = subjectionDirectoryIds.replace("," + fromDirectoryIds[j] + ",", (moveToDirectoryIds==null ? "," : "," + moveToDirectoryIds + ","));
						}
					}
					if(subjectionDirectoryIds.equals(",")) {
						onlineServiceItemService.delete(serviceItem); //删除
					}
					else {
						subjectionDirectoryIds = subjectionDirectoryIds.substring(1, subjectionDirectoryIds.length() - 1);
						onlineServiceItemService.updateServiceItemSubjectios(serviceItem, false, subjectionDirectoryIds);
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