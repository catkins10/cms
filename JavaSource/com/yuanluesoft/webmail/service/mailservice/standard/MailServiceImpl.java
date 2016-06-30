package com.yuanluesoft.webmail.service.mailservice.standard;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.pop3.POP3Client;
import org.apache.commons.net.pop3.POP3MessageInfo;
import org.apache.commons.net.smtp.SMTPClient;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Base64Encoder;
import com.yuanluesoft.webmail.model.MailInfo;
import com.yuanluesoft.webmail.model.MailSession;
import com.yuanluesoft.webmail.pojo.Mail;
import com.yuanluesoft.webmail.service.mailservice.MailReadCallback;
import com.yuanluesoft.webmail.service.mailservice.MailService;
import com.yuanluesoft.webmail.service.mailservice.MailWriteCallback;

/**
 * pop3和smtp邮件服务
 * USER username 认证用户名
 * PASS password 认证密码认证，认证通过则状态转换 
 * APOP name,digest 认可一种安全传输口令的办法，执行成功导致状态转换，请参见 RFC 1321 。
 * STAT 处理请求 server 回送邮箱统计资料，如邮件数、 邮件总字节数
 * UIDL n 处理 server 返回用于该指定邮件的唯一标识， 如果没有指定，返回所有的。
 * LIST n 处理 server 返回指定邮件的大小等 
 * RETR n 处理 server 返回邮件的全部文本 
 * DELE n 处理 server 标记删除，QUIT 命令执行时才真正删除
 * RSET 处理撤消所有的 DELE 命令 
 * TOP n,m 处理 返回 n 号邮件的前 m 行内容，m 必须是自然数 
 * NOOP 处理 server 返回一个肯定的响应 
 * QUIT 希望结束会话。如果 server 处于"处理" 状态，则现在进入"更新"状态，删除那些标记成删除的
 * @author linchuan
 *
 */
public class MailServiceImpl implements MailService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.MailService#login(java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, int)
	 */
	public MailSession login(String mailAddress, String serverHost, int servicePort, String loginName, String password, int sessionType) throws ServiceException {
		if(sessionType==MAIL_SESSION_TYPE_RECEIVE) { //收邮件
			POP3Client pop3Client = new POP3Client();
	        try {
	        	pop3Client.connect(serverHost, servicePort);
	        	pop3Client.setSoTimeout(20000); //设置超时时间20s
	        	if(!pop3Client.login(loginName, password)) {
	        		throw new ServiceException("pop3 not login");
	        	}
	        	return new POP3MailSession(mailAddress, serverHost, servicePort, loginName, password, pop3Client);
	        }
	        catch (Exception e) {
	        	try {
		            pop3Client.logout();
		        }
		        catch (IOException ie) {
		            
		        }
	            Logger.exception(e);
	            throw new ServiceException("pop3 login failed");
	        }
		}
		else if(sessionType==MAIL_SESSION_TYPE_SEND) { //发邮件
			SMTPClient smtpClient = new SMTPClient();
			try {
				smtpClient.connect(serverHost, servicePort); //连接服务器
		        smtpClient.setSoTimeout(20000); //设置超时时间20秒
		        if(!smtpClient.login()) {
		            throw new ServiceException("smtp login failed");
		        }
		        //登录方式用户验证,另外还有明文方式:AUTH PLAIN ["用户名\0密码"的base64编码]
		        if(smtpClient.sendCommand("AUTH LOGIN")!=334) { //开始身份验证
		            throw new ServiceException("smtp login failed");
		        }
		        //验证用户名
		        Base64Encoder base64Encoder = new Base64Encoder();
		        if(smtpClient.sendCommand(base64Encoder.encode(loginName, "UTF-8"))!=334) {
		            throw new ServiceException("smtp login failed");
		        }
		        //验证密码
		        if(smtpClient.sendCommand(base64Encoder.encode(password, "UTF-8"))!=235) { //235 表示验证通过,535为失败
		            throw new ServiceException("smtp login failed");
		        }
		        return new SMTPMailSession(mailAddress, serverHost, servicePort, loginName, password, smtpClient);
		    }
	        catch(Exception e) {
	        	try {
	        		smtpClient.logout();
		        }
		        catch (IOException ie) {
		            
		        }
	        	Logger.exception(e);
	            throw new ServiceException("smtp login failed");
	        }
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.MailService#logout(com.yuanluesoft.webmail.model.MailSession)
	 */
	public void logout(MailSession mailSession) throws ServiceException {
		if(mailSession instanceof POP3MailSession) {
			POP3Client pop3Client = ((POP3MailSession)mailSession).getPop3Client();
			try {
	            pop3Client.logout();
	        }
	        catch (IOException e) {
	            
	        }
	        try {
	            pop3Client.disconnect();
	        }
	        catch (IOException e) {
	            
	        }
		}
		else if(mailSession instanceof SMTPMailSession) {
			SMTPClient smtpClient = ((SMTPMailSession)mailSession).getSmtpClient();
			try {
	    		smtpClient.logout();
	    	}
	    	catch (IOException e) {

	    	}
	    	try {
	    		smtpClient.disconnect();
	    	}
	    	catch (IOException e) {

	    	}
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.MailService#listMails(com.yuanluesoft.webmail.model.MailSession)
	 */
	public List listMails(MailSession mailSession) throws ServiceException {
		POP3Client pop3Client = ((POP3MailSession)mailSession).getPop3Client();
        try {
        	List mailInfoList = new ArrayList();
            POP3MessageInfo[] messages = pop3Client.listUniqueIdentifiers(); //UIDL命令
            POP3MessageInfo[] messageSizes = pop3Client.listMessages(); //调用LIST命令,获取邮件长度
            for(int i=0; i<messages.length; i++) {
            	MailInfo mailInfo = new MailInfo();
                mailInfo.setMailId(messages[i].identifier); //邮件ID
                mailInfo.setMailSize(messageSizes[i].size); //邮件大小
                mailInfoList.add(mailInfo);
            }
            return mailInfoList;
        }
        catch(Exception e) {
            Logger.exception(e);
            throw new ServiceException();
        }
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.MailService#readMail(com.yuanluesoft.webmail.model.MailSession, java.lang.String, com.yuanluesoft.webmail.service.mailservice.MailReadCallback)
	 */
	public void readMail(MailSession mailSession, String mailId, MailReadCallback mailReadCallback) throws ServiceException {
		POP3Client pop3Client = ((POP3MailSession)mailSession).getPop3Client();
		try {
			int mailNumber = retrieveMailNumber(pop3Client, mailId);
			Reader reader = pop3Client.retrieveMessage(mailNumber); //RETR n 处理 server 返回邮件的全部文本 
			mailReadCallback.readMail(reader);
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.MailService#deleteMail(com.yuanluesoft.webmail.model.MailSession, java.lang.String)
	 */
	public void deleteMail(MailSession mailSession, String mailId) throws ServiceException {
		POP3Client pop3Client = ((POP3MailSession)mailSession).getPop3Client();
		try {
			if(!pop3Client.deleteMessage(retrieveMailNumber(pop3Client, mailId))) {
                throw new ServiceException("delete failed");
            }
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
	}
    
    /**
     * 获取邮件序号
     * @param pop3Client
     * @param mailId
     * @return
     * @throws ServiceException
     */
    private int retrieveMailNumber(POP3Client pop3Client, String mailId) throws ServiceException {
    	POP3MessageInfo[] messages;
		try {
			messages = pop3Client.listUniqueIdentifiers();
		}
		catch (IOException e) {
			throw new ServiceException("io error");
		}
        if(messages==null) {
        	 throw new ServiceException("mail has deleted");
        }
        int i = messages.length - 1;
        for(; i>=0 && !mailId.equals(messages[i].identifier); i--);
        if(i<0) {
        	throw new ServiceException("mail has deleted");
        }
        return messages[i].number;
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.MailService#writeMail(com.yuanluesoft.webmail.model.MailSession, com.yuanluesoft.webmail.pojo.Mail, com.yuanluesoft.webmail.service.mailservice.MailWriteCallback)
	 */
	public void writeMail(MailSession mailSession, Mail mail, MailWriteCallback mailWriteCallback) throws ServiceException {
		SMTPClient smtpClient = ((SMTPMailSession)mailSession).getSmtpClient();
		try {
            //设置发件人
            smtpClient.setSender(mail.getMailFrom().substring(mail.getMailFrom().indexOf(" <") + 2, mail.getMailFrom().length() - 1));
            addRecipient(smtpClient, mail.getMailTo()); //主送
            addRecipient(smtpClient, mail.getMailCc()); //抄送
            addRecipient(smtpClient, mail.getMailBcc()); //密送
            Writer writer = smtpClient.sendMessageData(); //打开邮件输出流
            mailWriteCallback.writeMail(writer, mail); //输出邮件
            if(!smtpClient.completePendingCommand()) { //发送失败
                throw new ServiceException();
            }
        }
		catch(Exception e) {
        	Logger.exception(e);
            throw new ServiceException("send mail failed");
        }
	}
    
    /**
     * 添加邮件接收人
     * @param smtpClient
     * @param mailAddresses
     */
    private void addRecipient(SMTPClient smtpClient, String mailAddresses) {
        if(mailAddresses==null || mailAddresses.equals("")) {
            return;
        }
        String[] addresses = mailAddresses.replaceAll(",", ";").split(";");
        for(int i=0; i<addresses.length; i++) {
            String address = addresses[i].trim();
            if(address!=null && !address.equals("")) {
                int index = address.indexOf('<');
                if(index!=-1) {
                    address = address.substring(index + 1, address.length() - 1);
                }
                try {
                    smtpClient.addRecipient(address);
                } catch (IOException e) {
                	
                }
            }
        }
    }
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.MailService#changePassword(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void changePassword(MailSession mailManageSession, String loginName, String mailDomain, String password) throws ServiceException {
		//不需要实现
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.MailService#createMailAccount(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String createMailAccount(MailSession mailManageSession, String loginName, String mailDomain, String password) throws ServiceException {
		//不需要实现
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.MailService#removeMailAccount(java.lang.String, java.lang.String)
	 */
	public void removeMailAccount(MailSession mailManageSession, String loginName, String mailDomain) throws ServiceException {
		//不需要实现
	}
}