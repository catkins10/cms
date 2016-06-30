package com.yuanluesoft.im.actions.persontree;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.im.service.IMService;
import com.yuanluesoft.jeaf.dialog.forms.TreeDialog;
import com.yuanluesoft.jeaf.sessionmanage.exception.SessionException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.usermanage.actions.selectperson.SelectPersonAction;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;

/**
 * 
 * @author linchuan
 *
 */
public class PersonTreeAction extends SelectPersonAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.action.BaseAction#getSessionInfo(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public SessionInfo getSessionInfo(HttpServletRequest request, HttpServletResponse response) throws SessionException, SystemUnregistException {
		return RequestUtils.getSessionInfo(request);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.actions.selectperson.SelectPersonAction#listExtendTreeNodes(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List listExtendTreeNodes(TreeDialog dialogForm, long parentDirectoryId, SessionInfo sessionInfo) throws Exception {
		List personNodes = super.listExtendTreeNodes(dialogForm, parentDirectoryId, sessionInfo);
		if(personNodes==null || personNodes.isEmpty()) {
			return personNodes;
		}
		//检查用户是否在线
		IMService imService = (IMService)getService("imService");
		for(Iterator iterator = personNodes.iterator(); iterator.hasNext();) {
			TreeNode personNode = (TreeNode)iterator.next();
			byte status = imService.getPersonStatus(Long.parseLong(personNode.getNodeId()));
			String text = (dialogForm.isMultiSelect() ? "<input type=\"checkbox\" class=\"checkbox\" name=\"select\" value=\"" + personNode.getNodeId() + "\"/>&nbsp;" : "") +
						  personNode.getNodeText() +
						  "&nbsp;<img title=\"" + IMService.IM_PERSON_STATUS_TEXTS[status -  - IMService.IM_PERSON_STATUS_OFFLINE] + "\" src=\"" + Environment.getContextPath() + "/im/icons/" + IMService.IM_PERSON_STATUS_NAMES[status - IMService.IM_PERSON_STATUS_OFFLINE] + ".gif\"/>";
			personNode.setNodeText(text);
		}
		return personNodes;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.usermanage.actions.selectperson.SelectPersonAction#getListChildNodesUrl(com.yuanluesoft.jeaf.dialog.forms.TreeDialog, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected String getListChildNodesUrl(TreeDialog dialogForm, HttpServletRequest request, SessionInfo sessionInfo) {
		return request.getContextPath() + "/im/listChildNodes.shtml?multiSelect=" + dialogForm.isMultiSelect();
	}
}