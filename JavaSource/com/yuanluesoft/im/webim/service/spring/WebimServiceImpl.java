package com.yuanluesoft.im.webim.service.spring;

import java.beans.PropertyDescriptor;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.callback.GeneratePartPageCallback;
import com.yuanluesoft.im.model.message.ChatDetail;
import com.yuanluesoft.im.model.message.Message;
import com.yuanluesoft.im.model.message.ServerException;
import com.yuanluesoft.im.model.message.SessionFailed;
import com.yuanluesoft.im.model.message.TalkDetail;
import com.yuanluesoft.im.service.IMService;
import com.yuanluesoft.im.service.MessageWriter;
import com.yuanluesoft.im.webim.model.Webim;
import com.yuanluesoft.im.webim.model.WebimAction;
import com.yuanluesoft.im.webim.model.WebimChat;
import com.yuanluesoft.im.webim.service.WebimService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.BeanUtils;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public class WebimServiceImpl implements WebimService {
	private IMService imService; //IM服务
	private PageService pageService; //页面服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.im.webim.service.WebimService#processMessage(long, com.yuanluesoft.im.model.message.Message, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void processMessage(long currentUserId, Message message, final HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		//创建XML文档
		Document document = DocumentHelper.createDocument();
		final Element messagesElement =  document.addElement("Messages");
    	try {
	    	if(currentUserId==-1) {
	    		writeMessage(new SessionFailed(), messagesElement, request); //输出会话失效通知
	    	}
	    	else {
	    		MessageWriter responseMessageWriter = new MessageWriter() {
					public void writeResponseMessage(Message message) {
						try {
							writeMessage(message, messagesElement, request);
						}
						catch (ServiceException e) {
							Logger.exception(e);
						}
					}
	    		};
	    		imService.processReceivedMessage(currentUserId, message, responseMessageWriter);
	    	}
    	}
    	catch(Exception e) {
    		Logger.exception(e);
    		writeMessage(new ServerException(), messagesElement, request);
    	}
    	//输出XML
    	XMLWriter writer = null;
		try {
			response.setContentType("text/xml");
	    	response.setCharacterEncoding("UTF-8");
	    	OutputFormat format = OutputFormat.createPrettyPrint();      
			format.setEncoding("UTF-8");      
			writer = new XMLWriter(response.getOutputStream(), format);   
			writer.write(document);
		}
		catch(Exception e) {
		
		}
		finally {
			try {
				writer.close();
			}
			catch(Exception e) {
				
			}
		}
	}
	
	/**
	 * 输出消息
	 * @param message
	 * @param messagesElement
	 */
	private void writeMessage(Message message, Element messagesElement, HttpServletRequest request) throws ServiceException {
		if(message instanceof TalkDetail) { //发言,替换为WEBIM发言
			generateTalkHtmlContent((TalkDetail)message, request);
		}
		else if(message instanceof ChatDetail) { //对话,转换为WEBIM对话
			message = generateWebimChat((ChatDetail)message, request);
		}
		//创建通知元素
		String className = message.getClass().getName();
		Element notifyElement = messagesElement.addElement(className.substring(className.lastIndexOf('.')+1));
		//输出属性值
		PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(message);
		for(int i=0; i<properties.length; i++) {
			String propertyName =  properties[i].getName();
			if("class".equals(propertyName)) {
				continue;
			}
			Object propertyValue = null;
			try {
				propertyValue = PropertyUtils.getProperty(message, propertyName);
			}
			catch(Exception e) {
				
			}
			if(propertyValue==null) {
				continue;
			}
			Element propertyElement = notifyElement.addElement(propertyName);
			if(propertyValue instanceof Date) { //日期
				propertyElement.setText(DateTimeUtils.formatDate((Date)propertyValue, null));
			}
			else if(propertyValue instanceof Timestamp) { //时间
				propertyElement.setText(DateTimeUtils.formatTimestamp((Timestamp)propertyValue, null));
			}
			else {
				propertyElement.setText(propertyValue.toString());
			}
		}
	}

	/**
	 * 生成发言对应的HTML
	 * @param talkDetail
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	private void generateTalkHtmlContent(TalkDetail talkDetail, HttpServletRequest request) throws ServiceException {
		WebimChat chat = new WebimChat();
		HashSet talks = new HashSet();
		talks.add(talkDetail);
		chat.setTalks(talks); //设置为components
		//回调
		GeneratePartPageCallback generatePartPageCallback = new GeneratePartPageCallback() {
			public HTMLElement getPartTemplate(HTMLDocument template) throws ServiceException {
				NodeList nodes = template.getElementsByTagName("a");
				for(int i=nodes.getLength()-1; i>=0; i--) {
					HTMLAnchorElement a = (HTMLAnchorElement)nodes.item(i);
					if("recordList".equals(a.getId()) && "talks".equals(StringUtils.getPropertyValue(a.getAttribute("urn"), "recordListName"))) {
						return a;
					}
				}
				return null;
			}
		};
		String applicationName = "im/webim";
		String pageName = "chat";
		if(RequestUtils.getParameterLongValue(request, "customerServiceChatId")>0) { //客服对话
			applicationName = "im/cs";
			pageName = "chatOfCustomer";
		}
		talkDetail.setContent(pageService.generatePartPage(applicationName, pageName, "talk", false, chat, RequestUtils.getParameterLongValue(request, "siteId"), request, generatePartPageCallback));
	}

	/**
	 * 生成WEBIM对话
	 * @param chat
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	private WebimChat generateWebimChat(ChatDetail chat, HttpServletRequest request) throws ServiceException {
		WebimChat webimChat = new WebimChat();
		try {
			PropertyUtils.copyProperties(webimChat, chat);
		}
		catch(Exception e) {
			
		}
		Webim webim = new Webim();
		webim.setChat(chat);
		//回调
		GeneratePartPageCallback generatePartPageCallback = new GeneratePartPageCallback() {
			public HTMLElement getPartTemplate(HTMLDocument template) throws ServiceException {
				//获取对话按钮
				NodeList webimActions = template.getElementsByName("webimAction");
				for(int i=webimActions.getLength()-1; i>=0; i--) {
					HTMLElement element = (HTMLElement)webimActions.item(i);
					//解析WEBIM按钮
					WebimAction webimAction = (WebimAction)BeanUtils.generateBeanByProperties(WebimAction.class, element.getAttribute("urn"), null);
					if(!"对话".equals(webimAction.getAction())) {
						continue;
					}
					//获取未读发言数字段,在字段外添加SPAN,用来更新发言数
					NodeList elements = element.getElementsByTagName("a");
					for(int j=(elements==null ? -1 : elements.getLength()-1); j>=0; j--) {
						HTMLAnchorElement a = (HTMLAnchorElement)elements.item(j);
						if("field".equals(a.getId()) && "chat.unreadTalkCount".equals(StringUtils.getPropertyValue(a.getAttribute("urn"), "name"))) {
							a.setName("recordCount");
							a.setAttribute("style", "display:none");
							break;
						}
					}
					return element;
				}
				return null;
			}
		};
		webimChat.setChatActionHTML(pageService.generatePartPage("im/webim", "webim", "chatAction", true, webim, RequestUtils.getParameterLongValue(request, "siteId"), request, generatePartPageCallback));
		return webimChat;
	}

	/**
	 * @return the imService
	 */
	public IMService getImService() {
		return imService;
	}

	/**
	 * @param imService the imService to set
	 */
	public void setImService(IMService imService) {
		this.imService = imService;
	}

	/**
	 * @return the pageService
	 */
	public PageService getPageService() {
		return pageService;
	}

	/**
	 * @param pageService the pageService to set
	 */
	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}
}