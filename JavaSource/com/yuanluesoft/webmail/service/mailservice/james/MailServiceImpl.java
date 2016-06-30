package com.yuanluesoft.webmail.service.mailservice.james;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.webmail.model.MailInfo;
import com.yuanluesoft.webmail.model.MailSession;
import com.yuanluesoft.webmail.service.mailservice.MailReadCallback;
import com.yuanluesoft.webmail.service.mailservice.MailService;

/**
 * 
 * @author linchuan
 *
 */
public class MailServiceImpl extends com.yuanluesoft.webmail.service.mailservice.standard.MailServiceImpl {
	private final String MAIL_FILE_EXT = ".Repository.FileStreamStore";
    private final String MAIL_OBJECT_EXT = ".Repository.FileObjectStore";
    private String inboxDirectory; //收件箱目录
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.standard.MailServiceImpl#login(java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, int)
	 */
	public MailSession login(String mailAddress, String serverHost, int servicePort, String loginName, String password, int sessionType) throws ServiceException {
		if(Logger.isTraceEnabled()) {
			Logger.trace("JamesMailService: login...");
		}
		if(sessionType==MAIL_SESSION_TYPE_RECEIVE) { //收邮件直接访问文件
			return new MailSession(mailAddress, serverHost, servicePort, loginName, password);
		}
		else if(sessionType==MAIL_SESSION_TYPE_SEND) { //发邮件,使用SMTP方式
			return super.login(mailAddress, serverHost, servicePort, loginName, password, sessionType);
		}
		else if(sessionType==MAIL_SESSION_TYPE_MANAGE) { //管理
			//创建telnet连接
			JamesManageSession jamesManageSession = null;
			try {
				Socket telnetClient = new Socket();
				InetSocketAddress address = new InetSocketAddress(serverHost, servicePort);
				telnetClient.connect(address, 10000); //连接时间10秒
				telnetClient.setSoTimeout(10000); //30s
				telnetClient.setReceiveBufferSize(1024);
				telnetClient.setSendBufferSize(1024);
				InputStream inputStream = telnetClient.getInputStream();
	        	OutputStream outputStream = telnetClient.getOutputStream();
	        	jamesManageSession = new JamesManageSession(mailAddress, serverHost, servicePort, loginName, password, telnetClient, inputStream, outputStream);
                String ret = sendManageCommand(jamesManageSession, null); //接收连接信息
                if(ret.indexOf("Login id")==-1) {
                	throw new ServiceException();
                }
                sendManageCommand(jamesManageSession, loginName); //发送用户名
                if(!sendManageCommand(jamesManageSession, password).startsWith("Welcome")) { //发送口令
                    throw new ServiceException();
                }
	        	return jamesManageSession;
	        }
	        catch (Exception e) {
	        	logout(jamesManageSession);
	            throw new ServiceException(e);
	        }
		}
		if(Logger.isTraceEnabled()) {
			Logger.trace("JamesMailService: logined.");
		}
		return null;
	}
	
    /**
     * 发送命令,并接收返回值
     * @param command
     * @return
     * @throws ServiceException
     */
    public String sendManageCommand(JamesManageSession jamesManageSession, String command) throws ServiceException {
    	try {
            if(command!=null) { //发送命令
            	jamesManageSession.getOutputStream().write((command + "\r\n").getBytes());
            	jamesManageSession.getOutputStream().flush();
            }
            for(int i=0; i<200 && jamesManageSession.getInputStream().available()<=0; i++) {
				try {
					Thread.sleep(50);
				}
				catch(InterruptedException e) {
					
				}
			}
            int len;
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			while(jamesManageSession.getInputStream().available()>0 && (len=jamesManageSession.getInputStream().read(buffer))>0) {
				byteArrayOutputStream.write(buffer, 0, len);
			}
	        String ret = byteArrayOutputStream.toString();
	        byteArrayOutputStream.close();
	        if(Logger.isDebugEnabled()) {
        		Logger.debug("JamesMailService: return " + ret);
        	}
            return ret;
        }
        catch (IOException e) {
            throw new ServiceException(e);
        }
    }

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.standard.MailServiceImpl#logout(com.yuanluesoft.webmail.model.MailSession)
	 */
	public void logout(MailSession mailSession) throws ServiceException {
		if(mailSession instanceof JamesManageSession) {
			JamesManageSession jamesManageSession = (JamesManageSession)mailSession;
			try { // 发送退出命令
				sendManageCommand(jamesManageSession, "quit");
            }
			catch (ServiceException e) {
                
            }
            try { //关闭输入流
            	jamesManageSession.getInputStream().close();
            }
            catch (IOException e) {
                
            }
            try { //关闭输出流
            	jamesManageSession.getOutputStream().close();
            }
            catch (IOException e) {
                
            }
            try { //断开连接
            	jamesManageSession.getTelnetClient().close();
            }
            catch (IOException e) {
                
            }
		}
		super.logout(mailSession);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.standard.MailServiceImpl#listMails(com.yuanluesoft.webmail.model.MailSession)
	 */
	public List listMails(MailSession mailSession) throws ServiceException {
		File[] mailFiles = new File(inboxDirectory + mailSession.getLoginName()).listFiles();
        if(mailFiles==null || mailFiles.length==0) {
            return null;
        }
        List mailInfoList = new ArrayList();
        int back = MAIL_FILE_EXT.length();
        for(int i=0; i<mailFiles.length; i++) {
            String name = mailFiles[i].getName();
            if(name.endsWith(MAIL_FILE_EXT)) {
                MailInfo mailInfo = new MailInfo();
                mailInfo.setMailId(name.substring(0, name.length() - back));
                mailInfo.setMailSize((int)mailFiles[i].length());
                mailInfoList.add(mailInfo);
            }
        }
        return mailInfoList;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.standard.MailServiceImpl#readMail(com.yuanluesoft.webmail.model.MailAccount, java.lang.String, com.yuanluesoft.webmail.service.mailservice.MailReadCallback)
	 */
	public void readMail(MailSession mailSession, String mailId, MailReadCallback mailReadCallback) throws ServiceException {
		try {
			Reader reader = new FileReader(inboxDirectory + mailSession.getLoginName() + "/" + mailId + MAIL_FILE_EXT);
			mailReadCallback.readMail(reader);
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.standard.MailServiceImpl#deleteMail(com.yuanluesoft.webmail.model.MailAccount, java.lang.String)
	 */
	public void deleteMail(MailSession mailSession, String mailId) throws ServiceException {
		new File(inboxDirectory + mailSession.getLoginName() + "/" +  mailId + MAIL_FILE_EXT).delete();
        new File(inboxDirectory + mailSession.getLoginName() + "/" +  mailId + MAIL_OBJECT_EXT).delete();
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.standard.MailServiceImpl#createMailAccount(com.yuanluesoft.webmail.model.MailSession, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String createMailAccount(MailSession mailManageSession, String loginName, String mailDomain, String password) throws ServiceException {
		JamesManageSession jamesManageSession = (JamesManageSession)mailManageSession;
		if(sendManageCommand(jamesManageSession, "verify " + loginName).startsWith("User " + loginName + " exists")) { //用户已存在
			//设置用户口令
			if(!sendManageCommand(jamesManageSession, "setpassword " + loginName + " " + password).startsWith("Password for " + loginName + " reset")) {
				throw new ServiceException();
			}
		}
		else { //用户不存在,添加用户
			if(!sendManageCommand(jamesManageSession, "adduser " + loginName + " " + password).startsWith("User " + loginName + " added")) {
				throw new ServiceException();
			}
		}
		return loginName + "@" + mailDomain; 
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.standard.MailServiceImpl#changePassword(com.yuanluesoft.webmail.model.MailSession, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void changePassword(MailSession mailManageSession, String loginName, String mailDomain, String password) throws ServiceException {
		createMailAccount(mailManageSession, loginName, mailDomain, password);
	}

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.mailservice.standard.MailServiceImpl#removeMailAccount(com.yuanluesoft.webmail.model.MailSession, java.lang.String, java.lang.String)
	 */
	public void removeMailAccount(MailSession mailManageSession, String loginName, String mailDomain) throws ServiceException {
		JamesManageSession jamesManageSession = (JamesManageSession)mailManageSession;
		if(!sendManageCommand(jamesManageSession, "deluser " + loginName).startsWith("User " + loginName + " deleted")) {
			throw new ServiceException();
		}
	}

	/**
	 * @return the inboxDirectory
	 */
	public String getInboxDirectory() {
		return inboxDirectory;
	}

	/**
	 * @param inboxDirectory the inboxDirectory to set
	 */
	public void setInboxDirectory(String inboxDirectory) {
		this.inboxDirectory = inboxDirectory;
	}

	
	public static void main(String[] args) throws Exception {
		new MailServiceImpl().login("", "127.0.0.1", 4555, "root", "root", MailService.MAIL_SESSION_TYPE_MANAGE);
	}
}