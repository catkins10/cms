package com.yuanluesoft.im.service;

import java.util.List;

import com.yuanluesoft.im.cs.pojo.CSSpecialist;
import com.yuanluesoft.im.model.IMClientLogin;
import com.yuanluesoft.im.model.message.ChatDetail;
import com.yuanluesoft.im.model.message.Message;
import com.yuanluesoft.im.model.message.SystemMessageDetail;
import com.yuanluesoft.im.pojo.IMPersonalSetting;
import com.yuanluesoft.jeaf.business.service.BusinessService;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * IM服务
 * @author linchuan
 *
 */
public interface IMService extends BusinessService {
	//用户状态
	public static final byte IM_PERSON_STATUS_OFFLINE = 0; //隐身
	public static final byte IM_PERSON_STATUS_HIDE = 1; //隐身
	public static final byte IM_PERSON_STATUS_ONLINE = 2; //在线
	public static final byte IM_PERSON_STATUS_BUSY = 3; //忙碌
	public static final byte IM_PERSON_STATUS_LEAVE = 4; //离开
	public static final String[] IM_PERSON_STATUS_TEXTS = {"离线", "离线", "在线", "忙碌", "离开"};
	public static final String[] IM_PERSON_STATUS_NAMES = {"offline", "hide", "online", "busy", "leave"};
	
	/**
	 * 处理接收到的消息
	 * @param personId
	 * @param message
	 * @param messageWriter
	 * @throws ServiceException
	 */
	public void processReceivedMessage(long personId, Message message, MessageWriter messageWriter) throws ServiceException;
	
	/**
	 * 客户端登录
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	public IMClientLogin clientLogin(long userId) throws ServiceException;
	
	/**
	 * 加载对话
	 * @param currentUserId
	 * @param chatId
	 * @return
	 * @throws ServiceException
	 */
	public ChatDetail loadChat(long currentUserId, long chatId) throws ServiceException;
	
	/**
	 * 获取客服人员列表,并转换为WebimPerson
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public List listSpecialists(long siteId) throws ServiceException;
	
	/**
	 * 获取空闲的客服人员
	 * @param siteId
	 * @return
	 * @throws ServiceException
	 */
	public CSSpecialist getFreeSpecialist(long siteId) throws ServiceException;
	
	/**
	 * 获取在线用户数
	 * @param currentUserId
	 * @return
	 * @throws ServiceException
	 */
	public int countOnlinePersons(long currentUserId) throws ServiceException;
	
	/**
	 * 获取在线用户(IMPerson)列表
	 * @param currentUserId
	 * @param offset
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
	public List listOnlinePersons(long currentUserId, int offset, int limit) throws ServiceException;
	
	/**
	 * 获取最近联系的用户(IMPerson)列表
	 * @param currentUserId
	 * @param limit
	 * @return
	 * @throws ServiceException
	 */
	public List listRecentPersons(long currentUserId, int limit) throws ServiceException;
	
	/**
	 * 获取用户状态
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	public byte getPersonStatus(long personId) throws ServiceException;
	
	/**
	 * 获取用户使用的客户端类型, WEBIM/IMClient
	 * @param personId
	 * @return
	 * @throws ServiceException
	 */
	public String getClientType(long personId) throws ServiceException;
	
	/**
	 * 获取附件在服务器上的保存天数
	 * @return
	 */
	public int getAttachmentSaveDays();
	
	/**
	 * 加载个人设置
	 * @param currentUserId
	 * @return
	 * @throws ServiceException
	 */
	public IMPersonalSetting loadPersonalSetting(long currentUserId) throws ServiceException;
	
	/**
	 * 发送一个消息
	 * @param receiverId
	 * @param message
	 * @throws ServiceException
	 */
	public void sendMessage(final long receiverId, final Message message) throws ServiceException;

	/**
	 * 发送一个系统消息
	 * @param systemMessage
	 * @param receiverId
	 * @throws ServiceException
	 */
	public void sendSystemMessage(com.yuanluesoft.jeaf.messagecenter.pojo.Message systemMessage, long receiverId) throws ServiceException;
	
	/**
	 * 获取发言记录
	 * @param chatId
	 * @param personId
	 * @param max
	 * @return
	 * @throws ServiceException
	 */
	public List listChatTalks(long chatId, long personId, int max) throws ServiceException;
	
	/**
	 * 获取系统消息详细内容
	 * @param currentUserId
	 * @param systemMessageId
	 * @return
	 * @throws ServiceException
	 */
	public SystemMessageDetail retrieveSystemMessage(long currentUserId, long systemMessageId) throws ServiceException;
}