package com.yuanluesoft.jeaf.mail.service.spring;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.mail.pojo.MailAccount;
import com.yuanluesoft.jeaf.mail.pojo.MailSend;
import com.yuanluesoft.jeaf.mail.service.MailService;
import com.yuanluesoft.jeaf.security.service.CryptService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class MailServiceImpl extends BusinessServiceImpl implements MailService {
	private CryptService cryptService; //加密服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof MailAccount) {
			MailAccount mailAccount = (MailAccount)record;
			mailAccount.setSmtpPassword(cryptService.encrypt(mailAccount.getSmtpPassword(), "" + mailAccount.getId(), true));
		}
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof MailAccount) {
			MailAccount mailAccount = (MailAccount)record;
			mailAccount.setSmtpPassword(cryptService.encrypt(mailAccount.getSmtpPassword(), "" + mailAccount.getId(), true));
		}
		return super.update(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.mail.service.MailService#sendMail(long, java.lang.String, long, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public void sendMail(long senderId, String senderName, long senderUnitId, String receivers, String subject, String body, boolean dispositionNotification) throws ServiceException {
		MailAccount mailAccount = getMailAccountByUnitId(senderUnitId);
		if(mailAccount==null) {
			throw new ServiceException("mail account required");
		}
		//密码解密
		mailAccount.setSmtpPassword(cryptService.decrypt(mailAccount.getSmtpPassword(), "" + mailAccount.getId(), true));
		//发邮件
		sendMail(mailAccount, receivers, subject, body, dispositionNotification);
		//创建邮件发送记录
		MailSend mailSend = new MailSend();
		mailSend.setId(UUIDLongGenerator.generateId()); //ID
		mailSend.setOrgId(senderUnitId); //单位ID
		//private String orgName; //单位名称
		mailSend.setMailAddress(mailAccount.getMailAddress()); //邮箱
		mailSend.setSubject(subject); //主题
		mailSend.setReceivers(receivers); //接收人
		mailSend.setBody(body); //内容
		mailSend.setSent(DateTimeUtils.now()); //发送时间
		mailSend.setSenderId(senderId); //发送人ID
		mailSend.setSender(senderName); //发送人
		getDatabaseService().saveRecord(mailSend);
	}

	/**
	 * 发邮件
	 * @param mailAccount
	 * @param receivers
	 * @param subject
	 * @param body
	 * @param dispositionNotification
	 * @throws ServiceException
	 */
	private void sendMail(MailAccount mailAccount, String receivers, String subject, String body, boolean dispositionNotification) throws ServiceException {
		try {
			Properties props = new Properties();
			/*if(ssl) {
				props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");  
				props.setProperty("mail.pop3.socketFactory.fallback", "false");
			}*/
			props.put("mail.smtp.auth", "true"); //是否需要验证
			props.put("mail.smtp.host", mailAccount.getSmtpHost()); //smtp主机名
			props.put("mail.smtp.port", new Integer(mailAccount.getSmtpPort())); //smtp端口
			props.put("mail.smtp.user", mailAccount.getMailAddress()); //发送方邮件地址
			props.put("mail.smtp.password", mailAccount.getSmtpPassword()); //邮件密码
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.timeout", new Integer(20000));
			PopupAuthenticator popupAuthenticator = new PopupAuthenticator();//邮件安全认证
			popupAuthenticator.performCheck(mailAccount.getSmtpUserName(), mailAccount.getSmtpPassword()); //填写用户名及密码
			
			Session sendMailSession = Session.getInstance(props, popupAuthenticator);
			Message newMessage = new MimeMessage(sendMailSession);
			sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
			newMessage.setFrom(new InternetAddress("=?GB2312?B?" + enc.encode(mailAccount.getName().getBytes()) + "?=" + " <" + mailAccount.getMailAddress() + ">"));
			newMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(receivers)); //接收方邮件地址
			//msg.setRecipients(Message.RecipientType.CC, address); //抄送
			//newMessage.setSubject(subject);
			//设置标题
			newMessage.setSubject("=?GB2312?B?" + enc.encode(subject.getBytes()) + "?=");
			//发送时间
			newMessage.setSentDate(DateTimeUtils.now());
			//邮件正文
			MimeMultipart multipart = new MimeMultipart();
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			//messageBodyPart.setText(body);
			messageBodyPart.setDataHandler(new DataHandler(body, "text/html;charset=GBK"));
			multipart.addBodyPart(messageBodyPart);
			//添加附件
			//MimeBodyPart messageBodyPart = new MimeBodyPart();
			//DataSource source = new FileDataSource(fileName);
			//messageBodyPart.setDataHandler(new DataHandler(source));
			//messageBodyPart.setFileName(name);
			//multipart.addBodyPart(messageBodyPart);
			newMessage.setContent(multipart);
			//要求接收人回执
			if(dispositionNotification) {
				newMessage.setHeader("Disposition-Notification-To", mailAccount.getMailAddress());
			}
			Transport.send(newMessage);
			if(Logger.isTraceEnabled()) {
				Logger.trace("MailService: send mail to " + receivers + " success");
			}
		}
		catch(MessagingException ex) {
			Logger.error("MailService: send mail to " + receivers + " failed");
			throw new ServiceException(ex);
		}
	}
	
	/**
	 * 
	 * @author linchuan
	 *
	 */
	public class PopupAuthenticator extends Authenticator {
		String username=null;
		String password=null;
		
		public PopupAuthenticator() {
			
		}
		
		public PasswordAuthentication performCheck(String user,String pass) {
			username = user;
			password = pass;
			return getPasswordAuthentication();
		}
		
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.mail.service.MailService#getMailAccount(long)
	 */
	public MailAccount getMailAccount(long orgId) throws ServiceException {
		String hql = "from MailAccount MailAccount where MailAccount.orgId=" + orgId;
		return (MailAccount)getDatabaseService().findRecordByHql(hql);
	}
	

	/**
	 * 按单位获取邮件帐号
	 * @param unitId
	 * @return
	 */
	private MailAccount getMailAccountByUnitId(long unitId) {
		String hql = "select MailAccount" +
					 " from MailAccount MailAccount, OrgSubjection OrgSubjection" +
					 " where MailAccount.orgId=OrgSubjection.parentDirectoryId" +
					 " and OrgSubjection.directoryId=" + unitId +
					 " order by OrgSubjection.id";
		return (MailAccount)getDatabaseService().findRecordByHql(hql);
	}

	/**
	 * @return the cryptService
	 */
	public CryptService getCryptService() {
		return cryptService;
	}

	/**
	 * @param cryptService the cryptService to set
	 */
	public void setCryptService(CryptService cryptService) {
		this.cryptService = cryptService;
	}
}
