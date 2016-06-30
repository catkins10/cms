package com.yuanluesoft.onlinesignup.service.spring;

import com.yuanluesoft.cms.leadermail.pojo.LeaderMailType;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.onlinesignup.service.SignupService;

/**
 * 
 * @author zyh
 *
 */
public class SignupServiceImpl extends PublicServiceImpl implements SignupService {
	private boolean alwaysPublishAll = false; //发布时,是否总是公开信件的全部信息
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		if((record instanceof LeaderMailType) ) {
			getExchangeClient().synchUpdate(record, null, 2000);
			getPageService().rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		return super.save(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		if(record instanceof LeaderMailType) {
			getExchangeClient().synchUpdate(record, null, 2000);
			getPageService().rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		}
		return super.update(record);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.publicservice.service.spring.PublicServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		if(record instanceof LeaderMailType) {
			getExchangeClient().synchDelete(record, null, 2000);
			getPageService().rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
		}
		super.delete(record);
	}

	

	/**
	 * @return the alwaysPublishAll
	 */
	public boolean isAlwaysPublishAll() {
		return alwaysPublishAll;
	}

	/**
	 * @param alwaysPublishAll the alwaysPublishAll to set
	 */
	public void setAlwaysPublishAll(boolean alwaysPublishAll) {
		this.alwaysPublishAll = alwaysPublishAll;
	}
}