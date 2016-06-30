package com.yuanluesoft.im.actions.chathistory.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.im.forms.admin.ChatHistory;
import com.yuanluesoft.im.pojo.IMChat;
import com.yuanluesoft.im.pojo.IMChatPerson;
import com.yuanluesoft.im.service.IMService;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.actions.ViewFormAction;
import com.yuanluesoft.jeaf.view.forms.ViewForm;
import com.yuanluesoft.jeaf.view.model.View;

/**
 * 
 * @author linchuan
 *
 */
public class ChatHistoryAction extends ViewFormAction {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewApplicationName(com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest)
	 */
	public String getViewApplicationName(ViewForm viewForm, HttpServletRequest request) {
		return "im";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#getViewName(com.yuanluesoft.jeaf.view.forms.ViewForm, javax.servlet.http.HttpServletRequest)
	 */
	public String getViewName(ViewForm viewForm, HttpServletRequest request) {
		return "admin/chatHistory";
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetView(com.yuanluesoft.jeaf.view.forms.ViewForm, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetView(ViewForm viewForm, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetView(viewForm, view, sessionInfo, request);
		//获取对话
		IMChat chat = getChat(viewForm, request);
		if(chat.getId()>0) {
			IMChatPerson chatPerson = (IMChatPerson)ListUtils.findObjectByProperty(chat.getChatPersons(), "personId", new Long(sessionInfo.getUserId()));
			view.addWhere("IMChatTalk.createdMillis>" + chatPerson.getJoinTime().getTime());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.actions.ViewFormAction#resetViewLocation(com.yuanluesoft.jeaf.view.forms.ViewForm, java.util.List, com.yuanluesoft.jeaf.view.model.View, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, javax.servlet.http.HttpServletRequest)
	 */
	public void resetViewLocation(ViewForm viewForm, List location, View view, SessionInfo sessionInfo, HttpServletRequest request) throws Exception {
		super.resetViewLocation(viewForm, location, view, sessionInfo, request);
		IMChat chat = getChat(viewForm, request);
		if(chat.getId()>0) {
			List chatPersons = new ArrayList(chat.getChatPersons());
			ListUtils.removeObjectByProperty(chatPersons, "personId", new Long(sessionInfo.getUserId()));
			viewForm.getViewPackage().getLocation().add(ListUtils.join(chatPersons, "personName", "、", false));
		}
	}
	
	/**
	 * 获取对话
	 * @param viewForm
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private IMChat getChat(ViewForm viewForm, HttpServletRequest request) throws Exception {
		IMChat chat = (IMChat)request.getAttribute("currentChat");
		if(chat!=null) {
			return chat;
		}
		ChatHistory chatHistory = (ChatHistory)viewForm;
		IMService imService = (IMService)getService("imService");
		chat = chatHistory.getChatId()==0 ? new IMChat() : (IMChat)imService.load(IMChat.class, chatHistory.getChatId());
		request.setAttribute("currentChat", chat);
		return chat;
	}
}