package com.yuanluesoft.im.service.spring;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.taglib.TagUtils;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.callback.GeneratePartPageCallback;
import com.yuanluesoft.im.cs.pojo.CSParameter;
import com.yuanluesoft.im.cs.pojo.CSSpecialist;
import com.yuanluesoft.im.cs.service.CSService;
import com.yuanluesoft.im.model.IMClientLogin;
import com.yuanluesoft.im.model.IMPerson;
import com.yuanluesoft.im.model.IMSession;
import com.yuanluesoft.im.model.message.ChatCreateFailed;
import com.yuanluesoft.im.model.message.ChatDetail;
import com.yuanluesoft.im.model.message.ChatDetailRequest;
import com.yuanluesoft.im.model.message.CloseCustomerServiceChat;
import com.yuanluesoft.im.model.message.CreateChat;
import com.yuanluesoft.im.model.message.CreateCustomerServiceChat;
import com.yuanluesoft.im.model.message.CreateGroupChat;
import com.yuanluesoft.im.model.message.ExitClient;
import com.yuanluesoft.im.model.message.FileTransferAck;
import com.yuanluesoft.im.model.message.FileTransferCancel;
import com.yuanluesoft.im.model.message.FileTransferComplete;
import com.yuanluesoft.im.model.message.FileTransferRequest;
import com.yuanluesoft.im.model.message.KeepAlive;
import com.yuanluesoft.im.model.message.Login;
import com.yuanluesoft.im.model.message.LoginAck;
import com.yuanluesoft.im.model.message.Logoff;
import com.yuanluesoft.im.model.message.Message;
import com.yuanluesoft.im.model.message.OfflineCheck;
import com.yuanluesoft.im.model.message.OnlineCountChanged;
import com.yuanluesoft.im.model.message.PersonStatusChanged;
import com.yuanluesoft.im.model.message.RetrieveMessage;
import com.yuanluesoft.im.model.message.SavePersonalSetting;
import com.yuanluesoft.im.model.message.SystemMessage;
import com.yuanluesoft.im.model.message.SystemMessageDetail;
import com.yuanluesoft.im.model.message.SystemMessageDetailRequest;
import com.yuanluesoft.im.model.message.SystemMessageFeedback;
import com.yuanluesoft.im.model.message.Talk;
import com.yuanluesoft.im.model.message.TalkDetail;
import com.yuanluesoft.im.model.message.TalkDetailRequest;
import com.yuanluesoft.im.model.message.TalkSubmit;
import com.yuanluesoft.im.parser.MessageParser;
import com.yuanluesoft.im.pojo.IMChat;
import com.yuanluesoft.im.pojo.IMChatPerson;
import com.yuanluesoft.im.pojo.IMChatTalk;
import com.yuanluesoft.im.pojo.IMPersonalSetting;
import com.yuanluesoft.im.service.IMService;
import com.yuanluesoft.im.service.MessageWriter;
import com.yuanluesoft.jeaf.application.model.navigator.ApplicationNavigator;
import com.yuanluesoft.jeaf.application.model.navigator.applicationtree.ApplicationTreeNavigator;
import com.yuanluesoft.jeaf.application.model.navigator.definition.ApplicationNavigatorDefinition;
import com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.cache.exception.CacheException;
import com.yuanluesoft.jeaf.distribution.service.DistributionService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.membermanage.model.Member;
import com.yuanluesoft.jeaf.membermanage.service.MemberServiceList;
import com.yuanluesoft.jeaf.messagecenter.service.MessageService;
import com.yuanluesoft.jeaf.network.datagramchannel.model.DatagramConnection;
import com.yuanluesoft.jeaf.network.datagramchannel.model.packet.DataPacket;
import com.yuanluesoft.jeaf.network.datagramchannel.serverchannel.DatagramServerChannel;
import com.yuanluesoft.jeaf.network.datagramchannel.serverchannel.listener.DatagramChannelListener;
import com.yuanluesoft.jeaf.network.exception.NetworkException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.tree.model.Tree;
import com.yuanluesoft.jeaf.tree.model.TreeNode;
import com.yuanluesoft.jeaf.usermanage.util.PersonUtils;
import com.yuanluesoft.jeaf.util.Base64Decoder;
import com.yuanluesoft.jeaf.util.Base64Encoder;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.ObjectSerializer;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.jeaf.util.UUIDStringGenerator;
import com.yuanluesoft.jeaf.util.threadpool.Task;
import com.yuanluesoft.jeaf.util.threadpool.ThreadPool;

/**
 * IM服务
 * @author linchuan
 *
 */
public class IMServiceImpl extends BusinessServiceImpl implements IMService, ApplicationNavigatorService, DatagramChannelListener {
	//IM参数
	private int messageLimit = 100000; //消息数量上限,超过以后,系统定时删除最早的消息,默认10万条
	private boolean friendEnabled = false; //是否启用朋友管理,如果启用,则获取在线用户列表时只获取朋友
	private int notifyDelaySeconds = 20; //消息通知延迟,默认20秒,由于缓存时间是30秒,所有不能超过30秒
	private int attachmentSaveDays = 7; //附件在服务器上的保存天数
	private String serverAddress; //服务器地址，可以是域名和IP,如果没有指定,自动使用HTTP登录时的地址
	private String udpPorts = "8000-8008,9000,9010"; //UDP端口列表,格式如：8000-8008,9000,9010
	
	//IM引用的服务
	private Cache ticketCache; //用户登录钥匙缓存
	private Cache sessionCache; //上线用户缓存,缓存时间为30秒钟
	private Cache chatCache; //对话缓存,缓存对话的用户ID列表,缓存时间为30分钟
	private Cache systemMessageCache; //系统消息缓存
	private Cache serverInfoCache; //IM服务器信息连接缓存,分布式缓存,用来存放服务器分布式部署时的访问地址(webApplicationDistributeUrl),IM会话(IMSession)中仅记录服务器序号
	private Cache udpConnectionCache; //UDP连接缓存,非分布式缓存

	private MemberServiceList memberServiceList; //成员服务列表
	private DistributionService distributionService; //分布式服务
	private MessageService messageService; //消息中心
	private CSService csService; //客服服务
	private PageService pageService; //页面服务
	
	//私有属性
	private Character serverIndex = null; //服务器索引号
	private List udpChannels = new ArrayList(); //UDP通道列表
	private int currentUdpChannelIndex = 0; //最后分配的UDP通道序号
	private Map mutexMap = new HashMap(); //消息监听列表
	private ThreadPool threadPool = new ThreadPool(200, 50, 10000); //线程池,最大200个线程,50个空闲线程,最长等待10秒
	private MessageParser messageParser = new MessageParser(); //消息解析器
	
	/**
	 * 启动
	 */
	public void startup() {
		try {
			Logger.info("IMService: startup.");
			String[] ports = udpPorts.split(",");
			for(int i=0; i<ports.length; i++) {
				int minPort;
				int maxPort;
				int index = ports[i].indexOf('-');
				if(index==-1) {
					minPort = maxPort = Integer.parseInt(ports[i]);
				}
				else {
					minPort = Integer.parseInt(ports[i].substring(0, index));
					maxPort = Integer.parseInt(ports[i].substring(index + 1));
				}
				for(; minPort<=maxPort; minPort++) {
					DatagramServerChannel datagramChannel = new DatagramServerChannel(udpConnectionCache);
					datagramChannel.openChannel(minPort);
					datagramChannel.addListener(this);
					udpChannels.add(datagramChannel);
				}
			}
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.im.server.channel.listener.DatagramChannelListener#onDataArrive(com.yuanluesoft.im.server.channel.DatagramChannel, com.yuanluesoft.im.server.channel.model.DatagramConnection)
	 */
	public void onDataArrive(final DatagramServerChannel channel, final DatagramConnection connection) {
		byte receiveBuffer[] = new byte[(10000 / DataPacket.MAX_DATA_LENGTH + 1) * DataPacket.MAX_DATA_LENGTH]; //10K
		int dataLength = 0;
		byte[] data;
		try {
			while((data=channel.readData(connection))!=null) {
				System.arraycopy(data, 0, receiveBuffer, dataLength, data.length);
				dataLength += data.length;
			}
		}
		catch (Exception e) {
			Logger.exception(e);
			return;
		}
		Message message = messageParser.parseMessage(receiveBuffer, dataLength);
		if(message==null) {
			return;
		}
		if(Logger.isDebugEnabled()) {
			Logger.debug("IMService: receive a message, class name is " + message.getClass().getName() + ", length is " + dataLength + ".");
		}
		//处理接收到的消息
		final String connectionInfo = udpChannels.indexOf(channel) + "/" + connection.getRemoteIp() + "/" + (int)connection.getRemotePort();
		Long personId = null;
		if(message.getCommand()!=Message.CMD_LOGIN) {
			//更新连接缓存
			try {
				personId = (Long)udpConnectionCache.get(connectionInfo);
				udpConnectionCache.get(personId);
			}
			catch (CacheException e) {
				Logger.exception(e);
			}
		}
		final long userId = (personId==null ? 0 : personId.longValue());
		MessageWriter messageWriter = new MessageWriter() {
			public void writeResponseMessage(Message message) {
				try {
					sendMessageToClient(userId, channel, connection, message, false); //发送消息
					if(message instanceof LoginAck) { //登录应答
						LoginAck loginAck = (LoginAck)message;
						try {
							//把连接写入缓存
							udpConnectionCache.put(new Long(loginAck.getPersonId()), connectionInfo);
							udpConnectionCache.put(connectionInfo, new Long(loginAck.getPersonId()));
						}
						catch (CacheException e) {
							Logger.exception(e);
						}
					}
				}
				catch (ServiceException e) {
					Logger.exception(e);
				}
			}
		};
		try {
			processReceivedMessage(userId, message, messageWriter);
		}
		catch (ServiceException e) {
			
		}
	}
	
	/**
	 * 发送消息
	 * @param receiverId
	 * @param datagramServerChannel
	 * @param connection
	 * @param message
	 * @param isResponse
	 * @throws ServiceException
	 */
	private void sendMessageToClient(long receiverId, DatagramServerChannel datagramServerChannel, DatagramConnection connection, Message message, boolean isResponse) throws ServiceException {
		if(message.getCommand()==0) {
			return;
		}
		if(message instanceof TalkDetail) { //发言
			//生成发言HTML
			TalkDetail talkDetail = (TalkDetail)message;
			if(talkDetail.getContent().startsWith("<!--FILETRANSFER/")) { //文件传输
				FileTransferRequest fileTransferRequest = null;
				try {
					fileTransferRequest = (FileTransferRequest)ObjectSerializer.deserialize(new Base64Decoder().decode(talkDetail.getContent().substring(talkDetail.getContent().indexOf(':') + 1, talkDetail.getContent().lastIndexOf("-->"))));
				}
				catch(Exception e) {
					
				}
				int index = fileTransferRequest.getFileName().lastIndexOf('\\');
				if(index==-1) {
					fileTransferRequest.getFileName().lastIndexOf('/');
				}
				String fileName = fileTransferRequest.getFileName().substring(index + 1);
				String html;
				if(talkDetail.getCreatorId()==receiverId) { //文件发送人
					html = "<div style=\"width:100%\">" +
						   "	<div style=\"padding-bottom: 3px\">" +
						   "		发送文件：" + TagUtils.getInstance().filter(fileName) + "(" + StringUtils.getFileSize(fileTransferRequest.getFileSize()) + ")" +
						   "		<a id=\"offlineSendFile_" + fileTransferRequest.getFileId() + "\" href=\"app://OFFLINESENDFILE/?fileId=" + fileTransferRequest.getFileId() + "&fileName=" + fileName + "\">发送离线文件</a>" +
						   "		<a id=\"cancelFile_" + fileTransferRequest.getFileId() + "\" href=\"app://CANCELFILE/?fileId=" + fileTransferRequest.getFileId() + "&fileName=" + fileName + "\">取消</a>" +
						   "	</div>" +
						   "	<div style=\"width:100%; border: buttonshadow 1 solid\">" +
						   "		<div id=\"transferProgress_" + fileTransferRequest.getFileId() + "\" style=\"margin: 1px; height:100%; background-color: highlight; width: 0%\"></div>" +
						   "	</div>" +
						   "	<div style=\"padding-top: 3px\">" +
						   "		已发送：<span id=\"percent_" + fileTransferRequest.getFileId() + "\">0%</span>， 速度：<span id=\"speed_" + fileTransferRequest.getFileId() + "\">0K/秒</span>" +
						   "	</div>" +
						   "</div>";
				}
				else { //文件接收人
					html = "<div style=\"width:100%\">" +
					  	   "	<div style=\"padding-bottom: 3px\">" +
					  	   "		发送文件：" + TagUtils.getInstance().filter(fileName) + "(" + StringUtils.getFileSize(fileTransferRequest.getFileSize()) + ")" +
					  	   "		<a id=\"receiveFile_" + fileTransferRequest.getFileId() + "\" href=\"app://RECEIVEFILE/?fileId=" + fileTransferRequest.getFileId() + "&fileSize=" + fileTransferRequest.getFileSize() + "&senderServerIp=" + fileTransferRequest.getSenderServerIp() + "&senderServerPort=" + (int)fileTransferRequest.getSenderServerPort() + "&senderInternetIp=" + fileTransferRequest.getSenderInternetIp() + "&senderInternetPort=" + (int)fileTransferRequest.getSenderInternetPort() + "&senderIntranetIp=" + fileTransferRequest.getSenderIntranetIp() + "&senderIntranetPort=" + (int)fileTransferRequest.getSenderIntranetPort() + "&fileName=" + fileName + "\">接收</a>&nbsp;" +
						   "		<a id=\"refuseFile_" + fileTransferRequest.getFileId() + "\" href=\"app://REFUSEFILE/?operatorId=" + receiverId + "&fileId=" + fileTransferRequest.getFileId() + "&fileName=" + fileName + "\">拒绝接收</a>" +
						   "		<a id=\"cancelFile_" + fileTransferRequest.getFileId() + "\" style=\"display:none\" href=\"app://CANCELFILE/?fileId=" + fileTransferRequest.getFileId() + "&fileName=" + fileName + "')\">取消</a>" +
						   "		<a id=\"openFile_" + fileTransferRequest.getFileId() + "\" style=\"display:none\">打开文件</a>" +
						   "		<a id=\"openFolder_" + fileTransferRequest.getFileId() + "\" style=\"display:none\">打开所在文件夹</a>" +
						   "	</div>" +
					  	   "	<div style=\"width:100%; border: buttonshadow 1 solid\">" +
					  	   "		<div id=\"transferProgress_" + fileTransferRequest.getFileId() + "\" style=\"margin: 1px; height:100%; background-color: highlight; width: 0%\"></div>" +
					  	   "	</div>" +
					  	   "	<div style=\"padding-top: 3px\">" +
					  	   "		已接收：<span id=\"percent_" + fileTransferRequest.getFileId() + "\">0%</span>， 速度：<span id=\"speed_" + fileTransferRequest.getFileId() + "\">0K/秒</span>" +
					  	   "	</div>" +
					  	   "</div>";
				}
				talkDetail.setContent(html); //内容
			}
			com.yuanluesoft.im.model.IMChat chat = new com.yuanluesoft.im.model.IMChat();
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
			talkDetail.setContent(pageService.generatePartPage("im", "chat", "talk", false, chat, 0, null, generatePartPageCallback));
		}
		byte sendBuffer[] = new byte[(10000 / DataPacket.MAX_DATA_LENGTH + 1) * DataPacket.MAX_DATA_LENGTH]; //10K
		int dataLength = messageParser.putMessageIntoBuffer(message, sendBuffer);
		if(Logger.isDebugEnabled()) {
			Logger.debug("IMService: send a message, class name is " + message.getClass().getName() + ", length is " + dataLength + ".");
		}
		try {
			datagramServerChannel.sendData(connection, sendBuffer, 0, dataLength, true, isResponse);
		}
		catch (NetworkException e) {
			Logger.exception(e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.im.service.IMService#processReceivedMessage(long, com.yuanluesoft.im.model.message.Message, com.yuanluesoft.im.service.ResponseMessageWriter)
	 */
	public void processReceivedMessage(long currentUserId, Message message, MessageWriter messageWriter) throws ServiceException {
		if(message instanceof Login) { //登录
	    	processLogin(currentUserId, (Login)message, messageWriter);
		}
		else if(message instanceof RetrieveMessage) { //获取消息
			retrieveMessage(currentUserId, messageWriter);
		}
		else if(message instanceof ExitClient) { //退出客户端
			//从缓存删除连接
		}
		else if(message instanceof KeepAlive) { //心跳
			processKeepAlive(currentUserId);
		}
		else if(message instanceof ChatDetailRequest) { //请求获取对话详情
			messageWriter.writeResponseMessage(loadChat(currentUserId, ((ChatDetailRequest)message).getChatId()));
		}
		else if(message instanceof TalkSubmit) { //提交发言
			TalkSubmit talkSubmit = (TalkSubmit)message;
			submitTalk(currentUserId, talkSubmit.getChatId(), talkSubmit.getContent());
		}
		else if(message instanceof TalkDetailRequest) { //请求获取发言内容
			processTalkDetailRequest(currentUserId, (TalkDetailRequest)message, messageWriter);
		}
		else if(message instanceof SystemMessageDetailRequest) { ////请求获取系统消息内容
			messageWriter.writeResponseMessage(retrieveSystemMessage(currentUserId, ((SystemMessageDetailRequest)message).getSystemMessageId()));
		}
		else if(message instanceof SystemMessageFeedback) { //系统消息反馈
			processSystemMessageFeedback(currentUserId, (SystemMessageFeedback)message);
		}
		else if(message instanceof CreateChat) { //创建一个对话
			messageWriter.writeResponseMessage(createChat(currentUserId, ((CreateChat)message).getChatPersonId()));
		}
		else if(message instanceof CreateGroupChat) { //创建一个多人对话
			CreateGroupChat createGroupChat = (CreateGroupChat)message;
			createGroupChat(currentUserId, createGroupChat.getFromChatId(), createGroupChat.getChatPersonIds());
		}
		else if(message instanceof CreateCustomerServiceChat) { //创建一个客服对话
			CreateCustomerServiceChat customerServiceChat  = (CreateCustomerServiceChat)message;
			createCustomerServiceChat(customerServiceChat.getSpecialistId(), customerServiceChat.getSiteId(), messageWriter);
		}
		else if(message instanceof CloseCustomerServiceChat) { //关闭客服对话
			closeCustomerServiceChat(((CloseCustomerServiceChat)message).getCustomerServiceChatId());
		}
		else if(message instanceof SavePersonalSetting) { //保存个人设置
			savePersonalSetting(currentUserId, (SavePersonalSetting)message);
		}
		else if(message instanceof OfflineCheck) { //检查用户是否已经离线
			offlineCheck(currentUserId);
		}
		else if(message instanceof FileTransferRequest) { //文件传送请求
			processFileTransferRequest(currentUserId, (FileTransferRequest)message, messageWriter);
		}
		else if(message instanceof FileTransferAck) { //文件传送请求应答
	        //转发文件传送请求应答
			
		}
		else if(message instanceof FileTransferCancel) { //放弃文件传送
	        //转发放弃文件传送
			
		}
		else if(message instanceof FileTransferComplete) { //文件传输完成
			processFileTransferComplete(currentUserId, (FileTransferComplete)message, messageWriter);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.im.service.IMService#sendMessage(long, com.yuanluesoft.im.model.message.Message)
	 */
	public void sendMessage(final long receiverId, final Message message) throws ServiceException {
		//获取用户状态
		final IMSession imSession;
		try {
			imSession = (IMSession)sessionCache.quietGet(new Long(receiverId));
		}
		catch (CacheException e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		if(imSession==null) { //不在线
			return;
		}
		try {
			threadPool.execute(new Task() {
				public void process() {
					//执行消息发送
					try {
						if(imSession.getServerIndex()==getServerIndex().charValue()) { //接收人连接的就是当前服务器
							doSendMessage(receiverId, message);
						}
						else { //接收人连接的不是当前服务器
							distributionService.invokeMethodOnRemoteServer((String)serverInfoCache.get(new Character(imSession.getServerIndex())), "imService", "sendMessage", new Serializable[]{new Long(receiverId), message});
						}
					}
					catch(Exception e) {
						Logger.exception(e);
					}
				}
			});
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * 执行消息发送
	 * @param receiverId
	 * @param notify
	 * @param callback
	 * @return
	 * @throws ServiceException
	 */
	private boolean doSendMessage(long receiverId, Message message) throws ServiceException {
		//检查是否存在客户端连接,如果有则发送给客户端
		try {
			String connectionInfo = (String)udpConnectionCache.quietGet(new Long(receiverId)); //获取用户UDP连接和通道
			if(connectionInfo!=null) {
				String[] values = connectionInfo.split("/");
				DatagramServerChannel channel = (DatagramServerChannel)udpChannels.get(Integer.parseInt(values[0]));
				DatagramConnection connection = channel.getConnection(values[1], (char)Integer.parseInt(values[2]));
				sendMessageToClient(receiverId, channel, connection, message, false);
				return true;
			}
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		//发送给WEBIM
		for(int i=0; i<2; i++) { //重试2次
			synchronized(mutexMap) {
				Object key = new Long(receiverId);
				Object value = mutexMap.get(key);
				if(value==null) { //用户还没有开始监听
					try {
						mutexMap.wait(500 + i * 500); //等待用户监听
					}
					catch (InterruptedException e) {
					
					}
					continue;
				}
				
				//输出消息
				if(value instanceof IMNotifyMutex){ //只有一个监听器
					synchronized(value) {
						IMNotifyMutex notifyMutex = (IMNotifyMutex)value;
						notifyMutex.setMessage(message);
						notifyMutex.notify();
					}
				}
				else { 
					for(Iterator iterator = ((List)value).iterator(); iterator.hasNext();) { //多个监听器
						IMNotifyMutex notifyMutex = (IMNotifyMutex)iterator.next();
						synchronized(notifyMutex) {
							notifyMutex.setMessage(message);
							notifyMutex.notify();
						}
					}
				}
				return true;
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.im.service.IMService#clientLogin(long)
	 */
	public IMClientLogin clientLogin(long userId) throws ServiceException {
		IMClientLogin clientLogin = new IMClientLogin();
		//创建登录钥匙
		String ticket = UUIDStringGenerator.generateId() +  UUIDStringGenerator.generateId();
		try {
			ticketCache.put(ticket, new Long(userId));
		}
		catch (CacheException e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		clientLogin.setTicket(ticket);
		//分配UDP通道
		clientLogin.setAssignUdpChannelIP(serverAddress);
		clientLogin.setAssignUdpChannelPort((char)((DatagramServerChannel)udpChannels.get(currentUdpChannelIndex)).getPort());
		currentUdpChannelIndex++;
		if(currentUdpChannelIndex>=udpChannels.size()) {
			currentUdpChannelIndex = 0;
		}
		return clientLogin;
	}
	
	/**
	 * 处理消息:登录
	 * @param login
	 * @param messageSource
	 * @throws ServiceException
	 */
	private void processLogin(long currentUserId, Login login, MessageWriter messageWriter) throws ServiceException {
		if(login.getTicket()!=null) { //客户端登录
			try {
				currentUserId = ((Number)ticketCache.quietGet(login.getTicket())).longValue(); //按登录钥匙获取用户ID
				ticketCache.remove(login.getTicket()); //删除钥匙
			}
			catch (Exception e) {
				Logger.exception(e);
				throw new ServiceException();
			}
			//发送注销(CMD_LOGOFF)给其他客户端,让其退出客户端,避免多重登录
			sendMessage(currentUserId, new Logoff());
		}
		login(currentUserId, login.getStatus(), messageWriter);
	}

	/**
	 * 登录
	 * @param currentUserId
	 * @param status
	 * @param messageWriter
	 * @throws ServiceException
	 */
	private void login(long currentUserId, byte status, MessageWriter messageWriter) throws ServiceException {
		//获取用户设置
		if(status==0xff) {
			IMPersonalSetting personalSetting = loadPersonalSetting(currentUserId);
			status = personalSetting.getStatus();
		}
		//修改用户状态
		changePersonStatus(currentUserId, status);
		//获取有留言的对话列表
		String hql = "select distinct IMChat" +
					 " from IMChat IMChat, IMChatPerson IMChatPerson, IMChatTalk IMChatTalk" +
					 " where IMChat.id=IMChatPerson.chatId" +
					 " and IMChatPerson.personId=" + currentUserId +
					 " and IMChatTalk.chatId=IMChatPerson.chatId" +
					 " and IMChatTalk.createdMillis>IMChatPerson.lastReadTime";
		List chats = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("chatPersons"));
		String offlineChats = null; //[chatId]$$[chatPersonNames]$$[isGroupChat]$$[isCustomerService]##
		if(chats!=null) {
			for(Iterator iterator = chats.iterator(); iterator.hasNext();) {
				IMChat chat = (IMChat)iterator.next();
				ListUtils.removeObjectsByProperty(chat.getChatPersons(), "personId", new Long(currentUserId));
				offlineChats = (offlineChats==null ? "" : offlineChats + "##") +
							   chat.getId() + "$$" +
							   ListUtils.join(chat.getChatPersons(), "personName", ",", false) + "$$" +
							   chat.getIsGroupChat() + "$$" +
							   chat.getIsCustomerService();
			}
		}
		//输出登录结果
		LoginAck loginAck = new LoginAck();
		loginAck.setPersonId(currentUserId); //用户ID,不输出到客户端
		loginAck.setStatus(status); //用户状态
		loginAck.setOfflineChats(offlineChats); //有留言的对话列表
		messageWriter.writeResponseMessage(loginAck);
		//获取在线用户数
		messageWriter.writeResponseMessage(new OnlineCountChanged(countOnlinePersons(currentUserId)));
	}
	
	/**
	 * 处理消息:心跳
	 * @param currentUserId
	 * @throws ServiceException
	 */
	private void processKeepAlive(long currentUserId) throws ServiceException {
		//更新会话缓存
		try {
			sessionCache.synchGet(new Long(currentUserId));
		}
		catch (CacheException e) {
			Logger.exception(e);
			throw new ServiceException();
		}
	}
	
	/**
	 * 处理消息:获取发言内容
	 * @param currentUserId
	 * @param talkDetailRequest
	 * @param messageWriter
	 * @throws ServiceException
	 */
	private void processTalkDetailRequest(long currentUserId, TalkDetailRequest talkDetailRequest, MessageWriter messageWriter) throws ServiceException {
		IMChat chat = loadCachedChat(talkDetailRequest.getChatId()); //加载对话
		final IMChatPerson chatPerson = (IMChatPerson)ListUtils.findObjectByProperty(chat.getChatPersons(), "personId", new Long(currentUserId)); //获取对话用户
		if(chatPerson==null) { //用户不在对话中
			return;
		}
		//获取未读发言
		String hql = "from IMChatTalk IMChatTalk" +
					 " where IMChatTalk.chatId=" + talkDetailRequest.getChatId() +
					 " and IMChatTalk.createdMillis>" + talkDetailRequest.getBeginTime() +
					 " order by IMChatTalk.createdMillis";
		List talks = getDatabaseService().findRecordsByHql(hql);
		if(talks==null || talks.isEmpty()) {
			return;
		}
		//输出发言内容
		IMChatTalk talk = null;
		for(Iterator iterator = talks.iterator(); iterator.hasNext();) {
			talk = (IMChatTalk)iterator.next();
			messageWriter.writeResponseMessage(new TalkDetail(talkDetailRequest.getChatId(), talk.getContent(), talk.getCreatorId(), talk.getCreatorName(), talk.getCreatedMillis()));
		}
		//设置最后接收时间为最后一条消息的创建时间
		final long lastTalkCreatedMillis = talk.getCreatedMillis();
		if(chatPerson.getLastReadTime()<lastTalkCreatedMillis) {
			//等待5秒后更新最后接收消息的时间
			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					try {
						chatPerson.setLastReadTime(lastTalkCreatedMillis);
						getDatabaseService().updateRecord(chatPerson);
					}
					catch (Exception e) {
						
					}
					timer.cancel();
				}
			}, 5000); //等待5s
		}
	}
	
	/**
	 * 处理消息:系统消息反馈
	 * @param currentUserId
	 * @param systemMessageFeedback
	 * @throws ServiceException
	 */
	private void processSystemMessageFeedback(long currentUserId, SystemMessageFeedback systemMessageFeedback) throws ServiceException {
		try {
			systemMessageCache.remove(new Long(systemMessageFeedback.getSystemMessageId()));
		}
		catch (CacheException e) {
			
		}
		messageService.feedbackMessage(systemMessageFeedback.getSystemMessageId(), currentUserId);
	}
	
	/**
	 * 处理文件传输请求
	 * @param currentUserId
	 * @param fileTransferRequest
	 * @param messageWriter
	 * @throws ServiceException
	 */
	private void processFileTransferRequest(long currentUserId, FileTransferRequest fileTransferRequest, MessageWriter messageWriter) throws ServiceException {
		int index = fileTransferRequest.getFileName().lastIndexOf('\\');
		if(index==-1) {
			fileTransferRequest.getFileName().lastIndexOf('/');
		}
		String fileName = fileTransferRequest.getFileName().substring(index + 1);
		String html = null;
		try {
			html = "<!--FILETRANSFER/" + fileTransferRequest.getFileId() + ":" + new Base64Encoder().encode(ObjectSerializer.serialize(fileTransferRequest)) + "-->" +
				   "发送文件：" + TagUtils.getInstance().filter(fileName) + "(" + StringUtils.getFileSize(fileTransferRequest.getFileSize()) + ")";
		}
		catch (IOException e) {
			
		}
		submitTalk(currentUserId, fileTransferRequest.getChatId(), html);
	}
	
	/**
	 * 处理：文件传输完成
	 * @param currentUserId
	 * @param fileTransferComplete
	 * @param messageWriter
	 * @throws ServiceException
	 */
	private void processFileTransferComplete(long currentUserId, FileTransferComplete fileTransferComplete, MessageWriter messageWriter) throws ServiceException {
		//获取发言记录
		String hql = "from IMChatTalk IMChatTalk" +
					 " where IMChatTalk.chatId=" + fileTransferComplete.getChatId() +
					 " and IMChatTalk.content like '<!--FILETRANSFER/" + fileTransferComplete.getFileId() + ":%'";
		IMChatTalk talk = (IMChatTalk)getDatabaseService().findRecordByHql(hql);
		//解析文件传输请求
		FileTransferRequest fileTransferRequest = null;
		try {
			fileTransferRequest = (FileTransferRequest)ObjectSerializer.deserialize(new Base64Decoder().decode(talk.getContent().substring(talk.getContent().indexOf(':') + 1, talk.getContent().lastIndexOf("-->"))));
		}
		catch(Exception e) {
			
		}
		int index = fileTransferRequest.getFileName().lastIndexOf('\\');
		if(index==-1) {
			fileTransferRequest.getFileName().lastIndexOf('/');
		}
		String fileName = fileTransferRequest.getFileName().substring(index + 1);
		//修改发言内容
		if(fileTransferComplete.getOfflineFileName()==null || fileTransferComplete.getOfflineFileName().isEmpty()) { //不是离线方式
			talk.setContent("发送文件：" + TagUtils.getInstance().filter(fileName) + "(" + StringUtils.getFileSize(fileTransferRequest.getFileSize()) + ")");
			String content = "文件：" + TagUtils.getInstance().filter(fileName) + "(" + StringUtils.getFileSize(fileTransferRequest.getFileSize()) + ")" + "接收成功。";
			submitTalk(currentUserId, fileTransferComplete.getChatId(), content);
		}
		else { //离线发送
			talk.setContent("发送文件：" + TagUtils.getInstance().filter(fileName) + "(" + StringUtils.getFileSize(fileTransferRequest.getFileSize()) + ")");
			String content = null;
			try {
				content = "发送离线文件：<a target=\"_blank\" href=\"" + Environment.getContextPath() + "/attachments/im/attachments/" + fileTransferComplete.getChatId() + "/" + URLEncoder.encode(fileTransferComplete.getOfflineFileName(), "utf-8") + "\">" + TagUtils.getInstance().filter(fileName) + "(" + StringUtils.getFileSize(fileTransferRequest.getFileSize()) + ")</a>";
			}
			catch (UnsupportedEncodingException e) {
				
			}
			submitTalk(currentUserId, fileTransferComplete.getChatId(), content);
		}
		getDatabaseService().updateRecord(talk);
	}	

	/**
	 * 检查用户是否已经离线
	 * @param currentUserId
	 * @throws ServiceException
	 */
	private void offlineCheck(final long currentUserId) throws ServiceException {
		//等待60秒后再检查
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				//获取用户状态
				IMSession imSession = null;
				try {
					imSession = (IMSession)sessionCache.quietGet(new Long(currentUserId));
				}
				catch (Exception e) {
					
				}
				if(imSession==null) { //不在线
					try {
						changePersonStatus(currentUserId, IM_PERSON_STATUS_OFFLINE);
					}
					catch (Exception e) {
						
					}
				}
				timer.cancel();
			}
		}, 60000); //等待60s
	}

	/**
	 * 创建一个对话
	 * @param currentUserId
	 * @param chatPersonId
	 * @return
	 * @throws ServiceException
	 */
	private ChatDetail createChat(long currentUserId, long chatPersonId) throws ServiceException {
		//检查对话是否已经存在
		String hql = "select IMChat" +
					 " from IMChat IMChat left join IMChat.chatPersons IMChatPerson" +
					 " where IMChat.isGroupChat!=1" +
					 " and IMChatPerson.personId=" + currentUserId +
					 " and (select min(IMChatPerson.personId)" +
					 "  from IMChatPerson IMChatPerson" +
					 "  where IMChatPerson.chatId=IMChat.id" +
					 "  and IMChatPerson.personId!=" + currentUserId +
					 " )=" + chatPersonId;
		IMChat imChat = (IMChat)getDatabaseService().findRecordByHql(hql, ListUtils.generateList("chatPersons"));
		if(imChat==null) { //对话不存在,创建新的对话
			imChat = createIMChat(false, currentUserId + "," + chatPersonId);
		}
		else { //对话已经存在,更新用户名,以显示最新的用户名
			IMChatPerson chatPerson = (IMChatPerson)ListUtils.findObjectByProperty(imChat.getChatPersons(), "personId", new Long(currentUserId));
			Member member = memberServiceList.getMember(currentUserId);
			if(member!=null) {
				chatPerson.setPersonName(member.getName());
			}
			chatPerson = (IMChatPerson)ListUtils.findObjectByProperty(imChat.getChatPersons(), "personId", new Long(chatPersonId));
			member = memberServiceList.getMember(chatPersonId);
			if(member!=null) {
				chatPerson.setPersonName(member.getName());
			}
		}
		//写入缓存
		try {
			chatCache.put(new Long(imChat.getId()), imChat);
		}
		catch (CacheException e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
		//返回对话模型
		return loadChat(currentUserId, imChat.getId());
	}

	/**
	 * 创建多人对话
	 * @param currentUserId
	 * @param fromChatId
	 * @param chatPersonIds
	 * @throws ServiceException
	 */
	private void createGroupChat(long currentUserId, long fromChatId, String chatPersonIds) throws ServiceException {
		//获取源对话
		IMChat chat = loadCachedChat(fromChatId);
		if(chat==null) {
			throw new ServiceException();
		}
		IMChatPerson currentChatPerson = (IMChatPerson)ListUtils.findObjectByProperty(chat.getChatPersons(), "personId", new Long(currentUserId));
		if(currentChatPerson==null) { //用户不是原来对话的参与者
			throw new ServiceException();
		}
		String newPersonNames = null;
		int sourceChatPersonNumber = chat.getChatPersons().size(); //原来的对话人数
		if(sourceChatPersonNumber==2) { //原来的对话不是多人对话
			//创建新的对话
			chat = createIMChat(true, ListUtils.join(chat.getChatPersons(), "personId", ",", false) + "," + chatPersonIds);
			int index = 0;
			for(Iterator iterator = chat.getChatPersons().iterator(); iterator.hasNext();) {
				IMChatPerson chatPerson = (IMChatPerson)iterator.next();
				if(index>=2) {
					newPersonNames = (newPersonNames==null ? "" : newPersonNames + "、") + chatPerson.getPersonName();
				}
				index++;
			}
			//写入缓存
			try {
				chatCache.put(new Long(chat.getId()), chat);
			}
			catch (CacheException e) {
				Logger.exception(e);
				throw new ServiceException(e.getMessage());
			}
		}
		else { //原来就是多人对话
			//追加对话用户
			String[] personIds = chatPersonIds.split(",");
			//获取最后的一个消息发送时间,作为新加入用户的最后获取消息时间
			long lastReadTime = getLastTalkTime(fromChatId);
			for(int i=0; i<personIds.length; i++) {
				long personId = Long.parseLong(personIds[i]);
				//检查用户是否已经加入过
				if(ListUtils.findObjectByProperty(chat.getChatPersons(), "personId", new Long(personId))!=null) {
					continue; //已经加入过
				}
				IMChatPerson chatPerson = addChatPerson(chat, personId, memberServiceList.getMember(personId).getName(), lastReadTime);
				newPersonNames = (newPersonNames==null ? "" : newPersonNames + "、") + chatPerson.getPersonName();
			}
		}
		if(newPersonNames==null) {
			return;
		}
		//发送通知给原来对话用户
		TalkDetail talkDetail = new TalkDetail(chat.getId(), "邀请" + newPersonNames + "加入对话。", currentUserId, currentChatPerson.getPersonName(), System.currentTimeMillis());
		int index = 0;
		for(Iterator iterator = chat.getChatPersons().iterator(); iterator.hasNext() && index<sourceChatPersonNumber; index++) {
			IMChatPerson chatPerson = (IMChatPerson)iterator.next();
			sendMessage(chatPerson.getPersonId(), talkDetail);
		}
	}
		
	/**
	 * 获取最后发言时间
	 * @param chatId
	 * @return
	 */
	private long getLastTalkTime(long chatId) {
		String hql = "select IMChatTalk.createdMillis" +
					 " from IMChatTalk IMChatTalk" +
					 " where IMChatTalk.chatId=" + chatId +
					 " order by IMChatTalk.createdMillis DESC";
		Number lastTalkTime = (Number)getDatabaseService().findRecordByHql(hql);
		return lastTalkTime==null ? 0 : lastTalkTime.longValue();
	}
	
	/**
	 * 创建一个客服对话
	 * @param specialistId
	 * @param siteId
	 * @param messageWriter
	 * @throws ServiceException
	 */
	private void createCustomerServiceChat(long specialistId, long siteId, MessageWriter messageWriter) throws ServiceException {
		CSParameter csParameter = csService.loadParameter(siteId);
		CSSpecialist specialist;
		if(specialistId>0) { //指定了客服人员
			//检查是否空闲
			specialist = getSpecialist(specialistId, csParameter, true);
		}
		else { //没有指定客服人员
			//获取空闲的客服人员
			specialist = getFreeSpecialist(siteId, csParameter);
		}
		if(specialist==null) {
			messageWriter.writeResponseMessage(new ChatCreateFailed());
			return;
		}
		IMChat imChat = new IMChat();
		imChat.setId(UUIDLongGenerator.generateId()); //ID
		imChat.setIsGroupChat(0); //是否讨论组
		imChat.setIsCustomerService(1); //是否客服
		imChat.setSiteId(siteId); //站点ID
		imChat.setCreated(DateTimeUtils.now()); //创建时间
		getDatabaseService().saveRecord(imChat);
		//把客服人员加入对话
		addChatPerson(imChat, specialist.getPersonId(), specialist.getExternalName(), 0);
		//构造一个虚拟用户(用户ID就是对话ID),并加入对话
		changePersonStatus(imChat.getId(), IM_PERSON_STATUS_ONLINE);
		addChatPerson(imChat, imChat.getId(), "网友", 0);
		//自动发送欢迎辞
		String welcome = csParameter==null || csParameter.getWelcome()==null ? "您好，请问有什么可以帮助您？" : csParameter.getWelcome();
		submitTalk(specialist.getPersonId(), imChat.getId(), welcome);
		//写入缓存
		try {
			chatCache.put(new Long(imChat.getId()), imChat);
		}
		catch (CacheException e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
		//输出对话
		messageWriter.writeResponseMessage(loadChat(imChat.getId(), imChat.getId()));
	}

	/**
	 * 关闭客服对话
	 * @param chatId
	 * @throws ServiceException
	 */
	public void closeCustomerServiceChat(long chatId) throws ServiceException {
		IMChat imChat = loadCachedChat(chatId);
		if(imChat.getIsCustomerService()==1 && imChat.getIsEnd()!=1) {
			submitTalk(chatId, chatId, "关闭对话。");
			imChat.setIsEnd(1);
			getDatabaseService().updateRecord(imChat);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.im.service.IMService#listSpecialists(long)
	 */
	public List listSpecialists(long siteId) throws ServiceException {
		String hql = "from CSSpecialist CSSpecialist" +
					 " where CSSpecialist.siteId=" + siteId +
					 " order by CSSpecialist.externalName";
		List specialists = getDatabaseService().findRecordsByHql(hql);
		if(specialists==null || specialists.isEmpty()) {
			return null;
		}
		CSParameter csParameter = csService.loadParameter(siteId);
		//获取空闲的客服
		List freeSpecialists = new ArrayList(specialists);
		listFreeSpecialists(freeSpecialists, csParameter);
		//获取状态、并转换为IM用户
		for(int i=0; i<specialists.size(); i++) {
			CSSpecialist specialist = (CSSpecialist)specialists.get(i);
			IMPerson imPerson = new IMPerson();
			imPerson.setPersonId(specialist.getId());
			imPerson.setPersonName(specialist.getExternalName());
			//设置状态,如果不在线都认为是离线
			byte status = getPersonStatus(specialist.getPersonId());
			if(status==IMService.IM_PERSON_STATUS_ONLINE) { //在线
				//检查用户是否忙碌
				if(freeSpecialists.indexOf(specialist)==-1) {
					status = IM_PERSON_STATUS_BUSY;
				}
			}
			imPerson.setStatus(status);
			specialists.set(i, imPerson);
		}
		return specialists;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.im.service.IMService#getFreeSpecialist(long)
	 */
	public CSSpecialist getFreeSpecialist(long siteId) throws ServiceException {
		CSParameter csParameter = csService.loadParameter(siteId);
		return getFreeSpecialist(siteId, csParameter);
	}

	/**
	 * 获取客服人员信息
	 * @param specialistId
	 * @param busyCheck
	 * @return
	 * @throws ServiceException
	 */
	private CSSpecialist getSpecialist(long specialistId, CSParameter csParameter, boolean busyCheck) throws ServiceException {
		CSSpecialist specialist = (CSSpecialist)getDatabaseService().findRecordById(CSSpecialist.class.getName(), specialistId);
		List specialists = ListUtils.generateList(specialist);
		listFreeSpecialists(specialists, csParameter);
		return specialists.isEmpty() ? null : specialist;
	}
	
	/**
	 * 获取站点的空闲客服人员
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	private CSSpecialist getFreeSpecialist(long siteId, CSParameter csParameter) throws ServiceException {
		List specialists = getDatabaseService().findRecordsByHql("from CSSpecialist CSSpecialist where CSSpecialist.siteId=" + siteId);
		if(specialists==null || specialists.isEmpty()) {
			return null;
		}
		listFreeSpecialists(specialists, csParameter);
		return specialists.isEmpty() ? null : (CSSpecialist)specialists.get(0);
	}
	
	/**
	 * 获取空闲的客服
	 * @param specialists
	 * @param csParameter
	 * @throws ServiceException
	 */
	private void listFreeSpecialists(List specialists, CSParameter csParameter) throws ServiceException {
		for(Iterator iterator = specialists.iterator(); iterator.hasNext();) {
			CSSpecialist specialist = (CSSpecialist)iterator.next();
			if(getPersonStatus(specialist.getPersonId())!=IM_PERSON_STATUS_ONLINE) { //状态不是“在线”
				iterator.remove();
			}
		}
		if(specialists.isEmpty()) {
			return;
		}
		//检查对话数量
		String hql = "select IMChatPerson.personId, count(distinct IMChat.id)" +
					 " from IMChat IMChat, IMChatPerson IMChatPerson" +
					 " where IMChat.isCustomerService=1" + //客服对话
					 " and IMChat.isEnd=0" + //未结束
					 " and IMChatPerson.chatId=IMChat.id" + //当前客服的对话
					 " and IMChatPerson.personId in (" + ListUtils.join(specialists, "personId", ",", false) + ")" +
					 " and (" + //最近有发言
					 "  select min(IMChatTalk.id)" +
					 "	 from IMChatTalk IMChatTalk" +
					 "	 where IMChatTalk.chatId=IMChat.id" +
					 "	 and IMChatTalk.createdMillis>" + (System.currentTimeMillis() - (csParameter==null || csParameter.getChatTimeout()<=0 ? 10 : csParameter.getChatTimeout()) * 60000) +
					 " ) is not null" +
					 " group by IMChatPerson.personId";
		final List chatCounts = getDatabaseService().findRecordsByHql(hql);
		for(Iterator iterator = specialists.iterator(); iterator.hasNext();) {
			CSSpecialist specialist = (CSSpecialist)iterator.next();
			int chatCount = getSpecialistChatCount(chatCounts, specialist); //获取对话数量
			if(chatCount>=(specialist.getMaxChat()<=0 ? 10 : specialist.getMaxChat())) { //对话超过上限
				iterator.remove();
			}
		}
		//排序,剩余对话数多的排前面
		Collections.sort(specialists, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				CSSpecialist specialist0 = (CSSpecialist)arg0;
				CSSpecialist specialist1 = (CSSpecialist)arg1;
				int left0 = (specialist0.getMaxChat()<=0 ? 10 : specialist0.getMaxChat()) - getSpecialistChatCount(chatCounts, specialist0);
				int left1 = (specialist1.getMaxChat()<=0 ? 10 : specialist1.getMaxChat()) - getSpecialistChatCount(chatCounts, specialist1);
				return left0==left1 ? 0 : (left0>left1 ? -1 : 1);
			}
		});
	}
	
	/**
	 * 获取客服的对话数量
	 * @param chatCounts
	 * @param specialist
	 * @return
	 */
	private int getSpecialistChatCount(List chatCounts, CSSpecialist specialist) {
		if(chatCounts==null || chatCounts.isEmpty()) {
			return 0;
		}
		for(Iterator iterator = chatCounts.iterator(); iterator.hasNext();) {
			Object[] values = (Object[])iterator.next();
			if(((Number)values[0]).longValue()==specialist.getPersonId()) {
				return values[1]==null ? 0 : ((Number)values[1]).intValue();
			}
		}
		return 0;
	}
	
	/**
	 * 创建对话
	 * @param isGroupChat
	 * @param chatPersonIds
	 * @return
	 */
	private IMChat createIMChat(boolean isGroupChat, String chatPersonIds) throws ServiceException {
		IMChat imChat = new IMChat();
		imChat.setId(UUIDLongGenerator.generateId()); //ID
		imChat.setIsGroupChat(isGroupChat ? 1: 0); //是否讨论组
		imChat.setCreated(DateTimeUtils.now()); //创建时间
		getDatabaseService().saveRecord(imChat);
		//保存对话用户
		String[] personIds = chatPersonIds.split(",");
		for(int i=0; i<personIds.length; i++) {
			long personId = Long.parseLong(personIds[i]);
			if(ListUtils.findObjectByProperty(imChat.getChatPersons(), "personId", new Long(personId))!=null) {
				continue;
			}
			addChatPerson(imChat, personId, memberServiceList.getMember(personId).getName(), 0);
		}
		return imChat;
	}
	
	/**
	 * 添加对话用户
	 * @param imChat
	 * @param personId
	 * @param personName
	 * @param lastReadTime
	 * @return
	 * @throws ServiceException
	 */
	private IMChatPerson addChatPerson(IMChat imChat, long personId, String personName, long lastReadTime) throws ServiceException {
		if(imChat.getChatPersons()==null) {
			imChat.setChatPersons(new LinkedHashSet());
		}
		IMChatPerson chatPerson = new IMChatPerson();
		chatPerson.setId(UUIDLongGenerator.generateId()); //ID
		chatPerson.setChatId(imChat.getId()); //对话ID
		chatPerson.setPersonId(personId); //用户ID
		chatPerson.setPersonName(personName); //用户名
		chatPerson.setJoinTime(new Timestamp(lastReadTime)); //加入对话时间
		chatPerson.setLastReadTime(lastReadTime); //最后接收消息的时间
		getDatabaseService().saveRecord(chatPerson);
		imChat.getChatPersons().add(chatPerson);
		return chatPerson;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.im.service.IMService#loadChat(long, long, boolean)
	 */
	public ChatDetail loadChat(long currentUserId, long chatId) throws ServiceException {
		//从缓存读取对话 
		IMChat cachedChat = loadCachedChat(chatId);
		if(cachedChat==null) {
			throw new ServiceException();
		}
		ChatDetail chat = new ChatDetail();
		chat.setChatId(chatId); //对话ID
		chat.setCustomerService(cachedChat.getIsCustomerService()==1); //是否客服对话
		String personIds = null;
		String personNames = null;
		IMChatPerson currentChatPerson = null;
		for(Iterator iterator = cachedChat.getChatPersons().iterator(); iterator.hasNext();) {
			IMChatPerson chatPerson = (IMChatPerson)iterator.next();
			if(chatPerson.getPersonId()==currentUserId) {
				currentChatPerson = chatPerson;
			}
			else {
				personIds = (personIds==null ? "" : personIds + ",") + chatPerson.getPersonId();
				personNames = (personNames==null ? "" : personNames + ",") + chatPerson.getPersonName();
			}
		}
		if(currentChatPerson==null) {
			throw new ServiceException();
		}
		chat.setChatPersonIds(personIds); //聊天用户ID列表,用逗号分隔,不包括自己
		long firstPersonId = Long.parseLong(personIds.split(",")[0]);
		chat.setChatPersonNames(personNames); //聊天用户姓名列表,用逗号分隔,不包括自己
		chat.setChatPersonStatus(getPersonStatus(firstPersonId)); //对话用户的状态
		//获取未读留言数
		String hql = "select count(IMChatTalk.id)" +
					 " from IMChatTalk IMChatTalk" +
					 " where IMChatTalk.chatId=" + chatId +
					 " and IMChatTalk.createdMillis>" + currentChatPerson.getLastReadTime();
		Number unreadTalkCount = (Number)getDatabaseService().findRecordByHql(hql);
		chat.setUnreadTalkCount(unreadTalkCount==null ? 0 : unreadTalkCount.intValue());
		chat.setLastReadTime(currentChatPerson.getLastReadTime());
		return chat;
	}
	
	/**
	 * 获取缓存的对话
	 * @param chatId
	 * @return
	 * @throws ServiceException
	 */
	private IMChat loadCachedChat(long chatId) throws ServiceException {
		//从缓存获取
		IMChat chat = null;
		try {
			chat = (IMChat)chatCache.get(new Long(chatId));
		}
		catch (CacheException e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
		if(chat==null) { //没有缓存
			//从数据库获取
			chat = (IMChat)getDatabaseService().findRecordById(IMChat.class.getName(), chatId, ListUtils.generateList("chatPersons"));
			if(chat==null) {
				return null;
			}
			//写入缓存
			try {
				chatCache.put(new Long(chatId), chat);
			}
			catch (CacheException e) {
				Logger.exception(e);
				throw new ServiceException(e.getMessage());
			}
		}
		return chat;
	}

	/**
	 * 读取消息
	 * @param currentUserId
	 * @param respMessageWriter
	 * @return
	 * @throws ServiceException
	 */
	public void retrieveMessage(long currentUserId, MessageWriter messageWriter) throws ServiceException {
		Object key = new Long(currentUserId);
		//从缓存获取用户状态
		IMSession imSession;
		try {
			imSession = (IMSession)sessionCache.synchGet(key);
		}
		catch (CacheException e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		if(imSession==null) { //原来没有上线
			login(currentUserId, (byte)0xff, messageWriter);
			return;
		}
		if(imSession.getServerIndex()!=getServerIndex().charValue()) { //用户原来登录的服务器不是当前服务器,更新缓存的会话
			imSession.setServerIndex(getServerIndex().charValue());
			try {
				sessionCache.put(key, imSession);
			}
			catch (CacheException e) {
				Logger.exception(e);
				throw new ServiceException();
			}
		}
		//创建监听器,等待20秒
		IMNotifyMutex notifyMutex = new IMNotifyMutex();
		//加入监听队列
		synchronized(mutexMap) {
			Object value = mutexMap.get(key);
			if(value==null) {
				mutexMap.put(key, notifyMutex);
			}
			else if(value instanceof List) { //已经是列表
				((List)value).add(notifyMutex);
			}
			else { //已经存在过监听器
				//转换为列表
				List mutexes = new ArrayList();
				mutexes.add(value);
				mutexes.add(notifyMutex);
				mutexMap.put(key, mutexes);
			}
		}
		synchronized(notifyMutex) {
			try {
				notifyMutex.wait(notifyDelaySeconds * 1000); //监听20秒
			}
			catch (InterruptedException e) {
				
			}
		}
		//从监听队列中删除
		synchronized(mutexMap) {
			Object value = mutexMap.get(key);
			try {
				if(value instanceof IMNotifyMutex) { //只有一个
					mutexMap.remove(key); //直接清除
				}
				else { //列表
					List mutexes = (List)value;
					if(mutexes.size()==1) { //最后一个
						mutexMap.remove(key);
					}
					else {
						mutexes.remove(notifyMutex);
					}
				}
			}
			catch(Exception e) {
				mutexMap.remove(key);
			}
		}
		//输出消息
		if(notifyMutex.getMessage()!=null) {
			messageWriter.writeResponseMessage(notifyMutex.getMessage());
		}
	}

	/**
	 * 改变用户状态
	 * @param currentUserId
	 * @param status
	 * @throws ServiceException
	 */
	private void changePersonStatus(long currentUserId, byte status) throws ServiceException {
		byte currentStatus = getPersonStatus(currentUserId); //获取当前状态
		if(currentStatus==status && status!=IM_PERSON_STATUS_OFFLINE) { //状态没有改变
			return;
		}
		//写入缓存
		try {
			if(status==IM_PERSON_STATUS_OFFLINE) { //离线
				sessionCache.remove(new Long(currentUserId));
			}
			else {
				IMSession imSession = new IMSession();
				imSession.setStatus(status); //状态
				imSession.setServerIndex(getServerIndex().charValue()); //服务器索引号
				sessionCache.put(new Long(currentUserId), imSession);
			}
		}
		catch (CacheException e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		//发送通知给朋友,不包括自己
		Collection friendIds = getFriendIds(currentUserId);
		if(friendIds==null || friendIds.isEmpty()) {
			return;
		}
		PersonStatusChanged personStatusChanged = new PersonStatusChanged(currentUserId, status);
		OnlineCountChanged onlineCountChanged = null;
		if(!friendEnabled) { //不支持朋友管理
			onlineCountChanged = new OnlineCountChanged(countOnlinePersons(currentUserId));
		}
		for(Iterator iterator = friendIds.iterator(); iterator.hasNext();) {
			Number friendId = (Number)iterator.next();
			if(friendId.longValue()==currentUserId) {
				continue; //不给自己发送消息
			}
			sendMessage(friendId.longValue(), personStatusChanged);
			if(onlineCountChanged!=null) {
				sendMessage(friendId.longValue(), onlineCountChanged);
			}
			else {
				sendMessage(friendId.longValue(), new OnlineCountChanged(countOnlinePersons(friendId.longValue())));
			}
		}
	}
	
	/**
	 * 获取服务器索引号
	 * @return
	 * @throws ServiceException
	 */
	private Character getServerIndex() throws ServiceException {
		try {
			if(serverIndex==null) { //索引号不存在
				//新建索引号
				for(int i=0; i<100; i++) {
					serverIndex = new Character((char)new Random().nextInt((int)Character.MAX_VALUE));
					if(serverInfoCache.get(serverIndex)==null) {
						break;
					}
				}
				serverInfoCache.put(serverIndex, distributionService.getWebApplicationDistributeUrl());
			}
			return serverIndex;
		}
		catch (CacheException e) {
			Logger.exception(e);
			throw new ServiceException();
		}
	}

	/**
	 * 提交发言
	 * @param currentUserId
	 * @param chatId
	 * @param content
	 * @throws ServiceException
	 */
	private void submitTalk(long currentUserId, long chatId, String content) throws ServiceException {
		IMChat chat = loadCachedChat(chatId);
		if(chat.getIsCustomerService()==1 && chat.getIsEnd()==1) { //客服对话,且已经结束
			return;
		}
		//保存发言
		final IMChatTalk talk = new IMChatTalk();
		talk.setId(UUIDLongGenerator.generateId()); //ID
		talk.setContent(StringUtils.removeWordFormat(content, true, true)); //发言内容
		talk.setCreatedMillis(Math.max(System.currentTimeMillis(), getLastTalkTime(chatId) + 1)); //发言时间,不允许早于于上一次发言时间（多服务器时会出现）
		talk.setChatId(chatId); //对话ID
		talk.setCreatorId(currentUserId); //发言人ID
		talk.setCreatorName(((IMChatPerson)ListUtils.findObjectByProperty(chat.getChatPersons(), "personId", new Long(currentUserId))).getPersonName()); //发言人姓名
		getDatabaseService().saveRecord(talk);
		//发送通知
		for(Iterator iterator = chat.getChatPersons().iterator(); iterator.hasNext();) {
			IMChatPerson chatPerson = (IMChatPerson)iterator.next();
			Talk talkNotify = new Talk();
			talkNotify.setChatId(chatId); //对话ID
			talkNotify.setGroupChat(chat.getIsGroupChat()==1); //是否多人对话
			talkNotify.setCustomerService(chat.getIsCustomerService()==1); //是否客服对话
			talkNotify.setSelfTalk(chatPerson.getPersonId()==talk.getCreatorId()); //是否用户自己的发言
			//设置对话用户姓名
			String personNames = null;
			for(Iterator iteratorPerson = chat.getChatPersons().iterator(); iteratorPerson.hasNext();) {
				IMChatPerson person = (IMChatPerson)iteratorPerson.next();
				if(person.getPersonId()!=currentUserId) {
					personNames = (personNames==null ? "" : personNames + ",") + chatPerson.getPersonName();
				}
			}
			talkNotify.setChatPersonNames(personNames); //对话用户姓名
			//发送通知
			sendMessage(chatPerson.getPersonId(), talkNotify);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.im.service.IMService#countOnlinePersons(long)
	 */
	public int countOnlinePersons(long currentUserId) throws ServiceException {
		Collection friendIds = getFriendIds(currentUserId);
		if(friendIds==null || friendIds.isEmpty()) {
			return 0;
		}
		int count = 0;
		for(Iterator iterator = friendIds.iterator(); iterator.hasNext();) {
			Object friendId = iterator.next();
			try {
				IMSession imSession = (IMSession)sessionCache.quietGet(friendId);
				if(imSession!=null && imSession.getStatus()!=IM_PERSON_STATUS_OFFLINE) {
					count++;
				}
			}
			catch (CacheException e) {
				Logger.exception(e);
				throw new ServiceException();
			}
		}
		return count;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.im.service.IMService#listOnlinePersons(long, int, int)
	 */
	public List listOnlinePersons(long currentUserId, int offset, int limit) throws ServiceException {
		Collection friendIds = getFriendIds(currentUserId);
		if(friendIds==null || friendIds.isEmpty()) {
			return null;
		}
		List onlinePersons = new ArrayList();
		int i = 0;
		for(Iterator iterator = friendIds.iterator(); iterator.hasNext() && i-offset<limit;) {
			Number friendId = (Number)iterator.next();
			if((i++)<offset || friendId.longValue()==currentUserId) {
				continue;
			}
			IMPerson imPerson = generateIMPerson(friendId.longValue(), true, true);
			if(imPerson!=null) {
				onlinePersons.add(imPerson);
			}
		}
		//按用户名排序
		Collections.sort(onlinePersons, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				IMPerson person0 = (IMPerson)arg0;
				IMPerson person1 = (IMPerson)arg1;
				return person1.getPersonName().compareTo(person0.getPersonName());
			}
		});
		return onlinePersons.isEmpty() ? null : onlinePersons;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.im.service.IMService#listRecentPersons(long, int)
	 */
	public List listRecentPersons(long currentUserId, int limit) throws ServiceException {
		String hql = "select IMChat" +
					 " from IMChat IMChat left join IMChat.chatPersons IMChatPerson" +
					 " where IMChatPerson.personId=" + currentUserId +
					 " and IMChat.isCustomerService=0" +
					 " order by IMChat.lastReadTime DESC";
		List chats = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("chatPersons"), 0, limit);
		if(chats==null || chats.isEmpty()) {
			return null;
		}
		List recentPersons = new ArrayList();
		for(Iterator iterator = chats.iterator(); iterator.hasNext();) {
			IMChat chat = (IMChat)iterator.next();
			IMPerson imPerson = null;
			if(chat.getIsGroupChat()!=1) { //非多人对话
				ListUtils.removeObjectByProperty(chat.getChatPersons(), "personId", new Long(currentUserId));
				IMChatPerson chatPerson = (IMChatPerson)chat.getChatPersons().iterator().next();
				imPerson = generateIMPerson(chatPerson.getPersonId(), false, false);
				if(imPerson==null) {
					continue;
				}
				imPerson.setPersonName(chatPerson.getPersonName()); //用户名
			}
			else {
				imPerson = new IMPerson();
				imPerson.setPersonId(0); //用户ID
				imPerson.setStatus(IM_PERSON_STATUS_ONLINE); //状态
				//设置对话用户姓名
				String personNames = null;
				int i = 0;
				for(Iterator iteratorPerson = chat.getChatPersons().iterator(); iteratorPerson.hasNext() && i<2;) {
					IMChatPerson person = (IMChatPerson)iteratorPerson.next();
					if(person.getPersonId()!=currentUserId) {
						i++;
						personNames = (personNames==null ? "" : personNames + ",") + person.getPersonName();
					}
				}
				imPerson.setPersonName(personNames + (chat.getChatPersons().size()<=3 ? "" : "等")); //用户名
				imPerson.setPortraitURL(Environment.getContextPath() + "/im/icons/groupChat.gif"); //头像
			}
			imPerson.setChatId(chat.getId()); //对话ID
			recentPersons.add(imPerson); //添加到列表
		}
		return recentPersons.isEmpty() ? null : recentPersons;
	}
	
	/**
	 * 生成IM用户模型
	 * @param personId
	 * @param retrievePersonName
	 * @param onlineOnly
	 * @return
	 * @throws ServiceException
	 */
	private IMPerson generateIMPerson(long personId, boolean retrievePersonName, boolean onlineOnly) throws ServiceException {
		IMSession imSession;
		try {
			imSession = (IMSession)sessionCache.quietGet(new Long(personId));
		}
		catch (CacheException e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		if(onlineOnly && (imSession==null || imSession.getStatus()<=IM_PERSON_STATUS_HIDE)) {
			return null;
		}
		IMPerson imPerson = new IMPerson();
		imPerson.setPersonId(personId); //用户ID
		if(retrievePersonName) {
			//获取成员信息
			Member member = memberServiceList.getMember(personId);
			if(member==null || member.getName()==null || member.getName().isEmpty()) {
				return null;
			}
			imPerson.setPersonName(member.getName()); //用户名
		}
		imPerson.setStatus(imSession==null ? IM_PERSON_STATUS_OFFLINE : imSession.getStatus()); //状态
		imPerson.setPortraitURL(PersonUtils.getPortraitURL(imPerson.getPersonId())); //头像
		return imPerson;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.im.service.IMService#getPersonStatus(long)
	 */
	public byte getPersonStatus(long personId) throws ServiceException {
		try {
			IMSession imSession = (IMSession)sessionCache.quietGet(new Long(personId));
			return (imSession==null ? IM_PERSON_STATUS_OFFLINE : imSession.getStatus());
		}
		catch (CacheException e) {
			Logger.exception(e);
			throw new ServiceException();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.im.service.IMService#getClientType(long)
	 */
	public String getClientType(long personId) throws ServiceException {
		if(getPersonStatus(personId)<=IM_PERSON_STATUS_HIDE) {
			return null;
		}
		try {
			if(udpConnectionCache.quietGet(new Long(personId))!=null) {
				return "IMClient";
			}
		}
		catch (CacheException e) {
			return null;
		}
		return "WEBIM";
	}

	/**
	 * 获取朋友ID列表
	 * @param currentUserId
	 * @return
	 * @throws ServiceException
	 */
	private Collection getFriendIds(long currentUserId) throws ServiceException {
		if(!friendEnabled) { //没有启用朋友管理
			try {
				return sessionCache.getKeys();
			}
			catch (CacheException e) {
				return null;
			}
		}
		//启用了朋友管理, TODO 获取朋友的ID列表
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.im.service.IMService#sendSystemMessage(com.yuanluesoft.jeaf.messagecenter.pojo.Message, long)
	 */
	public void sendSystemMessage(com.yuanluesoft.jeaf.messagecenter.pojo.Message systemMessage, long receiverId) throws ServiceException {
		try {
			systemMessageCache.put(new Long(systemMessage.getId()), systemMessage);
		}
		catch (CacheException e) {
			
		}
		sendMessage(receiverId, new SystemMessage(systemMessage.getId()));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.im.service.IMService#retrieveSystemMessage(long, long)
	 */
	public SystemMessageDetail retrieveSystemMessage(long currentUserId, long systemMessageId) throws ServiceException {
		com.yuanluesoft.jeaf.messagecenter.pojo.Message message = null;
		try {
			message = (com.yuanluesoft.jeaf.messagecenter.pojo.Message)systemMessageCache.get(new Long(systemMessageId));
		}
		catch (CacheException e) {
			
		}
		if(message==null) {
			message = messageService.retrieveMessage(systemMessageId, currentUserId);
		}
		SystemMessageDetail systemMessageDetail = new SystemMessageDetail();
		if(message!=null) {
			try {
				PropertyUtils.copyProperties(systemMessageDetail, message);
			}
			catch (Exception e) {
				
			}
			systemMessageDetail.setSystemMessageId(systemMessageId);
		}
		return systemMessageDetail;
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.im.service.IMService#processRemoteMessage(long, byte[])
	 */
	public boolean processRemoteMessage(long receiverId, byte[] data) throws ServiceException {
		Message message;
		try {
			message = (Message)ObjectSerializer.deserialize(data);
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
		return doSendMessage(receiverId, message);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.im.service.IMService#loadPersonalSetting(long)
	 */
	public IMPersonalSetting loadPersonalSetting(long currentUserId) throws ServiceException {
		IMPersonalSetting personalSetting = (IMPersonalSetting)getDatabaseService().findRecordById(IMPersonalSetting.class.getName(), currentUserId);
		if(personalSetting==null) { //没有设置过
			personalSetting = new IMPersonalSetting();
			personalSetting.setStatus(IM_PERSON_STATUS_ONLINE); //上线后状态,在线、忙碌、隐身、不在电脑旁
			personalSetting.setPlaySoundOnReceived(1); //消息到达是否发出声音
			personalSetting.setSetFocusOnReceived(1); //消息到达是否获取焦点
			personalSetting.setOpenChatDialogOnReceived(1); //是否主动弹出对话窗口
			personalSetting.setCtrlSend(0); //CTRL+Enter发送消息
			personalSetting.setFontName("宋体"); //字体
			personalSetting.setFontSize("12"); //字号
			personalSetting.setFontColor("#000000"); //颜色
		}
		return personalSetting;
	}

	/**
	 * 保存个人设置
	 * @param currentUserId
	 * @param personalSetting
	 * @throws ServiceException
	 */
	private void savePersonalSetting(long currentUserId, SavePersonalSetting personalSetting) throws ServiceException {
		IMPersonalSetting imPersonalSetting  = loadPersonalSetting(currentUserId);
		try {
			PropertyUtils.copyProperties(imPersonalSetting, personalSetting);
		}
		catch (Exception e1) {
			
		}
		imPersonalSetting.setId(currentUserId);
		try {
			getDatabaseService().updateRecord(imPersonalSetting);
		}
		catch(Exception e) {
			getDatabaseService().saveRecord(imPersonalSetting);
		}
		//更新用户状态
		changePersonStatus(currentUserId, personalSetting.getStatus());
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("loginStatus".equals(itemsName)) { //登录后的状态
			List items = new ArrayList();
			for(byte i=IM_PERSON_STATUS_HIDE; i<=IM_PERSON_STATUS_LEAVE; i++) {
				items.add(new Object[]{IM_PERSON_STATUS_TEXTS[i-IM_PERSON_STATUS_OFFLINE], new Byte(i)});
			}
			return items;
		}
		return super.listSelectItems(itemsName, bean, fieldDefine, request, sessionInfo);
	}
	
	/**
	 * 通知监听
	 * @author linchuan
	 *
	 */
	private class IMNotifyMutex {
		private Message message;

		/**
		 * @return the message
		 */
		public Message getMessage() {
			return message;
		}

		/**
		 * @param message the message to set
		 */
		public void setMessage(Message message) {
			this.message = message;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService#getApplicationNavigator(java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public ApplicationNavigator getApplicationNavigator(String applicationName, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		//创建根目录
		Tree tree = new Tree("im", "即时通讯", "root", Environment.getWebApplicationUrl() + "/im/icons/im.gif");
		tree.getRootNode().setExtendPropertyValue("dataUrl", Environment.getWebApplicationUrl() + "/im/admin/chatHistory.shtml");
		TreeNode groupChatNode = new TreeNode("groupChats", "多人对话", "groupChats", Environment.getWebApplicationUrl() + "/im/icons/groupChat.gif", true);
		//获取当前用户参与的对话列表
		String hql = "select IMChat" +
					 " from IMChat IMChat left join IMChat.chatPersons IMChatPerson" +
					 " where IMChatPerson.personId=" + sessionInfo.getUserId() +
					 " and IMChat.isCustomerService=0";
		List chats = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("chatPersons"));
		for(Iterator iterator = (chats==null ? null : chats.iterator()); iterator!=null && iterator.hasNext(); ) {
			IMChat chat = (IMChat)iterator.next();
			ListUtils.removeObjectByProperty(chat.getChatPersons(), "personId", new Long(sessionInfo.getUserId()));
			String personName = ListUtils.join(chat.getChatPersons(), "personName", ",", false);
			TreeNode chatNode;
			if(chat.getIsGroupChat()==1) {
				chatNode = groupChatNode.appendChildNode("" + chat.getId(), personName, "chat", Environment.getWebApplicationUrl() + "/im/icons/groupChat.gif", false);
			}
			else {
				chatNode = tree.appendChildNode("" + chat.getId(), personName, "chat", Environment.getWebApplicationUrl() + "/im/icons/chat.gif", false);
			}
			chatNode.setExtendPropertyValue("dataUrl", Environment.getWebApplicationUrl() + "/im/admin/chatHistory.shtml?chatId=" + chat.getId());
		}
		Comparator comparator = new Comparator() {
			public int compare(Object arg0, Object arg1) {
				TreeNode node0 = (TreeNode)arg0;
				TreeNode node1 = (TreeNode)arg1;
				if(node0==null || node1==null || node0.getNodeText()==null || node1.getNodeText()==null) {
					return 0;
				}
				return node0.getNodeText().compareTo(node1.getNodeText());
			}
		};
		//按名称排序
		if(tree.getRootNode().getChildNodes()!=null) {
			Collections.sort(tree.getRootNode().getChildNodes(), comparator);
		}
		if(groupChatNode.getChildNodes()!=null && !groupChatNode.getChildNodes().isEmpty()) {
			Collections.sort(groupChatNode.getChildNodes(), comparator);
			if(tree.getRootNode().getChildNodes()==null) {
				tree.getRootNode().setChildNodes(new ArrayList());
			}
			tree.getRootNode().getChildNodes().add(0, groupChatNode);
		}
		//返回应用导航树
		return new ApplicationTreeNavigator(tree);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService#listApplicationNavigatorTreeNodes(java.lang.String, java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listApplicationNavigatorTreeNodes(String applicationName, String parentTreeNodeId, SessionInfo sessionInfo) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.im.service.IMService#listChatTalks(long, long, int)
	 */
	public List listChatTalks(long chatId, long personId, int max) throws ServiceException {
		String hql = "from IMChatTalk IMChatTalk" +
					 " where IMChatTalk.chatId=" + chatId;
		if(personId>0) {
			String hqlPerson = "from IMChatPerson IMChatPerson" +
							   " where IMChatPerson.chatId=" + chatId +
							   " and IMChatPerson.personId=" + personId;
			IMChatPerson chatPerson = (IMChatPerson)getDatabaseService().findRecordByHql(hqlPerson);
			if(chatPerson==null) {
				return null;
			}
			hql += " and IMChatTalk.createdMillis>" + chatPerson.getJoinTime().getTime();
		}
		hql += " order by IMChatTalk.createdMillis DESC";
		List talks = getDatabaseService().findRecordsByHql(hql, 0, max);
		if(talks==null) {
			return null;
		}
		Collections.reverse(talks); //倒序
		return talks;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.application.service.ApplicationNavigatorService#saveApplicationNavigatorDefinition(java.lang.String, com.yuanluesoft.jeaf.application.model.navigator.definition.ApplicationNavigatorDefinition)
	 */
	public void saveApplicationNavigatorDefinition(String applicationName, ApplicationNavigatorDefinition navigatorDefinition) throws ServiceException {
		
	}

	/**
	 * @return the messageLimit
	 */
	public int getMessageLimit() {
		return messageLimit;
	}

	/**
	 * @param messageLimit the messageLimit to set
	 */
	public void setMessageLimit(int messageLimit) {
		this.messageLimit = messageLimit;
	}

	/**
	 * @return the cache
	 */
	public Cache getSessionCache() {
		return sessionCache;
	}

	/**
	 * @param cache the cache to set
	 */
	public void setSessionCache(Cache cache) {
		this.sessionCache = cache;
	}

	/**
	 * @return the friendEnabled
	 */
	public boolean isFriendEnabled() {
		return friendEnabled;
	}

	/**
	 * @param friendEnabled the friendEnabled to set
	 */
	public void setFriendEnabled(boolean friendEnabled) {
		this.friendEnabled = friendEnabled;
	}

	/**
	 * @return the notifyDelaySeconds
	 */
	public int getNotifyDelaySeconds() {
		return notifyDelaySeconds;
	}

	/**
	 * @param notifyDelaySeconds the notifyDelaySeconds to set
	 */
	public void setNotifyDelaySeconds(int notifyDelaySeconds) {
		this.notifyDelaySeconds = notifyDelaySeconds;
	}

	/**
	 * @return the attachmentSaveDays
	 */
	public int getAttachmentSaveDays() {
		return attachmentSaveDays;
	}

	/**
	 * @param attachmentSaveDays the attachmentSaveDays to set
	 */
	public void setAttachmentSaveDays(int attachmentSaveDays) {
		this.attachmentSaveDays = attachmentSaveDays;
	}

	/**
	 * @return the chatCache
	 */
	public Cache getChatCache() {
		return chatCache;
	}

	/**
	 * @param chatCache the chatCache to set
	 */
	public void setChatCache(Cache chatCache) {
		this.chatCache = chatCache;
	}

	/**
	 * @return the messageService
	 */
	public MessageService getMessageService() {
		return messageService;
	}

	/**
	 * @param messageService the messageService to set
	 */
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	/**
	 * @return the systemMessageCache
	 */
	public Cache getSystemMessageCache() {
		return systemMessageCache;
	}

	/**
	 * @param systemMessageCache the systemMessageCache to set
	 */
	public void setSystemMessageCache(Cache systemMessageCache) {
		this.systemMessageCache = systemMessageCache;
	}

	/**
	 * @return the csService
	 */
	public CSService getCsService() {
		return csService;
	}

	/**
	 * @param csService the csService to set
	 */
	public void setCsService(CSService csService) {
		this.csService = csService;
	}

	/**
	 * @return the memberServiceList
	 */
	public MemberServiceList getMemberServiceList() {
		return memberServiceList;
	}

	/**
	 * @param memberServiceList the memberServiceList to set
	 */
	public void setMemberServiceList(MemberServiceList memberServiceList) {
		this.memberServiceList = memberServiceList;
	}

	/**
	 * @return the serverAddress
	 */
	public String getServerAddress() {
		return serverAddress;
	}

	/**
	 * @param serverAddress the serverAddress to set
	 */
	public void setServerAddress(String serverAddress) {
		this.serverAddress = StringUtils.fillLocalHostIP(serverAddress);
	}

	/**
	 * @return the serverInfoCache
	 */
	public Cache getServerInfoCache() {
		return serverInfoCache;
	}

	/**
	 * @param serverInfoCache the serverInfoCache to set
	 */
	public void setServerInfoCache(Cache serverInfoCache) {
		this.serverInfoCache = serverInfoCache;
	}

	/**
	 * @return the udpPorts
	 */
	public String getUdpPorts() {
		return udpPorts;
	}

	/**
	 * @param udpPorts the udpPorts to set
	 */
	public void setUdpPorts(String udpPorts) {
		this.udpPorts = udpPorts;
	}

	/**
	 * @return the udpConnectionCache
	 */
	public Cache getUdpConnectionCache() {
		return udpConnectionCache;
	}

	/**
	 * @param udpConnectionCache the udpConnectionCache to set
	 */
	public void setUdpConnectionCache(Cache udpConnectionCache) {
		this.udpConnectionCache = udpConnectionCache;
	}

	/**
	 * @return the ticketCache
	 */
	public Cache getTicketCache() {
		return ticketCache;
	}

	/**
	 * @param ticketCache the ticketCache to set
	 */
	public void setTicketCache(Cache ticketCache) {
		this.ticketCache = ticketCache;
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

	/**
	 * @return the distributionService
	 */
	public DistributionService getDistributionService() {
		return distributionService;
	}

	/**
	 * @param distributionService the distributionService to set
	 */
	public void setDistributionService(DistributionService distributionService) {
		this.distributionService = distributionService;
	}
}