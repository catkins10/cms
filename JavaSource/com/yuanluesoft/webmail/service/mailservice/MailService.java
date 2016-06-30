package com.yuanluesoft.webmail.service.mailservice;

import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.webmail.model.MailSession;
import com.yuanluesoft.webmail.pojo.Mail;

/**
 * 
 * @author linchuan
 *
 */
public interface MailService {
	//邮件会话类型
	public static final int MAIL_SESSION_TYPE_RECEIVE = 0; //收邮件
	public static final int MAIL_SESSION_TYPE_SEND = 1; //发邮件
	public static final int MAIL_SESSION_TYPE_MANAGE = 2; //管理
	
	/**
	 * 登录邮件服务器
	 * @param mailAddress
	 * @param serverHost
	 * @param servicePort
	 * @param loginName
	 * @param password
	 * @param sessionType 0/收邮件,1/发邮件,2/管理
	 * @return
	 * @throws ServiceException
	 */
	public MailSession login(String mailAddress, String serverHost, int servicePort, String loginName, String password, int sessionType) throws ServiceException;
	
	/**
	 * 注销
	 * @param mailSession
	 * @throws ServiceException
	 */
	public void logout(MailSession mailSession) throws ServiceException;

	 /**
	  * 写邮件
	  * @param mailSession
	  * @param mail
	  * @param mailWriteCallback
	  * @throws ServiceException
	  */
    public void writeMail(MailSession mailSendSession, Mail mail, MailWriteCallback mailWriteCallback) throws ServiceException;
    
    /**
     * 获取邮件(MailInfo)列表
     * @param mailSession
     * @return
     * @throws ServiceException
     */
    public List listMails(MailSession mailReceiveSession) throws ServiceException;
    
    /**
     * 读邮件
     * @param mailSession
     * @param mailId
     * @param mailReadCallback
     * @return
     * @throws ServiceException
     */
    public void readMail(MailSession mailReceiveSession, String mailId, MailReadCallback mailReadCallback) throws ServiceException;
   
    /**
     * 删除邮件
     * @param mailSession
     * @param mailId
     * @throws ServiceException
     */
    public void deleteMail(MailSession mailReceiveSession, String mailId) throws ServiceException;
    
    /**
     * 创建一个邮件帐户,返回邮件帐户名称
     * @param mailManageSession
     * @param loginName
     * @param mailDomain
     * @param password
     * @return
     * @throws ServiceException
     */
    public String createMailAccount(MailSession mailManageSession, String loginName, String mailDomain, String password) throws ServiceException;
    
    /**
     * 删除一个帐号
     * @param mailManageSession
     * @param loginName
     * @param mailDomain
     * @throws ServiceException
     */
    public void removeMailAccount(MailSession mailManageSession, String loginName, String mailDomain) throws ServiceException;
    
    /**
     * 修改邮箱密码
     * @param mailManageSession
     * @param loginName
     * @param mailDomain
     * @param password
     * @throws ServiceException
     */
    public void changePassword(MailSession mailManageSession, String loginName, String mailDomain, String password) throws ServiceException;
}