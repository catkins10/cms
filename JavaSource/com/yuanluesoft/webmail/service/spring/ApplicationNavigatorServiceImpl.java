package com.yuanluesoft.webmail.service.spring;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.application.model.navigator.applicationtree.ApplicationTreeNavigator;
import com.yuanluesoft.jeaf.application.model.navigator.definition.ApplicationNavigatorDefinition;
import com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.webmail.pojo.Mailbox;
import com.yuanluesoft.webmail.service.MailboxService;

/**
 * 应用导航
 * @author linchuan
 *
 */
public class ApplicationNavigatorServiceImpl implements ApplicationNavigatorService {
	private MailboxService mailboxService; //邮箱服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService#getApplicationNavigator(java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public com.yuanluesoft.jeaf.application.model.navigator.ApplicationNavigator getApplicationNavigator(String applicationName, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		Tree tree = new Tree("webmail", "电子邮件", "root", Environment.getWebApplicationUrl() + "/webmail/icon/webmail.gif");
		tree.getRootNode().setExtendPropertyValue("dataUrl", Environment.getWebApplicationUrl() + "/webmail/openview.shtml?viewName=inbox&mailboxId=" + MailboxService.MAILBOX_INBOX_ID);
		
		//加入"收邮件"
		tree.appendChildNode("receive", "收邮件", "node", Environment.getWebApplicationUrl() + "/webmail/icon/receive.gif", false).setExtendPropertyValue("dataUrl", Environment.getWebApplicationUrl() + "/webmail/openview.shtml?viewName=inbox&mailboxId=" + MailboxService.MAILBOX_INBOX_ID);
		
		//加入"发邮件"
		tree.appendChildNode("send", "发邮件", "node", Environment.getWebApplicationUrl() + "/webmail/icon/send.gif", false).setExtendPropertyValue("dataUrl", "javascript:PageUtils.openurl('" + Environment.getWebApplicationUrl() + "/webmail/mail.shtml', 'width=780,height=560', 'mail')");
		
		//加入"邮箱"
		TreeNode mailboxNode = tree.appendChildNode("mailboxes", "邮箱", "node", Environment.getWebApplicationUrl() + "/webmail/icon/mailbox.gif", true);
		mailboxNode.setExpandTree(true);
		List mailBoxes = mailboxService.listMailboxes(false, true, sessionInfo); 
		for(Iterator iterator = mailBoxes.iterator(); iterator.hasNext();) {
			Mailbox mailbox = (Mailbox)iterator.next();
			String viewName;
			if(mailbox.getId()==MailboxService.MAILBOX_OUTBOX_ID) {
				viewName = "outbox";
			}
			else if(mailbox.getId()==MailboxService.MAILBOX_DRAFT_ID) {
				viewName = "draftbox";
			}
			else {
				viewName = "inbox";
			}
			String url = Environment.getWebApplicationUrl() + "/webmail/openview.shtml?viewName=" + viewName + "&mailboxId=" + mailbox.getId();
			TreeNode node = mailboxNode.appendChildNode("mailbox_" + mailbox.getId(), mailbox.getMailboxName() + (mailbox.getNewMailCount()==0 ? "" : "(" + mailbox.getNewMailCount() + ")"), "node", Environment.getWebApplicationUrl() + "/webmail/icon/mailbox.gif", false);
			node.setExtendPropertyValue("dataUrl", url);
		}

		//加入"通讯录"
		TreeNode addressListNode = tree.appendChildNode("addresslist", "通讯录", "node", Environment.getWebApplicationUrl() + "/webmail/icon/person.gif", true);
		//addressListNode.setExpandTree(true);
		addressListNode.appendChildNode("personalAddresslist", "个人通讯录", "node", Environment.getWebApplicationUrl() + "/webmail/icon/person.gif", false).setExtendPropertyValue("dataUrl", Environment.getWebApplicationUrl() + "/jeaf/application/applicationView.shtml?applicationName=j2oa/addresslist&viewName=personalAddresslist");
		addressListNode.appendChildNode("commonAddresslist", "公共通讯录", "node", Environment.getWebApplicationUrl() + "/webmail/icon/person.gif", false).setExtendPropertyValue("dataUrl", Environment.getWebApplicationUrl() + "/jeaf/application/applicationView.shtml?applicationName=j2oa/addresslist&viewName=commonAddresslist");
	
		//加入"设置"
		TreeNode configNode = tree.appendChildNode("config", "设置", "node", Environment.getWebApplicationUrl() + "/webmail/icon/config.gif", true);
		//configNode.setExpandTree(true);
		configNode.appendChildNode("filterConfig", "邮件过滤", "node", Environment.getWebApplicationUrl() + "/webmail/icon/config.gif", false).setExtendPropertyValue("dataUrl", Environment.getWebApplicationUrl() + "/webmail/openview.shtml?viewName=mailfilter");
		configNode.appendChildNode("mailboxConfig", "邮箱管理", "node", Environment.getWebApplicationUrl() + "/webmail/icon/config.gif", false).setExtendPropertyValue("dataUrl", Environment.getWebApplicationUrl() + "/jeaf/application/applicationView.shtml?applicationName=webmail&viewName=mailbox");
		
		//返回应用导航树
		return new ApplicationTreeNavigator(tree);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService#listApplicationNavigatorTreeNodes(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listApplicationNavigatorTreeNodes(String applicationPath, String parentTreeNodeId, SessionInfo sessionInfo) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService#saveApplicationNavigatorDefinition(java.lang.String, com.yuanluesoft.jeaf.application.model.navigator.definition.ApplicationNavigatorDefinition)
	 */
	public void saveApplicationNavigatorDefinition(String applicationName, ApplicationNavigatorDefinition navigatorDefinition) throws ServiceException {
		
	}

	/**
	 * @return the mailboxService
	 */
	public MailboxService getMailboxService() {
		return mailboxService;
	}

	/**
	 * @param mailboxService the mailboxService to set
	 */
	public void setMailboxService(MailboxService mailboxService) {
		this.mailboxService = mailboxService;
	}
}