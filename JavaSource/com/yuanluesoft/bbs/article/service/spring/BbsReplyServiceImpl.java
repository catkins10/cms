package com.yuanluesoft.bbs.article.service.spring;

import com.yuanluesoft.bbs.article.pojo.BbsReply;
import com.yuanluesoft.bbs.article.service.BbsReplyService;
import com.yuanluesoft.bbs.usermanage.service.BbsUserService;
import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.htmlparser.model.HTMLBodyInfo;
import com.yuanluesoft.jeaf.htmlparser.util.HTMLBodyUtils;
import com.yuanluesoft.jeaf.util.DateTimeUtils;

/**
 * 
 * @author linchuan
 *
 */
public class BbsReplyServiceImpl extends BusinessServiceImpl implements BbsReplyService {
	private BbsUserService bbsUserService; //BBS用户服务
	private ExchangeClient exchangeClient; //数据交换服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		BbsReply reply = (BbsReply)record;
		try {
			updateAttachmentInfo(reply);
		} 
		catch (Exception e) {
			
		}
		exchangeClient.synchUpdate(reply, null, 2000);
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		BbsReply reply = (BbsReply)record;
		try {
			updateAttachmentInfo(reply);
		} 
		catch (Exception e) {
			
		}
		exchangeClient.synchUpdate(reply, null, 2000);
		return super.update(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(java.lang.Object)
	 */
	public void delete(Record record) throws ServiceException {
		BbsReply reply = (BbsReply)record;
		if(reply.getCreated()!=null) {
			//更新用户的回帖记录数
			bbsUserService.increaseReply(reply.getCreatorId(), -1);
		}
		exchangeClient.synchDelete(reply, null, 2000);
		super.delete(record);
	}

	/**
	 * 更新图片和视频数量
	 * @param resource
	 */
	private void updateAttachmentInfo(BbsReply reply) throws Exception {
		HTMLBodyInfo htmlBodyInfo = HTMLBodyUtils.analysisHTMLBody(reply, reply.getBody(), null);
		if(htmlBodyInfo.isBodyChanged()) {
			reply.setBody(htmlBodyInfo.getNewBody());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.bbs.article.service.BbsReplyService#press(com.yuanluesoft.bbs.article.pojo.BbsReply)
	 */
	public void press(BbsReply reply) throws ServiceException {
		if(reply.getCreated()==null) {
			//设置创建时间,作为已发布的标记
			reply.setCreated(DateTimeUtils.now());
			update(reply);
			//更新用户的回帖记录数
			bbsUserService.increaseReply(reply.getCreatorId(), 1);
		}
	}

	/**
	 * @return the bbsUserService
	 */
	public BbsUserService getBbsUserService() {
		return bbsUserService;
	}

	/**
	 * @param bbsUserService the bbsUserService to set
	 */
	public void setBbsUserService(BbsUserService bbsUserService) {
		this.bbsUserService = bbsUserService;
	}

	/**
	 * @return the exchangeClient
	 */
	public ExchangeClient getExchangeClient() {
		return exchangeClient;
	}

	/**
	 * @param exchangeClient the exchangeClient to set
	 */
	public void setExchangeClient(ExchangeClient exchangeClient) {
		this.exchangeClient = exchangeClient;
	}
}
