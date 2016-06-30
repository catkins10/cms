package com.yuanluesoft.jeaf.messagecenter.sender.im;

import com.yuanluesoft.im.service.IMService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.messagecenter.pojo.Message;
import com.yuanluesoft.jeaf.messagecenter.sender.SendException;
import com.yuanluesoft.jeaf.messagecenter.sender.Sender;

/**
 * IM通知
 * @author linchuan
 *
 */
public class IMSender extends Sender {
	private IMService imService; //IM服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.sender.Sender#sendMessage(com.yuanluesoft.jeaf.messagecenter.pojo.Message, int)
	 */
	public boolean sendMessage(Message message, int feedbackDelay) throws SendException {
		if(imService==null) {
			return false;
		}
		try {
			imService.sendSystemMessage(message, message.getReceivePersonId());
			return true;
		}
		catch (ServiceException e) {
			Logger.exception(e);
			throw new SendException();
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.sender.Sender#loadPersonalCustom(long)
	 */
	public Object loadPersonalCustom(long personId) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.sender.Sender#savePersonalCustom(long, java.lang.Object)
	 */
	public void savePersonalCustom(long personId, Object config) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.sender.Sender#stopSender()
	 */
	public void stopSender() throws SendException {
		
	}

	/**
	 * @return the imService
	 */
	public IMService getImService() {
		return imService;
	}

	/**
	 * @param imService the imService to set
	 */
	public void setImService(IMService imService) {
		this.imService = imService;
	}
}