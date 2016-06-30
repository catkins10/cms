/*
 * Created on 2005-11-26
 *
 */
package com.yuanluesoft.jeaf.messagecenter.sender.msn;

import java.util.Properties;

import rath.msnm.SwitchboardSession;
import rath.msnm.entity.MsnFriend;
import rath.msnm.ftp.VolatileDownloader;
import rath.msnm.ftp.VolatileTransferServer;
import rath.msnm.msg.MimeMessage;

import com.yuanluesoft.jeaf.logger.Logger;

/**
 * 
 * @author linchuan
 *
 */
public class MsnListener implements rath.msnm.event.MsnListener {
	private MsnSender msnSender;
	
	public MsnListener(MsnSender msnSender) {
		this.msnSender = msnSender;
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#loginComplete(rath.msnm.entity.MsnFriend)
	 */
	public void loginComplete(MsnFriend friend) {
		synchronized(msnSender) { //通知发送器登录完成
			msnSender.notify();
		}
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#loginError(java.lang.String)
	 */
	public void loginError(String error) {
		if(error.equals("911")) {
			Logger.error("用户名或密码错误，MSN登录失败");
		}
		else {
			Logger.error("MSN登录失败");
		}
		synchronized(msnSender) { //通知发送器登录完成
			msnSender.notify();
		}
	}
	
	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#allListUpdated()
	 */
	public void allListUpdated() {
		synchronized(msnSender) { //通知发送器操作完成
			msnSender.notify();
		}
	}
	
	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#whoJoinSession(rath.msnm.SwitchboardSession, rath.msnm.entity.MsnFriend)
	 */
	public void whoJoinSession(SwitchboardSession session, MsnFriend friend) {
		try {
			msnSender.onJoinSession(session, friend);
		}
		catch (Exception e) {
			Logger.exception(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#switchboardSessionEnded(rath.msnm.SwitchboardSession)
	 */
	public void switchboardSessionEnded(SwitchboardSession session) {
		try {
			msnSender.onSessionEnded(session);
		}
		catch (Exception e) {
			Logger.exception(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#instantMessageReceived(rath.msnm.SwitchboardSession, rath.msnm.entity.MsnFriend, rath.msnm.msg.MimeMessage)
	 */
	public void instantMessageReceived(SwitchboardSession session, MsnFriend friend, MimeMessage message) {
		try {
			msnSender.onMessageReceived(session, friend, message);
		}
		catch (Exception e) {
			Logger.exception(e);
		}
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#addFailed(int)
	 */
	public void addFailed(int code) {
		
	}
	
	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#listAdd(rath.msnm.entity.MsnFriend)
	 */
	public void listAdd(MsnFriend friend) {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#listOnline(rath.msnm.entity.MsnFriend)
	 */
	public void listOnline(MsnFriend friend) {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#userOnline(rath.msnm.entity.MsnFriend)
	 */
	public void userOnline(MsnFriend friend) {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#userOffline(java.lang.String)
	 */
	public void userOffline(String arg0) {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#switchboardSessionStarted(rath.msnm.SwitchboardSession)
	 */
	public void switchboardSessionStarted(SwitchboardSession session) {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#whoPartSession(rath.msnm.SwitchboardSession, rath.msnm.entity.MsnFriend)
	 */
	public void whoPartSession(SwitchboardSession session, MsnFriend friend) {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#switchboardSessionAbandon(rath.msnm.SwitchboardSession, java.lang.String)
	 */
	public void switchboardSessionAbandon(SwitchboardSession session, String arg1) {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#progressTyping(rath.msnm.SwitchboardSession, rath.msnm.entity.MsnFriend, java.lang.String)
	 */
	public void progressTyping(SwitchboardSession session, MsnFriend friend, String arg2) {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#filePosted(rath.msnm.SwitchboardSession, int, java.lang.String, int)
	 */
	public void filePosted(SwitchboardSession session, int arg1, String arg2, int arg3) {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#fileSendAccepted(rath.msnm.SwitchboardSession, int)
	 */
	public void fileSendAccepted(SwitchboardSession session, int arg1) {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#fileSendRejected(rath.msnm.SwitchboardSession, int, java.lang.String)
	 */
	public void fileSendRejected(SwitchboardSession session, int arg1, String arg2) {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#fileSendStarted(rath.msnm.ftp.VolatileTransferServer)
	 */
	public void fileSendStarted(VolatileTransferServer server) {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#fileSendEnded(rath.msnm.ftp.VolatileTransferServer)
	 */
	public void fileSendEnded(VolatileTransferServer server) {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#fileReceiveStarted(rath.msnm.ftp.VolatileDownloader)
	 */
	public void fileReceiveStarted(VolatileDownloader downloader) {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#fileSendError(rath.msnm.ftp.VolatileTransferServer, java.lang.Throwable)
	 */
	public void fileSendError(VolatileTransferServer server, Throwable error) {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#fileReceiveError(rath.msnm.ftp.VolatileDownloader, java.lang.Throwable)
	 */
	public void fileReceiveError(VolatileDownloader downloader, Throwable error) {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#whoAddedMe(rath.msnm.entity.MsnFriend)
	 */
	public void whoAddedMe(MsnFriend friend) {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#whoRemovedMe(rath.msnm.entity.MsnFriend)
	 */
	public void whoRemovedMe(MsnFriend friend) {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#buddyListModified()
	 */
	public void buddyListModified() {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#renameNotify(rath.msnm.entity.MsnFriend)
	 */
	public void renameNotify(MsnFriend friend) {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#logoutNotify()
	 */
	public void logoutNotify() {
		
	}

	/* (non-Javadoc)
	 * @see rath.msnm.event.MsnListener#notifyUnreadMail(java.util.Properties, int)
	 */
	public void notifyUnreadMail(Properties properties, int arg1) {
		
	}
}
