package com.yuanluesoft.im.parser;

import com.yuanluesoft.im.model.message.ChatDetail;
import com.yuanluesoft.im.model.message.ChatDetailRequest;
import com.yuanluesoft.im.model.message.CreateChat;
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
import com.yuanluesoft.im.model.message.PersonStatusChanged;
import com.yuanluesoft.im.model.message.SystemMessage;
import com.yuanluesoft.im.model.message.SystemMessageDetail;
import com.yuanluesoft.im.model.message.SystemMessageDetailRequest;
import com.yuanluesoft.im.model.message.SystemMessageFeedback;
import com.yuanluesoft.im.model.message.Talk;
import com.yuanluesoft.im.model.message.TalkDetail;
import com.yuanluesoft.im.model.message.TalkDetailRequest;
import com.yuanluesoft.im.model.message.TalkSubmit;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.util.BufferUtils;

/**
 * 消息解析器
 * @author linchuan
 *
 */
public class MessageParser {

	/**
	 * 解析消息
	 * @param buffer
	 * @param len
	 * @return
	 */
	public Message parseMessage(byte[] buffer, int len) {
		byte command = buffer[0];
		int[] beginIndex = {1};
		Message message = null;
		switch(command) {
	    case Message.CMD_LOGIN: //登录
	        message = parseLogin(buffer, len, beginIndex);
	        break;

		case Message.CMD_LOGIN_ACK: //登录应答
	        message = parseLoginAck(buffer, len, beginIndex);
	        break;

		case Message.CMD_LOGOFF: //客户端登录后,通知其他客户端注销
	        message = parseLogoff(buffer, len, beginIndex);
	        break;

		case Message.CMD_EXIT_CLIENT: //退出客户端
	        message = parseExitClient(buffer, len, beginIndex);
	        break;

		case Message.CMD_KEEP_ALIVE: //心跳
	        message = parseKeepAlive(buffer, len, beginIndex);
	        break;

		case Message.CMD_PERSON_STATUS_CHANGED: //用户改变状态
	        message = parsePersonStatusChanged(buffer, len, beginIndex);
	        break;

		case Message.CMD_CREATE_CHAT: //创建对话
	        message = parseCreateChat(buffer, len, beginIndex);
	        break;

		case Message.CMD_CREATE_GROUP_CHAT: //创建多人对话
	        message = parseCreateGroupChat(buffer, len, beginIndex);
	        break;
	        
		case Message.CMD_CHAT_DETAIL_REQUEST: //请求获取对话详情
	        message = parseChatDetailRequest(buffer, len, beginIndex);
	        break;

		case Message.CMD_CHAT_DETAIL: //对话详情
	        message = parseChatDetail(buffer, len, beginIndex);
	        break;

		case Message.CMD_TALK_SUBMIT: //提交发言
	        message = parseTalkSubmit(buffer, len, beginIndex);
	        break;

		case Message.CMD_TALK: //对话对象发言
	        message = parseTalk(buffer, len, beginIndex);
	        break;

		case Message.CMD_TALK_DETAIL_REQUEST: //请求获取发言内容
	        message = parseTalkDetailRequest(buffer, len, beginIndex);
	        break;

		case Message.CMD_TALK_DETAIL: //发言内容列表,是对CMD_TALK_RETRIEVE_DETAIL的应答
	        message = parseTalkDetail(buffer, len, beginIndex);
	        break;

		case Message.CMD_SYSTEM_MESSAGE: //系统消息通知
	        message = parseSystemMessage(buffer, len, beginIndex);
	        break;

		case Message.CMD_SYSTEM_MESSAGE_DETAIL_REQUEST: //请求获取系统消息内容
	        message = parseSystemMessageDetailRequest(buffer, len, beginIndex);
	        break;

		case Message.CMD_SYSTEM_MESSAGE_DETAIL: //系统消息内容
	        message = parseSystemMessageDetail(buffer, len, beginIndex);
	        break;

		case Message.CMD_SYSTEM_MESSAGE_FEEDBACK: //系统消息反馈
	        message = parseSystemMessageFeedback(buffer, len, beginIndex);
	        break;

		case Message.CMD_FILE_TRANSFER_REQUEST: //文件传送请求
	        message = parseFileTransferRequest(buffer, len, beginIndex);
	        break;

		case Message.CMD_FILE_TRANSFER_ACK: //文件传送请求应答
	        message = parseFileTransferAck(buffer, len, beginIndex);
	        break;
	        
		case Message.CMD_FILE_TRANSFER_CANCEL: //放弃文件传送
	        message = parseFileTransferCancel(buffer, len, beginIndex);
	        break;
	        
		case Message.CMD_FILE_TRANSFER_COMPLETE: //文件传送完成
	        message = parseFileTransferComplete(buffer, len, beginIndex);
	        break;
	    }
	    if(message==null) {
	        return null;
	    }
	    return message;
	}

	/**
	 * 填充消息到缓存
	 * @param message
	 * @param buffer
	 * @return
	 * @throws ParseException
	 */
	public int putMessageIntoBuffer(Message message, byte[] buffer) {
		int beginIndex = 0;
		beginIndex = BufferUtils.putByte(message.getCommand(), buffer, beginIndex); //包命令
		switch(message.getCommand()) {
		case Message.CMD_LOGIN: //登录
	        beginIndex = putLogin((Login)message, buffer, beginIndex);
	        break;

		case Message.CMD_LOGIN_ACK: //登录应答
	        beginIndex = putLoginAck((LoginAck)message, buffer, beginIndex);
	        break;

		case Message.CMD_LOGOFF: //客户端登录后,通知其他客户端注销
	        beginIndex = putLogoff((Logoff)message, buffer, beginIndex);
	        break;

		case Message.CMD_EXIT_CLIENT: //退出客户端
	        beginIndex = putExitClient((ExitClient)message, buffer, beginIndex);
	        break;

		case Message.CMD_KEEP_ALIVE: //心跳
	        beginIndex = putKeepAlive((KeepAlive)message, buffer, beginIndex);
	        break;

		case Message.CMD_PERSON_STATUS_CHANGED: //用户改变状态
	        beginIndex = putPersonStatusChanged((PersonStatusChanged)message, buffer, beginIndex);
	        break;
	        
		case Message.CMD_CREATE_CHAT: //创建对话
			beginIndex = putCreateChat((CreateChat)message, buffer, beginIndex);
	        break;

		case Message.CMD_CREATE_GROUP_CHAT: //创建多人对话
			beginIndex = putCreateGroupChat((CreateGroupChat)message, buffer, beginIndex);
	        break;

		case Message.CMD_CHAT_DETAIL_REQUEST: //请求获取对话详情
	        beginIndex = putChatDetailRequest((ChatDetailRequest)message, buffer, beginIndex);
	        break;

		case Message.CMD_CHAT_DETAIL: //对话详情
	        beginIndex = putChatDetail((ChatDetail)message, buffer, beginIndex);
	        break;

		case Message.CMD_TALK_SUBMIT: //提交发言
	        beginIndex = putTalkSubmit((TalkSubmit)message, buffer, beginIndex);
	        break;

		case Message.CMD_TALK: //对话对象发言
	        beginIndex = putTalk((Talk)message, buffer, beginIndex);
	        break;

		case Message.CMD_TALK_DETAIL_REQUEST: //请求获取发言内容
	        beginIndex = putTalkDetailRequest((TalkDetailRequest)message, buffer, beginIndex);
	        break;

		case Message.CMD_TALK_DETAIL: //发言内容列表,是对CMD_TALK_RETRIEVE_DETAIL的应答
	        beginIndex = putTalkDetail((TalkDetail)message, buffer, beginIndex);
	        break;

		case Message.CMD_SYSTEM_MESSAGE: //系统消息通知
	        beginIndex = putSystemMessage((SystemMessage)message, buffer, beginIndex);
	        break;

		case Message.CMD_SYSTEM_MESSAGE_DETAIL_REQUEST: //请求获取系统消息内容
	        beginIndex = putSystemMessageDetailRequest((SystemMessageDetailRequest)message, buffer, beginIndex);
	        break;

		case Message.CMD_SYSTEM_MESSAGE_DETAIL: //系统消息内容
	        beginIndex = putSystemMessageDetail((SystemMessageDetail)message, buffer, beginIndex);
	        break;

		case Message.CMD_SYSTEM_MESSAGE_FEEDBACK: //系统消息反馈
	        beginIndex = putSystemMessageFeedback((SystemMessageFeedback)message, buffer, beginIndex);
	        break;

		case Message.CMD_FILE_TRANSFER_REQUEST: //文件传送请求
	        beginIndex = putFileTransferRequest((FileTransferRequest)message, buffer, beginIndex);
	        break;

		case Message.CMD_FILE_TRANSFER_ACK: //文件传送请求应答
	        beginIndex = putFileTransferAck((FileTransferAck)message, buffer, beginIndex);
	        break;
	        
		case Message.CMD_FILE_TRANSFER_CANCEL: //放弃文件传送
			 beginIndex = putFileTransferCancel((FileTransferCancel)message, buffer, beginIndex);
		     break;
		     
		case Message.CMD_FILE_TRANSFER_COMPLETE: //文件传送完成
			 beginIndex = putFileTransferComplete((FileTransferComplete)message, buffer, beginIndex);
		     break;
		}
	    return beginIndex;
	}

	/**
	 * 解析登录
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private Login parseLogin(byte[] buffer, int len, int[] beginIndex) {
		Login login = new Login();
		login.setTicket(BufferUtils.getString(buffer, beginIndex, len)); //登录钥匙
		login.setStatus(BufferUtils.getByte(buffer, beginIndex)); //状态
		return login;
	}
		
	/**
	 * 解析登录应答
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private LoginAck parseLoginAck(byte[] buffer, int len, int[] beginIndex) {
		LoginAck loginAck = new LoginAck();
		loginAck.setStatus(BufferUtils.getByte(buffer, beginIndex)); //状态
		loginAck.setOfflineChats(BufferUtils.getString(buffer, beginIndex, len)); //有留言的对话ID列表
		return loginAck;
	}

	/**
	 * 解析强制注销
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private Logoff parseLogoff(byte[] buffer, int len, int[] beginIndex) {
		Logoff logoff = new Logoff();
		return logoff;
	}

	/**
	 * 解析退出
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private ExitClient parseExitClient(byte[] buffer, int len, int[] beginIndex) {
		ExitClient exitClient = new ExitClient();
		return exitClient;
	}

	/**
	 * 解析心跳包
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private KeepAlive parseKeepAlive(byte[] buffer, int len, int[] beginIndex) {
		KeepAlive keepAlive = new KeepAlive();
		return keepAlive;
	}

	/**
	 * 解析用户状态改变
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private PersonStatusChanged parsePersonStatusChanged(byte[] buffer, int len, int[] beginIndex) {
		PersonStatusChanged personStatusChanged = new PersonStatusChanged();
		personStatusChanged.setPersonId(BufferUtils.getLong(buffer, beginIndex)); //用户ID
		personStatusChanged.setPersonName(BufferUtils.getString(buffer, beginIndex, len)); //用户名
		personStatusChanged.setStatus(BufferUtils.getByte(buffer, beginIndex)); //状态
		personStatusChanged.setPortraitURL(BufferUtils.getString(buffer, beginIndex, len)); //用户头像
		return personStatusChanged;
	}
	
	/**
	 * 解析创建对话
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private CreateChat parseCreateChat(byte[] buffer, int len, int[] beginIndex) {
		CreateChat createChat = new CreateChat();
		createChat.setChatPersonId(BufferUtils.getLong(buffer, beginIndex)); //对话用户ID
		return createChat;
	}
	
	/**
	 * 解析创建多人对话
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private CreateGroupChat parseCreateGroupChat(byte[] buffer, int len, int[] beginIndex) {
		CreateGroupChat createGroupChat = new CreateGroupChat();
		createGroupChat.setFromChatId(BufferUtils.getLong(buffer, beginIndex)); //源对话ID
		createGroupChat.setChatPersonIds(BufferUtils.getString(buffer, beginIndex, len)); //对话用户ID
		return createGroupChat;
	}
	
	/**
	 * 解析请求获取对话详情
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private ChatDetailRequest parseChatDetailRequest(byte[] buffer, int len, int[] beginIndex) {
		ChatDetailRequest chatDetailRequest = new ChatDetailRequest();
		chatDetailRequest.setChatId(BufferUtils.getLong(buffer, beginIndex)); //对话ID
		return chatDetailRequest;
	}
	
	/**
	 * 解析对话详情
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private ChatDetail parseChatDetail(byte[] buffer, int len, int[] beginIndex) {
		ChatDetail chatDetail = new ChatDetail();
		chatDetail.setChatId(BufferUtils.getLong(buffer, beginIndex)); //对话ID
		chatDetail.setCustomerService(BufferUtils.getBoolean(buffer, beginIndex)); //是否客服对话
		chatDetail.setChatPersonIds(BufferUtils.getString(buffer, beginIndex, len)); //聊天用户ID列表,用逗号分隔,不包括自己
		chatDetail.setChatPersonNames(BufferUtils.getString(buffer, beginIndex, len)); //聊天用户姓名列表,用逗号分隔,不包括自己
		chatDetail.setChatPersonStatus(BufferUtils.getByte(buffer, beginIndex)); //对话用户的状态
		chatDetail.setUnreadTalkCount(BufferUtils.getInt(buffer, beginIndex)); //未读取的发言数量
		chatDetail.setLastReadTime(BufferUtils.getLong(buffer, beginIndex)); //最后接收消息的时间
		return chatDetail;
	}

	/**
	 * 解析提交发言
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private TalkSubmit parseTalkSubmit(byte[] buffer, int len, int[] beginIndex) {
		TalkSubmit talkSubmit = new TalkSubmit();
		talkSubmit.setChatId(BufferUtils.getLong(buffer, beginIndex)); //对话ID
		talkSubmit.setCustomerServiceChatId(BufferUtils.getLong(buffer, beginIndex)); //客服对话ID
		talkSubmit.setContent(BufferUtils.getString(buffer, beginIndex, len)); //内容
		return talkSubmit;
	}

	/**
	 * 解析发言
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private Talk parseTalk(byte[] buffer, int len, int[] beginIndex) {
		Talk talk = new Talk();
		talk.setChatId(BufferUtils.getLong(buffer, beginIndex)); //对话ID
		talk.setSelfTalk(BufferUtils.getBoolean(buffer, beginIndex)); //是否用户自己的发言
		talk.setGroupChat(BufferUtils.getBoolean(buffer, beginIndex)); //是否多人对话
		talk.setCustomerService(BufferUtils.getBoolean(buffer, beginIndex)); //是否客服对话
		talk.setChatPersonNames(BufferUtils.getString(buffer, beginIndex, len)); //对话用户姓名
		return talk;
	}

	/**
	 * 解析请求获取发言内容
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private TalkDetailRequest parseTalkDetailRequest(byte[] buffer, int len, int[] beginIndex) {
		TalkDetailRequest talkDetailRequest = new TalkDetailRequest();
		talkDetailRequest.setChatId(BufferUtils.getLong(buffer, beginIndex)); //对话ID
		talkDetailRequest.setCustomerServiceChatId(BufferUtils.getLong(buffer, beginIndex)); //客服对话ID
		talkDetailRequest.setBeginTime(BufferUtils.getLong(buffer, beginIndex)); //开始时间
		return talkDetailRequest;
	}

	/**
	 * 解析发言内容列表,是对CMD_TALK_RETRIEVE_DETAIL的应答
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private TalkDetail parseTalkDetail(byte[] buffer, int len, int[] beginIndex) {
		TalkDetail talkDetail = new TalkDetail();
		talkDetail.setChatId(BufferUtils.getLong(buffer, beginIndex)); //对话ID
		talkDetail.setContent(BufferUtils.getString(buffer, beginIndex, len)); //内容
		talkDetail.setCreatorId(BufferUtils.getLong(buffer, beginIndex)); //发言人ID
		talkDetail.setCreator(BufferUtils.getString(buffer, beginIndex, len)); //发言人
		talkDetail.setCreatedMillis(BufferUtils.getLong(buffer, beginIndex)); //发言时间
		return talkDetail;
	}

	/**
	 * 解析系统消息通知
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private SystemMessage parseSystemMessage(byte[] buffer, int len, int[] beginIndex) {
		SystemMessage systemMessage = new SystemMessage();
		systemMessage.setSystemMessageId(BufferUtils.getLong(buffer, beginIndex)); //系统消息ID
		return systemMessage;
	}
			
	/**
	 * 解析请求获取系统消息内容
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private SystemMessageDetailRequest parseSystemMessageDetailRequest(byte[] buffer, int len, int[] beginIndex) {
		SystemMessageDetailRequest systemMessageDetailRequest = new SystemMessageDetailRequest();
		systemMessageDetailRequest.setSystemMessageId(BufferUtils.getLong(buffer, beginIndex)); //系统消息ID
		return systemMessageDetailRequest;
	}

	/**
	 * 解析系统消息内容
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private SystemMessageDetail parseSystemMessageDetail(byte[] buffer, int len, int[] beginIndex) {
		SystemMessageDetail systemMessageDetail = new SystemMessageDetail();
		systemMessageDetail.setSystemMessageId(BufferUtils.getLong(buffer, beginIndex)); //系统消息ID
		systemMessageDetail.setSenderName(BufferUtils.getString(buffer, beginIndex, len)); //发送人
		systemMessageDetail.setSendTime(BufferUtils.getTime(buffer, beginIndex)); //通知时间
		systemMessageDetail.setContent(BufferUtils.getString(buffer, beginIndex, len)); //内容
		systemMessageDetail.setWebLink(BufferUtils.getString(buffer, beginIndex, len)); //HTTP链接
		return systemMessageDetail;
	}

	/**
	 * 解析系统消息反馈
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private SystemMessageFeedback parseSystemMessageFeedback(byte[] buffer, int len, int[] beginIndex) {
		SystemMessageFeedback systemMessageFeedback = new SystemMessageFeedback();
		systemMessageFeedback.setSystemMessageId(BufferUtils.getLong(buffer, beginIndex)); //系统消息ID
		return systemMessageFeedback;
	}

	/**
	 * 解析文件传送请求
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private FileTransferRequest parseFileTransferRequest(byte[] buffer, int len, int[] beginIndex) {
		FileTransferRequest fileTransferRequest = new FileTransferRequest();
		fileTransferRequest.setChatId(BufferUtils.getLong(buffer, beginIndex)); //对话ID
		fileTransferRequest.setSenderServerIp(BufferUtils.getString(buffer, beginIndex, len)); //发送人连接的服务器IP
		fileTransferRequest.setSenderServerPort(BufferUtils.getChar(buffer, beginIndex)); //发送人连接的服务器端口
		fileTransferRequest.setSenderInternetIp(BufferUtils.getString(buffer, beginIndex, len)); //发送人公网IP
		fileTransferRequest.setSenderInternetPort(BufferUtils.getChar(buffer, beginIndex)); //发送人公网端口
		fileTransferRequest.setSenderIntranetIp(BufferUtils.getString(buffer, beginIndex, len)); //发送人内网IP
		fileTransferRequest.setSenderIntranetPort(BufferUtils.getChar(buffer, beginIndex)); //发送人内网端口
		fileTransferRequest.setFileId(BufferUtils.getLong(buffer, beginIndex)); //文件ID,由发送者生成,以避免应答时产生冲突
		fileTransferRequest.setFileSize(BufferUtils.getLong(buffer, beginIndex)); //文件长度
		fileTransferRequest.setFileName(BufferUtils.getString(buffer, beginIndex, len)); //文件名
		return fileTransferRequest;
	}

	/**
	 * 解析文件传送请求应答
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private FileTransferAck parseFileTransferAck(byte[] buffer, int len, int[] beginIndex) {
		FileTransferAck fileTransferAck = new FileTransferAck();
		fileTransferAck.setChatId(BufferUtils.getLong(buffer, beginIndex)); //对话ID
		fileTransferAck.setFileId(BufferUtils.getLong(buffer, beginIndex)); //文件ID,由发送者生成,以避免应答时产生冲突
		fileTransferAck.setAccept(BufferUtils.getByte(buffer, beginIndex)); //是否接收: 0/不接收, 1/接收, 2/离线接收(服务器转发)
		fileTransferAck.setFileSeek(BufferUtils.getLong(buffer, beginIndex)); //从文件指定位置开始传输,用于续传
		return fileTransferAck;
	}
	
	/**
	 * 解析放弃文件传送
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private FileTransferCancel parseFileTransferCancel(byte[] buffer, int len, int[] beginIndex) {
		FileTransferCancel fileTransferCancel = new FileTransferCancel();
		fileTransferCancel.setChatId(BufferUtils.getLong(buffer, beginIndex)); //对话ID
		fileTransferCancel.setOperatorId(BufferUtils.getLong(buffer, beginIndex)); //放弃传输的用户ID
		fileTransferCancel.setOperatorName(BufferUtils.getString(buffer, beginIndex, len)); //放弃传输的用户
		fileTransferCancel.setFileId(BufferUtils.getLong(buffer, beginIndex)); //文件ID,由发送者生成,以避免应答时产生冲突
		return fileTransferCancel;
	}
	
	/**
	 * 解析文件传送完成
	 * @param buffer
	 * @param len
	 * @param beginIndex
	 * @return
	 */
	private FileTransferComplete parseFileTransferComplete(byte[] buffer, int len, int[] beginIndex) {
		FileTransferComplete fileTransferComplete = new FileTransferComplete();
		fileTransferComplete.setChatId(BufferUtils.getLong(buffer, beginIndex)); //对话ID
		fileTransferComplete.setFileId(BufferUtils.getLong(buffer, beginIndex)); //文件ID,由发送者生成,以避免应答时产生冲突
		fileTransferComplete.setOfflineFileName(BufferUtils.getString(buffer, beginIndex, len)); //离线文件名称
		return fileTransferComplete;
	}
	

	/**
	 * 填充登录
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putLogin(Login message, byte[] buffer, int beginIndex) {
		beginIndex = BufferUtils.putString(message.getTicket(), buffer, beginIndex); //登录钥匙
		beginIndex = BufferUtils.putByte(message.getStatus(), buffer, beginIndex); //状态
		return beginIndex;
	}
		
	/**
	 * 登录应答
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putLoginAck(LoginAck message, byte[] buffer, int beginIndex) {
		beginIndex = BufferUtils.putByte(message.getStatus(), buffer, beginIndex); //状态
		beginIndex = BufferUtils.putString(message.getOfflineChats(), buffer, beginIndex); //有留言的对话ID列表
		return beginIndex;
	}

	/**
	 * 强制注销
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putLogoff(Logoff message, byte[] buffer, int beginIndex) {
		return beginIndex;
	}

	/**
	 * 退出
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putExitClient(ExitClient message, byte[] buffer, int beginIndex) {
		return beginIndex;
	}

	/**
	 * 心跳包
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putKeepAlive(KeepAlive message, byte[] buffer, int beginIndex) {
		return beginIndex;
	}

	/**
	 * 用户状态改变
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putPersonStatusChanged(PersonStatusChanged message, byte[] buffer, int beginIndex) {
		beginIndex = BufferUtils.putLong(message.getPersonId(), buffer, beginIndex); //用户ID
		beginIndex = BufferUtils.putString(message.getPersonName(), buffer, beginIndex); //用户名
		beginIndex = BufferUtils.putByte(message.getStatus(), buffer, beginIndex); //状态
		beginIndex = BufferUtils.putString(message.getPortraitURL(), buffer, beginIndex); //用户头像
		return beginIndex;
	}
	
	/**
	 * 创建对话
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putCreateChat(CreateChat message, byte[] buffer, int beginIndex) {
		beginIndex = BufferUtils.putLong(message.getChatPersonId(), buffer, beginIndex); //对话用户ID
		return beginIndex;
	}
	
	/**
	 * 创建多人对话
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putCreateGroupChat(CreateGroupChat message, byte[] buffer, int beginIndex) {
		beginIndex = BufferUtils.putLong(message.getFromChatId(), buffer, beginIndex); //源对话ID
		beginIndex = BufferUtils.putString(message.getChatPersonIds(), buffer, beginIndex); //对话用户ID
		return beginIndex;
	}

	/**
	 * 请求获取对话详情
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putChatDetailRequest(ChatDetailRequest message, byte[] buffer, int beginIndex) {
		beginIndex = BufferUtils.putLong(message.getChatId(), buffer, beginIndex); //对话ID
		return beginIndex;
	}

	/**
	 * 对话详情
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putChatDetail(ChatDetail message, byte[] buffer, int beginIndex) {
		beginIndex = BufferUtils.putLong(message.getChatId(), buffer, beginIndex); //对话ID
		beginIndex = BufferUtils.putBoolean(message.isCustomerService(), buffer, beginIndex); //是否客服对话
		beginIndex = BufferUtils.putString(message.getChatPersonIds(), buffer, beginIndex); //聊天用户ID列表,用逗号分隔,不包括自己
		beginIndex = BufferUtils.putString(message.getChatPersonNames(), buffer, beginIndex); //聊天用户姓名列表,用逗号分隔,不包括自己
		beginIndex = BufferUtils.putByte(message.getChatPersonStatus(), buffer, beginIndex); //对话用户的状态
		beginIndex = BufferUtils.putInt(message.getUnreadTalkCount(), buffer, beginIndex); //未读取的发言数量
		beginIndex = BufferUtils.putLong(message.getLastReadTime(), buffer, beginIndex); //最后接收消息的时间
		return beginIndex;
	}

	/**
	 * 提交发言
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putTalkSubmit(TalkSubmit message, byte[] buffer, int beginIndex) {
		beginIndex = BufferUtils.putLong(message.getChatId(), buffer, beginIndex); //对话ID
		beginIndex = BufferUtils.putLong(message.getCustomerServiceChatId(), buffer, beginIndex); //客服对话ID
		beginIndex = BufferUtils.putString(message.getContent(), buffer, beginIndex); //内容
		return beginIndex;
	}

	/**
	 * 发言
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putTalk(Talk message, byte[] buffer, int beginIndex) {
		beginIndex = BufferUtils.putLong(message.getChatId(), buffer, beginIndex); //对话ID
		beginIndex = BufferUtils.putBoolean(message.isSelfTalk(), buffer, beginIndex); //是否用户自己的发言
		beginIndex = BufferUtils.putBoolean(message.isGroupChat(), buffer, beginIndex); //是否多人对话
		beginIndex = BufferUtils.putBoolean(message.isCustomerService(), buffer, beginIndex); //是否客服对话
		beginIndex = BufferUtils.putString(message.getChatPersonNames(), buffer, beginIndex); //对话用户姓名
		return beginIndex;
	}

	/**
	 * 请求获取发言内容
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putTalkDetailRequest(TalkDetailRequest message, byte[] buffer, int beginIndex) {
		beginIndex = BufferUtils.putLong(message.getChatId(), buffer, beginIndex); //对话ID
		beginIndex = BufferUtils.putLong(message.getCustomerServiceChatId(), buffer, beginIndex); //客服对话ID
		beginIndex = BufferUtils.putLong(message.getBeginTime(), buffer, beginIndex); //开始时间
		return beginIndex;
	}

	/**
	 * 发言内容列表,是对CMD_TALK_RETRIEVE_DETAIL的应答
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putTalkDetail(TalkDetail message, byte[] buffer, int beginIndex) {
		beginIndex = BufferUtils.putLong(message.getChatId(), buffer, beginIndex); //对话ID
		beginIndex = BufferUtils.putString(message.getContent(), buffer, beginIndex); //内容
		beginIndex = BufferUtils.putLong(message.getCreatorId(), buffer, beginIndex); //发言人ID
		beginIndex = BufferUtils.putString(message.getCreator(), buffer, beginIndex); //发言人
		beginIndex = BufferUtils.putLong(message.getCreatedMillis(), buffer, beginIndex); //发言时间
		return beginIndex;
	}

	/**
	 * 系统消息通知
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putSystemMessage(SystemMessage message, byte[] buffer, int beginIndex) {
		beginIndex = BufferUtils.putLong(message.getSystemMessageId(), buffer, beginIndex); //系统消息ID
		return beginIndex;
	}
			
	/**
	 * 请求获取系统消息内容
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putSystemMessageDetailRequest(SystemMessageDetailRequest message, byte[] buffer, int beginIndex) {
		beginIndex = BufferUtils.putLong(message.getSystemMessageId(), buffer, beginIndex); //系统消息ID
		return beginIndex;
	}

	/**
	 * 系统消息内容
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putSystemMessageDetail(SystemMessageDetail message, byte[] buffer, int beginIndex) {
		beginIndex = BufferUtils.putLong(message.getSystemMessageId(), buffer, beginIndex); //系统消息ID
		beginIndex = BufferUtils.putString(message.getSenderName(), buffer, beginIndex); //发送人
		beginIndex = BufferUtils.putTime(message.getSendTime(), buffer, beginIndex); //通知时间
		beginIndex = BufferUtils.putString(message.getContent(), buffer, beginIndex); //内容
		beginIndex = BufferUtils.putString(message.getWebLink(), buffer, beginIndex); //HTTP链接
		return beginIndex;
	}

	/**
	 * 系统消息反馈
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putSystemMessageFeedback(SystemMessageFeedback message, byte[] buffer, int beginIndex) {
		beginIndex = BufferUtils.putLong(message.getSystemMessageId(), buffer, beginIndex); //系统消息ID
		return beginIndex;
	}

	/**
	 * 文件传送请求
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putFileTransferRequest(FileTransferRequest message, byte[] buffer, int beginIndex) {
		beginIndex = BufferUtils.putLong(message.getChatId(), buffer, beginIndex); //对话ID
		beginIndex = BufferUtils.putString(message.getSenderServerIp(), buffer, beginIndex); //发送人连接的服务器IP
		beginIndex = BufferUtils.putChar(message.getSenderServerPort(), buffer, beginIndex); //发送人连接的服务器端口
		beginIndex = BufferUtils.putString(message.getSenderInternetIp(), buffer, beginIndex); //发送人公网IP
		beginIndex = BufferUtils.putChar(message.getSenderInternetPort(), buffer, beginIndex); //发送人公网端口
		beginIndex = BufferUtils.putString(message.getSenderIntranetIp(), buffer, beginIndex); //发送人内网IP
		beginIndex = BufferUtils.putChar(message.getSenderIntranetPort(), buffer, beginIndex); //发送人内网端口
		beginIndex = BufferUtils.putLong(message.getFileId(), buffer, beginIndex); //文件ID,由发送者生成,以避免应答时产生冲突
		beginIndex = BufferUtils.putLong(message.getFileSize(), buffer, beginIndex); //文件长度
		beginIndex = BufferUtils.putString(message.getFileName(), buffer, beginIndex); //文件名
		return beginIndex;
	}

	/**
	 * 文件传送请求应答
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putFileTransferAck(FileTransferAck message, byte[] buffer, int beginIndex) {
		beginIndex = BufferUtils.putLong(message.getChatId(), buffer, beginIndex); //对话ID
		beginIndex = BufferUtils.putLong(message.getFileId(), buffer, beginIndex); //文件ID,由发送者生成,以避免应答时产生冲突
		beginIndex = BufferUtils.putByte(message.getAccept(), buffer, beginIndex); //是否接收: 0/不接收, 1/接收, 2/离线接收(服务器转发)
		beginIndex = BufferUtils.putLong(message.getFileSeek(), buffer, beginIndex); //从文件指定位置开始传输,用于续传
		return beginIndex;
	}
	
	/**
	 * 放弃文件传输
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putFileTransferCancel(FileTransferCancel message, byte[] buffer, int beginIndex) {
		beginIndex = BufferUtils.putLong(message.getChatId(), buffer, beginIndex); //对话ID
		beginIndex = BufferUtils.putLong(message.getOperatorId(), buffer, beginIndex); //放弃传输的用户ID
		beginIndex = BufferUtils.putString(message.getOperatorName(), buffer, beginIndex); //放弃传输的用户姓名
		beginIndex = BufferUtils.putLong(message.getFileId(), buffer, beginIndex); //文件ID,由发送者生成,以避免应答时产生冲突
		return beginIndex;
	}
	
	/**
	 * 文件传输完成
	 * @param message
	 * @param buffer
	 * @param beginIndex
	 * @return
	 */
	private int putFileTransferComplete(FileTransferComplete message, byte[] buffer, int beginIndex) {
		beginIndex = BufferUtils.putLong(message.getChatId(), buffer, beginIndex); //对话ID
		beginIndex = BufferUtils.putLong(message.getFileId(), buffer, beginIndex); //文件ID,由发送者生成,以避免应答时产生冲突
		beginIndex = BufferUtils.putString(message.getOfflineFileName(), buffer, beginIndex); //离线文件名称
		return beginIndex;
	}
}