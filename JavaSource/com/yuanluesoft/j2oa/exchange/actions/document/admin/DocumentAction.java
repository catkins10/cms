package com.yuanluesoft.j2oa.exchange.actions.document.admin;

import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yuanluesoft.j2oa.exchange.pojo.ExchangeDocument;
import com.yuanluesoft.j2oa.exchange.pojo.ExchangeDocumentUnit;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.form.actions.FormAction;
import com.yuanluesoft.jeaf.security.service.RecordControlService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class DocumentAction extends FormAction {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.base.actions.BaseAction#getAcl(java.lang.String, org.apache.struts.action.ActionForm, java.lang.Object, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List getAcl(String applicationName, org.apache.struts.action.ActionForm form, Record record, String openMode, SessionInfo sessionInfo) throws Exception {
		List acl = super.getAcl(applicationName, form, record, openMode, sessionInfo);
		ExchangeDocument document = (ExchangeDocument)record;
		if(OPEN_MODE_CREATE.equals(openMode)) {
			//检查用户的发布权限
			if(getOrgService().checkPopedom(sessionInfo.getUnitId(), "sendDocument", sessionInfo)) {
				acl.add("issue"); //新纪录,增加发布权限
			}
		}
		else if(document.getCreatorUnitId()==sessionInfo.getUnitId()) { //文件发布人所在单位
			//检查用户的收发权限
			if(getOrgService().checkPopedom(document.getCreatorUnitId(), "sendDocument", sessionInfo)) {
				acl.add(document.getIssue()=='1' ? "undo" : "issue");
			}
		}
		else if(document.getIssue()=='1') { //已发布
			//获取接收单位
			ExchangeDocumentUnit unit = (ExchangeDocumentUnit)ListUtils.findObjectByProperty(document.getExchangeUnits(), "unitId", new Long(sessionInfo.getUnitId()));
			if(unit!=null && getOrgService().checkPopedom(sessionInfo.getUnitId(), "receiveDocument", sessionInfo)) { //当前用户所在单位在接收单位中,并且有签收权限
				acl.add(unit.getSignTime()==null ? "sign" : "signed");
			}
		}
		return acl;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkLoadPrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.lang.String, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public char checkLoadPrivilege(ActionForm form, HttpServletRequest request, Record record, String openMode, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		if(OPEN_MODE_CREATE.equals(openMode)) {
			if(acl.contains("issue")) {
				return RecordControlService.ACCESS_LEVEL_EDITABLE;
			}
			throw new PrivilegeException();
		}
		if(acl.contains("undo") || acl.contains("issue")) { //发文单位
			return RecordControlService.ACCESS_LEVEL_EDITABLE;
		}
		ExchangeDocument document = (ExchangeDocument)record;
		if(document.getIssue()!='1') { //未发布
			throw new PrivilegeException();
		}
		//已发布,检查是否有签发权限
		if(acl.contains("signed") || acl.contains("sign")) { //签收单位
			return RecordControlService.ACCESS_LEVEL_READONLY;
		}
		throw new PrivilegeException();
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#checkDeletePrivilege(com.yuanluesoft.jeaf.form.ActionForm, javax.servlet.http.HttpServletRequest, java.lang.Object, java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void checkDeletePrivilege(ActionForm form, HttpServletRequest request, Record record, List acl, SessionInfo sessionInfo) throws PrivilegeException, SystemUnregistException {
		super.checkDeletePrivilege(form, request, record, acl, sessionInfo);
		ExchangeDocument document = (ExchangeDocument)record;
		if(document.getIssue()=='1') { //已发布
			throw new PrivilegeException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#loadResource(com.yuanluesoft.jeaf.form.ActionForm, java.lang.Object, java.util.List, char, boolean, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void loadResource(ActionForm form, Record record, List acl, char accessLevel, boolean deleteEnable, String openMode, HttpServletRequest request, SessionInfo sessionInfo) throws Exception {
		super.loadResource(form, record, acl, accessLevel, deleteEnable, openMode, request, sessionInfo);
		ExchangeDocument document = (ExchangeDocument)record;
		com.yuanluesoft.j2oa.exchange.forms.admin.ExchangeDocument exchangeForm = (com.yuanluesoft.j2oa.exchange.forms.admin.ExchangeDocument)form;

		//设置TAB列表
		exchangeForm.getTabs().addTab(-1, "basic", "基本信息", "SUBFORM", true);
		if(document!=null && document.getExchangeUndos()!=null && !document.getExchangeUndos().isEmpty()) {
			exchangeForm.getTabs().addTab(-1, "documentUndoList", "撤销记录", "documentUndoList.jsp", false);
		}
		if(document!=null && document.getExchangeMessages()!=null && !document.getExchangeMessages().isEmpty()) {
			List myMessages;
			if(sessionInfo.getUnitId()==document.getCreatorUnitId()) { //发布单位
				exchangeForm.getTabs().addTab(-1, "exchangeMessageList", "消息反馈", "exchangeMessageList.jsp", false);
			}
			else if((myMessages=ListUtils.getSubListByProperty(document.getExchangeMessages(), "creatorUnitId", new Long(sessionInfo.getUnitId())))!=null && !myMessages.isEmpty()) { //反馈单位
				document.setExchangeMessages(new LinkedHashSet(myMessages));
				exchangeForm.getTabs().addTab(-1, "exchangeMessageList", "消息反馈", "exchangeMessageList.jsp", false);
			}
		}
		if(document!=null && document.getIssue()=='1') { //已发布
			if(acl.contains("sign")) { //签收单位未签收
				exchangeForm.setSubForm("documentSign.jsp");
				return;
			}
			else if(acl.contains("signed")) { //签收单位已签收
				exchangeForm.setSubForm("documentSigned.jsp");
				return;
			}
			else { //发文单位
				exchangeForm.setSubForm(exchangeForm.getSubForm().replaceAll("Edit", "Read"));
			}
		}
		if(document!=null && document.getExchangeUnits()!=null && !document.getExchangeUnits().isEmpty()) {
			exchangeForm.getTabs().addTab(-1, "documentSignList", "签收情况", "documentSignList.jsp", false);
		}
		//撤销发布时设置对话框
		if(exchangeForm.isUndo()) {
			exchangeForm.setInnerDialog("unissueDocument.jsp");
			exchangeForm.setFormTitle("撤销发布");
			exchangeForm.getFormActions().addFormAction(-1, "撤销发布", "doUnissue()", true);
			addReloadAction(form, "取消", request, -1, false);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.form.actions.FormAction#fillForm(com.yuanluesoft.jeaf.form.ActionForm, com.yuanluesoft.jeaf.database.Record, char, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void fillForm(ActionForm form, Record record, char accessLevel, List acl, SessionInfo sessionInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.fillForm(form, record, accessLevel, acl, sessionInfo, request, response);
		com.yuanluesoft.j2oa.exchange.forms.admin.ExchangeDocument exchangeForm = (com.yuanluesoft.j2oa.exchange.forms.admin.ExchangeDocument)form;
		exchangeForm.setCurrentUnit((ExchangeDocumentUnit)ListUtils.findObjectByProperty(exchangeForm.getExchangeUnits(), "unitId", new Long(sessionInfo.getUnitId())));
	}
}