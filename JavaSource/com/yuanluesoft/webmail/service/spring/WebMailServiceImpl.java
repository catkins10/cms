/*
 * Created on 2006-5-12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.webmail.service.spring;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.jeaf.attachmentmanage.service.AttachmentService;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.image.service.ImageService;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.webmail.model.MailInfo;
import com.yuanluesoft.webmail.model.MailServer;
import com.yuanluesoft.webmail.model.MailSession;
import com.yuanluesoft.webmail.pojo.Mail;
import com.yuanluesoft.webmail.pojo.MailAttachment;
import com.yuanluesoft.webmail.pojo.MailBody;
import com.yuanluesoft.webmail.pojo.POP3Server;
import com.yuanluesoft.webmail.service.MailFilterService;
import com.yuanluesoft.webmail.service.MailboxService;
import com.yuanluesoft.webmail.service.PersonalPOP3Service;
import com.yuanluesoft.webmail.service.WebMailService;
import com.yuanluesoft.webmail.service.mailservice.MailReadCallback;
import com.yuanluesoft.webmail.service.mailservice.MailService;
import com.yuanluesoft.webmail.service.mailservice.MailWriteCallback;
import com.yuanluesoft.webmail.util.MailReader;
import com.yuanluesoft.webmail.util.MailWriter;

/**
 * TODO:发送邮件时,邮件正文中对附件的应用要转换成附件的Content-ID
 * @author linchuan
 * 
 */
public class WebMailServiceImpl extends BusinessServiceImpl implements WebMailService {
    private MailService standardMailService; //POP3、SMTP邮件服务
    private MailServer innerMailServer; //系统内部的邮件器
    
    private PersonalPOP3Service personalPOP3Service; //个人定义的pop3代收服务
    private AttachmentService attachmentService; //WEB邮件附件管理服务
    private ImageService imageService; //图片服务
    private MailFilterService mailFilterService; //邮件过滤服务
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.WebMailService#synchMailList(boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void synchMailList(final char readLevel, final SessionInfo sessionInfo) throws ServiceException {
		//从内置的邮件服务器同步邮件
		synchMailList(innerMailServer.getMailService(), sessionInfo.getLoginName() + "@" + innerMailServer.getMailDomain(), innerMailServer.getServerHost(), innerMailServer.getReceivePort(), sessionInfo.getLoginName(), sessionInfo.getPassword(), readLevel, sessionInfo);
		//从用户自定义的邮件服务器同步邮件
		List pop3Servers = personalPOP3Service.listPersonalPOP3Services(sessionInfo.getUserId());
		if(pop3Servers!=null && !pop3Servers.isEmpty()) {
			for(Iterator iterator = pop3Servers.iterator(); iterator.hasNext();) {
				POP3Server pop3Server = (POP3Server)iterator.next();
				synchMailList(standardMailService, pop3Server.getMailAddress(), pop3Server.getServerAddress(), pop3Server.getServerPort(), pop3Server.getLoginName(), pop3Server.getPassword(), readLevel, sessionInfo);
			}
		}
    }
	
	/**
	 * 从指定服务器同步邮件列表
	 * @param mailAddress
	 * @param mailServer
	 * @param receivePort
	 * @param mailUserName
	 * @param password
	 * @param readLevel
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	public void synchMailList(MailService mailService, String mailAddress, final String mailServer, int receivePort, String mailUserName, String password, final char readLevel, final SessionInfo sessionInfo) throws ServiceException {
		MailSession mailSession = mailService.login(mailAddress, mailServer, receivePort, mailUserName, password, MailService.MAIL_SESSION_TYPE_RECEIVE);
		try {
			List mailInfoList = mailService.listMails(mailSession);
			if(mailInfoList==null || mailInfoList.isEmpty()) { //删除全部从该服务器接收的邮件
				String hql = "from Mail Mail" +
				 			 " where Mail.userId=" + sessionInfo.getUserId() +
				 			 " and Mail.mailServer='" + JdbcUtils.resetQuot(mailServer) + "'" +
				 			 " and Mail.mailboxId>3"; //注:发件箱 mailboxId=1, 草稿箱 mailboxId=2, 临时(发邮件) mailboxId=3
				getDatabaseService().deleteRecordsByHql(hql);
				return;
			}
			String mailIds = "'" + ListUtils.join(mailInfoList, "mailId", "','", false) + "'";
			
			//删除不在邮件服务器上的邮件数据库记录
			String hql = "from Mail Mail" +
						 " where Mail.userId=" + sessionInfo.getUserId() +
						 " and Mail.mailServer='" + JdbcUtils.resetQuot(mailServer) + "'" +
						 " and Mail.mailboxId>3" +
						 " and not Mail.mailIdOnServer in (" + mailIds + ")";
			getDatabaseService().deleteRecordsByHql(hql);
	
			//获取已经同步到数据库的邮件列表
			hql = "select Mail.mailIdOnServer" +
			 	  " from Mail Mail" +
			 	  " where Mail.userId=" + sessionInfo.getUserId() +
			 	  " and Mail.mailServer='" + JdbcUtils.resetQuot(mailServer) + "'" +
			 	  " and Mail.mailboxId>3" +
			 	  " and Mail.readLevel>='" + readLevel + "'" +
			 	  " and Mail.mailIdOnServer in (" + mailIds + ")";
			List existsMailIds = getDatabaseService().findRecordsByHql(hql);
	        
			//更新新邮件
	        final List newMails = new ArrayList();
	        for(Iterator iterator = mailInfoList.iterator(); iterator.hasNext();) {
	            final MailInfo mailInfo = (MailInfo)iterator.next();
	            if(existsMailIds!=null && existsMailIds.indexOf(mailInfo.getMailId())!=-1) { //邮件已经同步过
	            	continue;
	            }
	            try {
	            	MailReadCallback mailReadCallback = new MailReadCallback() {
	            		public void readMail(Reader reader) throws ServiceException {
	            			com.yuanluesoft.webmail.model.Mail mailModel = new MailReader().readMail(reader, readLevel);
	            			String hql = "from Mail Mail" +
					   	 	  			 " where Mail.userId=" + sessionInfo.getUserId() +
					   	 	  			 " and Mail.mailServer='" + JdbcUtils.resetQuot(mailServer) + "'" +
					   	 	  			 " and Mail.mailIdOnServer='" + mailInfo.getMailId() + "'";
					       	Mail mail = (Mail)getDatabaseService().findRecordByHql(hql, ListUtils.generateListFromArray(new String[]{"mailBodies", "mailAttachments"}));
							mail = saveMailFromMailServer(mailModel, mail, sessionInfo.getUserId(), mailServer, mailInfo.getMailId(), mailInfo.getMailSize(), readLevel);
	    	                newMails.add(mail);
						}
	            	};
	            	mailService.readMail(mailSession, mailInfo.getMailId(), mailReadCallback);
	            }
	            catch (Exception e) {
	                Logger.exception(e);
	            }
	        }
	        mailFilterService.doFilter(newMails, sessionInfo); //过滤邮件
		}
		finally {
			mailService.logout(mailSession);
		}
    }
	
    /**
     * 把邮件服务器上的邮件保存到数据库
     * @param mail
     * @param pojoMail
     * @param userId
     * @param mailId
     * @param mailSize
     * @param readLevel
     */
    private Mail saveMailFromMailServer(com.yuanluesoft.webmail.model.Mail mailModel, Mail mail, long userId, String mailServer, String mailIdOnServer, long mailSize, char readLevel) {
        boolean isNew = (mail==null);
    	if(isNew) {
    		mail = new Mail();
        	try {
                PropertyUtils.copyProperties(mail, mailModel.getMailHeader()); //拷贝邮件头
            }
            catch (Exception e) {
            	
            }
            mail.setId(UUIDLongGenerator.generateId());
            mail.setMailboxId(MailboxService.MAILBOX_INBOX_ID); //设置收件箱ID
            mail.setNewMail(1); //设置为新邮件
            mail.setUserId(userId);
            mail.setSize(mailSize); //邮件大小
            mail.setHasAttachment("multipart/mixed".equals(mailModel.getMailHeader().getContentType()) ? '1' : '0'); //是否有附件
            mail.setMailServer(mailServer);
            mail.setMailIdOnServer(mailIdOnServer); //邮件在邮件服务器中的ID
    	}
        
	    //保存附件
    	mail.setMailAttachments(new HashSet());
        if(mailModel.getMailAttachments()!=null) {
           for(Iterator iteratorAttachment = mailModel.getMailAttachments().iterator(); iteratorAttachment.hasNext();) {
                com.yuanluesoft.webmail.model.MailAttachment mailAttachment = (com.yuanluesoft.webmail.model.MailAttachment)iteratorAttachment.next();
                MailAttachment pojoMailAttachment = new MailAttachment();
                try {
                	PropertyUtils.copyProperties(pojoMailAttachment, mailAttachment);
                }
                catch (Exception e) {

                }
                pojoMailAttachment.setId(UUIDLongGenerator.generateId());
                pojoMailAttachment.setMailId(mail.getId());
                getDatabaseService().saveRecord(pojoMailAttachment);
                mail.getMailAttachments().add(pojoMailAttachment);
            }
        }
	    
        //保存正文,只保存html格式,如果没有,保存最后一个正文
        if(mailModel.getMailBodies()!=null) {
        	if(!isNew) { //删除原来的正文
        		getDatabaseService().deleteRecordsByHql("from MailBody MailBody where MailBody.mailId=" + mail.getId());
        	}
        	com.yuanluesoft.webmail.model.MailBody mailBodyModel = null;
            for(Iterator iteratorBody = mailModel.getMailBodies().iterator(); iteratorBody.hasNext();) {
            	com.yuanluesoft.webmail.model.MailBody mailBody = (com.yuanluesoft.webmail.model.MailBody)iteratorBody.next();
                if(mailBody.getBody()==null || mailBody.getBody().equals("")) {
                	continue;
                }
                mailBodyModel = mailBody;
                if("text/html".equals(mailBody.getContentType())) {
                	break;
                }
            }
            if(mailBodyModel!=null) {
            	MailBody mailBody = new MailBody();
            	mailBody.setId(UUIDLongGenerator.generateId());
            	mailBody.setMailId(mail.getId());
            	String body = mailBodyModel.getBody();
            	//重置HTML格式正文中的图片src、backgroung属性
            	if(mail.getMailAttachments()!=null) {
            		for(Iterator iteratorAttachment = mail.getMailAttachments().iterator(); iteratorAttachment.hasNext();) {
            			MailAttachment mailAttachment = (MailAttachment)iteratorAttachment.next();
            			if(mailAttachment.getContentId()==null) { //不是正文附件
            				continue;
            			}
            			//重设附件URL
            			String url = "cid:" + mailAttachment.getContentId();
            			String newUrl = Environment.getContextPath() + "/webmail/downloadAttachment.shtml?id=" + mailAttachment.getId();
            			body = StringUtils.replace(body, url, newUrl);
            		}
            	}
            	mailBody.setBody(body);
            	getDatabaseService().saveRecord(mailBody);
            	mail.setMailBodies(new HashSet());
            	mail.getMailBodies().add(mailBody);
            }
        }
        
        mail.setReadLevel(readLevel); //重设读取级别
        if(isNew) {
        	getDatabaseService().saveRecord(mail);
        }
        else {
        	getDatabaseService().updateRecord(mail);
        }
        return mail;
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.WebMailService#receiveMail(long, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Mail receiveMail(long mailId, boolean setReadFlag, final SessionInfo sessionInfo) throws ServiceException {
	    final Mail mail = (Mail)getDatabaseService().findRecordById(Mail.class.getName(), mailId, ListUtils.generateListFromArray(new String[]{"mailBodies", "mailAttachments"}));
        if(mail==null || mail.getMailboxId()<=MailboxService.MAILBOX_DRAFT_ID) {
            return mail;
        }
        if(mail.getReadLevel()==MailReader.READ_LEVEL_FULL) { //已经全部被读取
        	return mail;
        }
        //重新读取邮件
        MailServerLogin mailServerLogin = loginMailServer(sessionInfo, mail.getMailServer(), MailService.MAIL_SESSION_TYPE_RECEIVE);
        try {
        	MailReadCallback mailReadCallback = new MailReadCallback() {
        		public void readMail(Reader reader) throws ServiceException {
        			com.yuanluesoft.webmail.model.Mail mailModel = new MailReader().readMail(reader, MailReader.READ_LEVEL_FULL);
        			saveMailFromMailServer(mailModel, mail, sessionInfo.getUserId(), mail.getMailServer(), mail.getMailIdOnServer(), mail.getSize(), MailReader.READ_LEVEL_FULL);
        			List newMails = new ArrayList();
        			newMails.add(mail);
        			mailFilterService.doFilter(newMails, sessionInfo); //过滤邮件
        		}
        	};
        	innerMailServer.getMailService().readMail(mailServerLogin.getMailSession(), mail.getMailIdOnServer(), mailReadCallback);
        }
        catch(Exception e) {
        	Logger.exception(e);
        	throw new ServiceException();
        }
        finally {
        	logoutMailServer(mailServerLogin);
        }
        
        if(setReadFlag && mail.getNewMail()==1) {
        	mail.setNewMail(0);
            getDatabaseService().updateRecord(mail);
        }
        return mail;
    }
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.WebMailService#sendMail(com.yuanluesoft.webmail.pojo.Mail, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void sendMail(Mail mail, SessionInfo sessionInfo) throws ServiceException {
        //登录邮件发送服务器
		if(mail.getMailFrom()==null) {
			mail.setMailFrom("\"" + sessionInfo.getUserName() + "\" <" + sessionInfo.getLoginName() + "@" + innerMailServer.getMailDomain() + ">");
		}
		MailSession mailSession = innerMailServer.getMailService().login(sessionInfo.getLoginName() + "@" + innerMailServer.getMailDomain(), innerMailServer.getServerHost(), innerMailServer.getSendPort(), sessionInfo.getLoginName(), sessionInfo.getPassword(), MailService.MAIL_SESSION_TYPE_SEND);
        try {
            //发送邮件
        	MailWriteCallback mailWriteCallback = new MailWriteCallback() {
        		public void writeMail(Writer writer, Mail mail) throws ServiceException {
        			List images = imageService.list("webmail", "images", mail.getId(), false, 0, null);
        			List flashs = attachmentService.list("webmail", "flashs", mail.getId(), false, 0, null); 
        			List attachments = attachmentService.list("webmail", "attachments", mail.getId(), false, 0, null);
        			new MailWriter().writeMail(writer, mail, images, flashs, attachments);
				}
        	};
        	innerMailServer.getMailService().writeMail(mailSession, mail, mailWriteCallback);
        }
        catch(Exception e) {
            Logger.exception(e);
            throw new ServiceException();
        }
        finally {
        	innerMailServer.getMailService().logout(mailSession); //注销
        }
    }
	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.WebMailService#createMail(java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Mail createMail(String mailTo, SessionInfo sessionInfo) throws ServiceException {
        Mail mail = new Mail();
        mail.setId(UUIDLongGenerator.generateId());
        mail.setMailFrom("\"" + sessionInfo.getUserName() + "\" <" + sessionInfo.getLoginName() + "@" + innerMailServer.getMailDomain() + ">"); //设置发件人
        mail.setMailIdOnServer("Mail" + UUIDLongGenerator.generateId());
        mail.setMailTo(mailTo); //收件人
        mail.setPriority("2");
        mail.setMailboxId(MailboxService.MAILBOX_TMP_ID); //新邮件直接放到临时文件夹
        return mail;
    }

    /* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.WebMailService#forwardMail(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Mail forwardMail(long mailId, SessionInfo sessionInfo) throws ServiceException {
		Mail mail = receiveMail(mailId, false, sessionInfo);
        //复制邮件信息
        Mail newMail = new Mail();
        try {
            PropertyUtils.copyProperties(newMail, mail);
        }
        catch (Exception e) {
            Logger.exception(e);
            throw new ServiceException();
        }
        newMail.setId(UUIDLongGenerator.generateId());
        newMail.setMailFrom("\"" + sessionInfo.getUserName() + "\" <" + sessionInfo.getLoginName() + "@" + innerMailServer.getMailDomain() + ">"); //设置发件人
        newMail.setMailboxId(MailboxService.MAILBOX_TMP_ID); //放到临时文件夹
        newMail.setLastModified(DateTimeUtils.now());
        newMail.setMailServer(null);
        newMail.setMailIdOnServer(null);
        newMail.setReceiveDate(null);
        
        newMail.setMailBodies(new HashSet());
        newMail.setMailAttachments(null);
        
        //复制正文
        if(mail.getMailBodies()!=null && !mail.getMailBodies().isEmpty()) {
        	MailBody currentMailBody = (MailBody)mail.getMailBodies().iterator().next(); //获取原来的正文
        	MailBody mailBody = new MailBody();
        	try {
        		PropertyUtils.copyProperties(mailBody, currentMailBody);
        	} 
        	catch (Exception e) {
        		throw new ServiceException(e.getMessage());
        	}
        	//更新附件URL
        	if(mail.getMailAttachments()!=null) {
        		String body = mailBody.getBody();
        		for(Iterator iteratorAttachment = mail.getMailAttachments().iterator(); iteratorAttachment.hasNext();) {
        			MailAttachment mailAttachment = (MailAttachment)iteratorAttachment.next();
        			if(mailAttachment.getContentId()==null) { //不是正文附件
        				continue;
        			}
        			//重设附件URL
        			String url = Environment.getContextPath() + "/webmail/downloadAttachment.shtml?id=" + mailAttachment.getId();
        			String newUrl = attachmentService.createDownload("webmail", "images", newMail.getId(), mailAttachment.getName(), false, null);
        			body = StringUtils.replace(body, url, newUrl);
        		}
        		mailBody.setBody(body);
        	}
        	mailBody.setId(UUIDLongGenerator.generateId());
        	mailBody.setMailId(newMail.getId());
        	getDatabaseService().saveRecord(mailBody);
        	newMail.getMailBodies().add(mailBody);
        }
        //复制附件
        if(mail.getMailAttachments()!=null) {
            for(Iterator iterator = mail.getMailAttachments().iterator(); iterator.hasNext();) {
                MailAttachment mailAttachment = (MailAttachment)iterator.next();
                //复制附件文件
                String attachmentPath;
                if(mailAttachment.getContentId()!=null) { //正文中的附件,保存到图片目录
                	attachmentPath = attachmentService.getSavePath("webmail", (mailAttachment.getName().endsWith(".swf") ? "flashs" : "images"), newMail.getId(), true);
                }
                else {
                	attachmentPath = attachmentService.getSavePath("webmail", "attachments", newMail.getId(), true);
                }
                FileOutputStream out;
                try {
                    out = new FileOutputStream(attachmentPath + mailAttachment.getName());
                }
                catch (FileNotFoundException e) {
                    Logger.exception(e);
                    throw new ServiceException();
                }
                outputAttachment(out, mail, mailAttachment, sessionInfo);
            }
        }
        newMail.setMailTo(null);
        newMail.setMailCc(null);
        newMail.setMailBcc(null);
        newMail.setSubject("Fw: " + (mail.getSubject()==null ? "" : mail.getSubject()));
        getDatabaseService().saveRecord(newMail);
        return newMail;
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.WebMailService#replyMail(java.lang.String, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public Mail replyMail(String mailIds, SessionInfo sessionInfo) throws ServiceException {
	    String[] ids = mailIds.split(","); 
        String mailTo = null;
        Mail mail = null;
		for(int i=ids.length-1; i>=0; i--) {
			mail = receiveMail(Long.parseLong(ids[i]), false, sessionInfo);
		    if(mailTo==null || mailTo.indexOf(mail.getMailFrom())==-1) { 
		        mailTo = mail.getMailFrom() + (mailTo==null ? "" : "; " + mailTo);
		    }
	    }
		Mail replyMail = createMail(mailTo, sessionInfo);
		replyMail.setSubject("Re: " + (mail.getSubject()==null ? "" : mail.getSubject()));
		if(ids.length==1) {
			String body = "";
			if(mail.getMailBodies()!=null && !mail.getMailBodies().isEmpty()) {
				body = ((MailBody)mail.getMailBodies().iterator().next()).getBody();
				int index = body.indexOf("<body");
				if(index!=-1) {
					int beginIndex = body.indexOf('>', index + 5);
					if(beginIndex!=-1) {
						index = body.indexOf("</body>", beginIndex + 1);
						if(index!=-1) {
							body = body.substring(beginIndex + 1, index);
						}
					}
				}
				else if((index=body.indexOf("<BODY"))!=-1) {
					int beginIndex = body.indexOf('>', index + 5);
					if(beginIndex!=-1) {
						index = body.indexOf("</BODY>", beginIndex + 1);
						if(index!=-1) {
							body = body.substring(beginIndex + 1, index);
						}
					}
				}
			}
			body = "<br><br>------------------ 原始邮件 ------------------<br>" +
				   "发&nbsp件&nbsp人:&nbsp;" + mail.getMailFrom().replaceAll("\\x22", "&quot;").replaceAll("\\x3c", "&lt;").replaceAll("\\x3e", "&gt;") + "<br>" +
				   "发送时间:&nbsp;" + DateTimeUtils.formatTimestamp(mail.getReceiveDate(), null) + "<br>" +
				   "收&nbsp件&nbsp人:&nbsp;" + mail.getMailTo().replaceAll("\\x22", "&quot;").replaceAll("\\x3c", "&lt;").replaceAll("\\x3e", "&gt;") + "<br>" +
				   "主　　题:&nbsp;" + (mail.getSubject()==null ? "无主题" : mail.getSubject().replaceAll("\\x22", "&quot;").replaceAll("\\x3c", "&lt;").replaceAll("\\x3e", "&gt;")) + "<br><br>" +
				   body;
			
			MailBody mailBody = new MailBody();
			mailBody.setBody(body);
			replyMail.setMailBodies(new HashSet());
			replyMail.getMailBodies().add(mailBody);
		}
		return replyMail;
    }
	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.WebMailService#saveMail(com.yuanluesoft.webmail.pojo.Mail, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void saveMail(Mail mail, SessionInfo sessionInfo, boolean isNew) throws ServiceException {
	    mail.setUserId(sessionInfo.getUserId()); //设置邮箱用户ID
	    mail.setNewMail(0);
	    List attachments = attachmentService.list("webmail", "attachments", mail.getId(), false, 0, null);
	    mail.setHasAttachment(attachments==null || attachments.isEmpty() ? '0' : '1'); //是否有附件
        mail.setLastModified(DateTimeUtils.now()); //最后修改时间
        if(isNew) { //新邮件
            getDatabaseService().saveRecord(mail);
        }
        else {
            getDatabaseService().updateRecord(mail);
            //删除原正文
            getDatabaseService().deleteRecordsByHql("from MailBody MailBody where MailBody.mailId=" + mail.getId());
        }
        //保存正文
        if(mail.getMailBodies()!=null && !mail.getMailBodies().isEmpty()) {
        	MailBody mailBody = (MailBody)mail.getMailBodies().iterator().next();
        	mailBody.setId(UUIDLongGenerator.generateId());
        	mailBody.setMailId(mail.getId());
        	getDatabaseService().saveRecord(mailBody);
        }
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.WebMailService#deleteMail(long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void deleteMail(long mailId, SessionInfo sessionInfo) throws ServiceException {
        Mail mail = (Mail)getDatabaseService().findRecordByHql("from Mail Mail where Mail.id=" + mailId + " and Mail.userId=" + sessionInfo.getUserId());
        if(mail==null) {
            return;
        }
        if(mail.getMailboxId() >= MailboxService.MAILBOX_INBOX_ID) { //收邮件,删除邮件服务器上的邮件
        	MailServerLogin mailServerLogin = loginMailServer(sessionInfo, mail.getMailServer(), MailService.MAIL_SESSION_TYPE_RECEIVE);
        	try {
            	innerMailServer.getMailService().deleteMail(mailServerLogin.getMailSession(), mail.getMailIdOnServer());
            }
            catch(Exception e) {
                Logger.exception(e);
                throw new ServiceException();
            }
            finally {
            	logoutMailServer(mailServerLogin);
            }
        }
        //删除数据库记录
        delete(mail);
    }
	
    /* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.WebMailService#moveMail(long, long, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void moveMail(long mailId, long mailboxId, SessionInfo sessionInfo) throws ServiceException {
	    if(mailboxId>20 && getDatabaseService().findRecordByHql("from Mailbox Mailbox where Mailbox.userId=" + sessionInfo.getUserId() + " and Mailbox.id=" + mailboxId)==null) {
            return;
        }
        Mail mail = (Mail)getDatabaseService().findRecordByHql("from Mail Mail where Mail.id=" + mailId + " and Mail.userId=" + sessionInfo.getUserId());
        mail.setMailboxId(mailboxId); //设为新邮箱
        getDatabaseService().updateRecord(mail);
    }
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.WebMailService#downloadAttachmentOnMailServer(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, long, boolean, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void downloadAttachmentOnMailServer(HttpServletRequest resquest, HttpServletResponse response, long mailAttachmentId, boolean attachment, SessionInfo sessionInfo) throws ServiceException {
		MailAttachment mailAttachment = (MailAttachment)getDatabaseService().findRecordById(MailAttachment.class.getName(), mailAttachmentId);
		Mail mail = (Mail)getDatabaseService().findRecordById(Mail.class.getName(), mailAttachment.getMailId());
		ServletOutputStream out = null;
		try {
			//输出附件头
			response.setContentType(mailAttachment.getContentType());
			response.addHeader("Content-Disposition", (attachment ? "attachment" : "inline") + "; filename=" + FileUtils.encodeFileName(mailAttachment.getName(), "utf-8"));
			response.addHeader("Accept-Ranges", "bytes");
			response.setContentLength((int)mailAttachment.getSize());
			//response.addHeader("Pragma", "public"); // for SSL
			//输出附件内容
			out = response.getOutputStream();
			outputAttachment(out, mail, mailAttachment, sessionInfo);
		}
		catch (Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		finally {
			try {
				out.close();
			}
			catch (IOException e) {

			}
		}
    }
    
    /**
     * 输出附件到输出流
     * @param mailSession
     * @param out
     * @param mailAttachment
     * @throws ServiceException
     */
    private void outputAttachment(final OutputStream out, Mail mail, final MailAttachment mailAttachment, SessionInfo sessionInfo) throws ServiceException {
        //断点续传
    	MailServerLogin mailServerLogin = loginMailServer(sessionInfo, mail.getMailServer(), MailService.MAIL_SESSION_TYPE_RECEIVE);
    	try {
        	MailReadCallback mailReadCallback = new MailReadCallback() {
        		public void readMail(Reader reader) throws ServiceException {
        			new MailReader().readAttachment(reader, out, mailAttachment.getBeginIndex(), mailAttachment.getEndIndex(), mailAttachment.getContentTransferEncoding());
				}
        	};
        	innerMailServer.getMailService().readMail(mailServerLogin.getMailSession(), mail.getMailIdOnServer(), mailReadCallback);
        }
        catch(Exception e) {
            Logger.exception(e);
            throw new ServiceException();
        }
        finally {
        	logoutMailServer(mailServerLogin);
        }
    }
    
	/* (non-Javadoc)
	 * @see com.yuanluesoft.webmail.service.MailService#cleanTemporaryMail()
	 */
	public void cleanTemporaryMail() throws ServiceException {
		Logger.info("****************** clean temporary mail ******************");
		//获取修改时间在3小时前的临时邮件
		String hql = "from Mail Mail" +
					 " where Mail.mailboxId=" + MailboxService.MAILBOX_TMP_ID +
					 " and Mail.lastModified<TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.add(DateTimeUtils.now(), Calendar.HOUR_OF_DAY, -3), null) + ")"; 
		for(int i=0; ; i+=100) {
			List mails = getDatabaseService().findRecordsByHql(hql, i, 100);
			if(mails==null || mails.isEmpty()) {
				return;
			}
			for(Iterator iterator = mails.iterator(); iterator.hasNext();) {
				Mail mail = (Mail)iterator.next();
				delete(mail);
			}
		}
	}
	
	/**
	 * 登录邮件服务器
	 * @param sessionInfo
	 * @param serverName
	 * @param receivalMail
	 * @return
	 * @throws ServiceException
	 */
	private MailServerLogin loginMailServer(SessionInfo sessionInfo, String serverName, int sessionType) throws ServiceException {
		MailSession mailSession;
		MailService mailService;
		if(serverName.equals(innerMailServer.getServerHost())) { //内置邮件服务器
			mailService = innerMailServer.getMailService();
			mailSession = mailService.login(sessionInfo.getLoginName() + "@" + innerMailServer.getMailDomain(), innerMailServer.getServerHost(), innerMailServer.getReceivePort(), sessionInfo.getLoginName(), sessionInfo.getPassword(), sessionType);
		}
		else {
			POP3Server pop3Server = personalPOP3Service.getPOP3Server(sessionInfo.getUserId(), serverName);
			if(pop3Server==null) {
				return null;
			}
			mailService = standardMailService;
			mailSession = mailService.login(pop3Server.getMailAddress(), pop3Server.getServerAddress(), pop3Server.getServerPort(), pop3Server.getLoginName(), pop3Server.getPassword(), sessionType);
		}
		MailServerLogin mailServerLogin = new MailServerLogin();
		mailServerLogin.setMailService(mailService);
		mailServerLogin.setMailSession(mailSession);
		return mailServerLogin;
	}
	
	/**
	 * 从邮件服务器注销
	 * @param mailServerLogin
	 * @throws ServiceException
	 */
	private void logoutMailServer(MailServerLogin mailServerLogin)  throws ServiceException {
		mailServerLogin.getMailService().logout(mailServerLogin.getMailSession());
	}
	
	/**
	 * 登录邮件服务器
	 * @author linchuan
	 *
	 */
	private class MailServerLogin {
		private MailSession mailSession;
		private MailService mailService;
		
		/**
		 * @return the mailService
		 */
		public MailService getMailService() {
			return mailService;
		}
		/**
		 * @param mailService the mailService to set
		 */
		public void setMailService(MailService mailService) {
			this.mailService = mailService;
		}
		/**
		 * @return the mailSession
		 */
		public MailSession getMailSession() {
			return mailSession;
		}
		/**
		 * @param mailSession the mailSession to set
		 */
		public void setMailSession(MailSession mailSession) {
			this.mailSession = mailSession;
		}
	}
    
    /**
     * @return Returns the attachmentService.
     */
    public AttachmentService getAttachmentService() {
        return attachmentService;
    }
    /**
     * @param attachmentService The attachmentService to set.
     */
    public void setAttachmentService(
            AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }
    /**
     * @return Returns the mailFilterService.
     */
    public MailFilterService getMailFilterService() {
        return mailFilterService;
    }
    /**
     * @param mailFilterService The mailFilterService to set.
     */
    public void setMailFilterService(MailFilterService mailFilterService) {
        this.mailFilterService = mailFilterService;
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
	 * @return the standardMailService
	 */
	public MailService getStandardMailService() {
		return standardMailService;
	}

	/**
	 * @param standardMailService the standardMailService to set
	 */
	public void setStandardMailService(MailService standardMailService) {
		this.standardMailService = standardMailService;
	}

	/**
	 * @return the personalPOP3Service
	 */
	public PersonalPOP3Service getPersonalPOP3Service() {
		return personalPOP3Service;
	}

	/**
	 * @param personalPOP3Service the personalPOP3Service to set
	 */
	public void setPersonalPOP3Service(PersonalPOP3Service personalPOP3Service) {
		this.personalPOP3Service = personalPOP3Service;
	}

	/**
	 * @return the innerMailServer
	 */
	public MailServer getInnerMailServer() {
		return innerMailServer;
	}

	/**
	 * @param innerMailServer the innerMailServer to set
	 */
	public void setInnerMailServer(MailServer innerMailServer) {
		this.innerMailServer = innerMailServer;
	}
}