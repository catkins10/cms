package com.yuanluesoft.cms.advice.service.spring;

import com.yuanluesoft.cms.advice.pojo.AdviceFeedback;
import com.yuanluesoft.cms.advice.pojo.AdviceTopic;
import com.yuanluesoft.cms.advice.service.AdviceService;
import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public class AdviceServiceImpl extends BusinessServiceImpl implements AdviceService {
	private ExchangeClient exchangeClient; //数据交换服务
	private PageService pageService; //页面服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		super.save(record);
		exchangeClient.synchUpdate(record, null, 2000);
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		super.update(record);
		exchangeClient.synchUpdate(record, null, 2000);
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		exchangeClient.synchDelete(record, null, 2000);
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.advice.service.AdviceService#getAdviceTopic(long)
	 */
	public AdviceTopic getAdviceTopic(long topicId) throws ServiceException {
		return (AdviceTopic)load(AdviceTopic.class, topicId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.advice.service.AdviceService#getAdviceFeedback(long)
	 */
	public AdviceFeedback getAdviceFeedback(long topicId) throws ServiceException {
		String hql = "from AdviceFeedback AdviceFeedback where AdviceFeedback.topicId=" + topicId;
		return (AdviceFeedback)getDatabaseService().findRecordByHql(hql);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.advice.service.AdviceService#hasFeedback(long)
	 */
	public boolean hasFeedback(long topicId) throws ServiceException {
		String hql = "select AdviceFeedback.id from AdviceFeedback AdviceFeedback where AdviceFeedback.topicId=" + topicId;
		return getDatabaseService().findRecordByHql(hql)!=null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.advice.service.AdviceService#hasPublicedAdvice(long)
	 */
	public boolean hasPublicedAdvice(long topicId) throws ServiceException {
		String hql = "select Advice.id from Advice Advice where Advice.topicId=" + topicId + " and Advice.publicPass='1'";
		return getDatabaseService().findRecordByHql(hql)!=null;
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

	/**
	 * @return the pageService
	 */
	public PageService getPageService() {
		return pageService;
	}

	/**
	 * @param pageService the pageService to set
	 */
	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}
}
