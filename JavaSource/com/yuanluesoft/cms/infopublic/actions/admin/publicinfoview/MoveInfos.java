package com.yuanluesoft.cms.infopublic.actions.admin.publicinfoview;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.yuanluesoft.cms.infopublic.pojo.PublicInfo;
import com.yuanluesoft.cms.infopublic.service.PublicDirectoryService;
import com.yuanluesoft.cms.infopublic.service.PublicInfoService;
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
public class MoveInfos extends PublicInfoViewAction {
	
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
		PublicDirectoryService publicDirectoryService = (PublicDirectoryService)getService("publicDirectoryService"); //信息公开服务
		PublicInfoService publicInfoService = (PublicInfoService)getService("publicInfoService"); //信息服务
		long fromDirectoryId = RequestUtils.getParameterLongValue(request, "from");
		//获取源目录的子目录ID列表
		String[] fromDirectoryIds = fromDirectoryId==0 ? null : publicDirectoryService.getAllChildDirectoryIds("" + fromDirectoryId, null).split(",");
		//检查用户对新目录的编辑权限
		String[] columnIds = (fromDirectoryId + (moveToDirectoryIds==null ? "" : "," + moveToDirectoryIds)).split(",");
		boolean moveabled = false;
		for(int i=0; i<columnIds.length; i++) {
			moveabled = false;
			List popedoms = publicDirectoryService.listDirectoryPopedoms(Long.parseLong(columnIds[i]), sessionInfo); //获取用户对栏目的权限
			if(popedoms.contains("manager")) { //管理员
				moveabled = true;
			}
			else if(!physicalDelete && popedoms.contains("editor")) { //编辑
				if(remove) {
					moveabled = publicDirectoryService.isEditorDeleteable(Long.parseLong(columnIds[i])); //检查是否允许删除
				}
				else  {
					moveabled = publicDirectoryService.isEditorReissueable(Long.parseLong(columnIds[i])); //检查是否允许重新发布
				}
			}
			if(!moveabled) {
				return mapping.getInputForward();
			}
		}
		String[] ids = viewForm.getViewPackage().getSelectedIds().split(",");
		for(int i=0; i<ids.length; i++) {
			try {
				PublicInfo info = (PublicInfo)publicInfoService.load(PublicInfo.class, Long.parseLong(ids[i]));
				//写日志
				Logger.action("cms/infopublic", this.getClass(), (remove ? "remove" : (physicalDelete ? "delete" : "move")), info, info.getId(), info.getSubject(), sessionInfo.getUserId(), sessionInfo.getUserName(), request);
				if(physicalDelete) { //物理删除
					publicDirectoryService.delete(info);
				}
				else {
					//更新信息隶属目录
					String subjectionDirectoryIds = "," + ListUtils.join(info.getSubjections(), "directoryId", ",", false) + ",";
					if(fromDirectoryIds==null) {
						subjectionDirectoryIds = (moveToDirectoryIds==null ? "," : "," + moveToDirectoryIds + ",");
					}
					else  {
						for(int j=0; j<fromDirectoryIds.length; j++) {
							subjectionDirectoryIds = subjectionDirectoryIds.replace("," + fromDirectoryIds[j] + ",", (moveToDirectoryIds==null ? "," : "," + moveToDirectoryIds + ","));
						}
					}
					if(subjectionDirectoryIds.equals(",")) {
						if(publicInfoService.isLogicalDelete()) { //逻辑删除
							info.setStatus((char)(PublicInfoService.INFO_STATUS_DELETED + (info.getStatus() - '0')));
							publicInfoService.update(info);
						}
						else {
							publicInfoService.delete(info);
						}
					}
					else {
						publicInfoService.updateInfoSubjections(info, false, subjectionDirectoryIds.substring(1, subjectionDirectoryIds.length() - 1));
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