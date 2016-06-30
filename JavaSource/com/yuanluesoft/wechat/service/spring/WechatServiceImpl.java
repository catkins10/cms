package com.yuanluesoft.wechat.service.spring;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.httpclient.NameValuePair;
import org.dom4j.Element;
import org.dom4j.tree.DefaultCDATA;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.directorymanage.model.DirectoryPopedomType;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.exception.ValidateException;
import com.yuanluesoft.jeaf.image.model.Image;
import com.yuanluesoft.jeaf.image.service.ImageService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Encoder;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.HttpUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.workflow.callback.WorkflowParticipantCallback;
import com.yuanluesoft.jeaf.workflow.pojo.WorkflowData;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.wechat.model.MessageNews;
import com.yuanluesoft.wechat.pojo.WechatAccount;
import com.yuanluesoft.wechat.pojo.WechatMedia;
import com.yuanluesoft.wechat.pojo.WechatMenuItem;
import com.yuanluesoft.wechat.pojo.WechatMessageNews;
import com.yuanluesoft.wechat.pojo.WechatMessageReceive;
import com.yuanluesoft.wechat.pojo.WechatMessageResponse;
import com.yuanluesoft.wechat.pojo.WechatMessageSend;
import com.yuanluesoft.wechat.pojo.WechatUser;
import com.yuanluesoft.wechat.pojo.WechatUserGroupMember;
import com.yuanluesoft.wechat.pojo.WechatWorkflowConfig;
import com.yuanluesoft.wechat.service.WechatService;
import com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback;
import com.yuanluesoft.workflow.client.model.definition.WorkflowPackage;

/**
 *
 * @author linchuan
 *
 */
public class WechatServiceImpl extends BusinessServiceImpl implements WechatService {
	private OrgService orgService; //组织机构服务
	private ImageService imageService; //图片服务
	private SiteService siteService; //站点服务
	private WorkflowExploitService workflowExploitService; //工作流利用服务
	
	//私有属性
	private List accessTokens = new ArrayList(); //令牌
	
	/**
	 * 初始化
	 *
	 */
	public void init() {
		try {
			orgService.appendDirectoryPopedomType("wechatMessageManager", "微信消息管理", "unit", DirectoryPopedomType.INHERIT_FROM_PARENT_NO, false, true);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		record = super.save(record);
		if(record instanceof WechatMenuItem) {
			createMenu(((WechatMenuItem)record).getAccountId());
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		record = super.update(record);
		if(record instanceof WechatMenuItem) {
			createMenu(((WechatMenuItem)record).getAccountId());
		}
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		if(record instanceof WechatMenuItem) {
			createMenu(((WechatMenuItem)record).getAccountId());
		}
	}

	/**
	 * 创建菜单
	 * @param account
	 */
	private void createMenu(long accountId) throws ServiceException  {
		WechatAccount account = (WechatAccount)load(WechatAccount.class, accountId);
		JSONObject jsonObject = new JSONObject();
		JSONArray array = new JSONArray();
		createMenuJSONObject(account, array, 0);
		jsonObject.put("button", array);
		if(Logger.isTraceEnabled()) {
			Logger.trace("WechatService: create menu by json object " + jsonObject.toJSONString());
		}
		//正确时的返回JSON数据包如下：{"errcode":0,"errmsg":"ok"}
		//错误时的返回JSON数据包如下（示例为无效菜单名长度）： {"errcode":40018,"errmsg":"invalid button name size"}
		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + getAccessToken(account);
		JSONObject result = openWechatURL(url, jsonObject.toJSONString());
		if(!"0".equals("" + result.get("errcode"))) {
			throw new ValidateException("创建菜单时出错," + result.get("errmsg"));
		}
	}
	
	/**
	 * 递归函数:创建菜单JSON对象
	 * @param account
	 * @param parentNode
	 * @param parentItemId
	 */
	private void createMenuJSONObject(WechatAccount account, JSONArray parentArray, long parentItemId) {
		List menuItems = ListUtils.getSubListByProperty(account.getMenuItems(), "parentItemId", new Long(parentItemId));
		for(Iterator iterator = menuItems==null ? null : menuItems.iterator(); iterator!=null && iterator.hasNext();) {
			WechatMenuItem menuItem = (WechatMenuItem)iterator.next();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", menuItem.getName());
			if(menuItem.getType()==0) { //0/父菜单,1/链接,2/触发点击事件
				JSONArray array = new JSONArray();
				createMenuJSONObject(account, array, menuItem.getId()); //递归创建下一级
				jsonObject.put("sub_button", array);
				if(array.isEmpty()) {
					continue;
				}
			}
			else if(menuItem.getType()==1) {
				jsonObject.put("type", "view");
				jsonObject.put("url", resetUrl(account, menuItem.getUrl()));
			}
			else {
				jsonObject.put("type", "click");
				jsonObject.put("key", "" + menuItem.getId());
			}
			parentArray.add(jsonObject);
		}
	}
	
	/**
	 * 重置URL
	 * @param account
	 * @param url
	 * @return
	 */
	private String resetUrl(WechatAccount account, String url) {
		if(url.indexOf("://")!=-1) {
			return url;
		}
		String siteUrl = account.getSiteUrl();
		int index = siteUrl.indexOf("://");
		index = siteUrl.indexOf('/', index==-1 ? 0 : index + 3);
		if(index!=-1) {
			siteUrl = siteUrl.substring(0, index);
		}
		return siteUrl + url;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.wechat.service.WechatService#getWechatAccountByUnitId(long)
	 */
	public WechatAccount getWechatAccountByUnitId(long unitId) throws ServiceException {
		String hql = "from WechatAccount WechatAccount where WechatAccount.unitId=" + unitId;
		return (WechatAccount)getDatabaseService().findRecordByHql(hql, listLazyLoadProperties(WechatAccount.class));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.wechat.service.WechatService#createMenuTree(com.yuanluesoft.wechat.pojo.WechatAccount)
	 */
	public Tree createMenuTree(WechatAccount account) throws ServiceException {
		Tree tree = new Tree("0", "微信菜单", "root", Environment.getContextPath() + "/wechat/icons/wechat.png");
		createMenuTreeNodes(account, tree.getRootNode(), 0);
		return tree;
	}
	
	/**
	 * 递归函数:创建树节点
	 * @param account
	 * @param parentNode
	 * @param parentItemId
	 */
	private void createMenuTreeNodes(WechatAccount account, TreeNode parentNode, long parentItemId) {
		if(account==null) {
			return;
		}
		List menuItems = ListUtils.getSubListByProperty(account.getMenuItems(), "parentItemId", new Long(parentItemId));
		for(Iterator iterator = menuItems==null ? null : menuItems.iterator(); iterator!=null && iterator.hasNext();) {
			WechatMenuItem menuItem = (WechatMenuItem)iterator.next();
			String nodeType;
			if(menuItem.getType()==0) { //0/父菜单,1/链接,2/触发点击事件
				nodeType = "folder";
			}
			else if(menuItem.getType()==1) {
				nodeType = "link";
			}
			else {
				nodeType = "event";
			}
			TreeNode node = parentNode.appendChildNode("" + menuItem.getId(), menuItem.getName(), nodeType, Environment.getContextPath() + "/wechat/icons/" + nodeType + ".png", false);
			node.setExpandTree(true);
			//递归创建下一级
			createMenuTreeNodes(account, node, menuItem.getId());
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.wechat.service.WechatService#getWorkflowConfig(long, boolean)
	 */
	public WechatWorkflowConfig getWorkflowConfig(long orgId, boolean inheritParentEnabled) throws ServiceException {
		String hql;
		if(!inheritParentEnabled || orgId==0) {
			hql = "from WechatWorkflowConfig WechatWorkflowConfig" +
				  " where WechatWorkflowConfig.orgId=" + orgId;
		}
		else {
			//查找当前机构以及上级机构的流程配置
			hql = "select WechatWorkflowConfig" +
				  " from WechatWorkflowConfig WechatWorkflowConfig, OrgSubjection OrgSubjection" +
				  " where WechatWorkflowConfig.orgId=OrgSubjection.parentDirectoryId" +
				  " and OrgSubjection.directoryId=" + orgId +
				  " order by OrgSubjection.id";
		}
		return (WechatWorkflowConfig)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.wechar.service.WechatService#processReceivedMessage(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void processReceivedMessage(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		try {
			if(Logger.isDebugEnabled()) {
				Logger.debug("WechatService: process wechat request " + RequestUtils.getRequestURL(request, true));
			}
			//按站点获取公众帐号配置
			long unitId = RequestUtils.getParameterLongValue(request, "unitId");
			WechatAccount wechatAccount = getWechatAccountByUnitId(unitId);
			if(!checkSignature(wechatAccount.getToken(), request)) {
				return;
			}
			//读取消息内容
			StringBuffer requestBody = new StringBuffer();
			char[] buffer = new char[2048];
			BufferedReader reader = request.getReader();
			int readLen;
			while((readLen=reader.read(buffer))!=-1) {
				requestBody.append(buffer, 0, readLen);
			}
			if(requestBody.length()==0) { //没有内容
				response.getWriter().write(request.getParameter("echostr")); //输出随机字符串
				return;
			}
			//解析接收到的信息
			if(Logger.isDebugEnabled()) {
				Logger.debug("WechatService: wechat request body is " + requestBody.toString());
			}
			Element xmlElement = new XmlParser().parseXmlString(requestBody.toString());
			String msgType = xmlElement.elementText("MsgType");
			if("text".equalsIgnoreCase(msgType)) {
				processReceivedTextMessage(xmlElement, wechatAccount, request, response);
			}
			else if("image".equalsIgnoreCase(msgType)) {
				processReceivedImageMessage(xmlElement, wechatAccount, request, response); 
			}
			else if("voice".equalsIgnoreCase(msgType)) {
				processReceivedVoiceMessage(xmlElement, wechatAccount, request, response); 
			}
			else if("video".equalsIgnoreCase(msgType)) {
				processReceivedVideoMessage(xmlElement, wechatAccount, request, response); 
			}
			else if("location".equalsIgnoreCase(msgType)) {
				processReceivedLocationMessage(xmlElement, wechatAccount, request, response); 
			}
			else if("link".equalsIgnoreCase(msgType)) {
				processReceivedLinkMessage(xmlElement, wechatAccount, request, response); 
			}
			else if("event".equalsIgnoreCase(msgType)) {
				String event = xmlElement.elementText("Event");
				if("subscribe".equalsIgnoreCase(event)) {
					processSubscribeEvent(xmlElement, wechatAccount, request, response); //事件类型，subscribe(订阅)、unsubscribe(取消订阅)
				}
				else if("unsubscribe".equalsIgnoreCase(event)) {
					processUnsubscribeEvent(xmlElement, wechatAccount, request, response); //事件类型，subscribe(订阅)、unsubscribe(取消订阅)
				}
				else if("LOCATION".equalsIgnoreCase(event)) {
					//processReceivedLocationMessage(xmlElement, wechatAccount, request, response);
				}
				else if("CLICK".equalsIgnoreCase(event)) {
					processClickEvent(xmlElement, wechatAccount, request, response);
				}
				else if("VIEW".equalsIgnoreCase(event)) {
					processViewEvent(xmlElement, wechatAccount, request, response);
				}
				else if("MASSSENDJOBFINISH".equalsIgnoreCase(event)) {
					processMessageSendJobFinishEvent(xmlElement, wechatAccount, request, response);
				}
			}
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
	
	/**
	 * 处理接收到的文本消息
	 * @param xmlElement
	 * @param wechatAccount
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	private void processReceivedTextMessage(Element xmlElement, WechatAccount wechatAccount, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		//ToUserName:开发者微信号, FromUserName:发送方帐号（一个OpenID）, CreateTime:消息创建时间 （整型）, MsgType:text, Content:文本消息内容, MsgId:消息id，64位整型
		WechatMessageReceive messageReceive = createMessageReceive(xmlElement, wechatAccount);
		if(messageReceive==null) {
			return;
		}
		messageReceive.setContent(StringUtils.generateHtmlContent(xmlElement.elementText("Content"))); //消息内容
		//获取应答消息
		WechatMessageResponse wechatMessageResponse = getWechatMessageResponse(wechatAccount, "talk", messageReceive.getContent());
		//回复消息
		if(replyReceivedMessage(wechatAccount, wechatMessageResponse, messageReceive.getFromUserOpenId(), messageReceive.getToUserName(), response)) {
			messageReceive.setReplyTime(DateTimeUtils.now());
		}
		save(messageReceive);
	}
	
	/**
	 * 处理接收到的图片消息
	 * @param xmlElement
	 * @param wechatAccount
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	private void processReceivedImageMessage(Element xmlElement, WechatAccount wechatAccount, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		//ToUserName:开发者微信号, FromUserName: 发送方帐号（一个OpenID）, CreateTime: 消息创建时间 （整型）, MsgType: image, PicUrl: 图片链接
		//MediaId: 图片消息媒体id，可以调用多媒体文件下载接口拉取数据。MsgId: 消息id，64位整型
		WechatMessageReceive messageReceive = createMessageReceive(xmlElement, wechatAccount);
		if(messageReceive==null) {
			return;
		}
		String picUrl = xmlElement.elementText("PicUrl");
		messageReceive.setPicUrl(picUrl); //图片链接,消息类型为image时有效
		if(picUrl!=null && !picUrl.isEmpty()) {
			messageReceive.setContent("<img src=\"" + picUrl + "\">"); //消息内容
		}
		else {
			//private String mediaId; //消息媒体ID,可以调用多媒体文件下载接口拉取数据
		}
		//获取应答消息
		WechatMessageResponse wechatMessageResponse = getWechatMessageResponse(wechatAccount, "talk", null);
		//回复消息
		if(replyReceivedMessage(wechatAccount, wechatMessageResponse, messageReceive.getFromUserOpenId(), messageReceive.getToUserName(), response)) {
			messageReceive.setReplyTime(DateTimeUtils.now());
		}
		save(messageReceive);
	}
	
	/**
	 * 处理接收到的语音消息
	 * @param xmlElement
	 * @param wechatAccount
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	private void processReceivedVoiceMessage(Element xmlElement, WechatAccount wechatAccount, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		//ToUserName:开发者微信号 FromUserName:发送方帐号（一个OpenID） CreateTime:消息创建时间 （整型） MsgType:语音为voice 
		//MediaId:语音消息媒体id，可以调用多媒体文件下载接口拉取数据。Format:语音格式，如amr，speex等 MsgID:消息id，64位整型 
		//Recognition:语音识别结果，UTF8编码 
		WechatMessageReceive messageReceive = createMessageReceive(xmlElement, wechatAccount);
		if(messageReceive==null) {
			return;
		}
		messageReceive.setVoiceFormat(xmlElement.elementText("Format")); //语音格式,amr，speex等
		//private String content; //消息内容
		//获取应答消息
		WechatMessageResponse wechatMessageResponse = getWechatMessageResponse(wechatAccount, "talk", null);
		//回复消息
		if(replyReceivedMessage(wechatAccount, wechatMessageResponse, messageReceive.getFromUserOpenId(), messageReceive.getToUserName(), response)) {
			messageReceive.setReplyTime(DateTimeUtils.now());
		}
		save(messageReceive);
	}
	
	/**
	 * 处理接收到的视频消息
	 * @param xmlElement
	 * @param wechatAccount
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	private void processReceivedVideoMessage(Element xmlElement, WechatAccount wechatAccount, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		//ToUserName:开发者微信号 FromUserName:发送方帐号（一个OpenID） CreateTime:消息创建时间 （整型） MsgType:视频为video 
		//MediaId: 视频消息媒体id，可以调用多媒体文件下载接口拉取数据。 ThumbMediaId:视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。 MsgId:消息id，64位整型
		WechatMessageReceive messageReceive = createMessageReceive(xmlElement, wechatAccount);
		if(messageReceive==null) {
			return;
		}
		//private String content; //消息内容
		//获取应答消息
		WechatMessageResponse wechatMessageResponse = getWechatMessageResponse(wechatAccount, "talk", null);
		//回复消息
		if(replyReceivedMessage(wechatAccount, wechatMessageResponse, messageReceive.getFromUserOpenId(), messageReceive.getToUserName(), response)) {
			messageReceive.setReplyTime(DateTimeUtils.now());
		}
		save(messageReceive);
	}
	
	/**
	 * 处理接收到的地理位置消息
	 * @param xmlElement
	 * @param wechatAccount
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	private void processReceivedLocationMessage(Element xmlElement, WechatAccount wechatAccount, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		//ToUserName:开发者微信号 FromUserName:发送方帐号（一个OpenID） CreateTime:消息创建时间 （整型） MsgType:location
		//Location_X:地理位置维度 Location_Y:地理位置经度 Scale:地图缩放大小, Label:地理位置信息 MsgId: 消息id，64位整型
		//上报地理位置事件: Latitude:地理位置纬度, Longitude:地理位置经度 Precision:地理位置精度
		WechatMessageReceive messageReceive = createMessageReceive(xmlElement, wechatAccount);
		if(messageReceive==null) {
			return;
		}
		messageReceive.setMsgType("location");
		messageReceive.setLocationX(xmlElement.elementText("Location_X")); //地理位置纬度
		messageReceive.setLocationY(xmlElement.elementText("Location_Y")); //地理位置经度
		messageReceive.setMapScale(xmlElement.elementText("Scale")); //地图缩放大小
		messageReceive.setLocationLabel(xmlElement.elementText("Label")); //地理位置信息
		if(messageReceive.getLocationX()==null || messageReceive.getLocationX().isEmpty()) {
			messageReceive.setLocationX(xmlElement.elementText("Latitude")); //地理位置纬度
			messageReceive.setLocationY(xmlElement.elementText("Longitude")); //地理位置经度
		}
		messageReceive.setContent("当前位置<br/>" + (messageReceive.getLocationLabel()==null || messageReceive.getLocationLabel().isEmpty() ? "" : StringUtils.generateHtmlContent(messageReceive.getLocationLabel()) + "<br/>") + "经度:" + messageReceive.getLocationY() + ", 纬度:" + messageReceive.getLocationX()); //消息内容
		//TODO: 根据地理位置获取相关数据
		//获取应答消息
		WechatMessageResponse wechatMessageResponse = getWechatMessageResponse(wechatAccount, "location", null);
		//回复消息
		if(replyReceivedMessage(wechatAccount, wechatMessageResponse, messageReceive.getFromUserOpenId(), messageReceive.getToUserName(), response)) {
			messageReceive.setReplyTime(DateTimeUtils.now());
		}
		save(messageReceive);
	}
	
	/**
	 * 处理接收到的链接消息
	 * @param xmlElement
	 * @param wechatAccount
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	private void processReceivedLinkMessage(Element xmlElement, WechatAccount wechatAccount, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		//ToUserName:接收方微信号 FromUserName:发送方微信号，若为普通用户，则是一个OpenID CreateTime:消息创建时间 MsgType: 消息类型，link Title:消息标题 
		//Description:消息描述 Url:消息链接 MsgId:消息id，64位整型 
		WechatMessageReceive messageReceive = createMessageReceive(xmlElement, wechatAccount);
		if(messageReceive==null) {
			return;
		}
		messageReceive.setLinkTitle(xmlElement.elementText("Title")); //链接消息标题
		messageReceive.setLinkDescription(xmlElement.elementText("Description")); //链接消息描述
		messageReceive.setUrl(xmlElement.elementText("Url")); //消息链接
		messageReceive.setContent("<a href=\"" + messageReceive.getUrl() + "\">" + StringUtils.generateHtmlContent(messageReceive.getLinkTitle()) + "</a>" + (messageReceive.getLinkDescription()==null ? "" : "<br/>" + StringUtils.escape(messageReceive.getLinkDescription()))); //消息内容
		//获取应答消息
		WechatMessageResponse wechatMessageResponse = getWechatMessageResponse(wechatAccount, "talk", messageReceive.getContent());
		//回复消息
		if(replyReceivedMessage(wechatAccount, wechatMessageResponse, messageReceive.getFromUserOpenId(), messageReceive.getToUserName(), response)) {
			messageReceive.setReplyTime(DateTimeUtils.now());
		}
		save(messageReceive);
	}

	/**
	 * 处理订阅事件
	 * @param xmlElement
	 * @param wechatAccount
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	private void processSubscribeEvent(Element xmlElement, WechatAccount wechatAccount, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		//ToUserName:开发者微信号 FromUserName:发送方帐号（一个OpenID） CreateTime:消息创建时间 （整型） MsgType:消息类型，event Event:事件类型，subscribe(订阅)、unsubscribe(取消订阅)
		//扫描带参数二维码事件: EventKey:事件KEY值，qrscene_为前缀，后面为二维码的参数值 Ticket:二维码的ticket，可用来换取二维码图片
		String fromUserName = xmlElement.elementText("FromUserName");
		getWechatUser(wechatAccount, fromUserName, true);
		//获取应答消息
		WechatMessageResponse wechatMessageResponse = getWechatMessageResponse(wechatAccount, "subscribe", null);
		//回复消息
		replyReceivedMessage(wechatAccount, wechatMessageResponse, fromUserName, xmlElement.elementText("ToUserName"), response);
	}
	
	/**
	 * 处理取消订阅事件
	 * @param xmlElement
	 * @param wechatAccount
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	private void processUnsubscribeEvent(Element xmlElement, WechatAccount wechatAccount, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		//ToUserName:开发者微信号 FromUserName:发送方帐号（一个OpenID） CreateTime:消息创建时间 （整型） MsgType:消息类型，event Event:事件类型，subscribe(订阅)、unsubscribe(取消订阅)
		String fromUserName = xmlElement.elementText("FromUserName");
		WechatUser wechatUser = getWechatUser(wechatAccount, fromUserName, false);
		if(wechatUser!=null) {
			wechatUser.setUnsubscribeTime(new Timestamp(Long.parseLong(xmlElement.elementText("CreateTime")) * 1000));
			update(wechatUser);
		}
		//获取应答消息
		WechatMessageResponse wechatMessageResponse = getWechatMessageResponse(wechatAccount, "unsubscribe", null);
		//回复消息
		replyReceivedMessage(wechatAccount, wechatMessageResponse, fromUserName, xmlElement.elementText("ToUserName"), response);
	}
	
	/**
	 * 处理点击菜单拉取消息时的事件推送 
	 * @param xmlElement
	 * @param wechatAccount
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	private void processClickEvent(Element xmlElement, WechatAccount wechatAccount, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		//ToUserName:开发者微信号 FromUserName:发送方帐号（一个OpenID） CreateTime:消息创建时间 （整型） MsgType:消息类型，event Event:事件类型，CLICK EventKey:事件KEY值，与自定义菜单接口中KEY值对应 
		//获取应答消息
		WechatMessageResponse wechatMessageResponse = getWechatMessageResponse(wechatAccount, "menu_" + xmlElement.elementText("EventKey"), null);
		//回复消息
		replyReceivedMessage(wechatAccount, wechatMessageResponse, xmlElement.elementText("FromUserName"), xmlElement.elementText("ToUserName"), response);
	}
	
	/**
	 * 处理点击菜单跳转链接时的事件推送 
	 * @param xmlElement
	 * @param wechatAccount
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	private void processViewEvent(Element xmlElement, WechatAccount wechatAccount, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		
	}
	
	/**
	 * 处理推送群发结果事件推送,由于群发任务提交后，群发任务可能在一定时间后才完成，因此，群发接口调用时，仅会给出群发任务是否提交成功的提示，若群发任务提交成功，则在群发任务结束时，会向开发者在公众平台填写的开发者URL（callback URL）推送事件
	 * @param xmlElement
	 * @param wechatAccount
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	private void processMessageSendJobFinishEvent(Element xmlElement, WechatAccount wechatAccount, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		//ToUserName:公众号的微信号 FromUserName:公众号群发助手的微信号，为mphelper CreateTime:创建时间的时间戳
		//MsgType:消息类型，此处为event Event：事件信息，此处为MASSSENDJOBFINISH MsgID:群发的消息ID
		//Status:群发的结构，为“send success”或“send fail”或“err(num)”。但send success时，也有可能因用户拒收公众号的消息、系统错误等原因造成少量用户接收失败。err(num)是审核失败的具体原因，可能的情况如下：
		//err(10001), //涉嫌广告 err(20001), //涉嫌政治 err(20004), //涉嫌社会 err(20002), //涉嫌色情 err(20006), //涉嫌违法犯罪 err(20008), //涉嫌欺诈
		//err(20013)//涉嫌版权 err(22000), //涉嫌互推(互相宣传) err(21000), //涉嫌其他
		//TotalCount:group_id下粉丝数；或者openid_list中的粉丝数 FilterCount:过滤（过滤是指特定地区、性别的过滤、用户设置拒收的过滤，用户接收已超4条的过滤）后，准备发送的粉丝数，原则上，FilterCount = SentCount + ErrorCount
		//SentCount:发送成功的粉丝数 ErrorCount:发送失败的粉丝数 
		String hql = "from WechatMessageSend WechatMessageSend" +
					" where WechatMessageSend.unitId=" + wechatAccount.getUnitId() +
					" and WechatMessageSend.msgId='" + xmlElement.elementText("MsgID") + "'";
		WechatMessageSend messageSend = (WechatMessageSend)getDatabaseService().findRecordByHql(hql);
		if(messageSend==null) {
			return;
		}
		String status = xmlElement.elementText("Status");
		if("send success".equals(status)) {
			status = "发送成功";
		}
		else if("send fail".equals(status)) {
			status = "发送失败";
		}
		else if("err(10001)".equals(status)) {
			status = "涉嫌广告";
		}
		else if("err(20001)".equals(status)) {
			status = "涉嫌政治";
		}
		else if("err(20004)".equals(status)) {
			status = "涉嫌社会";
		}
		else if("err(20002)".equals(status)) {
			status = "涉嫌色情";
		}
		else if("err(20006)".equals(status)) {
			status = "涉嫌违法犯罪";
		}
		else if("err(20008)".equals(status)) {
			status = "涉嫌欺诈";
		}
		else if("err(20013)".equals(status)) {
			status = "涉嫌版权";
		}
		else if("err(22000)".equals(status)) {
			status = "涉嫌互推(互相宣传)";
		}
		else if("err(21000)".equals(status)) {
			status = "涉嫌其他";
		}
		messageSend.setStatus(status); //状态
		messageSend.setTotalCount(Integer.parseInt(xmlElement.elementText("TotalCount"))); //用户数,group_id下粉丝数；或者openid_list中的粉丝数 
		messageSend.setFilterCount(Integer.parseInt(xmlElement.elementText("FilterCount"))); //过滤后用户数,FilterCount = SentCount + ErrorCount 
		messageSend.setSentCount(Integer.parseInt(xmlElement.elementText("SentCount"))); //发送成功用户数
		messageSend.setErrorCount(Integer.parseInt(xmlElement.elementText("ErrorCount"))); //发送失败用户数
		update(messageSend);
	}
	
	/**
	 * 创建接收到的消息,如果之前已经接收过,返回null
	 * @param xmlElement
	 * @param wechatAccount
	 * @return
	 * @throws ServiceException
	 */
	private WechatMessageReceive createMessageReceive(Element xmlElement, WechatAccount wechatAccount) throws ServiceException {
		String msgId = xmlElement.elementText("MsgId");
		if(msgId!=null) {
			String hql = "select WechatMessageReceive.id" +
						 " from WechatMessageReceive WechatMessageReceive" +
						 " where WechatMessageReceive.unitId=" + wechatAccount.getUnitId() +
						 " and WechatMessageReceive.msgId='" + msgId + "'";
			if(getDatabaseService().findRecordByHql(hql)!=null) {
				return null;
			}
		}
		WechatMessageReceive messageReceive = new WechatMessageReceive();
		messageReceive.setId(UUIDLongGenerator.generateId()); //ID
		messageReceive.setUnitId(wechatAccount.getUnitId()); //单位ID
		messageReceive.setToUserName(xmlElement.elementText("ToUserName")); //开发者微信号
		messageReceive.setFromUserOpenId(xmlElement.elementText("FromUserName")); //发送方帐号,一个OpenID
		//获取用户信息
		WechatUser wechatUser = getWechatUser(wechatAccount, messageReceive.getFromUserOpenId(), false);
		if(wechatUser!=null) {
			messageReceive.setFromUserNickname(wechatUser.getNickname());
			messageReceive.setFromUserId(wechatUser.getId()); //发送方用户ID
		}
		messageReceive.setCreateTime(new Timestamp(Long.parseLong(xmlElement.elementText("CreateTime")) * 1000)); //消息创建时间
		messageReceive.setMsgId(msgId); //消息ID,64位整型
		messageReceive.setMsgType(xmlElement.elementText("MsgType")); //消息类型,text/image/voice/location/link
		messageReceive.setMediaId(xmlElement.elementText("MediaId")); //消息媒体ID,可以调用多媒体文件下载接口拉取数据
		return messageReceive;
	}
	
	/**
	 * 获取应答消息
	 * @param wechatAccount
	 * @param responseType subscribe(关注)、unsubscribe(取消关注)、menu_(菜单事件)、location(上报地理位置事件) 、qrscene(扫描带参数二维码)、talk(用户发言)
	 * @param content
	 * @return
	 * @throws ServiceException
	 */
	private WechatMessageResponse getWechatMessageResponse(WechatAccount wechatAccount, String responseType, String content) throws ServiceException {
		String hql = "from WechatMessageResponse WechatMessageResponse" +
					 " where WechatMessageResponse.unitId=" + wechatAccount.getUnitId() +
					 " and WechatMessageResponse.responseType='" + responseType + "'";
		List responses = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("news"));
		if(responses==null || responses.isEmpty()) {
			return null;
		}
		if(!"talk".equals(responseType)) { //不是发言
			return (WechatMessageResponse)responses.get(0);
		}
		WechatMessageResponse noKeywordsEesponse = null; //没有指定关键字的应答
		for(Iterator iterator = responses.iterator(); iterator.hasNext();) {
			WechatMessageResponse response = (WechatMessageResponse)iterator.next();
			if(response.getKeywords()==null || response.getKeywords().isEmpty()) {
				noKeywordsEesponse = response;
			}
			else if(StringUtils.isMatch(content, response.getKeywords())) {
				return response;
			}
		}
		return noKeywordsEesponse;
	}
	
	/**
	 * 消息应答
	 * @param wechatAccount
	 * @param messageResponse
	 * @param messageReceive
	 * @param response
	 * @throws ServiceException
	 */
	private boolean replyReceivedMessage(WechatAccount wechatAccount, WechatMessageResponse messageResponse, String toUserName, String fromUserName, HttpServletResponse response) throws ServiceException {
		if(messageResponse==null) {
			return false;
		}
		XmlParser xmlParser = new XmlParser();
		Element xmlElement = null;
		try {
			xmlElement = xmlParser.parseXmlString("<xml/>");
		}
		catch (ParseException e) {
		
		}
		xmlElement.addElement("ToUserName").add(new DefaultCDATA(toUserName)); //接收方帐号（收到的OpenID） 
		xmlElement.addElement("FromUserName").add(new DefaultCDATA(fromUserName)); //开发者微信号
		xmlElement.addElement("CreateTime").setText("" + (int)System.currentTimeMillis()/1000); //消息创建时间 （整型） 
		if("text".equals(messageResponse.getType())) { //回复文本消息
			/*
			<xml>
			<ToUserName><![CDATA[toUser]]></ToUserName>
			<FromUserName><![CDATA[fromUser]]></FromUserName>
			<CreateTime>12345678</CreateTime>
			<MsgType><![CDATA[text]]></MsgType>
			<Content><![CDATA[你好]]></Content>
			</xml>
			 */
			xmlElement.addElement("MsgType").add(new DefaultCDATA("text")); //消息类型
			xmlElement.addElement("Content").add(new DefaultCDATA(messageResponse.getContent())); //回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示） 
		}
		else if("image".equals(messageResponse.getType())) { //回复图片消息
			/*
			<xml>
			<ToUserName><![CDATA[toUser]]></ToUserName>
			<FromUserName><![CDATA[fromUser]]></FromUserName>
			<CreateTime>12345678</CreateTime>
			<MsgType><![CDATA[image]]></MsgType>
			<Image>
			<MediaId><![CDATA[media_id]]></MediaId>
			</Image>
			</xml>
			 */
			xmlElement.addElement("MsgType").add(new DefaultCDATA("image")); //消息类型
			String mediaId = getAttachmentMediaId(wechatAccount, messageResponse, "image", "image");
			if(mediaId==null) {
				return false;
			}
			xmlElement.addElement("Image").addElement("MediaId").add(new DefaultCDATA(mediaId)); //通过上传多媒体文件，得到的id。  
		}
		else if("voice".equals(messageResponse.getType())) { //回复语音消息
			/*
			<xml>
			<ToUserName><![CDATA[toUser]]></ToUserName>
			<FromUserName><![CDATA[fromUser]]></FromUserName>
			<CreateTime>12345678</CreateTime>
			<MsgType><![CDATA[voice]]></MsgType>
			<Voice>
			<MediaId><![CDATA[media_id]]></MediaId>
			</Voice>
			</xml>
			 */
			xmlElement.addElement("MsgType").add(new DefaultCDATA("voice")); //消息类型
			String mediaId = getAttachmentMediaId(wechatAccount, messageResponse, "voice", "voice");
			if(mediaId==null) {
				return false;
			}
			xmlElement.addElement("Voice").addElement("MediaId").add(new DefaultCDATA(mediaId)); //通过上传多媒体文件，得到的id。  
		}
		else if("voice".equals(messageResponse.getType())) { //回复视频消息
			/*
			<xml>
			<ToUserName><![CDATA[toUser]]></ToUserName>
			<FromUserName><![CDATA[fromUser]]></FromUserName>
			<CreateTime>12345678</CreateTime>
			<MsgType><![CDATA[video]]></MsgType>
			<Video>
			<MediaId><![CDATA[media_id]]></MediaId>
			<Title><![CDATA[title]]></Title>
			<Description><![CDATA[description]]></Description>
			</Video> 
			</xml>
			 */
			xmlElement.addElement("MsgType").add(new DefaultCDATA("video")); //消息类型
			String mediaId = getAttachmentMediaId(wechatAccount, messageResponse, "video", "video");
			if(mediaId==null) {
				return false;
			}
			Element xmlVideo = xmlElement.addElement("Video");
			xmlVideo.addElement("MediaId").add(new DefaultCDATA(mediaId)); //通过上传多媒体文件，得到的id。
			if(messageResponse.getTitle()!=null) {
				xmlVideo.addElement("Title").add(new DefaultCDATA(messageResponse.getTitle().replaceAll("[\r]", "").replaceAll("[\n]", " "))); //视频消息的标题
			}
			if(messageResponse.getDescription()!=null) {
				xmlVideo.addElement("Description").add(new DefaultCDATA(messageResponse.getDescription().replaceAll("[\r]", ""))); //视频消息的描述
			}
		}
		else if("news".equals(messageResponse.getType())) { //回复图文消息
			/*
			<xml>
			<ToUserName><![CDATA[toUser]]></ToUserName>
			<FromUserName><![CDATA[fromUser]]></FromUserName>
			<CreateTime>12345678</CreateTime>
			<MsgType><![CDATA[news]]></MsgType>
			<ArticleCount>2</ArticleCount>
			<Articles>
			<item>
			<Title><![CDATA[title1]]></Title> 
			<Description><![CDATA[description1]]></Description>
			<PicUrl><![CDATA[picurl]]></PicUrl>
			<Url><![CDATA[url]]></Url>
			</item>
			<item>
			<Title><![CDATA[title]]></Title>
			<Description><![CDATA[description]]></Description>
			<PicUrl><![CDATA[picurl]]></PicUrl>
			<Url><![CDATA[url]]></Url>
			</item>
			</Articles>
			</xml>
			 */
			xmlElement.addElement("MsgType").add(new DefaultCDATA("news")); //消息类型
			if(messageResponse.getNews()==null || messageResponse.getNews().isEmpty()) {
				return false;
			}
			xmlElement.addElement("ArticleCount").setText("" + messageResponse.getNews().size()); //图文消息个数，限制为10
			Element xmlArticles = xmlElement.addElement("Articles");
			for(Iterator iterator = messageResponse.getNews().iterator(); iterator.hasNext();) {
				WechatMessageNews news = (WechatMessageNews)iterator.next();
				Element xmlItem = xmlArticles.addElement("item");
				xmlItem.addElement("Title").add(new DefaultCDATA(news.getTitle().replaceAll("[\r]", "").replaceAll("[\n]", " "))); //图文消息标题 
				if(news.getDescription()!=null) {
					xmlItem.addElement("Description").add(new DefaultCDATA(news.getDescription().replaceAll("[\r]", ""))); //图文消息描述 
				}
				List images = imageService.list("wechat", "image", news.getId(), false, 1, null);
				if(images!=null && !images.isEmpty()) {
					xmlItem.addElement("PicUrl").add(new DefaultCDATA(resetUrl(wechatAccount, ((Image)images.get(0)).getUrl()))); //图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200 
				}
				if(news.getUrl()!=null) {
					xmlItem.addElement("Url").add(new DefaultCDATA(resetUrl(wechatAccount, news.getUrl()))); //点击图文消息跳转链接 
				}
			}
		}
		try {
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(xmlParser.asXML(xmlElement));
			return true;
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		return false;
	}
	
	/**
	 * 获取附件在微信服务器上的媒体ID
	 * @param wechatAccount
	 * @param mainRecord
	 * @param attachmentType
	 * @param mediaType 图片（image）、语音（voice）、视频（video）和缩略图（thumb） 
	 * @return
	 * @throws ServiceException
	 */
	private String getAttachmentMediaId(WechatAccount wechatAccount, Record mainRecord, String attachmentType, String mediaType) throws ServiceException {
		List attachments = null;
		try {
			attachments = (List)FieldUtils.getFieldValue(mainRecord, attachmentType, null);
		}
		catch (Exception e) {
		
		} 
		if(attachments==null || attachments.isEmpty()) {
			return null;
		}
		return getMediaId(wechatAccount, ((Attachment)attachments.get(0)).getFilePath(), attachmentType);
	}
	
	/**
	 * 获取文件在微信服务器上的媒体ID
	 * @param wechatAccount
	 * @param filePath
	 * @param mediaType 图片（image）、语音（voice）、视频（video）和缩略图（thumb） 
	 * @return
	 * @throws ServiceException
	 */
	private String getMediaId(WechatAccount wechatAccount, String filePath, String mediaType) throws ServiceException {
		//获取媒体记录
		WechatMedia media = (WechatMedia)getDatabaseService().findRecordByHql("from WechatMedia WechatMedia where WechatMedia.path='" + JdbcUtils.resetQuot(filePath) + "'");
		if(media!=null && (System.currentTimeMillis() - media.getUploadTime().getTime()) < 70*3600*1000) { //小于70小时,每个多媒体文件（media_id）会在上传、用户发送到微信服务器3天后自动删除
			return media.getMediaId();
		}
		OutputStream out = null;
		DataInputStream in = null;
		HttpURLConnection connection = null;
		String url = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=" + getAccessToken(wechatAccount) + "&type=" + mediaType;
		try {
			//连接微信服务器
			connection = (HttpURLConnection)new URL(url).openConnection();
			connection.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false); // post方式不能使用缓存
			//设置请求头信息
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Charset", "UTF-8");
			//设置边界
			String boundary = "----------" + System.currentTimeMillis();
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ boundary);
	
			//请求正文信息,第一部分
			File file = new File(filePath);
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("--"); // 必须多两道线
			stringBuilder.append(boundary);
			stringBuilder.append("\r\n");
			stringBuilder.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
			stringBuilder.append("Content-Type:application/octet-stream\r\n\r\n");
			//获得输出流
			out = new DataOutputStream(connection.getOutputStream());
			//输出表头
			out.write(stringBuilder.toString().getBytes("utf-8"));
			//文件正文部分,把文件已流文件的方式 推入到url中
			in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[4096];
			while((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			//结尾部分
			out.write(("\r\n--" + boundary + "--\r\n").getBytes("utf-8")); //定义最后数据分隔线
			out.flush();
		}
		catch(Exception e) {
			
		}
		finally {
			try {
				in.close();
			}
			catch(Exception e) {
				
			}
			try {
				out.close();
			}
			catch(Exception e) {
				
			}
		}
		
		//读取请求结果
		BufferedReader reader = null;
		StringBuffer buffer = new StringBuffer();
		try {
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = null;
			while((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		}
		catch(IOException e) {
			throw new ServiceException(e);
		} 
		finally {
			try {
				reader.close();
			}
			catch(Exception e) {
			
			}
		}
		if(Logger.isTraceEnabled()) {
			Logger.trace("WechatService: upload media file " + filePath + ", result is " + buffer.toString());
		}
		JSONObject jsonObject;
		try {
			jsonObject = (JSONObject)new JSONParser().parse(buffer.toString());
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
		String mediaId = (String)jsonObject.get("media_id");
		if(mediaId==null || mediaId.isEmpty()) {
			throw new ServiceException("upload media error, " + jsonObject.get("errmsg"));
		}
		if(media!=null) {
			media.setMediaId(mediaId);
			media.setUploadTime(DateTimeUtils.now());
			getDatabaseService().updateRecord(media);
		}
		else {
			media = new WechatMedia();
			media.setId(UUIDLongGenerator.generateId());
			media.setPath(filePath);
			media.setMediaId(mediaId);
			media.setUploadTime(DateTimeUtils.now());
			getDatabaseService().saveRecord(media);
		}
		return media.getMediaId();
	}
	
	/**
	 * 获取微信用户
	 * @param unitId
	 * @param openId
	 * @param retrieve 是否重新获取
	 * @return
	 * @throws ServiceException
	 */
	private WechatUser getWechatUser(WechatAccount wechatAccount, String openId, boolean retrieve) throws ServiceException {
		String hql = "from WechatUser WechatUser" +
					 " where WechatUser.unitId=" + wechatAccount.getUnitId() +
					 " and WechatUser.openId='" + JdbcUtils.resetQuot(openId) + "'";
		WechatUser wechatUser = (WechatUser)getDatabaseService().findRecordByHql(hql);
		if(wechatUser!=null && !retrieve) {
			return wechatUser;
		}
		//获取微信用户
		//https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
		//access_token: 调用接口凭证 
		//openid:普通用户的标识，对当前公众号唯一 
		//lang: 返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
		String token = getAccessToken(wechatAccount);
		if(token==null || token.isEmpty()) {
			return wechatUser;
		}
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + token + "&openid=" + openId + "&lang=zh_CN";
		JSONObject jsonObject = openWechatURL(url, null);
		//subscribe: 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。 
		//openid: 用户的标识，对当前公众号唯一 
		//nickname: 用户的昵称 
		//sex: 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知 
		//city: 用户所在城市 
		//country: 用户所在国家 
		//province: 用户所在省份 
		//language: 用户的语言，简体中文为zh_CN 
		//headimgurl: 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空 
		//subscribe_time: 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
		if("0".equals("" + jsonObject.get("subscribe"))) { //没有关注该公众号
			return wechatUser;
		}
		boolean isNew = wechatUser==null;
		if(isNew) {
			wechatUser = new WechatUser();
			wechatUser.setId(UUIDLongGenerator.generateId()); //ID
			wechatUser.setUnitId(wechatAccount.getUnitId()); //单位ID
			wechatUser.setOpenId(openId); //用户标识,对当前公众号唯一
			wechatUser.setCreated(DateTimeUtils.now()); //创建时间
		}
		wechatUser.setNickname("" + jsonObject.get("nickname")); //用户昵称
		String sex = "" + jsonObject.get("sex");
		wechatUser.setSex("1".equals(sex) ? 'M' : ("2".equals(sex) ? 'F' : '0')); //用户的性别,M/F
		wechatUser.setCity("" + jsonObject.get("city")); //用户所在城市
		wechatUser.setProvince("" + jsonObject.get("province")); //用户所在省份
		wechatUser.setCountry("" + jsonObject.get("country")); //用户所在国家
		wechatUser.setLanguage("" + jsonObject.get("language")); //用户的语言,简体中文为zh_CN
		wechatUser.setHeadimgUrl("" + jsonObject.get("headimgurl")); //用户头像,最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
		try {
			wechatUser.setSubscribeTime(new Timestamp(Long.parseLong("" + jsonObject.get("subscribe_time")) * 1000)); //用户关注时间,为时间戳。如果用户曾多次关注，则取最后关注时间
		}
		catch(Exception e) {
			
		}
		wechatUser.setUnsubscribeTime(null); //取消关注时间
		if(isNew) {
			save(wechatUser);
		}
		else {
			update(wechatUser);
		}
		return wechatUser;
	}
	
	/**
	 * 微信签名验证
	 * @param token
	 * @param request
	 * @return
	 **/
	private boolean checkSignature(String token, HttpServletRequest request) {
		String signature = request.getParameter("signature"); //微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。 
		String timestamp = request.getParameter("timestamp"); //时间戳 
		String nonce = request.getParameter("nonce"); //随机数 
		//加密/校验流程如下：
		//1. 将token、timestamp、nonce三个参数进行字典序排序
		//2. 将三个参数字符串拼接成一个字符串进行sha1加密
		//3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
		List list = ListUtils.generateListFromArray(new String[]{token, timestamp, nonce});
		Collections.sort(list);
		try {
			return signature.equalsIgnoreCase(Encoder.getInstance().sha1(ListUtils.join(list, "", false)));
		}
		catch(Exception e) {
			Logger.exception(e);
			return false;
		}
	}
	
	/**
	 * 获取公众号全局唯一票据,access_token是公众号的全局唯一票据，公众号调用各接口时都需使用access_token。正常情况下access_token有效期为7200秒，重复获取将导致上次获取的access_token失效。由于获取access_token的api调用次数非常有限，建议开发者全局存储与更新access_token，频繁刷新access_token会导致api调用受限，影响自身业务。
	 * @return
	 **/
	private String getAccessToken(WechatAccount wechatAccount) throws ServiceException {
		//从列表中获取
		AccessToken accessToken = null;
		for(Iterator iterator = accessTokens.iterator(); iterator.hasNext();) {
			AccessToken token = (AccessToken)iterator.next();
			if(System.currentTimeMillis()>token.getExpires()) {
				iterator.remove();
			}
			else if(token.getAppid().equals(wechatAccount.getAppId()) && token.getSecret().equals(wechatAccount.getAppSecret())) {
				accessToken = token;
			}
		}
		if(accessToken!=null) {
			return accessToken.getToken();
		}
		if(wechatAccount.getAppId()==null || wechatAccount.getAppId().isEmpty()) {
			return null;
		}
		//访问微信服务器
		//https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
		//grant_type: 获取access_token填写client_credential
		//appid: 第三方用户唯一凭证 
		//secret: 第三方用户唯一凭证密钥，即appsecret 
		//正常情况下，微信会返回下述JSON数据包给公众号： {"access_token":"ACCESS_TOKEN","expires_in":7200}
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + wechatAccount.getAppId() + "&secret=" + wechatAccount.getAppSecret();
		JSONObject jsonObject = openWechatURL(url, null);
		accessToken = new AccessToken();
		accessToken.setAppid(wechatAccount.getAppId()); //第三方用户唯一凭证 
		accessToken.setSecret(wechatAccount.getAppSecret()); //第三方用户唯一凭证密钥，即appsecret 
		accessToken.setToken("" + jsonObject.get("access_token")); //令牌
		int seconds = Integer.parseInt("" + jsonObject.get("expires_in")) - 20;
		accessToken.setExpires(System.currentTimeMillis() + seconds * 1000); //有效期
		accessTokens.add(accessToken);
		return accessToken.getToken();
	}
	
	/**
	 * 打开URL,并解析JSON对象
	 * @param url
	 * @param postData
	 * @return
	 * @throws ServiceException
	 */
	private JSONObject openWechatURL(String url, String postData) throws ServiceException {
		try {
			if(Logger.isTraceEnabled()) {
				Logger.trace("WechatService: open url " + url);
			}
			String json;
			if(postData==null) {
				json = HttpUtils.getHttpContent(url, null, true, null, 8000).getResponseBody();
			}
			else {
				json = HttpUtils.doPost(url, "utf-8", new NameValuePair[]{new NameValuePair(null, postData)}, null).getResponseBody();
			}
			if(Logger.isTraceEnabled()) {
				Logger.trace("WechatService: response is " + json);
			}
			return (JSONObject)new JSONParser().parse(json);
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.workflow.client.configure.WorkflowConfigureCallback#processWorkflowConfigureNotify(java.lang.String, java.lang.String, long, com.yuanluesoft.workflow.client.model.definition.WorkflowPackage, javax.servlet.http.HttpServletRequest)
	 */
	public void processWorkflowConfigureNotify(String workflowId, String workflowConfigureAction, long userId, WorkflowPackage workflowPackage, HttpServletRequest notifyRequest) throws Exception {
		long orgId = RequestUtils.getParameterLongValue(notifyRequest, "orgId"); //组织机构ID
		WechatWorkflowConfig workflowConfig = getWorkflowConfig(orgId, false);
		if(WorkflowConfigureCallback.CONFIGURE_ACTION_DELETE.equals(workflowConfigureAction)) { //流程已删除
			getDatabaseService().deleteRecord(workflowConfig);
			return;
		}
		boolean isNew = (workflowConfig==null);
		if(isNew) {
			workflowConfig = new WechatWorkflowConfig();
			workflowConfig.setId(UUIDLongGenerator.generateId()); //ID
			workflowConfig.setOrgId(orgId); //机构ID
		}
		workflowConfig.setWorkflowId(workflowId); //流程ID
		workflowConfig.setWorkflowName(workflowPackage.getName()); //流程名称
		if(isNew) {
			getDatabaseService().saveRecord(workflowConfig);
		}
		else {
			getDatabaseService().updateRecord(workflowConfig);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.wechat.service.WechatService#saveUserGroupMembers(long, java.lang.String)
	 */
	public void saveUserGroupMembers(long groupId, String memberIds) throws ServiceException {
		//删除原有的成员列表
		getDatabaseService().deleteRecordsByHql("from WechatUserGroupMember WechatUserGroupMember where WechatUserGroupMember.groupId=" + groupId);
		//添加新的成员列表
		if(memberIds==null || memberIds.isEmpty()) {
			return;
		}
		List users = getDatabaseService().findRecordsByHql("from WechatUser WechatUser where WechatUser.id in (" + JdbcUtils.validateInClauseNumbers(memberIds) + ")");
		String[] ids = memberIds.split(",");
		for(int i=0; i<ids.length; i++) {
			WechatUser user = (WechatUser)ListUtils.findObjectByProperty(users, "id", new Long(ids[i]));
			if(user==null) {
				continue;
			}
			WechatUserGroupMember groupMember = new WechatUserGroupMember();
			groupMember.setId(UUIDLongGenerator.generateId()); //ID
			groupMember.setGroupId(groupId); //分组ID
			groupMember.setMemberId(user.getId()); //成员ID
			groupMember.setMemberNickname(user.getNickname()); //成员昵称
			save(groupMember);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.wechat.service.WechatService#issue(com.yuanluesoft.wechat.pojo.WechatMessageSend, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void issue(WechatMessageSend messageSend, SessionInfo sessionInfo) throws ServiceException {
		WechatAccount wechatAccount = getWechatAccountByUnitId(messageSend.getUnitId());
		JSONObject jsonObject = new JSONObject();
		if(messageSend.getReceiveMessageId()>0) { //客服消息
			String hql = "select WechatMessageReceive.fromUserOpenId" +
						 " from WechatMessageReceive WechatMessageReceive" +
						 " where WechatMessageReceive.id=" + messageSend.getReceiveMessageId();
			String openId = (String)getDatabaseService().findRecordByHql(hql);
			if(openId==null) {
				return;
			}
			jsonObject.put("touser", openId);
		}
		else if(messageSend.getRangeMode()==0) { //全部用户
			JSONObject group = new JSONObject();
			group.put("is_to_all", Boolean.TRUE); //使用is_to_all为true且成功群发，会使得此次群发进入历史消息列表,is_to_all为false时是可以多次群发的，但每个用户只会收到最多4条，且这些群发不会进入历史消息列表
			group.put("group_id", "0"); //0/未分组, 1/黑名单, 2/星标组 
			jsonObject.put("filter", group);
		}
		else { //指定用户
			//获取用户列表
			List openIds = null;
			if(messageSend.getUserIds()!=null && !messageSend.getUserIds().isEmpty()) {
				String hql = "select WechatUser.openId" +
							 " from WechatUser WechatUser" +
							 " where WechatUser.id in (" + messageSend.getUserIds() + ")";
				openIds = getDatabaseService().findRecordsByHql(hql);
			}
			if(openIds==null) {
				openIds = new ArrayList();
			}
			if(messageSend.getGroupIds()!=null && !messageSend.getGroupIds().isEmpty()) {
				String hql = "select WechatUser.openId" +
				 			 " from WechatUser WechatUser, WechatUserGroupMember WechatUserGroupMember" +
				 			 " where WechatUserGroupMember.memberId=WechatUser.id" +
				 			 " and WechatUserGroupMember.groupId in (" + messageSend.getGroupIds() + ")";
				List groupOpenIds = getDatabaseService().findRecordsByHql(hql);
				for(Iterator iterator = groupOpenIds==null ? null : groupOpenIds.iterator(); iterator!=null && iterator.hasNext();) {
					String openId = (String)iterator.next();
					if(openIds.indexOf(openId)==-1) {
						openIds.add(openId);
					}
				}
			}
			if(openIds.isEmpty()) {
				return;
			}
			JSONArray tousers = new JSONArray();
			for(Iterator iterator = openIds.iterator(); iterator.hasNext();) {
				String openId = (String)iterator.next();
				tousers.add(openId);
			}
			jsonObject.put("touser", tousers);
		}
		String msgType = messageSend.getType();
		if("text".equals(msgType)) { //文本
			if(messageSend.getContent()==null || messageSend.getContent().isEmpty()) {
				return;
			}
			/*
			 "text":{
			      "content":"CONTENT"
			 }
			 */
			JSONObject text = new JSONObject();
			text.put("content", messageSend.getContent());
			jsonObject.put("text", text);
		}
		else if("voice".equals(msgType)) { //语音
			/* 
			"voice":{
		      "media_id":"123dsdajkasd231jhksad"
		    }
		    */
			String mediaId = getAttachmentMediaId(wechatAccount, messageSend, "voice", "voice");
			if(mediaId==null) {
				return;
			}
			JSONObject voice = new JSONObject();
			voice.put("media_id", mediaId);
			jsonObject.put("voice", voice);
		}
		else if("image".equals(msgType)) { //图片
			/* 
			"image":{
		      "media_id":"123dsdajkasd231jhksad"
		    }
		    */
			String mediaId = getAttachmentMediaId(wechatAccount, messageSend, "image", "image");
			if(mediaId==null) {
				return;
			}
			JSONObject image = new JSONObject();
			image.put("media_id", mediaId);
			jsonObject.put("image", image);
		}
		else if("video".equals(msgType)) { //视频
			/* 
			"video":{
		      "media_id":"123dsdajkasd231jhksad",
		      "title":"TITLE",
		      "description":"DESCRIPTION"
		    }
		    */
			String mediaId = getAttachmentMediaId(wechatAccount, messageSend, "video", "video");
			if(mediaId==null) {
				return;
			}
			/*此处视频的media_id需通过POST请求到下述接口特别地得到： https://file.api.weixin.qq.com/cgi-bin/media/uploadvideo?access_token=ACCESS_TOKEN POST数据如下（此处media_id需通过基础支持中的上传下载多媒体文件来得到）：
			{
			  "media_id": "rF4UdIMfYK3efUfyoddYRMU50zMiRmmt_l0kszupYh_SzrcW5Gaheq05p_lHuOTQ",
			  "title": "TITLE",
			  "description": "Description"
			}
			 
			返回将为 
			{
			  "type":"video",
			  "media_id":"IhdaAQXuvJtGzwwc0abfXnzeezfO0NgPK6AQYShD8RQYMTtfzbLdBIQkQziv2XJc",
			  "created_at":1398848981
			}
			*/
			JSONObject video = new JSONObject();
			video.put("media_id", mediaId);
			if(messageSend.getTitle()!=null) {
				video.put("title", messageSend.getTitle().replaceAll("[\r]", "").replaceAll("[\n]", " "));
			}
			if(messageSend.getDescription()!=null) {
				video.put("description", messageSend.getDescription().replaceAll("[\r]", ""));
			}
			if(messageSend.getReceiveMessageId()==0) { //不是客服消息
				String url = "https://file.api.weixin.qq.com/cgi-bin/media/uploadvideo?access_token=" + getAccessToken(wechatAccount);
				video = openWechatURL(url, video.toString());
				mediaId = (String)video.get("media_id");
				if(mediaId==null || mediaId.isEmpty()) {
					throw new ServiceException("upload video error, " + jsonObject.get("errmsg"));
				}
				if(messageSend.getRangeMode()==0) { //全部用户
					msgType = "mpvideo";
				}
				video = new JSONObject();
				video.put("media_id", mediaId);
				if(messageSend.getTitle()!=null) {
					video.put("title", messageSend.getTitle().replaceAll("[\r]", "").replaceAll("[\n]", " "));
				}
				if(messageSend.getDescription()!=null) {
					video.put("description", messageSend.getDescription().replaceAll("[\r]", ""));
				}
			}
			jsonObject.put(msgType, video);
		}
		else if("news".equals(msgType)) { //图文消息
			if(messageSend.getNews()==null || messageSend.getNews().isEmpty()) {
				return;
			}
			if(messageSend.getReceiveMessageId()>0) { //客服消息
				/*
				"news":{
		        	"articles": [
			         {
			             "title":"Happy Day",
			             "description":"Is Really A Happy Day",
			             "url":"URL",
			             "picurl":"PIC_URL"
			         },
			         {
			             "title":"Happy Day",
			             "description":"Is Really A Happy Day",
			             "url":"URL",
			             "picurl":"PIC_URL"
			         }
			         ]
			    }
				*/
				JSONArray articles = new JSONArray();
				for(Iterator iterator = messageSend.getNews().iterator(); iterator.hasNext();) {
					WechatMessageNews news = (WechatMessageNews)iterator.next();
					JSONObject article = new JSONObject();
					article.put("title", resetQuot(news.getTitle().replaceAll("[\r]", "").replaceAll("[\n]", " "), false)); //图文消息的标题 
					if(news.getDescription()!=null) {
						article.put("description", resetQuot(news.getDescription().replaceAll("[\r]", ""), false)); //图文消息的描述 
					}
					if(news.getUrl()!=null) {
						article.put("url", resetUrl(wechatAccount, news.getUrl())); //在图文消息页面点击“阅读原文”后的页面 
					}
					List images = imageService.list("wechat", "image", news.getId(), false, 1, null);
					if(images!=null && !images.isEmpty()) {
						article.put("picurl", resetUrl(wechatAccount, ((Image)images.get(0)).getUrl())); //图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80 
					}
					articles.add(article);
				}
				JSONObject news = new JSONObject();
				news.put("articles", articles);
				jsonObject.put("news", news);
			}
			else { //不是客服消息
				msgType = "mpnews";
				/*
				上传图文消息素材 https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=ACCESS_TOKEN
				POST数据示例如下： 
				{
				   "articles": [
						 {
				             "thumb_media_id":"qI6_Ze_6PtV7svjolgs-rN6stStuHIjs9_DidOHaj0Q-mwvBelOXCFZiq2OsIU-p",
				             "author":"xxx",
							 "title":"Happy Day",
							 "content_source_url":"www.qq.com",
							 "content":"content",
							 "digest":"digest",
							 "show_cover_pic":"1"
						 },
						 {
				             "thumb_media_id":"qI6_Ze_6PtV7svjolgs-rN6stStuHIjs9_DidOHaj0Q-mwvBelOXCFZiq2OsIU-p",
				             "author":"xxx",
							 "title":"Happy Day",
							 "content_source_url":"www.qq.com",
							 "content":"content",
							 "digest":"digest",
							 "show_cover_pic":"0"
						 }
				   ]
				}
				返回数据示例（正确时的JSON返回结果）： 
				{
				   "type":"news",
				   "media_id":"CsEf3ldqkAYJAU6EJeIkStVDSvffUJ54vqbThMgplD-VJXXof6ctX5fI6-aYyUiQ",
				   "created_at":1391857799
				}
				*/
				JSONObject uploadArticles = new JSONObject();
				JSONArray articles = new JSONArray();
				for(Iterator iterator = messageSend.getNews().iterator(); iterator.hasNext();) {
					WechatMessageNews news = (WechatMessageNews)iterator.next();
					String mediaId = getAttachmentMediaId(wechatAccount, news, "image", "thumb");
					if(mediaId==null) {
						continue;
					}
					JSONObject article = new JSONObject();
					article.put("thumb_media_id", mediaId); //图文消息缩略图的media_id，可以在基础支持-上传多媒体文件接口中获得 
					if(news.getAuthor()!=null) {
						article.put("author", news.getAuthor()); //图文消息的作者 
					}
					article.put("title", resetQuot(news.getTitle().replaceAll("[\r]", "").replaceAll("[\n]", " "), false)); //图文消息的标题 
					String url = null;
					if(news.getUrl()!=null) {
						url = resetUrl(wechatAccount, news.getUrl());
						article.put("content_source_url", url); //在图文消息页面点击“阅读原文”后的页面 
					}
					article.put("content", resetQuot(url==null ? news.getContent() : StringUtils.resetResorcePath(url, news.getContent()), true)); //图文消息页面的内容，支持HTML标签
					if(news.getDescription()!=null) {
						article.put("digest", resetQuot(news.getDescription().replaceAll("[\r]", ""), false)); //图文消息的描述 
					}
					article.put("show_cover_pic", "" + (news.getShowCoverPic()==2 ? messageSend.getShowCoverPic() : news.getShowCoverPic())); //是否显示封面，1为显示，0为不显示
					articles.add(article);
				}
				if(articles.isEmpty()) {
					return;
				}
				uploadArticles.put("articles", articles);
				String url = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=" + getAccessToken(wechatAccount); 
				uploadArticles = openWechatURL(url, uploadArticles.toString());
				String mediaId = (String)uploadArticles.get("media_id");
				if(mediaId==null || mediaId.isEmpty()) {
					throw new ServiceException("upload news error, " + jsonObject.get("errmsg"));
				}
				/*
				"mpnews":{
			      "media_id":"123dsdajkasd231jhksad"
			    },
				*/
				JSONObject mpnews = new JSONObject();
				mpnews.put("media_id", mediaId);
				jsonObject.put("mpnews", mpnews);
			}
		}
		jsonObject.put("msgtype", msgType);
		if(Logger.isTraceEnabled()) {
			Logger.trace("WechatService: send message " + jsonObject.toJSONString());
		}
		/*
		返回数据示例（正确时的JSON返回结果）： 
		{
		   "errcode":0,
		   "errmsg":"send job submission success",
		   "msg_id":34182
		}
		*/
		String url;
		if(messageSend.getReceiveMessageId()>0) { //客服消息
			url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + getAccessToken(wechatAccount);
		}
		else if(messageSend.getRangeMode()==0) { //全部用户
			url = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=" + getAccessToken(wechatAccount);
		}
		else { //指定用户
			url = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=" + getAccessToken(wechatAccount);
		}
		jsonObject = openWechatURL(url, jsonObject.toString());
		if(!"0".equals("" + jsonObject.get("errcode"))) {
			String error = "" + jsonObject.get("errmsg");
			if("api unauthorized".equals(error)) {
				throw new ValidateException("没有发送消息的授权");
			}
			throw new ServiceException("send failed, " + error);
		}
		messageSend.setSendTime(DateTimeUtils.now());
		messageSend.setMsgId("" + jsonObject.get("msg_id"));
		update(messageSend);
		if(messageSend.getReceiveMessageId()>0) { //客服消息
			WechatMessageReceive messageReceive = (WechatMessageReceive)load(WechatMessageReceive.class, messageSend.getReceiveMessageId());
			messageReceive.setReplyTime(DateTimeUtils.now());
			messageReceive.setReplier(sessionInfo.getUserName());
			messageReceive.setReplierId(sessionInfo.getUserId());
			update(messageReceive);
		}
	}
	
	/**
	 * 重置中文引号
	 * @param text
	 * @param isHtml
	 * @return
	 */
	private String resetQuot(String text, boolean isHtml) {
		if(text==null) {
			return text;
		}
		return text.replaceAll("“", isHtml ? "&ldquo;" : "\"")
				   .replaceAll("”", isHtml ? "&rdquo;" : "\"")
				   .replaceAll("‘", isHtml ? "&lsquo;" : "'")
				   .replaceAll("’", isHtml ? "&rsquo;" : "'")
				   .replaceAll("—", "-")
				   .replaceAll("…", "...")
				   .replaceAll(" ", isHtml ? "&nbsp;" : " "); //空格%E2%80%82
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.wechat.service.WechatService#createWechatMessageNews(java.util.List, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public String createWechatMessageNews(List newsList, final SessionInfo sessionInfo) throws ServiceException {
		if(!orgService.checkPopedom(sessionInfo.getUnitId(), "wechatMessageManager", sessionInfo)) {
			throw new ServiceException("no privilege");
		}
		String workflowInstanceId = null;
		WechatMessageSend messageSend = new WechatMessageSend();
		try {
			messageSend.setId(UUIDLongGenerator.generateId()); //ID
			messageSend.setUnitId(sessionInfo.getUnitId()); //单位ID
			messageSend.setType("news"); //消息类型,text/image/voice/video/news
			messageSend.setCreatorId(sessionInfo.getUserId()); //创建人ID
			messageSend.setCreator(sessionInfo.getUserName()); //创建人
			messageSend.setCreated(DateTimeUtils.now()); //创建时间
			messageSend.setRangeMode(0); //发送范围,0/全部,1/指定分组,2/指定用户
			//创建流程实例
			WechatWorkflowConfig workflowConfig = getWorkflowConfig(sessionInfo.getUnitId(), true);
			if(workflowConfig==null) {
				throw new ServiceException("Approval workflows are not exists.");
			}
			//按ID查找流程
			WorkflowParticipantCallback workflowParticipantCallback = new WorkflowParticipantCallback() {
				public boolean isMemberOfProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
					return false;
				}
				public List listProgrammingParticipants(String programmingParticipantId, String programmingParticipantName, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
					if("wechatMessageManager".equals(programmingParticipantId)) {
						return getOrgService().listDirectoryVisitors(sessionInfo.getUnitId(), "wechatMessageManager", true, false, 0);
					}
					return null;
				}
				public List resetParticipants(List participants, boolean anyoneParticipate, WorkflowData workflowData, SessionInfo sessionInfo) throws ServiceException {
					return null;
				}
			};
			com.yuanluesoft.workflow.client.model.runtime.WorkflowEntry workflowEntry = getWorkflowExploitService().getWorkflowEntry(workflowConfig.getWorkflowId(), workflowParticipantCallback, (WorkflowData)messageSend, sessionInfo);
			if(workflowEntry==null) {
				throw new ServiceException("Workflow entry not exists.");
			}
			workflowInstanceId = workflowExploitService.createWorkflowInstance(workflowEntry.getWorkflowId(), ((com.yuanluesoft.jeaf.base.model.Element)workflowEntry.getActivityEntries().get(0)).getId(), false, messageSend, workflowParticipantCallback, sessionInfo);
			messageSend.setWorkflowInstanceId(workflowInstanceId);
			getDatabaseService().saveRecord(messageSend);
			
			String siteLogoPath = null; //获取站点LOGO
			//添加图文消息
			for(Iterator iterator = newsList.iterator(); iterator.hasNext();) {
				MessageNews messageNews = (MessageNews)iterator.next();
				WechatMessageNews news = new WechatMessageNews();
				PropertyUtils.copyProperties(news, messageNews);
				if(news.getTitle()!=null && news.getTitle().length()>100) {
					news.setTitle(news.getTitle().replaceAll("[\r]", "").replaceAll("[\n]", " ").substring(0, 100));
				}
				if(news.getDescription()!=null && news.getDescription().length()>120) {
					news.setDescription(news.getDescription().replaceAll("[\r]", "").substring(0, 120));
				}
				if(news.getContent()!=null && news.getContent().length()>20000) {
					news.setContent(news.getContent().substring(0, 20000));
				}
				if(news.getAuthor()!=null && news.getAuthor().length()>25) {
					news.setAuthor(news.getAuthor().substring(0, 25));
				}
				news.setId(UUIDLongGenerator.generateId()); //ID
				news.setMessageId(messageSend.getId()); //消息ID
				getDatabaseService().saveRecord(news);
				//上传图片
				String imageFilePath = messageNews.getImageFilePath();
				if(imageFilePath==null || imageFilePath.isEmpty()) {
					if(siteLogoPath==null) {
						siteLogoPath = "";
						List logo;
				    	//获取用户站点
						WebSite webSite = siteService.getSiteByOwnerUnitId(sessionInfo.getUnitId());
						if(webSite!=null && (logo=(List)FieldUtils.getFieldValue(webSite, "logo", null))!=null && !logo.isEmpty()) {
				    		siteLogoPath = ((Attachment)logo.get(0)).getFilePath();
				    	}
				    }
					imageFilePath = siteLogoPath;
				}
				if(imageFilePath!=null && !imageFilePath.isEmpty()) {
					imageService.uploadFile("wechat", "image", FieldUtils.getRecordField(WechatMessageNews.class.getName(), "image", null), news.getId(), imageFilePath);
				}
			}
			return Environment.getContextPath() + "/wechat/sendMessage.shtml?act=edit&id=" + messageSend.getId();
		}
		catch(Exception e) {
			if(workflowInstanceId!=null) {
				workflowExploitService.removeWorkflowInstance(workflowInstanceId, messageSend, sessionInfo);
			}
			throw e instanceof ServiceException ? (ServiceException)e : new ServiceException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, java.lang.Object, com.yuanluesoft.jeaf.business.model.Field, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("responseType".equals(itemsName)) { //消息响应类型
			//subscribe(关注)、unsubscribe(取消关注)、menu_(菜单事件)、location(上报地理位置事件) 、qrscene(扫描带参数二维码)、talk(用户发言)
			//获取菜单配置
			Number unitId = null;
			try {
				unitId = (Number)PropertyUtils.getProperty(bean, "unitId");
			}
			catch(Exception e) {
				
			}
			List items = (List)request.getAttribute("responseType" + unitId);
			if(items!=null) {
				return items;
			}
			items = new ArrayList();
			items.add(new Object[]{"关注", "subscribe"});
			items.add(new Object[]{"取消关注", "unsubscribe"});
			items.add(new Object[]{"上报地理位置", "location"});
			items.add(new Object[]{"扫描带参数二维码", "qrscene"});
			items.add(new Object[]{"用户发言", "talk"});
			String hql = "select WechatMenuItem" +
						 " from WechatMenuItem WechatMenuItem, WechatAccount WechatAccount" +
						 " where WechatMenuItem.accountId=WechatAccount.id" +
						 " and WechatAccount.unitId=" + unitId +
						 " and WechatMenuItem.type=2" +
						 " order by WechatMenuItem.id";
			List menuItems = getDatabaseService().findRecordsByHql(hql);
			for(Iterator iterator = menuItems==null ? null : menuItems.iterator(); iterator!=null && iterator.hasNext();) {
				WechatMenuItem menuItem = (WechatMenuItem)iterator.next();
				items.add(new Object[]{"点击菜单:" + menuItem.getName(), "menu_" + menuItem.getId()});
			}
			request.setAttribute("responseType" + unitId, items);
			return items;
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}

	/**
	 * 微信令牌
	 * @author linchuan
	 *
	 */
	private class AccessToken {
		private String appid; //第三方用户唯一凭证 
		private String secret; //第三方用户唯一凭证密钥，即appsecret 
		private String token; //令牌
		private long expires; //有效期
		
		/**
		 * @return the expires
		 */
		public long getExpires() {
			return expires;
		}
		/**
		 * @param expires the expires to set
		 */
		public void setExpires(long expires) {
			this.expires = expires;
		}
		/**
		 * @return the token
		 */
		public String getToken() {
			return token;
		}
		/**
		 * @param token the token to set
		 */
		public void setToken(String token) {
			this.token = token;
		}
		/**
		 * @return the appid
		 */
		public String getAppid() {
			return appid;
		}
		/**
		 * @param appid the appid to set
		 */
		public void setAppid(String appid) {
			this.appid = appid;
		}
		/**
		 * @return the secret
		 */
		public String getSecret() {
			return secret;
		}
		/**
		 * @param secret the secret to set
		 */
		public void setSecret(String secret) {
			this.secret = secret;
		}
	}

	/**
	 * @return the orgService
	 */
	public OrgService getOrgService() {
		return orgService;
	}

	/**
	 * @param orgService the orgService to set
	 */
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	/**
	 * @return the workflowExploitService
	 */
	public WorkflowExploitService getWorkflowExploitService() {
		return workflowExploitService;
	}

	/**
	 * @param workflowExploitService the workflowExploitService to set
	 */
	public void setWorkflowExploitService(
			WorkflowExploitService workflowExploitService) {
		this.workflowExploitService = workflowExploitService;
	}

	/**
	 * @return the imageService
	 */
	public ImageService getImageService() {
		return imageService;
	}

	/**
	 * @param imageService the imageService to set
	 */
	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	/**
	 * @return the siteService
	 */
	public SiteService getSiteService() {
		return siteService;
	}

	/**
	 * @param siteService the siteService to set
	 */
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}
}