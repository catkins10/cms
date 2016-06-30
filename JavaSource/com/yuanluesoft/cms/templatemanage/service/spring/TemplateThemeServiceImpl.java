package com.yuanluesoft.cms.templatemanage.service.spring;

import com.yuanluesoft.cms.templatemanage.pojo.TemplateTheme;
import com.yuanluesoft.cms.templatemanage.service.TemplateService;
import com.yuanluesoft.cms.templatemanage.service.TemplateThemeService;
import com.yuanluesoft.exchange.client.ExchangeClient;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;

/**
 * 
 * @author linchuan
 *
 */
public class TemplateThemeServiceImpl extends BusinessServiceImpl implements TemplateThemeService {
	private ExchangeClient exchangeClient; //数据交换服务
	private TemplateService templateService; //模板服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if(record instanceof TemplateTheme) {
			TemplateTheme theme = (TemplateTheme)record;
			if(theme.getType()==TemplateThemeService.THEME_TYPE_COMPUTER || //电脑
			   theme.getType()==TemplateThemeService.THEME_TYPE_WAP || //WAP
			   theme.getType()==TemplateThemeService.THEME_TYPE_WECHAT) { //微信
				theme.setPageWidth(0);
				theme.setFlashSupport(1);
			}
		}
		super.save(record);
		exchangeClient.synchUpdate(record, null, 2000); //同步更新
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		super.update(record);
		exchangeClient.synchUpdate(record, null, 2000); //同步更新
		templateService.clearCachedTemplate(); //清空模板缓存
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		super.delete(record);
		exchangeClient.synchDelete(record, null, 2000); //同步删除
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateThemeService#isThemeUsed(long)
	 */
	public boolean isThemeUsed(long themeId) throws ServiceException {
		//检查是否有模板使用了当前主题
		return getDatabaseService().findRecordByHql("from Template Template where Template.themeId=" + themeId)!=null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.templatemanage.service.TemplateThemeService#isTypeOf(long, int)
	 */
	public boolean isTypeOf(long themeId, int themeType) throws ServiceException {
		String hql = "select TemplateTheme.id" +
					 " from TemplateTheme TemplateTheme" +
					 " where TemplateTheme.id=" + themeId +
					 " and TemplateTheme.type=" + themeType;
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
	 * @return the templateService
	 */
	public TemplateService getTemplateService() {
		return templateService;
	}

	/**
	 * @param templateService the templateService to set
	 */
	public void setTemplateService(TemplateService templateService) {
		this.templateService = templateService;
	}
}